import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class LexicalParser {

	private File lexspec;
	private Scanner scan;
	private String buff;
	
	public LexicalParser(String path){
	if(Driver.LEX_PARSE_DEBUG){ System.out.println("LexParse Constr");}
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
		
		if(Driver.LEX_PARSE_DEBUG){System.out.printf("Token %s", tok);}

		return tok;
	}

	public Lexical scanLexicon(){
    if(Driver.LEX_PARSE_DEBUG){System.out.println("LexParse ScanLex");}
		String line= "";
		HashMap<String, CharacterC> chars = new HashMap<String, CharacterC>();
		ArrayList<TokenC> tokens = new ArrayList<TokenC>();
		ArrayList<TokenC> rules = new ArrayList<TokenC>();
		
		int state = 0; //state 0 = first comments, 1 = in characters, 2 = in identifiers.
		
		while((line = this.getLine()) != null)
		{
      if(Driver.LEX_PARSE_DEBUG){ System.out.println("Lex Line :" +line);}
			if(line.startsWith("%") || line.isEmpty())
			{
				if(state == 0){
          if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}
					state = 1;
        }
				else if(state == 1){
          if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}
					state = 2;
        }
				else if (state == 2){//start symbol
          if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}
					state =3;
        }
				else if (state == 3){//rules
          if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}
					state =4;
				}
				
				if(state == 1 || state == 0) // characters
				{
          if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}
					if(state == 0) state = 1;
					
					//Create new char and parse line.
					CharacterC character = new CharacterC(line, chars);
					chars.put(character.getTitle(), character);
				}
				else if(state == 2) // identifiers
				{
         if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}

					//create new token and parse line
					TokenC token = new TokenC(line);
					tokens.add(token);
				}
				else if (state == 3 || state == 4){
          if(Driver.LEX_PARSE_DEBUG){ System.out.println(state);}

					if (line.startsWith("<")){//MINI RE rules
						TokenC token = new TokenC(line);
						rules.add(token);
					}
				}
			}
		}
		//Create epsilon using @ symbol
		String epsilonStr = "$Epsilon [@]";
		chars.put("$Epsilon", new CharacterC(epsilonStr, chars));
		return new Lexical(tokens, chars, rules);
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
