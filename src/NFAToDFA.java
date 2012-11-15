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
		ArrayList<Set<Entry<String, Integer>>> successorStates = inputStateTable.getSuccessorStates();
		
		//A queue of next states to look at during the conversion
		Queue<ArrayList<Integer>> nextToParse = new LinkedList<ArrayList<Integer>>();
		
		ArrayList<Integer> currState = new ArrayList<Integer>(); //Current State
		ArrayList<String> alreadyProcessed = new ArrayList<String>(); //list of states already processed
		currState.add(0); //Set up initial state
		alreadyProcessed.add("0,"); //set up initial state
		
		
		while(!currState.isEmpty()) //while still states to parse
		{
			Map<String, ArrayList<Integer>> nextStates = new HashMap<String, ArrayList<Integer>>();
			for(int state : currState) //loops over state whether state is just state 1 or state is state 1,2,3
			{
				nextStates = findNextState(successorStates, nextStates, state);
			}
			
			//Add all the new states to nextToParse if not already parsed.
			for(String key : nextStates.keySet())
			{
				ArrayList<Integer> nextState = nextStates.get(key);
				String stateStr = "";
				for(int in : nextState) //create name for already processed.
				{
					stateStr += in + ",";
				}
				if(!alreadyProcessed.contains(stateStr)) //If not already processed, add
				{
					alreadyProcessed.add(stateStr);
					nextToParse.add(nextStates.get(key));
				}
			}
			outputStateTable.addState(nextStates, "something", outputIndex); //add to output table
			currState = nextToParse.poll(); //get next state to parse
		}
		
		return outputStateTable;
	}
	
	/**
	 * Computes nextStates
	 * 
	 * @param successorStates successor states 
	 * @param nextStates next states
	 * @param state current state
	 * @return nextStates next states
	 */
	private static Map<String, ArrayList<Integer>> findNextState(ArrayList<Set<Entry<String, Integer>>> successorStates, 
																Map<String, ArrayList<Integer>> nextStates, int state)
	{
		for(Entry<String, Integer> st : successorStates.get(state))
		{
			if(nextStates.containsKey(st.getKey()))
			{
				//get values for current key index and add this value before putting it back
				ArrayList<Integer> vals = nextStates.get(st.getKey());
				vals.add(st.getValue());
				nextStates.put(st.getKey(), vals);
			}
			else
			{	
				if(st.getKey() == "@") //If epsilon, go deeper!
				{
					nextStates = findNextState(successorStates, nextStates, st.getValue());
				}
				//add key index and value to next states.
				ArrayList<Integer> vals = new ArrayList<Integer>();
				vals.add(st.getValue());
				nextStates.put(st.getKey(), vals);
			}
		}
		return nextStates;
	}
	
}
