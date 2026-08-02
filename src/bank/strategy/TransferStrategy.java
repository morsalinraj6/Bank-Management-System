package bank.strategy;

import bank.entity.Account;
import bank.entity.Customer;
import bank.entity.Transaction;
import bank.singleton.DataManager;

import java.time.LocalDateTime;

/**
 * TransferStrategy implements transfer logic as a strategy.
 */
public class TransferStrategy implements TransactionStrategy {
    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public Transaction execute(Customer fromCustomer, String targetAccountId, double amount) {
        if (fromCustomer == null) {
            throw new IllegalArgumentException("Customer null");
        }
        if (targetAccountId == null || targetAccountId.isBlank()) {
            throw new IllegalArgumentException("Destination account is required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        Account fromAccount = dataManager.findAccountById(fromCustomer.getAccountId());
        if (fromAccount == null) {
            throw new IllegalStateException("Source account not found");
        }
        if (fromAccount.getAccountId().equalsIgnoreCase(targetAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to your own account");
        }

        Account toAccount = dataManager.findAccountById(targetAccountId);
        if (toAccount == null) {
            throw new IllegalArgumentException("Destination account not found");
        }
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        Customer toCustomer = dataManager.findCustomerByAccountId(targetAccountId);

        double newFromBalance = fromAccount.getBalance() - amount;
        fromAccount.setBalance(newFromBalance);
        fromCustomer.setBalance(newFromBalance);

        double newToBalance = toAccount.getBalance() + amount;
        toAccount.setBalance(newToBalance);
        if (toCustomer != null) {
            toCustomer.setBalance(newToBalance);
        }

        dataManager.saveAccounts();
        dataManager.saveCustomers();

        Transaction outTransaction = new Transaction(
                dataManager.nextTransactionId(),
                fromCustomer.getCustomerId(),
                fromAccount.getAccountId(),
                toAccount.getAccountId(),
                LocalDateTime.now(),
                "TRANSFER_OUT",
                amount,
                newFromBalance
        );
        dataManager.addTransaction(outTransaction);

        if (toCustomer != null) {
            Transaction inTransaction = new Transaction(
                    dataManager.nextTransactionId(),
                    toCustomer.getCustomerId(),
                    toAccount.getAccountId(),
                    fromAccount.getAccountId(),
                    LocalDateTime.now(),
                    "TRANSFER_IN",
                    amount,
                    newToBalance
            );
            dataManager.addTransaction(inTransaction);
        }

        return outTransaction;
    }
}
