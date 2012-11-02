import java.util.ArrayList;


public class StateTable {

	private static ArrayList<iIdentifier> stateTable = new ArrayList<iIdentifier>(0);
	
	private StateTable(){
		
	}
	
	public void addState(iIdentifier row){
		stateTable.add(row);
	}
	
	public void lookUp(){
		
	}
	
}
