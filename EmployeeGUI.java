package com.employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeGUI extends JFrame {

    private EmployeeManager employeeManager;

    // GUI Components
    private JTextField idField;
    private JTextField nameField;
    private JTextField departmentField;
    private JTextField salaryField;
    private JTextArea displayArea;
    private JButton addButton;
    private JButton viewAllButton;

    public EmployeeGUI() {
        // Set up the frame
        super("Employee Management System"); // Set window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setSize(400, 400); // Window size
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout()); // Use BorderLayout for the main frame

        // Initialize the backend manager
        employeeManager = new EmployeeManager();

        // --- Input Panel (North) ---
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // 5 rows, 2 columns, gap
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Employee"));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        inputPanel.add(departmentField);

        inputPanel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        inputPanel.add(salaryField);

        addButton = new JButton("Add Employee");
        inputPanel.add(addButton); // Add button to panel

        viewAllButton = new JButton("View All Employees");
        inputPanel.add(viewAllButton); // Add button to panel


        add(inputPanel, BorderLayout.NORTH); // Add the input panel to the top

        // --- Display Area (Center) ---
        displayArea = new JTextArea();
        displayArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(displayArea); // Add scrollability
        add(scrollPane, BorderLayout.CENTER); // Add the scrollable text area to the center

        // --- Action Listeners ---
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllEmployees();
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    private void addEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String department = departmentField.getText();
            double salary = Double.parseDouble(salaryField.getText());

            // Basic validation (can be more comprehensive)
            if (name.isEmpty() || department.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Name and Department cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            // Check if ID already exists (using the manager's method)
            if (employeeManager.getEmployeeById(id).isPresent()) {
                 JOptionPane.showMessageDialog(this, "Employee with ID " + id + " already exists.", "Input Error", JOptionPane.WARNING_MESSAGE);
                 return;
            }


            Employee newEmployee = new Employee(id, name, department, salary);
            employeeManager.addEmployee(newEmployee); // Use the manager's method

            // Clear fields after adding
            idField.setText("");
            nameField.setText("");
            departmentField.setText("");
            salaryField.setText("");

            // Optional: Show success message or update display area
            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID or Salary format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace(); // Print stack trace for debugging
        }
    }

    private void viewAllEmployees() {
        List<Employee> employees = employeeManager.getAllEmployees();
        displayArea.setText(""); // Clear previous text

        if (employees.isEmpty()) {
            displayArea.setText("No employees found.");
        } else {
            StringBuilder sb = new StringBuilder("--- All Employees ---\n");
            for (Employee emp : employees) {
                sb.append(emp.toString()).append("\n");
            }
            displayArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmployeeGUI(); // Create and show the GUI
            }
        });
    }
}