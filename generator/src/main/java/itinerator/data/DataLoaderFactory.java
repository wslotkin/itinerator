package itinerator.data;

public class DataLoaderFactory {

    public static DataLoader createForFile(String filename) {
        for (FileType fileType : FileType.values()) {
            if (filename.contains(fileType.getExtenstion())) {
                return create(fileType);
            }
        }
        throw new IllegalArgumentException("Unrecognized input file: " + filename);
    }

    private static DataLoader create(FileType fileType) {
        switch (fileType) {
            case CSV:
                return new CsvDataLoader();
            case TEXT:
                return new TextDataLoader();
            default:
                throw new IllegalArgumentException("Unexpected file type: " + fileType);
        }
    }
}
