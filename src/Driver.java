/**
 * 
 * @author nick
 *
 *	Driver
		call walker on input with DFAtable
		store tokens from walker
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Lexical lexSpec = PScanner.scanLexicon("sample_spec2.txt");
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
		
		
		StateTable dfa = NFAToDFA.convert(table);
		
		System.out.println("");
		System.out.println("");
		System.out.println("************************");
		System.out.println("**    Printing DFA    **");
		System.out.println("************************");
		dfa.printTable();
	}

}
