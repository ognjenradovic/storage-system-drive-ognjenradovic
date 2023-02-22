import java.util.Date;
import java.util.List;

public abstract class StorageManagement {

    private Configuration configuration;


    public StorageManagement(){


    }

    //Klasa za direktorijum??


    abstract void createStorage(String path,Configuration configuration);
        //Pravi konfiguraciju
        //mozda treba skinuti public

    abstract void loadStorage(String path);

    abstract void uploadFiles(MyFile myFile);
    abstract void uploadFiles(List<MyFile> myFiles);

    abstract void removeFiles(String paths);
    abstract void removeFiles(List<String> paths);

    abstract void moveFiles(String filePath,String destinationPath);

    //U downloadu se vrv prima apsoultna putanja kad se skida sa lokalnom
    abstract void download(String path,String destinationPath);


    abstract void rename(String path,String parentPath,String newName);

    //search nad skladistem
    //Objekat Filter
    //boolean name
    //boolean date
    //boolean path

  //  searchDirectory("c://fawfawfawf",new Filter (true,true,false));



    abstract List<MyFile> searchDirectory(String directoryPath, boolean asc,Filter filter);
    abstract List<MyFile> searchDirectory(String directoryPath, SortType sortType, boolean asc); // moze i enum
    abstract List<MyFile> searchDirectoryAll(String directoryPath,boolean asc);
    abstract List<MyFile> searchDirectoryExtension(String directoryPath,String extension);
    //mozda ext u sortType da ide kao enumi
    //Mozda treba???
    //abstract void searchDirectoryExtension(String directoryPath,String extension, String sortType);
    abstract List<MyFile> searchDirectoryNameContains(String directoryPath,String nameSubstring);
    abstract List<MyFile> searchDirectoryName(String directoryPath,String fileName);
    abstract MyFile searchDirectoryReturnFolder(String directoryPath);
    abstract List<MyFile> searchDirectoryPeriod(String directoryPath, String startDate, String endDate);

    //Dodati filtriranje
    //search -name program -f name,datecreated (-f je filter
    //abstract void filterSearch(List<String> searchResultSet,String name);
    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }





    /* TODO FALI NAM JOS JEDNA METODA SLICNA searchDirectoryAll PITAJ */





}
