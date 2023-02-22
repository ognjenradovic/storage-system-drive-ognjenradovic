import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration implements Serializable {
    private String name = "NewDirectory";
    private int maxSize = 1024;
    private int maxFiles = 1000;
    private String absolutePath ="./";//ili relativnu
    private List<String> illegalExtentions = Arrays.asList(new String[]{" "});
    private Map<String , Integer> maxSizeSubdirectories=new HashMap<>();
    //prolazi kroz roditelje i za svakog roditelja proverava u ovoj mapi da li ima ogranicenje
    private Map<String , Integer> maxFileSubdirectories=new HashMap<>();;

    //Moze da bude singlton
    //Modzda da dodamo builder pattern

    public Configuration(String name, int maxSize, int maxFiles,String absolutePath, List<String> illegalExtentions) {
        this.name = name;
        this.maxSize = maxSize;
        this.maxFiles = maxFiles;
        this.absolutePath = absolutePath;
        this.illegalExtentions = illegalExtentions;
    }

    public Configuration(String name, int maxSize, int maxFiles, String absolutePath, List<String> illegalExtentions, Map<String, Integer> maxSizeSubdirectories, Map<String, Integer> maxFileSubdirectories) {
        this.name = name;
        this.maxSize = maxSize;
        this.maxFiles = maxFiles;
        this.absolutePath = absolutePath;
        this.illegalExtentions = illegalExtentions;
        this.maxSizeSubdirectories = maxSizeSubdirectories;
        this.maxFileSubdirectories = maxFileSubdirectories;
    }

    public Configuration(String name) {
    this.name=name;
    }

    public Map<String, Integer> getMaxSizeSubdirectories() {
        return maxSizeSubdirectories;
    }

    public Map<String, Integer> getMaxFileSubdirectories() {
        return maxFileSubdirectories;
    }

    public void addMaxSizeSubRestriction(String path,int num) {
        this.maxSizeSubdirectories.put(path,num);
    }
    public void addMaxFileSubdirectories(String path,int num) {
        this.maxFileSubdirectories.put(path,num);
    }

    public void setMaxSizeSubdirectories(Map<String, Integer> maxSizeSubdirectories) {
        this.maxSizeSubdirectories = maxSizeSubdirectories;
    }

    public void setMaxFileSubdirectories(Map<String, Integer> maxFileSubdirectories) {
        this.maxFileSubdirectories = maxFileSubdirectories;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "name='" + name + '\'' +
                ", maxSize=" + maxSize +
                ", maxFiles=" + maxFiles +
                ", absolutePath='" + absolutePath + '\'' +
                ", illegalExtentions=" + illegalExtentions +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    public List<String> getIllegalExtentions() {
        return illegalExtentions;
    }

    public void setIllegalExtentions(List<String> illegalExtentions) {
        this.illegalExtentions = illegalExtentions;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}
