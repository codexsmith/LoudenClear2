import java.util.Map;


public class tableRow{
	//Map of strings to tableRows, transitions
	private Map<String,tableRow> successorStates;
	private boolean accept;
	private String name;
	private String type;
	
	public tableRow(Map<String,tableRow> nextStates, String n, String t){
		successorStates = nextStates;
		name = n;
		type = t;
		accept = false;
	}
	
	public tableRow getNextState(String c){
		return successorStates.get(c);
	}
	
	public boolean accept(){
		return accept;
	}
	
	public String toString(){
		return "State: "+name+" :: Accept: "+accept;
	}
	
	public void setAccept(boolean val){
		accept = val;
	}
}