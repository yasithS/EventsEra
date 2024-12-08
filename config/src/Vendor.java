import java.math.BigDecimal;

public class Vendor implements  Runnable{

    private int totalTickets;
    private int ticketReleaseRate;
    private TicketPool ticketPool;

    public Vendor(int totalTickets, int ticketReleaseRate, TicketPool ticketPool) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
    }

    public void run(){
        for (int i = 1 ; i < totalTickets; i++) {
            Ticket ticket = new Ticket(i, "API_KAWURUDA_WAYO", new BigDecimal("3000"));
            ticketPool.addTicket(ticket);
            try{
                Thread.sleep(ticketReleaseRate * 1000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
