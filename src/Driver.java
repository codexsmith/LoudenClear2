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
		
		Lexical lexSpec = PScanner.scanLexicon("sample_spec.txt");
		String regex = "a";
		NFAGenerator gen = new NFAGenerator(regex);
		StateTable table = gen.genNFA();
		table.printTable();
	}

}
