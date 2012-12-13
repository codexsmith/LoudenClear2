import java.util.ArrayList;
import java.util.HashMap;

public class LLoneGenerator {
	
	private Lexical lexspec;
  private HashMap<TokenC, ArrayList<String>> firstSet;
  private HashMap<TokenC, ArrayList<String>> followSet;
  private ArrayList<String> nonTerminalSet;
	
	public LLoneGenerator(String specPath){
		if (Driver.DEBUG){ 
			System.out.println("==============LLONE==============");
		}

		LexicalParser lexparse = new LexicalParser(specPath);
		lexspec = lexparse.scanLexicon();

    nonTerminalSet = genTerminalSet();
    
    firstSet = genFirstSet();
    
//     followSet = genFollowSet();
// 
    
	}

	public ArrayList<String> genTerminalSet(){
    ArrayList<String> out = new ArrayList<String>();

    for (TokenC tok : lexspec.getRules()){
      if (!out.contains(tok.getTitle())){
        out.add(tok.getTitle());
      }
    }
    return out;
	}
	
	public HashMap<TokenC, ArrayList<String>> genFirstSet(){
		HashMap<TokenC, ArrayList<String>> first = new HashMap<TokenC, ArrayList<String>>();
    ArrayList<String> newSet;//a rule's first set

    
    
		for (String nonTerm:nonTerminalSet){//build first set of this nonTerm
      if(Driver.LLONE_DEBUG){System.out.println("Calculating First set of " +nonTerm );}
      int firstI = -1, endI = -1;
      newSet = new ArrayList<String>();
      TokenC putTerm = null;
      int pipeCount = 0;//MUST BE RESET TO ZERO SOMEWHERE
      
      for(TokenC tokNonTerm : lexspec.getRules()){//find all rules that match this nonTer
        if (tokNonTerm.getTitle().compareTo(nonTerm) == 0){
          putTerm = tokNonTerm;
          if(tokNonTerm.getLegal().contains("|")){//use indexOf
            for(int i = 0; i < tokNonTerm.getLegal().length(); i++){
              
                pipeCount = pipeCount +1;
              
            }
          }
          if(tokNonTerm.getLegal().startsWith("<")){//IS A NONTERMINAL
            for(String word : lexspec.getKeywords()){
              if (tokNonTerm.getLegal().startsWith(word)){
                newSet.add(word);
                break;
              }
            }
          }
          else if (newSet.isEmpty()){//check for terminal at front of input
              //we know that the first character isnt a nonterminal or a keyword
              newSet.add(tokNonTerm.getLegal().substring(0,1));
          }
          else if (tokNonTerm.getLegal().contains("<") && newSet.isEmpty()){
            if(Driver.LLONE_DEBUG){System.out.println("ADDING <");}

            firstI = tokNonTerm.getLegal().indexOf("<");
            String temp = tokNonTerm.getLegal().substring(firstI);
            endI = temp.indexOf(">") + firstI;
            if(Driver.LLONE_DEBUG){System.out.println("Index of < " + firstI + "  Index of > " + endI);}

            newSet.add(tokNonTerm.getLegal().substring(firstI, endI+1));
          }
          
      }
       
    }
    if (putTerm != null){
      first.put(putTerm,newSet);
    }
    }
      
    if(Driver.LLONE_DEBUG){
      for(TokenC key : first.keySet()){
        System.out.print("FIRST " + key.getTitle() + " --> ");
        for(String next : first.get(key)){
          System.out.print(next + " : ");
        }
        System.out.println("");
      }
    }
      
		return first;
	}

	
	public ArrayList<TokenC> genFollowSet(){
		ArrayList<TokenC> follow = new ArrayList<TokenC>();
		return follow;
		
		
	}
	
}
