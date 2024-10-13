// Класс Product представляет товар с идентификатором, названием и ценой
class Product {
    String id;
    String name;
    int price;

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

// Исключение для случая, если товар не найден
class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

// Исключение для случая сбоя оплаты
class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}

// Подсистема каталога товаров
class ProductCatalog {
    public Product findProduct(String productId) throws ProductNotFoundException {
        System.out.println("ProductCatalog: Поиск товара " + productId);
        if (productId.equals("NOT_FOUND")) {
            throw new ProductNotFoundException("Товар с ID " + productId + " не найден.");
        }
        return new Product(productId, "Onim Ulgisi", 100);
    }
}

// Подсистема обработки платежей
class PaymentProcessor {
    public boolean processPayment(int amount) throws PaymentFailedException {
        System.out.println("PaymentProcessor: Обработка платежа на сумму $" + amount);
        if (amount <= 0) {
            throw new PaymentFailedException("Сумма платежа должна быть больше 0.");
        }
        return true;
    }
}

// Подсистема управления запасами
class InventoryManager {
    public boolean checkInventory(String productId) {
        System.out.println("InventoryManager: Проверка наличия товара " + productId);
        return true; // Товар в наличии
    }

    public void updateInventory(String productId) {
        System.out.println("InventoryManager: Обновление запасов для товара " + productId);
    }
}

// Подсистема расчета и обработки доставки
class ShippingService {
    public int calculateShipping(String productId) {
        System.out.println("ShippingService: Рассчет доставки для товара " + productId);
        return 10; // Стоимость доставки
    }

    public void shipProduct(String productId) {
        System.out.println("ShippingService: Отправка товара " + productId);
    }
}

// Подсистема управления скидками
class DiscountManager {
    public int applyDiscount(String productId, int price) {
        // Пример: применение фиксированной скидки 10%
        System.out.println("DiscountManager: Применение скидки для товара " + productId);
        return (int) (price * 0.9); // 10% скидка
    }
}

// Подсистема отслеживания заказа
class OrderTrackingService {
    public void trackOrder(String productId) {
        System.out.println("OrderTrackingService: Отслеживание заказа для товара " + productId);
    }
}

// Фасад для управления процессом покупки
class ShoppingFacade {
    private ProductCatalog productCatalog;
    private PaymentProcessor paymentProcessor;
    private InventoryManager inventoryManager;
    private ShippingService shippingService;
    private DiscountManager discountManager;
    private OrderTrackingService orderTrackingService;

    public ShoppingFacade() {
        this.productCatalog = new ProductCatalog();
        this.paymentProcessor = new PaymentProcessor();
        this.inventoryManager = new InventoryManager();
        this.shippingService = new ShippingService();
        this.discountManager = new DiscountManager();
        this.orderTrackingService = new OrderTrackingService();
    }

    public void placeOrder(String productId) {
        try {
            // Найти товар
            Product product = productCatalog.findProduct(productId);

            // Применить скидку
            int discountedPrice = discountManager.applyDiscount(productId, product.price);

            // Проверить запасы
            if (!inventoryManager.checkInventory(productId)) {
                System.out.println("Ошибка: товар отсутствует на складе.");
                return;
            }

            // Обработать платеж
            if (!paymentProcessor.processPayment(discountedPrice)) {
                System.out.println("Ошибка: платеж не прошел.");
                return;
            }

            // Обновить запасы
            inventoryManager.updateInventory(productId);

            // Рассчитать доставку и отправить товар
            int shippingCost = shippingService.calculateShipping(productId);
            System.out.println("Итоговая стоимость: $" + (discountedPrice + shippingCost));
            shippingService.shipProduct(productId);

            // Отслеживание заказа
            orderTrackingService.trackOrder(productId);

            System.out.println("Заказ успешно оформлен!");

        } catch (ProductNotFoundException | PaymentFailedException e) {
            System.out.println("Ошибка при оформлении заказа: " + e.getMessage());
        }
    }
}

// Тестирование системы
 class ShoppingFacadeExample {
    public static void main(String[] args) {
        ShoppingFacade shoppingFacade = new ShoppingFacade();
        shoppingFacade.placeOrder("AITU2024"); // Пример успешного заказа
        shoppingFacade.placeOrder("NOT_FOUND"); // Пример ошибки при поиске товара
    }
}
