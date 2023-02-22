public class ExporterManager {

    private static StorageManagement storageManagement;

    public static void registerStorage(StorageManagement storage) {
        storageManagement = storage;
    }

    public static StorageManagement getStorage() {
        //dbExporter.setFileName(fileName);
        return storageManagement;
    }


}
