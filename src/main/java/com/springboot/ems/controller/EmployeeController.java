package com.springboot.ems.controller;

import com.springboot.ems.Service.EmployeeReadService;
import com.springboot.ems.Service.EmployeeWriteService;
import com.springboot.ems.model.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee API", description = "CRUD operations for Employee Management")
public class EmployeeController {

    @Autowired
    private EmployeeReadService readService;

    @Autowired
    private EmployeeWriteService writeService;

    @GetMapping
    @Operation(summary = "Get all employees")
    public List<Employee> getAllEmployees() {
        return readService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<Employee> getById(@PathVariable int id) {
        return ResponseEntity.ok(readService.getEmployeeById(id));
    }

    @PostMapping
    @Operation(summary = "Add a new employee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee emp) {
        return ResponseEntity.ok(writeService.addEmployee(emp));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing employee")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @Valid @RequestBody Employee emp) {
        return ResponseEntity.ok(writeService.updateEmployee(id, emp));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        writeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees by name or department")
    public List<Employee> search(@RequestParam String keyword) {
        return readService.searchEmployees(keyword);
    }
}
