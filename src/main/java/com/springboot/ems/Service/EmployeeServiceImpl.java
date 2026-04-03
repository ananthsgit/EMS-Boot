package com.springboot.ems.Service;

import com.springboot.ems.model.Employee;
import com.springboot.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeReadService, EmployeeWriteService {

    @Autowired
    private EmployeeRepository repo;

    @Override
    public Employee addEmployee(Employee emp) {
        return repo.save(emp);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    @Override
    public Employee getEmployeeById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(int id, Employee emp) {
        Employee existing = getEmployeeById(id);
        existing.copyFrom(emp);
        return repo.save(existing);
    }

    @Override
    public void deleteEmployee(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<Employee> searchEmployees(String keyword) {
        return repo.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCase(keyword, keyword);
    }
}
