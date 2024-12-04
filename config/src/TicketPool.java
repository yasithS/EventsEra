import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {

    private int maximumTicketCount;
    private Queue<Ticket> ticketQueue;
    private FileWriter fileWriter;

    public TicketPool(int maximumTicketCount) {
        this.maximumTicketCount = maximumTicketCount;
        ticketQueue = new LinkedList<>();
    }

    public synchronized void addTicket(Ticket ticket) {
        while (ticketQueue.size() >= maximumTicketCount) {
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        this.ticketQueue.add(ticket);
        String logMessage = Thread.currentThread().getName() + " added ticket to the system. Ticket pool updated; current size " + ticketQueue.size();
        System.out.println(logMessage);

        // updating the txt file
        try {
            this.fileWriter = new FileWriter("logs.txt", true);
        } catch (IOException e) {
            System.err.println("Could not create log file: " + e.getMessage());
            e.printStackTrace();
        }

        try{
            if (fileWriter != null) {
                fileWriter.write(logMessage+"\n");
                fileWriter.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        notifyAll();

    }

    public synchronized Ticket removeTicket() {
        while (ticketQueue.isEmpty()) {
            try{
                wait();
            }catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

        Ticket ticket = ticketQueue.poll();
        String removeLog = Thread.currentThread().getName() + " bought ticket from the system. current size: " + ticketQueue.size() +" Ticket is "+ ticket;
        System.out.println(removeLog);

        // updating the txt file
        try {
            this.fileWriter = new FileWriter("logs.txt", true);
        } catch (IOException e) {
            System.err.println("Could not create log file: " + e.getMessage());
            e.printStackTrace();
        }

        try{
            if (fileWriter != null) {
                fileWriter.write(removeLog+"\n");
                fileWriter.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        notifyAll();
        return ticket;
    }

}
