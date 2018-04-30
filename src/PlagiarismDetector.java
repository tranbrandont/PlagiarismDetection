//Main file to run the Plagiarism Detection System

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlagiarismDetector {
    //Will read provided directory for c++ files for plagiarism detection
    public static void main (String[] args) throws IOException, InterruptedException {
        int running = 0;

        running = ChooseDirectoryGui.main(args);

        //Waits for user to pick directory
        while (running == 0) {
            Thread.sleep(1000);
        }
        String myDirectory = ChooseDirectoryGui.getPath();
        File dir = new File(myDirectory);
        File[] directoryListing = dir.listFiles();

        System.out.println(myDirectory);
        //converts each file in directory to generalized file.
        File file = new File("testFile1.txt");
        file.createNewFile();
        for (File assignment : directoryListing) {
            System.out.println(assignment);
            ConvertFile.textConverter(assignment, file);
        }
        file.delete();
    }

}