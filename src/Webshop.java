import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Webshop {

    private Scanner input = new Scanner(System.in);
    private Repository rep = new Repository();
    private Boolean shopping = true;
    private Customer customer;
    private PurchaseOrder activeOrder;
    private List<Product> shoes;
    private List<Product> shoppingCart = new ArrayList<Product>();


    public void run() throws IOException, InterruptedException {

        System.out.println("Välkommen till Shoeshoe shop!\nVar god och logga in med din emailadress nedan");
        String email = input.nextLine();
        System.out.println("Var god skriv in ditt lösenord nedan");
        String password = input.nextLine();
        if (login(email.trim(), password.trim())) {
            shoes = rep.getProducts();
            System.out.println("Välkommen " + customer.getFirstName() + "!");
            if (rep.getActiveOrder(customer.getCustomerId()) != null) {
                activeOrder = rep.getActiveOrder(customer.getCustomerId());
                loadShoppingCart(rep.getOrderDetails(activeOrder.getOrderNumber()));
            } else {
                rep.createNewOrder(customer.getCustomerId());
                activeOrder = rep.getActiveOrder(customer.getCustomerId());
            }

            while (shopping) {
                System.out.println("Nedan ser du vilka skor vi har inne för tillfället.\nAnvänd siffror " +
                        "för att välja skor att lägga i varukorgen.\nOm du vill betala, välj 0.\nOm du" +
                        " vill avsluta, välj -1.");
                showShoes();
                try {
                    int chosenShoes = Integer.parseInt(input.nextLine());
                    if (chosenShoes == 0) {
                        if (!shoppingCart.isEmpty()) {
                            pay();
                        } else {
                            System.out.println("Varukorgen är tom! Du behöver välja ett par skor!");
                            Thread.sleep(2000);
                        }
                    } else if (chosenShoes == -1) {
                        System.out.println("Tack för ditt besök! Välkommen åter!");
                        shopping = false;
                    } else if (chosenShoes < -1 || chosenShoes >= shoes.size()) {
                        System.out.println("Felaktigt val. Försök igen!");
                    } else {
                        String result;
                        Product theChosenShoes = getShoe(chosenShoes);
                        if (activeOrder != null) {
                            result = rep.addToCart(customer.getCustomerId(), activeOrder.getOrderNumber(), theChosenShoes.getId());
                            System.out.println(result);
                        } else {
                            result = rep.addToCart(customer.getCustomerId(), null, theChosenShoes.getId());
                            System.out.println(result);
                        }
                        if (result.equalsIgnoreCase("Varan har lagts till i din varukorg!")) {
                            shoppingCart.add(theChosenShoes);
                        } else if (result.equalsIgnoreCase("Produkten finns ej i lager")) {
                            shoes.remove(theChosenShoes);
                        }

                        Thread.sleep(2000);
                    }
                } catch (NumberFormatException n) {
                    System.out.println("Ogiltigt val. Du måste ange en siffra.");
                    Thread.sleep(2000);
                    continue;
                }
            }

        } else {
            System.out.println("Inloggning misslyckades! Kolla att du skrivit in rätt email/lösenord.");
        }
    }

    public boolean login(String email, String password) throws IOException {
        List<Customer> allCustomers = rep.getCustomers();
        for (Customer c : allCustomers) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                customer = c;
                return true;
            }
        }
        return false;
    }

    public void showShoes() {
        for (int i = 0; i < shoes.size(); i++) {
            System.out.println((i + 1) + ". " + shoes.get(i).getBrand() + ", " + shoes.get(i).getModel() +
                    ", " + shoes.get(i).getColor() + ", " + shoes.get(i).getSize()
                    + " " + shoes.get(i).getPrice() + ":- ");
        }
    }

    public Product getShoe(int chosenShoes) {
        return shoes.get(chosenShoes - 1);
    }

    public void pay() throws InterruptedException, IOException {
        int totalPrice = 0;
        boolean paying = true;
        System.out.println("Dessa produkter ligger i varukorgen:");

        for (Product p : shoppingCart) {
            System.out.println(p.getBrand() + ", " + p.getModel() + ", " + p.getColor() +
                    ", " + p.getSize() + " " + p.getPrice() + ":-");
            totalPrice += p.getPrice();
        }

        System.out.println("Total pris: " + totalPrice + ":-\nVill du betala? Svara med ja eller avbryt");
        String paymentChoice = input.nextLine();

        while (paying) {
            if (paymentChoice.trim().equalsIgnoreCase("ja")) {
                if (rep.updateOrderStatus(activeOrder.getOrderNumber(), customer.getCustomerId())) {
                    activeOrder.setActive(false);
                    System.out.println("Tack för din beställning! Faktura skickas inom kort!");
                    shopping = false;
                    paying = false;
                } else {
                    System.out.println("Något gick fel, försök igen!");
                    break;
                }
            } else if (paymentChoice.trim().equalsIgnoreCase("avbryt")) {
                System.out.println("Betalning avbröts");
                Thread.sleep(2000);
                paying = false;
            } else {
                System.out.println("Ogiltigt val. Försök igen!");
                input.nextLine();
            }
        }
    }

    public void loadShoppingCart(List<Integer> loadShoppingCart) {
        for (Integer id : loadShoppingCart) {
            for (Product shoe : shoes) {
                if (id == shoe.getId()) {
                    shoppingCart.add(shoe);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Webshop webshop = new Webshop();
        webshop.run();
    }
}