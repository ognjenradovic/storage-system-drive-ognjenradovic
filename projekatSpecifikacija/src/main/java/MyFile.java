import java.util.Comparator;
import java.util.Date;

public class MyFile implements Comparable<MyFile> {
    //cuvamo metapodatke
    //Size
    //Name
    //Datum kreiranja
    //path
    //ekstenzija
    private String name;
    private Date dateCreated; // mozda da bude tip date
    private String extensions;
    private String path;
    private int size;

    //konstruktor sa svim poljima

    public MyFile(String name, Date dateCreated, String extensions, String path, int size) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.extensions = extensions;
        this.path = path;
        this.size = size;
    }

    //konstruktor za bilder
    public MyFile(MyFileBuild myFileBuild){
        this.name = myFileBuild.name;
        this.dateCreated = myFileBuild.dateCreated;
        this.extensions = myFileBuild.extensions;
        this.path = myFileBuild.path;
        this.size = myFileBuild.size;
    }

    @Override
    public int compareTo(MyFile o) {
            String compareName = ((MyFile) o).getName();

            //ascending order
            return o.getName().compareTo(compareName);
    }

    public static Comparator<MyFile> myFileNameComparator
            = new Comparator<MyFile>() {

        public int compare(MyFile myFile1, MyFile myFile2) {

            String myFile1Name = myFile1.getName().toUpperCase();
            String myFile2Name = myFile2.getName().toUpperCase();


            //ascending order
            return myFile1Name.compareTo(myFile2Name);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };

    public static Comparator<MyFile> myFileDateComparator
            = new Comparator<MyFile>() {

        public int compare(MyFile myFile1, MyFile myFile2) {

            Date myFile1Date = myFile1.getDateCreated();
            Date myFile2Date = myFile2.getDateCreated();

            //ascending order
            return myFile1Date.compareTo(myFile2Date);
        }

    };

    //builder
    public static class MyFileBuild{
        private String name;
        private Date dateCreated; // mozda da bude tip date
        private String extensions;
        private String path;
        private int size;

        public MyFileBuild withName(String name){
            this.name = name;
            return this;
        }

        public MyFileBuild withDateCreated(Date dateCreated){
            this.dateCreated = dateCreated;
            return this;
        }

        public MyFileBuild withExtensions(String extensions){
            this.extensions = extensions;
            return this;
        }

        public MyFileBuild withPath(String path){
            this.path = path;
            return this;
        }

        public MyFileBuild withSize(int size){
            this.size = size;
            return this;
        }

        public MyFile build(){
            return new MyFile(this);
        }

        //kako bi koristii ovo
        //npr. MyFile myFile = new MyFile.MyFileBuilder.withName("Ime").build();


    }

    //neka toString metoda koja se moze filtrirati

    //dodato primera radi
    @Override
    public String toString() {
        return "File :" + name + dateCreated + extensions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
