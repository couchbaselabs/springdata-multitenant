package com.couchbase.springdatamulticluster.config;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "spring.couchbase")
public class CouchbaseProperties {
        @Value("${spring.couchbase.connection-string:localhost}")
        private String connectionString;
        @NestedConfigurationProperty
        private CouchbaseBucketProperties primary;
        @NestedConfigurationProperty
        private CouchbaseBucketProperties secondary;

        @Data
        public static class CouchbaseBucketProperties {
            String name;
            String username;
            String password;
            String scope;
        }
}
