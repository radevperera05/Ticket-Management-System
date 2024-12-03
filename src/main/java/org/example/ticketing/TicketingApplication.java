package org.example.ticketing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class TicketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);

        Scanner scanner = new Scanner(System.in);

        Configuration config = new Configuration();

        System.out.println("=== Ticketing System Configuration ===");

        System.out.print("Enter the total number of tickets: ");
        config.setTotalTickets(scanner.nextInt());

        System.out.print("Enter the ticket release rate (milliseconds): ");
        config.setTicketReleaseRate(scanner.nextInt());

        System.out.print("Enter the customer retrieval rate (milliseconds): ");
        config.setCustomerRetrievalRate(scanner.nextInt());

        System.out.print("Enter the maximum ticket pool capacity: ");
        config.setMaxTicketCapacity(scanner.nextInt());

        saveConfiguration(config);

        System.out.println("\nConfiguration complete. Starting the Ticketing System...\n");

        // Create a shared ticket pool
        TicketPool ticketPool = new TicketPool(config.getTotalTickets());

        // Create vendor and customer threads
        Vendor vendor = new Vendor(ticketPool, 5, config.getTicketReleaseRate());
        Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate());

        Thread vendorThread = new Thread(vendor);
        Thread customerThread = new Thread(customer);

        vendorThread.start();
        customerThread.start();
    }

    private static void saveConfiguration(Configuration config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("configuration.json")) {
            gson.toJson(config, writer);
            System.out.println("Configuration saved to configuration.json.");
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }
}
