package org.superdata.medismart.kgRepo.nodeType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.superdata.medismart.entity.kg.nodeType.Department;
import org.superdata.medismart.kgRepo.NodeRepository;

import java.util.List;

public interface DepartmentRepository extends NodeRepository<Department>, Neo4jRepository<Department, Long> {
    @Query("MATCH (n:Department) WHERE n.name CONTAINS $keyword RETURN n")
    List<Department> fuzzyMatchByName(@Param("keyword") String keyword);

    @Query("MATCH (n:Department) WHERE n.name = $keyword RETURN n")
    List<Department> preciseMatchByName(@Param("keyword")String keyword);

    @Query("MATCH (n:Department) WHERE id(n) = $id RETURN n")
    Department matchById(@Param("id") Long id);
}
