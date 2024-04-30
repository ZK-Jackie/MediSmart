package org.superdata.medismart.kgRepo;


import org.superdata.medismart.entity.kg.Node;

import java.util.List;

/**
 * @Author: Jackie
 * @Time: 2024-04-22 20:42
 * @Description: Linking all the node repositories and offering the basic query operations
 * */
public interface NodeRepository<T extends Node>{

    List<T> fuzzyMatchByName(String keyword);

    List<T> preciseMatchByName(String keyword);

    T matchById(Long id);

}
