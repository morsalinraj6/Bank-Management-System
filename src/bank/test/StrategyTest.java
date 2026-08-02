package bank.test;

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

public class StrategyTest {

    private static final String TEST_DIR = "data/test-strategy";

    @BeforeAll
    public static void initAll() {
        assertNotNull(TEST_DIR);
    }

    @BeforeEach
    public void setup() {
        DataManager.useDataDirectory(TEST_DIR);
        new File(TEST_DIR).mkdirs();
    }

    @AfterEach
    public void cleanup() {
        assertTrue(new File(TEST_DIR + "/customers.dat").delete() || !new File(TEST_DIR + "/customers.dat").exists());
        assertTrue(new File(TEST_DIR + "/accounts.dat").delete() || !new File(TEST_DIR + "/accounts.dat").exists());
        assertTrue(new File(TEST_DIR + "/transactions.dat").delete() || !new File(TEST_DIR + "/transactions.dat").exists());
        assertTrue(new File(TEST_DIR).delete() || !new File(TEST_DIR).exists());
        DataManager.useDataDirectory("data");
    }

    @Test
    public void testDepositStrategy() {
        Customer customer = BankService.register("Deposit User", "dep@example.com", "deposituser", "pass1234", "Savings", "Q?", "a");
        Transaction txn = BankService.deposit(customer, 250.0);
        assertEquals("DEPOSIT", txn.getType());
        assertEquals(250.0, txn.getAmount());
        assertEquals(250.0, customer.getBalance());
        assertNotNull(txn.getTxnId());
    }

    @Test
    public void testWithdrawStrategy() {
        Customer customer = BankService.register("Withdraw User", "with@example.com", "withdrawuser", "pass1234", "Savings", "Q?", "b");
        BankService.deposit(customer, 300.0);
        Transaction txn = BankService.withdraw(customer, 100.0);
        assertEquals("WITHDRAW", txn.getType());
        assertEquals(100.0, txn.getAmount());
        assertEquals(200.0, customer.getBalance());
        assertFalse(customer.getBalance() < 0);
    }

    @Test
    public void testTransferStrategy() {
        Customer from = BankService.register("From User", "from@example.com", "fromuser", "pass1234", "Savings", "Q?", "c");
        Customer to = BankService.register("To User", "to@example.com", "touser", "pass1234", "Current", "Q?", "d");
        BankService.deposit(from, 400.0);
        Transaction txn = BankService.transfer(from, to.getAccountId(), 150.0);
        assertEquals("TRANSFER_OUT", txn.getType());
        assertEquals(150.0, txn.getAmount());
        assertEquals(250.0, from.getBalance());
        assertDoesNotThrow(() -> BankService.findAccountById(to.getAccountId()));
    }
}
