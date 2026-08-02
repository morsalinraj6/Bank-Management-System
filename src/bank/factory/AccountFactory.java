package bank.factory;

import bank.entity.Account;

/**
 * AccountFactory implements the Factory Method Pattern for account creation.
 * It centralizes creation of Savings and Current accounts.
 */
public class AccountFactory {

    public static Account createAccount(String accountId, String accountType, double balance) {
        String normalizedType = normalizeType(accountType);
        if ("Current".equalsIgnoreCase(normalizedType)) {
            return createCurrentAccount(accountId, balance);
        }
        return createSavingsAccount(accountId, balance);
    }

    private static Account createSavingsAccount(String accountId, double balance) {
        return new Account(accountId, "Savings", balance);
    }

    private static Account createCurrentAccount(String accountId, double balance) {
        return new Account(accountId, "Current", balance);
    }

    private static String normalizeType(String accountType) {
        if (accountType == null || accountType.isBlank()) {
            return "Savings";
        }
        return accountType.trim();
    }
}
