
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tokenizer t = new Tokenizer("sample_input.txt","sample_spec.txt");
		String output = t.parse();
		System.out.println(output);
//		for(int i = 1; i<=7; i++){
//			String test_case = Integer.toString(i);			
// 			Tokenizer t = new Tokenizer("C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\input","C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\spec");
//			String output = t.parse();
//			System.out.println(output);
//		}
	}

}
