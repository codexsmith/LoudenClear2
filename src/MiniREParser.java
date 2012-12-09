
public class MiniREParser {
	private final boolean DEBUG = true;
	private String expression;
	private int exp_ind;
	private boolean epsilon;
	private int line;
	
	public MiniREParser(){
		line = 1;
		expression = "";
		exp_ind = 0;
	}
	
	public void parse(String exp){
		expression = exp;
		exp_ind = 0;
		miniREprogram();
	}
	
	private void miniREprogram(){
		if(!match("begin")){
			System.out.println("Error(Line"+line+"): Expected token \"begin\" not found.");
			return;
		}
		if(!statement_list()){
			System.out.println("Error(Line"+line+"): Invalid \"statement-list\" found.");
			return;
		}
		if(!match("end")){
			System.out.println("Error(Line"+line+"): Expected token \"end\" not found.");
			return;
		}
	}
	
	private boolean statement_list(){
		if(DEBUG)System.out.println("statement_list()");
		int temp_ind = exp_ind;
		if(!statement()){
			System.out.println("Error(Line"+line+"): Invalid \"statement\" found.");
			exp_ind = temp_ind;
			return false;
		}
		if(!epsilon){
			statement_list_tail();
		}
		epsilon = false;
		return true;
	}
	
	private boolean statement_list_tail(){
		if(DEBUG)System.out.println("statment_list_tail");
		int temp_ind = exp_ind;
		if(!statement()){
			epsilon = true;
			exp_ind = temp_ind;
			return true;
		}
		else{
			return statement_list_tail();
		}
	}
	
