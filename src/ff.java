import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class ff {
    Collection<File> javaFiles;

    public void updateUml() {
        takeJavaFromSrc();
        for (File javaFile : javaFiles) {
            StringBuilder content = takeContent(javaFile);
            String umlFile = createUmlFile(javaFile);
            Processor processor = new Processor();
            content = processor.process(content);
            giveContent(content, umlFile);
        }
    }

    private void giveContent(StringBuilder content, String umlFile) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(umlFile);
            fw.write(String.valueOf(content));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private StringBuilder takeContent(File javaFile) {
        StringBuilder builder = new StringBuilder();
        FileReader fr = null;
        Scanner sc = null;
        try {
            fr = new FileReader(javaFile);
            sc = new Scanner(fr);
            while (sc.hasNextLine()) {
                builder.append(sc.nextLine());
                builder.append("\n");
            }
            return builder;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (sc != null) {
                    sc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String createUmlFile(File javaFile) {
        String parent = javaFile.getParent().replaceAll("src", "uml");
        String name = javaFile.getName().replaceAll("java", "puml");
        File file = new File(parent);
        file.mkdirs();
        file = new File(parent, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }

    private void takeJavaFromSrc() {
        javaFiles = new ArrayList<File>();
        addTree(new File("."), javaFiles);
    }

    private void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                String path = null;
                try {
                    path = child.getCanonicalPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (path.contains("java") && path.contains("src")) {
                    all.add(child);
                }
                addTree(child, all);
            }
        }
    }

}
