import java.util.ArrayList;
import java.util.Map;


public class StateTable {

	private static ArrayList<tableRow> stateTable = new ArrayList<tableRow>(0);
	private Integer currState;
	
	
	
	public StateTable(){
		
	}
	
	public void addState(){
		tableRow newRow = new tableRow(); //create
		stateTable.add(newRow);
	}
	
	//this will tell us if the symbol has a valid translation in the stateTable
	public boolean DFAlookUp(String c){
		Integer nextState; //state table index
		boolean val = false;
		nextState = stateTable.get(currState).getNextState(c);
		if (nextState != null){
			currState = nextState;
		}
		return val;
	}
	
	/**this will likely get broken into it's own class heirarchy
	 * but i dont know it yet. so it is here for convenience while editing the stateTable itself
	 * @author nick
	 *
	 */
	private class tableRow{
		//Map of strings to tableRows
		private Map<String,Integer> successorStates;
		private boolean accept;
		
		public tableRow(){
			
		}
		
		/**
		 * 
		 * @param c
		 * @return
		 */
		public Integer getNextState(String c){
			return successorStates.get(c);
		}
		
		public boolean accept(){
			return accept;
		}
		
	}
}
