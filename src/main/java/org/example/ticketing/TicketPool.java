package org.example.ticketing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new LinkedList<>());
    private int totalTickets;
    private final int maxCapacity;

    public TicketPool(int totalTickets, int maxCapacity) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTickets(List<String> newTickets) {
        while (tickets.size() >= maxCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        tickets.addAll(newTickets);
        Logger.log("Tickets added: " + newTickets.size() + " | Total in pool: " + tickets.size());
        notifyAll();
    }

    public synchronized String removeTicket() {
        while (tickets.isEmpty() && totalTickets > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (!tickets.isEmpty()) {
            String ticket = tickets.remove(0);
            notifyAll();
            return ticket;
        }
        return null;
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
