package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Check;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;

public interface CheckRepository extends NodeRepository<Check>, Neo4jRepository<Check, Long> {
    @Query("MATCH (n:Check) WHERE n.name CONTAINS $keyword RETURN n")
    List<Check> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Check) WHERE n.name = $keyword RETURN n")
    List<Check> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Check) WHERE id(n) = $id RETURN n")
    Check matchById(@Param("id") Long id);
}
