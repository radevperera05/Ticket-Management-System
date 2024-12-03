package org.example.ticketing;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval;
    private final int customerId;

    public Customer(TicketPool ticketPool, int retrievalInterval, int customerId) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        try {
            synchronized (ticketPool) {
                while (true) {
                    if (ticketPool.allTicketsSold() && ticketPool.getRemainingTicketsToRelease() == 0) {
                        Logger.log("No tickets left to purchase. Customer " + customerId + " is stopping. \n");
                        break;
                    }

                    String ticket = ticketPool.removeTicket();
                    if (ticket != null) {
                        Logger.log("Customer " + customerId + " purchased ticket: " + ticket + "\n");
                        break;
                    } else {
                        Logger.log("Customer " + customerId + " is waiting for a ticket... \n");
                    }
                }
            }
            Thread.sleep(retrievalInterval * 1000L);
        } catch (InterruptedException e) {
            Logger.log("Customer " + customerId + " interrupted.");
        }
    }
}
