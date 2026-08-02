package bank.test;

import bank.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilPasswordUtilTest {

    @Test
    public void testGenerateSaltAndHash() {
        String salt = PasswordUtil.generateSalt();
        assertNotNull(salt);
        assertFalse(salt.isBlank());

        String hash = PasswordUtil.hash("securePassword123", salt);
        assertNotNull(hash);
        assertFalse(hash.isBlank());
        assertNotEquals(salt, hash);
    }

    @Test
    public void testVerifyAcceptsCorrectPasswordAndRejectsWrongPassword() {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash("myPassword", salt);

        assertTrue(PasswordUtil.verify("myPassword", salt, hash));
        assertFalse(PasswordUtil.verify("wrongPassword", salt, hash));
    }

    @Test
    public void testDifferentPasswordsProduceDifferentHashesForSameSalt() {
        String salt = PasswordUtil.generateSalt();
        String firstHash = PasswordUtil.hash("password1", salt);
        String secondHash = PasswordUtil.hash("password2", salt);

        assertNotEquals(firstHash, secondHash);
        assertFalse(PasswordUtil.verify("password2", salt, firstHash));
    }
}
