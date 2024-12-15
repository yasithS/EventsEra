import java.math.BigDecimal;

public class Ticket {

    private int TicketId;
    private String eventName;
    private BigDecimal ticketPrice;

    public Ticket(int ticketId, String eventName, BigDecimal ticketPrice) {
        this.TicketId = ticketId;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "TicketId=" + TicketId +
                ", eventName='" + eventName + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
