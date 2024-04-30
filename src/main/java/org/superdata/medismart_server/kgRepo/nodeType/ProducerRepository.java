package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Producer;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;


public interface ProducerRepository extends NodeRepository<Producer>, Neo4jRepository<Producer, Long> {
    @Query("MATCH (n:Producer) WHERE n.name CONTAINS $keyword RETURN n")
    List<Producer> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Producer) WHERE n.name = $keyword RETURN n")
    List<Producer> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Producer) WHERE id(n) = $id RETURN n")
    Producer matchById(@Param("id") Long id);
}
