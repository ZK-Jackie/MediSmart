package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Symptom;

import java.util.List;

@FactoryRegister
@Repository("Symptom")
public interface SymptomRepository extends NodeRepository<Symptom>, Neo4jRepository<Symptom, Long> {
    @Query("MATCH (n:Symptom) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Symptom> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Symptom) WHERE n.name = $keyword RETURN n")
    List<Symptom> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Symptom) WHERE id(n) = $id RETURN n")
    Symptom matchById(@Param("id") Long id);

    Symptom findByIdIs(Long id);
}
