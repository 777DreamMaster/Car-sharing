package carsharing;

import carsharing.DAO.CarDaoImpl;
import carsharing.DAO.CompanyDaoImpl;
import carsharing.DAO.CustomerDaoImpl;
import carsharing.Models.Car;
import carsharing.Models.Company;
import carsharing.Models.Customer;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {

    private static final Scanner sc = new Scanner(System.in);
    private static final CompanyDaoImpl companies = new CompanyDaoImpl();
    private static final CarDaoImpl cars = new CarDaoImpl();
    private static final CustomerDaoImpl customers = new CustomerDaoImpl();

    public static Customer currentCustomer = null;

    public static void process() {
        boolean mainMenu = true;
        while (mainMenu) {
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");

            int option = sc.nextInt();
            switch (option) {
                case 1 -> processManagerMenu();
                case 2 -> showCustomers();
                case 3 -> createCustomer();
                case 0 -> mainMenu = false;
                default -> System.out.println("Invalid op, try again.");
            }
        }
    }

    private static void processManagerMenu() {
        boolean subMenu = true;
        while (subMenu){
            System.out.println("1. Company list\n2. Create a company\n0. Back");
            int option = sc.nextInt();

            switch (option) {
                case 1 -> processCompaniesMenu();
                case 2 -> createCompany();
                case 0 -> subMenu = false;
                default -> System.out.println("Invalid op, try again.");
            }
        }
    }

    private static void createCompany() {
        System.out.println("Enter the company name:");
        sc.nextLine();
        String name = sc.nextLine();
        companies.create(new Company(0, name));
        System.out.println("\nThe company was created!\n");
    }

    private static void processCompaniesMenu() {
        List<Company> list = companies.getAll();
        boolean hasCompanies = showCompanies();
        
        boolean companyMenu = true;
        while (companyMenu){
            if (!hasCompanies) {
                companyMenu = false;
                continue;
            }
            int option = sc.nextInt();
            int param = option > list.size() ? -1 : option == 0 ? 0 : 1;
            switch (param) {
                case 1 -> {
                    processCarsMenu(list.get(option - 1));
                    companyMenu = false;
                }
                case 0 -> companyMenu = false;
                default -> System.out.println("Invalid op, try again.");
            }
            }
    }

    private static boolean showCompanies() {
        List<Company> list = companies.getAll();
        if (list.isEmpty()) {
            System.out.println("\nThe company list is empty!\n");
            return false;
        }
        System.out.println("\nChoose the company:");
//        list.forEach(comp -> System.out.println(comp.toString()));
        IntStream.iterate(1, i -> i <= list.size(),i -> i + 1)
                .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1).getName()));

        System.out.println("0. Back");
        return true;
    }

    private static void processCarsMenu(Company company) {
        boolean carMenu = true;
        System.out.printf("'%s' company%n", company.getName());
        while (carMenu){
            System.out.println("1. Car list\n2. Create a car\n0. Back");
            int option = sc.nextInt();
            switch (option) {
                case 1 -> showCars(company);
                case 2 -> createCar(company);
                case 0 -> carMenu = false;
                default -> System.out.println("Invalid op, try again.");
            }

        }
    }

    private static void showCars(Company company) {
        List<Car> list = cars.getAllByCompany(company);
        if (list.isEmpty()) {
            System.out.println("\nThe car list is empty!\n");
        } else {
            System.out.println("\nCar list:");
            IntStream.iterate(1, i -> i <= list.size(),i -> i + 1)
                    .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1).getName()));
            System.out.println();
        }
    }

    private static void createCar(Company company) {
        System.out.println("Enter the car name:");
        sc.nextLine();
        String name = sc.nextLine();
        cars.create(new Car(0, name, company.getId()));
        System.out.println("\nThe car was added!\n");
    }

    private static void createCustomer() {
        System.out.println("Enter the customer name:");
        sc.nextLine();
        String name = sc.nextLine();
        customers.create(new Customer(0, name));
        System.out.println("\nThe customer was added!\n");
    }

    private static void showCustomers() {
        List<Customer> list = customers.getAll();
        if (list.isEmpty()) {
            System.out.println("\nThe customer list is empty!\n");
        } else {
            boolean customerMenu = true;
            while (customerMenu){
                System.out.println("\nChoose the customer:");
                list.forEach(customer -> System.out.println(customer.toString()));
                System.out.println("0. Back");

                int option = sc.nextInt();
                int param = option > list.size() ? -1 : option == 0 ? 0 : 1;
                switch (param) {
                    case 1 -> {
                        currentCustomer = list.get(option - 1);
                        processCustomerMenu();
                        customerMenu = false;
                    }
                    case 0 -> customerMenu = false;
                    default -> System.out.println("Invalid op, try again.");
                }
            }
        }
    }

    private static void processCustomerMenu() {
        boolean customerMenu = true;
        while (customerMenu){
            System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
            int option = sc.nextInt();
            switch (option) {
                case 1 -> rentCarController();
                case 2 -> returnCar();
                case 3 -> myCar();
                case 0 -> customerMenu = false;
                default -> System.out.println("Invalid op, try again.");
            }
        }
    }

    private static void rentCarController() {
        if (currentCustomer.getRentedCarId() != 0) {
            System.out.println("\nYou've already rented a car!\n");
            return;
        }
        chooseCompany();
    }

    private static void chooseCompany() {
        List<Company> list = companies.getAll();
        boolean hasCompanies = showCompanies();

        boolean companyMenu = true;
        while (companyMenu){
            if (!hasCompanies) {
                companyMenu = false;
                continue;
            }
            int option = sc.nextInt();
            int param = option > list.size() ? -1 : option == 0 ? 0 : 1;
            switch (param) {
                case 1 -> {
                    chooseCar(list.get(option - 1));
                    companyMenu = false;
                }
                case 0 -> companyMenu = false;
                default -> System.out.println("Invalid op, try again.");
            }
        }
    }

    private static void chooseCar(Company company) {
        List<Car> list = cars.getAllAvailable(company);
        if (list.size() == 0) {
            System.out.printf("%nNo available cars in the %s company%n%n", company.getName());
            return;
        }

        System.out.println("\nChoose a car:");
        IntStream.iterate(1, i -> i <= list.size(),i -> i + 1)
                .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1).getName()));
        System.out.println("0. Back");

        int option = sc.nextInt();
        if (option == 0) return;
        currentCustomer = new Customer(currentCustomer.getId(), currentCustomer.getName(), list.get(option - 1).getId());
        customers.update(currentCustomer);
        System.out.printf("%nYou rented '%s'%n%n", list.get(option - 1).getName());
    }

    private static void returnCar() {
        if (currentCustomer.getRentedCarId() == 0) {
            System.out.println("\nYou didn't rent a car!\n");
            return;
        }
        currentCustomer =  new Customer(currentCustomer.getId(), currentCustomer.getName(), 0);
        customers.update(currentCustomer);

        System.out.println("\nYou've returned a rented car!\n");
    }

    private static void myCar() {
        if (currentCustomer.getRentedCarId() == 0) {
            System.out.println("\nYou didn't rent a car!\n");
            return;
        }
        Car car = cars.getById(currentCustomer.getRentedCarId()).get();
        System.out.println("\nYour rented car:");
        System.out.println(car.getName());
        System.out.println("Company:");
        System.out.println(companies.getById(car.getCompanyId()).get().getName());
        System.out.println();
    }
}
