package com.couchbase.springdatamulticluster.serviceA;

import com.couchbase.springdatamulticluster.serviceB.EntityB;
import org.springframework.stereotype.Service;

@Service
public class EntityAService {

    private final EntityARepository repository;

    public EntityAService(EntityARepository repository) {
        this.repository = repository;
    }

    public void save(EntityA entity) {
        repository.save(entity);
    }

    public EntityA findById(String id) {
        return repository.findById(id).orElse(null);
    }
}
