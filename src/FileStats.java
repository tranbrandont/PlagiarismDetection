import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileStats
{
	/* Fields */ 
	//private String fileName;
	private ArrayList <String> tokenizedLines;
	private Map <Character, Integer> tokenCount;
	  
	/* Constructor */
	FileStats(String name) 
	{
		//this.fileName = name;
		tokenizedLines = new ArrayList<String>();
		tokenCount = new HashMap<Character, Integer>();
	}
	
	/* Functions */
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
	
	private void WriteLine (String str)
	{
		tokenizedLines.add(str);
	}
	
	private void HashLine (String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			PutToken(str.charAt(i));
		}
	}
	
	public void HandleLine (String str)
	{
		WriteLine(str);
		HashLine(str);
	}
	
	public Map<Character, Integer> GetCharCount ()
	{
		return tokenCount;
	}
	
	public ArrayList<String> GetLines ()
	{
		return tokenizedLines;
	}
	
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
}
