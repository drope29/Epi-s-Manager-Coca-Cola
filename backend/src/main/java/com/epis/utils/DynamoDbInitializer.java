package com.epis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
public class DynamoDbInitializer implements CommandLineRunner {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbInitializer(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void run(String... args) {
        createTableIfNotExists("funcionario", "funcionarioId");
        createTableIfNotExists("funcao", "funcaoId");
        createTableIfNotExists("epi", "epiId");
        createTableIfNotExists("uniforme", "uniformeId");
        createTableIfNotExists("movimentacao", "movimentacaoId");
    }

    private void createTableIfNotExists(String tableName, String partitionKey) {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());
            System.out.println("Tabela " + tableName + " já existe.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Criando tabela: " + tableName);
            dynamoDbClient.createTable(CreateTableRequest.builder()
                    .tableName(tableName)
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName(partitionKey)
                            .attributeType(ScalarAttributeType.S)
                            .build())
                    .keySchema(KeySchemaElement.builder()
                            .attributeName(partitionKey)
                            .keyType(KeyType.HASH)
                            .build())
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build());
        }
    }

}
