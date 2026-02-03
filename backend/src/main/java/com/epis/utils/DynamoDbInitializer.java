package com.epis.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
@Order(1)
public class DynamoDbInitializer implements CommandLineRunner {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbInitializer(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void run(String... args) {
        createTableIfNotExists("funcionario", "funcionarioId", "funcionario-nome-index", "nome");
        createTableIfNotExists("funcao", "funcaoId", "funcao-nome-index", "nome");
        createTableIfNotExists("epi", "epiId", null, null);
        createTableIfNotExists("uniforme", "uniformeId", null, null);
        createTableIfNotExists("movimentacao", "movimentacaoId", null, null);
        createTableIfNotExists("usuario", "usuarioId", "username-index", "username");
        createTableIfNotExists("log", "logId", null, null);
    }

    private void createTableIfNotExists(
            String tableName,
            String partitionKey,
            String gsiName,
            String gsiPartitionKey
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

        builder.attributeDefinitions(
                AttributeDefinition.builder()
                        .attributeName(partitionKey)
                        .attributeType(ScalarAttributeType.S)
                        .build()
        );

        if (gsiName != null && gsiPartitionKey != null) {

            builder.attributeDefinitions(
                    AttributeDefinition.builder()
                            .attributeName(partitionKey)
                            .attributeType(ScalarAttributeType.S)
                            .build(),
                    AttributeDefinition.builder()
                            .attributeName(gsiPartitionKey)
                            .attributeType(ScalarAttributeType.S)
                            .build()
            );

            builder.globalSecondaryIndexes(
                    GlobalSecondaryIndex.builder()
                            .indexName(gsiName)
                            .keySchema(KeySchemaElement.builder()
                                    .attributeName(gsiPartitionKey)
                                    .keyType(KeyType.HASH)
                                    .build())
                            .projection(Projection.builder()
                                    .projectionType(ProjectionType.ALL)
                                    .build())
                            .build()
            );
        }

        dynamoDbClient.createTable(builder.build());
    }


}
