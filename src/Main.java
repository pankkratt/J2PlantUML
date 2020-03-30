import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    String fromSource;
    StringBuilder content;




    // This method writes a string to the system clipboard.
    // otherwise it returns null.
    public void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }
}
