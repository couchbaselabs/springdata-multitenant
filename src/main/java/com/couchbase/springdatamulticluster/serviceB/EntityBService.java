package com.couchbase.springdatamulticluster.serviceB;

import org.springframework.stereotype.Service;

@Service
public class EntityBService {
    private final EntityBRepository repository;

    public EntityBService(EntityBRepository repository) {
        this.repository = repository;
    }

    public void save(EntityB entity){
        repository.save(entity);
    }

    public EntityB findById(String id) {
        return repository.findById(id).orElse(null);
    }
}
