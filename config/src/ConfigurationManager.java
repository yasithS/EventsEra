import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigurationManager {

    private Connection connection;

    public ConfigurationManager(){
        this.connection = DBConnection.createDBConnection();
    }

    public void saveConfiguration(int maxTicketPool,
                                  int totalVendors, int releasePerVendor, int vendorReleaseRate,
                                  int totalCustomers, int ticketsPerCustomer, int customerBuyingRate) throws SQLException {
        String insertConfigQuery = "INSERT INTO adminConfigurations (Maximum_Pool_Size, Total_Vendors, Release_Per_Vendor, " +
                "Vendor_Release_Rate, Total_Customers, Tickets_Per_Customer, Customer_Buying_Rate) VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement InsertStatement = connection.prepareStatement(insertConfigQuery)){
            InsertStatement.setInt(1, maxTicketPool);
            InsertStatement.setInt(2, totalVendors);
            InsertStatement.setInt(3, releasePerVendor);
            InsertStatement.setInt(4, vendorReleaseRate);
            InsertStatement.setInt(5, totalCustomers);
            InsertStatement.setInt(6, ticketsPerCustomer);
            InsertStatement.setInt(7, customerBuyingRate);
            InsertStatement.executeUpdate();
            System.out.println("Admin configuration data saved to the database!");
        }
    }

    public class Configuration {
        private int maxTicketCap;
        private int vendorTotal;
        private int vendorRelease;
        private int vendorReleaseRate;
        private int customerTotal;
        private int numCustomersBuy;
        private int vendorTicketBuyRate;

        public Configuration(
                int maxTicketCap,
                int vendorTotal,
                int vendorRelease,
                int vendorReleaseRate,
                int customerTotal,
                int numCustomersBuy,
                int vendorTicketBuyRate) {
            this.maxTicketCap = maxTicketCap;
            this.vendorTotal = vendorTotal;
            this.vendorRelease = vendorRelease;
            this.vendorReleaseRate = vendorReleaseRate;
            this.customerTotal = customerTotal;
            this.numCustomersBuy = numCustomersBuy;
            this.vendorTicketBuyRate = vendorTicketBuyRate;
        }

        // Getters
        public int getMaxTicketCap() { return maxTicketCap; }
        public int getVendorTotal() { return vendorTotal; }
        public int getVendorRelease() { return vendorRelease; }
        public int getVendorReleaseRate() { return vendorReleaseRate; }
        public int getCustomerTotal() { return customerTotal; }
        public int getNumCustomersBuy() { return numCustomersBuy; }
        public int getVendorTicketBuyRate() { return vendorTicketBuyRate; }
    }









}
