import java.util.List;

public class Filter {
    //moze biti builder pattern
    private boolean name;
    private boolean dateCreated;
    private boolean path;
    private List<MyFile> trenutniFajlovi;

    public Filter() {
    }

    public Filter(boolean name, boolean dateCreated, boolean path, List<MyFile> trenutniFajl) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.path = path;
        this.trenutniFajlovi = trenutniFajl;
    }

    public Filter(boolean name, boolean dateCreated, boolean path) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.path = path;
    }

    //metoda filtriraj()?
    // gde cemo gledati npr ukoliko je name true da nam vrati trenutniFajl.getName()


    public String filtriraj(List <MyFile> myFiles) {
        String str = "";
        for (MyFile myFile : myFiles) {
            String currentFileString = "";
            if (this.name) {
                currentFileString += myFile.getName();
                currentFileString += " ";

            }
            if(this.dateCreated){
                currentFileString += myFile.getDateCreated();
                currentFileString += " ";
            }
            if (this.path) {
                currentFileString += myFile.getName();
                currentFileString += " ";
            }
            str += currentFileString + "\n";
        }
        return str;
    }

        /*
        File directory = new File(path);
        trenutniFajlovi =  directory.list();
        if(this.name == true && this.dataCreated == false && this.path == false){
            return sout(trenutniFajl.getName());
        }else if(this.name == true && this.dataCreated == true && this.path == false){
            return sout(trenutniFajl.getName() + trenutni.fajl().getDataCreated);
        }else if(this.name == true && this.dataCreated == true && this.path == true){
            return sout(trenutniFajl.getName() + trenutni.fajl().getDataCreated() + trenutni.fajl().getPath());
        }else if(this.name == true && this.dataCreated == false && this.path == true){
            return sout(trenutniFajl.getName() + trenutni.fajl().getPath());
        }

     */


    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(boolean dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isPath() {
        return path;
    }

    public void setPath(boolean path) {
        this.path = path;
    }


}
