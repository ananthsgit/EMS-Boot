package com.springboot.ems.repository;

import com.springboot.ems.model.Employee;
import com.springboot.ems.model.Employee.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(String name, String department);

    List<Employee> findByDepartment(String department);

    List<Employee> findByStatus(Status status);

    long countByStatus(Status status);

    @Query("SELECT DISTINCT e.department FROM Employee e")
    List<String> findAllDepartments();

    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> countByDepartment();
}
