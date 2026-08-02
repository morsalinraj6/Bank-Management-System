package bank.test;

import bank.entity.Account;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private static Account sampleAccount;

    @BeforeAll
    public static void init() {
        sampleAccount = new Account("A-0000", "Savings", 100.0);
    }

    @Test
    public void testCreateAccount() {
        Account account = new Account("A-0001", "Savings", 1000.0);
        assertEquals("A-0001", account.getAccountId(), "Account ID should match");
        assertEquals("Savings", account.getAccountType(), "Account type should be Savings");
        assertEquals(1000.0, account.getBalance(), "Initial balance should be 1000");
        assertNotNull(account.toString(), "toString should not return null");
    }

    @Test
    public void testSettersAndGetters() {
        Account account = new Account();
        account.setAccountId("A-0002");
        account.setAccountType("Current");
        account.setBalance(500.0);

        assertEquals("A-0002", account.getAccountId());
        assertEquals("Current", account.getAccountType());
        assertEquals(500.0, account.getBalance());
        assertNotSame(sampleAccount, account);
    }

    @Test
    public void testBalanceUpdate() {
        Account account = new Account("A-0003", "Savings", 200.0);
        account.setBalance(account.getBalance() + 300.0);
        assertEquals(500.0, account.getBalance());
        assertFalse(account.getBalance() < 0, "Balance should not be negative");
    }
}
