package org.superdata.medismart.service;

import java.util.List;
import java.util.Map;

public interface KgService {

    Map<String, List<Object>> fuzzyQuery(String keyword, Integer pageNow, Integer pageSize);

    Map<String, List<Object>> preciseQuery(String keyword, Integer pageNow, Integer pageSize);

    Map<String, List<Object>> linkQuery(Long id);
}