package com.couchbase.springdatamulticluster.serviceB;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityBRepository extends CouchbaseRepository<EntityB, String> {
}
