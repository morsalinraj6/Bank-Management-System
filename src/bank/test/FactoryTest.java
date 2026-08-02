package bank.test;

import bank.entity.Account;
import bank.factory.AccountFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {

    @Test
    public void testSavingsAccountCreation() {
        Account account = AccountFactory.createAccount("A-1000", "Savings", 0.0);
        assertEquals("Savings", account.getAccountType());
        assertEquals("A-1000", account.getAccountId());
        assertNotNull(account);
    }

    @Test
    public void testCurrentAccountCreation() {
        Account account = AccountFactory.createAccount("A-1001", "Current", 100.0);
        assertEquals("Current", account.getAccountType());
        assertEquals(100.0, account.getBalance());
        assertDoesNotThrow(() -> AccountFactory.createAccount("A-1002", "Current", 250.0));
    }

    @Test
    public void testDefaultAccountType() {
        Account account = AccountFactory.createAccount("A-1003", null, 0.0);
        assertEquals("Savings", account.getAccountType());
    }
}
