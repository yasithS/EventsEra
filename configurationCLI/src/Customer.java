public class Customer implements Runnable{

    private TicketPool ticketPool;
    private int customerRetrivelRate;
    private int Qty;

    public Customer(TicketPool ticketPool, int customerRetrivelRate, int Qty) {
        this.ticketPool = ticketPool;
        this.customerRetrivelRate = customerRetrivelRate;
        this.Qty = Qty;
    }

    public void run() {
        for (int i = 0; i < Qty; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return; // Exit the loop if interrupted
            }
            ticketPool.removeTicket();
            try {
                Thread.sleep(customerRetrivelRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                return; // Exit the thread
            }
        }
    }
}
