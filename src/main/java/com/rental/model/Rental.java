package com.rental.model;

import java.time.LocalDate;

public class Rental {
    private int rentalId;
    private int customerId;
    private int vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;     // null while the vehicle is still rented out
    private double totalAmount;

    public Rental(int rentalId, int customerId, int vehicleId,
                  LocalDate startDate, LocalDate endDate, double totalAmount) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
    }

    public int getRentalId() { return rentalId; }
    public int getCustomerId() { return customerId; }
    public int getVehicleId() { return vehicleId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Rental#" + rentalId + " cust=" + customerId + " veh=" + vehicleId
                + " " + startDate + " -> " + (endDate == null ? "ongoing" : endDate)
                + " ₹" + totalAmount;
    }
}