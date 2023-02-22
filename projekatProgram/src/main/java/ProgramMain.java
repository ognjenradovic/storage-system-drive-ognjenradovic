import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Filter;

public class ProgramMain  {

    public static void main(String[] args){
        boolean local=true;
        LocalStorage localStorage=null;
        DriveStorage driveStorage=null;

        //Example commands

        //Extension
        //searchdir -p 1HCNNmQ9_TR-ytRdlmN2JABkKfVYOEiXN -ext xlsx

        //Name contains
        //searchdir -p 1HCNNmQ9_TR-ytRdlmN2JABkKfVYOEiXN -nc config

        //Name
        //searchdir -p 1HCNNmQ9_TR-ytRdlmN2JABkKfVYOEiXN -n configuration.txt

        //All
        //searchdir -p 1HCNNmQ9_TR-ytRdlmN2JABkKfVYOEiXN -all

        //Date
        //searchdir -p 1HCNNmQ9_TR-ytRdlmN2JABkKfVYOEiXN -d 2021-10-01T20:44:39 2022-11-25T20:44:39

        //Name
        //searchdir -n configuration.txt -pf

        //Upload
        //upload -p C:\Untitled.png


        //Download
        //download -s 1Y6TaufUmKY7LwFAZ4R1I67i5DgEsY-K_ -d aaaaaa



        System.out.println("Dobrodosli");
        System.out.println("Unesite tip skladišta: drive/local");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        while(true){
            if(input.equals("drive")){
                driveStorage=new DriveStorage();
                local=false;
                break;
            }else if(input.equals("local")){
                localStorage=new LocalStorage();
                break;
            }
            else{
                System.out.println("Pogresan unos");
            }
        }
        while (true) {
            System.out.println("Unesi komandu:");
            input = in.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (input.equals("")) {
                System.out.println("Nista nije uneseno");
            }
            String[] split = input.split(" ");

            switch (split[0]) {
                case "createstorage":
                    createstr(split, driveStorage, localStorage, local);
                    break;
                case "loadstorage":
                    loadstr(split, driveStorage, localStorage, local);
                    break;
                case "rename":
                    renamestr(split, driveStorage, localStorage, local);
                    break;
                case "move":
                    movestr(split, driveStorage, localStorage, local);
                    break;
                case "remove":
                    removestr(split, driveStorage, localStorage, local);
                    break;
                case "download":
                    downloadstr(split, driveStorage, localStorage, local);
                    break;
                case "searchdir":
                    searchstr(split, driveStorage, localStorage, local);
                    break;
                case "upload":
                    upload(split, driveStorage, localStorage, local);
                    break;
                default:
                    System.out.println("Nepoznata komanda");
                    break;
            }
        }




    }
    static void upload(String[] split,DriveStorage driveStorage,LocalStorage localStorage,boolean local){
        String path = "";
        int i=0;
        //load -p sjfhjdfh
        for(String str : split) {
            if (str.equals("-p")) {
                path = split[i+1];
            }
            i++;
        }
        List<MyFile> files=new ArrayList<>();
        files.add(new MyFile(new MyFile.MyFileBuild().withName(path).withPath(path)));
        if(local){
            localStorage.uploadFiles(files);
        }
        else{
            driveStorage.uploadFiles(files);
        }
    }
    static void listMyFile(List<MyFile> myFiles){
        for(MyFile myFile:myFiles){
            System.out.println(myFile.toString());
        }
    }

    static void listMyFileFilter(List<MyFile> myFiles,Filter filter){
//        System.out.println(filter.filtriraj(myFiles));
    }
    static void searchstr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean local) {
        String path = null;
        String extension = null;
        String nameContains = null;
        String name = null;
        String date1 = null;
        String date2 = null;
        boolean searchAll = false;
        boolean searchFolders = false;

        // Parse command line arguments
        for (int i = 1; i < split.length; i++) {
            switch (split[i]) {
                case "-p":
                    path = split[++i];
                    break;
                case "-ext":
                    extension = split[++i];
                    break;
                case "-nc":
                    nameContains = split[++i];
                    break;
                case "-n":
                    name = split[++i];
                    break;
                case "-all":
                    searchAll = true;
                    break;
                case "-d":
                    date1 = split[++i];
                    date2 = split[++i];
                    break;
                case "-pf":
                    searchFolders = true;
                    break;
                default:
                    System.out.printf("Unknown option: %s%n", split[i]);
                    return;
            }
        }

        if (path == null) {
            System.out.println("Missing required option: -p");
            return;
        }

        if (searchFolders && name != null) {
            if (local) {
                System.out.println(localStorage.searchDirectoryReturnFolder(name).getName());
            } else {
                System.out.println(driveStorage.searchDirectoryReturnFolder(name).getPath());
            }
            return;
        }

        List<MyFile> result = new ArrayList<>();

        if (extension != null) {
            result.addAll(local ? localStorage.searchDirectoryExtension(path, extension)
                    : driveStorage.searchDirectoryExtension(extension, path));
        }

        if (nameContains != null) {
            result.addAll(local ? localStorage.searchDirectoryNameContains(path, nameContains)
                    : driveStorage.searchDirectoryNameContains(nameContains, path));
        }

        if (name != null) {
            result.addAll(local ? localStorage.searchDirectoryName(path, name)
                    : driveStorage.searchDirectoryName(name, path));
        }

        if (searchAll) {
            result.addAll(local ? localStorage.searchDirectoryAll(path, false)
                    : driveStorage.searchDirectoryAll(path, false));
        }

        if (date1 != null && date2 != null) {
            result.addAll(local ? localStorage.searchDirectoryPeriod(path, date1, date2)
                    : driveStorage.searchDirectoryPeriod(path, date1, date2));
        }

        listMyFile(result);
    }











    static void createstr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean local) {
        String name = "";
        String config = "";
        int i = 0;
        for (String str : split) {
            if (str.equals("-n")) {
                name = split[i + 1];
            } else if (str.contains("-c")) {
                config = split[i];
            }
            i++;
        }
        if (name.isEmpty()) {
            System.err.println("Storage name is missing.");
            return;
        }
        Configuration configuration = null;
        if (!config.isEmpty()) {
            configuration = parseConfigString(config);
            if (configuration == null) {
                System.err.println("Invalid configuration input.");
                return;
            }
        }
        if (local) {
            if (configuration == null) {
                configuration = new Configuration(name);
            }
            localStorage.createStorage(name, configuration);
            System.out.println(localStorage.getConfiguration());
        } else {
            if (configuration == null) {
                configuration = new Configuration(name);
            }
            driveStorage.createStorage(name, configuration);
            System.out.println(driveStorage.getConfiguration());
        }
    }

