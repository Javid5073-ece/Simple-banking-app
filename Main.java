import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bank bank = new Bank();

    public static void main(String[] args) {
        System.out.println("Welcome to SimpleBank!");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createAccount();
                case "2" -> deposit();
                case "3" -> withdraw();
                case "4" -> transfer();
                case "5" -> viewAccount();
                case "6" -> listAccounts();
                case "0" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Create account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. View account details & transactions");
        System.out.println("6. List all accounts");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void createAccount() {
        try {
            System.out.print("Enter owner name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Account type (1=Savings, 2=Current): ");
            String typeStr = scanner.nextLine().trim();
            Account.AccountType type;
            switch (typeStr) {
                case "1" -> type = Account.AccountType.SAVINGS;
                case "2" -> type = Account.AccountType.CURRENT;
                default -> {
                    System.out.println("Invalid account type.");
                    return;
                }
            }
            System.out.print("Initial deposit: ");
            double init = Double.parseDouble(scanner.nextLine().trim());
            Account acc = bank.createAccount(name, type, init);
            System.out.println("Account created: " + acc);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Error: " + iae.getMessage());
        }
    }

    private static void deposit() {
        try {
            System.out.print("Account id: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Amount to deposit: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            boolean ok = bank.deposit(id, amt);
            System.out.println(ok ? "Deposit successful." : "Deposit failed. Check account id or amount.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number.");
        }
    }

    private static void withdraw() {
        try {
            System.out.print("Account id: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Amount to withdraw: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            boolean ok = bank.withdraw(id, amt);
            System.out.println(ok ? "Withdrawal successful." : "Withdrawal failed. Check balance, minimum for savings, or amount.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number.");
        }
    }

    private static void transfer() {
        try {
            System.out.print("From account id: ");
            int from = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("To account id: ");
            int to = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Amount to transfer: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            boolean ok = bank.transfer(from, to, amt);
            System.out.println(ok ? "Transfer successful." : "Transfer failed. Check accounts, balance, or amount.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number.");
        }
    }

    private static void viewAccount() {
        try {
            System.out.print("Account id: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            var opt = bank.getAccount(id);
            if (opt.isEmpty()) {
                System.out.println("Account not found.");
                return;
            }
            Account a = opt.get();
            System.out.println(a);
            System.out.println("Transactions:");
            List<Transaction> tx = a.getTransactions();
            if (tx.isEmpty()) System.out.println("  (no transactions)");
            else tx.forEach(t -> System.out.println("  " + t));
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number.");
        }
    }

    private static void listAccounts() {
        if (bank.getAllAccounts().isEmpty()) {
            System.out.println("No accounts.");
            return;
        }
        bank.getAllAccounts().values().forEach(a -> System.out.println(a));
    }
}
