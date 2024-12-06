package org.example.ticketing;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerId;

    public Customer(TicketPool ticketPool, int customerId) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        try {
            synchronized (ticketPool) {
                while (true) {
                    if (ticketPool.allTicketsSold()) {
                        Logger.log("No tickets left to purchase. Customer-" + customerId + " is stopping.");
                        break;
                    }

                    String ticket = ticketPool.removeTicket(String.valueOf(customerId));
                    if (ticket != null) {
                        Logger.log("Customer-" + customerId + " purchased: " + ticket);
                        break; // Exit after successful purchase
                    } else {
                        Logger.log("Customer-" + customerId + " is waiting: Ticket pool is empty...");
                    }
                }
            }
            Thread.sleep(1000L); // Simulate customer retrieval interval
        } catch (InterruptedException e) {
            Logger.log("Customer-" + customerId + " interrupted.");
        }
    }
}
