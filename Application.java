import java.util.*;

class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class ProductService {
    private List<Product> products;

    public ProductService() {
        // Initialize products (you may load them from a database or other source)
        this.products = new ArrayList<>();
        products.add(new Product(1, "Product1", 19.99));
        products.add(new Product(2, "Product2", 29.99));
        products.add(new Product(3, "Product3", 39.99));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

class CartService {
    private final Map<Product, Integer> cartItems = new HashMap<>();

    public void addToCart(Product product, int quantity) {
        cartItems.put(product, cartItems.getOrDefault(product, 0) + quantity);
    }

    public Map<Product, Integer> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
}

class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public Product getProductById(int id) {
        return productService.getProductById(id);
    }
}

class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    public void addToCart(Product product, int quantity) {
        cartService.addToCart(product, quantity);
    }

    public Map<Product, Integer> getCartItems() {
        return cartService.getCartItems();
    }

    public void clearCart() {
        cartService.clearCart();
    }

    public double calculateTotal() {
        return cartService.calculateTotal();
    }
}

public class Application {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        ProductController productController = new ProductController(productService);

        CartService cartService = new CartService();
        CartController cartController = new CartController(cartService);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nE-commerce Platform");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    displayProducts(productController.getAllProducts());
                    break;
                case 2:
                    addToCart(scanner, productController, cartController);
                    break;
                case 3:
                    displayCart(cartController);
                    break;
                case 4:
                    checkout(cartController);
                    break;
                case 5:
                    System.out.println("Exiting E-commerce Platform. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void displayProducts(List<Product> products) {
        System.out.println("\nProducts Available:");
        for (Product product : products) {
            System.out.println(product.getId() + ". " + product.getName() + " - $" + product.getPrice());
        }
    }

    private static void addToCart(Scanner scanner, ProductController productController, CartController cartController) {
        displayProducts(productController.getAllProducts());
        System.out.print("Enter the product ID to add to your cart: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        Product product = productController.getProductById(productId);

        if (product != null) {
            System.out.print("Enter the quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            cartController.addToCart(product, quantity);
            System.out.println(quantity + " " + product.getName() + "(s) added to the cart.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void displayCart(CartController cartController) {
        Map<Product, Integer> cartItems = cartController.getCartItems();
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nYour Cart:");
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                System.out.println(entry.getKey().getName() + " - Quantity: " + entry.getValue());
            }
            System.out.println("Total: $" + cartController.calculateTotal());
        }
    }

    private static void checkout(CartController cartController) {
        displayCart(cartController);
        System.out.print("Proceed to checkout? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().toLowerCase();

        if (choice.equals("yes")) {
            System.out.println("Checkout completed. Thank you for shopping with us!");
            cartController.clearCart();
        } else {
            System.out.println("Checkout cancelled.");
        }
    }
}
