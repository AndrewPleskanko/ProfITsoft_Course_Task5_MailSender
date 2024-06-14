package com.profitsoft.mailsender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * Configuration class for Elasticsearch.
 * This class extends the ElasticsearchConfiguration class and overrides the clientConfiguration method.
 * The Elasticsearch address is injected from the application properties file.
 */
@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value(value = "${elasticsearch.address}")
    private String esAddress;

    /**
     * Configures the Elasticsearch client.
     * The client is set to connect to the Elasticsearch address defined in the application properties file.
     *
     * @return a ClientConfiguration instance with the Elasticsearch address set
     */
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(esAddress)
                .build();
    }
}
