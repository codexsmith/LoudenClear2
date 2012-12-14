import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LLoneGenerator {
	
	private Lexical lexspec;
  private HashMap<TokenC, ArrayList<String>> firstSet;
  private HashMap<TokenC, ArrayList<String>> followSet;
  private ArrayList<String> nonTerminalSet;
	
	public LLoneGenerator(String specPath){
		if (Driver.DEBUG){ 
			System.out.println("==============LLONE==============");
		}

		LexicalParser lexparse;
		lexparse = new LexicalParser(specPath);
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
    String production;
    
		for (String nonTerm : nonTerminalSet){//build first set of this nonTerm
      if(Driver.LLONE_DEBUG){System.out.println("Calculating First set of " +nonTerm );}
      int firstI = -1, endI = -1;
      newSet = new ArrayList<String>();
      TokenC putTerm = null;
      int pipeCount = 1;
      
      for(TokenC tokNonTerm : lexspec.getRules()){//find all rules that match this nonTer
        if (tokNonTerm.getTitle().compareTo(nonTerm) == 0){
          putTerm = tokNonTerm;
          
          production = tokNonTerm.getLegal();
          pipeCount = 1;
          if(Driver.LLONE_DEBUG){System.out.println("Matched Line, Production " +production+ " Pipe "+pipeCount);}
          
          if(production.contains("|")){
            for(int i = 0; i < production.length(); i++){
                if(production.charAt(i) == '|'){
                  pipeCount = pipeCount +1;
                }
            }
          }
          
          while(pipeCount > 0){
          if(production.startsWith("$") && production.length() == 1 ){
            pipeCount = 0;
          }
          if(Driver.LLONE_DEBUG){System.out.println("Pipes Left " +pipeCount );}
            if(!production.startsWith("<")){//doesnt start with a NonTerm
              if(Driver.LLONE_DEBUG){System.out.println("Start with Not < : "+ production);}
              for(String word : lexspec.getKeywords()){
                if (production.startsWith(word)){
                  if(Driver.LLONE_DEBUG){System.out.println("Matched : "+ word);}
                  newSet.add(word);
                  pipeCount--;
                  break;
                }
              }
            }
            else if (production.startsWith("<")){
              

              firstI = production.indexOf("<");
              String temp = production.substring(firstI);
              endI = temp.indexOf(">") + firstI;
//               if(Driver.LLONE_DEBUG){System.out.println("Index of < " + firstI + "  Index of > " + endI);}
              pipeCount--;

              if(Driver.LLONE_DEBUG){System.out.println("Start with Not < : "+ production);}
              if(Driver.LLONE_DEBUG){System.out.println("ADDING < : " + production.substring(firstI, endI+1));}
              newSet.add(production.substring(firstI, endI+1));
            }
            else{//check for terminal at front of input
              if(Driver.LLONE_DEBUG)System.out.println("Starts with term : "+ production);
              //we know that the first character isnt a nonterminal or a keyword
              pipeCount--;

              newSet.add(production.substring(0,1));
            }

            if(pipeCount > 0){
              production = production.substring(production.indexOf("|")+1);
            }
          }
        }
      }
      if (putTerm != null){
        first.put(putTerm,newSet);
        pipeCount = 1;
      }
    }
//     if(Driver.LLONE_DEBUG){System.out.println(first);}
    first = replaceFirst(first);
//     if(Driver.LLONE_DEBUG){System.out.println(first);}
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
  
  public HashMap<TokenC, ArrayList<String>> replaceFirst(HashMap<TokenC, ArrayList<String>> set){
    HashMap<TokenC, ArrayList<String>> inSet = set;
//     HashMap<TokenC, ArrayList<String>> outSet = null;
    boolean changes = true;
    int startI = 0, endI = 0;

    while(changes){
//       outSet = new HashMap<TokenC, ArrayList<String>>();
      //for each nonterminal rule in inSet, check for nonterminals in the production,
      //if such a nonterminal exists replace that nonterminal with the current rule
      //for that nonterminal in inSet
      for(TokenC key : inSet.keySet()){
        for(String first : inSet.get(key)){//arrayList<String>
          if (first.contains("<") && first.contains(">")){
            ArrayList<String> temp1 = inSet.get(key);
            ArrayList<String> temp2 = findReplacement(first, inSet);
            
            inSet.put(key, listMerge(temp1, temp2));
            
          }//if non-terminal
        }//for strings in inSet
      }//for key
      changes = hasChanges(inSet);
      if(Driver.LLONE_DEBUG){System.out.println("OUTSET " +inSet);}
//       inSet = outSet;
    }
     
    return inSet;
  }
  //replaces a nonterminal in A with the sequence in B
  public ArrayList<String> listMerge(ArrayList<String> A, ArrayList<String> B){
    if(Driver.LLONE_DEBUG){System.out.println("listMerge A: " + A + " B: " + B);}
    String replace = "";
    int i = 0;
    
    if(B.contains("@")){
      for(i = 0; i < A.size(); i++){
        if(A.get(i).compareTo("<epsilon>") == 0){
          A.set(i, "@");
        }
      }
    }
    else{
      for(String b : B){
        replace = replace.concat(b);
        replace = replace.concat(" ");
      }
      
      for(i = 0; i < A.size(); i++){
        if (A.get(i).contains("<") && A.get(i).contains(">")){
          A.set(i, replace);
          break;
        }
        
      }
    }
    if(Driver.LLONE_DEBUG){System.out.println("END " + A);}
    return A;
  }
  
  public ArrayList<String> findReplacement(String title, HashMap<TokenC, ArrayList<String>> set){
    ArrayList<String> outString = new ArrayList<String>();
    
    if(title.compareTo("<epsilon>") == 0){
      outString.add("@");
    }
    else{
      for(TokenC key : set.keySet()){
        if (key.getTitle().compareTo(title) == 0){
          for(String s : set.get(key)){
            outString.add(s);
          }
        }
      }
    }
    if(Driver.LLONE_DEBUG){System.out.println("Replacement for " +title + " : " + outString);}
    return outString;
  }
  
  public boolean hasChanges(HashMap<TokenC, ArrayList<String>> in){
    
     Iterator rules = in.keySet().iterator();

    while (rules.hasNext()){
      ArrayList<String> val = (ArrayList<String>)in.get(rules.next());
      for(String s : val){
        if (s.contains("<") && s.contains(">")){
          return true;
        }
      }
    }
    return false;
  }
	
	public ArrayList<TokenC> genFollowSet(){
		ArrayList<TokenC> follow = new ArrayList<TokenC>();
		return follow;
		
	}
}
