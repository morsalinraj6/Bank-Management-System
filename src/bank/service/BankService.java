package bank.service;

import bank.entity.*;
import bank.factory.AccountFactory;
import bank.singleton.DataManager;
import bank.strategy.DepositStrategy;
import bank.strategy.TransactionContext;
import bank.strategy.TransferStrategy;
import bank.strategy.WithdrawStrategy;

import java.time.LocalDateTime;
import java.util.*;

public class BankService {

    // Demo-only admin credentials. Production e eta secure config e rakha uchit, code e na.
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private static DataManager dataManager() {
        return DataManager.getInstance();
    }

    public static synchronized List<Customer> customers() {
        return dataManager().getCustomers();
    }

    public static synchronized List<Account> accounts() {
        return dataManager().getAccounts();
    }

    public static synchronized List<Transaction> transactions() {
        return dataManager().getTransactions();
    }

    public static String nextCustomerId() {
        return dataManager().nextCustomerId();
    }

    public static String nextAccountId() {
        return dataManager().nextAccountId();
    }

    public static String nextTxnId() {
        return dataManager().nextTransactionId();
    }

    public static synchronized Customer register(String name, String email, String username, String password,
                                                  String accountType, String securityQuestion, String securityAnswer) {
        if (isBlank(name) || isBlank(email) || isBlank(username) || isBlank(password)
                || isBlank(securityQuestion) || isBlank(securityAnswer))
            throw new IllegalArgumentException("All fields are required");
        if (!email.matches("^\\S+@\\S+\\.\\S+$"))
            throw new IllegalArgumentException("Invalid email");
        if (findCustomerByUsername(username) != null)
            throw new IllegalArgumentException("Username already exists");
        if (findCustomerByEmail(email) != null)
            throw new IllegalArgumentException("Email already exists");

        String accId = nextAccountId();
        Account acc = AccountFactory.createAccount(accId, accountType, 0.0);
        dataManager().addAccount(acc);

        String pwSalt = PasswordUtil.generateSalt();
        String pwHash = PasswordUtil.hash(password, pwSalt);

        String ansSalt = PasswordUtil.generateSalt();
        String ansHash = PasswordUtil.hash(securityAnswer.trim().toLowerCase(), ansSalt);

        String custId = nextCustomerId();
        Customer c = new Customer(custId, name, email, username, pwHash, pwSalt,
                securityQuestion, ansHash, ansSalt, accId, 0.0);
        dataManager().addCustomer(c);

        return c;
    }

    public static Customer findCustomerByUsername(String username) {
        for (Customer c : customers()) if (c.getUsername().equalsIgnoreCase(username)) return c;
        return null;
    }
    public static Customer findCustomerByEmail(String email) {
        for (Customer c : customers()) if (c.getEmail().equalsIgnoreCase(email)) return c;
        return null;
    }
    public static Customer findCustomerById(String id) {
        for (Customer c : customers()) if (c.getCustomerId().equalsIgnoreCase(id)) return c;
        return null;
    }
    public static Account findAccountById(String id) {
        for (Account a : accounts()) if (a.getAccountId().equalsIgnoreCase(id)) return a;
        return null;
    }
    public static Customer findCustomerByAccountId(String accountId) {
        for (Customer c : customers()) if (c.getAccountId().equalsIgnoreCase(accountId)) return c;
        return null;
    }

    /** Returns "ADMIN", a Customer, or null. */
    public static Object login(String userOrEmail, String password) {
        if (ADMIN_USERNAME.equalsIgnoreCase(userOrEmail) && ADMIN_PASSWORD.equals(password)) return "ADMIN";
        for (Customer c : customers()) {
            boolean match = c.getUsername().equalsIgnoreCase(userOrEmail) || c.getEmail().equalsIgnoreCase(userOrEmail);
            if (match && PasswordUtil.verify(password, c.getPasswordSalt(), c.getPasswordHash())) return c;
        }
        return null;
    }

    public static synchronized void updateCustomerProfile(Customer updated, String newName, String newEmail, String newPassword) {
        if (updated == null) return;
        if (isBlank(newName)) throw new IllegalArgumentException("Name required");
        if (newEmail == null || !newEmail.matches("^\\S+@\\S+\\.\\S+$")) throw new IllegalArgumentException("Valid email required");

        for (Customer c : customers()) {
            if (!c.getCustomerId().equals(updated.getCustomerId()) && c.getEmail().equalsIgnoreCase(newEmail)) {
                throw new IllegalArgumentException("Email already in use");
            }
        }

        updated.setName(newName.trim());
        updated.setEmail(newEmail.trim());
        if (newPassword != null && !newPassword.isBlank()) {
            String salt = PasswordUtil.generateSalt();
            updated.setPasswordSalt(salt);
            updated.setPasswordHash(PasswordUtil.hash(newPassword, salt));
        }
        dataManager().saveCustomers();
    }

    public static String getSecurityQuestion(String username) {
        Customer c = findCustomerByUsername(username);
        return c == null ? null : c.getSecurityQuestion();
    }

    public static synchronized boolean resetPassword(String username, String securityAnswer, String newPassword) {
        Customer c = findCustomerByUsername(username);
        if (c == null) return false;
        boolean correct = PasswordUtil.verify(securityAnswer.trim().toLowerCase(), c.getSecurityAnswerSalt(), c.getSecurityAnswerHash());
        if (!correct) return false;
        if (newPassword == null || newPassword.isBlank()) throw new IllegalArgumentException("New password required");

        String salt = PasswordUtil.generateSalt();
        c.setPasswordSalt(salt);
        c.setPasswordHash(PasswordUtil.hash(newPassword, salt));
        dataManager().saveCustomers();
        return true;
    }

    public static synchronized Transaction deposit(Customer c, double amount) {
        if (c == null) throw new IllegalArgumentException("Customer null");
        TransactionContext context = new TransactionContext(new DepositStrategy());
        return context.execute(c, null, amount);
    }

    public static synchronized Transaction withdraw(Customer c, double amount) {
        if (c == null) throw new IllegalArgumentException("Customer null");
        TransactionContext context = new TransactionContext(new WithdrawStrategy());
        return context.execute(c, null, amount);
    }

    public static synchronized Transaction transfer(Customer from, String toAccountId, double amount) {
        if (from == null) throw new IllegalArgumentException("Customer null");
        TransactionContext context = new TransactionContext(new TransferStrategy());
        return context.execute(from, toAccountId, amount);
    }

    public static List<Transaction> customerTransactions(String customerId) {
        List<Transaction> out = new ArrayList<>();
        for (Transaction t : transactions()) if (t.getCustomerId().equalsIgnoreCase(customerId)) out.add(t);
        out.sort(Comparator.comparing(Transaction::getDateTime).reversed());
        return out;
    }

    public static double totalBankBalance() {
        double total = 0;
        for (Account a : accounts()) total += a.getBalance();
        return total;
    }

    public static List<Transaction> allTransactionsSorted() {
        List<Transaction> out = new ArrayList<>(transactions());
        out.sort(Comparator.comparing(Transaction::getDateTime).reversed());
        return out;
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}