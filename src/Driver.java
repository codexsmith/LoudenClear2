
public class Driver {

	/**
	 * @param args
	 */
  public boolean DEBUG = true;
	public static void main(String[] args) {
		for(int i = 1; i<=7; i++){
			String test_case = Integer.toString(i);
			Tokenizer t = new Tokenizer("C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\input","C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\spec");
			String output = t.parse();
			System.out.println(output);
		}
	}

}
