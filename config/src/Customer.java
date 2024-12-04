public class Customer implements Runnable{

    private TicketPool ticketPool;
    private int customerRetrivelRate;
    private int Qty;

    public Customer(TicketPool ticketPool, int customerRetrivelRate, int Qty) {
        this.ticketPool = ticketPool;
        this.customerRetrivelRate = customerRetrivelRate;
        this.Qty = Qty;
    }

    public void run(){
        for(int i = 0; i < Qty; i++){
            Ticket ticket = ticketPool.removeTicket();
            System.out.println("Ticket - "+ ticket + "Customer name is "+Thread.currentThread().getName() );
            try {
                Thread.sleep(customerRetrivelRate*1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