	private boolean statement(){
		if(DEBUG)System.out.println("statement()");
		int temp_ind = exp_ind;
		int temp_line = line;

		if(match("replace")){
			String regex = "";
			boolean escaped = false;
			if(!match("\'")){
				exp_ind = temp_ind;
				return false;
			}
			while(true){
				if(peekChar()=='\\'){
					escaped = true;
				}
				if(match("\'")&&!escaped){
					System.out.println("REGEX:"+regex);
					break;
				}
				else{
					escaped = false;
					regex+=getChar();
				}
			}
			if(!match("with")){
				System.out.println("Error(Line"+line+"): Expected \"with\" token not found.");
				exp_ind = temp_ind;
				line = temp_line;
			}
			String replacement = "";
			replacement = source_file();
			if(replacement == null){
				System.out.println("Error(Line"+line+"): Expected \"replacement\" token not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match("in")){
				System.out.println("Error(Line"+line+"): Expected \"in\" token not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			String file = "";
			file = file_names();
			if(file == null){
				System.out.println("Error(Line"+line+"): Invalid \"file-names\" found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected \";\" token not found");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			return true;
		}
		else if(match("recursivereplace")){
			String regex = "";
			boolean escaped = false;
			if(!match("\'")){
				exp_ind = temp_ind;
				return false;
			}
			while(true){
				if(peekChar()=='\\'){
					escaped = true;
				}
				if(match("\'")&&!escaped){
					System.out.println("REGEX:"+regex);
					break;
				}
				else{
					escaped = false;
					regex+=getChar();
				}
			}
			if(!match("with")){
				System.out.println("Error(Line"+line+"): Expected \"with\" token not found.");
				exp_ind = temp_ind;
				line = temp_line;
			}
			String replacement = "";
			replacement = source_file();
			if(replacement == null){
				System.out.println("Error(Line"+line+"): Expected \"replacement\" token not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match("in")){
				System.out.println("Error(Line"+line+"): Expected \"in\" token not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			String file = "";
			file = file_names();
			if(file == null){
				System.out.println("Error(Line"+line+"): Invalid \"file-names\" found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected \";\" token not found");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			return true;
		}
		else if(match("print")){
			if(!match("(")){
				System.out.println("Error(Line"+line+"): Expected \"(\" token not found");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!exp_list()){
				System.out.println("Error(Line"+line+"): Invalid \"exp-list\" found");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match(")")){
				System.out.println("Error(Line"+line+"): Expected \")\" token not found");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected \";\" token not found");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			return true;
		}
		else if(id()!=null){
			if(!match("=")){
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(match("#")){
				if(!exp()){
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				else{
					if(!match(";")){
						System.out.println("Error(Line"+line+"): Expected \";\" token not found");
						exp_ind = temp_ind;
						line = temp_line;
						return false;
					}
				}
				return true;
			}
			else if(match("maxfreqstring")){
				if(!match("(")){
					System.out.println("Error(Line"+line+"): Expected \")\" token not found");
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				if(id()==null){
					System.out.println("Error(Line"+line+"): Expected \"ID\" token not found");
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				if(!match(")")){
					System.out.println("Error(Line"+line+"): Expected \")\" token not found");
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				if(!match(";")){
					System.out.println("Error(Line"+line+"): Expected \";\" token not found");
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				return true;
			}
			else if(exp()){
				if(!match(";")){
					System.out.println("Error(Line"+line+"): Expected \";\" token not found");
					exp_ind = temp_ind;
					return false;
				}
				return true;
			}
		}
		exp_ind = temp_ind;
		line = temp_line;
		return false;
	}
	
	private String file_names(){
		if(DEBUG)System.out.println("file_names()");
		String src_file = source_file();
		if(match(">!")){
			String dst_file = destination_file();
			if(src_file!=null&&dst_file!=null){
				return src_file+">!"+dst_file;
			}
			else{
				System.out.println("Error(Line"+line+"): Expected a \"destination file\"");
			}
		}
		return null;
	}
	
	private String source_file(){
		boolean escaped = false;
		String filename = null;
		if(match("\"")){
			filename="";
		}
		
		while((peekChar()>=32&&peekChar()<=126)){
			if(peekChar()=='\\'){
				escaped = true;
			}
			if(peekChar()=='\"'){
				if(!escaped){
					match("\"");
					break;
				}
			}
			if(peekChar()!='\n'){
				filename+=getChar();
				escaped = false;
			}
		}
		return filename;
	}
	
	private String destination_file(){
		boolean escaped = false;
		String filename = null;
		if(match("\"")){
			filename="";
		}
		while((peekChar()>=32&&peekChar()<=126)){
			if(peekChar()=='\\'){
				escaped = true;
			}
			if(peekChar()=='\"'){
				if(!escaped){
					match("\"");
					break;
				}
			}
			if(peekChar()!='\n'){
				filename+=getChar();
			}
		}
		return filename;
	}
	
	private boolean exp_list(){
		int temp_ind = exp_ind;
		int temp_line = line;
		if(!exp()){
			System.out.println("Error(Line"+line+"): Invalid \"exp\" found.");
			exp_ind = temp_ind;
			line = temp_line;
			return false;
		}
		if(!epsilon){
			exp_list_tail();
		}
		epsilon = false;
		return true;
	}
	
	private boolean exp_list_tail(){
		if(DEBUG)System.out.println("exp_list_tail");
		int temp_ind = exp_ind;
		int temp_line = line;
		if(!exp()){
			epsilon = true;
			exp_ind = temp_ind;
			line = temp_line;
			return true;
		}
		else{
			return exp_list_tail();
		}
	}
	
	private boolean exp(){
		int temp_ind = exp_ind;
		int temp_line = line;
		if(term()){
			exp_tail();
			return true;
		}
		else if(match("(")){
			if(!exp()){
				System.out.println("Error(Line"+line+"): Invalid \"exp\" found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!match(")")){
				System.out.println("Error(Line"+line+"): Expected \")\" token not found");
				exp_ind = temp_ind;
				return false;
			}
			return true;
		}
		if(id()!=null){
			return true;
		}
		return false;
	}
	
	private boolean exp_tail(){
		int temp_ind = exp_ind;
		int temp_line = line;
		if(bin_op()){
			if(!term()){
				System.out.println("Error(Line"+line+"): Invalid \"term\" found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			if(!exp_tail()){
				System.out.println("Error(Line"+line+"): Invalid \"exp-tail\" found.");
				exp_ind = temp_ind;
				line = temp_line;
				return false;
			}
			epsilon = false;
			return true;
		}
		else{
			epsilon = true;
		}
		return true;
	}
	
	private boolean term(){
		int temp_ind = exp_ind;
		int temp_line = line;
		if(match("find"))
		{
			String regex = "";
			boolean escaped = false;
			if(!match("\'")){
				exp_ind = temp_ind;
				return false;
			}
			while(true){
				if(peekChar()=='\\'){
					escaped = true;
				}
				if(peekChar()=='\''&&!escaped){
					break;
				}
				else{
					escaped = false;
					regex+=getChar();
				}
			}
			if(DEBUG)System.out.println("REGEX: "+regex);
				if(!match("\'")){
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				if(!match("in")){
					System.out.println("Error(Line"+line+"): Expected \"in\" token not found.");
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				String file = file_name();
				if(file==null){
					System.out.println("Error(Line"+line+"): Invalid \"file-name\" found.");
					exp_ind = temp_ind;
					line = temp_line;
					return false;
				}
				NFAGenerator n = new NFAGenerator(new Lexical());
				n.generateNFA(regex);
				return true;
			}
		return false;
	}

	private String file_name(){
		boolean escaped = false;
		String filename = null;
		if(match("\"")){
			filename="";
		}
		
		while((peekChar()>=32&&peekChar()<=126)){
			if(peekChar()=='\\'){
				escaped = true;
			}
			if(peekChar()=='\"'){
				if(!escaped){
					if(DEBUG)System.out.println("File: "+filename);
					match("\"");
					break;
				}
			}
			if(peekChar()!='\n'){
				filename+=getChar();
				escaped = false;
			}
		}
		return filename;
	}
	
	private boolean bin_op(){
		if(match("diff")||match("union")||match("inters")){
			return true;
		}
		return false;
	}
	
	private char peekChar(){
		if(exp_ind>=expression.length())
			return '\0';
//		if(DEBUG)System.out.println(expression.charAt(exp_ind));
		return expression.charAt(exp_ind);
	}
	
	private String id(){
		if(DEBUG)System.out.println("id()");
		String id = "";
		while(true){
			if(Character.isLetter(peekChar())){
				id+=getChar();
				break;
			}
			else if(Character.isWhitespace(peekChar())){
//				if(Character.getType(peekChar())!=Character.LINE_SEPARATOR){
//					line++;
//				}
//				if(DEBUG)System.out.println("WHITESPACE");
				getChar();
			}
			else{
				return null;
			}

		}
		for(int i=0;i<9;i++){
			if(Character.isLetterOrDigit(peekChar())||peekChar()=='_'){
				id+=getChar();
			}
			else{
				break;
			}
		}
		if(DEBUG)System.out.println("ID = "+id);
		return id;
	}
	
	private char getChar(){
		if(expression.length()<=exp_ind){
			return '\0';
		}
		char result = expression.charAt(exp_ind);
		exp_ind++;
		return result;
	}
	
	private boolean match(String s){
		int temp_ind = exp_ind;
		for(int i=0;i<s.length();i++){
			if(peekChar()==s.charAt(i)){
				getChar();
			}
			else if(Character.isWhitespace(peekChar())){
				i--;
				getChar();
			}
			else{
				exp_ind=temp_ind;
				return false;
			}
		}
		if(Character.getType(peekChar())!=Character.SPACE_SEPARATOR){
			line++;
		}
		if(DEBUG)System.out.println("Matched: "+s);
		return true;
	}
}

