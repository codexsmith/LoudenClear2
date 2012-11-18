import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StateTable {
	private ArrayList<TableRow> stateTable;
	private ArrayList<TableRow> currState;
	private ArrayList<TableRow> NFAState; 
	private boolean accepted = false;
	private TableRow acceptedState;
	private String tokenGenerated;
	private boolean returned = false;
	private Lexical lex;
	Iterator <CharacterC> classChar;
	
	
	private enum TableType {NFA,DFA};
	private TableRow removedRow; //storage for addState's remove state
	
	public StateTable(Lexical l){
		stateTable = new ArrayList<TableRow>(0);
		currState = new ArrayList<TableRow>(0);
		NFAState = new ArrayList<TableRow>(0); 
		accepted = false;
		acceptedState = null;
		tokenGenerated = "";
		lex = l;
		classChar = lex.getCharacters().values().iterator();
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
	public boolean addState(Map<String, ArrayList<TableRow>> map, String name, String type, int index, boolean accepted){
		TableRow newRow = new TableRow(map, name, type); //create
		newRow.setAccept(accepted);
		boolean replace = false;
		
		if (stateTable.size() <= index && index >=0){ //append at index
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
				if(Driver.DEBUG)System.out.println(rowvalues);
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
	
	//this will tell us if the symbol has a valid translation from the currentState to another state in the table
	public ArrayList<String> DFAlookUp(PScanner input){
		currState.add(stateTable.get(0));
		ArrayList<String> tokOut = new ArrayList<String>(0);
		ArrayList<String> tokClass = new ArrayList<String>(0);
		String c;
		ArrayList<TableRow> nextState;
		
		CharacterC temp;
		
		int count = 0;
		while(!input.endOfFile()){
			c = input.getToken();
			tokOut.add("");
			tokOut.get(count).concat(c);
			
			if (currState.get(0).accept()){
				return tokOut;
			}
			
			while(classChar.hasNext()){
				 temp = classChar.next();
				if(temp.isLegal(c)){
					tokClass.add(temp.getTitle());
					if(Driver.DEBUG){System.out.println("Found Token Class "+ tokClass+" for token "+ c);}
				}
			}
			
			for (String toc : tokClass){//each possible identifier that matches a token
				nextState = currState.get(0).getNextState(toc);
				
				
					
			}
			
			
			
			count++;
		}
		
		return tokOut;
	
		
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
}
