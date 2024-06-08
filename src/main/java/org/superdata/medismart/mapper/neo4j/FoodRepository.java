package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Drug;
import org.superdata.medismart.entity.node.Food;

import java.util.List;

@FactoryRegister
@Repository("Food")
public interface FoodRepository extends NodeRepository<Food>, Neo4jRepository<Food, Long> {
    @Query("MATCH (n:Food) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Food> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Food) WHERE n.name = $keyword RETURN n")
    List<Food> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Food) WHERE id(n) = $id RETURN n")
    Food matchById(@Param("id") Long id);

    Food findByIdIs(Long id);
}
