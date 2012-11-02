import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author nick
 * 
 *	code starts on page 512
 *	book discusses it on page 77 
 *	need the helper methods
 */


public class PScanner {
	
	public static final boolean DEBUG = true;
	
	public static ArrayList<iIdentifier> charClasses = new ArrayList<iIdentifier>(0);
	public static ArrayList<iIdentifier> tokenClasses = new ArrayList<iIdentifier>(0);
	
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
	
	/**MAIN RESPONSIBILITY 
	 * gets the next character from input file
	 * this is called by the tablewalker to get the next input to the DFA
	 * @return String token
	 */
	public String scanToken(){
		String tok;
		if(buff.isEmpty()){
			buff = iScan.nextLine();
		}
		tok = buff.substring(0,1);//sets first element
		buff = buff.substring(1); //removes the first element

		if(DEBUG){System.out.printf("Token %s", tok);}

		return tok;
	}
	
	public void pushToken(String tok){
		buff = tok.concat(buff);
	}
	
	/**reads lexical input file
	 * 
	 */
	public static void scanLexicon(String lexFilePath){
		String line= "";
		iIdentifier out;
		PScanner scan = new PScanner(lexFilePath);

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
	
	
    /**intended for use with lexical input, not normal scanner operation.
     * this does not allow characters to be placed back into the scanning stream 
     * @return the remaining current line as a string, placing the reader at the start of the next line
     */
	public String getLine(){
		return iScan.nextLine();
	}
	
	public boolean endOfFile(){
		return !iScan.hasNext();
	}

}
