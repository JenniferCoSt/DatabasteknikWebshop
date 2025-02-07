import java.time.LocalDate;

public class PurchaseOrder {
    private int orderNumber;
    private int customerId;
    private LocalDate orderDate;
    private boolean isActive;

    public PurchaseOrder() {
    }

    ;

    public PurchaseOrder(int customerId, boolean isActive) {
        this.customerId = customerId;
        this.isActive = isActive;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
