package bank.test;

import bank.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testCreateTransaction() {
        Transaction txn = new Transaction("T-00001", "C-0001", "A-0001", null,
                LocalDateTime.now(), "DEPOSIT", 100.0, 100.0);
        assertEquals("T-00001", txn.getTxnId());
        assertEquals("C-0001", txn.getCustomerId());
        assertEquals("A-0001", txn.getAccountId());
        assertEquals("DEPOSIT", txn.getType());
        assertEquals(100.0, txn.getAmount());
        assertFalse(txn.getBalanceAfter() < 0);
    }

    @Test
    public void testTransactionValues() {
        Transaction txn = new Transaction();
        txn.setTxnId("T-00002");
        txn.setCustomerId("C-0002");
        txn.setAccountId("A-0002");
        txn.setType("WITHDRAW");
        txn.setAmount(50.0);
        txn.setBalanceAfter(450.0);
        assertEquals(450.0, txn.getBalanceAfter());
        assertEquals(50.0, txn.getAmount());
        assertNotNull(txn.getType());
    }
}
