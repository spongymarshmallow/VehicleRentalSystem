package com.rental.model;

public class Vehicle {
    private int vehicleId;
    private String regNo;
    private String model;
    private String type;
    private double dailyRate;

    public Vehicle(int vehicleId, String regNo, String model, String type, double dailyRate) {
        this.vehicleId = vehicleId;
        this.regNo = regNo;
        this.model = model;
        this.type = type;
        this.dailyRate = dailyRate;
    }

    public int getVehicleId() { return vehicleId; }
    public String getRegNo() { return regNo; }
    public String getModel() { return model; }
    public String getType() { return type; }
    public double getDailyRate() { return dailyRate; }

    @Override
    public String toString() {
        return model + " (" + regNo + ") - ₹" + dailyRate + "/day";
    }
}