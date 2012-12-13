import java.util.ArrayList;


public class NFAGenerator {
	private final boolean DEBUG = false;
	private StateTable nfa;
	private Lexical lexspec;
	private String regex;
	private int index;
	private String token;
	private boolean epsilon;
	private boolean star;
	private boolean plus;
	private String charset_regex;
	
	public NFAGenerator(Lexical l){
		lexspec = l;
		nfa = new StateTable();
		regex = "";
		token = "";
		index = 0;
		epsilon = false;
		star = false;
		plus = false;
	}
	
	public StateTable generateNFA(){
		int i = 0;
		nfa = regex();
		for(State s: nfa.getTable()){
			s.setName(Integer.toString(i));
			i++;
		}
		return nfa;
	}
	
	public StateTable generateNFA(String st){
		regex = st;
		nfa = regex_single();
		int i = 0;
		for(State s: nfa.getTable()){
			s.setName(Integer.toString(i));
			i++;
		}

		return nfa;
	}
	
	public StateTable regex(){
		StateTable result = new StateTable();
		StateTable temp;
		
		populate(result,"\\@");
		for(TokenC t:lexspec.getTokens()){
			regex = t.getLegal();
			index = 0;
			token = t.getTitle().substring(1);
			temp = rexp();
			branch(result,temp);
			result.getTable().getLast().accept(token);
		}
		return result;
	}
	
	public StateTable regex_single(){
		StateTable result = new StateTable();
		result.getTable().add(new State());
		result = rexp();
		result.getTable().getLast().accept("accept");
		return result;
	}
	
	public StateTable rexp(){
		if(DEBUG)System.out.println("rexp()");
		if(isDone()){
			return null;
		}
		StateTable result;
		StateTable temp1 = rexp1();
		StateTable temp2 = null;
		if(peekChar()=='|'){
			result = new StateTable();
			result.getTable().add(new State());
			temp2 = rexp_();
			unionTables(result,temp1,temp2);
			return result;
		}
		else{
			result = temp1;
		}
		return result;
	}
	
	public StateTable rexp_(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("rexp_()");
		StateTable result = new StateTable();
		if(peekChar()=='|'){
			match('|');
			result = rexp1();
			StateTable temp = null;
			if(result!=null){
				temp = rexp_();
			}
			concatTables(result,temp);
		}
		else{
			populate(result,"\\@");
			epsilon = false;
		}
		return result;
	}
	
	public StateTable rexp1(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("rexp1()");
		StateTable result;
		StateTable temp;
		result = rexp2();
		temp = rexp1_();
		if(result==null){
			return null;
		}
		concatTables(result,temp);
		epsilon = false;
		return result;
	}
	
	public StateTable rexp1_(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("rexp1_()");
		StateTable result;
		StateTable temp;
		result = rexp2();
		if(result==null){
			result = new StateTable();
			populate(result,"\\@");
		}
		else{
			if(!epsilon){
				temp = rexp1_();
				concatTables(result,temp);
				epsilon = true;
			}
		}
		return result;
	}
	
	public StateTable rexp2(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("rexp2()");
		StateTable result = null;
		StateTable temp;
		if(peekChar()=='('){
			match('(');
			temp = rexp();
			match(')');
			rexp2_tail();
			if(star){
				result = new StateTable();
				result.getTable().add(new State());
				concatTables(result,temp);
				concatStates(result.getTable().getFirst(),temp.getTable().getLast(),"\\@");
				concatStates(temp.getTable().getLast(),temp.getTable().getFirst(),"\\@");
				temp = new StateTable();
				temp.getTable().add(new State());
				concatTables(result,temp);
				star = false;
			}
			else if(plus){
				result = temp;
				temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(result.getTable().getLast(),result.getTable().getFirst(),"\\@");
				concatTables(result,temp);
				plus = false;
			}
			else{
				result = temp;
			}
		}
		else if(isRE_CHAR(peekChar())){
			char c = peekChar();
			match(peekChar());
			rexp2_tail();
			temp = new StateTable();
			populate(temp,String.valueOf(c));
			if(star){
				result = new StateTable();
				result.getTable().add(new State());
				concatTables(result,temp);
				concatStates(result.getTable().getFirst(),temp.getTable().getLast(),"\\@");
				concatStates(temp.getTable().getLast(),temp.getTable().getFirst(),"\\@");
				temp = new StateTable();
				temp.getTable().add(new State());
				concatTables(result,temp);
				star = false;
			}
			else if(plus){
				result = temp;
				concatStates(result.getTable().getLast(),result.getTable().getFirst(),"\\@");
				plus = false;
			}
			else{
				result = temp;
			}
		}
		else{
			if(!epsilon)
				result = rexp3();
			else{
				result = null;
			}
		}
		return result;
	}
	
