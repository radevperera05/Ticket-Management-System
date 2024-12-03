package org.example.ticketing;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval;

    public Customer(TicketPool ticketPool, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticketPool) {
                    if (ticketPool.allTicketsSold()) {
                        System.out.println("No tickets left to purchase. Customer is stopping.");
                        break;
                    }

                    String ticket = ticketPool.removeTicket();
                    if (ticket != null) {
                        System.out.println("Customer purchased: " + ticket);
                    } else {
                        System.out.println("No tickets available in the pool.");
                    }
                }
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("Customer interrupted.");
        }
    }
}
