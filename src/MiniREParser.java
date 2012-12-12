
public class MiniREParser {
	private final boolean DEBUG = true;
	private String expression;
	private int exp_ind;
	private boolean epsilon;
	private int line;
	private TreeNode root;

	public MiniREParser(){
		line = 1;
		expression = "";
		exp_ind = 0;
		root = null;
	}//end MiniREParser constructor
	
	public void parse(String exp){
		expression = exp;
		exp_ind = 0;
		root = miniREprogram();
	}//end parse
	
	private TreeNode miniREprogram(){
		TreeNode root = new TreeNode("MiniRE-program");
		
		if(!match("begin")){//failed to match begin
			System.out.println("Error(Line"+line+"): Expected token \"begin\" not found.");
			return null;
		}//end if
		
		TreeNode statement_list = statement_list();
		if(statement_list==null){//invalid statement-list
			return null;
		}//end if
		root.addChild(statement_list);

		if(!match("end")){//failed to match end
			System.out.println("Error(Line"+line+"): Expected token \"end\" not found.");
			return null;
		}//end if
		return root;
	}//end miniREprogram
	
	private TreeNode statement_list(){
		if(DEBUG)System.out.println("statement_list()");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode statement_list = new TreeNode("statement-list");
		TreeNode statement = statement();
		if(statement==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		statement_list.addChild(statement);
		
		TreeNode statement_list_tail =  statement_list_tail();
		if(statement_list_tail!=null){
			statement_list.addChild(statement_list_tail);
		}
		return statement_list;
	}
	
	private TreeNode statement_list_tail(){
		if(DEBUG)System.out.println("statment_list_tail");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode statement_list_tail = new TreeNode("statement-list-tail");
		
		TreeNode statement = statement();
		if(statement == null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		statement_list_tail.addChild(statement);
		
		TreeNode statement_list_tail2 = statement_list_tail();
		if(statement_list_tail2==null){
			return statement_list_tail;
		}
		statement_list_tail.addChild(statement_list_tail2);
		return statement_list_tail;
	}
	
	private TreeNode statement(){
		if(DEBUG)System.out.println("statement()");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode statement = new TreeNode("statement");
				
		if(match("replace")){
			statement.setName("replace");
			String regex = regex();
			if(regex==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addArg(regex);
			if(!match("with")){
				System.out.println("Error(Line"+line+"): Expected token \"with\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			
			String ascii = ascii_str();
			if(ascii==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addArg(ascii);
			
			if(!match("in")){
				System.out.println("Error(Line"+line+"): Expected token \"in\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}

			TreeNode file_names = file_names();
			if(file_names==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addChild(file_names);
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			return statement;
		}
		else if(match("recursivereplace")){
			statement.setName("recursivereplace");
			String regex = regex();
			if(regex==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addArg(regex);
			if(!match("with")){
				System.out.println("Error(Line"+line+"): Expected token \"with\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			String ascii = ascii_str();
			if(ascii==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addArg(ascii);
			if(!match("in")){
				System.out.println("Error(Line"+line+"): Expected token \"in\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			TreeNode file_names = file_names();
			if(file_names==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addChild(file_names);
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			return statement;
		}
		else if(match("print")){
			statement.setName("print");
			if(!match("(")){
				System.out.println("Error(Line"+line+"): Expected token \")\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			TreeNode exp_list = exp_list();
			if(exp_list==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			statement.addChild(exp_list);
			if(!match(")")){
				System.out.println("Error(Line"+line+"): Expected token \")\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			return statement;
		}
		else if(id()!=null){
			if(!match("=")){
//				System.out.println("Error(Line"+line+"): Expected token \"=\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			if(match("#")){
				TreeNode exp = exp();
				if(exp==null){
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				statement.addChild(exp);
				if(!match(";")){
					System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				return statement;
			}
			else if(match("maxfreqstring")){
				if(!match("(")){
					System.out.println("Error(Line"+line+"): Expected token \"(\" not found.");
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				String id = id();
				if(id==null){
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				statement.addArg(id);
				if(!match(")")){
					System.out.println("Error(Line"+line+"): Expected token \")\" not found.");
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				if(!match(";")){
					System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}

			}
			else{
				TreeNode exp = exp();
				if(exp==null){
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				statement.addChild(exp);
				if(!match(";")){
					System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
					exp_ind = temp_ind;
					line = temp_line;
					return null;
				}
				return statement;
			}
		}
		System.out.println("Error(Line"+line+"): Invalid \"statement\" found.");
		exp_ind = temp_ind;
		line = temp_line;
		return null;
	}
	
	private TreeNode file_names(){
		if(DEBUG)System.out.println("file_names()");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode file_names = new TreeNode("file-names");
		
		TreeNode src_file = source_file();
		if(src_file==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		file_names.addChild(src_file);
		
		if(!match(">!")){
			System.out.println("Error(Line"+line+"): Expected token \">!\" not found.");
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		
		TreeNode dst_file = destination_file();
		if(dst_file==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		file_names.addChild(dst_file);
		return file_names;
	}
	
	private TreeNode source_file(){
		TreeNode source_file = new TreeNode("source-file");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		String filename = ascii_str();
		if(filename==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		source_file.addArg(filename);
		return source_file;
	}
	
	private TreeNode destination_file(){
		TreeNode destination_file = new TreeNode("destination-file");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		String filename = ascii_str();
		if(filename==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		destination_file.addArg(filename);
		return destination_file;
	}
	
	private TreeNode exp_list(){
		if(DEBUG)System.out.println("exp_list()");
		TreeNode exp_list = new TreeNode("exp-list");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		TreeNode exp = exp();
		if(exp==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		exp_list.addChild(exp);
		
		TreeNode exp_list_tail = exp_list_tail();
		if(exp_list_tail!=null){
			exp_list.addChild(exp_list_tail);
		}
		return exp_list;
	}
	
	private TreeNode exp_list_tail(){
		if(DEBUG)System.out.println("exp_list_tail()");
		TreeNode exp_list_tail = new TreeNode("exp-list-tail");		
		int temp_ind = exp_ind;
		int temp_line = line;
		
		TreeNode exp = exp();
		if(exp==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		exp_list_tail.addChild(exp);
		
		TreeNode exp_list_tail2 = exp_list_tail();
		if(exp_list_tail2==null){
			return exp_list_tail;
		}
		exp_list_tail.addChild(exp_list_tail2);
		return exp_list_tail;
	}
	
	private TreeNode exp(){
		TreeNode exp = new TreeNode("exp");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		TreeNode term = term();
		if(term!=null){
			exp.addChild(term);
			TreeNode exp_tail = exp_tail();
			if(exp_tail!=null){
				exp.addChild(exp_tail);
			}
			return exp;
		}
		
		else if(match("(")){
			TreeNode exp2 = exp();
			if(exp2==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			if(!match(")")){
				System.out.println("Error(Line"+line+"): Expected \")\" token not found");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			exp.addChild(exp2);
			return exp;
		}
		
		String id = id();
		if(id==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		exp.addArg(id);
		return exp;
	}
	
	private TreeNode exp_tail(){
		TreeNode exp_tail = new TreeNode("exp-tail");
		int temp_ind = exp_ind;
		int temp_line = line;

		TreeNode bin_op = bin_op();
		if(bin_op==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		else{
			TreeNode term = term();
			if(term==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			exp_tail.addChild(term);
			
			TreeNode exp_tail2 = exp_tail();
			if(exp_tail2==null){
				return exp_tail;
			}
			exp_tail.addChild(exp_tail2);
			return exp_tail;
		}
	}
	
	private TreeNode term(){
		TreeNode term = new TreeNode("term");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		if(!match("find")){
//			System.out.println("Error(Line"+line+"): Expected \"find\" token not found");
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		
		String regex = regex();
		if(regex==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		term.addArg(regex);

		if(!match("in")){
			System.out.println("Error(Line"+line+"): Expected \"in\" token not found.");
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		TreeNode file_name = file_name();
		if(file_name==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		term.addChild(file_name);
		return term;
	}

	private TreeNode file_name(){
		TreeNode file_name = new TreeNode("file-name");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		String filename = ascii_str();
		if(filename==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		file_name.addArg(filename);
		return file_name;
	}
	
	private TreeNode bin_op(){
		if(match("diff")){
			TreeNode result = new TreeNode("diff");
			return result;
		}
		if(match("union")){
			TreeNode result = new TreeNode("union");
			return result;
		}
		if(match("inters")){
			TreeNode result = new TreeNode("inters");
			return result;
		}
		return null;
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
	
	private String ascii_str(){
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
	
	private String regex(){
		int temp_ind = exp_ind;
		int temp_line = line;
		String regex = "";
		boolean escaped = false;
		if(!match("\'")){
			exp_ind = temp_ind;
			line = temp_line;
			System.out.println("Error(Line"+line+"): Invalid regular expression found.");
			return null;
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
		return regex;
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

