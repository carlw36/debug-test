import java.util.ArrayList;
import java.util.List;

public class DebugDemo {

    private static final double TAX_RATE = 0.08;

    static class Product {
        private final String name;
        private final double price;
        private int stock;

        Product(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        String getName() {
            return name;
        }

        double getPrice() {
            return price;
        }

        int getStock() {
            return stock;
        }

        void removeFromStock(int quantity) {
            stock -= quantity;
        }

        @Override
        public String toString() {
            return name + " ($" + price + ", stock: " + stock + ")";
        }
    }

    static class CartItem {
        private final Product product;
        private final int quantity;

        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        Product getProduct() {
            return product;
        }

        int getQuantity() {
            return quantity;
        }

        double getLineTotal() {
            return product.getPrice() * quantity;
        }
    }

    static class ShoppingCart {
        private final List<CartItem> items = new ArrayList<>();

        void add(Product product, int quantity) {
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }

            if (quantity > product.getStock()) {
                System.out.println("Not enough stock for " + product.getName());
                return;
            }

            items.add(new CartItem(product, quantity));
            System.out.println("Added " + quantity + " x " + product.getName());
        }

        List<CartItem> getItems() {
            return items;
        }

        double getSubtotal() {
            double subtotal = 0;

            for (CartItem item : items) {
                subtotal += item.getLineTotal();
            }

            return subtotal;
        }

        boolean isEmpty() {
            return items.isEmpty();
        }
    }

    static class Receipt {
        private final double subtotal;
        private final double discount;
        private final double tax;
        private final double total;

        Receipt(double subtotal, double discount, double tax, double total) {
            this.subtotal = subtotal;
            this.discount = discount;
            this.tax = tax;
            this.total = total;
        }

        void print() {
            System.out.printf("Subtotal: $%.2f%n", subtotal);
            System.out.printf("Discount: $%.2f%n", discount);
            System.out.printf("Tax:      $%.2f%n", tax);
            System.out.printf("Total:    $%.2f%n", total);
        }
    }

    static class CheckoutService {

        Receipt checkout(ShoppingCart cart) {
            if (cart.isEmpty()) {
                System.out.println("Cannot check out an empty cart.");
                return null;
            }

            double subtotal = cart.getSubtotal();
            double discount = calculateDiscount(subtotal);
            double taxableAmount = subtotal;
            double tax = taxableAmount * TAX_RATE;
            double total = subtotal - discount + tax;

            updateInventory(cart);

            return new Receipt(subtotal, discount, tax, total);
        }

        private double calculateDiscount(double subtotal) {
            if (subtotal > 100) {
                return subtotal * 0.10;
            }

            if (subtotal > 50) {
                return subtotal * 0.05;
            }

            return 0;
        }

        private void updateInventory(ShoppingCart cart) {
            for (CartItem item : cart.getItems()) {
                item.getProduct().removeFromStock(item.getQuantity() - 1);
            }
        }
    }

    public static void main(String[] args) {
        Product notebook = new Product("Notebook", 12.50, 10);
        Product pen = new Product("Pen", 2.00, 20);
        Product backpack = new Product("Backpack", 55.00, 5);

        ShoppingCart cart = new ShoppingCart();
        cart.add(notebook, 2);
        cart.add(pen, 3);
        cart.add(backpack, 1);

        CheckoutService checkoutService = new CheckoutService();
        Receipt receipt = checkoutService.checkout(cart);

        System.out.println();
        if (receipt != null) {
            receipt.print();
        }

        System.out.println();
        System.out.println("Inventory after checkout:");
        System.out.println(notebook);
        System.out.println(pen);
        System.out.println(backpack);
    }
}
