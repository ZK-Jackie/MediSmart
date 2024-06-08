package org.superdata.medismart.entity.node;

import lombok.Getter;
import org.superdata.medismart.entity.GraphNode;

@Getter
public class Check extends GraphNode {
    public Check(Long id, String name) {
        super(id, name, "Check");
    }

    public Check(String name) {
        super(name, "Check");
    }

    public Check() {
        super(null, "Check");
    }
}
