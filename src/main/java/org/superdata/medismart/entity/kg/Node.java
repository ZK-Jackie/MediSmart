package org.superdata.medismart.entity.kg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    @Id
    @GeneratedValue
    public Long id;

    @Property("name")
    public String name;
}
