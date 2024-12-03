package org.example.ticketing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class TicketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);

        Scanner scanner = new Scanner(System.in);

        Configuration config = new Configuration();

        System.out.println("=== Ticketing System Configuration ===");

        config.setTotalTickets(getValidatedInput(scanner, "Enter the total number of tickets: "));
        config.setTicketReleaseRate(getValidatedInput(scanner, "Enter the ticket release rate (seconds): "));
        config.setCustomerRetrievalRate(getValidatedInput(scanner, "Enter the customer retrieval rate (seconds): "));
        config.setMaxTicketCapacity(getValidatedInput(scanner, "Enter the maximum ticket pool capacity: "));
        config.setNumberOfVendors(getValidatedInput(scanner, "Enter the number of vendors: "));

        saveConfiguration(config);

        System.out.println("\nConfiguration complete. Starting the Ticketing System...\n");

        // Create a shared ticket pool
        TicketPool ticketPool = new TicketPool(config.getTotalTickets(), config.getMaxTicketCapacity());

        // Create vendor threads
        List<Thread> vendorThreads = new ArrayList<>();
        for (int i = 1; i <= config.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(ticketPool, i, config.getTicketReleaseRate());
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create customer threads (equal to the total number of tickets)
        List<Thread> customerThreads = new ArrayList<>();
        for (int i = 1; i <= config.getTotalTickets(); i++) {
            Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(), i);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        // Join all threads
        try {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.join();
            }
            for (Thread customerThread : customerThreads) {
                customerThread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted: " + e.getMessage());
        }

        System.out.println("Ticketing System has completed execution. All tickets are sold.");
    }

    private static int getValidatedInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            }
        }
        return value;
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
