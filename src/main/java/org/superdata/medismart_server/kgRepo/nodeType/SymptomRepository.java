package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Symptom;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;


public interface SymptomRepository extends NodeRepository<Symptom>, Neo4jRepository<Symptom, Long> {
    @Query("MATCH (n:Symptom) WHERE n.name CONTAINS $keyword RETURN n")
    List<Symptom> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Symptom) WHERE n.name = $keyword RETURN n")
    List<Symptom> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Symptom) WHERE id(n) = $id RETURN n")
    Symptom matchById(@Param("id") Long id);
}
