package bank.test;

import bank.entity.Account;
import bank.entity.Customer;
import bank.entity.Transaction;
import bank.service.BankService;
import bank.singleton.DataManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {

    private static final String TEST_DIR = "data/test-bank-service";

    @BeforeAll
    public static void beforeAll() {
        assertNotNull(TEST_DIR);
    }

    @BeforeEach
    public void setup() {
        DataManager.useDataDirectory(TEST_DIR);
        new File(TEST_DIR).mkdirs();
    }

    @AfterEach
    public void cleanup() {
        File customers = new File(TEST_DIR + "/customers.dat");
        File accounts = new File(TEST_DIR + "/accounts.dat");
        File transactions = new File(TEST_DIR + "/transactions.dat");
        if (customers.exists()) assertTrue(customers.delete());
        if (accounts.exists()) assertTrue(accounts.delete());
        if (transactions.exists()) assertTrue(transactions.delete());
        new File(TEST_DIR).delete();
        DataManager.useDataDirectory("data");
    }

    @Test
    public void testRegisterAndLogin() {
        Customer customer = BankService.register("Test User", "test@example.com", "testuser", "Password1!", "Savings", "Favorite color?", "blue");
        assertNotNull(customer);
        Object loginResult = BankService.login("testuser", "Password1!");
        assertTrue(loginResult instanceof Customer);
        assertSame(customer, loginResult);
    }

    @Test
    public void testDepositWithdrawTransfer() {
        Customer from = BankService.register("User One", "one@example.com", "userone", "Secret1!", "Savings", "Pet name?", "fluffy");
        Customer to = BankService.register("User Two", "two@example.com", "usertwo", "Secret2!", "Current", "School?", "central");

        Transaction depositTxn = BankService.deposit(from, 500.0);
        assertEquals("DEPOSIT", depositTxn.getType());
        assertEquals(500.0, depositTxn.getAmount());
        assertEquals(500.0, from.getBalance());

        Transaction withdrawTxn = BankService.withdraw(from, 200.0);
        assertEquals("WITHDRAW", withdrawTxn.getType());
        assertEquals(300.0, from.getBalance());

        Transaction transferTxn = BankService.transfer(from, to.getAccountId(), 100.0);
        assertEquals("TRANSFER_OUT", transferTxn.getType());
        assertEquals(100.0, transferTxn.getAmount());
        assertEquals(200.0, from.getBalance());

        Account toAccount = BankService.findAccountById(to.getAccountId());
        assertEquals(100.0, toAccount.getBalance());
        assertNotSame(from.getAccountId(), toAccount.getAccountId());
    }
}
