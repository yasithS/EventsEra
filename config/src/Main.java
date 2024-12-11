import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static ConfigurationManager configurationManager;
    private static TicketPool ticketPool;
    private static Vendor[] vendorsArray;
    private static Customer[] customersArray;
    private static Thread[] vendorThreads;
    private static Thread[] customerThreads;

    // creating new static variables to store admin inputs
    private static int maxTicketCap;
    private static int vendorTotal;
    private static int vendorRelease;
    private static int vendorReleaseRate;
    private static int customerTotal;
    private static int numCustomersBuy;
    private static int vendorTicketBuyRate;

    public static void main(String[] args) {

        while (true) {
            printMenu();
            Scanner scanner = new Scanner(System.in);
            int adminInput = scanner.nextInt();

            configurationManager = new ConfigurationManager();

            switch (adminInput) {
                case 1:
                    System.out.println("configure system");
                    configureSystem();
                    break;
                case 2:
                    System.out.println("start simulation");
                    startSimulation();
                    break;
                case 3:
                    System.out.println("stop simulation");
                    stopSimulation();
                    break;
                case 4:
                    systemStatus();
                    break;
                case 5:
                    displayLogs();
                    break;
                case 6:
                    System.out.println("exit");
                    break;
                default:
                    System.out.println("Invalid choice. please try again");
            }
        }
    }



    private static void configureSystem(){
        Scanner scanner = new Scanner(System.in);
            try{
                System.out.print("Enter the maximum ticket capacity:  ");
                maxTicketCap = scanner.nextInt();

                System.out.println("Enter the number of vendors:  ");
                vendorTotal = scanner.nextInt();

                System.out.println("Enter the number of tickets vendor release:  ");
                vendorRelease = scanner.nextInt();

                System.out.println("Enter the vendor ticket release rate per second:  ");
                vendorReleaseRate = scanner.nextInt();

                System.out.println("Enter the number of customers:  ");
                customerTotal = scanner.nextInt();

                System.out.println("Enter the number of tickets customer buys: ");
                numCustomersBuy = scanner.nextInt();

                System.out.println("Enter the customer ticket buying rate per second: ");
                vendorTicketBuyRate = scanner.nextInt();

                configurationManager.saveConfiguration(
                        maxTicketCap,vendorTotal,vendorRelease,vendorReleaseRate,
                        customerTotal,numCustomersBuy,vendorTicketBuyRate);

                ticketPool = new TicketPool(maxTicketCap);
                vendorsArray = new Vendor[vendorTotal];
                customersArray = new Customer[customerTotal];
                vendorThreads = new Thread[vendorTotal];
                customerThreads = new Thread[customerTotal];

                System.out.println("Configure system successful");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    private static int inputValidation(Scanner scanner, int min, int max){
        int userInput;
        while (true){
            userInput = scanner.nextInt();
            if (userInput >= min && userInput <= max){
                break;
            }System.out.println("Validation error! set configuration input between "+min+" and "+ max);
        }
        return userInput;

    }


    private static void startSimulation(){

        // starting the vendor thread
        try{
            for (int i = 0; i < vendorsArray.length; i ++){
                vendorsArray[i] = new Vendor(vendorRelease, vendorReleaseRate, ticketPool);
                vendorThreads[i] = new Thread(vendorsArray[i]);
                vendorThreads[i].start();
            }

            for (int i = 0; i < customersArray.length; i ++){
                customersArray[i] = new Customer(ticketPool, vendorTicketBuyRate, numCustomersBuy);
                customerThreads[i] = new Thread(customersArray[i]);
                customerThreads[i].start();
            }
            System.out.println("simulation started successfully");
        }catch (NullPointerException e){
            System.out.println("You haven't configured the ticket pool, please configure the system first");
        }

    }

    private static void stopSimulation(){


        if(vendorThreads != null){
            for (int i = 0; i < vendorThreads.length; i ++){
                Thread vendorThread = vendorThreads[i];
                if (vendorThread != null & vendorThread.isAlive()) {
                    try{
                        vendorThread.interrupt();
                        vendorThread.join(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if (customerThreads != null){
            for (int i = 0; i < customerThreads.length; i ++){
                Thread customerThread = customerThreads[i];
                if (customerThread != null & customerThread.isAlive()) {
                    try{
                        customerThread.interrupt();
                        customerThread.join(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void printMenu(){
        System.out.println("===Event Ticketing System===\n" +
                "1. Configure System\n" +
                "2. Start Simulation\n" +
                "3. Stop Simulation\n" +
                "4. Display System Status\n" +
                "5. Display Logs\n" +
                "6. Exit\n" +
                "Enter your choice:");
    }

    public static void systemStatus(){
        try{
            if (ticketPool == null){
                System.out.println("MyEvents configuration panel needs to be configured first!\n" +
                        "please configure the system using option (1)");
            }
            System.out.println("\n===System Status===");
            System.out.println("Configuration details");
            System.out.println("--------------------");
            System.out.println("Maximum pool size: "+ ticketPool.getMaximumTicketCount());
            System.out.println("Current pool size: "+ ticketPool.getCurrentTicketCount());
            System.out.println("Available space: "+ (ticketPool.getMaximumTicketCount() - ticketPool.getCurrentTicketCount()));

            System.out.println("\nOperation Statistics");
            System.out.println("--------------------");
            System.out.println("Total tickets released by vendors: "+ ticketPool.getTotalTicketsReleased());
            System.out.println("Total tickets bought by customers: "+ ticketPool.getTotalTicketsBought());

            System.out.println("\nThread status: ");
            System.out.println("--------------------");
            if (vendorThreads != null){
                int activeVendors = 0;
                for(Thread vendor : vendorThreads){
                    if (vendor != null & vendor.isAlive()){
                        activeVendors++;
                    }
                }
                System.out.println("Active vendor threads: " +activeVendors + "/" + vendorTotal);
            }
            if (customerThreads != null){
                int activeCustomers = 0;
                for(Thread customer : customerThreads){
                    if (customer != null && customer.isAlive()){
                        activeCustomers++;
                    }
                }
                System.out.println("Active customer threads: " +activeCustomers + "/" + customerTotal+"\n");
            }

        } catch (Exception e) {
            System.out.println("error occurred while displaying system status "+ e.getMessage());
        }
    }

    public static void displayLogs(){

        File logFile = new File("logs.txt");

        if (!logFile.exists()){
            System.out.println("No log file found");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))){
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }catch (IOException e){
            System.out.println("Error reading file: "+e.getMessage());
        }



    }

}