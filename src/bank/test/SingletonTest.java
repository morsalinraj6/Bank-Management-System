package bank.test;

import bank.singleton.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @BeforeEach
    public void reset() {
        DataManager.resetInstance();
    }

    @Test
    public void testSameInstanceReturned() {
        DataManager first = DataManager.getInstance();
        DataManager second = DataManager.getInstance();
        assertSame(first, second);
    }

    @Test
    public void testResetInstanceCreatesNewOne() {
        DataManager first = DataManager.getInstance();
        DataManager.resetInstance();
        DataManager second = DataManager.getInstance();
        assertNotSame(first, second);
    }
}
