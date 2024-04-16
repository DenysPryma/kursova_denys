import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static List<Equipment> equipmentList = new ArrayList<>();
    private static List<EquipmentRental> rentals = new ArrayList<>();
    private static User currentUser;


    public static void main(String[] args) {
        equipmentList.add(new Equipment("Велосипед", "1", 100.0));
        equipmentList.add(new Equipment("Мотоцикл", "2", 200.0));
        equipmentList.add(new Equipment("Лижі", "5", 600.0));
        equipmentList.add(new Equipment("Сноуборд", "4", 400.0));
        equipmentList.add(new Equipment("Ролики", "3", 500.0));

        users.put("Denys", new User("Denys", "1234"));


        boolean exit = false;
        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Реєстрація");
            System.out.println("2. Вхід");
            System.out.println("3. Переглянути список спорядження");
            System.out.println("4. Додати спорядження");
            System.out.println("5. Видалити спорядження");
            System.out.println("6. Орендувати спорядження");
            System.out.println("7. Переглянути орендоване спорядження");
            System.out.println("8. Вийти з програми");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    displayEquipment();
                    break;
                case 4:
                    addEquipment();
                    break;
                case 5:
                    removeEquipment();
                    break;
                case 6:
                    rentEquipment();
                    break;
                case 7:
                    displayRentedEquipment();
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("Введіть ім'я користувача:");
        String username = scanner.next();
        System.out.println("Введіть пароль:");
        String password = scanner.next();
        users.put(username, new User(username, password));
        System.out.println("Реєстрація успішна.");
        currentUser = users.get(username);
    }

    private static void loginUser() {
        System.out.println("Введіть ім'я користувача:");
        String username = scanner.next();
        System.out.println("Введіть пароль:");
        String password = scanner.next();
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            System.out.println("Вхід успішний.");
        } else {
            System.out.println("Невірний ім'я користувача або пароль.");
        }
    }

    private static void displayEquipment() {
        System.out.println("Оберіть критерій сортування:");
        System.out.println("1. За назвою");
        System.out.println("2. За типом");
        System.out.println("3. За ціною");
        int sortBy = scanner.nextInt();

        switch (sortBy) {
            case 1:
                Collections.sort(equipmentList, Comparator.comparing(Equipment::getName));
                break;
            case 2:
                Collections.sort(equipmentList, Comparator.comparing(Equipment::getType));
                break;
            case 3:
                Collections.sort(equipmentList, Comparator.comparingDouble(Equipment::getPrice));
                break;
            default:
                System.out.println("Невірний вибір. Спробуйте ще раз.");
                return;
        }

        System.out.println("Список спорядження:");
        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment equipment = equipmentList.get(i);
            System.out.println(i + ". " + equipment.getName() + " - " + equipment.getType() + " - " + equipment.getPrice());
        }
    }

    private static void addEquipment() {
        System.out.println("Введіть назву спорядження:");
        String name = scanner.next();
        System.out.println("Введіть тип спорядження:");
        String type = scanner.next();
        System.out.println("Введіть ціну спорядження:");
        double price = scanner.nextDouble();
        equipmentList.add(new Equipment(name, type, price));
        System.out.println("Спорядження успішно додано.");
    }

    private static void removeEquipment() {
        System.out.println("Список спорядження:");
        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment equipment = equipmentList.get(i);
            System.out.println(i + ". " + equipment.getName() + " - " + equipment.getType() + " - " + equipment.getPrice());
        }
        System.out.println("Введіть номер спорядження, яке потрібно видалити:");
        int index = scanner.nextInt();
        if (index >= 0 && index < equipmentList.size()) {
            equipmentList.remove(index);
            System.out.println("Спорядження успішно видалено.");
        } else {
            System.out.println("Невірний номер спорядження.");
        }
    }

    private static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber.length() != 19) {
            return false;
        }
        for (int i = 0; i < cardNumber.length(); i++) {
            if ((i + 1) % 5 == 0) {
                if (cardNumber.charAt(i) != '-') {
                    return false;
                }
            } else {
                if (!Character.isDigit(cardNumber.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    private static void rentEquipment() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            System.out.println("Будь-ласка увійдіть в аккаунт.");
            return;
        }

        double totalCost = 0.0;
        do {
            displayEquipment();
            int index = getEquipmentIndex();
            if (index == -1) {
                System.out.println("Невірний номер спорядження.");
                return;
            }

            int duration = getRentalDuration();
            if (duration == -1) {
                System.out.println("Невірна тривалість оренди.");
                return;
            }

            double rentCost = calculateRentCost(index, duration);
            rentals.add(new EquipmentRental(currentUser, equipmentList.get(index), duration));
            System.out.println("Оренда успішно оформлена.");
            System.out.println("Вартість оренди: " + rentCost);
            totalCost += rentCost;

            System.out.println("Бажаєте орендувати ще щось? (так/ні)");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("так")) {
                continue;
            } else if (choice.equalsIgnoreCase("ні")) {
                System.out.println("Сума оренди: " + totalCost);
                displayReceipt();
                processPayment();
                return;
            } else {
                System.out.println("Невірний вибір. Спробуйте ще раз.");
                return;
            }
        } while (true);
    }


    private static int getEquipmentIndex() {
        System.out.println("Введіть номер спорядження для оренди:");
        int index = scanner.nextInt();
        return (index >= 0 && index < equipmentList.size()) ? index : -1;
    }

    private static int getRentalDuration() {
        System.out.println("Введіть тривалість оренди в днях:");
        int duration = scanner.nextInt();
        return (duration > 0) ? duration : -1;
    }

    private static double calculateRentCost(int index, int duration) {
        return equipmentList.get(index).getPrice() * duration;
    }


    private static void processPayment() {
        System.out.println("Оберіть метод оплати: готівкою або картою");
        String paymentMethod;

        do {
            paymentMethod = scanner.next();
            if (paymentMethod.equalsIgnoreCase("готівка")) {
                System.out.println("Оплачено готівкою.");
                break;
            } else if (paymentMethod.equalsIgnoreCase("карта")) {
                processCardPayment();
                break;
            } else {
                System.out.println("Невірний метод оплати. Введіть 'готівка' або 'карта'.");
            }
        } while (true);
    }

    private static void processCardPayment() {
        System.out.println("Оплачено карткою.");
        System.out.println("Введіть номер карти:");
        String cardNumber = scanner.next();
        if (isValidCardNumber(cardNumber)) {
            System.out.println("Введіть CVV код:");
            String cvv = scanner.next();
            System.out.println("Платіж успішно здійснено.");
            displayReceipt();
        } else {
            System.out.println("Невірний формат номеру карти. Повторіть спробу.");
            processCardPayment();
        }
    }

    private static User getCurrentUser() {
        if (currentUser != null) {
            return currentUser;
        } else {
            System.out.println("Будь-ласка увійдіть в аккаунт.");
            System.out.println("Введіть ім'я користувача:");
            String username = scanner.next();
            System.out.println("Введіть пароль:");
            String password = scanner.next();
            User user = users.get(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                return currentUser;
            } else {
                return null;
            }
        }
    }
    private static void displayReceipt() {
        System.out.println("Чек:");
        double totalCost = 0.0;

        for (EquipmentRental rental : rentals) {
            User user = rental.getUser();
            if (!user.equals(currentUser)) continue;

            Equipment equipment = rental.getEquipment();
            int duration = rental.getDuration();
            double cost = equipment.getPrice() * duration;
            System.out.println("Назва: " + equipment.getName() + ", Тип: " + equipment.getType() + ", Ціна: " + equipment.getPrice() + ", Тривалість: " + duration + " днів, Вартість: " + cost);
            totalCost += cost;
        }
        System.out.println("Загальна вартість: " + totalCost);
    }



    private static double calculateTotalCost() {
        double totalCost = 0.0;
        for (EquipmentRental rental : rentals) {
            Equipment equipment = rental.getEquipment();
            int duration = rental.getDuration();
            double cost = equipment.getPrice() * duration;
            totalCost += cost;
        }
        return totalCost;
    }

    private static void displayRentedEquipment() {
        System.out.println("Орендоване спорядження:");
        for (EquipmentRental rental : rentals) {
            Equipment equipment = rental.getEquipment();
            User user = rental.getUser();
            int duration = rental.getDuration();
            System.out.println("Клієнт: " + user.getUsername() + ", Спорядження: " + equipment.getName() + ", Тип: " + equipment.getType() + ", Тривалість оренди: " + duration + " днів");
        }
    }
}