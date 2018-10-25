import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import javafx.scene.control.Button;

public class FileComparer
{
	
	public static String[][] CompareFiles(List<FileStats> files, Button btn)
	{
		
		String[][] scores = new String[files.size() - 1][files.size() - 1];

		// Find Common occurrences among all files //
		String commonStructure  = files.get(0).GetAllLinesAsString();
		String next;
		for (int i = 0; i < files.size()-1; i++)
		{
			commonStructure = lcs(commonStructure, next = files.get(i+1).GetAllLinesAsString(), commonStructure.length(), next.length());
		}
		// End Common Occurrence calculation //
		System.out.println("Running File Comparison...");
		//double totalCalculations = (files.size()*(files.size()-1))/2;
		double completedCalculations = 0;
		for (int i = 0; i < files.size()-1; i++)
		{
			for(int j = i+1; j < files.size(); j++)
			{
				String str1 = files.get(i).GetAllLinesAsString();
				String str2 = files.get(j).GetAllLinesAsString();
				int distance = CalculateEditDistance(str1, str2);
				int bigger = Math.max(str1.length(), str2.length());
				double percent = (bigger - distance) / (double) bigger * 100;
				scores[i][j - 1] = String.format("%.2f", percent);
			}
			btn.setText(Double.toString(completedCalculations));
		}
		System.out.println();
		return scores;
	}

	static int CalculateEditDistance(String str1, String str2)
	{
		// Create a table to store results of subproblems
		int m = str1.length();
		int n = str2.length();
		int dp[][] = new int[m + 1][n + 1];

		// Fill d[][] in bottom up manner
		for (int i = 0; i <= m; i++)
		{
			for (int j = 0; j <= n; j++)
			{
				// If first string is empty, only option is to
				// isnert all characters of second string
				if (i == 0)
					dp[i][j] = j; // Min. operations = j

				// If second string is empty, only option is to
				// remove all characters of second string
				else if (j == 0)
					dp[i][j] = i; // Min. operations = i

				// If last characters are same, ignore last char
				// and recur for remaining string
				else if (str1.charAt(i - 1) == str2.charAt(j - 1))
					dp[i][j] = dp[i - 1][j - 1];

				// If last character are different, consider all
				// possibilities and find minimum
				else
					dp[i][j] = 1 + min(dp[i][j - 1], // Insert
							dp[i - 1][j], // Remove
							dp[i - 1][j - 1]); // Replace
			}
		}

		return dp[m][n];
	}

	public static int costOfSubstitution(char a, char b)
	{
		return a == b ? 0 : 1;
	}

	public static int min(int... numbers)
	{
		return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
	}

	private static String lcs(String X, String Y, int m, int n)
	{
		int[][] L = new int[m + 1][n + 1];

		// Following steps build L[m+1][n+1] in bottom up fashion. Note
		// that L[i][j] contains length of LCS of X[0..i-1] and Y[0..j-1]
		for (int i = 0; i <= m; i++)
		{
			for (int j = 0; j <= n; j++)
			{
				if (i == 0 || j == 0)
					L[i][j] = 0;
				else if (X.charAt(i - 1) == Y.charAt(j - 1))
					L[i][j] = L[i - 1][j - 1] + 1;
				else
					L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
			}
		}

		// Following code is used to print LCS
		int index = L[m][n];
		int temp = index;

		// Create a character array to store the lcs string
		char[] lcs = new char[index + 1];
		lcs[index] = '\0'; // Set the terminating character

		// Start from the right-most-bottom-most corner and
		// one by one store characters in lcs[]
		int i = m, j = n;
		while (i > 0 && j > 0)
		{
			// If current character in X[] and Y are same, then
			// current character is part of LCS
			if (X.charAt(i - 1) == Y.charAt(j - 1))
			{
				// Put current character in result
				lcs[index - 1] = X.charAt(i - 1);

				// reduce values of i, j and index
				i--;
				j--;
				index--;
			}

			// If not same, then find the larger of two and
			// go in the direction of larger value
			else if (L[i - 1][j] > L[i][j - 1])
				i--;
			else
				j--;
		}

		StringBuilder str = new StringBuilder();
		for (int k = 0; k <= temp; k++)
			str.append(lcs[k]);
		return str.toString();
	}

	public static File RemoveCommonElements(String commonElements, File file)
	{
		String str = Long.toHexString(Double.doubleToLongBits(Math.random())) + "R";
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(file);
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		while (scanner.hasNextLine())
		{
			String curr = scanner.nextLine();
			int LCSindex = 0;
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < curr.length(); i++)
			{
				if(LCSindex == commonElements.length())
				{
					break;
				}
				if(curr.charAt(i) == commonElements.charAt(LCSindex))
				{
					LCSindex++;
				}
				else
				{
					builder.append(curr.charAt(i));
				}
			}
			
			try (FileWriter fw = new FileWriter(str, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				out.println(builder.toString());
				out.flush();
				out.close();
				bw.close();
				fw.close();
			}
			catch (IOException e)
			{
				// exception handling left as an exercise for the reader
				System.out.println("///// DIDN'T WORK /////");
			}
		}
		scanner.close();
		return new File(str);
	}
	
	public static void FindOutliers(List<FileStats> files)
	{
		int [] lengths = new int[files.size()];
		for (int i = 0; i < files.size(); i++)
		{
			lengths[i] = files.get(i).getStringLength();
		}
		double sum = 0.0, standardDeviation = 0.0;
        int length = lengths.length;

        for(double num : lengths) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: lengths) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        standardDeviation = Math.sqrt(standardDeviation/length)*2;
        
        int outlierCount = 0;
        for(FileStats file : files)
        {
        	int strLen = file.getStringLength(); 
        	if (strLen > mean + standardDeviation || strLen < mean - standardDeviation)
        	{
        		file.MakeOutlier();
        		outlierCount++;
        	}
        }
        System.out.println("Done checking outliers");
	}
}