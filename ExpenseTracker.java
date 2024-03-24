import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    private Map<String, List<Expense>> expensesByCategory;
    private File dataFile;

    public ExpenseTracker(String dataFilePath) {
        expensesByCategory = new HashMap<>();
        dataFile = new File(dataFilePath);
        loadExpenses();
    }

    public void registerExpense(String category, LocalDate date, double amount) {
        Expense expense = new Expense(date, category, amount);
        expensesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(expense);
        saveExpenses();
    }

    public void listExpenses() {
        System.out.println("Expense Listing:");
        for (String category : expensesByCategory.keySet()) {
            System.out.println("Category: " + category);
            List<Expense> expenses = expensesByCategory.get(category);
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
            System.out.println("Total for " + category + ": " + calculateTotal(expenses));
        }
    }

    public void listExpensesByCategory(String category) {
        if (expensesByCategory.containsKey(category)) {
            System.out.println("Expenses for category " + category + ":");
            List<Expense> expenses = expensesByCategory.get(category);
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
            System.out.println("Total for " + category + ": " + calculateTotal(expenses));
        } else {
            System.out.println("No expenses found for category " + category);
        }
    }

    private double calculateTotal(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    private void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            expensesByCategory = (Map<String, List<Expense>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File might not exist or could not be read, ignore for now
        }
    }

    private void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(expensesByCategory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExpenseTracker expenseTracker = new ExpenseTracker("expenses.dat");

        // Sample usage
        expenseTracker.registerExpense("Food", LocalDate.now(), 50.0);
        expenseTracker.registerExpense("Transportation", LocalDate.now(), 30.0);
        expenseTracker.registerExpense("Food", LocalDate.now(), 20.0);
        expenseTracker.registerExpense("Entertainment", LocalDate.now(), 100.0);

        expenseTracker.listExpenses();
    }
}

class Expense implements Serializable {
    private LocalDate date;
    private String category;
    private double amount;

    public Expense(LocalDate date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "date=" + date +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                '}';
    }
}
