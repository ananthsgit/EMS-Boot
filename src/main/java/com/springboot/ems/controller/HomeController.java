package com.springboot.ems.controller;

import com.springboot.ems.Service.EmployeeReadService;
import com.springboot.ems.Service.EmployeeStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private EmployeeReadService readService;

    @Autowired
    private EmployeeStatsService statsService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalEmployees", statsService.getTotalEmployees());
        model.addAttribute("activeCount", statsService.getActiveCount());
        model.addAttribute("inactiveCount", statsService.getInactiveCount());
        model.addAttribute("onLeaveCount", statsService.getOnLeaveCount());
        model.addAttribute("departmentStats", statsService.getDepartmentStats());
        model.addAttribute("recentEmployees", readService.getAllEmployees());
        return "dashboard";
    }
}
