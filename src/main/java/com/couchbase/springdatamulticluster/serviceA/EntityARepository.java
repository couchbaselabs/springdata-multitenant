package com.couchbase.springdatamulticluster.serviceA;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityARepository extends CouchbaseRepository<EntityA, String> {
}
