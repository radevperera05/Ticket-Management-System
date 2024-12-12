# Event Ticket Manager

## Overview

The **Event Ticket Manager** is a web application that simulates the vending and purchasing of tickets for an event. The application uses a configurable ticket pool and real-time logs to track ticket releases by vendors and purchases by customers. It ensures proper synchronization between vendors and customers and automatically stops when all tickets are sold.

---

## Features

- **Real-time Updates**: Logs ticket release and purchase events live.
- **Configurable Settings**: Customize total ticket count, release rate, retrieval rate, and ticket pool size.
- **Concurrency Handling**: Proper synchronization between vendors and customers interacting with the ticket pool.
- **Automatic System Stop**: Stops when all tickets are sold.
- **User Interface**: Web-based frontend to configure the system, monitor logs, and control the system.

---

## Prerequisites

- **Backend**:
  - Java 17+
  - Spring Boot Framework
  - Maven
- **Frontend**:
  - Node.js 18+
  - npm or yarn
- **Database**: No external database is required.

---

## Setup Instructions

### Backend

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Install Dependencies**:
   Ensure `Maven` is installed and run:
   ```bash
   mvn install
   ```

3. **Run the Backend**:
   Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   The backend will run on `http://localhost:8080`.

### Frontend

1. **Navigate to Frontend Directory**:
   ```bash
   cd frontend
   ```

2. **Install Dependencies**:
   Use `npm` or `yarn`:
   ```bash
   npm install
   ```
   or
   ```bash
   yarn install
   ```

3. **Run the Frontend**:
   Start the React application:
   ```bash
   npm start
   ```
   or
   ```bash
   yarn start
   ```
   The frontend will run on `http://localhost:3000`.

---

## Usage Instructions

### Configuration

1. Launch the application at `http://localhost:3000`.
2. Configure the system using the **Configuration** form:
   - **Total Tickets**: Total number of tickets to sell.
   - **Ticket Release Rate**: Time interval (in seconds) for vendors to release tickets.
   - **Customer Retrieval Rate**: Time interval (in seconds) for customers to retrieve tickets.
   - **Maximum Ticket Capacity**: Maximum number of tickets the pool can hold at any given time.
3. Click **Save** to apply the configuration.

### Start the System

1. Click the **Start** button in the **Control Panel** to begin ticket sales.
2. Monitor real-time logs for:
   - Vendors releasing tickets.
   - Customers purchasing tickets.
   - Ticket pool status updates.

### Stop the System

1. To manually stop the system, click the **Stop** button in the **Control Panel**.
2. The system will stop automatically when all tickets are sold. Logs will indicate vendor and system stoppage.

---

## Backend APIs

| Endpoint               | Method | Description                              |
|------------------------|--------|------------------------------------------|
| `/api/configure`       | POST   | Configure the system with ticket details.|
| `/api/start`           | POST   | Start the ticket management system.      |
| `/api/stop`            | POST   | Stop the ticket management system.       |
| `/api/ticket-status`   | GET    | Fetch the current ticket status.         |

---

## Example Logs

```
System configured with 20 tickets
System Start
Vendor 1 released ticket: ID: 001
Vendor 2 released ticket: ID: 002
Customer 1 purchased ticket: ID: 001
Vendor 3 released ticket: ID: 003
Customer 2 purchased ticket: ID: 002
Customer 3 purchased ticket: ID: 003
...
All tickets are sold. Vendor 1 is stopping.
System Stop
```

---

## Development Notes

### Key Components

- **Backend**:
  - `TicketPool`: Manages ticket pool size and synchronization.
  - `Vendor`: Simulates ticket release into the pool.
  - `Customer`: Simulates ticket purchase from the pool.
  - `Logger`: Logs actions and updates.
- **Frontend**:
  - React-based user interface for configuration, status monitoring, and control.

### Configuration

- The system is configured using a REST API with JSON payloads.
- Real-time updates are implemented using WebSocket communication.

### Logs

Logs are stored in:
- The `ticketing_system_output.txt` file (backend).
- Real-time updates in the frontend log viewer.

---

## Troubleshooting

- Ensure both backend and frontend servers are running.
- Check for CORS issues and update the backend's `WebConfig` if necessary.
- Verify WebSocket connection (`/ws`) is properly established.

---

## Contributions

Contributions are welcome! Fork the repository and create pull requests for new features, enhancements, or bug fixes.

---
