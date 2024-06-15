package com.profitsoft.mailsender.services;

import java.time.Duration;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Testcontainers
public class BaseServiceTest extends ElasticsearchConfiguration {

    public static final String ELASTICSEARCH_DOCKER_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:8.14.0";

    @Bean(destroyMethod = "stop")
    public ElasticsearchContainer elasticsearchContainer() {
        ElasticsearchContainer container = new ElasticsearchContainer(
                ELASTICSEARCH_DOCKER_IMAGE);

        container.setEnv(List.of(
                "discovery.type=single-node",
                "ES_JAVA_OPTS=-Xms1g -Xmx1g",
                "xpack.security.enabled=false")
        );
        container.start();
        log.info("Elasticsearch container started at {}", container.getHttpHostAddress());
        return container;
    }

    @Bean
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchContainer().getHttpHostAddress())
                .build();
    }
}