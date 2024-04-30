package org.superdata.medismart.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.superdata.medismart.entity.kg.Relation;
import org.superdata.medismart.kgRepo.*;
import org.superdata.medismart.service.KgService;
import org.superdata.medismart.utils.Neo4jQueryUtils;
import org.superdata.medismart.utils.StringUtil;


import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class KgServiceImpl implements KgService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Neo4jQueryUtils neo4jQueryUtils;

    private Map<String, NodeRepository> nodeRepositories;

    @PostConstruct
    public void init() {
        nodeRepositories = applicationContext.getBeansOfType(NodeRepository.class);
        log.info("Neo4j node repositories: {} are ready", nodeRepositories.keySet());
    }

    /**
     * @Description:
     * Fuzzy query, need a key word, return a map that consists of label name and list of nodes
     * @param keyword the key word that user input to search
     * @return Map > key: label name > value: list of nodes
     */
    @Override
    public Map<String, List<Object>> fuzzyQuery(String keyword, Integer pageNow, Integer pageSize) {
        Map<String, List<Object>> queryResult = fuzzyMatchByName(keyword);
        // TODO 分页模糊查询
//        //1.设置分页参数
//        PageHelper.startPage(pageNow, pageSize);
//        //2.将查询结果交给Page泛型
//        List<Emp> data = empMapper.getEmp(empPageInfo);
//        //3.包装数据
//        empPageInfo.setTableData(((Page<Emp>) data).getResult());
//        empPageInfo.setTotalSize(((Page<Emp>) data).getTotal());
//        //4.返回controller
//        return empPageInfo;
        return queryResult;
    }

    /**
     * @description:
     * Precise query, need a key word, return the list of objects. In general, the list size is 1
     * @param keyword the key word that user input to search
     * @return Map > key: label name > value: list of nodes
     */
    @Override
    public Map<String, List<Object>> preciseQuery(String keyword, Integer pageNow, Integer pageSize) {
        return preciseMatchByName(keyword);
    }

    /**
     * @description:
     * Precise query, need a key word, return the list of objects. In general, the list size is 1
     * @param id the id of the node
     * @return Map > key: name of edge > value: list of nodes
     */
    @Override
    public Map<String, List<Object>> linkQuery(Long id) {
        Relation[] relationEnum = Relation.values();
        // 1. Find the relations of the node
        String relationCypher = "MATCH (n)-[r]->() WHERE ID(n) = $id RETURN DISTINCT TYPE(r)";
        List<String> relations = neo4jQueryUtils.executeCypherQuery(relationCypher, Collections.singletonMap("id", id), String.class);
        // 2. Find the nodes ids of the relations
        Map<String, List<Object>> returnMap = new HashMap<>();
        relations.forEach(relation -> {
            String nodeCypher = "MATCH (n)-[r]->(m) WHERE ID(n) = $id AND TYPE(r) = $relation RETURN ID(m)";
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("relation", relation);
            List<Integer> nodeIds = neo4jQueryUtils.executeCypherQuery(nodeCypher, params, Integer.class);
            // 3. Find the nodes of the ids
            List<Object> nodes = new ArrayList<>();
            nodeIds.forEach(nodeId -> {
                nodes.add(matchById(nodeId));
                returnMap.put(relation, nodes);
            });
        });
        // 4. Return the result
        return returnMap;
    }

    private <T> Map<String, List<T>> fuzzyMatchByName(String keyword) {
        Map<String, List<T>> returnMap = new HashMap<>();
        List<Object> retList = new ArrayList<>();
        nodeRepositories.forEach((k, v) ->{
            String labelName = StringUtil.capitalizeFirstLetter(k.substring(0, k.length() - 10));
            List<T> results = v.fuzzyMatchByName(keyword);
            if(!results.isEmpty())
                returnMap.put(labelName, results);
        });
        return returnMap;
    }

    private <T> Map<String, List<T>> preciseMatchByName(String keyword) {
        Map<String, List<T>> returnMap = new HashMap<>();
        nodeRepositories.forEach((k, v) ->{
            String labelName = StringUtil.capitalizeFirstLetter(k.substring(0, k.length() - 10));
            List<T> results = v.preciseMatchByName(keyword);
            if(!results.isEmpty())
                returnMap.put(labelName, results);
        });
        return returnMap;
    }

    private Object matchById(Integer id) {
        AtomicBoolean breakSignal = new AtomicBoolean(false);
        AtomicReference<Object> result = new AtomicReference<>();
        nodeRepositories.forEach((k, v) ->{
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
