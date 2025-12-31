package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Drug;
import org.superdata.medismart.entity.node.Producer;

import java.util.List;

@FactoryRegister
@Repository("Producer")
public interface ProducerRepository extends NodeRepository<Producer>, Neo4jRepository<Producer, Long> {
    @Query("MATCH (n:Producer) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Producer> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Producer) WHERE n.name = $keyword RETURN n")
    List<Producer> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Producer) WHERE id(n) = $id RETURN n")
    Producer matchById(@Param("id") Long id);

    Producer findByIdIs(Long id);
}
