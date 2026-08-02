package bank.test;

import bank.entity.Customer;
import bank.singleton.DataManager;
import bank.ui.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class UiTest {

    private static final String TEST_DIR = "data/test-ui";

    @BeforeEach
    public void setup() {
        DataManager.useDataDirectory(TEST_DIR);
        new File(TEST_DIR).mkdirs();
    }

    @AfterEach
    public void cleanup() {
        DataManager.resetInstance();
        DataManager.useDataDirectory("data");
        File dir = new File(TEST_DIR);
        if (dir.exists()) {
            deleteRecursively(dir);
        }
    }

    @Test
    public void testUiThemeCreatesStyledComponents() {
        JButton primary = UITheme.primaryButton("Save");
        JButton flat = UITheme.flatButton("Cancel");
        JTextField field = UITheme.textField();
        JPasswordField passwordField = UITheme.passwordField();
        JPanel card = UITheme.card();

        assertNotNull(primary);
        assertNotNull(flat);
        assertNotNull(field);
        assertNotNull(passwordField);
        assertNotNull(card);
        assertEquals("Save", primary.getText());
        assertEquals("Cancel", flat.getText());
        assertEquals(UITheme.PRIMARY, primary.getBackground());
        assertEquals(UITheme.CARD_BG, flat.getBackground());
    }

    @Test
    public void testLoginFrameCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        LoginFrame frame = createOnEdt(() -> new LoginFrame());
        assertNotNull(frame);
        assertEquals("SecureBank - Login", frame.getTitle());
        frame.dispose();
    }

    @Test
    public void testRegisterFrameCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        RegisterFrame frame = createOnEdt(() -> new RegisterFrame());
        assertNotNull(frame);
        assertEquals("SecureBank - Register", frame.getTitle());
        frame.dispose();
    }

    @Test
    public void testTransferDialogCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Customer customer = new Customer("C-1001", "Test", "test@example.com", "tester",
                "hash", "salt", "Q?", "ansHash", "ansSalt", "A-1001", 100.0);
        TransferDialog dialog = createOnEdt(() -> new TransferDialog(new Frame(), customer));
        assertNotNull(dialog);
        assertEquals("Transfer Money", dialog.getTitle());
        dialog.dispose();
    }

    @Test
    public void testForgotPasswordFrameCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        ForgotPasswordFrame frame = createOnEdt(ForgotPasswordFrame::new);
        assertNotNull(frame);
        assertEquals("SecureBank - Reset Password", frame.getTitle());
        frame.dispose();
    }

    @Test
    public void testEditProfileDialogCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Customer customer = new Customer("C-1002", "Alice", "alice@example.com", "alice",
                "hash", "salt", "Q?", "ansHash", "ansSalt", "A-1002", 50.0);
        EditProfileDialog dialog = createOnEdt(() -> new EditProfileDialog(new Frame(), customer));
        assertNotNull(dialog);
        assertEquals("Edit Profile", dialog.getTitle());
        dialog.dispose();
    }

    @Test
    public void testAdminDashboardCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        AdminDashboard frame = createOnEdt(AdminDashboard::new);
        assertNotNull(frame);
        assertEquals("SecureBank - Admin Panel", frame.getTitle());
        frame.dispose();
    }

    @Test
    public void testCustomerDashboardCreatesWithExpectedTitle() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Customer customer = new Customer("C-1003", "Bob", "bob@example.com", "bob",
                "hash", "salt", "Q?", "ansHash", "ansSalt", "A-1003", 200.0);
        CustomerDashboard frame = createOnEdt(() -> new CustomerDashboard(customer));
        assertNotNull(frame);
        assertEquals("SecureBank - Dashboard", frame.getTitle());
        frame.dispose();
    }

    private static <T> T createOnEdt(java.util.function.Supplier<T> supplier) throws Exception {
        AtomicReference<T> ref = new AtomicReference<>();
        AtomicReference<Exception> error = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            try {
                ref.set(supplier.get());
            } catch (Exception ex) {
                error.set(ex);
            }
        });
        if (error.get() != null) {
            throw error.get();
        }
        return ref.get();
    }

    private static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        file.delete();
    }
}
