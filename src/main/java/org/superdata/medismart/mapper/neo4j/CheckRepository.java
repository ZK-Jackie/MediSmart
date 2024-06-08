package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Check;

import java.util.List;

@FactoryRegister
@Repository("Check")
public interface CheckRepository extends NodeRepository<Check>, Neo4jRepository<Check, Long> {
    @Query("MATCH (n:Check) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Check> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Check) WHERE n.name = $keyword RETURN n")
    List<Check> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Check) WHERE id(n) = $id RETURN n")
    Check matchById(@Param("id") Long id);

    List<Check> searchCheckByNameContaining(String name);

    List<Check> searchByNameIs(String name);

    Check findCheckByIdIs(Long id);

    Check findByIdIs(Long id);
}
