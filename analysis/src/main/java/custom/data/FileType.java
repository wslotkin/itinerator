package custom.data;

public enum FileType {
    CSV(".csv", ","),
    TEXT(".txt", "\t");

    private final String extenstion;
    private final String delimiter;

    FileType(String extenstion, String delimiter) {
        this.extenstion = extenstion;
        this.delimiter = delimiter;
    }

    public String getExtenstion() {
        return extenstion;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
