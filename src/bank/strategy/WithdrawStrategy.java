package bank.strategy;

import bank.entity.Account;
import bank.entity.Customer;
import bank.entity.Transaction;
import bank.singleton.DataManager;

import java.time.LocalDateTime;

/**
 * WithdrawStrategy implements withdraw logic as a strategy.
 */
public class WithdrawStrategy implements TransactionStrategy {
    private final DataManager dataManager = DataManager.getInstance();

    @Override
    public Transaction execute(Customer customer, String targetAccountId, double amount) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        Account account = dataManager.findAccountById(customer.getAccountId());
        if (account == null) {
            throw new IllegalStateException("Account not found");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        customer.setBalance(newBalance);

        dataManager.saveAccounts();
        dataManager.saveCustomers();

        Transaction transaction = new Transaction(
                dataManager.nextTransactionId(),
                customer.getCustomerId(),
                account.getAccountId(),
                null,
                LocalDateTime.now(),
                "WITHDRAW",
                amount,
                newBalance
        );
        dataManager.addTransaction(transaction);
        return transaction;
    }
}
