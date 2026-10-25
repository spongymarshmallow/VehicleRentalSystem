-- Vehicle Rental System: database schema and sample data
-- Usage: mysql -u root -p < schema.sql

CREATE DATABASE IF NOT EXISTS vehicle_rental;
USE vehicle_rental;

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    phone       VARCHAR(15)  NOT NULL,
    license_no  VARCHAR(30)  NOT NULL,
    PRIMARY KEY (customer_id),
    UNIQUE KEY phone (phone),
    UNIQUE KEY license_no (license_no)
    );

CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT NOT NULL AUTO_INCREMENT,
    reg_no     VARCHAR(20)   NOT NULL,
    model      VARCHAR(50)   NOT NULL,
    type       ENUM('CAR','BIKE','SUV','VAN') NOT NULL,
    daily_rate DECIMAL(10,2) NOT NULL,
    available  TINYINT(1)    NOT NULL DEFAULT 1,
    PRIMARY KEY (vehicle_id),
    UNIQUE KEY reg_no (reg_no),
    CHECK (daily_rate > 0)
    );

CREATE TABLE IF NOT EXISTS rentals (
    rental_id    INT NOT NULL AUTO_INCREMENT,
    customer_id  INT NOT NULL,
    vehicle_id   INT NOT NULL,
    start_date   DATE NOT NULL,
    end_date     DATE DEFAULT NULL,
    total_amount DECIMAL(10,2) DEFAULT NULL,
    PRIMARY KEY (rental_id),
    KEY customer_id (customer_id),
    KEY vehicle_id (vehicle_id),
    CONSTRAINT rentals_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    CONSTRAINT rentals_ibfk_2 FOREIGN KEY (vehicle_id)  REFERENCES vehicles (vehicle_id)
    );

-- Sample data
INSERT INTO customers (name, phone, license_no)
VALUES ('Test Customer', '9999999999', 'RJ14-2020-0001');

INSERT INTO vehicles (reg_no, model, type, daily_rate, available)
VALUES ('RJ14AB1234', 'Maruti Swift',   'CAR',  1500.00, 1),
       ('RJ14CD5678', 'Royal Enfield',  'BIKE',  800.00, 1),
       ('RJ14EF9012', 'Toyota Innova',  'VAN',  3000.00, 1);