//    static Filter parseFilterString(String input){
////        //searchdir -n direktorijum -p putanja -f(name,path)
////        try{
////            Boolean name;
////            Boolean dateCreated;
////            Boolean path;
////
////            char[] inputchars=  input.toCharArray();
////            if((inputchars[0] != '-') || (inputchars[1] != 'f') || (inputchars[2] != '(') || (inputchars[inputchars.length - 1] != ')')){
////
////                return null;
////            }
////            input=input.substring(3,input.length()-1);
////            if(input.contains("name"))name=true;
////            if(input.contains("dateCreated"))dateCreated=true;
////            if(input.contains("path"))path=true;
////
////            return new Filter(name,path,dateCreated);
////        }
////        catch (Exception e){
////            return null;
////        }
//    }


    static Configuration parseConfigString(String input) {
        try {
            if (!input.startsWith("-c(") || !input.endsWith(")")) {
                return null;
            }
            input = input.substring(3, input.length() - 1);
            String[] split = input.split(",");
            String extensionsString = split[split.length - 1];
            if (!extensionsString.startsWith("(") || !extensionsString.endsWith(")")) {
                return null;
            }
            extensionsString = extensionsString.substring(1, extensionsString.length() - 1);
            List<String> extensions = Arrays.asList(extensionsString.split(";"));
            return new Configuration(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], extensions);
        } catch (Exception e) {
            return null;
        }
    }

    static void loadstr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean isLocal) {
        String path = "";
        for (int i = 0; i < split.length - 1; i++) {
            if (split[i].equals("-p")) {
                path = split[i + 1];
                System.out.println(path);
                break;
            }
        }
        if (isLocal) {
            localStorage.loadStorage(path);
        } else {
            driveStorage.loadStorage(path);
        }
    }

    static void renamestr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean isLocal) {
        String path = "";
        String newName = "";
        for (int i = 0; i < split.length - 2; i++) {
            if (split[i].equals("-p")) {
                path = split[i + 1];
                newName = split[i + 2];
                System.out.println(path);
                System.out.println(newName);
                break;
            }
        }
        if (isLocal) {
            localStorage.rename(path, "", newName);
        } else {
            driveStorage.rename(path, "", newName);
        }
    }

    static void movestr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean isLocal) {
        String source = "";
        String destination = "";
        for (int i = 0; i < split.length - 1; i++) {
            if (split[i].equals("-s")) {
                source = split[i + 1];
            } else if (split[i].equals("-d")) {
                destination = split[i + 1];
            }
        }
        if (isLocal) {
            localStorage.moveFiles(source, destination);
            System.out.println("File successfully moved.");
        } else {
            driveStorage.moveFiles(source, destination);
            System.out.println("File successfully moved.");
        }
    }

    static void removestr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean isLocal) {
        String path = "";
        for (int i = 0; i < split.length - 1; i++) {
            if (split[i].equals("-p")) {
                path = split[i + 1];
                System.out.println(path);
                break;
            }
        }
        if (isLocal) {
            localStorage.removeFiles(path);
        } else {
            driveStorage.removeFiles(path);
        }
    }
    static void downloadstr(String[] split, DriveStorage driveStorage, LocalStorage localStorage, boolean isLocal) {
        String source = "";
        String destination = "";
        for (int i = 0; i < split.length - 1; i++) {
            if (split[i].equals("-s")) {
                source = split[i + 1];
            } else if (split[i].equals("-d")) {
                destination = split[i + 1];
            }
        }
        if (isLocal) {
            localStorage.download(source, destination);
            System.out.println("File successfully downloaded.");
        } else {
            driveStorage.download(source, destination);
            System.out.println("File successfully downloaded.");
        }
    }


}
