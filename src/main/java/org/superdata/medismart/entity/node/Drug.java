package org.superdata.medismart.entity.node;

import lombok.Getter;
import org.superdata.medismart.entity.GraphNode;

@Getter
public class Drug extends GraphNode {
    public Drug(Long id, String name) {
        super(id, name, "Drug");
    }

    public Drug(String name) {
        super(name, "Drug");
    }

    public Drug() {
        super(null, "Drug");
    }
}
