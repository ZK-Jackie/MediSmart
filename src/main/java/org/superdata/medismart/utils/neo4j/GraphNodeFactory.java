package org.superdata.medismart.utils.neo4j;

import org.superdata.medismart.common.constant.Constants;
import org.superdata.medismart.entity.GraphNode;
import org.superdata.medismart.entity.node.*;
import org.superdata.medismart.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GraphNodeFactory {

    private static GraphNodeFactory instance;
    private final Map<String, Supplier<GraphNode>> map;

    private GraphNodeFactory() {
        map = new HashMap<>();
        map.put("Check", Check::new);
        map.put("Department", Department::new);
        map.put("Disease", Disease::new);
        map.put("Drug", Drug::new);
        map.put("Food", Food::new);
        map.put("Producer", Producer::new);
        map.put("Symptom", Symptom::new);
    }

    public static synchronized GraphNodeFactory me() {
        if (instance == null) {
            instance = new GraphNodeFactory();
        }
        return instance;
    }

    public Object getGraphNode(String nodeType) {
        Supplier<GraphNode> node = map.get(nodeType);
        if (node != null) {
            return node.get();
        }
        throw new IllegalArgumentException("Invalid node type: " + nodeType);
    }

    public Object getGraphNode(String nodeType, Object nodeInfo) {
        try{
            return ObjectUtils.cast(Constants.NODE_ENTITY_PACKAGE + "." + nodeType, nodeInfo);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid node");
        }
    }
}