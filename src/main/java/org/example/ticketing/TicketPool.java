package org.example.ticketing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new LinkedList<>());
    private int totalTickets; // Total tickets available to release
    private int maxCapacity;  // Maximum ticket pool capacity

    public TicketPool(int totalTickets) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTickets(List<String> newTickets) {
        tickets.addAll(newTickets);
        System.out.println("Tickets added: " + newTickets.size() + " | Total in pool: " + tickets.size());
    }

    public synchronized String removeTicket() {
        if (!tickets.isEmpty()) {
            return tickets.remove(0);
        }
        return null;
    }

    public synchronized int getRemainingTicketsInPool() {
        return tickets.size();
    }

    public synchronized int getRemainingTicketsToRelease() {
        return totalTickets;
    }

    public synchronized void decrementTotalTickets(int count) {
        totalTickets -= count;
    }

    public synchronized boolean canAddTickets(int count) {
        return tickets.size() + count <= maxCapacity;
    }

    public synchronized boolean allTicketsSold() {
        return totalTickets == 0 && tickets.isEmpty();
    }
}
