package org.superdata.medismart.mapper;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Component;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.mapper.neo4j.NodeRepository;
import org.superdata.medismart.utils.neo4j.Neo4jQuery;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class NodeRepoFactory{
    /**
     * Bean名称及对应的NodeRepository对象
     * keys: Bean名称，一般是label名称
     * values: 对应的NodeRepository仓库
     * */
    public static Map<String, NodeRepository> map = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private Neo4jQuery neo4jQuery;

    @PostConstruct
    public void init() {
        // 获取所有带有FactoryRegister注解的Bean，并将其放入map中，方便调用
        Map<String, Object> temp = applicationContext.getBeansWithAnnotation(FactoryRegister.class);
        temp.forEach((k, v) -> map.put(k, (NodeRepository) v));
    }

    public static Neo4jRepository getRepository(String label) {
        return (Neo4jRepository)map.get(label);
    }

    public static Neo4jRepository getRepository(){
        return (Neo4jRepository)map.values().iterator().next();
    }

    public <T> Map<String, List<T>> fuzzyMatchByName(String keyword) {
        Map<String, List<T>> returnMap = new HashMap<>();
        map.forEach((k, v) ->{
            List<T> results = v.fuzzyMatchByName(keyword);
            if(!results.isEmpty())
                returnMap.put(k, results);
        });
        return returnMap;
    }

    public <T> Map<String, List<T>> preciseMatchByName(String keyword) {
        Map<String, List<T>> returnMap = new HashMap<>();
        map.forEach((k, v) ->{
            List<T> results = v.preciseMatchByName(keyword);
            if(!results.isEmpty())
                returnMap.put(k, results);
        });
        return returnMap;
    }

    public Map<String, List<Object>> detailMatchById(Long id) {
        // 1. 查找当前节点有哪些关系边
        String relationCypher = "MATCH (n)-[r]-() WHERE ID(n) = $id RETURN DISTINCT TYPE(r)";
        List<String> relations = neo4jQuery.executeCypherQuery(relationCypher, Collections.singletonMap("id", id), String.class);
        // 2. 查找每种关系边有哪些id与当前节点相连，并放入一个map中
        Map<String, List<Object>> returnMap = new HashMap<>();
        relations.forEach(relation -> {
            String nodeCypher = "MATCH (n)-[r]-(m) WHERE ID(n) = $id AND TYPE(r) = $relation RETURN ID(m)";
            // 先把当前点放入
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("relation", relation);
            // 执行查找
            List<Integer> nodeIds = neo4jQuery.executeCypherQuery(nodeCypher, params, Integer.class);
            // 3. 查询所有id的详细信息，作为关系的其中一个key
            List<Object> nodes = new ArrayList<>();
            nodeIds.forEach(nodeId -> nodes.add(matchById(nodeId)));
            returnMap.put(relation, nodes);
        });
        // 4. Return the result
        return returnMap;
    }

    private Object matchById(Integer id) {
        AtomicBoolean breakSignal = new AtomicBoolean(false);
        AtomicReference<Object> result = new AtomicReference<>();
        map.forEach((k, v) ->{
            if(breakSignal.get())
                return;
            Object node = v.matchById(id.longValue());
            if(node != null){
                result.set(node);
                breakSignal.set(true);
            }
        });
        try{
            return result.get();
        }catch (NullPointerException e){
            log.error("No such node with id: {}", id);
            return null;
        }
    }
}

