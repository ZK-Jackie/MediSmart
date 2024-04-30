package org.superdata.medismart.entity.kg.nodeType;

import lombok.Getter;
import org.superdata.medismart.entity.kg.Node;

@Getter
public class Symptom extends Node {
    private final String category = "症状";
}
