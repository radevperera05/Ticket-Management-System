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

    private static final List<String> customerPurchases = new ArrayList<>();
    private static final List<String> vendorReleases = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);

        Scanner scanner = new Scanner(System.in);

        Configuration config = new Configuration();

        System.out.println("=== Ticketing System Configuration ===");

        config.setTotalTickets(getValidatedInput(scanner, "Enter total ticket number: "));
        config.setMaxTicketCapacity(getValidatedInput(scanner, "Enter maximum ticket pool capacity: "));
        config.setNumberOfVendors(getValidatedInput(scanner, "Enter number of vendors: "));
        config.setTicketReleaseRate(getValidatedInput(scanner, "Enter ticket release rate: "));

        saveConfiguration(config);

        System.out.println("\nConfiguration complete. Starting the Ticketing System...\n");

        // Create a shared ticket pool
        TicketPool ticketPool = new TicketPool(config.getTotalTickets(), config.getMaxTicketCapacity(), vendorReleases, customerPurchases);

        // Create vendor threads
        List<Thread> vendorThreads = new ArrayList<>();
        for (int i = 1; i <= config.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(ticketPool, i, config.getTicketReleaseRate());
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create customer threads
        List<Thread> customerThreads = new ArrayList<>();
        for (int i = 1; i <= config.getTotalTickets(); i++) {
            Customer customer = new Customer(ticketPool, i);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        // Wait for all threads to complete
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

        System.out.println("\nSystem terminated: All tickets are sold.\n");
        displaySummary(scanner);
    }

    private static void displaySummary(Scanner scanner) {
        System.out.println("===== Summary =====");
        System.out.println("Total Tickets Sold: " + customerPurchases.size());
        System.out.println("Total Customers Served: " + customerPurchases.size());
        System.out.println("Times Ticket Pool Was Filled by Vendors: 0"); // Placeholder for now
        System.out.println("===================");

        while (true) {
            System.out.println("\nAdditional Information");
            System.out.println("1. View all tickets purchased");
            System.out.println("2. View all tickets sold");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("\n=== Tickets Purchased ===");
                customerPurchases.forEach(System.out::println);
            } else if (choice == 2) {
                System.out.println("\n=== Tickets Sold by Vendors ===");
                vendorReleases.forEach(System.out::println);
            } else if (choice == 3) {
                System.out.println("Exiting.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
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
