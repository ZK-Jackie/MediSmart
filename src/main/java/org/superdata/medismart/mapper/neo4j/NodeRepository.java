package org.superdata.medismart.mapper.neo4j;


import org.superdata.medismart.entity.GraphNode;

import java.util.List;

/**
 * @Author: Jackie
 * @Time: 2024-04-22 20:42
 * @Description: Linking all the node repositories and offering the basic query operations
 * */
public interface NodeRepository<T extends GraphNode>{

    List<T> fuzzyMatchByName(String keyword);

    List<T> preciseMatchByName(String keyword);

    T matchById(Long id);
}
