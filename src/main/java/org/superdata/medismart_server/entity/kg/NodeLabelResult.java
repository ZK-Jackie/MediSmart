package org.superdata.medismart.entity.kg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeLabelResult {
    @Property("labels")
    public List<String> labels;

    @Property("node")
    public Node node;
}
