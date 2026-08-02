package bank.test;

import bank.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = new Customer("C-0002", "Bob", "bob@example.com", "bob123",
                "hash", "salt", "Pet name?", "ansHash", "ansSalt", "A-0002", 1500.0);
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer("C-0001", "Alice", "alice@example.com", "alice123",
                "hash", "salt", "Favorite color?", "ansHash", "ansSalt", "A-0001", 0.0);
        assertEquals("C-0001", customer.getCustomerId());
        assertEquals("Alice", customer.getName());
        assertEquals("alice@example.com", customer.getEmail());
        assertEquals("alice123", customer.getUsername());
        assertEquals("A-0001", customer.getAccountId());
        assertNotNull(customer.getPasswordHash());
    }

    @Test
    public void testProfileUpdate() {
        customer.setName("Robert");
        customer.setEmail("robert@example.com");
        assertEquals("Robert", customer.getName());
        assertEquals("robert@example.com", customer.getEmail());
        assertNotEquals("Bob", customer.getName());
    }
}
