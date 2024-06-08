package org.superdata.medismart.entity.node;

import lombok.Getter;
import org.superdata.medismart.entity.GraphNode;

@Getter
public class Producer extends GraphNode {
    public Producer(Long id, String name) {
        super(id, name, "Producer");
    }

    public Producer(String name) {
        super(name, "Producer");
    }

    public Producer() {
        super(null, "Producer");
    }
}
