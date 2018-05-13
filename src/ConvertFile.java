//Converts files to a generalized version of the file

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConvertFile {
    //Takes a file to be read through and converted into a generalized version of the file.
    //Will read line by line through file and call whichever function is suitable for that situation
    //Writes generalized version to a new file
	private static ArrayList<String> keywords = new ArrayList<String>();
	private static ArrayList<String> types = new ArrayList<String>();
	private static HashMap<String, Character> commonKeywords;
	
    public static void textConverter (File assignment, String fileName) {
    	// Helps keep track of recurring variables in file.
    	
    	//ArrayList<String> commmonVars = new ArrayList<String>();
    	Scanner scanner;
    	StringBuilder builder = new StringBuilder();
    	try
		{
			scanner = new Scanner(assignment);
		} 
    	catch (FileNotFoundException e)
		{
    		System.out.println(assignment.getName() + "does not exist. Skipping...");
			return;
		}
    	String currentLine;
    	while(scanner.hasNextLine())
    	{
    		currentLine = scanner.nextLine();
    		ProcessLine(currentLine, builder);
    	}
    	try(FileWriter fw = new FileWriter(fileName, true);
    		    BufferedWriter bw = new BufferedWriter(fw);
    		    PrintWriter out = new PrintWriter(bw))
    		{
    		    out.println(builder.toString());
    		    System.out.println("Printed!");
    		} catch (IOException e) {
    		    //exception handling left as an exercise for the reader
    		}

    	scanner.close();
    }
    
	private static void ProcessLine(String line, StringBuilder output)
    {
    	
    	int currentCharIndex = 0;
    	int tempIndex = 0;
    	while(currentCharIndex < line.length())
    	{
    		char c = line.charAt(currentCharIndex);
    		if((Character.isLetter(c) || c == '_') && currentCharIndex + 1 <= line.length()-1)
    		{
    			char nextc = line.charAt(currentCharIndex+1);
    			tempIndex = currentCharIndex+1;
    			while((Character.isLetter(nextc) || nextc == '_' || Character.isDigit(nextc)) && tempIndex < line.length())
    			{
    				tempIndex++;
    				if(tempIndex < line.length())
    				{
    					nextc = line.charAt(tempIndex);
    				}
    			}
    			String str = line.substring(currentCharIndex, tempIndex);
    			//Make sure to ignore extra spaces before checking next character
    			if (isKeyword(str))
    			{
    				if(isType(str))
    				{
    					if (isNumberType(str))
    					{
    						output.append("n");
    					}
    					else
    					{
    						output.append("t");
    					}
    				}
    				else
    				{
    					output.append(GetKeywordChar(str));
    				}
    			}
    			else if(line.charAt(Math.min(line.length()-1,tempIndex+1)) == '(' || line.charAt(tempIndex) == '(')
    			{
    				output.append("f"); 
    			}
    			else
    			{
    				output.append("v");
    			}
    			currentCharIndex = tempIndex;
    		}
    		else if(Character.isDigit(c))
    		{
    			tempIndex = currentCharIndex+1;
    			while(Character.isDigit(line.charAt(tempIndex)))
    			{
    				tempIndex++;
    			}
    			output.append("d");
    			currentCharIndex = tempIndex;
    		}
    		else if(c == ' ' || c == '\t' || c == '{' || c == '}' || c == ',')
    		{
    			currentCharIndex++;
    		}
    		else if(c == '#')
    		{
    			output.append("i");
    			return;
    		}
    		else if(c == '"')
    		{
    			tempIndex = currentCharIndex + 1;
    			while (line.charAt(tempIndex) != '"')
    			{
    				tempIndex++;
    			}
    			output.append("s");
    			currentCharIndex = tempIndex+1;
    		}
    		else if(c == '\'')
    		{
    			tempIndex = currentCharIndex + 1;
    			while (line.charAt(tempIndex) != '\'')
    			{
    				tempIndex++;
    			}
    			output.append("h");
    			currentCharIndex = tempIndex+1;
    		}
    		else if(c == '/' && line.charAt(currentCharIndex+1) == '/')
    		{
    			return;
    		}
    		else
    		{
    			output.append(c);
    			currentCharIndex++;
    		}
    	}
    }
    
    public static void InitializeCPPLists(String path)
    {
    	Scanner s = null;
    	String current = null;
		try
		{
			s = new Scanner(new File(path));
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("Definitions file missing. Please locate file or re-download package and replace.");
			e.printStackTrace();
		}
		boolean inTypes = false;
    	while (s.hasNext())
    	{
    		current = s.next();
    		if(!inTypes)
    		{
    			if(current.equals("//TYPES//"))
    			{
    				inTypes = true;
    			}
    			else
    			{
    				keywords.add(current);
    			}
    		}
    		else
    		{
    			types.add(current);
    		}
    	}
    	while (s.hasNext())
    	{
    		types.add(s.next());
    	}
    	s.close();
    }
    
    public static void InitializeCommonKeywordsMap()
    {
    	commonKeywords = new HashMap<String, Character>();
    	commonKeywords.put("do", 'o');
    	commonKeywords.put("using", 'u');
    	commonKeywords.put("namespace", 'm');
    	commonKeywords.put("for", 'r');
    	commonKeywords.put("switch", 'w');
    	commonKeywords.put("case", 'c');
    	commonKeywords.put("break", 'b');
    	commonKeywords.put("if", 'x');
    	commonKeywords.put("else", 'e');
    	commonKeywords.put("false", 'l');
    	commonKeywords.put("true", 'q');
    	commonKeywords.put("while", 'y');
    	commonKeywords.put("default", 'a');
    	commonKeywords.put("return", 'z');
    }
    
    private static boolean isType(String str)
    {
    	for(String type: types) 
    	{
    	    if(str.equals(type))
    	    {
    	    	return true;
    	    }
    	}
    	return false;
    }
    
    private static boolean isNumberType(String str)
    {
    	String[] numTypes = {"short","int","long","float","double"};
    	for (int i = 0; i < numTypes.length; i++)
    	{
    		if(numTypes[i].equals(str))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    private static boolean isKeyword(String str)
    {
    	for(String keyword: keywords) 
    	{
    	    if(str.equals(keyword))
    	    {
    	       return true;
    	    }
    	}
    	return false;
    }
    
    private static char GetKeywordChar(String str)
    {
    	if(commonKeywords.containsKey(str))
    	{
    		return commonKeywords.get(str);
    	}
    	else
    	{
    		return 'k';
    	}
    }
}