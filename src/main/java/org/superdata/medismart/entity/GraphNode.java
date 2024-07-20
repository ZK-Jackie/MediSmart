package org.superdata.medismart.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphNode {
    @Id
    @GeneratedValue
    public Long id;

    @Property("uid")
    public String uid;

    @Property("name")
    public String name;

    @Property("category")
    public String category;

}
