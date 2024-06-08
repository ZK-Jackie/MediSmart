package org.superdata.medismart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;


@Data
@AllArgsConstructor
public class GraphNode {
    @Id
    @GeneratedValue
    public Long id;

    @Property("name")
    public String name;

    public final String category;

    protected GraphNode(String name, String category) {
        this.id = null;
        this.name = name;
        this.category = category;
    }

    public void setCategory(String category) {
        throw new UnsupportedOperationException("Category is final and cannot be changed");
    }
}
