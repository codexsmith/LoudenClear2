import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tokenizer {
	private final boolean DEBUG = false;
	private StateTable dfa;
	private File input;
	private String lex;
	private String output;
	private State state;
	private String name;
	private boolean accept;
	
	public Tokenizer(String f,String l){
		input = new File(f);
		lex = l;
	}
	
	public String parse(){
		Scanner scan = null;
		output = "";
		name = "";
		accept = false;
		LexicalParser lp = new LexicalParser(lex);
		Lexical lexspec = lp.scanLexicon();
		NFAGenerator ngen = new NFAGenerator(lexspec);
		
		LLoneGenerator LLone = new LLoneGenerator(lexspec);
		ArrayList<TokenC> temp = LLone.firstSet();
		
		StateTable nfa = ngen.generateNFA();
		DFAConverter conv = new DFAConverter(nfa);
		dfa = conv.convert();
		if(DEBUG)System.out.println(dfa);
		state = dfa.getTable().get(0);
		try {
			scan = new Scanner(input);
			scan.useDelimiter("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scan.hasNext()){
			step(scan.next());
		}
		if(accept){
			output+=state.getToken()+" "+name+"\n";
		}
		state = dfa.getTable().get(0);
		name = "";
		accept = false;
		return output;
	}
	
	private void step(String c){
		int type = Character.getType(c.charAt(0));
		if (type != Character.LINE_SEPARATOR && type != Character.SPACE_SEPARATOR && type != Character.CONTROL && type != Character.PARAGRAPH_SEPARATOR){
			if(state.getTable().get(c)==null){
				State temp = state;
				if(DEBUG)System.out.println("("+state+") -"+c+"-> null ; Accept:"+accept);
				if(accept){
					output+=state.getToken()+" "+name+"\n";
				}
				name = c;
				state = dfa.getTable().get(0);
				System.out.println(state.toString()); //test
				state = state.getTable().get(c).get(0);
				System.out.println(state.toString()); //test
				accept = false;
				if(state.getAccept()){
					accept = true;
				}
				if(DEBUG)System.out.println("("+temp+") -"+c+"-> "+"("+state+")");
			}
			else if(state.getTable().get(c)!=null){
				State temp = state;
				name+=c;
				state=state.getTable().get(c).get(0);
				if(state.getAccept()){
					accept = true;
				}
				if(DEBUG)System.out.println("("+temp+") -"+c+"-> "+"("+state+")");
			}
		}
		/*else{
			if(accept){
				output+=state.getToken()+" "+name+"\n";
			}
			state = dfa.getTable().get(0);
			name = "";
			accept = false;
		}*/
	}
}
