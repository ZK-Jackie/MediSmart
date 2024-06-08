package org.superdata.medismart.utils.neo4j;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class Neo4jQueryTemplate {
    @Resource
    private Neo4jClient neo4jClient;

    public static String fuzzyQuery = "MATCH (n:%s) WHERE n.name CONTAINS '%s' RETURN n";
    public static String preciseQuery = "MATCH (n:%s) WHERE n.name = '%s' RETURN n";
    public static String linkQuery = "MATCH (n:%s) WHERE id(n) = %ld RETURN n";
    public static String addNode = "CREATE (n:%s {name: '%s'}) RETURN n";
    public static String addRelation = "MATCH (a:%s),(b:%s) WHERE id(a) = %ld AND id(b) = %ld CREATE (a)-[r:%s]->(b) RETURN r";
    public static String deleteNode = "MATCH (n:%s) WHERE id(n) = %ld DELETE n";
    public static String deleteRelation = "MATCH (a:%s)-[r:%s]->(b:%s) WHERE id(a) = %ld AND id(b) = %ld DELETE r";
    public static String updateNode = "MATCH (n:%s) WHERE id(n) = %ld SET n.name = '%s' RETURN n";
    public static String updateRelation = "MATCH (a:%s)-[r:%s]->(b:%s) WHERE id(a) = %ld AND id(b) = %ld SET r.name = '%s' RETURN r";
    public static String matchRelation = "MATCH (a:%s)-[r:%s]->(b:%s) WHERE id(a) = %ld AND id(b) = %ld RETURN r";
    public static String matchNode = "MATCH (n:%s) WHERE id(n) = %ld RETURN n";
    public static String matchAll = "MATCH (n:%s) RETURN n";
    public static String matchAllRelation = "MATCH (a:%s)-[r:%s]->(b:%s) RETURN r";
    public static String matchAllRelationByNode = "MATCH (a:%s)-[r:%s]->(b:%s) WHERE id(a) = %ld RETURN r";
    public static String matchAllRelationByRelation = "MATCH (a:%s)-[r:%s]->(b:%s) WHERE id(r) = %ld RETURN r";
    public static String matchAllRelationByNodeAndRelation = "MATCH (a:%s)-[r:%s]->(b:%s) WHERE id(a) = %ld AND id(r) = %ld RETURN r";

    public static String format(String query, Object... label) {
        return String.format(query, label);
    }
}
