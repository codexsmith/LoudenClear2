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
	private String regex;
	private File file;
	private int index;
	
	public Tokenizer(String f, String a){
		input = new File(f);
		lex = a;
	}
	
	public Tokenizer(File f, String r){
		file = f;
		regex = r;
		index = 0;
	}
	
	public String parse(){
		Scanner scan = null;
		output = "";
		name = "";
		accept = false;
		LexicalParser lp = new LexicalParser(lex);
		Lexical lexspec = lp.scanLexicon();
 		NFAGen ngen = new NFAGen(lexspec);

//     //LLONE TESTING
// 		if (Driver.LLNONE_DEBUG){
//       LLoneGenerator LLone = new LLoneGenerator(lexspec);
//       ArrayList<TokenC> temp = LLone.firstSet();
// 		}

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
				if(DEBUG)System.out.println(state.toString()); //test
				state = state.getTable().get(c).get(0);
				if(DEBUG)System.out.println(state.toString()); //test
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
		else{
			if(accept){
				output+=state.getToken()+" "+name+"\n";
			}
			state = dfa.getTable().get(0);
			name = "";
			accept = false;
		}
	}
	
	public ArrayList<String> getTokens(){
		ArrayList<String> result = new ArrayList<String>();
		NFAGen ngen = new NFAGen(new Lexical());
		StateTable nfa = ngen.generateNFA(regex);
		DFAConverter con = new DFAConverter(nfa);
		dfa = con.convert();
		String token = strtok();
		while(token!=null){
			result.add(token);
			token = strtok();
		}
		return result;
	}
	
	public String strtok(){
		return null;
	}
}
