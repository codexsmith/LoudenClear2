import java.util.ArrayList;


public class NFAGen {
	private final boolean DEBUG = false;
	private final int STAR = 1;
	private final int PLUS = 2;
	private int index;
	private String regex;
	private StateTable nfa;
	private Lexical lexspec;
	
	public NFAGen(Lexical l){
		index = 0;
		regex = "";
		nfa = new StateTable();
		lexspec = l;
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
	
	public StateTable generateNFA(String regex){
		this.regex = regex;
		int i = 0;
		nfa = regex_single();
		for(State s: nfa.getTable()){
			s.setName(Integer.toString(i));
			i++;
		}
		return nfa;
	}
	
	private StateTable regex(){
		StateTable regex = new StateTable();
		StateTable temp;
		String token;
		populate(regex,"\\@");
		for(TokenC t:lexspec.getTokens()){
			this.regex = t.getLegal();
			index = 0;
			token = t.getTitle().substring(1);
			temp = rexp();
			branch(regex,temp);
			regex.getTable().getLast().accept(token);
		}
		return regex;
	}
	
	private StateTable regex_single(){
		StateTable regex_single = new StateTable();
		regex_single.getTable().add(new State());
		regex_single = rexp();
		regex_single.getTable().getLast().accept("accept");
		return regex_single;
	}
	
	private StateTable rexp(){
		int temp_i = index;
		StateTable rexp = new StateTable();
		rexp.getTable().add(new State());
		StateTable rexp1 = rexp1();
		if(rexp1==null){
			index = temp_i;
			return null;
		}
		
		temp_i = index;
		StateTable rexp_ = rexp_();
		if(rexp_==null){
			return rexp1;
		}		
		unionTables(rexp,rexp1,rexp_);
		return rexp;
	}
	
	private StateTable rexp_(){
		int temp_i = index;
		StateTable rexp_ = new StateTable();
		
		if(!match("|")){
			index = temp_i;
			return null;
		}
		
		StateTable rexp1 = rexp1();
		if(rexp1==null){
			index = temp_i;
			return null;
		}
		
		StateTable rexp_2 = rexp_();
		if(rexp_2==null){
			rexp_ = rexp1;
			return rexp_;
		}
		
		unionTables(rexp_,rexp1,rexp_2);
		return rexp_;
	}
	
	private StateTable rexp1(){
		StateTable rexp1 = new StateTable();
		int temp_i = index;
		
		StateTable rexp2 = rexp2();
		if(rexp2==null){
			index = temp_i;
			return null;
		}
		
		StateTable rexp1_ = rexp1_();
		if(rexp1_==null){
			rexp1 = rexp2;
			return rexp1;
		}
		
		rexp1 = concatTables(rexp2,rexp1_);
		return rexp1;
	}
	
	private StateTable rexp1_(){
		StateTable rexp1_ = new StateTable();
		int temp_i = index;
		
		StateTable rexp2 = rexp2();
		if(rexp2==null){
			index = temp_i;
			return null;
		}
		
		StateTable rexp1_2 = rexp1_();
		if(rexp1_2==null){
			rexp1_ = rexp2;
			return rexp1_;
		}
		
		rexp1_ = concatTables(rexp2,rexp1_2);
		return rexp1_;
	}
	
	private StateTable rexp2(){
		StateTable rexp2 = new StateTable();
		int temp_i = index;
		
		if(match("(")){
			StateTable rexp = rexp();
			if(rexp==null){
				index = temp_i;
				return null;
			}
			if(!match(")")){
				System.out.println("Expected \")\" not found.");
				index = temp_i;
				return null;
			}
			int rexp2_tail = rexp2_tail();
			if(rexp2_tail==STAR){
				rexp2 = new StateTable();
				rexp2.getTable().add(new State());
				concatTables(rexp2,rexp);
				concatStates(rexp2.getTable().getFirst(),rexp.getTable().getLast(),"\\@");
				concatStates(rexp2.getTable().getLast(),rexp.getTable().getFirst(),"\\@");
				rexp = new StateTable();
				rexp.getTable().add(new State());
				concatTables(rexp2,rexp);
				return rexp2;
			}
			else if(rexp2_tail==PLUS){
				rexp2 = rexp;
				rexp = new StateTable();
				rexp.getTable().add(new State());
				concatStates(rexp2.getTable().getLast(),rexp2.getTable().getFirst(),"\\@");
				concatTables(rexp2,rexp);
				return rexp2;
			}
			else{
				return rexp;
			}
		}
		
		if(isRE_CHAR(peekChar())){
			char c = getChar();
			int rexp2_tail = rexp2_tail();
			StateTable temp = new StateTable();
			populate(temp,String.valueOf(c));
			if(rexp2_tail==STAR){
				rexp2 = new StateTable();
				rexp2.getTable().add(new State());
				concatTables(rexp2,temp);
				concatStates(rexp2.getTable().getFirst(),temp.getTable().getLast(),"\\@");
				concatStates(temp.getTable().getLast(),temp.getTable().getFirst(),"\\@");
				temp = new StateTable();
				temp.getTable().add(new State());
				concatTables(rexp2,temp);
				return rexp2;
			}
			else if(rexp2_tail==PLUS){
				rexp2 = temp;
				concatStates(rexp2.getTable().getLast(),rexp2.getTable().getFirst(),"\\@");
				return rexp2;
			}
			else{
				rexp2 = temp;
				return rexp2;
			}
		}
		
		StateTable rexp3 = rexp3();
		if(rexp3==null){
			index = temp_i;
			return null;
		}
		rexp2 = rexp3;
		return rexp2;
	}
	
	private int rexp2_tail(){
		if(match("*")){
			return 1;
		}
		if(match("+")){
			return 2;
		}
		return 0;
	}
	
	private StateTable rexp3(){
		StateTable rexp3 = new StateTable();
		int temp_i = index;
		
		rexp3 = char_class();
		if(rexp3==null){
			index = temp_i;
			return null;
		}
		return rexp3;
	}
	
	private StateTable char_class(){
		StateTable char_class = new StateTable();
		int temp_i = index;
		
		if(match(".")){
			CharacterC period = new CharacterC("[.]", lexspec.getCharacters());
			char_class = new StateTable();
			char_class.getTable().add(new State());
			ArrayList<String> legals = period.getLegal();
			State rendev = new State();
			for(String s: legals){
				StateTable temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(char_class.getTable().getFirst(), rendev,s);
			}
			StateTable temp = new StateTable();
			char_class.getTable().add(rendev);
			temp.getTable().add(new State());
			concatTables(char_class,temp);
			return char_class;
		}
		
		if(match("[")){
			String cs = "[";
			String char_class1 = char_class1();
			CharacterC set = new CharacterC(cs+char_class1,lexspec.getCharacters());
			char_class.getTable().add(new State());
			ArrayList<String> legals = set.getLegal();
			State rendev = new State();
			for(String s: legals){
				StateTable temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(char_class.getTable().getFirst(), rendev,s);
			}
			StateTable temp = new StateTable();
			char_class.getTable().add(rendev);
			temp.getTable().add(new State());
			concatTables(char_class,temp);
			return char_class;
		}
		else{
			String defined_class = defined_class();
			if(defined_class==null){
				index = temp_i;
				return null;
			}
			char_class.getTable().add(new State());
			ArrayList<String> legals = lexspec.getCharacters().get(defined_class).getLegal();
			State rendev = new State();
			for(String s: legals){
				StateTable temp = new StateTable();
				temp.getTable().add(new State());
				concatStates(char_class.getTable().getFirst(), rendev,s);
			}
			StateTable temp = new StateTable();
			char_class.getTable().add(rendev);
			temp.getTable().add(new State());
			concatTables(char_class,temp);
			return char_class;
		}
	}
	
	private String char_class1(){
		int temp_i = index;
		String char_set_list = char_set_list();
		if(char_set_list!=null){
			return char_set_list;
		}
		String exclude_set = exclude_set();
		if(exclude_set!=null){
			return exclude_set;
		}
		index = temp_i;
		return null;
	}
	
	private String char_set_list(){
		int temp_i = index;
		String char_set = char_set();
		if(char_set!=null){
			String char_set_tail = char_set_tail();
			if(char_set_tail==null){
				return char_set;
			}
			return char_set+char_set_tail;
		}
		if(!match("[")){
			index = temp_i;
			return null;
		}
		return "]";		
	}
	
	private String char_set(){
		int temp_i = index;
		String char_set = "";
		if(isCLS_CHAR(peekChar())){
			char_set+=getChar();
			String char_set_tail = char_set_tail();
			if(char_set_tail==null){
				return char_set;
			}
			return char_set+char_set_tail;
		}
		index = temp_i;
		return null;
	}
	
	private String char_set_tail(){
		String char_set_tail = "";
		int temp_i = index;
		if(match("-")){
			char_set_tail+="-";
			if(!isCLS_CHAR(peekChar())){
				index = temp_i;
				return null;
			}
			char_set_tail+=getChar();
			return char_set_tail;
		}
		return null;
	}
	
	private String exclude_set(){
		String exclude_set = "";
		int temp_i = index;
		if(!match("^")){
			index = temp_i;
			return null;
		}
		exclude_set+="^";
		String char_set = char_set();
		if(char_set==null){
			index = temp_i;
			return null;
		}
		exclude_set+=char_set;
		if(!match("]")){
			index = temp_i;
			return null;
		}
		exclude_set+="]";
		if(!match("IN")){
			index = temp_i;
			return null;
		}
		exclude_set+="IN";
		String exclude_set_tail = exclude_set_tail();
		if(exclude_set_tail==null){
			index = temp_i;
			return null;
		}
		exclude_set+=exclude_set_tail;
		return exclude_set;
	}
	
	private String exclude_set_tail(){
		String exclude_set_tail = "";
		int temp_i = index;
		if(match("[")){
			exclude_set_tail+="[";
			String char_set = char_set();
			if(char_set==null){
				index = temp_i;
				return null;
			}
			exclude_set_tail+=char_set;
			if(match("]")){
				return exclude_set_tail+"]";
			}
			index = temp_i;
			return null;
		}
		String defined_class = defined_class();
		if(defined_class==null){
			index = temp_i;
			return null;
		}
		return defined_class;
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
			match("$");
			while(isUpper(peekChar())){
				token+=peekChar();
				match(Character.toString(peekChar()));
			}//end while
			if(DEBUG)System.out.println(token);
			return token;
		}//end if
		return null;
	}//end defined_class method
	
	private char peekChar(){
		if(index>=regex.length())
			return '\0';
//		if(DEBUG)System.out.println(expression.charAt(exp_ind));
		return regex.charAt(index);
	}
		
	private char getChar(){
		if(regex.length()<=index){
			return '\0';
		}
		char result = regex.charAt(index);
		index++;
		return result;
	}
	
	private boolean match(String s){
		int temp_ind = index;
		for(int i=0;i<s.length();i++){
			if(peekChar()==s.charAt(i)){
				getChar();
			}
			else if(Character.isWhitespace(peekChar())){
				i--;
				getChar();
			}
			else{
				index=temp_ind;
				return false;
			}
		}
		if(DEBUG)System.out.println("Matched: "+s);
		return true;
	}
	
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
				match("\\");
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
				match("\\");
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
	
	private StateTable concatTables(StateTable s1,StateTable s2){
		if(s1==null || s2==null){
			return null;
		}
		State state1 = s1.getTable().getLast();
		State state2 = s2.getTable().getFirst();
		concatStates(state1,state2,"\\@");
		for(State s: s2.getTable()){
			s1.getTable().add(s);
		}
		return s1;
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
}
