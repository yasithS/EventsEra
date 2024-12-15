import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketPool {

    private int maximumTicketCount;
    private Queue<Ticket> ticketQueue;
    private FileWriter fileWriter;

    private static final DateTimeFormatter currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // using volatile keyword to ensure memory visibility through threads.
    private volatile int totalTicketsReleased;
    private volatile int totalTicketsBought;

    public TicketPool(int maximumTicketCount) {
        this.maximumTicketCount = maximumTicketCount;
        ticketQueue = new LinkedList<>();
    }

    public synchronized void addTicket(Ticket ticket) {
        while (ticketQueue.size() >= maximumTicketCount) {
            try{
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                return; // Exit the method
            }
        }
        this.ticketQueue.add(ticket);
        totalTicketsReleased ++; // increasing the total tickets released by vendors by 1

        // String logMessage ="[LOG] Vendor"+Thread.currentThread().getName() + " added ticket to the system. Ticket pool updated; current size " + ticketQueue.size();
        String logMessage = "[LOG]  | " + LocalDateTime.now() + " | VENDOR | " + "Vendor"+Thread.currentThread().getName() + " | Added ticket \n" +
                "[INFO] | Current pool size "+ ticketQueue.size() +" of "+maximumTicketCount;


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
                Thread.currentThread().interrupt(); // Restore interrupted status
                return null; // Exit the method
            }
        }

        Ticket ticket = ticketQueue.poll();
        totalTicketsBought ++; // increasing the total tickets bought by customers by 1
        // String removeLog = Thread.currentThread().getName() + " bought ticket from the system. current size: " + ticketQueue.size() ;

        String removeLog = "[LOG]  | " + LocalDateTime.now()  + " | CUSTOMER | "+ "customer"+Thread.currentThread().getName()+ " | purchased ticket \n"+
                "[INFO] | Current pool size "+ticketQueue.size() + "/"+maximumTicketCount;


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

    public synchronized int getCurrentTicketCount() {
        return ticketQueue.size();
    }

    public synchronized int getTotalTicketsReleased(){
        return totalTicketsReleased;
    }

    public synchronized int getTotalTicketsBought(){
        return totalTicketsBought;
    }

    public synchronized  int getMaximumTicketCount(){
        return maximumTicketCount;
    }


}

