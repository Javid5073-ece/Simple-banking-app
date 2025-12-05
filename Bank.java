import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Bank {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private int nextAccountId = 1;

    public Account createAccount(String ownerName, Account.AccountType type, double initialDeposit) throws IllegalArgumentException {
        if (initialDeposit < 0) throw new IllegalArgumentException("Initial deposit cannot be negative");
        if (type == Account.AccountType.SAVINGS && initialDeposit < Account.MIN_SAVINGS_BALANCE) {
            throw new IllegalArgumentException("Savings account requires minimum initial deposit of " + Account.MIN_SAVINGS_BALANCE);
        }
        int id = nextAccountId++;
        Account acc = new Account(id, ownerName, type, initialDeposit);
        // record initial deposit transaction if > 0
        if (initialDeposit > 0) {
            Transaction t = new Transaction(Transaction.TransactionType.DEPOSIT, initialDeposit, "Initial deposit");
            acc.addTransaction(t);
        }
        accounts.put(id, acc);
        return acc;
    }

    public Optional<Account> getAccount(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public boolean deposit(int id, double amount) {
        Account acc = accounts.get(id);
        if (acc == null) return false;
        if (amount <= 0) return false;
        acc.applyDeposit(amount);
        acc.addTransaction(new Transaction(Transaction.TransactionType.DEPOSIT, amount, "Deposit"));
        return true;
    }

    public boolean withdraw(int id, double amount) {
        Account acc = accounts.get(id);
        if (acc == null) return false;
        if (amount <= 0) return false;
        if (!acc.canWithdraw(amount)) return false;
        acc.applyWithdrawal(amount);
        acc.addTransaction(new Transaction(Transaction.TransactionType.WITHDRAWAL, amount, "Withdrawal"));
        return true;
    }

    public boolean transfer(int fromId, int toId, double amount) {
        if (amount <= 0) return false;
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null) return false;
        // check if source can withdraw
        if (!from.canWithdraw(amount)) return false;
        // apply changes
        from.applyWithdrawal(amount);
        to.applyDeposit(amount);
        from.addTransaction(new Transaction(Transaction.TransactionType.TRANSFER, amount, "Transfer to " + toId));
        to.addTransaction(new Transaction(Transaction.TransactionType.TRANSFER, amount, "Transfer from " + fromId));
        return true;
    }

    public Map<Integer, Account> getAllAccounts() {
        return accounts;
    }
}
