package j2uml;

public class Chunk {
    private String packageName;
    private StringBuilder umlFile;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public StringBuilder getUmlFile() {
        return umlFile;
    }

    public void setUmlFile(StringBuilder umlFile) {
        this.umlFile = umlFile;
    }
}
