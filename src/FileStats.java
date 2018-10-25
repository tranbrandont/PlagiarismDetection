import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileStats
{
	/* Fields */ 
	private String fileName;
	private ArrayList <String> tokenizedLines;
	private Map <Character, Integer> tokenCount;
	static public double[][] scores;
	  
	/* Constructor */
	FileStats(String name) 
	{
		//this.fileName = name;
		tokenizedLines = new ArrayList<String>();
		tokenCount = new HashMap<Character, Integer>();
	}
	
	/* Functions */
	
	// Adds a single token to the HashMap
	private void PutToken(char c)
	{
		if (tokenCount.containsKey(c))
		{
			tokenCount.put(c, tokenCount.get(c)+1);
		}
		else
		{
			tokenCount.put(c, 1);
		}
	}
	
	// Stores a tokenized line in the file's line list
	private void WriteLine (String str)
	{
		tokenizedLines.add(str);
	}
	
	// Function that calls adds all of a line's tokenized
	// chars into the HashMap
	private void HashLine (String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			PutToken(str.charAt(i));
		}
	}
	
	// Master function for converting files:
	// Writes the line to the Lines List and Hashes the tokens
	public void HandleLine (String str)
	{
		WriteLine(str);
		HashLine(str);
	}
	
	// returns the tokenCount for a given file
	public Map<Character, Integer> GetCharCount ()
	{
		return tokenCount;
	}
	
	// Returns filename for given FileStat object
	public String GetFileName ()
	{
		return this.fileName;
	}
	
	// Returns the list of tokenized lines
	public ArrayList<String> GetLines ()
	{
		return tokenizedLines;
	}
	
	// Returns all lines of a file as a single string of tokenized characters
	public String GetAllLinesAsString ()
	{
		StringBuilder builder = new StringBuilder();
		String currentline;
		for (int i = 0; i < tokenizedLines.size(); i++)
		{
			currentline = tokenizedLines.get(i);
			builder.append(currentline);
		}
		return builder.toString();
	}
	
	// Initializes the score array after files have been scanned;
	public static void SetScoresSize(int size)
	{
		scores = new double[size][size];
	}
}
