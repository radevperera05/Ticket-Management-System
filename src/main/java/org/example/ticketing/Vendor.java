package org.example.ticketing;

import java.util.List;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releaseInterval;
    private static int ticketCounter = 0; // Shared counter for unique ticket IDs

    public Vendor(TicketPool ticketPool, int vendorId, int releaseInterval) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticketPool) {
                    if (ticketPool.allTicketsSold()) {
                        Logger.log("\nAll tickets are sold. Vendor-" + vendorId + " is stopping.");
                        break;
                    }

                    if (ticketPool.getRemainingTicketsToRelease() > 0) {
                        ticketCounter++;
                        String ticket = String.format("Ticket - ID: %03d", ticketCounter);
                        ticketPool.addTickets(List.of(ticket), String.valueOf(vendorId));
                        ticketPool.decrementTotalTickets(1);
                    }
                }
                Thread.sleep(releaseInterval * 1000L); // Simulate ticket release interval
            }
        } catch (InterruptedException e) {
            Logger.log("Vendor-" + vendorId + " interrupted.");
        }
    }
}
