package org.superdata.medismart.entity.node;

import lombok.Getter;
import org.superdata.medismart.entity.GraphNode;

@Getter
public class Food extends GraphNode {
    public Food(Long id, String name) {
        super(id, name, "Food");
    }

    public Food(String name) {
        super(name, "Food");
    }

    public Food() {
        super(null, "Food");
    }
}
