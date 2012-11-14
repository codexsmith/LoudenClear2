import java.util.ArrayList;
import java.util.Map;


public class StateTable {

	private static ArrayList<tableRow> stateTable = new ArrayList<tableRow>(0);
	private Integer currState = 0;
	private ArrayList<Integer> NFAState = new ArrayList<Integer>(0); 
	private boolean accepted = false;
	
	
	public StateTable(){
		
	}
	
	public tableRow getTableRow(int i){
		return stateTable.get(i);
	}
	/**
	 * 
	 * @param map - transitions
	 * @param name
	 * @param index - if table size is less than index, it will REPLACE the current table entry
	 */
	public void addState(Map<String, Integer> map, String name,int index){
		tableRow newRow = new tableRow(map, name); //create
		
		if (stateTable.size() < index && index >=0){ //append at index
			stateTable.add(index, newRow);
		}
		else if(stateTable.size() > index){ //REPLACE CURRENT TABLEROW AT INDEX
			stateTable.remove(index);
			stateTable.add(index, newRow);
		}
		else if(index < 0){ //append to end
			stateTable.add(newRow);
		}
	}
	
	/**
	 * 
	 * @return a list of integers that are currently listed as next states
	 */
	public ArrayList<ArrayList<Integer>> getSuccessorStates(){
		ArrayList<Integer> rowKey = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> values = new ArrayList<ArrayList<Integer>>();
		
		for (tableRow row : stateTable){
			for (String key : row.successorStates.keySet()){
				rowKey.add(row.successorStates.get(key));
			}
			values.add(rowKey);
		}
		return values;
	}
	
	/**
	 * 
	 * @return a list of strings that are currently listed as next states
	 */
	public ArrayList<ArrayList<String>> getSuccessorTransitions(){
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		
		for (tableRow row : stateTable){
			values.add((ArrayList<String>) row.successorStates.keySet());
		}
		
		return values;
	}
	
	/**EXTRA CREDIT
	 * 
	 * @param c
	 */
	public void NFAlookUp(String c){
		ArrayList<Integer> next = new ArrayList<Integer>(0);
		for (Integer state : NFAState){
			next.add(stateTable.get(state).getNextState(c));
		}
		
	}
	
	//this will tell us if the symbol has a valid translation from the currentState to another state in the table
	public boolean DFAlookUp(String c){
		Integer nextState; //state table index
		boolean val = false;
		nextState = stateTable.get(currState).getNextState(c);
		if (nextState != null){
			currState = nextState;
		}
		if (stateTable.get(currState).accept()){
			
		}
		return val;
	}
	
	/**this will likely get broken into it's own class heirarchy
	 * but i dont know it yet. so it is here for convenience while editing the stateTable itself
	 * @author nick
	 *
	 */
	public class tableRow{
		//Map of strings to tableRows
		private Map<String,Integer> successorStates;
		private boolean accept;
		private String name;
		
		public tableRow(Map<String,Integer> nextStates, String n){
			successorStates = nextStates;
			name = n;
		}
		
		public Integer getNextState(String c){
			return successorStates.get(c);
		}
		
		public boolean accept(){
			return accept;
		}
		
	}
}
