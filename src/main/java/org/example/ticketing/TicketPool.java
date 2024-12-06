package org.example.ticketing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new LinkedList<>());
    private int totalTickets; // Total tickets available for release
    private final int maxCapacity; // Maximum ticket pool capacity
    private final List<String> vendorReleases; // Tracks tickets released by vendors
    private final List<String> customerPurchases; // Tracks tickets purchased by customers

    public TicketPool(int totalTickets, int maxCapacity, List<String> vendorReleases, List<String> customerPurchases) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
        this.vendorReleases = vendorReleases;
        this.customerPurchases = customerPurchases;
    }

    public synchronized void addTickets(List<String> newTickets, String vendorId) {
        while (tickets.size() >= maxCapacity) {
            try {
                wait(); // Wait until space is available in the pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        tickets.addAll(newTickets);
        newTickets.forEach(ticket -> vendorReleases.add("\nVendor-" + vendorId + " added: " + ticket));
        Logger.log("Vendor-" + vendorId + " added: " + newTickets.get(0) + " | Tickets in pool now: " + tickets.size() + "\n");
        notifyAll(); // Notify waiting threads
    }

    public synchronized String removeTicket(String customerId) {
        while (tickets.isEmpty() && totalTickets > 0) {
            try {
                wait(); // Wait until a ticket is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (!tickets.isEmpty()) {
            String ticket = tickets.remove(0);
            customerPurchases.add("Customer-" + customerId + " purchased: " + ticket);
            Logger.log("Customer-" + customerId + " removed: " + ticket + " | Tickets left in pool now: " + tickets.size());
            notifyAll(); // Notify waiting threads
            return ticket;
        }
        return null;
    }

    public synchronized int getRemainingTicketsToRelease() {
        return totalTickets;
    }

    public synchronized boolean allTicketsSold() {
        return totalTickets == 0 && tickets.isEmpty();
    }

    public synchronized void decrementTotalTickets(int count) {
        totalTickets -= count;
    }
}
