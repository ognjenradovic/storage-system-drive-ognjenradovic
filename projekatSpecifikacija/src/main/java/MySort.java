import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MySort {
    //moze biti builder pattern
    private SortType sortType;
    private boolean asc;

    public MySort() {

    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public MySort(SortType sortType, boolean asc) {
        this.sortType = sortType;
        this.asc = asc;
    }

    //metoda filtriraj()?
    // gde cemo gledati npr ukoliko je name true da nam vrati trenutniFajl.getName()


    public List<MyFile> sortiraj(List <MyFile> myFiles) {
        if(this.sortType.equals(SortType.NAME)){
            myFiles.sort(MyFile.myFileNameComparator);
        }
        else if(this.sortType.equals(SortType.DATE_CREATED)){
            myFiles.sort(MyFile.myFileDateComparator);
        }

        if(!this.asc){
            Collections.reverse(myFiles);
        }
        return myFiles;
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


}
