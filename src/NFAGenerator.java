import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author andrew
 *
 */
public class NFAGenerator {
	private final boolean DEBUG = true;
	private StateTable nfa;
	private int index;
	private Lexical lex;
	private String regex;
	private String token;
	private int entry_ind;
	
	public NFAGenerator(String s){
		//lex = l;
		index = 0;
		entry_ind = 0;
		nfa = new StateTable();
		regex = s; // new String();
		token = new String();
	}
	
	public StateTable genNFA(){
		if(DEBUG)System.out.println("genNFA()");
/*		for(TokenC t: lex.getTokens()){
			regex = t.getLegal().get(0);
			token = t.getTitle().substring(1);
			regex();
		}*/
		regex();
		return nfa;
	}
	
	private boolean regex(){
		if(DEBUG)System.out.println("regex()");
		if(rexp()){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean rexp(){
		if(DEBUG)System.out.println("rexp()");
		if(rexp1()&&rexpprime())
			return true;
		else
			return false;
	}
	
	private boolean rexpprime(){
		if(DEBUG)System.out.println("rexpprime()");
		if(index>=regex.length()){
			return true;
		}
		if(peekChar()=='|'){
			if(match('|')&&rexp1()&&rexpprime())
				return true;
		}
		return true;
	}
	
	private boolean rexp1(){
		if(DEBUG)System.out.println("rexp1()");
		if(rexp2()&&rexp1prime())
			return true;
		return false;
	}
	
	private boolean rexp1prime(){
		if(DEBUG)System.out.println("rexp1prime()");
		if(index>=regex.length()){
			return true;
		}
		if(rexp2()&&rexp1prime())
			return true;
		return true;
	}
	
	private boolean rexp2(){
		if(DEBUG)System.out.println("rexp2()");
		if(peekChar()=='('){
			if(match('(')&&rexp()&&match(')')){
				return true;
			}
		}
		if(isRE_CHAR(peekChar())){
			char temp = peekChar();
			if(match(peekChar())&&rexp2_tail()){
				handleChar(String.valueOf(temp));
				return true;
			}
		}
		if(rexp3())
			return true;
		return false;
	}
	
	private boolean rexp2_tail(){
		if(DEBUG)System.out.println("rexp2_tail()");
		if(index>=regex.length()){
			return true;
		}
		if(peekChar()=='*'){
			return true;
		}
		if(peekChar()=='+'){
			return true;
		}
		else{
			return true;
		}
	}
	
	private boolean rexp3(){
		if(DEBUG)System.out.println("rexp3()");
		if(index>=regex.length()){
			return true;
		}
		if(char_class()){
			return true;
		}
		return true;
	}
	
	private boolean char_class(){
		if(DEBUG)System.out.println("char_class()");
		if(peekChar()=='.'){
			return true;
		}
		if(peekChar()=='['){
			if(match('[')&&char_class1())
				return true;
		}
		if(defined_class())
			return true;
		return false;
	}
	
	private boolean char_class1(){
		if(DEBUG)System.out.println("char_class1()");
		if(char_set_list())
			return true;
		if(exclude_set())
			return true;
		return false;
	}
	
	private boolean char_set_list(){
		if(DEBUG)System.out.println("char_set_list()");
		if(char_set()&&char_set_list()){
			return true;
		}
		if(peekChar()==']'){
			if(match(']'))
				return true;
		}
		return false;
	}
	
	private boolean char_set(){
		if(DEBUG)System.out.println("char_set()");
		if(isCLS_CHAR(peekChar())){
			if(match(peekChar())&&char_set_tail())
				return true;
		}
		return false;
	}
	
	private boolean char_set_tail(){
		if(DEBUG)System.out.println("char_set_tail()");
		if(index>=regex.length()){
			return true;
		}
		if(peekChar()=='-'){
			if(match('-')&&isCLS_CHAR(peekChar())){
				match(peekChar());
				return true;
			}
		}
		return true;
	}
	
	private boolean exclude_set(){
		if(DEBUG)System.out.println("exclude_set()");
		if(peekChar()=='^'){
			if(match('^')&&char_set()&&match(']')&&match('I')&&match('N')&&exclude_set_tail())
				return true;
		}
		return false;
	}
	
	private boolean exclude_set_tail(){
		if(DEBUG)System.out.println("exclude_set_tail()");
		if(peekChar()=='['){
			if(match('[')&&char_set()&&match(']'))
				return true;
		}
		if(defined_class())
			return true;
		return false;
	}

	private boolean defined_class(){
		if(DEBUG)System.out.println("define_class()");
		String token = "";
		if(peekChar()=='$'){
			match('$');
			while(isUpper(peekChar())){
				token+=peekChar();
				match(peekChar());
			}
			System.out.println(token);
		}
		return false;
	}
	
	private char peekChar(){
		if(index>=regex.length())
			return '\0';
		if(DEBUG)System.out.println(regex.charAt(index));
		return regex.charAt(index);
	}
	
	private char getChar(){
		char result = regex.charAt(index);
		index++;
		return result;
	}
	
	private boolean match(char c){
		if(peekChar()==c){
			if(DEBUG)System.out.printf("Consumed: %c\n",getChar());
			return true;
		}
		
		else{
			System.out.printf("Error: Expected %c but found %c\n", c,peekChar());
			return false;
		}
	}
	
	private boolean isRE_CHAR(char c){
		if(c>=0x20&&c<=0x7E){
			if(c!='\\'&&c!='*'&&c!='+'&&c!='?'&&c!='|'&&c!='['&&c!=']'&&c!='('&&c!=')'&&c!='.'&&c!='\''&&c!='\"'&&c!='$'){
				return true;
			}
			else if(c=='\\'){
				match('\\');
				char t = peekChar();
				if(t==' '||t=='\\'||t=='*'||t=='?'||t=='|'||t=='['||t==']'||t=='('||t==')'||t=='.'||t=='\''||t=='\"'||t=='$'){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isCLS_CHAR(char c){
		if(c>=0x20&&c<=0x7E){
			if(c!='\\'&&c!='^'&&c!='-'&&c!='['&&c!=']'){
				return true;
			}
			else if(c=='\\'){
				match('\\');
				char t = peekChar();
				if(t=='\\'||t=='^'||t=='-'||t=='['||t==']'){
						return true;
				}
			}
		}
		return false;
	}
	
	private boolean isUpper(char c){
		return c>='A'&&c<='Z';
	}
	
	private void handleChar(String c){
		Map<String,StateTable.TableRow> trans = new HashMap<String,StateTable.TableRow>();
		nfa.addState(null, Integer.toString(entry_ind)+1,"Invalid Type", entry_ind+1);
		trans.put(c, nfa.getTableRow(entry_ind+1));
		nfa.addState(trans, Integer.toString(entry_ind), "Invalid Type", entry_ind);
		entry_ind++;
	}
	
	private void handleToken(TokenC t){
		
	}
	
	private void handleUnion(){
		
	}
	
	private void handleStar(){
		
	}
	
	private void handlePlus(){
		
	}
}
