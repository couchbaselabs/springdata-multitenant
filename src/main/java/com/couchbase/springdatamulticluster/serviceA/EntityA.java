package com.couchbase.springdatamulticluster.serviceA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
@Data
@TypeAlias("typeA")
public class EntityA implements Serializable {
    @Id
    String id;
    @Field
    String name;
    @Field
    String propertyA;
    @Field
    Long valueA;

}
