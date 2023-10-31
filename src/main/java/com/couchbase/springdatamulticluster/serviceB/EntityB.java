package com.couchbase.springdatamulticluster.serviceB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
@Data
@TypeAlias("typeB")
public class EntityB implements Serializable {
    @Id
    String id;
    String name;
    String propertyB;
}
