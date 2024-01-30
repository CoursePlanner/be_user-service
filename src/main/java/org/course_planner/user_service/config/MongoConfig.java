package org.course_planner.user_service.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Data
@EnableMongoRepositories(basePackages = "org.course_planner.user_service")
public class MongoConfig extends AbstractMongoClientConfiguration {
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private String authenticationDatabase;
    private boolean autoIndexCreation;

    @Override
    public MongoClient createMongoClient(MongoClientSettings settings) {
        String connectionString = "mongodb://" + username + ":" + password + "@" +
                host + ":" + port + "/" + database + "?authSource=admin";
        return MongoClients.create(connectionString);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}
