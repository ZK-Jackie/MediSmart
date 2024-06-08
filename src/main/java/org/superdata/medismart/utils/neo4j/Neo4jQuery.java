package org.superdata.medismart.utils.neo4j;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class Neo4jQuery {
    @Resource
    private Neo4jClient neo4jClient;


    /**
     * @Time: 2024-04-22 20:54
     * @Description: Execute the cypher query and return the result
     * @Example:
     * cypherQuery = "MATCH (n:Person {name: {name}}) RETURN n";<br/>
     * Map&lt;String, Object&gt; parameters = new HashMap&lt;&gt;();<br/>
     * parameters.put("name", "Alice");<br/>
     * List&lt;Map&lt;String, Object&gt;&gt; result = neo4jQueryUtils.executeCypherQuery(cypherQuery, parameters, Map. Class);
     * */
    public <T> List<T> executeCypherQuery(String cypherQuery, Map<String, Object> parameters, Class<T> returnType) {
        return (List<T>) neo4jClient.query(cypherQuery).bindAll(parameters).fetchAs(returnType).all();
    }
}