import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 */

public class PScanner {
	
	public static final boolean DEBUG = false;
	
	private File in;
	private String buff;
	private Scanner iScan; 
	
	
	public PScanner(String path){
	//PHASE II BEGINS
		this.in = new File(path);
		this.buff = "";
		
		in = new File(path);
		try {
			iScan = new Scanner(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
//	if(Driver.DEBUG){System.out.println("");;};
	
	public void sanitize(){
		String str = new String(this.buff);
		int type;
		String out = new String();
		for (int i = 0; i < str.length(); i++){
			type = Character.getType(str.charAt(i));
			if (type == Character.DIRECTIONALITY_WHITESPACE || type == Character.LINE_SEPARATOR || type == Character.SPACE_SEPARATOR || type == Character.CONTROL || type == Character.PARAGRAPH_SEPARATOR){
				continue;
			}
			else{
				out = out.concat(String.valueOf(str.charAt(i)));
			}
		}
//		if(Driver.DEBUG){System.out.println("San out buff "+ out);};
		this.buff = out;
	}
	
	/**MAIN RESPONSIBILITY 
	 * gets the next character from input file
	 * this is called by the tablewalker to get the next input to the DFA
	 * @return String token
	 */
	public String getToken(){
		String tok = "";
		if (this.buff.isEmpty()){
			this.buff = iScan.nextLine();
			sanitize();
			if(Driver.DEBUG){System.out.println("post san buff "+ this.buff);};
		}
		
		
		tok = this.buff.substring(0,1);//gets first element
		this.buff = this.buff.substring(1); //removes the first element
		
		if(DEBUG){System.out.printf("Token %s", tok);}

		return tok;
	}
	
	public void pushToken(String tok){
		this.buff = tok.concat(this.buff);
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
		if(this.buff.length() == 0 && !iScan.hasNextLine()){
			return true;
		}
		return false;
	}
	
	

}
