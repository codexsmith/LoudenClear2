import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author nick
 *
 *	Driver
		call walker on input with DFAtable
		store tokens from walker
 */
public class Driver {
	
	public static String errCode = "error ";
	public static boolean DEBUG = true;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String spec_file = args[0];
		String input_file = args[1];
		
		Lexical lexSpec = PScanner.scanLexicon(spec_file);
		PScanner readInput = new PScanner(input_file);
		
		ArrayList<String> tokenList = new ArrayList<String>();

		//Lexical lexSpec = PScanner.scanLexicon("sample_spec2.txt");
//		PScanner readInput = new PScanner("test_input.txt");
//		String nextToken;
//		ArrayList<String> tokenList = new ArrayList<String>();
		
		NFAGenerator gen = new NFAGenerator(lexSpec);
//		String regex = "\\**";
//		NFAGenerator gen = new NFAGenerator(regex);
		StateTable table = gen.genNFA();
		
		System.out.println("");
		System.out.println("");
		System.out.println("************************");
		System.out.println("**    Printing NFA    **");
		System.out.println("************************");
		table.printTable();
		
		
		StateTable dfa = NFAToDFA.convert(table, lexSpec);
		
		System.out.println("");
		System.out.println("");
		System.out.println("************************");
		System.out.println("**    Printing DFA    **");
		System.out.println("************************");
		dfa.printTable();
		
		//Consult the StateTable with the input of the user file
		tokenList = dfa.DFALookup(readInput);
		
		System.out.println("Accepted Tokens (Complete)");
		for(String s : tokenList){
			System.out.println(s);
		}
	
	}

}
