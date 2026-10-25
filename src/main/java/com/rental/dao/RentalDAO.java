package com.rental.dao;

import com.rental.model.Rental;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalDAO {

    // A vehicle is unavailable if it has a rental with no end date yet
    public boolean isAvailable(int vehicleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rentals WHERE vehicle_id = ? AND end_date IS NULL";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }

    public int rentVehicle(int customerId, int vehicleId, LocalDate startDate) throws SQLException {
        String sql = "INSERT INTO rentals (customer_id, vehicle_id, start_date) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.setInt(2, vehicleId);
            ps.setDate(3, Date.valueOf(startDate));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    // Closes the rental and returns the total amount (minimum 1 day)
    public double returnVehicle(int rentalId, LocalDate endDate) throws SQLException {
        String select = "SELECT r.start_date, v.daily_rate FROM rentals r "
                + "JOIN vehicles v ON r.vehicle_id = v.vehicle_id WHERE r.rental_id = ?";
        String update = "UPDATE rentals SET end_date = ?, total_amount = ? WHERE rental_id = ?";
        try (Connection con = DBConnection.getConnection()) {
            LocalDate start;
            double rate;
            try (PreparedStatement ps = con.prepareStatement(select)) {
                ps.setInt(1, rentalId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Rental not found: " + rentalId);
                    start = rs.getDate("start_date").toLocalDate();
                    rate = rs.getDouble("daily_rate");
                }
            }
            long days = Math.max(1, ChronoUnit.DAYS.between(start, endDate));
            double total = days * rate;
            try (PreparedStatement ps = con.prepareStatement(update)) {
                ps.setDate(1, Date.valueOf(endDate));
                ps.setDouble(2, total);
                ps.setInt(3, rentalId);
                ps.executeUpdate();
            }
            return total;
        }
    }

    public List<Rental> getActiveRentals() throws SQLException {
        List<Rental> list = new ArrayList<>();
        String sql = "SELECT rental_id, customer_id, vehicle_id, start_date FROM rentals WHERE end_date IS NULL";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Rental(rs.getInt("rental_id"), rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"), rs.getDate("start_date").toLocalDate(), null, 0));
            }
        }
        return list;
    }
}