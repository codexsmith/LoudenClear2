import java.util.ArrayList;

/**
 * 
 * @author nick
 *
 *	Driver
		call walker on input with DFAtable
		store tokens from walker
 */
public class Driver {
	public static String errCode = "Error ";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PScanner readInput = new PScanner("test_input.txt");
//		Lexical lexSpec = PScanner.scanLexicon("sample_spec.txt");
//		String regex = "a*";
//		NFAGenerator gen = new NFAGenerator(regex);

		Lexical lexSpec = PScanner.scanLexicon("sample_spec2.txt");
		NFAGenerator gen = new NFAGenerator(lexSpec);
//		String regex = "\\**";
//		NFAGenerator gen = new NFAGenerator(regex);

		StateTable table = gen.genNFA();
		ArrayList<String> tokenList = new ArrayList<String>();
		String nextToken = null;
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("************************");
		System.out.println("**    Printing NFA    **");
		System.out.println("************************");
		table.printTable();
		
		
		StateTable dfa = NFAToDFA.convert(table);
		
		System.out.println("");
		System.out.println("");
		System.out.println("************************");
		System.out.println("**    Printing DFA    **");
		System.out.println("************************");
		dfa.printTable();
		
		//Consult the StateTable with the input of the user file
		while(!readInput.endOfFile()){
			nextToken = dfa.DFAlookUp(readInput.getToken());
			if (nextToken.startsWith(errCode)){//Err on Token
				System.out.println(nextToken);
				break;
			}
			else{
				tokenList.add(nextToken);
			}
		}
	}

}
