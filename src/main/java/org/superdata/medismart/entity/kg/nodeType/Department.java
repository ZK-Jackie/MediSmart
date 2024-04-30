package org.superdata.medismart.entity.kg.nodeType;

import lombok.Getter;
import org.superdata.medismart.entity.kg.Node;

@Getter
public class Department extends Node {
    private final String category = "科室";
}
