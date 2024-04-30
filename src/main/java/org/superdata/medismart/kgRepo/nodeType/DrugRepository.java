package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Drug;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;


public interface DrugRepository extends NodeRepository<Drug>, Neo4jRepository<Drug, Long> {
    @Query("MATCH (n:Drug) WHERE n.name CONTAINS $keyword RETURN n")
    List<Drug> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Drug) WHERE n.name = $keyword RETURN n")
    List<Drug> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Drug) WHERE id(n) = $id RETURN n")
    Drug matchById(@Param("id") Long id);
}
