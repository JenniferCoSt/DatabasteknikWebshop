import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Repository {

    private Properties props = new Properties();

    public List<Customer> getCustomers() throws IOException {
        props.load(new FileInputStream("src/settings.properties"));

        List<Customer> customers = new ArrayList<Customer>();

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, firstName, email, customerPassword from customer")) {


            while (rs.next()) {
                Customer customer = new Customer();
                int id = rs.getInt("id");
                customer.setCustomerId(id);
                String fname = rs.getString("firstName");
                customer.setFirstName(fname);
                String email = rs.getString("email");
                customer.setEmail(email);
                String password = rs.getString("customerPassword");
                customer.setPassword(password);
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return customers;
    }

    public PurchaseOrder getActiveOrder(int customerId) throws IOException {
        props.load(new FileInputStream("src/settings.properties"));

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement("SELECT orderNumber, orderDate, isActive from PurchaseOrder where CustomerId = ? and isActive = true");) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PurchaseOrder activeOrder = new PurchaseOrder();
                int orderNumber = rs.getInt("orderNumber");
                activeOrder.setOrderNumber(orderNumber);
                activeOrder.setCustomerId(customerId);
                LocalDate orderDate = rs.getDate("orderDate").toLocalDate();
                activeOrder.setOrderDate(orderDate);
                boolean isActive = rs.getBoolean("isActive");
                activeOrder.setActive(isActive);

                return activeOrder;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public void createNewOrder(int customerId) throws IOException {
        props.load(new FileInputStream("src/settings.properties"));

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement("INSERT INTO PurchaseOrder(customerId, isActive) values (?, true);");) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<Integer> getOrderDetails(int purchaseOrderNumber) throws IOException {
        props.load(new FileInputStream("src/settings.properties"));
        List<Integer> shoppingCart = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement("SELECT id, productTypeId, amount from OrderDetails where PurchaseOrderNumber = ?");) {
            stmt.setInt(1, purchaseOrderNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderDetails orderDetails = new OrderDetails();
                int id = rs.getInt("id");
                orderDetails.setId(id);
                orderDetails.setPurchaseOrderNumber(purchaseOrderNumber);
                int productTypeId = rs.getInt("productTypeId");
                orderDetails.setProductTypeId(productTypeId);
                int amount = rs.getInt("amount");
                orderDetails.setAmount(amount);

                shoppingCart.add(productTypeId);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return shoppingCart;
    }

    public List<Product> getProducts() throws IOException {
        props.load(new FileInputStream("src/settings.properties"));
        List<Product> shoes = new ArrayList<Product>();

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, brand, model, color, size, price from Shoes")) {

            while (rs.next()) {
                Product shoe = new Product();
                int id = rs.getInt("id");
                shoe.setId(id);
                String brand = rs.getString("brand");
                shoe.setBrand(brand);
                String model = rs.getString("model");
                shoe.setModel(model);
                String color = rs.getString("color");
                shoe.setColor(color);
                int size = rs.getInt("size");
                shoe.setSize(size);
                int price = rs.getInt("price");
                shoe.setPrice(price);
                shoes.add(shoe);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return shoes;
    }

    public String addToCart(int customerId, Integer orderId, int productId) throws IOException {
        props.load(new FileInputStream("src/settings.properties"));

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             CallableStatement stmt = con.prepareCall("{call AddToCart(?, ?, ?, ?)}")) {

            stmt.setInt(1, customerId);
            if (orderId != null) {
                stmt.setInt(2, orderId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, productId);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();

            return stmt.getString(4);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public boolean updateOrderStatus(int orderNumber, int customerId) throws IOException {
        props.load(new FileInputStream("src/settings.properties"));

        try (Connection con = DriverManager.getConnection(
                props.getProperty("connectionString"),
                props.getProperty("username"),
                props.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement("UPDATE PurchaseOrder set isActive = false where orderNumber = ? and CustomerId = ?");) {
            stmt.setInt(1, orderNumber);
            stmt.setInt(2, customerId);
            int update = stmt.executeUpdate();

            if (update > 0) {
                System.out.println("Din beställning är mottagen!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}
