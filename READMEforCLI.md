# Event Ticketing System

## Overview
The **Event Ticketing System** is a multithreaded Java application designed to simulate ticket sales for events. The system models vendors releasing tickets and customers purchasing them in real-time, with synchronized access to a shared ticket pool. 

The program offers functionalities like system configuration, simulation start/stop, and status logging.

---

## Features
1. **Configuration Management**:
   - Define the maximum ticket pool size.
   - Set the number of vendors and customers.
   - Configure ticket release and purchase rates.

2. **Vendor Simulation**:
   - Vendors release tickets into a shared pool at a configurable rate.

3. **Customer Simulation**:
   - Customers retrieve tickets from the shared pool at a configurable rate.

4. **Thread Management**:
   - Multithreaded execution for real-time simulation of vendors and customers.
   - Proper thread interruption handling for clean shutdown.

5. **System Status and Logs**:
   - View real-time system status.
   - Log all transactions (ticket additions and removals) to a file (`logs.txt`).

---

## Technologies Used
- **Java**: Core programming language.
- **MySQL**: Database to store system configurations.
- **Multithreading**: Java's `Thread` class for concurrent execution.

---

## Project Structure
### Classes and Responsibilities
1. **`Main`**:
   - Entry point of the application.
   - Menu-driven interface for administrators.
   - Manages the lifecycle of the simulation.

2. **`ConfigurationManager`**:
   - Handles system configuration.
   - Stores configuration in a database.

3. **`DBConnection`**:
   - Establishes a connection to the MySQL database.

4. **`TicketPool`**:
   - Shared resource for tickets.
   - Synchronizes ticket addition and removal to ensure thread safety.
   - Logs ticket transactions to `logs.txt`.

5. **`Vendor`**:
   - Simulates a vendor releasing tickets to the pool.

6. **`Customer`**:
   - Simulates a customer purchasing tickets from the pool.

7. **`Ticket`**:
   - Represents an individual ticket with attributes like ID, event name, and price.

---

## Prerequisites
### Software Requirements
- **Java JDK 8+**
- **MySQL Database**
  - Database name: `MyEvents`
  - Table: `adminConfigurations`

### Database Configuration
Ensure a table is created for storing system configurations:
```sql
CREATE TABLE adminConfigurations (
    Maximum_Pool_Size INT,
    Total_Vendors INT,
    Release_Per_Vendor INT,
    Vendor_Release_Rate INT,
    Total_Customers INT,
    Tickets_Per_Customer INT,
    Customer_Buying_Rate INT
);

