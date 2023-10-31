package com.couchbase.springdatamulticluster.serviceB;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/userB")
public class EntityBController {
    private final EntityBService service;

    public EntityBController(EntityBService service) {
        this.service = service;
    }


    @PostMapping(path = "/entityB")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void upsertEntityB(@RequestBody EntityB entityB) {
        log.info("upserting entity {}", entityB);
        service.save(entityB);
    }

    @GetMapping(path = "/entityB/{id}")
    public EntityB getEntityB(@PathVariable("id") String id) {
        return service.findById(id);
    }


}
