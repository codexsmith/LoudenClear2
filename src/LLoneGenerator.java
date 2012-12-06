import java.util.ArrayList;


public class LLoneGenerator {
	
	private Lexical lexspec;
	
	public LLoneGenerator(Lexical lex){
		if (Driver.DEBUG){ 
			System.out.println("==============LLONE==============");

//			for (TokenC t:lexspec.getTokens()){
//				System.out.println(t.getTitle());
//			}
		}
		
		lexspec = lex;
	}
	
	public ArrayList<TokenC> firstSet(){
		ArrayList<TokenC> first = new ArrayList<TokenC>();
		
		for (TokenC t:lexspec.getTokens()){
			t.getLegal();
		}
		return first;
		
	}
	
	public ArrayList<TokenC> followSet(){
		ArrayList<TokenC> follow = new ArrayList<TokenC>();
		return follow;
		
		
	}
	
}
