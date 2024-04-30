package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Food;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;


public interface FoodRepository extends NodeRepository<Food>, Neo4jRepository<Food, Long> {
    @Query("MATCH (n:Food) WHERE n.name CONTAINS $keyword RETURN n")
    List<Food> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Food) WHERE n.name = $keyword RETURN n")
    List<Food> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Food) WHERE id(n) = $id RETURN n")
    Food matchById(@Param("id") Long id);
}
