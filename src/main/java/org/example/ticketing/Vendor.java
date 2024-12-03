package org.example.ticketing;

import java.util.List;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releaseInterval;
    private static int ticketCounter = 0;

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
                        Logger.log("All tickets are sold. Vendor " + vendorId + " is stopping.");
                        break;
                    }

                    if (ticketPool.canAddTickets(1) && ticketPool.getRemainingTicketsToRelease() > 0) {
                        ticketCounter++;
                        String ticket = String.format("ID: %03d", ticketCounter);
                        ticketPool.addTickets(List.of(ticket));
                        ticketPool.decrementTotalTickets(1);
                        Logger.log("Vendor " + vendorId + " released ticket: " + ticket);
                    } else {
                        Logger.log("Vendor " + vendorId + " waiting, ticket pool at capacity.");
                    }
                }
                Thread.sleep(releaseInterval * 1000L);
            }
        } catch (InterruptedException e) {
            Logger.log("Vendor " + vendorId + " interrupted.");
        }
    }
}
