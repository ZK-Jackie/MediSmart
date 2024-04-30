package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Disease;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;


public interface DiseaseRepository extends NodeRepository<Disease>, Neo4jRepository<Disease, Long> {
    @Query("MATCH (n:Disease) WHERE n.name CONTAINS $keyword RETURN n")
    List<Disease> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Disease) WHERE n.name = $keyword RETURN n")
    List<Disease> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Disease) WHERE id(n) = $id RETURN n")
    Disease matchById(@Param("id") Long id);
}
