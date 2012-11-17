import java.util.ArrayList;
import java.util.Collections;
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
		ArrayList<Set<Entry<String, ArrayList<TableRow>>>> successorStates = inputStateTable.getSuccessorStates();
		
		//A queue of next states to look at during the conversion
		Queue<ArrayList<TableRow>> nextToParse = new LinkedList<ArrayList<TableRow>>();
		
		ArrayList<TableRow> currState = new ArrayList<TableRow>(); //Current State
		ArrayList<String> alreadyProcessed = new ArrayList<String>(); //list of states already processed
		TableRow tr = inputStateTable.getTableRow(0);
		currState.add(tr); //Set up initial state
		alreadyProcessed.add(tr.getName() + ","); //set up initial state
		
		
		while(currState != null) //while still states to parse
		{
			
			Map<String, ArrayList<TableRow>> nextStates = new HashMap<String, ArrayList<TableRow>>();
			String type = "";
			boolean accepted = false;
			for(TableRow state : currState) //loops over state whether state is just state 1 or state is state 1,2,3
			{
				int stateIndex = inputStateTable.getIndexOf(state);
				nextStates = findNextState(successorStates, nextStates, stateIndex, new ArrayList<TableRow>());
				if(state.accept())
				{
					accepted = true;
					type = state.getType();
				}
			}
			
			if(acceptBase)
			{
				accepted = true;
				type = acceptBaseType;
				
			}
			//Add all the new states to nextToParse if not already parsed.
			for(String key : nextStates.keySet())
			{
				ArrayList<TableRow> nextState = nextStates.get(key);
				String stateStr = computeName(nextState);
				if(!alreadyProcessed.contains(stateStr)) //If not already processed, add
				{
					alreadyProcessed.add(stateStr);
					nextToParse.add(nextStates.get(key));
				}
				
//				boolean inAlreadyProcessed = false;
//				for(String ap : alreadyProcessed)
//				{
//					if(ap.contains(stateStr))
//						inAlreadyProcessed = true;
//				}
//				if(inAlreadyProcessed)
//				{
//					alreadyProcessed.add(stateStr);
//					nextToParse.add(nextStates.get(key));
//				}
			}
			
			print_already_processed(alreadyProcessed);
			
			System.out.println("");
			System.out.println("looking at: " + computeName(currState));
			outputStateTable.addState(nextStates, computeName(currState), type, outputIndex, accepted); //add to output table
			outputIndex++;
			currState = nextToParse.poll(); //get next state to parse
		}
		
		return outputStateTable;
	}
	
	private static String computeName(ArrayList<TableRow> rows)
	{
		String name = "";
		Collections.sort(rows);
		for(TableRow row : rows)
		{
			name += row.getName() + ",";
		}
		return name;
	}
	
	private static void print_already_processed(ArrayList<String> alreadyProcessed)
	{
		System.out.println("");
		System.out.println("");
		System.out.println("************************");
		System.out.println("**Printing already processed **");
		System.out.println("************************");
		for(String ap : alreadyProcessed)
			System.out.println(ap);
	}
	
	
	static boolean acceptBase = false;
	static String acceptBaseType = "";
	/**
	 * Computes nextStates
	 * 
	 * @param successorStates successor states 
	 * @param nextStates next states
	 * @param state current state
	 * @param prevIndex index of the previous state
	 * @return nextStates next states
	 */
	private static Map<String, ArrayList<TableRow>> findNextState(ArrayList<Set<Entry<String, ArrayList<TableRow>>>> successorStates, 
																Map<String, ArrayList<TableRow>> nextStates, int state, ArrayList<TableRow> prevStates)
	{
		
		if(successorStates.get(state).isEmpty())
		{
			TableRow finalState = inputStateTable.getTableRow(state);
			if(finalState.accept())
			{
				acceptBase = true;	
				acceptBaseType = finalState.getType();
			}
		}
		
		for(Entry<String, ArrayList<TableRow>> st : successorStates.get(state))
		{
			if(nextStates.containsKey(st.getKey()))
			{
				//get values for current key index and add this value before putting it back
				ArrayList<TableRow> vals = nextStates.get(st.getKey());
				
				for(TableRow tr : st.getValue())
				{
					if(!vals.contains(tr))
					{
						vals.add(tr);
					}
				}
				
//				if(prevStates != null)
//					for(TableRow prev : prevStates)
//					{
//						if(!vals.contains(prev))
//							vals.add(prev);
//					}

				nextStates.put(st.getKey(), vals);
			}
			else
			{	
				if(st.getKey() == "@") //If epsilon, go deeper!
				{
					for(TableRow row : st.getValue())
					{
						int stateIndex = inputStateTable.getIndexOf(row);
						prevStates.add(row);
						nextStates = findNextState(successorStates, nextStates, stateIndex, prevStates);
					}
				}
				else
				{
					//add key index and value to next states.
					ArrayList<TableRow> vals = new ArrayList<TableRow>();
					vals.addAll(st.getValue());
//					if(prevStates != null)
//						vals.addAll(prevStates);
					nextStates.put(st.getKey(), vals);
				}
			}
		}
		return nextStates;
	}
	
}
