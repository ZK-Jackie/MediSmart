package org.superdata.medismart.service;

import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.entity.GraphNode;
import org.superdata.medismart.entity.node.Disease;

import java.util.List;
import java.util.Map;

public interface GraphService{

    Map<String, List<Object>> fuzzyQuery(String keyword, Integer pageNow, Integer pageSize);

    Map<String, List<Object>> preciseQuery(String keyword, Integer pageNow, Integer pageSize);

    Map<String, List<Object>> linkQuery(Long id);

    Boolean addNodeByFile(GraphNode node);

    Boolean addNodeByFile(MultipartFile file);

    Boolean saveNode(GraphNode node);

    Boolean deleteNode(Long id);

    Boolean saveNode(Disease node, String nodeType);
}