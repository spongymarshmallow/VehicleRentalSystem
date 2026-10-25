package com.rental.ui;

import com.rental.dao.CustomerDAO;
import com.rental.dao.RentalDAO;
import com.rental.dao.VehicleDAO;
import com.rental.model.Customer;
import com.rental.model.Rental;
import com.rental.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final RentalDAO rentalDAO = new RentalDAO();

    private final DefaultTableModel vehicleModel = new DefaultTableModel(
            new String[]{"ID", "Reg No", "Model", "Type", "Rate/day", "Available"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final DefaultTableModel rentalModel = new DefaultTableModel(
            new String[]{"Rental ID", "Customer ID", "Vehicle ID", "Start date"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable vehicleTable = new JTable(vehicleModel);
    private final JTable rentalTable = new JTable(rentalModel);
    private final JComboBox<Customer> customerBox = new JComboBox<>();

    public MainFrame() {
        setTitle("Vehicle Rental System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Rent", buildRentPanel());
        tabs.addTab("Return", buildReturnPanel());
        add(tabs);

        refreshAll();
    }

    private JPanel buildRentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        JButton rentBtn = new JButton("Rent Selected Vehicle");
        JButton addCustBtn = new JButton("Add Customer");
        rentBtn.addActionListener(e -> rentSelected());
        addCustBtn.addActionListener(e -> addCustomer());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Customer:"));
        bottom.add(customerBox);
        bottom.add(rentBtn);
        bottom.add(addCustBtn);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildReturnPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(rentalTable), BorderLayout.CENTER);

        JButton returnBtn = new JButton("Return Selected Rental");
        returnBtn.addActionListener(e -> returnSelected());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(returnBtn);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAll() {
        try {
            vehicleModel.setRowCount(0);
            for (Vehicle v : vehicleDAO.getAllVehicles()) {
                vehicleModel.addRow(new Object[]{v.getVehicleId(), v.getRegNo(), v.getModel(),
                        v.getType(), v.getDailyRate(),
                        rentalDAO.isAvailable(v.getVehicleId()) ? "Yes" : "No"});
            }
            customerBox.removeAllItems();
            for (Customer c : customerDAO.getAllCustomers()) {
                customerBox.addItem(c);
            }
            rentalModel.setRowCount(0);
            for (Rental r : rentalDAO.getActiveRentals()) {
                rentalModel.addRow(new Object[]{r.getRentalId(), r.getCustomerId(),
                        r.getVehicleId(), r.getStartDate()});
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void rentSelected() {
        int row = vehicleTable.getSelectedRow();
        Customer customer = (Customer) customerBox.getSelectedItem();
        if (row == -1 || customer == null) {
            JOptionPane.showMessageDialog(this, "Select a vehicle and a customer first.");
            return;
        }
        int vehicleId = (int) vehicleModel.getValueAt(row, 0);
        try {
            if (!rentalDAO.isAvailable(vehicleId)) {
                JOptionPane.showMessageDialog(this, "This vehicle is already rented out.");
                return;
            }
            int id = rentalDAO.rentVehicle(customer.getCustomerId(), vehicleId, LocalDate.now());
            JOptionPane.showMessageDialog(this, "Rented! Rental ID: " + id);
            refreshAll();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void returnSelected() {
        int row = rentalTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a rental first.");
            return;
        }
        int rentalId = (int) rentalModel.getValueAt(row, 0);
        try {
            double total = rentalDAO.returnVehicle(rentalId, LocalDate.now());
            JOptionPane.showMessageDialog(this, "Vehicle returned. Total bill: ₹" + total);
            refreshAll();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void addCustomer() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField license = new JTextField();
        Object[] form = {"Name:", name, "Phone:", phone, "License no:", license};
        int ok = JOptionPane.showConfirmDialog(this, form, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;
        if (name.getText().isBlank() || license.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Name and license number are required.");
            return;
        }
        try {
            customerDAO.addCustomer(name.getText().trim(), phone.getText().trim(), license.getText().trim());
            refreshAll();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}