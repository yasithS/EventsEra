import javax.security.auth.login.Configuration;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner configInput = new Scanner(System.in);
        ConfigurationManager configurationManager = new ConfigurationManager();

        System.out.print("Enter the maximum ticket capacity:  ");
        int maxTicketCap = configInput.nextInt();

        System.out.println("Enter the number of vendors:  ");
        int vendorTotal = configInput.nextInt();

        System.out.println("Enter the number of tickets vendor release:  ");
        int vendorRelease = configInput.nextInt();

        System.out.println("Enter the vendor ticket release rate per second:  ");
        int vendorReleaseRate = configInput.nextInt();

        System.out.println("Enter the number of customers:  ");
        int customerTotal = configInput.nextInt();

        System.out.println("Enter the number of tickets customer buys: ");
        int numCustomersBuy = configInput.nextInt();

        System.out.println("Enter the customer ticket buying rate per second: ");
        int vendorTicketBuyRate = configInput.nextInt();

        try{
            configurationManager.saveConfiguration(
                    maxTicketCap,vendorTotal,vendorRelease,vendorReleaseRate,customerTotal,numCustomersBuy,vendorTicketBuyRate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // creating the ticket pool
        TicketPool eventsEra = new TicketPool(maxTicketCap);

        // array of vendors using configuration inputs
        Vendor[] vendorsArray = new Vendor[vendorTotal];
        for (int currentVendor = 0; currentVendor < vendorTotal; currentVendor++) {
            vendorsArray[currentVendor] = new Vendor(vendorRelease, vendorReleaseRate, eventsEra);
            Thread vendorsThread = new Thread(vendorsArray[currentVendor], "Vendor Id"+currentVendor);
            vendorsThread.start();
        }

        // array of customers using configuration inputs
        Customer[] customersArray = new Customer[customerTotal];
        for (int currentCustomer = 0; currentCustomer < customerTotal; currentCustomer++) {
            customersArray[currentCustomer] = new Customer(eventsEra,vendorTicketBuyRate, numCustomersBuy);
            Thread customersThread = new Thread(customersArray[currentCustomer], "Customer Id"+currentCustomer);
            customersThread.start();
        }



    }
}