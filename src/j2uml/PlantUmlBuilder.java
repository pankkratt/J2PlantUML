package j2uml;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlantUmlBuilder {
    private StringBuilder plantUmlFile;
    private StringBuilder links;

    public PlantUmlBuilder() {
        plantUmlFile = new StringBuilder();
        links = new StringBuilder();
    }

    public String buildPlantUmlFile(Map<String, List<StringBuilder>> umlFiles, Set<String> classNames) {
        plantUmlFile.append("@startuml\n");
        for (String packageName : umlFiles.keySet()) {
            List<StringBuilder> classUmls = umlFiles.get(packageName);
            if (!packageName.equals("None")) {
                plantUmlFile.append("package ").append(packageName).append(" {\n");
            }
            for (StringBuilder classUml : classUmls) {
                if (!getCurrentClassName(classNames, classUml).equals("Main")) {
                    plantUmlFile.append(classUml);
                    addLinks(classNames, classUml);
                }
            }
            if (!packageName.equals("None")) {
                plantUmlFile.append("}\n");
            }
        }
        plantUmlFile.append(links);
        plantUmlFile.append("@enduml\n");
        return String.valueOf(plantUmlFile);
    }

    private void addLinks(Set<String> classNames, StringBuilder classUml) {
        String currentClassName = getCurrentClassName(classNames, classUml);
        for (String className : classNames) {
            if (!className.equals(currentClassName)) {
                int from = classUml.indexOf("{");
                int to = classUml.indexOf("\n\n");
                if (classUml.indexOf(className + " ", from) != -1 && classUml.lastIndexOf(className, to) != -1) {
                    links.append(currentClassName);
                    links.append(" o--- ");
                    links.append(className);
                    links.append("\n");
                }
            }
        }
    }

    private String getCurrentClassName(Set<String> classNames, StringBuilder classUml) {
        int minIndex = Integer.MAX_VALUE;
        String currentClassName = "";
        for (String className : classNames) {
            int from = classUml.indexOf("\n");
            int index = classUml.lastIndexOf(" " + className, from);
            if (index != -1 && index < minIndex) {
                currentClassName = className;
            }
        }
        return currentClassName;
    }
}
