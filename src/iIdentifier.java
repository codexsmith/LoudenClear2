import java.util.ArrayList;


public abstract class iIdentifier {
	
	public abstract ArrayList<String> getLegal();
	public abstract boolean match(String title);
	public abstract boolean islegal(String c);

}
