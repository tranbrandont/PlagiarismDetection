import java.io.File;
import java.util.ArrayList;

public class StudentFiles {

    File studentFile;
    ArrayList<FileSimilarity> similarFiles = new ArrayList<>();

    public StudentFiles(File studentFile) {
        this.studentFile = studentFile;
    }

    public void addFile(FileSimilarity similarFile) {
        similarFiles.add(similarFile);
    }
}
