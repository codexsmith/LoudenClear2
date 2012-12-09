
public class Driver {

	/**
	 * @param args
	 */
	public static boolean DEBUG = true;
	
	public static void main(String[] args) {
		MiniREParser parser = new MiniREParser();
		parser.parse("begin\n " +
				"matches = find \'[A-Z a-z]*ment[A-Z a-z]*\' in \"file1.txt\" inters find \'(A|a) [A-Z a-z]*\' in \"file2.txt\";\n" +
				"n_matches = #matches;\n" +
				"print (n_matches);\n" +
				"replace '[A-Z a-z]*ment' with \"\" in \"file1.txt\" >! \"file3.txt\";\n" +
				"end");
//		System.out.println();
//		System.out.println(output);
//		for(int i = 1; i<=7; i++){
//			String test_case = Integer.toString(i);			
// 			Tokenizer t = new Tokenizer("C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\input","C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\spec");
//			String output = t.parse();
//			System.out.println(output);
//		}
	}

}
