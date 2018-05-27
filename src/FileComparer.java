import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class FileComparer
{
    public static String[][] CompareFiles(String fileName, int fileCount)
    {
        Scanner scanner1 = null;
        Scanner scanner2 = null;
        try
        {
            scanner1 = new Scanner(new File(fileName));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        String[][] scores = new String[fileCount - 1][fileCount - 1];

        // Find Common occurrences among all files//
        String commonStructure = scanner1.nextLine();
        while (scanner1.hasNextLine())
        {
            String next = scanner1.nextLine();
            commonStructure = lcs(commonStructure, next, commonStructure.length(), next.length());
        }
        // End Common Occurrence calculation //

        File file = RemoveCommonElements(commonStructure, new File(fileName));
        //File file = new File(fileName);
        try
        {
            scanner1 = new Scanner(file);
            scanner2 = new Scanner(file);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        System.out.println("Running File Comparison...");
        for (int i = 0; i < scores.length; i++)
        {
            String str1 = scanner1.nextLine();
            for (int k = 0; k <= i; k++)
            {
                scanner2.nextLine();
            }
            for (int j = i + 1; j <= scores.length; j++)
            {
                String str2 = scanner2.nextLine();
                int distance = CalculateEditDistance(str1, str2, str1.length(), str2.length());
                int bigger = Math.max(str1.length(), str2.length());
                double percent = (bigger - distance) / (double) bigger * 100;
                scores[i][j - 1] = String.format("%.2f", percent);
                if(scores[i][j - 1].equals("33.33"))
                {
                    int p = 3;
                    p++;
                }
            }
            try
            {
                scanner2.close();
                scanner2 = null;
                scanner2 = new Scanner(file);
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                System.out.println("Didn't work");
            }
        }
        System.out.println("Done");
        return scores;
    }

    static int CalculateEditDistance(String str1, String str2, int m, int n)
    {
        // Create a table to store results of subproblems
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

        // Print the lcs
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
            // TODO Auto-generated catch block
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
                int mid = builder.toString().length();
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
}