package com.rental.model;

public class Customer {
    private int customerId;
    private String name;
    private String phone;
    private String licenseNo;

    public Customer(int customerId, String name, String phone, String licenseNo) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLicenseNo() { return licenseNo; }

    @Override
    public String toString() { return name + " (" + licenseNo + ")"; }
}