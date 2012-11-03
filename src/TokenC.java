import java.util.ArrayList;


public class TokenC extends iIdentifier{
	private ArrayList<String> legal; //array list of length 1, containing the regex as a string. this must be of compatible type with the regex parser
	//the regex parser that we build must provide some way to operate on strings as both inputs: (regexp, str)
	private String title;
	
	public TokenC(String line){
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
	public boolean isLegal(String c) {
		String regexp = legal.get(0); 
		
		return false;
	}

	@Override
	public String getTitle() {
		return title;	
	}
	
}
