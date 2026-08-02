package bank.test;

import bank.data.FileHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {

    private final String testFile = "data/test-file-handler.dat";

    @BeforeEach
    public void setup() {
        File parent = new File("data");
        if (!parent.exists()) {
            assertTrue(parent.mkdirs());
        }
    }

    @AfterEach
    public void cleanup() {
        File f = new File(testFile);
        if (f.exists()) {
            assertTrue(f.delete());
        }
    }

    @Test
    public void testSaveAndLoadData() {
        List<String> input = new ArrayList<>();
        input.add("item1");
        input.add("item2");
        FileHandler.saveList(input, testFile);

        List<String> output = FileHandler.loadList(testFile);
        assertEquals(2, output.size());
        assertEquals("item1", output.get(0));
        assertEquals("item2", output.get(1));
        assertDoesNotThrow(() -> FileHandler.loadList(testFile));
    }
}
