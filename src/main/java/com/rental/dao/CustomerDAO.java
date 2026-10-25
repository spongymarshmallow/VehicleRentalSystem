package com.rental.dao;

import com.rental.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id, name, phone, license_no FROM customers";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Customer(rs.getInt("customer_id"), rs.getString("name"),
                        rs.getString("phone"), rs.getString("license_no")));
            }
        }
        return list;
    }

    public void addCustomer(String name, String phone, String licenseNo) throws SQLException {
        String sql = "INSERT INTO customers (name, phone, license_no) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, licenseNo);
            ps.executeUpdate();
        }
    }
}