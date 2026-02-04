package com.epis.entities;

public class GsiDefinition {

    private final String indexName;
    private final String partitionKey;

    public GsiDefinition(String indexName, String partitionKey) {
        this.indexName = indexName;
        this.partitionKey = partitionKey;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getPartitionKey() {
        return partitionKey;
    }
}
