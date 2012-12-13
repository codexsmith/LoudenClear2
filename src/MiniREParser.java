
/**
 * A parser that recognizes the MiniRE scripting language generates an abstract syntax tree.
 * @author andrew
 *
 */
public class MiniREParser {
	private final boolean DEBUG = true;
	private String expression;
	private int exp_ind;
	private int line;
	private TreeNode root;

	/**
	 * Constructor for a MiniREParser
	 */
	public MiniREParser(){
		line = 1;
		expression = "";
		exp_ind = 0;
		root = null;
	}//end MiniREParser constructor
	
	/**
	 * Parses the script
	 * @param exp The content of a script
	 * @return root of an abstract syntax tree
	 */
	public TreeNode parse(String exp){
		expression = exp;
		exp_ind = 0;
		root = miniREprogram();
		return root;
	}//end parse
	
	/**
	 * Parses the beginning of the file and the end of the file
	 * @return root of an abstract syntax tree
	 */
	private TreeNode miniREprogram(){
		TreeNode root = new TreeNode("MiniRE-program");
		if(DEBUG)System.out.println("MiniRE-program");
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
	
	/**
	 * Parses a list of statements
	 * @return subtree of all statements
	 */
	private TreeNode statement_list(){
		if(DEBUG)System.out.println("statement-list");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode statement_list = new TreeNode("statement-list");
		TreeNode statement = statement();
		if(statement==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}//end if
		statement_list.addChild(statement);
		
		TreeNode statement_list_tail =  statement_list_tail();
		if(statement_list_tail!=null){
			statement_list.addChild(statement_list_tail);
		}//end if
		return statement_list;
	}//end statement_list
	
	/**
	 * Parses any additional statements
	 * @return subtree of additional statements
	 */
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
	
	/**
	 * Parses a single statement
	 * @return subtree of a statement
	 */
	private TreeNode statement(){
//		if(DEBUG)System.out.println("statement");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode statement = new TreeNode("statement");
				
		if(match("replace")){
			if(DEBUG)System.out.println("replace");
			statement.setName("replace");
			String regex = regex();
			if(regex==null){//check if valid regex found
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addArg(regex);
			
			if(!match("with")){
				System.out.println("Error(Line"+line+"): Expected token \"with\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			
			String ascii = ascii_str();
			if(ascii==null){//check if valid ascii string
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addArg(ascii);
			
			if(!match("in")){
				System.out.println("Error(Line"+line+"): Expected token \"in\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if

			TreeNode file_names = file_names();
			if(file_names==null){//check if valid file names
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addChild(file_names);
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			return statement;
		}//end if
		else if(match("recursivereplace")){
			if(DEBUG)System.out.println("recursivereplace");
			statement.setName("recursivereplace");
			String regex = regex();
			if(regex==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addArg(regex);
			if(!match("with")){
				System.out.println("Error(Line"+line+"): Expected token \"with\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			String ascii = ascii_str();
			if(ascii==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addArg(ascii);
			if(!match("in")){
				System.out.println("Error(Line"+line+"): Expected token \"in\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			TreeNode file_names = file_names();
			if(file_names==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addChild(file_names);
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			return statement;
		}//end if
		else if(match("print")){
			if(DEBUG)System.out.println("print");
			statement.setName("print");
			if(!match("(")){
				System.out.println("Error(Line"+line+"): Expected token \")\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			TreeNode exp_list = exp_list();
			if(exp_list==null){
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			statement.addChild(exp_list);
			if(!match(")")){
				System.out.println("Error(Line"+line+"): Expected token \")\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			if(!match(";")){
				System.out.println("Error(Line"+line+"): Expected token \";\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}//end if
			return statement;
		}//end else if
		String id = id();
		if(id!=null){
			System.out.println("statement");
			statement.addArg(id);
			if(!match("=")){
				System.out.println("Error(Line"+line+"): Expected token \"=\" not found.");
				exp_ind = temp_ind;
				line = temp_line;
				return null;
			}
			if(match("#")){
				statement.addArg("#");
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
				id = id();
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
		exp_ind = temp_ind;
		line = temp_line;
		return null;
	}//end statement
	
	/**
	 * Parses two file names
	 * @return Subtree of two file names
	 */
	private TreeNode file_names(){
		if(DEBUG)System.out.println("file-names");
		int temp_ind = exp_ind;
		int temp_line = line;
		TreeNode file_names = new TreeNode("file-names");
		
		TreeNode src_file = source_file();
		if(src_file==null){//check valid source file name
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
		if(dst_file==null){//check valid destination file name
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		file_names.addChild(dst_file);
		return file_names;
	}//end file_names
	
	/**
	 * Parses a source file
	 * @return Node containing the file name
	 */
	private TreeNode source_file(){
		if(DEBUG)System.out.println("source-file");
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
	}//end source_file
	
	/**
	 * Parses a destination file
	 * @return Node containing the file name
	 */
	private TreeNode destination_file(){
		if(DEBUG)System.out.println("destination-file");
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
	}//end destination_file
	
	/**
	 * Parses a list of expressions
	 * @return Subtree of expressions
	 */
	private TreeNode exp_list(){
		if(DEBUG)System.out.println("exp-list");
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
	}//end exp_list
	
	/**
	 * Parses and additional expressions
	 * @return Subtree of additional expressions
	 */
	private TreeNode exp_list_tail(){
		if(DEBUG)System.out.println("exp-list-tail");
		TreeNode exp_list_tail = new TreeNode("exp-list-tail");		
		int temp_ind = exp_ind;
		int temp_line = line;
		
		if(!match(",")){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
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
	}//end exp_list_tail
	
	/**
	 * Parses a single expression
	 * @return Subtree of an expression
	 */
	private TreeNode exp(){
		if(DEBUG)System.out.println("exp");
		TreeNode exp = new TreeNode("exp");
		int temp_ind = exp_ind;
		int temp_line = line;
		
		TreeNode term = term();
		if(term!=null){
			exp.addChild(term);
			TreeNode exp_tail = exp_tail();
			if(exp_tail!=null){
				for(int i=0;i<exp_tail.getSize();i++){
					exp_tail.addChild(exp_tail.getChild(i));
				}
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
	}//end exp
	
	/**
	 * Parses and additional expressions
	 * @return Subtree of additional expressions
	 */
	private TreeNode exp_tail(){
		TreeNode exp_tail = new TreeNode("exp-tail");
		int temp_ind = exp_ind;
		int temp_line = line;

		if(DEBUG)System.out.println("exp-tail");
		
		TreeNode bin_op = bin_op();
		if(bin_op==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
//		exp_tail.addChild(bin_op);
		
		TreeNode term = term();
		if(term==null){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		exp_tail.addChild(bin_op);
		exp_tail.addChild(term);
		
		TreeNode exp_tail2 = exp_tail();
		if(exp_tail2==null){
			return exp_tail;
		}
		for(int i=0;i<exp_tail2.getSize();i++){
			exp_tail.addChild(exp_tail2.getChild(i));
		}
		return exp_tail;

	}//end exp_tail
	
	/**
	 * Parses a single term
	 * @return Subtree of a single term
	 */
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
		term.setName("find");
		if(DEBUG)System.out.println("find");
		
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
	}//end term

	/**
	 * Parses a file name
	 * @return Subtree of a parsed file name
	 */
	private TreeNode file_name(){
		if(DEBUG)System.out.println("file-name");
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
	}//end file_name
	
	/**
	 * Parses binary operation
	 * @return Leaf node of binary operation
	 */
	private TreeNode bin_op(){
		if(match("diff")){
			if(DEBUG)System.out.println("diff");
			TreeNode result = new TreeNode("diff");
			return result;
		}
		if(match("union")){
			if(DEBUG)System.out.println("union");
			TreeNode result = new TreeNode("union");
			return result;
		}
		if(match("inters")){
			if(DEBUG)System.out.println("inters");
			TreeNode result = new TreeNode("inters");
			return result;
		}
		return null;
	}//end bin_op
	
	/**
	 * Peeks at a char ahead
	 * @return Next character in the expression
	 */
	private char peekChar(){
		if(exp_ind>=expression.length())
			return '\0';
//		if(DEBUG)System.out.println(expression.charAt(exp_ind));
		return expression.charAt(exp_ind);
	}
	
	/**
	 * Parses ID
	 * @return A string of ID if valid ID is found.
	 */
	private String id(){
		//if(DEBUG)System.out.println("id()");
		String id = "";
		int temp_ind = exp_ind;
		int temp_line = line;
		while(true){
			if(Character.isLetter(peekChar())){
				id+=getChar();
				break;
			}
			else if(Character.isWhitespace(peekChar())){
				getChar();
			}
			else{
				System.out.println("Error(Line"+line+"): Unexpected token \""+peekChar()+"\" found.");
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
		if(id.compareTo("begin")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		if(id.compareTo("end")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		if(id.compareTo("find")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		if(id.compareTo("replace")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		if(id.compareTo("find")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		if(id.compareTo("with")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		if(id.compareTo("in")==0){
			exp_ind = temp_ind;
			line = temp_line;
			return null;
		}
		//if(DEBUG)System.out.println("ID = "+id);
		return id;
	}//end id
	
	/**
	 * Parses an ASCII string inside double quotes
	 * @return An ASCII string if valid
	 */
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
	}//end ascii_str

	/**
	 * Parses a regular expression inside single quotes
	 * @return Unsanitized regular expression if in side single quotes
	 */
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
//				if(DEBUG)System.out.println("REGEX:"+regex);
				break;
			}
			else{
				escaped = false;
				regex+=getChar();
			}
		}
		return regex;
	}//end regex
	
	/**
	 * Consumes a character from the expression
	 * @return Consumed character
	 */
	private char getChar(){
		if(expression.length()<=exp_ind){
			return '\0';
		}
		char result = expression.charAt(exp_ind);
		exp_ind++;
		return result;
	}//end getChar
	
	/**
	 * Matches a string to current stream of expression
	 * @param s String to match
	 * @return True if matched. False otherwise
	 */
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
		if(peekChar()=='\n'){
			line++;
		}
		//if(DEBUG)System.out.println("Matched: "+s);
		return true;
	}//end match
}//end MiniREParser

