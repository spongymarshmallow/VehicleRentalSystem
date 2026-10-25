# Vehicle Rental System

A desktop application for managing vehicle rentals, built in **Java (Swing)** with a **MySQL** database. It is a combined OOPs and RDBMS mini project.

## Features

- View available vehicles loaded live from the database
- Rent a vehicle to a customer (Rent tab)
- Return a rented vehicle (Return tab)
- Vehicle availability and rental records are updated in the database on each action

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 25 |
| GUI | Java Swing |
| Database | MySQL 8 |
| DB access | JDBC (MySQL Connector/J 8.4.0) |
| Build tool | Maven |

## Project Structure

```
src/main/java/com/rental/
├── model/    # Entity classes (Vehicle, Customer, Rental)
├── dao/      # Database access (DBConnection, VehicleDAO, CustomerDAO, RentalDAO)
├── service/  # Business logic
└── ui/       # Swing GUI
```

## Database

Three tables in the `vehicle_rental` database:

- `customers` - customer details
- `vehicles` - vehicle details and availability
- `rentals` - rental records linking customers and vehicles

The full schema and sample data are in `schema.sql`.

## Setup and Run

1. **Prerequisites:** JDK 25, Maven, MySQL Server 8.x
2. **Create the database:** run `schema.sql` in MySQL Workbench or the MySQL CLI:
   ```
   mysql -u root -p < schema.sql
   ```
3. **Set the database password** as an environment variable (it is not stored in the code):
   - Windows (PowerShell): `$env:DB_PASSWORD="your_password"`
   - Linux/macOS: `export DB_PASSWORD=your_password`
4. **Check the connection settings** in `DBConnection.java` (default: `127.0.0.1:3306`, user `root`, database `vehicle_rental`).
5. **Build and run:** Open the project in IntelliJ IDEA, add `DB_PASSWORD` to the run configuration, and run `Main`.

## Author

Shriyans Purkayastha

B.Tech CSE, Manipal University Jaipur
