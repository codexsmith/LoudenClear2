import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StateTable {

	private static ArrayList<TableRow> stateTable;
	private ArrayList<TableRow> currState;
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
	 * @param index into stateTable
	 * @return ArrayList of tableRow object at that index
	 */
	public ArrayList<TableRow> getTableRowArray(int i){
		ArrayList<TableRow> s = new ArrayList<TableRow>();
		s.add(stateTable.get(i));
		return s;
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
		if (stateTable.size() < index){
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
	public boolean addState(Map<String, ArrayList<TableRow>> map, String name, String type, int index){
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
	public ArrayList<Set<Entry<String, ArrayList<TableRow>>>> getSuccessorStates(){
		Set<Entry<String, ArrayList<TableRow>>> rowvalues;
		ArrayList<Set<Entry<String,ArrayList<TableRow>>>> values = new ArrayList<Set<Entry<String,ArrayList<TableRow>>>>();
		
		for (TableRow row : stateTable){
			if (row.getSuccessorStates() != null){
				rowvalues = row.getSuccessorStates().entrySet();
				System.out.println(rowvalues);
				values.add(rowvalues);
			}
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
	public void NFAlookUp(String c){//redo
		ArrayList<TableRow> next = new ArrayList<TableRow>(0);
		ArrayList<TableRow> nextState;
		
		for (TableRow state : NFAState){//
			nextState = stateTable.get(stateTable.indexOf(state)).getNextState(c);
			if (nextState != null){
				next.addAll(nextState);
			}
		}
		for (TableRow state : next){//follows epsilon transitions
			nextState = stateTable.get(stateTable.indexOf(state)).getNextState("@");
			if(nextState != null){
				next.addAll(nextState);
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
		ArrayList<TableRow> nextState; //state table index
		boolean val = false;
		nextState = stateTable.get(stateTable.indexOf(currState)).getNextState(c);
		if (nextState != null){
			currState = nextState;
		}
		for (TableRow state : currState){
			if (state.accept()){
				accepted = true;
			}
		}
	}
	
	public void printTable(){
		for(TableRow t:stateTable){
			System.out.println(t);
		}
	}
}
