package org.example.ticketing;

import java.util.ArrayList;
import java.util.List;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private static int ticketCounter = 0; // Shared ticket counter across all vendors

    public Vendor(TicketPool ticketPool, int ticketsPerRelease, int releaseInterval) {
        this.ticketPool = ticketPool;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticketPool) {
                    if (ticketPool.allTicketsSold()) {
                        System.out.println("All tickets are sold. Vendor is stopping.");
                        break;
                    }

                    int ticketsToAdd = Math.min(ticketsPerRelease, ticketPool.getRemainingTicketsToRelease());

                    // Check if there's enough space in the pool
                    if (ticketPool.canAddTickets(ticketsToAdd)) {
                        List<String> newTickets = new ArrayList<>();
                        for (int i = 0; i < ticketsToAdd; i++) {
                            ticketCounter++;
                            newTickets.add(String.format("ID: %03d", ticketCounter)); // Format ticket number
                        }
                        ticketPool.addTickets(newTickets);
                        ticketPool.decrementTotalTickets(ticketsToAdd);
                        System.out.println("Vendor added " + ticketsToAdd + " tickets.");
                    } else {
                        System.out.println("Vendor waiting, ticket pool at capacity.");
                    }
                }
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor interrupted.");
        }
    }
}
