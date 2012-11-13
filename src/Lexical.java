import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Lexical {
	private ArrayList<TokenC> tokens;
	private HashMap<String, CharacterC> characters;
	
	public Lexical(ArrayList<TokenC> toks, HashMap<String, CharacterC> chars)
	{
		this.tokens = toks;
		this.characters = chars;
	}
	
	public ArrayList<TokenC> getTokens()
	{
		return tokens;
	}
	
	public Map<String, CharacterC> getCharacters()
	{
		return characters;
	}
}
