package com.epis.utils;

import com.epis.entities.GsiDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(1)
public class DynamoDbInitializer implements CommandLineRunner {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbInitializer(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void run(String... args) {

        createTableIfNotExists("funcionario", "funcionarioId",
                List.of(new GsiDefinition("funcionario-nome-index", "nome"),
                        new GsiDefinition("funcionario-ativo-index", "cadastroAtivo")));

        createTableIfNotExists("funcao", "funcaoId",
                List.of(new GsiDefinition("funcao-nome-index", "nome")));

        createTableIfNotExists("epi", "epiId",
                List.of(new GsiDefinition("epi-ativo-index", "cadastroAtivo")));

        createTableIfNotExists("movimentacao", "movimentacaoId",
                List.of(new GsiDefinition("movimentacao-ativo-index", "cadastroAtivo")));

        createTableIfNotExists("usuario", "usuarioId",
                List.of(new GsiDefinition("username-index", "username")));

        createTableIfNotExists("log", "logId", null);
        createTableIfNotExists("uniforme", "uniformeId", null);

    }

    private void createTableIfNotExists(
            String tableName,
            String partitionKey,
            List<GsiDefinition> gsis
    ) {

        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());

            System.out.println("Tabela " + tableName + " já existe.");
            return;

        } catch (ResourceNotFoundException ignored) {
            System.out.println("Criando tabela: " + tableName);
        }

        CreateTableRequest.Builder builder = CreateTableRequest.builder()
                .tableName(tableName)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .keySchema(KeySchemaElement.builder()
                        .attributeName(partitionKey)
                        .keyType(KeyType.HASH)
                        .build());

        Set<AttributeDefinition> attributeDefinitions = new HashSet<>();

        attributeDefinitions.add(
                AttributeDefinition.builder()
                        .attributeName(partitionKey)
                        .attributeType(ScalarAttributeType.S)
                        .build()
        );

        if (gsis != null && !gsis.isEmpty()) {

            List<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<>();

            for (GsiDefinition gsi : gsis) {

                attributeDefinitions.add(
                        AttributeDefinition.builder()
                                .attributeName(gsi.getPartitionKey())
                                .attributeType(ScalarAttributeType.S)
                                .build()
                );

                globalSecondaryIndexes.add(
                        GlobalSecondaryIndex.builder()
                                .indexName(gsi.getIndexName())
                                .keySchema(KeySchemaElement.builder()
                                        .attributeName(gsi.getPartitionKey())
                                        .keyType(KeyType.HASH)
                                        .build())
                                .projection(Projection.builder()
                                        .projectionType(ProjectionType.ALL)
                                        .build())
                                .build()
                );
            }

            builder.globalSecondaryIndexes(globalSecondaryIndexes);
        }

        builder.attributeDefinitions(attributeDefinitions);

        dynamoDbClient.createTable(builder.build());
    }

}
