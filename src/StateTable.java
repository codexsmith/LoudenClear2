import java.util.ArrayList;


public class StateTable {

	private static ArrayList<tableRow> stateTable = new ArrayList<tableRow>(0);
	
	private StateTable(){
		
	}
	
	public void addState(tableRow row){
		stateTable.add(row);
	}
	
	//this will tell us if the symbol has a valid translation in the stateTable
	public boolean lookUp(String c){
		String nextState; //this will likely become a state type or class, an enum more than likely.
		boolean val = false;
		for (tableRow I : stateTable){
			if(I.isLegal(c)){
				nextState = I.getNextState();
			}
		}
		
		return val;
	}
	
	/**Class stub for the final DFA, this will likely get broken into it's own class heirarchy
	 * but i dont know it yet. so it is here for convenience while editing the stateTable itself
	 * @author nick
	 *
	 */
	private class tableRow{
		
		private String successorState;
		private String DFA; //this will obviously become whatever type the DFA is
		
		public String getNextState(){
			return successorState;
		}
		
		public boolean isLegal(String c){
			return DFA.compareTo(c) == 0;
		}
		
	}
}
