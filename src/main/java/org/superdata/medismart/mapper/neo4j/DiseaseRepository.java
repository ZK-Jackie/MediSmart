package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Department;
import org.superdata.medismart.entity.node.Disease;

import java.util.List;

@FactoryRegister
@Repository("Disease")
public interface DiseaseRepository extends NodeRepository<Disease>, Neo4jRepository<Disease, Long> {
    @Query("MATCH (n:Disease) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Disease> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Disease) WHERE n.name = $keyword RETURN n")
    List<Disease> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Disease) WHERE id(n) = $id RETURN n")
    Disease matchById(@Param("id") Long id);

    Disease findByIdIs(Long id);
}
