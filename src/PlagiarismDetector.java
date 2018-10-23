//Main file to run the Plagiarism Detection System


import java.io.IOException;

public class PlagiarismDetector {
	// Will read provided directory for c++ files for plagiarism detection
	public static void main(String[] args) throws IOException, InterruptedException
	{
	    HomePageView.launch(HomePageView.class, args);
	    
//        //ChooseDirectoryView.launch(ChooseDirectoryView.class, args);
//        String assignmentDirectory = "";
//
//        //Waits for user to pick directory
//        while (assignmentDirectory == "") {
//			assignmentDirectory = ChooseDirectoryView.getAssignmentDirectory();
//			Thread.sleep(1000);
//        }
//		File dir = new File(assignmentDirectory);
//		File[] directoryListing = dir.listFiles();
//		ConvertFile.InitializeCPPLists("cpp_keywords.txt");
//		ConvertFile.InitializeCommonKeywordsMap();
//
//		System.out.println(assignmentDirectory);
//		// converts each file in directory to generalized file.
//		String str = Long.toHexString(Double.doubleToLongBits(Math.random()));
//		int validFileCount = 1;
//		ArrayList<String> fileNames = new ArrayList<String>();
//		for (File assignment : directoryListing)
//		{
//			String name = assignment.getName();
//			name = name.substring(name.length() - 3, name.length());
//			if (name.equals("cpp"))
//			{
//				fileNames.add(assignment.getName());
//				System.out.print(validFileCount + ": ");
//				System.out.println(assignment);
//				ConvertFile.textConverter(assignment, str);
//				// ConvertFile.textConverter(assignment, str);
//				validFileCount++;
//			}
//		}
//		String[][] scores = FileComparer.CompareFiles(str, validFileCount - 1);
//		for (int i = 0; i < scores.length; i++)
//		{
//			for (int j = i; j < scores.length; j++)
//			{
//				//Only prints those above given threshold
//				if (StrToDouble(scores[i][j]) >= 90)
//				{
//					System.out.println(fileNames.get(i).substring(0, 10) + "~ is " + scores[i][j]
//							+ "% similar to assignment " + fileNames.get(j+1).substring(0, 10) + "~");
//				}
//			}
//		}
//		System.out.println("Done!");
	}
//
//	private static double StrToDouble(String str)
//	{
//		return Double.parseDouble(str);
//	}



}