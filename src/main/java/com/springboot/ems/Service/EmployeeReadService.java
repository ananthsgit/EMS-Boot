package com.springboot.ems.Service;

import com.springboot.ems.model.Employee;

import java.util.List;

public interface EmployeeReadService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(int id);
    List<Employee> searchEmployees(String keyword);
}
