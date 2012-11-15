import java.util.Map;


public class TableRow{
	//Map of strings to tableRows, transitions
	private Map<String,TableRow> successorStates;
	private boolean accept;
	private String name;
	private String type;
	
	public TableRow(Map<String,TableRow> nextStates, String n, String t){
		setSuccessorStates(nextStates);
		setName(n);
		type = t;
		accept = false;
	}
	
	public TableRow getNextState(String c){
		return getSuccessorStates().get(c);
	}
	
	public boolean accept(){
		return accept;
	}
	
	public String toString(){
		return "State: "+getName()+" :: Accept: "+accept;
	}
	
	public void setAccept(boolean val){
		accept = val;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String,TableRow> getSuccessorStates() {
		return successorStates;
	}

	public void setSuccessorStates(Map<String,TableRow> successorStates) {
		this.successorStates = successorStates;
	}
}