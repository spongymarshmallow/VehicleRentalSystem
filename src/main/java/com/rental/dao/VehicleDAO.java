package com.rental.dao;

import com.rental.model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT vehicle_id, reg_no, model, type, daily_rate FROM vehicles";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("reg_no"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getDouble("daily_rate")));
            }
        }
        return list;
    }
}