package org.superdata.medismart.entity.node;

import lombok.Getter;
import org.superdata.medismart.entity.GraphNode;

@Getter
public class Department extends GraphNode {
    public Department(Long id, String name) {
        super(id, name, "Department");
    }

    public Department(String name) {
        super(name, "Department");
    }

    public Department( ) {
        super(null, "Department");
    }
}
