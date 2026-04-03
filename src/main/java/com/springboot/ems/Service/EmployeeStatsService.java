package com.springboot.ems.Service;

import java.util.List;
import java.util.Map;

public interface EmployeeStatsService {
    long getTotalEmployees();
    long getActiveCount();
    long getInactiveCount();
    long getOnLeaveCount();
    List<String> getAllDepartments();
    Map<String, Long> getDepartmentStats();
}
