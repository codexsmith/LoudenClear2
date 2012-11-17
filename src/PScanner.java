import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * @author nick
 * 
 */

public class PScanner {
	
	public static final boolean DEBUG = true;
	
	private File in;
	private Scanner iScan;
	private String buff;
	
	
	public PScanner(String path){
		in = new File(path);
		try {
			iScan = new Scanner(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void sanitize(String str){
		char[] arr = str.toCharArray();
		int type;
		String a, b;
		for (int i = 0; i < arr.length; ++i){
			type = Character.getType(arr[i]);
			if (type != Character.LINE_SEPARATOR || type == Character.SPACE_SEPARATOR || type == Character.CONTROL || type == Character.PARAGRAPH_SEPARATOR){
				a = arr.toString(); //remove back character
				b = a.substring(0, i).concat(a.substring(i+1));
				arr = b.toCharArray();
			}
		}
	}
	
	/**MAIN RESPONSIBILITY 
	 * gets the next character from input file
	 * this is called by the tablewalker to get the next input to the DFA
	 * @return String token
	 */
	public String getToken(){
		String tok;
		if(buff.isEmpty()){
			buff = this.getLine();
		}
		sanitize(buff);
		
		tok = buff.substring(0,1);//gets first element
		buff = buff.substring(1); //removes the first element
		
		if(DEBUG){System.out.printf("Token %s", tok);}

		return tok;
	}
	
	public void pushToken(String tok){
		buff = tok.concat(buff);
	}
	
	/**
	 * reads lexical input file
	 * 
	 * Use states to keep track of what I'm looking at inside of the file (char/token)
	 * 
	 * @return lexical container for tokens and characters
	 */
	public static Lexical scanLexicon(String lexFilePath){		
		String line= "";
		iIdentifier out;
		PScanner scan = new PScanner(lexFilePath);
		HashMap<String, CharacterC> chars = new HashMap<String, CharacterC>();
		ArrayList<TokenC> tokens = new ArrayList<TokenC>();
		
		int state = 0; //state 0 = first comments, 1 = in characters, 2 = in identifiers.
		
		while((line = scan.getLine()) != null)
		{
			if(line.startsWith("%") || line.isEmpty())
			{
				if(state == 0)
					state = 1;
				else if(state == 1)
					state = 2;
			}
			else
				if(state == 1 || state == 0) // characters
				{
					if(state == 0) state = 1;
					
					//Create new char and parse line.
					CharacterC character = new CharacterC(line, chars);
					chars.put(character.getTitle(), character);
				}
				else if(state == 2) // identifiers
				{
					//create new token and parse line
					TokenC token = new TokenC(line);
					tokens.add(token);
				}			
		}
		//Create epsilon using @ symbol
		String epsilonStr = "$Epsilon [@]";
		chars.put("$Epsilon", new CharacterC(epsilonStr, chars));
		return new Lexical(tokens, chars);
	}
	
	
    /**intended for use with lexical input, not normal scanner operation.
     * this does not allow characters to be placed back into the scanning stream 
     * @return the remaining current line as a string, placing the reader at the start of the next line
     */
	public String getLine(){
		if(iScan.hasNext())
			return iScan.nextLine();
		else
			return null;
	}
	
	public boolean endOfFile(){
		if(iScan.nextLine() != null){
			return true;
		}
		return false;
	}
	
	

}
