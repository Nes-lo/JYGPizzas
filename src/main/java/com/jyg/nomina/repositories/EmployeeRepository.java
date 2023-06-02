package com.jyg.nomina.repositories;

import com.jyg.nomina.models.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Long> {

    List<Employee> findAll();
    Optional<Employee> findByEmployeeName(String name);
    Optional<Employee> findByIdAndAsset(Long id, Boolean asset);
}
