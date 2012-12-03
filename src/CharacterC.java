import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.StringTokenizer;
/** 
 *	Contains the representations of the Regex Characters from the lexical spec
 *  Example: $DIGIT [0-9]
 *	
 */
public class CharacterC extends iIdentifier{

	private ArrayList<String> legal;
	private String title;
	
	/**
	 * Constructor
	 * 
	 * @param line String to parse
	 * @param map map to reference for other CharacterC's
	 */
	public CharacterC(String line, HashMap<String, CharacterC> map){
		String title = "", symbols;
		legal = new ArrayList<String>();
		
		line = line.trim();//removes leading & trailing whitespaces
		
		//Split into tokens for easy references
		StringTokenizer tokenizer = new StringTokenizer(line, " ", false);
		this.title = tokenizer.nextToken(); // Set title
		System.out.println("CharacterC TITLE "+this.title);
		String token = "";
		String regex = "";
		ArrayList<String> otherCharLegal = null;
		while(tokenizer.hasMoreTokens()) //while still has tokens
		{
			token = tokenizer.nextToken();
			if(token.equals("IN")) //can ignore IN
				continue;
			else if(token.startsWith("$")) //Reference to other Character
			{
				CharacterC charC = map.get(token);
				otherCharLegal = charC.getLegal();
				
			}
			else if(token.startsWith("[")) //This CharacterC's regex
			{
				//remove brackets and save token
				regex = token.substring(1, token.length()-1);
			}
		}
		
		//If reference to other Char exists, get legals of it
		if(otherCharLegal != null)
			legal = (ArrayList<String>) otherCharLegal.clone();
		
		//parse and set regex
		parseRegex(regex);
	}

	/* (non-Javadoc)
	 * @see iIdentifier#getLegal()
	 */
	@Override
	public ArrayList<String> getLegal() {
		return legal;
	}

	/* (non-Javadoc)
	 * @see iIdentifier#isLegal(java.lang.String)
	 */
	@Override
	public boolean isLegal(String c) {
		for (String s : legal){
			if (s.compareTo(c) == 0){
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see iIdentifier#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Parse the regex found in brackets to form a legal assignment for Character
	 * 
	 * @param regex regex to parse
	 */
	private void parseRegex(String regex) //a-z, ^0, A-Z
	{
		int strLen = regex.length();
		if(regex.startsWith("^")) //is a not
		{
			
			for(int i = 1; i < strLen; i++)
			{
				if(regex.length() > i + 2 && regex.charAt(i+1) == '-') //fits a-z pattern
				{
					for(int j = regex.charAt(i); j <= regex.charAt(i+2); j++) //ignore - char
					{
						legal.remove(String.valueOf((char)j));
					}
					i += 2; //account for extra char's used in pattern.
				}
				else //single char
				{
					char toRemove = regex.charAt(i);
					String strToRemove = String.valueOf(toRemove);
					legal.remove(strToRemove);
				}
				
			}
		}
		else // normal regex, not a not.
		{
			for(int i = 0; i < strLen; i++)
			{
 				if(regex.length() > i + 2 && regex.charAt(i+1) == '-') //fits a-z pattern
				{
					for(int j = regex.charAt(i); j <= regex.charAt(i+2); j++) //ignore - char
					{
						legal.add(String.valueOf((char)j));
					}
					i += 2; //account for extra char's used in pattern.
				}
				else //single char
				{
					legal.add(String.valueOf(regex.charAt((char)i)));
				}
				
			}
		}
	}	
	
}
