import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;


public class NFAToDFA {
	
	private static StateTable inputStateTable;
	private static StateTable outputStateTable;
	
	public static StateTable convert(StateTable table)
	{
		inputStateTable = table;
		outputStateTable = new StateTable();
		
		ArrayList<Set<Entry<String, Integer>>> successorStates = inputStateTable.getSuccessorStates();
		ArrayList<ArrayList<String>> successorTransitions = inputStateTable.getSuccessorTransitions();
		
		boolean doneParsing = false;
		Queue<ArrayList<Integer>> nextToParse = new LinkedList<ArrayList<Integer>>();
		
		ArrayList<Integer> currState = new ArrayList<Integer>();
		
		ArrayList<String> alreadyProcessed = new ArrayList<String>();
		
		currState.add(0);
		alreadyProcessed.add("0,");
		while(!currState.isEmpty())
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
				for(int in : nextState)
				{
					stateStr += in + ",";
				}
				if(!alreadyProcessed.contains(nextStates.toString()))
				{
					alreadyProcessed.add(stateStr);
					nextToParse.add(nextStates.get(key));
				}
			}
			outputStateTable.addState(nextStates, "something", 0);
			currState = nextToParse.poll();
		}
		
		return outputStateTable;
	}
	
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
				if(st.getKey() == "@")
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
