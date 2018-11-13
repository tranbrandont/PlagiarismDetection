import java.io.File;
import java.util.ArrayList;

public class StudentFiles {

    File studentFile;
    ArrayList<File> similarFiles = new ArrayList<>();

    public StudentFiles(File studentFile) {
        this.studentFile = studentFile;
    }

    public void addFile(File similarFile) {
        similarFiles.add(similarFile);
    }
}
