package com.employeemanagement;

import java.sql.*; // Import JDBC classes
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeManager {

    // --- Database Connection Details (for MySQL) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeedb"; // <-- Update if your host/port/db name are different
    private static final String DB_USER = "your_mysql_username"; // <-- REPLACE WITH YOUR MYSQL USERNAME
    private static final String DB_PASSWORD = "your_mysql_password"; // <-- REPLACE WITH YOUR MYSQL PASSWORD

    // Load the MySQL JDBC driver class
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found! Make sure mysql-connector-j.jar is in your classpath.");
            e.printStackTrace();
        }
    }

    public EmployeeManager() {
        // When the manager is created, ensure the database table exists
        // This is less critical if you've already created it in Workbench,
        // but good for ensuring the table is there if running for the first time.
        createEmployeeTable();
    }

    // Helper method to get a database connection
    private Connection getConnection() throws SQLException {
        // Connect to the MySQL database using the defined URL, user, and password
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Method to create the employees table if it doesn't exist
    // (This is here for completeness, but you've already done it in Workbench)
    private void createEmployeeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                     "id INT PRIMARY KEY," +
                     "name VARCHAR(255) NOT NULL," +
                     "department VARCHAR(255)," +
                     "salary DOUBLE" +
                     ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Employee table checked/created successfully in MySQL.");

        } catch (SQLException e) {
            // Handle potential errors during table creation
            // If the table already exists, IF NOT EXISTS handles it, no error.
            System.err.println("Error creating or checking employee table in MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Modified Methods to use Database (These remain the same as in the previous SQL example) ---

    // Add a new employee to the database
    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employees(id, name, department, salary) VALUES(?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getDepartment());
            pstmt.setDouble(4, employee.getSalary());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee added successfully to database: " + employee.getName());
            } else {
                 System.out.println("Failed to add employee to database: " + employee.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error adding employee to database: " + e.getMessage());
            // Check for duplicate entry error code (specific to MySQL)
            if (e.getErrorCode() == 1062) { // 1062 is the error code for duplicate entry for primary key in MySQL
                 System.err.println("Error: Employee with ID " + employee.getId() + " already exists.");
            }
            e.printStackTrace();
        }
    }

    // Retrieve all employees from the database
    public List<Employee> getAllEmployees() {
        String sql = "SELECT id, name, department, salary FROM employees";
        List<Employee> employeeList = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                employeeList.add(new Employee(id, name, department, salary));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all employees from database: " + e.getMessage());
            e.printStackTrace();
        }
        return employeeList;
    }

    // Retrieve a specific employee by ID from the database
    public Optional<Employee> getEmployeeById(int id) {
         String sql = "SELECT id, name, department, salary FROM employees WHERE id = ?";
         try (Connection conn = getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setInt(1, id);
             ResultSet rs = pstmt.executeQuery();
             if (rs.next()) {
                 String name = rs.getString("name");
                 String department = rs.getString("department");
                 double salary = rs.getDouble("salary");
                 return Optional.of(new Employee(id, name, department, salary));
             }
         } catch (SQLException e) {
             System.err.println("Error fetching employee by ID from database: " + e.getMessage());
             e.printStackTrace();
         }
         return Optional.empty();
    }

    // Update employee details in the database
    public boolean updateEmployee(int id, String newDepartment, double newSalary) {
        String sql = "UPDATE employees SET department = ?, salary = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newDepartment);
            pstmt.setDouble(2, newSalary);
            pstmt.setInt(3, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee with ID " + id + " updated successfully in database.");
                return true;
            } else {
                System.out.println("Employee with ID " + id + " not found in database.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating employee with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete an employee from the database by ID
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee with ID " + id + " deleted successfully from database.");
                return true;
            } else {
                System.out.println("Employee with ID " + id + " not found in database.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting employee with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
