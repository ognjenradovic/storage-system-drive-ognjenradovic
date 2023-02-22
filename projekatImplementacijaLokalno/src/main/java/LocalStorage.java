import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LocalStorage extends StorageManagement{

    static {
        ExporterManager.registerStorage(new LocalStorage());
    }


    public LocalStorage(){
    super();
    }
    @Override
    void createStorage(String s, Configuration configuration) {
    File storageFolder=createFolder(configuration.getAbsolutePath(),configuration.getName());
    File fileConfiguration=new File(storageFolder.getPath(),"configuration.txt");
    FileWriter myWriter = null;
    this.setConfiguration(configuration);
        try {
            FileOutputStream file = new FileOutputStream(fileConfiguration);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(configuration);

            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    void loadStorage(String s) {
        File configurationFolder=new File(s);
        File configurationFile=new File(s+"//configuration.txt");
        if(configurationFolder==null || configurationFile==null)
            System.out.println("Storage doesnt exsist");
        BufferedReader br = null;
        Configuration configuration = null;
        try {
            FileInputStream file = new FileInputStream(configurationFile);
            ObjectInputStream in = new ObjectInputStream(file);

            configuration = (Configuration)in.readObject();

            in.close();
            file.close();

            this.setConfiguration(configuration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    void uploadFiles(MyFile myFile) {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {

            File afile = new File(myFile.getPath());
            File bfile = new File(getConfiguration().getAbsolutePath() +"\\"+ getConfiguration().getName());
            System.out.println(afile.getPath());
            System.out.println(bfile.getPath());

            inStream = new FileInputStream(afile);

            if (bfile.isDirectory()){
                System.out.println("Unesite ime novog fajl: ");
                Scanner in = new Scanner(System.in);
                String strFileName = in.nextLine();
                File copyFile = new File(bfile + "\\" + strFileName);
                outStream = new FileOutputStream(copyFile);
            } else {
                outStream = new FileOutputStream(bfile);
            }

            byte[] buffer = new byte[1024 * 4];

            int length;
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            System.out.println("File is uploaded successful!");

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void uploadFiles(List<MyFile> myFiles) {
        for (MyFile myFile: myFiles) {
            InputStream inStream = null;
            OutputStream outStream = null;
            try {

                File afile = new File(myFile.getPath());
                File bfile = new File(getConfiguration().getAbsolutePath() +"\\"+ getConfiguration().getName());
                System.out.println(afile.getPath());
                System.out.println(bfile.getPath());

                inStream = new FileInputStream(afile);

                if (bfile.isDirectory()){
                    System.out.println("Unesite ime novog fajl: ");
                    Scanner in = new Scanner(System.in);
                    String strFileName = in.nextLine();
                    File copyFile = new File(bfile + "\\" + strFileName);
                    outStream = new FileOutputStream(copyFile);
                } else {
                    outStream = new FileOutputStream(bfile);
                }

                byte[] buffer = new byte[1024 * 4];

                int length;
                while ((length = inStream.read(buffer)) > 0) {

                    outStream.write(buffer, 0, length);

                }

                System.out.println("File is uploaded successful!");

            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    void removeFiles(String s) {
        s = getConfiguration().getAbsolutePath() + "\\"+ getConfiguration().getName()+"\\" + s;
        System.out.println(s);
        try
        {
            File f= new File(s);
            if(f.delete())
            {
                System.out.println(f.getName() + " deleted");
            }
            else
            {
                System.out.println("Deletion failed");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    void removeFiles(List<String> list) {
        for(String path:list){
            try
            {
                File f= new File(path);
                if(f.delete())
                {
                    System.out.println(f.getName() + " deleted");
                }
                else
                {
                    System.out.println("Deletion failed");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

    }
    }

    @Override
    void moveFiles(String s, String s1) {
        File sourceFolder = new File(getConfiguration().getAbsolutePath() + "\\" +getConfiguration().getName() + "\\" +s);
        File destinationFolder = new File(getConfiguration().getAbsolutePath() +  "\\"+ getConfiguration().getName() + "\\" +s1);
        System.out.println("Source: " + sourceFolder.getAbsolutePath());
        System.out.println("Dest: " + destinationFolder.getAbsolutePath());

        if (!destinationFolder.exists())
        {
            System.out.println("ne postoji destination folder");
        }

        if (sourceFolder.exists() && sourceFolder.isDirectory())
        {
            File[] listOfFiles = sourceFolder.listFiles();

            if (listOfFiles != null)
            {
                for (File child : listOfFiles )
                {
                    child.renameTo(new File( destinationFolder + "\\" + child.getName()));
                }

            }
        } else if(!sourceFolder.isDirectory()){

            sourceFolder.renameTo(new File(destinationFolder + "\\" + sourceFolder.getName()));
        }
        else
        {
            System.out.println(sourceFolder + "  Folder does not exists");
        }

    }

    @Override
    void download(String s, String s1) {

        InputStream inStream = null;
        OutputStream outStream = null;
        s = getConfiguration().getAbsolutePath() + "\\"+ getConfiguration().getName()+"\\" + s;
        //s1 = getConfiguration().getAbsolutePath() + "\\" + s1;

        try {

            File afile = new File(s);
            File bfile = new File(s1);
            System.out.println(bfile.isDirectory());

            inStream = new FileInputStream(afile);

            if (bfile.isDirectory()){
                System.out.println("Unesite ime novog fajl: ");
                Scanner in = new Scanner(System.in);
                String strFileName = in.nextLine();
                File copyFile = new File(bfile + "\\" + strFileName);
                outStream = new FileOutputStream(copyFile);
            } else {
                outStream = new FileOutputStream(bfile);
            }

            byte[] buffer = new byte[1024 * 4];

            int length;
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            System.out.println("File is downloaded successful!");

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    void rename(String s, String p,String s1) {
        File file = new File(s);

        File rename = new File(p+"//"+s1);

        boolean flag = file.renameTo(rename);

        if (flag == true) {
            System.out.println("File Successfully Rename");
        }
        else {
            System.out.println("Operation Failed");
        }
    }

    @Override
    List<MyFile> searchDirectory(String s, boolean b, Filter filter) {

        s = getConfiguration().getAbsolutePath() + "\\" + s;
        File directory = new File(getConfiguration().getAbsolutePath()+s);
        List<MyFile> myFiles = new ArrayList<>();
        System.out.println("Unesite po kojim parametrima zelite da pretrazice direktorijum...");

        File[] files = directory.listFiles();
        for(File file : files){
            MyFile myFile = fileToMyFile(file);
            myFiles.add(myFile);
        }
        System.out.println(filter.filtriraj(myFiles));

        return null;
    }

    @Override
    List<MyFile> searchDirectory(String s, SortType sortType, boolean b) {
        return null;
    }


    @Override
    List<MyFile> searchDirectoryAll(String s, boolean b) {
        s = getConfiguration().getAbsolutePath() + "\\" + s;
        return searchDirectoryRekurzive(s,b);
    }

    @Override
    List<MyFile> searchDirectoryExtension(String s, String s1) {
        //exe
        s = getConfiguration().getAbsolutePath() + "\\" + s;
        File directory = new File(s);
        List<MyFile> myFiles = new ArrayList<>();

        File[] files = directory.listFiles();
        for(File file : files){
            MyFile myFile = fileToMyFile(file);
            if(getExtensionByStringHandling(file.getName()).orElse("").equals(s1)) {
                myFiles.add(myFile);
            }
        }
        for (MyFile myFile : myFiles){
            System.out.println(myFile.getName());
        }

        return myFiles;
    }

    @Override
    List<MyFile> searchDirectoryNameContains(String s, String s1) {
        //dir
        s = getConfiguration().getAbsolutePath() + "\\" + s;
        File directory = new File(s);
        List<MyFile> myFiles = new ArrayList<>();

        File[] files = directory.listFiles();
        for(File file : files){
            MyFile myFile = fileToMyFile(file);
            if(myFile.getName().contains(s1)) {
                myFiles.add(myFile);
            }
        }
        for (MyFile myFile : myFiles){
            System.out.println(myFile.getName());
        }
        return myFiles;
    }

    @Override
    List<MyFile> searchDirectoryName(String s, String s1) {
        s = getConfiguration().getAbsolutePath() + "\\" + s;

        File directory = new File(s);
        List<MyFile> myFiles = new ArrayList<>();

        File[] files = directory.listFiles();
        for(File file : files){
            MyFile myFile = fileToMyFile(file);
            if(myFile.getName().equals(s1)) {
                myFiles.add(myFile);
            }
        }
        for (MyFile myFile : myFiles){
            System.out.println(myFile.getName());
        }
        return null;
    }

    @Override
    MyFile searchDirectoryReturnFolder(String s) {

        s = getConfiguration().getAbsolutePath() + "\\" +s;
        File file = new File(s);

        String str = file.getParentFile().getName();
        MyFile myFile = fileToMyFile(file);
        System.out.println(file.getParentFile().getName());
        return myFile;
    }

    @Override
    List<MyFile> searchDirectoryPeriod(String s, String s1, String s2) {
        s = getConfiguration().getAbsolutePath() + "\\" +s;

        File directory = new File(s);
        List<MyFile> myFiles = new ArrayList<>();

        Date date1= null;
        Date date2 = null;
        try {
            date1 = new SimpleDateFormat("dd/MMM/yyyy").parse(s1);
            System.out.println(date1);
            date2 = new SimpleDateFormat("dd/MMM/yyyy").parse(s2);
            System.out.println(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        File[] files = directory.listFiles();
        for(File file : files){
            MyFile myFile = fileToMyFile(file);
            //System.out.println(myFile.getDateCreated());
            //Fri Nov 04 19:16:27 CET 2022
            //Mon Nov 07 16:04:40 CET 2022


            if(myFile.getDateCreated().after(date1) && myFile.getDateCreated().before(date2)) {
                myFiles.add(myFile);
           }
        }
        for (MyFile myFile : myFiles){
            System.out.println(myFile.getName());
        }

        return myFiles;
    }


    //Pomocne metodice
    File createFolder(String parentPath,String name){
        File f1 = new File(parentPath+"\\"+name);
        //Creating a folder using mkdir() method
        boolean bool = f1.mkdir();
        if(bool){
            System.out.println("Folder is created successfully");
            return f1;
        }else{
            System.out.println("Error Found!");
            return null;
        }
    }
    MyFile fileToMyFile(File file){
        MyFile myFile=new MyFile(new MyFile.MyFileBuild().withName(file.getName())
                .withPath(file.getPath())
                .withSize((int)file.getTotalSpace())
                .withExtensions(String.valueOf(getExtensionByStringHandling(file.getName())))
                .withDateCreated(new Date(file.lastModified())));
        return myFile;
    }
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    List<MyFile> searchDirectoryRekurzive(String s, boolean b) {
        File directory = new File(s);

        File[] files = directory.listFiles();
        if(files.length == 0){
            System.out.println("Prazan folder :" + s);
        }
        List<MyFile> myFiles = new ArrayList<>();

        for (File f: files) {
            System.out.println(f.getName());
            MyFile myFile = fileToMyFile(f);
            myFiles.add(myFile);
            if(f.isDirectory()){
                searchDirectoryRekurzive(f.getPath(),false);
            }
        }

        return myFiles;
    }
}