	public StateTable rexp2_tail(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("rexp2_tail()");
		StateTable result = null;
		if(peekChar()=='*'){
			star = true;
			match('*');
		}
		else if(peekChar()=='+'){
			plus = true;
			match('+');
		}
		else{
			
		}
		return result;
	}
	
	public StateTable rexp3(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("rexp3()");
		StateTable result;
		result = char_class();
		if(result==null){
			result = new StateTable();
			populate(result,"\\@");
			epsilon = true;
		}
		return result;
	}
	
	public StateTable char_class(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("char_class()");
		StateTable result;
		if(peekChar()=='.'){
			match('.');
			CharacterC period = new CharacterC("[.]", lexspec.getCharacters());
			result = new StateTable();
			result.getTable().add(new State());
			ArrayList<String> legals = period.getLegal();
			State rendev = new State();
			for(String s: legals){
				StateTable temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(result.getTable().getFirst(), rendev,s);
			}
			StateTable temp = new StateTable();
			result.getTable().add(rendev);
			temp.getTable().add(new State());
			concatTables(result,temp);
		}
		else if(peekChar()=='['){
			match('[');
			charset_regex = "[";
			result = char_class1();
			CharacterC period = new CharacterC(charset_regex, lexspec.getCharacters());
			result = new StateTable();
			result.getTable().add(new State());
			ArrayList<String> legals = period.getLegal();
			State rendev = new State();
			for(String s: legals){
				StateTable temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(result.getTable().getFirst(), rendev,s);
			}
			StateTable temp = new StateTable();
			result.getTable().add(rendev);
			temp.getTable().add(new State());
			concatTables(result,temp);
		}
		else{
			String cn = defined_class();
			if(cn==null)
				return null;
			result = new StateTable();
			result.getTable().add(new State());
			ArrayList<String> legals = lexspec.getCharacters().get(cn).getLegal();
			State rendev = new State();
			for(String s: legals){
				StateTable temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(result.getTable().getFirst(), rendev,s);
			}
			StateTable temp = new StateTable();
			result.getTable().add(rendev);
			temp.getTable().add(new State());
			concatTables(result,temp);
		}
		return result;
	}
	
	public StateTable char_class1(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("char_class1()");
		StateTable result;
		result = char_set_list();
		result = exclude_set();
		return result;
	}
	
	public StateTable char_set_list(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("char_set_list()");
		if(peekChar()==']'){
			charset_regex+=getChar();
		}
		else{
			char_set();
//			System.out.println(epsilon);
			if(!epsilon){
				char_set_list();
			}
		}
		return null;
	}
	
	public StateTable char_set(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("char_set()");
		if(isCLS_CHAR(peekChar())){
			charset_regex += getChar();
			char_set_tail();
			epsilon = false;
		}
		return null;
	}
	
	public StateTable char_set_tail(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("char_set_tail()");
		if(peekChar()=='-'){
			charset_regex += getChar();
			if(isCLS_CHAR(peekChar())){
				charset_regex += getChar();
			}
		}
		else{
			epsilon = true;
		}
		return null;
	}
	
	public StateTable exclude_set(){
		if(isDone()){
			return null;
		}
		if(DEBUG)System.out.println("exclude_set()");
		if(peekChar()=='^'){
			charset_regex += getChar();
			char_set();
			if(match(']')&&match('I')&&match('N')){
				charset_regex += "]";
				charset_regex += "I";
				charset_regex += "N";
			}
			exclude_set_tail();
		}
		return null;
	}
	
	public StateTable exclude_set_tail(){
		if(match('[')){
			charset_regex += "[";
			char_set();
			if(match(']')){
				charset_regex += "]";
			}
		}
		else{
			charset_regex += defined_class();
		}
		return null;
	}
	
	private void concatTables(StateTable s1,StateTable s2){
		if(s1==null || s2==null){
			return;
		}
		State state1 = s1.getTable().getLast();
		State state2 = s2.getTable().getFirst();
		concatStates(state1,state2,"\\@");
		for(State s: s2.getTable()){
			s1.getTable().add(s);
		}
	}
	
	private void concatStates(State s1, State s2, String key){
		s1.addTrans(key, s2);
	}
	
