import java.util.ArrayList;
import java.util.HashMap;


/**
 * Lexical class serves as a wrapper class for TokenCs and CharacterCs
 * This is done to make returning and passing parameters easier.
 *
 */
public class Lexical {
	private ArrayList<TokenC> tokens;
	private HashMap<String, CharacterC> characters;
  private ArrayList<TokenC> rules;
    
	public Lexical(){
		tokens = new ArrayList<TokenC>();
		characters = new HashMap<String,CharacterC>();
		rules = new ArrayList<TokenC>();
	}
	/**
	 * Constructor
	 * @param toks TokenCs to set
	 * @param chars CharacterCs to set
	 */
	public Lexical(ArrayList<TokenC> toks, HashMap<String, CharacterC> chars, ArrayList<TokenC> rules)
	{
    this.rules = rules;
		this.tokens = toks;
		this.characters = chars;
	}

    /**
   * Returns TokenC's
   * @return rules of TokenC's
   */
  public ArrayList<TokenC> getRules()
  {
    return rules;
  }
    
	/**
	 * Returns TokenC's
	 * @return tokens TokenC's to return
	 */
	public ArrayList<TokenC> getTokens()
	{
		return tokens;
	}
	
	/**
	 * Returns CharacterCs
	 * @return characters CharacterCs to return
	 */
	public HashMap<String, CharacterC> getCharacters()
	{
		return characters;
	}
}