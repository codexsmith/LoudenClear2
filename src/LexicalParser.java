import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class LexicalParser {
	public static final boolean DEBUG = true;
	private File lexspec;
	private Scanner scan;
	private String buff;
	
	public LexicalParser(String path){
		lexspec = new File(path);
		try{
			scan = new Scanner(lexspec);
		}
		catch(FileNotFoundException e){
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
				a = arr.toString();
				b = a.substring(0, i).concat(a.substring(i+1));
				arr = b.toCharArray();
			}
		}
	}
	
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
	
	public Lexical scanLexicon(){		
		String line= "";
		HashMap<String, CharacterC> chars = new HashMap<String, CharacterC>();
		ArrayList<TokenC> tokens = new ArrayList<TokenC>();
		
		int state = 0; //state 0 = first comments, 1 = in characters, 2 = in identifiers.
		
		while((line = this.getLine()) != null)
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
	
	public String getLine(){
		if(scan.hasNext())
			return scan.nextLine();
		else
			return null;
	}
	
	public boolean endOfFile(){
		if(scan.nextLine() != null){
			return true;
		}
		return false;
	}
}
