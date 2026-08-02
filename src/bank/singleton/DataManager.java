package bank.singleton;

import bank.data.FileHandler;
import bank.entity.Account;
import bank.entity.Customer;
import bank.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * DataManager implements the Singleton Pattern for centralized data management.
 * It manages customers, accounts, and transactions and delegates persistence to FileHandler.
 */
public class DataManager {
    private static String dataDirectory = "data";

    private static DataManager instance;

    private List<Customer> customers;
    private List<Account> accounts;
    private List<Transaction> transactions;

    private DataManager() {
        customers = FileHandler.loadList(getCustomersFile());
        accounts = FileHandler.loadList(getAccountsFile());
        transactions = FileHandler.loadList(getTransactionsFile());
    }

    private static String getCustomersFile() {
        return dataDirectory + "/customers.dat";
    }

    private static String getAccountsFile() {
        return dataDirectory + "/accounts.dat";
    }

    private static String getTransactionsFile() {
        return dataDirectory + "/transactions.dat";
    }

    public static synchronized void useDataDirectory(String directory) {
        if (directory == null || directory.isBlank()) {
            throw new IllegalArgumentException("Data directory cannot be blank");
        }
        dataDirectory = directory;
        resetInstance();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }

    public synchronized List<Customer> getCustomers() {
        if (customers == null) customers = new ArrayList<>();
        return customers;
    }

    public synchronized List<Account> getAccounts() {
        if (accounts == null) accounts = new ArrayList<>();
        return accounts;
    }

    public synchronized List<Transaction> getTransactions() {
        if (transactions == null) transactions = new ArrayList<>();
        return transactions;
    }

    public synchronized void addCustomer(Customer customer) {
        getCustomers().add(customer);
        saveCustomers();
    }

    public synchronized void addAccount(Account account) {
        getAccounts().add(account);
        saveAccounts();
    }

    public synchronized void addTransaction(Transaction transaction) {
        getTransactions().add(transaction);
        saveTransactions();
    }

    public synchronized void saveCustomers() {
        FileHandler.saveList(getCustomers(), getCustomersFile());
    }

    public synchronized void saveAccounts() {
        FileHandler.saveList(getAccounts(), getAccountsFile());
    }

    public synchronized void saveTransactions() {
        FileHandler.saveList(getTransactions(), getTransactionsFile());
    }

    public synchronized String nextCustomerId() {
        return "C-" + String.format("%04d", getCustomers().size() + 1);
    }

    public synchronized String nextAccountId() {
        return "A-" + String.format("%04d", getAccounts().size() + 1);
    }

    public synchronized String nextTransactionId() {
        return "T-" + String.format("%05d", getTransactions().size() + 1);
    }

    public synchronized Customer findCustomerByUsername(String username) {
        if (username == null) return null;
        for (Customer c : getCustomers()) {
            if (username.equalsIgnoreCase(c.getUsername())) {
                return c;
            }
        }
        return null;
    }

    public synchronized Customer findCustomerByEmail(String email) {
        if (email == null) return null;
        for (Customer c : getCustomers()) {
            if (email.equalsIgnoreCase(c.getEmail())) {
                return c;
            }
        }
        return null;
    }

    public synchronized Customer findCustomerById(String id) {
        if (id == null) return null;
        for (Customer c : getCustomers()) {
            if (id.equalsIgnoreCase(c.getCustomerId())) {
                return c;
            }
        }
        return null;
    }

    public synchronized Account findAccountById(String id) {
        if (id == null) return null;
        for (Account a : getAccounts()) {
            if (id.equalsIgnoreCase(a.getAccountId())) {
                return a;
            }
        }
        return null;
    }

    public synchronized Customer findCustomerByAccountId(String accountId) {
        if (accountId == null) return null;
        for (Customer c : getCustomers()) {
            if (accountId.equalsIgnoreCase(c.getAccountId())) {
                return c;
            }
        }
        return null;
    }
}
