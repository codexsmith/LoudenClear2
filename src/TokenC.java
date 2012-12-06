import java.util.ArrayList;


public class TokenC {
	private String legal; //array list of length 1, containing the regex as a string. this must be of compatible type with the regex parser
	//the regex parser that we build must provide some way to operate on strings as both inputs: (regexp, str)
	private String title;
	
	public TokenC(String line){
		String symbols;
		legal = "";
		
		line = line.trim();//removes leading & trailing whitespaces
		
		if(line.substring(0, 1).compareTo("$") == 0){ //proper identifier
			this.title = line.substring(0, line.indexOf(" "));
			
			symbols = line.substring(line.indexOf(" ")+1);
			
			symbols = symbols.replace(" ", "");
			
			this.legal = (symbols);
		}
		
		if(line.startsWith("<")){
			this.title = line.substring(0,line.indexOf(">")+1);
			System.out.println(title);
			if (line.indexOf("-->") != -1)
			{//normal rule of miniRE grammar
				symbols = line.replace(" ", "").substring(line.replace(" ", "").indexOf("-->")+3);
			
				System.out.println(symbols);
				this.legal = (symbols);
			}
			else
			{//start symbol of miniRE grammar
				symbols = "$";
				System.out.println(symbols);
				this.legal = (symbols);
			}
		}
	}
	
	public String getLegal() {
		return legal;
	}

	public boolean isLegal(String c) {
		String regexp = legal; 
		return false;
	}

	public String getTitle() {
		return title;	
	}


}