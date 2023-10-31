package com.couchbase.springdatamulticluster.serviceA;

import com.couchbase.springdatamulticluster.serviceB.EntityB;
import com.couchbase.springdatamulticluster.serviceB.EntityBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/userA")
public class EntityAController {
    private final EntityAService service;

    public EntityAController(EntityAService service) {
        this.service = service;
    }

    @PostMapping("/entityA")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void upsertEntityA(@RequestBody EntityA entityA) {
        log.info("upserting entity {}", entityA);
        service.save(entityA);
    }

    @GetMapping("/entity/{id}")
    public EntityA getEntityB(@PathVariable(value = "id", required = true) String id) {
        log.info("get entyty {}", id);
        return service.findById(id);
    }

}
