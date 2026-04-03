package com.springboot.ems.Service;

import com.springboot.ems.model.Employee;

public interface EmployeeWriteService {
    Employee addEmployee(Employee emp);
    Employee updateEmployee(int id, Employee emp);
    void deleteEmployee(int id);
}
