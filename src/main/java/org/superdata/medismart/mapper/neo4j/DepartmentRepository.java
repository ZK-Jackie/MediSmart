package org.superdata.medismart.mapper.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.superdata.medismart.annotation.FactoryRegister;
import org.superdata.medismart.entity.node.Department;

import java.util.List;

@FactoryRegister
@Repository("Department")
public interface DepartmentRepository extends NodeRepository<Department>, Neo4jRepository<Department, Long> {
    @Query("MATCH (n:Department) WHERE n.name CONTAINS $keyword RETURN n LIMIT 3")
    List<Department> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Department) WHERE n.name = $keyword RETURN n")
    List<Department> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Department) WHERE id(n) = $id RETURN n")
    Department matchById(@Param("id") Long id);

    Department findByIdIs(Long id);
}
