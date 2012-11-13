import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
/** Character classes require input that is fully enumerated. [a-z] will not work. you must use [a,b,...,w,x,y,z]
 * 
 * @author nick
 *	
 *	
 */

public class CharacterC extends iIdentifier{

	private ArrayList<String> legal;
	private String title;
	
	public CharacterC(String line, HashMap<String, CharacterC> map){
		String title = "", symbols;
		legal = new ArrayList<String>();
		
		line = line.trim();//removes leading & trailing whitespaces
		
		StringTokenizer tokenizer = new StringTokenizer(line, " ", false);
		this.title = tokenizer.nextToken();
		String token = "";
		String regex = "";
		ArrayList<String> otherCharLegal = null;
		while(tokenizer.hasMoreTokens())
		{
			token = tokenizer.nextToken();
			if(token.equals("IN"))
				continue;
			else if(token.startsWith("$"))
			{
				CharacterC charC = map.get(token);
				otherCharLegal = charC.getLegal();
				
			}
			else if(token.startsWith("["))
			{
				//remove brackets and save token
				regex = token.substring(1, token.length()-1);
			}
		}
		
		if(otherCharLegal != null)
			legal = (ArrayList<String>) otherCharLegal.clone();
		
		parseRegex(regex);
	}

	@Override
	public ArrayList<String> getLegal() {
		return legal;
	}

	@Override
	public boolean isLegal(String c) {
		for (String s : legal){
			if (s.compareTo(c) == 0){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getTitle() {
		return title;
	}
	
	private void parseRegex(String regex) //a-z, ^0, A-Z
	{
		int strLen = regex.length();
		if(regex.startsWith("^")) //not
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
		else // normal regex
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
