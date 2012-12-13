import java.util.ArrayList;
import java.util.HashMap;

public class LLoneGenerator {
	
	private Lexical lexspec;
  private HashMap<TokenC, ArrayList<TokenC>> firstSet;
  private HashMap<TokenC, ArrayList<TokenC>> followSet;
  
	
	public LLoneGenerator(String specPath){
		if (Driver.DEBUG){ 
			System.out.println("==============LLONE==============");
		}

		LexicalParser lexparse = new LexicalParser(specPath);
		lexspec = lexparse.scanLexicon();
    
    firstSet = genFirstSet();
//     followSet = genFollowSet();
// 
    
	}
	
	public HashMap<TokenC, ArrayList<TokenC>> genFirstSet(){
		HashMap<TokenC, ArrayList<TokenC>> first = new HashMap<TokenC, ArrayList<TokenC>>();

		ArrayList<TokenC> newSet;//a rule's first set
		
		for (TokenC nonTerm:lexspec.getRules()){
      newSet = new ArrayList<TokenC>();
      for (TokenC test: lexspec.getRules()){
      
        if(nonTerm.getLegal().contains(test.getTitle())){
          newSet.add(test);
        }
      }
      first.put(nonTerm,newSet);
    }
      
      if(Driver.LLONE_DEBUG){
        for(TokenC key : first.keySet()){
          for(TokenC next : first.get(key))
          System.out.println("FIRST " + key.getTitle() + " : " + next.getTitle());
        }
      }
		
		return first;
		
	}
	
	public ArrayList<TokenC> genFollowSet(){
		ArrayList<TokenC> follow = new ArrayList<TokenC>();
		return follow;
		
		
	}
	
}
