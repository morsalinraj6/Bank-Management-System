package bank.strategy;

import bank.entity.Customer;
import bank.entity.Transaction;

/**
 * Strategy interface for transaction operations.
 */
public interface TransactionStrategy {
    Transaction execute(Customer customer, String targetAccountId, double amount);
}
