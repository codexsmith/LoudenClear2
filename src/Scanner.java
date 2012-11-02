import java.util.ArrayList;

/**
 * 
 * @author nick
 * 
 *	code starts on page 512
 *	book discusses it on page 77 
 *	need the helper methods
 */


public class Scanner {
	
	public static ArrayList<iIdentifier> charClasses = new ArrayList<iIdentifier>(0);
	public static ArrayList<iIdentifier> tokenClasses = new ArrayList<iIdentifier>(0);

	
	//dictionary to contain lexical spec
	//parse dictionary return values for more tokens to lookup
	
	/**MAIN RESPONSIBILITY 
	 * Gets the next token from the source file as recognized by the DFA
	 * 
	 * @return String token
	 */
	public String getToken(){
		String tok = null;
		
		return tok;
	}
	
	public static void scanLexicon(){
		String line= "";
		iIdentifier out;
		Scanner scan = new Scanner();

		String pre_tok = scan.getLine(); 
		if(pre_tok.startsWith("%%")){
			while(!scan.getLine().startsWith("%%") && !scan.endOfFile()){
				line = scan.getLine();
				out = new CharacterC(line);
				charClasses.add(out);
			}
			while (!scan.endOfFile()){
				line = scan.getLine();
				out = new TokenC(line);
				tokenClasses.add(out);
			}
		}
		else{
			System.out.println("invalid lex spec");
		}
	}
	
	public String getLine(){ //gets a line from the SYNTAX FILE, not input
		return "line";
	}
	
	public boolean endOfFile(){
		return false;
	}

}
