


public class NFAGenerator {
	private final boolean DEBUG = true;
	private StateTable nfa;
	private int index;
	private boolean failed;
	private Lexical lex;
	private String regex;
	
	public NFAGenerator(Lexical l){
		lex = l;
		index = 0;
		nfa = new StateTable();
		regex = new String();
	}
	
	
	public StateTable genNFA(){
		if(DEBUG)System.out.println("genNFA()");
		regex();
		return nfa;
	}
	
	private void regex(){
		if(DEBUG)System.out.println("regex()");
		rexp();
	}
	
	private void rexp(){
		if(DEBUG)System.out.println("rexp()");
		rexp1(); rexpprime();
	}
	
	private void rexpprime(){
		if(DEBUG)System.out.println("rexpprime()");
		if(peekChar()=='|'){
			match('|'); rexp1(); rexpprime();
		}
		else {
			return;
		}			
	}
	
	private void rexp1(){
		if(DEBUG)System.out.println("rexp1()");
		rexp2();rexp1prime();
	}
	
	private void rexp1prime(){
		if(DEBUG)System.out.println("rexp1prime()");
		if(peekChar()=='\0')
			return;
		else{
			rexp2();rexp1prime();
		}
	}
	
	private void rexp2(){
		if(DEBUG)System.out.println("rexp2()");
		if(peekChar()=='('){
			match('(');rexp();match(')');
		}
		else if(isRE_CHAR(peekChar())){
			match(peekChar());rexp2_tail();
		}
		else{
			rexp3();
		}
	}
	
	private void rexp2_tail(){
		if(DEBUG)System.out.println("rexp2_tail()");
		if(peekChar()=='*'){
			return;
		}
		else if(peekChar()=='+'){
			return;
		}
		else{
			return;
		}
	}
	
	private void rexp3(){
		if(DEBUG)System.out.println("rexp3()");
		if(peekChar()=='\0'){
			return;
		}
		else{
			char_class();
		}
	}
	
	private void char_class(){
		if(DEBUG)System.out.println("char_class()");
		if(peekChar()=='.'){
			return;
		}
		if(peekChar()=='['){
			match('[');char_class1();
		}
		else{
			defined_class();
		}
	}
	
	private void char_class1(){
		if(DEBUG)System.out.println("char_class1()");
		if(peekChar()=='^'){
			exclude_set();
		}
		else{
			char_set_list();
		}
	}
	
	private void char_set_list(){
		if(DEBUG)System.out.println("char_set_list()");
		if(peekChar()==']'){
			return;
		}
		else{
			char_set(); char_set_list();
		}
	}
	
	private void char_set(){
		if(DEBUG)System.out.println("char_set()");
		if(isCLS_CHAR(peekChar())){
			match(peekChar()); char_set_tail();
		}
		else{
			error();
		}
	}
	
	private void char_set_tail(){
		if(DEBUG)System.out.println("char_set_tail()");
		if(peekChar()=='-'){
			match('-');
			if(isCLS_CHAR(peekChar())){
				match(peekChar());
				return;
			}
		}
		else{
			return;
		}
	}
	
	private void exclude_set(){
		if(DEBUG)System.out.println("exclude_set()");
		if(peekChar()=='^'){
			match('^'); char_set();match(']');match('I');match('N');exclude_set_tail();
		}
	}
	
	private void exclude_set_tail(){
		if(DEBUG)System.out.println("exclude_set_tail()");
		if(peekChar()=='['){
			match('['); char_set();match(']');
		}
		else{
			defined_class();
		}
	}

	private void defined_class(){
		if(DEBUG)System.out.println("define_class()");
		
	}
	
	private char peekChar(){
		if(index>regex.length())
			return '\0';
		if(DEBUG)System.out.println(regex.charAt(index));
		return regex.charAt(index);
	}
	
	private char getChar(){
		char result = regex.charAt(index);
		index++;
		return result;
	}
	
	private void match(char c){
		if(peekChar()==c){
			System.out.printf("Consumed: %c\n",getChar());
			return;
		}
		
		else{
			System.out.printf("Error: Expected %c but found %c\n", c,peekChar());
			System.exit(1);
		}
	}
	
	private boolean isRE_CHAR(char c){
		if(c>=0x20&&c<=0x7E){
			if(c!='\\'&&c!='*'&&c!='+'&&c!='?'&&c!='|'&&c!='['&&c!=']'&&c!='('&&c!=')'&&c!='.'&&c!='\''&&c!='\"'){
				return true;
			}
			else if(c=='\\'){
				match('\\');
				char t = peekChar();
				if(t==' '||t=='\\'||t=='*'||t=='?'||t=='|'||t=='['||t==']'||t=='('||t==')'||t=='.'||t=='\''||t=='\"'){
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
	

	
	private void error(){
		System.out.println("Error: Unexpected token found!");
		System.exit(1);
	}
}