	private void unionTables(StateTable ep, StateTable s1, StateTable s2){
		State rendev = new State();
		concatStates(s1.getTable().getLast(),rendev,"\\@");
		concatStates(s2.getTable().getLast(),rendev,"\\@");
		concatStates(ep.getTable().getLast(),s1.getTable().getFirst(),"\\@");
		concatStates(ep.getTable().getLast(),s2.getTable().getFirst(),"\\@");
		for(State s: s1.getTable()){
			ep.getTable().add(s);
		}
		for(State s: s2.getTable()){
			ep.getTable().add(s);
		}
		ep.getTable().add(rendev);
	}
	
	private void branch(StateTable stem, StateTable branch){
		if(branch==null){
			return;
		}
		concatStates(stem.getTable().get(1),branch.getTable().getFirst(),"\\@");
		for(State s: branch.getTable()){
			stem.getTable().add(s);
		}
	}
	
	/**
	 * Parses a defined class given after a '$' character
	 * @return name of the CharacterC object
	 */
	private String defined_class(){
		if(DEBUG)System.out.println("define_class()");
		String token = "";
		if(peekChar()=='$'){
			token+='$';
			match('$');
			while(isUpper(peekChar())){
				token+=peekChar();
				match(peekChar());
			}//end while
			if(DEBUG)System.out.println(token);
			return token;
		}//end if
		return null;
	}//end defined_class method
	
	/**
	 * Peeks at the next character in the regex without consuming it.
	 * @return Character read from the regular expression
	 */
	private char peekChar(){
		if(index>=regex.length())
			return '\0';
//		if(DEBUG)System.out.println(regex.charAt(index));
//		System.out.println(regex.charAt(index));
		return regex.charAt(index);
	}//end peekChar method
	
	/**
	 * Same as peekChar except this one consumes the character
	 * @return Character consumed from the regular expression
	 */
	private char getChar(){
		char result = regex.charAt(index);
//		System.out.println(result);
		index++;
		return result;
	}//end getChar method
	
	/**
	 * Checks if the next character is an expected character
	 * @param c Character expected
	 * @return True if the expected character and the next character matches.
	 */
	private boolean match(char c){
		if(peekChar()==c){
			if(DEBUG)System.out.printf("Consumed: %c\n",getChar());
			else{
				getChar();
			}//end else
			return true;
		}//end if
		
		else{
			System.out.printf("Error: Expected %c but found %c\n", c,peekChar());
			return false;
		}//end else
	}//end math method
	
	/**
	 * Checks if a character is an instance of RE_CHAR
	 * @param c Character to be checked
	 * @return True of c is an instance of RE_CHAR
	 */
	private boolean isRE_CHAR(char c){
		if(c>=0x20&&c<=0x7E){
			if(c!='\\'&&c!='*'&&c!='+'&&c!='?'&&c!='|'&&c!='['&&c!=']'&&c!='('&&c!=')'&&c!='.'&&c!='\''&&c!='\"'&&c!='$'){
				return true;
			}//end if
			else if(c=='\\'){//character is escaped
				match('\\');
				char t = peekChar();
				if(t==' '||t=='\\'||t=='*'||t=='+'||t=='?'||t=='|'||t=='['||t==']'||t=='('||t==')'||t=='.'||t=='\''||t=='\"'||t=='$'){
					return true;
				}//end else if
			}//end if
		}//end if
		return false;
	}//end isRE_CHAR method
	
	/**
	 * Checks if a character is an instance of CLS_CHAR
	 * @param c Character to be checked
	 * @return True of c is an instance of CLS_CHAR
	 */
	private boolean isCLS_CHAR(char c){
		if(c>=0x20&&c<=0x7E){
			if(c!='\\'&&c!='^'&&c!='-'&&c!='['&&c!=']'){
				return true;
			}//end if
			else if(c=='\\'){
				match('\\');
				char t = peekChar();
				if(t=='\\'||t=='^'||t=='-'||t=='['||t==']'){
						return true;
				}//end if
			}//end else if
		}//end if
		return false;
	}//end isCLS_CHAR method
	
	/**
	 * Checks if a character is an upper case character
	 * @param c Character to be checked
	 * @return True if uppercase.
	 */
	private boolean isUpper(char c){
		return c>='A'&&c<='Z';
	}//end isUpper method
	
	private void populate(StateTable st, String key){
		State s1 = new State();
		State s2  = new State();
		s1.addTrans(key, s2);
		st.getTable().add(s1);
		st.getTable().add(s2);
	}
	
	private boolean isDone(){
		if(peekChar()=='\0'||peekChar()==')'){
			return true;
		}
		else return false;
	}
}
