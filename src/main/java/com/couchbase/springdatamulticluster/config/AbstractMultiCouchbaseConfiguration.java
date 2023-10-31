package com.couchbase.springdatamulticluster.config;

import com.couchbase.client.core.env.Authenticator;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.convert.translation.TranslationService;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

@Slf4j
public abstract class AbstractMultiCouchbaseConfiguration extends AbstractCouchbaseConfiguration {
    public abstract String getSecondaryUsername();
    public abstract String getSecondaryPassword();
    public abstract String getSecondaryBucketName();

    protected String getSecondaryScope() { return null; }

    // override methods for the default user/bucket

    @Bean(
            name = {"couchbaseClientFactory"}
    )
    @Primary
    @Override // override method to add @Primary annotation and avoid beans clashing in private not qualified method couchbaseTransactionManager
    public CouchbaseClientFactory couchbaseClientFactory(final Cluster couchbaseCluster) {
        return new SimpleCouchbaseClientFactory(couchbaseCluster, this.getBucketName(), this.getScopeName());
    }

    @Bean(
            name = {"reactiveCouchbaseRepositoryOperationsMapping"}
    )
    @DependsOn("secondaryReactiveTemplate") // Added bean dependency to force this reactive couchbase template exists for mapping entities to buckets/clients
    @Override
    public ReactiveRepositoryOperationsMapping reactiveCouchbaseRepositoryOperationsMapping(ReactiveCouchbaseTemplate reactiveCouchbaseTemplate) {
        ReactiveRepositoryOperationsMapping baseMapping = new ReactiveRepositoryOperationsMapping(reactiveCouchbaseTemplate);
        this.configureReactiveRepositoryOperationsMapping(baseMapping);
        return baseMapping;
    }

    @Bean(
            name = {"couchbaseRepositoryOperationsMapping"}
    )
    @DependsOn("secondaryCouchbaseTemplate")
    @Override // Added the secondary CouchbaseTemplate dependency on it
    public RepositoryOperationsMapping couchbaseRepositoryOperationsMapping(CouchbaseTemplate couchbaseTemplate) {
        RepositoryOperationsMapping baseMapping = new RepositoryOperationsMapping(couchbaseTemplate);
        this.configureRepositoryOperationsMapping(baseMapping);
        return baseMapping;
    }

    // methods for secondary user/cluster

    protected Authenticator secondaryAuthenticator() {
        return PasswordAuthenticator.create(getSecondaryUsername(), getSecondaryPassword());
    }

    @Bean(
            destroyMethod = "shutdown"
    )
    @DependsOn("couchbaseClusterEnvironment")
    public ClusterEnvironment couchbaseSecondaryClusterEnvironment(ClusterEnvironment couchbaseClusterEnvironment) {
        ClusterEnvironment.Builder builder = ClusterEnvironment.builder();
        builder.jsonSerializer(couchbaseClusterEnvironment.jsonSerializer());
        builder.cryptoManager(couchbaseClusterEnvironment.cryptoManager().orElse(null));
        this.configureSecondaryEnvironment(builder);
        return builder.build();
    }

    protected void configureSecondaryEnvironment(final ClusterEnvironment.Builder builder) {

    }


    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseSecondaryCluster(@Qualifier("couchbaseSecondaryClusterEnvironment") ClusterEnvironment couchbaseSecondaryClusterEnvironment) {
        return Cluster.connect(this.getConnectionString(), ClusterOptions.clusterOptions(this.secondaryAuthenticator()).environment(couchbaseSecondaryClusterEnvironment));
    }

    @Bean
    public CouchbaseClientFactory secondaryClientFactory(@Qualifier("couchbaseSecondaryCluster")Cluster couchbaseSecondaryCluster) {
        return new SimpleCouchbaseClientFactory(couchbaseSecondaryCluster, getSecondaryBucketName(), getSecondaryScope());
    }

    @Bean
    public CouchbaseTemplate secondaryCouchbaseTemplate(@Qualifier("secondaryClientFactory") CouchbaseClientFactory secondaryClientFactory){
        return new CouchbaseTemplate(secondaryClientFactory, new MappingCouchbaseConverter());
    }

    @Bean
    public ReactiveCouchbaseTemplate secondaryReactiveTemplate(@Qualifier("secondaryClientFactory") CouchbaseClientFactory secondaryClientFactory, MappingCouchbaseConverter mappingCouchbaseConverter, TranslationService couchbaseTranslationService) {
        return new ReactiveCouchbaseTemplate(secondaryClientFactory, mappingCouchbaseConverter, couchbaseTranslationService, this.getDefaultConsistency());
    }


}
