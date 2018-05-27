//Main file to run the Plagiarism Detection System


import java.io.File;
import java.io.IOException;

public class PlagiarismDetector {
    //Will read provided directory for c++ files for plagiarism detection
    public static void main (String[] args) throws IOException, InterruptedException {
        ChooseDirectoryView.launch(ChooseDirectoryView.class, args);
        String assignmentDirectory = "";

        //Waits for user to pick directory
        while (assignmentDirectory == "") {
            assignmentDirectory = ChooseDirectoryView.getAssignmentDirectory();
            Thread.sleep(1000);
        }

        File dir = new File(assignmentDirectory);
        File[] directoryListing = dir.listFiles();
        ConvertFile.InitializeCPPLists("cpp_keywords.txt");
    	ConvertFile.InitializeCommonKeywordsMap();

        System.out.println(assignmentDirectory);
        //converts each file in directory to generalized file.
        String str = Long.toHexString(Double.doubleToLongBits(Math.random()));
        for (File assignment : directoryListing) {
            System.out.println(assignment);
            ConvertFile.textConverter(assignment, str);
        }
        FileComparer.CompareFiles(str, directoryListing.length);
    }



}