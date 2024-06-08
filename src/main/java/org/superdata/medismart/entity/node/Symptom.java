package org.superdata.medismart.entity.node;

import lombok.Getter;
import org.superdata.medismart.entity.GraphNode;

@Getter
public class Symptom extends GraphNode {
    public Symptom(Long id, String name) {
        super(id, name, "Symptom");
    }

    public Symptom(String name) {
        super(name, "Symptom");
    }

    public Symptom() {
        super(null, "Symptom");
    }
}
