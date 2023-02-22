

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class DriveStorage extends StorageManagement {

    private Drive service;
    private File root;
    private String configurationFile;

   public DriveStorage(Drive service) {
        this.service = service;
    }
    public DriveStorage() {
    DriveMainClass.init(this);
    }

    public Drive getService() {
        return service;
    }

    public void setService(Drive service) {
        this.service = service;
    }
    public File getRoot(){
        return this.root;
    }
    public String getRootID(){
        return this.root.getId();
    }
    public void setRoot(String fileID) {
        this.root = getFileById(fileID);
    }

    public void setRoot(File root) {
        this.root = root;
    }
    public void checkConnection(){
        if(this.service==null || this.getConfiguration()==null || this.getRootID()==null){
            throw new RuntimeException("Storage is not properly setup");
        }


    }
    boolean checkConfigRequirements(String directory, long fileSize, int fileNum,String extension) {
        if(this.getConfiguration().getIllegalExtentions().contains("."+extension)){
            System.err.println("Illegal extension");
            return false;
        }
        return checkConfigRequirements(directory,fileSize,fileNum);
    }
    boolean checkConfigRequirements(String directory, long fileSize, int fileNum){
//        List<String> parents=new ArrayList<>();
//        parents.add(getFileById(directory).getName());
//        for(String parent:listParentsRecursive(parents)){
//        if(checkConfigRequirements(directory,fileSize,fileNum)==false){
//            return false;
//        }
//        }

        if(directory==this.getRootID()){
            if(this.getConfiguration().getMaxFiles()>=(countFilesInDir(directory)+fileNum) && this.getConfiguration().getMaxSize()>=countMemoryInDir(directory)+fileSize)
                return true;

            System.err.println("Action doesnt compile file requirements \nMax files:"+this.getConfiguration().getMaxFiles()+" Files after action: "+ (countFilesInDir(directory)+fileNum)+"\n" + "Max memory:"+this.getConfiguration().getMaxSize()+" Memory after action: "+(countMemoryInDir(directory)+fileSize));
            return false;
        }
        else{
            if(this.getConfiguration().getMaxFileSubdirectories().get(directory)!=null && this.getConfiguration().getMaxFileSubdirectories().get(directory)<countFilesInDir(directory)+fileNum){
                System.err.println("Action doesnt compile file requirements \n Max files:"+this.getConfiguration().getMaxFileSubdirectories().get(directory)+"Files after action: "+(countFilesInDir(directory)+fileNum)+"\n" + "Max memory:"+this.getConfiguration().getMaxSizeSubdirectories().get(directory)+"Memory after action: "+(countMemoryInDir(directory)+fileSize));
                return false;
            }
            if(this.getConfiguration().getMaxSizeSubdirectories().get(directory)!=null && this.getConfiguration().getMaxSizeSubdirectories().get(directory)<countMemoryInDir(directory)+fileSize){
                System.err.println("Action doesnt compile file requirements \n Max files:"+this.getConfiguration().getMaxFileSubdirectories().get(directory)+"Files after action: "+(countFilesInDir(directory)+fileNum)+"\n" + "Max memory:"+this.getConfiguration().getMaxSizeSubdirectories().get(directory)+"Memory after action: "+(countMemoryInDir(directory)+fileSize));
                return false;
            }
                return true;
        }
    }

    int getFileSize(String fileID) {
        try {
            File f=service.files()
                    .get(fileID)
                    .setFields("name,parents,size")
                    .execute();
            FileList result = this.service.files().list()
                    .setFields("files(id,parents,size)")
                    .setQ("name='"+f.getName()+"' and parents='"+f.getParents().get(0)+"' and trashed=false")
                    .execute();
            for(File file:result.getFiles()){
                System.out.println(file.getSize());
            }
            return 1;
           // return new Long(result.getFiles().get(0).getSize()).intValue();
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }
    File getFileById(String fileID){
        try {
            return service.files()
                    .get(fileID)
                    .setFields("id,name,parents,size")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void updateConfigurationFile(){

        FileList result=null;
        try {
            result = service.files().list()
                    .setQ("name = 'configuration.txt' and trashed=false and parents ='" + this.getRootID() + "'")
                    .setFields("files(id, name, parents)")
                    .execute();
            if (!result.getFiles().isEmpty()) {
                for (File f : result.getFiles()) {
                    removeFiles(f.getId());
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            }


        File fileMetadata = new File();
        Configuration configuration=this.getConfiguration();
        java.io.File fileConfiguration=new java.io.File("configuration.txt");
        FileWriter myWriter = null;
        try {
            FileOutputStream file = new FileOutputStream(fileConfiguration);
            ObjectOutputStream out = new ObjectOutputStream(file);
            // Method for serialization of object
            out.writeObject(configuration);
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileMetadata.setName(fileConfiguration.getName());
        FileContent mediaContent = new FileContent(null, fileConfiguration);
        //checkConfigRequirements("null",(int)filePath.getTotalSpace(), 1);
        File file = null;
        try {
            file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        moveFiles(file.getId(),this.getRootID());
       fileConfiguration.delete();
    }
    @Override
    void createStorage(String s, Configuration configuration) {
        try {
        this.setRoot(createFolder(s));
        configuration.setName(s);
        configuration.setAbsolutePath(this.getRootID());
        this.setConfiguration(configuration);
        this.updateConfigurationFile();
        System.out.println(this.getConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    void loadStorage(String s) {
        try {
            java.io.File configurationFile = new java.io.File("configuration.txt");

            FileList result = this.service.files().list()
                    .setQ("name = 'configuration.txt' and trashed=false")
                    .setFields("files(id, name, parents)")
                    .execute();

            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("No configuration found.");
            } else {
                for (File file : files) {
                    download(file.getId(), configurationFile.getPath());
                }
            }


            if(configurationFile==null)
                System.out.println("Storage doesnt exsist");
            BufferedReader br = null;
            Configuration configuration;
                FileInputStream file = new FileInputStream(configurationFile);
                ObjectInputStream in = new ObjectInputStream(file);

                // Method for deserialization of object
                configuration = (Configuration)in.readObject();

                in.close();
                file.close();

                this.setConfiguration(configuration);
                this.setRoot(configuration.getAbsolutePath());
                //System.out.println("Majmune "+ configuration.getName());
                //this.setRoot();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    //!!!!!!!!DA LI TREBA KLASA FAJL!!!!!!!!!!!!//

    @Override
    void uploadFiles(MyFile myFile) {
        // TODO(developer) - See https://developers.google.com/identity for
        // guides on implementing OAuth2 for your application.
        Drive service=this.service;
        this.checkConnection();

            File fileMetadata = new File();
            fileMetadata.setName(myFile.getName());
            java.io.File filePath = new java.io.File(myFile.getPath());

            System.out.println("File PATH: "+ filePath);
        try {
            //filePath.length()
            //Files.size(Path.of(filePath.getPath()))
            if(this.checkConfigRequirements(this.getRootID(),filePath.length(),1,getExtensionByStringHandling(filePath.getPath()).orElse("empty"))){
                FileContent mediaContent = new FileContent(null, filePath);
                try {
                    File file = service.files().create(fileMetadata,mediaContent)
                            .setFields("id")
                            .execute();
                    moveFiles(file.getId(),getRootID());
                } catch (GoogleJsonResponseException e) {
                    // TODO(developer) - handle error appropriately
                    System.err.println("Unable to upload file: " + e.getDetails());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    boolean isValidFileID(String fileID){
        try {
            File result = this.service.files()
                    .get(fileID)
                    .execute();
            return true;
        }
        catch (Exception e){
            System.out.println("Invalid file ID");
            return false;
        }
    }


    @Override
    void uploadFiles(List<MyFile> myFiles) {
    for(MyFile myFile:myFiles){
        uploadFiles(myFile);
    }
    }

    @Override
    void removeFiles(String fileId) {
            try {
                this.service.files().delete(fileId).execute();
            } catch (IOException e) {
                System.out.println("Unable to delete files");
            }
        }

    @Override
    void removeFiles(List<String> fileIDs) {
    for(String fileId:fileIDs){
        removeFiles(fileId);
    }
    }

    List <String> listParents(String fileId){
        List<String> parents = new ArrayList<String>();
        parents.add(fileId);
        listParentsRecursive(parents);
        return parents;
    }

    List <String> listParentsRecursive(List<String> fileIDs){
        try {
            File f = this.service.files()
                    .get(fileIDs.get(fileIDs.size()-1))
                    .setFields("id, parents")
                    .execute();

                if(f.getParents()==null){
                    return fileIDs;
                }
                else{
                    fileIDs.add(f.getParents().get(0));
                    return listParentsRecursive(fileIDs);
                }
        }
        catch (Exception e){
            System.out.println("No files found");
            return null;
        }
    }

    List <String> listChildren(String fileID){


//        try {
//            FileList result = this.service.files().list()
//                    .setQ("parents contains '"+fileIDs.get(fileIDs.size()-1)+"'")
//                    .setFields("id, parents")
//                    .execute();
//            List<File> files=result.getFiles();
//            for(File f:files){
//                    fileIDs.add(f.getId());
//                    return listParents(fileIDs);
//            }
//        }
//        catch (Exception e){
//            //System.out.println("No files found");
//            return fileIDs;
//        }
//        return fileIDs;
            return null;
    }

    @Override
    void moveFiles(String fileId, String folderId) {
        if(this.checkConfigRequirements(folderId,getFileSize(fileId),1)){
            File file = null;
            try {
                file = service.files().get(fileId).setFields("*")
                        .execute();
            } catch (IOException e) {
                System.out.println("Unable to get fileID");
            }
            StringBuilder previousParents = new StringBuilder();
            for (String parent : file.getParents()) {
                previousParents.append(parent);
                previousParents.append(',');
            }
            try {
                file = service.files().update(fileId, null)
                        .setAddParents(folderId)
                        .setRemoveParents(previousParents.toString())
                        .setFields("id, parents")
                        .execute();
            } catch (GoogleJsonResponseException e) {
                // TODO(developer) - handle error appropriately
                System.err.println("Unable to move file: " + e.getDetails());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String DriveMoveFiles(String fileId, String folderId) {
        File file = null;
        try {
            file = service.files().get(fileId)
                    .setFields("parents")
                    .execute();
        } catch (IOException e) {
            System.out.println("Unable to get fileID");
        }
        StringBuilder previousParents = new StringBuilder();
        for (String parent : file.getParents()) {
            previousParents.append(parent);
            previousParents.append(',');
        }
        try {
            // Move the file to the new folder
            file = service.files().update(fileId, null)
                    .setAddParents(folderId)
                    .setRemoveParents(previousParents.toString())
                    .setFields("id, parents")
                    .execute();
            return file.getId();

        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to move file: " + e.getDetails());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void download(String realFileId, String FileName){
        ByteArrayOutputStream ByteArrayOutputStream  = null;
        try {
            ByteArrayOutputStream  = new ByteArrayOutputStream();

            this.service.files().get(realFileId)
                    .executeMediaAndDownloadTo(ByteArrayOutputStream );

        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to move file: " + e.getDetails());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(OutputStream outputStream = new FileOutputStream(FileName)) {
            ByteArrayOutputStream.writeTo(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int countMemoryInDir(String directoryID) {
        int fileSize=0;
        try {
            String pageToken = null;
            do {
                FileList result = this.service.files().list()
                        .setFields("nextPageToken,files(id,parents,mimeType,size)")
                        .setQ("parents='"+directoryID+"' and trashed=false")
                        .setPageToken(pageToken)
                        .execute();
                List<File> files = result.getFiles();
                for (File file : files) {
                    if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                        fileSize+=countMemoryInDir(file.getId());
                    }
                    else{
                       fileSize+=file.getSize();
                    }
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        }
        catch (Exception e){
            System.out.println(e);
            return fileSize;
        }
        return fileSize;
    }


    int countFilesInDir(String directoryID) {
        int fileNum=0;
        try {
            String pageToken = null;
            do {
                FileList result = this.service.files().list()
                        .setFields("nextPageToken,files(id,parents,mimeType)")
                        .setQ("parents='"+directoryID+"' and trashed=false ")
                        .setPageToken(pageToken)
                        .execute();
                List<File> files = result.getFiles();
                for (File file : files) {
                    if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                        fileNum+=countFilesInDir(file.getId());
                        fileNum+=1;
                    }
                    else{
                        fileNum++;
                    }
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        }
        catch (Exception e){
            System.out.println(e);
            return fileNum;
        }
        return fileNum;
    }


    @Override
    void rename(String s, String s1, String s2) {
//        try {
//           this.service.files().get(s).setFields("name,"+s2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        //this.service.files().update()
//        Drive service = this.service;
//    File file = null;
//
//        try {
//        file = service.files().get(fileId)
//                .setFields("name")
//                .execute();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
    }



    @Override
    List<MyFile> searchDirectory(String s, boolean b, Filter filter) {
    return null;
    }

    @Override
    List<MyFile> searchDirectory(String s, SortType sortType, boolean b) {
        List<MyFile> myFiles = new ArrayList<MyFile>();

        try {
            FileList result = this.service.files().list()
                    .setFields("files(id,name,modifiedTime,parents,size)")
                    .setQ("parents contains '"+s+"'")
                    .execute();
            List<File> files=result.getFiles();
            for (File file : files) {
                myFiles.add(driveFileToMyFile(file)) ;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }

    MyFile driveFileToMyFile(File file) {
        MyFile myFile=null;
        if(file.getName()!=null && file.getModifiedTime()!=null && file.getParents()!=null)
            myFile=new MyFile(file.getName(),new Date(file.getModifiedTime().getValue()),".test",file.getParents().toString(),file.size());
        else
            myFile=new MyFile("error",new Date(),".test","roditelji",5);
        return myFile;
    }



    public void listDriveRoot() {
        FileList result = null;
        try {
            result = this.service.files().list()
                    .setFields("files(id, name,parents)")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId(),file.getParents());
            }
        }
    }

    @Override
    List<MyFile> searchDirectoryAll(String s, boolean b) {
        List<MyFile> myFiles = new ArrayList<MyFile>();

        try {
            FileList result = this.service.files().list()
                    .setFields("files(id,name,modifiedTime,parents,size)")
                    .execute();
            List<File> files=result.getFiles();
            for (File file : files) {
                myFiles.add(driveFileToMyFile(file)) ;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }

    @Override
    List<MyFile> searchDirectoryExtension(String s, String s1) {
        if(s.contains(".")){
            return  searchDirectoryNameContains(s,s1);
        }else{
            return searchDirectoryNameContains("."+s,s1);
        }
    }

    @Override
    List<MyFile> searchDirectoryNameContains(String s, String s1) {
        List<MyFile> myFiles = new ArrayList<MyFile>();

        String pageToken = null;
        try {
            do {
                FileList result = this.service.files().list()
                        //      .setQ("name contains '"+s+"' and parents='"+s1+"'")
                        .setQ("name contains '"+s+"'")
                        .setFields("nextPageToken, files(id,name,modifiedTime,fileExtension,parents,size)")
                        .execute();
                for (File file : result.getFiles()) {
//                    if(file.getParents().contains(s1)){
                        myFiles.add(driveFileToMyFile(file)) ;
//                    }
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(myFiles.size());
        return myFiles;
    }

    @Override
    List<MyFile> searchDirectoryName(String s, String s1) {
        List<MyFile> myFiles = new ArrayList<MyFile>();

        String pageToken = null;
        try {
            do {
                FileList result = this.service.files().list()
                        .setQ("name = '"+s+"'")
                        .setFields("nextPageToken, files(id,name,modifiedTime,fileExtension,parents,size)")
                        .execute();
                for (File file : result.getFiles()) {
                    myFiles.add(driveFileToMyFile(file)) ;
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(myFiles.size());
        return myFiles;
    }

    @Override
    MyFile searchDirectoryReturnFolder(String s) {

//        try {
//                File result = this.service.files()
//                        .get(s)
//                        .setFields("parents")
//                        .execute();
//               return result.getParents().get(0).toString();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
        if(searchDirectoryName(s,s).get(0)!=null)
            return searchDirectoryName(s,s).get(0);
        return null;
    }

    @Override
    List<MyFile> searchDirectoryPeriod(String s, String s1, String s2) {
        List<MyFile> myFiles = new ArrayList<MyFile>();
        //DateTime startPeriod=new DateTime(s1);
        //DateTime endPeriod=new DateTime(s2);
        try {
                FileList result = this.service.files().list()
                       // .setQ(" modifiedTime > '"+s1+"' and modifiedTime < '"+s2+"' and parents contains '"+s+"'")
                        //    .setQ(" modifiedTime > '"+s1+"' and modifiedTime < '"+s2+"' and parents = '"+s+"'")
                        .setQ(" modifiedTime > '"+s1+"' and modifiedTime < '"+s2+"' and parents = '"+s+"'")
                        //.setQ("modifiedTime > '2012-06-04T12:00:00'")
                        .setFields("files(id,name,modifiedTime,fileExtension,parents,size)")
                        .execute();
                for (File file : result.getFiles()) {
                    myFiles.add(driveFileToMyFile(file)) ;
                }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }

    //Pomocne metode
    public String createFolder(String name) throws IOException {
        // File's metadata.
        File fileMetadata = new File();
        fileMetadata.setName(name);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = this.service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}