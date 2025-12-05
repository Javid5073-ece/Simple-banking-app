import java.util.ArrayList;
import java.util.List;

public class Account {
    public enum AccountType { SAVINGS, CURRENT }

    private final int id;
    private final String ownerName;
    private final AccountType type;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public static final double MIN_SAVINGS_BALANCE = 1000.0;

    public Account(int id, String ownerName, AccountType type, double initialDeposit) {
        this.id = id;
        this.ownerName = ownerName;
        this.type = type;
        this.balance = initialDeposit;
    }

    public int getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public AccountType getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    void applyDeposit(double amount) {
        balance += amount;
    }

    void applyWithdrawal(double amount) {
        balance -= amount;
    }

    void addTransaction(Transaction t) {
        transactions.add(t);
    }

    boolean canWithdraw(double amount) {
        if (amount <= 0) return false;
        if (type == AccountType.SAVINGS) {
            return (balance - amount) >= MIN_SAVINGS_BALANCE;
        } else {
            return balance >= amount;
        }
    }

    @Override
    public String toString() {
        return String.format("Account{id=%d, owner='%s', type=%s, balance=%.2f}", id, ownerName, type, balance);
    }
}
