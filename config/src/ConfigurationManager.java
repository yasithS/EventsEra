import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigurationManager {

    private Connection connection;
    private Configuration currnetConfiguration;

    public ConfigurationManager(){
        this.connection = DBConnection.createDBConnection();
    }

    // save configuration data into the database
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

            // update configuration
            this.currnetConfiguration = new Configuration(
                    maxTicketPool, totalVendors, releasePerVendor, vendorReleaseRate,
                    totalCustomers, ticketsPerCustomer, customerBuyingRate
            );
            System.out.println("Admin configuration data saved to the database!");
        }
    }

    public Configuration getCurrnetConfiguration() {
        if (currnetConfiguration == null){
            System.out.println("Current configuration not found!");
        }
        return currnetConfiguration;
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

    }









}
