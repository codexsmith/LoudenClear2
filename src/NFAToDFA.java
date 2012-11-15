import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;


/**
 * Converts the NFA StateTable to a DFA StateTable
 *
 */
/**
 * @author donovanhatch
 *
 */
public class NFAToDFA {
	
	private static StateTable inputStateTable;
	private static StateTable outputStateTable;
	private static int outputIndex;
	
	public static StateTable convert(StateTable table)
	{
		inputStateTable = table;
		outputStateTable = new StateTable();
		outputIndex = 0;
		
		//Contains a list of every index in the table that has every single transition from that state
		ArrayList<Set<Entry<String, StateTable.tableRow>>> successorStates = inputStateTable.getSuccessorStates();
		
		//A queue of next states to look at during the conversion
		Queue<ArrayList<StateTable.tableRow>> nextToParse = new LinkedList<ArrayList<StateTable.tableRow>>();
		
		ArrayList<StateTable.tableRow> currState = new ArrayList<StateTable.tableRow>(); //Current State
		ArrayList<String> alreadyProcessed = new ArrayList<String>(); //list of states already processed
		StateTable.tableRow tr = inputStateTable.getTableRow(0);
		currState.add(tr); //Set up initial state
		alreadyProcessed.add(tr.getName() + ","); //set up initial state
		
		
		while(!currState.isEmpty()) //while still states to parse
		{
			Map<String, ArrayList<StateTable.tableRow>> nextStates = new HashMap<String, ArrayList<StateTable.tableRow>>();
			for(StateTable.tableRow state : currState) //loops over state whether state is just state 1 or state is state 1,2,3
			{
				int stateIndex = inputStateTable.getIndexOf(state);
				nextStates = findNextState(successorStates, nextStates, stateIndex);
			}
			
			//Add all the new states to nextToParse if not already parsed.
			for(String key : nextStates.keySet())
			{
				ArrayList<StateTable.tableRow> nextState = nextStates.get(key);
				String stateStr = computeName(nextState);
				
				if(!alreadyProcessed.contains(stateStr)) //If not already processed, add
				{
					alreadyProcessed.add(stateStr);
					nextToParse.add(nextStates.get(key));
				}
			}
			outputStateTable.addState(nextStates, computeName(currState), outputIndex); //add to output table
			currState = nextToParse.poll(); //get next state to parse
		}
		
		return outputStateTable;
	}
	
	private static String computeName(ArrayList<StateTable.tableRow> rows)
	{
		String name = "";
		for(StateTable.tableRow row : rows)
		{
			name += row.getName() + ",";
		}
		return name;
	}
	
	/**
	 * Computes nextStates
	 * 
	 * @param successorStates successor states 
	 * @param nextStates next states
	 * @param state current state
	 * @return nextStates next states
	 */
	private static Map<String, ArrayList<StateTable.tableRow>> findNextState(ArrayList<Set<Entry<String, StateTable.tableRow>>> successorStates, 
																Map<String, ArrayList<StateTable.tableRow>> nextStates, int state)
	{
		for(Entry<String, StateTable.tableRow> st : successorStates.get(state))
		{
			if(nextStates.containsKey(st.getKey()))
			{
				//get values for current key index and add this value before putting it back
				ArrayList<StateTable.tableRow> vals = nextStates.get(st.getKey());
				vals.add(st.getValue());
				nextStates.put(st.getKey(), vals);
			}
			else
			{	
				if(st.getKey() == "@") //If epsilon, go deeper!
				{
					int stateIndex = inputStateTable.getIndexOf(st.getValue());
					nextStates = findNextState(successorStates, nextStates, stateIndex);
				}
				//add key index and value to next states.
				ArrayList<StateTable.tableRow> vals = new ArrayList<StateTable.tableRow>();
				vals.add(st.getValue());
				nextStates.put(st.getKey(), vals);
			}
		}
		return nextStates;
	}
	
}
