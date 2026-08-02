package bank.data;

import java.io.*;
import java.util.*;

public class FileHandler {

    @SuppressWarnings("unchecked")
    public static synchronized <T> List<T> loadList(String file) {
        File f = new File(file);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static synchronized <T> void saveList(List<T> list, String file) {
        File f = new File(file);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
