package com.employeemanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeManager {
    private List<Employee> employeeList;

    public EmployeeManager() {
        employeeList = new ArrayList<>();
    }

    // Add a new employee
    public void addEmployee(Employee employee) {
        employeeList.add(employee);
        System.out.println("Employee added successfully: " + employee.getName());
    }

    // View all employees
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    // View a specific employee by ID
    public Optional<Employee> getEmployeeById(int id) {
        for (Employee employee : employeeList) {
            if (employee.getId() == id) {
                return Optional.of(employee);
            }
        }
        return Optional.empty(); // Return empty Optional if employee not found
    }

    // Update employee details (basic example: update department and salary)
    public boolean updateEmployee(int id, String newDepartment, double newSalary) {
        Optional<Employee> employeeOptional = getEmployeeById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setDepartment(newDepartment);
            employee.setSalary(newSalary);
            System.out.println("Employee with ID " + id + " updated successfully.");
            return true;
        } else {
            System.out.println("Employee with ID " + id + " not found.");
            return false;
        }
    }

    // Delete an employee by ID
    public boolean deleteEmployee(int id) {
        return employeeList.removeIf(employee -> employee.getId() == id);
    }
}