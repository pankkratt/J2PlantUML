package j2uml;

import java.util.HashMap;
import java.util.Map;

public class Processor {
    private StringBuilder content;

    public Chunk process(StringBuilder content) {
        Chunk chunk = new Chunk();
        this.content = content;
        removeMethodBodies();
        removeEmptyRows();
        String packageName = cutHead();
        chunk.setPackageName(packageName);
        changeModifiers();
        removeSemicolons();
        removeAnnotations();
        chunk.setUmlFile(content);
        return chunk;
    }

    private void removeAnnotations() {
        while (true) {
            int index = content.indexOf("@");
            if (index == -1) {
                break;
            }
            int from = content.lastIndexOf("\n", index);
            int to = content.indexOf("\n", index);
            content.replace(from, to, "");
        }
    }

    private String cutHead() {
        String packageName = "None";

        int from = content.indexOf("package ");
        if (from != -1) {
            packageName = content.substring(content.indexOf(" ", from + 1) + 1,
                    content.indexOf(";"));
            int to = content.indexOf("\n", from);
            content.delete(from, to);
        }
        while (true) {
            from = content.indexOf("import ");
            int to = content.indexOf("\n", from);
            if (from != -1) {
                content.delete(from, to);
            } else {
                break;
            }
        }
        while (content.charAt(0) == '\n') {
            content.deleteCharAt(0);
        }
        return packageName;
    }

    private void removeMethodBodies() {
        int cursor = content.indexOf("{") + 1;
        while (content.indexOf("{", cursor) != -1) {
            int from = content.indexOf("{", cursor);
            int to = content.indexOf("}", cursor);
            int nextFrom = content.indexOf("{", from + 1);
            while (true) {
                if (nextFrom != -1 && nextFrom < to) {
                    nextFrom = content.indexOf("{", nextFrom + 1);
                    to = content.indexOf("}", to + 1);
                } else {
                    break;
                }
            }
            content.delete(from, to + 1);
        }
    }

    private void removeEmptyRows() {
        int start = content.indexOf(")");
        while (true) {
            int i = content.indexOf("\n\n", start);
            if (i != -1) {
                content.delete(i, i + 1);
            } else {
                break;
            }
        }
    }

    private void removeSemicolons() {
        while (true) {
            int i = content.indexOf(";");
            if (i != -1) {
                content.deleteCharAt(i);
            } else {
                break;
            }
        }
    }

    private void changeModifiers() {
        Map<String, String> modifiers = new HashMap<>();
        modifiers.put("public ", "+");
        modifiers.put("private ", "-");
        modifiers.put("protected ", "#");
        modifiers.put("static ", "{static}");

        for (String modifier : modifiers.keySet()) {
            while (true) {
                int i = content.indexOf(modifier);
                if (i != -1) {
                    content.replace(i, i + modifier.length(), modifiers.get(modifier));
                } else {
                    break;
                }
            }
        }
        int cursor = 0;
        while (true) {
            int i = content.indexOf("\n    ", cursor);
            if (i == -1) {
                break;
            }
            int i1 = content.indexOf("\n    +", cursor);
            int i2 = content.indexOf("\n    -", cursor);
            int i3 = content.indexOf("\n    #", cursor);
            if (i != i1 && i != i2 && i != i3) {
                content.insert(i + "\n    ".length(), "~");
            }
            cursor = ++i;
        }
        char ch = content.charAt(0);
        if (ch != '+' && ch != '-' && ch != '#') {
            content.insert(0, "~");
        }
    }

}
