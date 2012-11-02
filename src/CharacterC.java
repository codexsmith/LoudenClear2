import java.util.ArrayList;
/** Character classes require input that is fully enumerated. [a-z] will not work. you must use [a,b,...,w,x,y,z]
 * 
 * @author nick
 *	
 *	
 */

public class CharacterC extends iIdentifier{

	private ArrayList<String> legal;
	private String title;
	
	public CharacterC(String line){
		String title, symbols;
		
		line = line.trim();//removes leading & trailing whitespaces
		
		if(line.substring(0, 1).compareTo("$") == 0){ //proper identifier
			title = line.substring(1, line.indexOf(" "));
			
			symbols = line.substring(line.lastIndexOf(" ")+1);
			
			symbols.replaceAll("[|]", "");//removes brackets
			symbols.trim();
		
		for (String s : symbols.split(",")){
			legal.add(s);
		}
		
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
		for (String s : legal){
			if (s.compareTo(c) == 0){
				return true;
			}
		}
		return false;
	}
	
	
	
}
