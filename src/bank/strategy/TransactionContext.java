package bank.strategy;

import bank.entity.Customer;
import bank.entity.Transaction;

/**
 * TransactionContext selects the transaction strategy at runtime.
 */
public class TransactionContext {
    private final TransactionStrategy strategy;

    public TransactionContext(TransactionStrategy strategy) {
        this.strategy = strategy;
    }

    public Transaction execute(Customer customer, String targetAccountId, double amount) {
        return strategy.execute(customer, targetAccountId, amount);
    }
}
