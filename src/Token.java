import java.util.ArrayList;


public class Token extends iIdentifier{
	private ArrayList<String> legal; //array list of length 1, containing the regex as a string. this must be of compatible type with the regex parser
	//the regex parser that we build must provide some way to operate on strings as both inputs: (regexp, str)
	private String title;
	
	public Token(String line){
		String title, symbols;
		
		line = line.trim();//removes leading & trailing whitespaces
		
		if(line.substring(0, 1).compareTo("$") == 0){ //proper identifier
			title = line.substring(1, line.indexOf(" "));
			
			symbols = line.substring(line.lastIndexOf(" ")+1);
			
			symbols.trim();
			
			this.legal.add(symbols);
			this.title = title;
		}
	}
	
	@Override
	public ArrayList<String> getLegal() {
		return legal;
	}

	@Override
	public boolean match(String title) {
		if (title.compareTo(this.title) == 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean islegal(String c) {
		String regexp = legal.get(0); 
		
		
		
		return false;
	}
	
}
