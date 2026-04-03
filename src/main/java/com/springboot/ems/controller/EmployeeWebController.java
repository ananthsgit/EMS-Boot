package com.springboot.ems.controller;

import com.springboot.ems.Service.EmployeeReadService;
import com.springboot.ems.Service.EmployeeStatsService;
import com.springboot.ems.Service.EmployeeWriteService;
import com.springboot.ems.model.Employee;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    @Autowired
    private EmployeeReadService readService;

    @Autowired
    private EmployeeWriteService writeService;

    @Autowired
    private EmployeeStatsService statsService;

    @GetMapping
    public String listEmployees(Model model, @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("employees", readService.searchEmployees(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("employees", readService.getAllEmployees());
        }
        model.addAttribute("totalEmployees", statsService.getTotalEmployees());
        return "employees/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("statuses", Employee.Status.values());
        model.addAttribute("genders", Employee.Gender.values());
        return "employees/form";
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Employee.Status.values());
            model.addAttribute("genders", Employee.Gender.values());
            return "employees/form";
        }
        writeService.addEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("employee", readService.getEmployeeById(id));
        model.addAttribute("statuses", Employee.Status.values());
        model.addAttribute("genders", Employee.Gender.values());
        return "employees/form";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, @Valid @ModelAttribute Employee employee,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Employee.Status.values());
            model.addAttribute("genders", Employee.Gender.values());
            return "employees/form";
        }
        writeService.updateEmployee(id, employee);
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {
        writeService.deleteEmployee(id);
        return "redirect:/employees";
    }

    @GetMapping("/mark-on-leave/{id}")
    public String markOnLeave(@PathVariable int id) {
        Employee emp = readService.getEmployeeById(id);
        emp.setStatus(Employee.Status.ON_LEAVE);
        writeService.updateEmployee(id, emp);
        return "redirect:/employees";
    }

    @GetMapping("/mark-active/{id}")
    public String markActive(@PathVariable int id) {
        Employee emp = readService.getEmployeeById(id);
        emp.setStatus(Employee.Status.ACTIVE);
        writeService.updateEmployee(id, emp);
        return "redirect:/employees";
    }

    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable int id, Model model) {
        model.addAttribute("employee", readService.getEmployeeById(id));
        return "employees/view";
    }
}
