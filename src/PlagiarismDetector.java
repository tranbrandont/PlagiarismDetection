//Main file to run the Plagiarism Detection System

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlagiarismDetector
{
	// Will read provided directory for c++ files for plagiarism detection
	public static void main(String[] args) throws IOException, InterruptedException
	{
		int running = 0;
		ArrayList<String> fileNames = null;
		running = ChooseDirectoryGui.main(args);

		// Waits for user to pick directory
		while (running == 0)
		{
			Thread.sleep(1000);
		}
		String myDirectory = ChooseDirectoryGui.getPath();
		File dir = new File(myDirectory);
		File[] directoryListing = dir.listFiles();
		ConvertFile.InitializeCPPLists("cpp_keywords.txt");
		ConvertFile.InitializeCommonKeywordsMap();

		System.out.println(myDirectory);
		// converts each file in directory to generalized file.
		String str = Long.toHexString(Double.doubleToLongBits(Math.random()));
		int validFileCount = 1;
		fileNames = new ArrayList<String>();
		for (File assignment : directoryListing)
		{
			String name = assignment.getName();
			name = name.substring(name.length() - 3, name.length());
			if (name.equals("cpp"))
			{
				fileNames.add(assignment.getName());
				System.out.print(validFileCount + ": ");
				System.out.println(assignment);
				ConvertFile.textConverter(assignment, str);
				// ConvertFile.textConverter(assignment, str);
				validFileCount++;
			}
		}
		String[][] scores = FileComparer.CompareFiles(str, validFileCount - 1);
		for (int i = 0; i < scores.length; i++)
		{
			for (int j = i; j < scores.length; j++)
			{
				//Only prints those above given threshold
				if (StrToDouble(scores[i][j]) >= 0 && StrToDouble(scores[i][j]) < 11)
				{
					System.out.println(fileNames.get(i).substring(0, 10) + "~ is " + scores[i][j]
							+ "% similar to assignment " + fileNames.get(j+1).substring(0, 10) + "~");
				}
			}
		}
		System.out.println("Done!");
	}

	private static double StrToDouble(String str)
	{
		return Double.parseDouble(str);
	}

}