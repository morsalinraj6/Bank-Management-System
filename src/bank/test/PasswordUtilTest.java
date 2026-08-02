package bank.test;

import bank.service.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testGenerateSaltAndHash() {
        String salt = PasswordUtil.generateSalt();
        assertNotNull(salt);
        assertFalse(salt.isBlank());

        String hash = PasswordUtil.hash("password123", salt);
        assertNotNull(hash);
        assertFalse(hash.isBlank());
        assertNotEquals(salt, hash);
    }

    @Test
    public void testPasswordVerification() {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash("secret", salt);
        assertTrue(PasswordUtil.verify("secret", salt, hash));
        assertFalse(PasswordUtil.verify("wrong", salt, hash));
        assertDoesNotThrow(() -> PasswordUtil.verify("secret", salt, hash));
    }
}
