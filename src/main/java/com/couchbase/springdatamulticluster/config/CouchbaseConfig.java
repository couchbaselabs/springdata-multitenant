package com.couchbase.springdatamulticluster.config;

import com.couchbase.springdatamulticluster.serviceB.EntityB;
import com.couchbase.springdatamulticluster.serviceB.EntityBRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

@Slf4j
//@EnableCouchbaseRepositories
@EnableConfigurationProperties(CouchbaseProperties.class)
@Configuration
public class CouchbaseConfig extends AbstractMultiCouchbaseConfiguration {
    private final CouchbaseProperties properties;
    private final ApplicationContext context;

    public CouchbaseConfig(CouchbaseProperties properties, ApplicationContext context) {
        this.properties = properties;
        this.context = context;
    }

    @Override
    public String getConnectionString() {
        return properties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return properties.getPrimary().getUsername();
    }

    @Override
    public String getPassword() {
        return properties.getPrimary().getPassword();
    }

    @Override
    public String getBucketName() {
        return properties.getPrimary().getName();
    }

    @Override
    public String getSecondaryUsername() {
        return properties.getSecondary().getUsername();
    }

    @Override
    public String getSecondaryPassword() {
        return properties.getSecondary().getPassword();
    }

    @Override
    public String getSecondaryBucketName() {
        return properties.getSecondary().getName();
    }

    // methods for mapping both clusters/users : each entity for their own user/bucket/template/cluster
    @Override
    public void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            CouchbaseTemplate secondaryCouchbaseTemplate = context.getBean("secondaryCouchbaseTemplate", CouchbaseTemplate.class);
            baseMapping.mapEntity(EntityB.class, secondaryCouchbaseTemplate);
            baseMapping.map(EntityBRepository.class, secondaryCouchbaseTemplate);
        } catch (Exception e) {
            log.error("{} exception mapping entities to secondary CouchbaseTemplate. ", e.getClass().getSimpleName(), e);
            throw e;
        }
    }

    @Override
    public void configureReactiveRepositoryOperationsMapping(ReactiveRepositoryOperationsMapping baseMapping){
        try {
            ReactiveCouchbaseTemplate secondaryReactiveTemplate = context.getBean("secondaryReactiveTemplate", ReactiveCouchbaseTemplate.class);
            baseMapping.mapEntity(EntityB.class, secondaryReactiveTemplate);
            baseMapping.map(EntityBRepository.class, secondaryReactiveTemplate);
        } catch (Exception e) {
            log.error("{} exception mapping reactive repository.", e.getClass().getSimpleName(), e);
            throw e;
        }
    }
}
