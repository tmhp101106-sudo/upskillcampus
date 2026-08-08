import java.util.*;
import java.io.*;

public class ExpenseTracker {
    // Expense Class
    
    static class Expense {
        private int id;
        private String date;
        private double amount;
        private String category;
        private String description;
        public Expense(int id, String date, double amount, String category, String description) {
            this.id = id;
            this.date = date;
            this.amount = amount;
            this.category = category;
            this.description = description;
        }
        public int getId() {
            return id;
        }
        public String getDate() {
            return date;
        }
        public double getAmount() {
            return amount;
        }
        public String getCategory() {
            return category;
        }
        public String getDescription() {
            return description;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public void setAmount(double amount) {
            this.amount = amount;
        }
        public void setCategory(String category) {
            this.category = category;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String toFileString() {
            return id + "," + date + "," + amount + "," + category + "," + description;
        }

        public static Expense fromFile(String line) {
            String[] parts = line.split(",", 5);
            if (parts.length != 5)
                return null;
            return new Expense(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    Double.parseDouble(parts[2]),
                    parts[3],
                    parts[4]
            );
        }
        @Override
        public String toString() {
            return String.format("%-5d %-12s %-10.2f %-15s %-25s",
                    id,date,amount,category,description);
        }
    }

    // Global Variables
    
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Expense> expenses =new ArrayList<>();
    static ArrayList<String> categories =new ArrayList<>();
    static final String EXPENSE_FILE ="expenses.txt";
    static final String CATEGORY_FILE ="categories.txt";
    static int nextId = 1;

    // Welcome Screen
    
    static void welcomeScreen() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("      EXPENSE TRACKER APPLICATION");
        System.out.println("==========================================");
        System.out.println("        Core Java Internship");
        System.out.println("==========================================");
        System.out.println();
    }

    // Login
    
    static void login() {
        System.out.print("Enter User Name : ");
        String username =scanner.nextLine();
        if (username.trim().isEmpty()) {
            username = "User";
        }
        System.out.println();
        System.out.println("Welcome " + username + "!");
        System.out.println("Login Successful.");
        System.out.println();
    }

    // Main Method
    
    public static void main(String[] args) {
        welcomeScreen();
        login();
        loadCategories();
        loadExpenses();
        while (true) {
            System.out.println();
            System.out.println("============== MAIN MENU ==============");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Category Management");
            System.out.println("6. Search Expenses");
            System.out.println("7. Filter Expenses");
            System.out.println("8. Reports");
            System.out.println("9. Save Data");
            System.out.println("10. About");
            System.out.println("11. Exit");
            System.out.println("=======================================");
            System.out.print("Enter Choice : ");
            int choice = readInt();
            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    updateExpense();
                    break;
                case 4:
                    deleteExpense();
                    break;
                case 5:
                    categoryMenu();
                    break;
                case 6:
                    searchMenu();
                    break;
                case 7:
                    filterMenu();
                    break;
                case 8:
                    reportMenu();
                    break;
                case 9:
                    saveExpenses();
                    saveCategories();
                    System.out.println("Data Saved Successfully.");
                    break;
                case 10:
                    about();
                    break;
                case 11:
                    saveExpenses();
                    saveCategories();
                    System.out.println("Thank You For Using Expense Tracker.");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
    // Add Expense
    
    static void addExpense() {
        System.out.println("\n========== ADD EXPENSE ==========");
        System.out.print("Enter Date (yyyy-mm-dd): ");
        String date = scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = readDouble();
        if (categories.isEmpty()) {
            System.out.println("No Categories Found.");
            System.out.println("Please Add Categories First.");
            return;
        }
        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        System.out.print("Choose Category Number: ");
        int choice = readInt();
        if (choice < 1 || choice > categories.size()) {
            System.out.println("Invalid Category.");
            return;
        }
        String category = categories.get(choice - 1);
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(nextId++,date,amount,category,description);
        expenses.add(expense);
        System.out.println("\nExpense Added Successfully.");
    }
    // View Expenses
    
    static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("\nNo Expense Records Available.");
            return;
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-5s %-12s %-10s %-15s %-25s\n",
                "ID","DATE","AMOUNT","CATEGORY","DESCRIPTION");
        System.out.println("--------------------------------------------------------------------------");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }

        System.out.println("--------------------------------------------------------------------------");
    }
    // Update Expense
    
