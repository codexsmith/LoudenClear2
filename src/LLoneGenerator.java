import java.util.ArrayList;


public class LLoneGenerator {
	
	private Lexical lexspec;
  private ArrayList<TokenC> firstSet = new ArrayList<TokenC>();
  private ArrayList<TokenC> followSet = new ArrayList<TokenC>();
  private ArrayList<TokenC> terminalSet = new ArrayList<TokenC>();
  
	
	public LLoneGenerator(String specPath){
		if (Driver.DEBUG){ 
			System.out.println("==============LLONE==============");
		}

		LexicalParser lexparse = new LexicalParser(specPath);
		lexspec = lexparse.scanLexicon();
    
    terminalSet = genTerminals();
		
    firstSet = genFirstSet();
    followSet = genFollowSet();

    
	}

  public ArrayList<TokenC> genTerminals(){
    ArrayList<TokenC> results = new ArrayList<TokenC>();
    if(Driver.LLONE_DEBUG){System.out.println("LOLS");}
    int i = 0;
    
    for (TokenC t:lexspec.getRules()){
      t.getLegal();
      if(Driver.LLONE_DEBUG){System.out.println("LOLS " + i);}
      i++;
      if(Driver.LLONE_DEBUG){
        System.out.println("Gen Terminals"+ t.getTitle());
      }
    }
    return results;
  }
	
	public ArrayList<TokenC> genFirstSet(){
		ArrayList<TokenC> first = new ArrayList<TokenC>();
		
		for (TokenC t:lexspec.getRules()){
			t.getLegal();
			if(Driver.LLONE_DEBUG){
        System.out.println("First Set " + t.getTitle());
      }
		}
		
		return first;
		
	}
	
	public ArrayList<TokenC> genFollowSet(){
		ArrayList<TokenC> follow = new ArrayList<TokenC>();
		return follow;
		
		
	}
	
}
