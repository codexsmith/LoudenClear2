import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class StateTable {

<<<<<<< HEAD
	private static ArrayList<tableRow> stateTable = new ArrayList<tableRow>(0);
	private Integer currState = 0;
	private ArrayList<Integer> NFAState = new ArrayList<Integer>(0); 
	private boolean accepted = false;
	private tableRow removedRow;
=======
	private static ArrayList<tableRow> stateTable;
	private Integer currState;
	private ArrayList<Integer> NFAState; 
	private boolean accepted;
	private Integer acceptedState;
	private String tokenGenerated;
	
	private enum TableType {NFA,DFA};
	private tableRow removedRow; //storage for addState's remove state
>>>>>>> e0f69b9e761851a1f2bb7ee101bc3fd4a5cfcc0a
	
	public StateTable(){
		stateTable = new ArrayList<tableRow>(0);
		currState = 0;
		NFAState = new ArrayList<Integer>(0); 
		accepted = false;
		acceptedState = -1;
		tokenGenerated = "";
	}
	
	/**
	 * @param index into stateTable
	 * @return tableRow object at that index
	 */
	public tableRow getTableRow(int i){
		return stateTable.get(i);
	}
	
	/**
	 * @param name of the table row to return
	 * @return a matching tableRow
	 */
	public tableRow getTableRowbyName(String name){
		for (tableRow row : stateTable){
			if (row.name.compareTo(name) == 0){
				return row;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param title is a title to search for in the table
	 * @return tableRow with matching title, or null if it doesn't exist
	 */
	public tableRow getTableRow(String title){
		return stateTable.get(0);
	}
	
	/**
	 * 
	 * @param map - transitions
	 * @param name - the title of the state
	 * @param index - if table size is less than index, it will REPLACE the current table entry
	 * @return boolean if a row is replaced true is returned an the replaced row is stored in a removedRow
	 */
	public boolean addState(Map<String, tableRow> map, String name,int index){
		tableRow newRow = new tableRow(map, name); //create
		boolean replace = false;
		
		if (stateTable.size() < index && index >=0){ //append at index
			stateTable.add(index, newRow);
		}
		else if(stateTable.size() > index){ //REPLACE CURRENT TABLEROW AT INDEX
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
	public ArrayList<Set<Entry<String, tableRow>>> getSuccessorStates(){
		Set<Entry<String, tableRow>> rowvalues;
		ArrayList<Set<Entry<String,tableRow>>> values = new ArrayList<Set<Entry<String,tableRow>>>(0);
		
		for (tableRow row : stateTable){
			rowvalues = row.successorStates.entrySet();
			values.add(rowvalues);
		}
		return values;
	}
	
	/**
	 * @return list of all strings that are legal transitions in the table
	 */
	public ArrayList<ArrayList<String>> getSuccessorTransitions(){
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		
		for (tableRow row : stateTable){
			values.add((ArrayList<String>) row.successorStates.keySet());
		}
		
		return values;
	}
	
	/**performs the NFA state walking, checking for epsilon transitions and accepting states
	 * 
	 * @param string to lookup
	 */
	public void NFAlookUp(String c){
		ArrayList<Integer> next = new ArrayList<Integer>(0);
		Integer nextState;
		
		for (Integer state : NFAState){//
			nextState = stateTable.get(state).getNextState(c);
			if (nextState != null){
				next.add(nextState);
			}
		}
		for (Integer state : next){//follows epsilon transitions
			nextState = stateTable.get(state).getNextState("@");
			if(nextState != null){
				next.add(nextState);
			}
		}
		
		for(Integer state : next){//checks for accepting state
			if(stateTable.get(state).accept()){
				accepted = true;
				acceptedState = state;
			}
		}
		NFAState = next;
		
	}
	
	//this will tell us if the symbol has a valid translation from the currentState to another state in the table
	public void DFAlookUp(String c){
		Integer nextState; //state table index
		boolean val = false;
		nextState = stateTable.get(currState).getNextState(c);
		if (nextState != null){
			currState = nextState;
		}
		if (stateTable.get(currState).accept()){
			accepted = true;
		}
		
	}
	
	/**
	 * represents a single state in the automaton 
	 */
	public class tableRow{
		//Map of strings to tableRows, transitions
		private Map<String,tableRow> successorStates;
		private boolean accept;
		private String name;
		
		public tableRow(Map<String,tableRow> nextStates, String n){
			successorStates = nextStates;
			name = n;
		}
		
		public tableRow getNextState(String c){
			return successorStates.get(c);
		}
		
		public boolean accept(){
			return accept;
		}
		
		
	}
}
