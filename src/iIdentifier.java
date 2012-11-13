import java.util.ArrayList;


public abstract class iIdentifier extends Node{
	
	public abstract ArrayList<String> getLegal();
//	public abstract boolean match(String title); //ignore this for now
	public abstract boolean isLegal(String c);
	public abstract String getTitle();
	public abstract int compareTo(iIdentifier a);
	

}
