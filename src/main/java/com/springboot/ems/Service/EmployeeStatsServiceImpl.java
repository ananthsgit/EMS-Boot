package com.springboot.ems.Service;

import com.springboot.ems.model.Employee.Status;
import com.springboot.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeStatsServiceImpl implements EmployeeStatsService {

    @Autowired
    private EmployeeRepository repo;

    @Override
    public long getTotalEmployees() {
        return repo.count();
    }

    @Override
    public long getActiveCount() {
        return repo.countByStatus(Status.ACTIVE);
    }

    @Override
    public long getInactiveCount() {
        return repo.countByStatus(Status.INACTIVE);
    }

    @Override
    public long getOnLeaveCount() {
        return repo.countByStatus(Status.ON_LEAVE);
    }

    @Override
    public List<String> getAllDepartments() {
        return repo.findAllDepartments();
    }

    @Override
    public Map<String, Long> getDepartmentStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        repo.countByDepartment().forEach(row -> stats.put((String) row[0], (Long) row[1]));
        return stats;
    }
}
