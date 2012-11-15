import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StateTable {

	private static ArrayList<TableRow> stateTable;
	private TableRow currState;
	private ArrayList<TableRow> NFAState; 
	private boolean accepted;
	private TableRow acceptedState;
	private String tokenGenerated;
	
	private enum TableType {NFA,DFA};
	private TableRow removedRow; //storage for addState's remove state
	
	public StateTable(){
		stateTable = new ArrayList<TableRow>(0);
		currState = null;
		NFAState = new ArrayList<TableRow>(0); 
		accepted = false;
		acceptedState = null;
		tokenGenerated = "";
	}
	
	/**
	 * @param index into stateTable
	 * @return tableRow object at that index
	 */
	public TableRow getTableRow(int i){
		return stateTable.get(i);
	}
	
	/**
	 * @param name of the table row to return
	 * @return a matching tableRow
	 */
	public TableRow getTableRowbyName(String name){
		for (TableRow row : stateTable){
			if (row.getName().compareTo(name) == 0){
				return row;
			}
		}
		return null;
	}
	
	/** 
	 * @param TableRow to lookup in stateTable
	 * @return index of tableRow in stateTable, or null if not
	 */
	public int getIndexOf(TableRow t){
		return stateTable.indexOf(t);
	}
	
	
	public void add(TableRow t, int index){
		if (stateTable.size() <= index){
			stateTable.ensureCapacity(index+1);
			stateTable.add(index, t);
		}
		else{
			removedRow = stateTable.set(index, t);
		}
	}
	
	/**
	 * @param map - transitions
	 * @param name - the title of the state
	 * @param index - if table size is less than index, it will REPLACE the current table entry
	 * @return boolean if a row is replaced true is returned an the replaced row is stored in a removedRow
	 */
	public boolean addState(Map<String, TableRow> map, String name, String type, int index){
		TableRow newRow = new TableRow(map, name, type); //create
		boolean replace = false;
		
		if (stateTable.size() < index && index >=0){ //append at index
			stateTable.ensureCapacity(index+1);
			stateTable.add(index, newRow);
		}
		else if(stateTable.size() > index){ //REPLACE CURRENT TABLEROW AT INDEX
			stateTable.ensureCapacity(index+1);
			removedRow = stateTable.remove(index);//stores removed row
			stateTable.add(index, newRow);
			replace = true;
		}
		else if(index < 0){ //append to end
			stateTable.add(newRow);
		}
		return replace;
	}
	
	/**
	 * @return a list of all the transition maps that are currently in the stateTable
	 */
	public ArrayList<Set<Entry<String, TableRow>>> getSuccessorStates(){
		Set<Entry<String, TableRow>> rowvalues;
		ArrayList<Set<Entry<String,TableRow>>> values = new ArrayList<Set<Entry<String,TableRow>>>(0);
		
		for (TableRow row : stateTable){
			rowvalues = row.getSuccessorStates().entrySet();
			values.add(rowvalues);
		}
		return values;
	}
	
	/**
	 * @return list of all strings that are legal transitions in the table
	 */
	public ArrayList<ArrayList<String>> getSuccessorTransitions(){
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		
		for (TableRow row : stateTable){
			values.add((ArrayList<String>) row.getSuccessorStates().keySet());
		}
		
		return values;
	}
	
	/**performs the NFA state walking, checking for epsilon transitions and accepting states
	 * 
	 * @param string to lookup
	 */
	public void NFAlookUp(String c){
		ArrayList<TableRow> next = new ArrayList<TableRow>(0);
		TableRow nextState;
		
		for (TableRow state : NFAState){//
			nextState = stateTable.get(stateTable.indexOf(state)).getNextState(c);
			if (nextState != null){
				next.add(nextState);
			}
		}
		for (TableRow state : next){//follows epsilon transitions
			nextState = stateTable.get(stateTable.indexOf(state)).getNextState("@");
			if(nextState != null){
				next.add(nextState);
			}
		}
		
		for(TableRow state : next){//checks for accepting state
			if(stateTable.get(stateTable.indexOf(state)).accept()){
				accepted = true;
				acceptedState = state;
			}
		}
		NFAState = next;
		
	}
	
	//this will tell us if the symbol has a valid translation from the currentState to another state in the table
	public void DFAlookUp(String c){
		TableRow nextState; //state table index
		boolean val = false;
		nextState = stateTable.get(stateTable.indexOf(currState)).getNextState(c);
		if (nextState != null){
			currState = nextState;
		}
		if (currState.accept()){
			accepted = true;
		}
	}
	
	public void printTable(){
		for(TableRow t:stateTable){
			System.out.println(t);
			System.out.println(t.getSuccessorStates());
/*			for(int i=0;i<t.getSuccessorStates().size();i++){
				t.getSuccessorStates().
			}*/
		}
	}
	/**
	 * represents a single state in the automaton 
	 */

}
