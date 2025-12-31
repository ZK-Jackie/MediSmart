package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Department;
import org.superdata.medismart.entity.node.Drug;

import java.util.List;

@FactoryRegister
@Repository("Drug")
public interface DrugRepository extends NodeRepository<Drug>, Neo4jRepository<Drug, Long> {
    @Query("MATCH (n:Drug) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Drug> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Drug) WHERE n.name = $keyword RETURN n")
    List<Drug> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Drug) WHERE id(n) = $id RETURN n")
    Drug matchById(@Param("id") Long id);

    Drug findByIdIs(Long id);
}
