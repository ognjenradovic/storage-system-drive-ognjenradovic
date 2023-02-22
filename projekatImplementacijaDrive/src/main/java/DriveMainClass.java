import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class DriveMainClass {

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "My project";

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = DriveStorage.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void init(DriveStorage driveStorage){
        Drive drive= null;
        try {
            drive = getDriveService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driveStorage.setService(drive);
        //driveStorage.searchDirectoryNameContains("Rub","1HCNNmQ9_TR-ytRdlmN2JABkKfVYOEiXN");
        return;

    }


//    public static void main(String[] args) throws IOException {
//
//        //stavi u konektoru da se povezuje
//       // storage.createFolder("alooTest");
//       // storage.uploadFiles(new MyFile(new MyFile.MyFileBuild().withName("komponente.txt").withPath("C:\\Users\\Ognjen Radovic\\Desktop\\komponente.txt")));
//        //storage.download("1aAJgj2HSd661CilayT0hMAD3vgX0Hhd2","C:\\Users\\Ognjen Radovic\\Desktop\\test666666666666.png");
//       // storage.removeFiles("1pBtUelwvJEiNwOxzisTXg-j4BC6KPXhx");
//      //  storage.rename("1QJBwQiUgnIsgdT3N3OswSRVB6KY0NTtx","","novoIme.txt");
//       // List<MyFile> myFiles=storage.searchDirectoryAll("docx",true);
//      //  storage.listDriveRoot()
//       // System.out.println(storage.searchDirectoryPeriod("1MgW1nx9BtSPFbpSSx1D48KIspmUQ71d-","2022-10-01T20:44:39","2022-10-25T20:44:39"));
////        List<String> zabranjenjeEkstenzije=new ArrayList<>();
////        zabranjenjeEkstenzije.add(".docx");
////
////        //storage.createStorage("NAJNOVIJII",new Configuration("NAJNOVIJI",10000,5,"NAJNOVIJI",zabranjenjeEkstenzije));
////        storage.loadStorage("NAJNOVIJII");
////       // storage.getConfiguration().setMaxFiles(10);
////        storage.getConfiguration().setIllegalExtentions(zabranjenjeEkstenzije);
////        storage.getConfiguration().setMaxSize(99900000);
////        storage.getConfiguration().addMaxFileSubdirectories("1fBIy0vKpuz0PouxMWyvcmsbIUV7fSPar",2);
////        storage.updateConfigurationFile();
////
////
////       // storage.moveFiles("1UshkYijfk8FOA8OCe8R9Uz6etmeKYNmq","1fBIy0vKpuz0PouxMWyvcmsbIUV7fSPar");
////        storage.uploadFiles(new MyFile(new MyFile.MyFileBuild().withName("komponente.txt").withPath("C:\\Users\\Ognjen Radovic\\Desktop\\komponente.txt")));
////                //storage.uploadFiles(new MyFile(new MyFile.MyFileBuild().withName("Statistika.docx").withPath("C:\\Users\\Ognjen Radovic\\Desktop\\Statistika.docx")));
////        // System.out.println("Broj fajlova" + storage.countMemoryInDir("1hnWYNDSMCRIY6GeUiohwqNlJ_hTe6o-F"));
//
//    }
}
