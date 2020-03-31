package j2uml;

import java.util.List;
import java.util.Map;

public class PlantUmlBuilder {
    private StringBuilder plantUmlFile;
    private Map<String, List> umlFiles;

    public PlantUmlBuilder(Map<String, List> umlFiles) {
        this.umlFiles = umlFiles;
        plantUmlFile = new StringBuilder();
    }

    public String buildPlantUmlFile() {
        plantUmlFile.append("@startuml\n");
        for (String s : umlFiles.keySet()) {
            List list = umlFiles.get(s);
            if (s != "None") {
                plantUmlFile.append("package " + s + " {\n");
            }
            for (Object o : list) {
                plantUmlFile.append(o);
            }
            if (s != "None") {
                plantUmlFile.append("}\n");
            }
        }
        plantUmlFile.append("@endtuml\n");
        return String.valueOf(plantUmlFile);
    }
}
