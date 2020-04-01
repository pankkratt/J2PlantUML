package j2uml;

import java.io.*;
import java.util.*;

public class Worker {
    Collection<File> javaFiles;


    public void updateUml() {
        Map<String, List<StringBuilder>> umlFiles = new HashMap<>();
        Set<String> classNames = new HashSet<>();

        takeJavaFromSrc();
        for (File javaFile : javaFiles) {

            String className = javaFile.getName();
            className = className.replace(".java", "");
            classNames.add(className);

            StringBuilder content = takeContent(javaFile);
            Processor processor = new Processor();
            Chunk chunk = processor.process(content);
            String packageName = chunk.getPackageName();
            StringBuilder umlFile = chunk.getUmlFile();
            if (umlFiles.containsKey(packageName)) {
                List<StringBuilder> list = umlFiles.get(packageName);
                list.add(umlFile);
                umlFiles.put(packageName, list);
            } else {
                List<StringBuilder> list = new ArrayList<>();
                list.add(umlFile);
                umlFiles.put(packageName, list);
            }
        }
        PlantUmlBuilder plantUmlBuilder = new PlantUmlBuilder();
        String plantUmlFile = plantUmlBuilder.buildPlantUmlFile(umlFiles, classNames);
        String path = "./current.puml";
        giveContent(plantUmlFile, path);
        System.out.println(plantUmlFile);
    }

    private void giveContent(String umlFile, String umlFilePath) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(umlFilePath);
            fw.write(umlFile);
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

    private void takeJavaFromSrc() {
        javaFiles = new ArrayList<>();
        addTree(new File("."), javaFiles);
    }

    private void addTree(File file, Collection<File> all) {
        // copy/paste
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                String path = null;
                try {
                    path = child.getCanonicalPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (path != null && path.contains("java") && path.contains("src") && !path.contains("j2uml")) {
                    all.add(child);
                }
                addTree(child, all);
            }
        }
    }
}