    static void updateExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No Expenses Available.");
            return;
        }

        viewExpenses();
        System.out.print("\nEnter Expense ID: ");
        int id = readInt();
        Expense expense = findExpense(id);
        if (expense == null) {
            System.out.println("Expense Not Found.");
            return;
        }
        System.out.print("New Date: ");
        expense.setDate(scanner.nextLine());
        System.out.print("New Amount: ");
        expense.setAmount(readDouble());
        System.out.println("\nCategories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        System.out.print("Choose Category: ");
        int option = readInt();
        if (option >= 1 && option <= categories.size()) {
            expense.setCategory(categories.get(option - 1));
        }
        System.out.print("New Description: ");
        expense.setDescription(scanner.nextLine());
        System.out.println("Expense Updated Successfully.");
    }
    // Delete Expense
    
    static void deleteExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No Expenses Available.");
            return;
        }
        viewExpenses();
        System.out.print("\nEnter Expense ID to Delete: ");
        int id = readInt();
        Iterator<Expense> iterator = expenses.iterator();
        while (iterator.hasNext()) {
            Expense expense = iterator.next();
            if (expense.getId() == id) {
                iterator.remove();
                System.out.println("Expense Deleted Successfully.");
                return;
            }
        }
        System.out.println("Expense ID Not Found.");
    }

    // Find Expense By ID
    
    static Expense findExpense(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null;
    }

    // Check Expense Exists
    
    static boolean expenseExists(int id) {
        return findExpense(id) != null;
    }
    // CATEGORY MANAGEMENT
    
    static void categoryMenu() {
        while (true) {
            System.out.println("\n========== CATEGORY MENU ==========");
            System.out.println("1. Add Category");
            System.out.println("2. View Categories");
            System.out.println("3. Delete Category");
            System.out.println("4. Back");
            System.out.print("Enter Choice: ");
            int choice = readInt();
            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    viewCategories();
                    break;
                case 3:
                    deleteCategory();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
    static void addCategory() {
        System.out.print("\nEnter Category Name: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Category Cannot Be Empty.");
            return;
        }
        if (categories.contains(category)) {
            System.out.println("Category Already Exists.");
            return;
        }
        categories.add(category);
        System.out.println("Category Added Successfully.");
    }
    static void viewCategories() {
        if (categories.isEmpty()) {
            System.out.println("\nNo Categories Available.");
            return;
        }
        System.out.println("\n------ CATEGORY LIST ------");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
    }
    static void deleteCategory() {
        if (categories.isEmpty()) {
            System.out.println("No Categories Available.");
            return;
        }
        viewCategories();
        System.out.print("\nEnter Category Number: ");
        int choice = readInt();
        if (choice < 1 || choice > categories.size()) {
            System.out.println("Invalid Category.");
            return;
        }
        String removedCategory = categories.remove(choice - 1);
        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(removedCategory)) {
                expense.setCategory("Others");
            }
        }
        System.out.println("Category Deleted Successfully.");
    }
    // SEARCH MENU
    
    static void searchMenu() {
        while (true) {
            System.out.println("\n========== SEARCH MENU ==========");
            System.out.println("1. Search By ID");
            System.out.println("2. Search By Category");
            System.out.println("3. Search By Date");
            System.out.println("4. Back");
            System.out.print("Enter Choice: ");
            int choice = readInt();
            switch (choice) {
                case 1:
                    searchById();
                    break;
                case 2:
                    searchByCategory();
                    break;
                case 3:
                    searchByDate();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
    static void searchById() {
        System.out.print("\nEnter Expense ID: ");
        int id = readInt();
        Expense expense = findExpense(id);
        if (expense == null) {
            System.out.println("Expense Not Found.");
            return;
        }
        System.out.println("\nExpense Details:");
        System.out.println(expense);
    }
    static void searchByCategory() {
        System.out.print("\nEnter Category: ");
        String category = scanner.nextLine();
        boolean found = false;
        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                System.out.println(expense);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Expense Found.");
        }
    }
    static void searchByDate() {
        System.out.print("\nEnter Date (yyyy-mm-dd): ");
        String date = scanner.nextLine();
        boolean found = false;
        for (Expense expense : expenses) {
            if (expense.getDate().equals(date)) {
                System.out.println(expense);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Expense Found.");
        }
    }
    // FILTER MENU
    
    static void filterMenu() {
        System.out.print("\nEnter Minimum Amount: ");
        double minimum = readDouble();
        System.out.println("\nExpenses Greater Than " + minimum);
        boolean found = false;
        for (Expense expense : expenses) {
            if (expense.getAmount() >= minimum) {
                System.out.println(expense);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Matching Expenses Found.");
        }
    }
    // REPORT MENU
    
    static void reportMenu() {
        while (true) {
            System.out.println("\n========== REPORT MENU ==========");
            System.out.println("1. Monthly Report");
            System.out.println("2. Category-wise Report");
            System.out.println("3. Total Expense");
            System.out.println("4. Highest Expense");
            System.out.println("5. Lowest Expense");
            System.out.println("6. Average Expense");
            System.out.println("7. Sort by Amount");
            System.out.println("8. Sort by Date");
            System.out.println("9. Back");
            System.out.print("Enter Choice: ");
            int choice = readInt();
            switch (choice) {
                case 1:
                    monthlyReport();
                    break;
                case 2:
                    categoryWiseReport();
                    break;
                case 3:
                    totalExpense();
                    break;
                case 4:
                    highestExpense();
                    break;
                case 5:
                    lowestExpense();
                    break;
                case 6:
                    averageExpense();
                    break;
                case 7:
                    sortByAmount();
                    break;
                case 8:
                    sortByDate();
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
    // MONTHLY REPORT
    
    static void monthlyReport() {
        if (expenses.isEmpty()) {
            System.out.println("No Expense Records.");
            return;
        }
        HashMap<String, Double> monthly = new HashMap<>();
        for (Expense expense : expenses) {
            String month = expense.getDate().substring(0, 7);
            monthly.put(month,monthly.getOrDefault(month, 0.0)+ expense.getAmount());
        }
        System.out.println("\n====== MONTHLY REPORT ======");
        for (String month : monthly.keySet()) {
            System.out.printf("%-10s : %.2f\n", month,monthly.get(month));
        }
    }
    // CATEGORY REPORT
    
    static void categoryWiseReport() {
        if (expenses.isEmpty()) {
            System.out.println("No Expense Records.");
            return;
        }
        HashMap<String, Double> report = new HashMap<>();
        for (Expense expense : expenses) {
            report.put(expense.getCategory(),report.getOrDefault(expense.getCategory(),0.0)+ expense.getAmount());
        }
        System.out.println("\n====== CATEGORY REPORT ======");
        for (String category : report.keySet()) {
            System.out.printf("%-15s : %.2f\n",
                    category,
                    report.get(category));
        }
    }
    // TOTAL EXPENSE
    
    static void totalExpense() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        System.out.printf("\nTotal Expense : %.2f\n", total);
    }
    // HIGHEST EXPENSE
    
    static void highestExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No Expense Records.");
            return;
        }
        Expense highest = expenses.get(0);
        for (Expense expense : expenses) {
            if (expense.getAmount() > highest.getAmount()) {
                highest = expense;
            }
        }
        System.out.println("\nHighest Expense");
        System.out.println(highest);
    }
    // LOWEST EXPENSE
    
    static void lowestExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No Expense Records.");
            return;
        }
        Expense lowest = expenses.get(0);
        for (Expense expense : expenses) {
            if (expense.getAmount() < lowest.getAmount()) {
                lowest = expense;
            }
        }
        System.out.println("\nLowest Expense");
        System.out.println(lowest);
    }
    // AVERAGE EXPENSE
    
    static void averageExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No Expense Records.");
            return;
        }
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        double average = total / expenses.size();
        System.out.printf("\nAverage Expense : %.2f\n",average);
    }
    // SORT BY AMOUNT
    
    static void sortByAmount() {
        Collections.sort(expenses, Comparator.comparingDouble(Expense::getAmount));
        System.out.println("\nExpenses Sorted By Amount.");
        viewExpenses();
    }
    // SORT BY DATE
    
    static void sortByDate() {
        Collections.sort(expenses, Comparator.comparing(Expense::getDate));
        System.out.println("\nExpenses Sorted By Date.");
        viewExpenses();
    }
    // SAVE EXPENSES
    
    static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXPENSE_FILE))) {
            for (Expense expense : expenses) {
                writer.write(expense.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error Saving Expenses.");
        }
    }
    // LOAD EXPENSES
    
    static void loadExpenses() {
        File file = new File(EXPENSE_FILE);
        if (!file.exists())
            return;
        try (BufferedReader reader = new BufferedReader( new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Expense expense = Expense.fromFile(line);
                if (expense != null) {
                    expenses.add(expense);
                    if (expense.getId() >= nextId) {
                        nextId = expense.getId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error Loading Expenses.");
        }
    }
    // SAVE CATEGORIES
    static void saveCategories() {
        try (BufferedWriter writer = new BufferedWriter( new FileWriter(CATEGORY_FILE))) {
            for (String category : categories) {
                writer.write(category);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error Saving Categories.");
        }
    }
    // LOAD CATEGORIES
    
    static void loadCategories() {
        File file = new File(CATEGORY_FILE);
        if (!file.exists()) {
            categories.add("Food");
            categories.add("Travel");
            categories.add("Shopping");
            categories.add("Medical");
            categories.add("Education");
            categories.add("Bills");
            categories.add("Entertainment");
            categories.add("Others");
            saveCategories();
            return;
        }
        try (BufferedReader reader = new BufferedReader( new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    categories.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error Loading Categories.");
        }
    }
    // READ INTEGER

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(
                        scanner.nextLine());
            } catch (Exception e) {
                System.out.print(
                        "Invalid Input. Enter Integer: ");
            }
        }
    }
    // READ DOUBLE

    static double readDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.print( "Amount Cannot Be Negative: ");
                    continue;
                }
                return value;
            } catch (Exception e) {
                System.out.print(
                        "Invalid Amount. Enter Again: ");
            }
        }
    }
    // ABOUT APPLICATION

    static void about() {
        System.out.println();
        System.out.println("====================================");
        System.out.println("Expense Tracker Application");
        System.out.println("====================================");
        System.out.println("Language : Core Java");
        System.out.println("Storage  : Text Files");
        System.out.println("Collections : ArrayList");
        System.out.println("Concepts Used:");
        System.out.println("- Object Oriented Programming");
        System.out.println("- File Handling");
        System.out.println("- Exception Handling");
        System.out.println("- Java Collections");
        System.out.println("- Menu Driven Programming");
        System.out.println("====================================");
    }
    // PRESS ENTER
    static void pause() {
        System.out.println();
        System.out.println("Press ENTER To Continue...");
        scanner.nextLine();
    }
}