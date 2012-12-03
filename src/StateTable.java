import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StateTable {
	private ArrayList<TableRow> stateTable;
	
	ArrayList<TableRow> currState;
	ArrayList<CharacterC> classCharList = new ArrayList<CharacterC>();
	Iterator <CharacterC> classChar;
	
	
	private TableRow removedRow; //storage for addState's remove state
	
//	if(Driver.DEBUG){System.out.println("");};
	
	public ArrayList<String> DFALookup(PScanner input){
		currState = new ArrayList<TableRow>();
		ArrayList<TableRow> nextState;
		ArrayList<TableRow> temp;
		ArrayList<String> tokClass;
		
		currState.add(stateTable.get(0));
		
		ArrayList<String> tokenOut = new ArrayList<String>(0);
		
		tokenOut.add(new String());
		int tokCount = 0;
		int acceptTokPtr = 0;
		boolean accept = false, error = false;
		
		while(!input.endOfFile()){
			nextState = new ArrayList<TableRow>();
			String str = input.getToken();
			if(Driver.DEBUG){System.out.println("Not end of file. Got '"+str+"'");};
			if(Driver.DEBUG){System.out.println("Char Type "+Character.getType(str.toCharArray()[0]));};

			tokenOut.set(tokCount, tokenOut.get(tokCount).concat(str));
			
			if(Driver.DEBUG){
				System.out.println("tokenCount "+tokCount);
				for(String s : tokenOut){
					System.out.println("token "+s);
				}
			}

			
			for(TableRow aState : currState){
				if (aState.accept()){
					accept = true;
					acceptTokPtr = tokenOut.get(tokCount).length()-1; //we've put the next str on there, so this points to the string we need to return to the input buffer
				}													  // minus one b/c length does not count from zero, like arrays do
			}
			tokClass = new ArrayList<String>();
			//find the token classes that c fits into
			for(CharacterC potClass : classCharList){
				if(potClass.isLegal(str)){
					tokClass.add(potClass.getTitle());//fill tokClass
					if(Driver.DEBUG){System.out.println("Found Token Class "+ tokClass+" for token "+ str);}
				}
			}
			
			for(int stateI = 0; stateI < currState.size(); stateI++){
				for (String tokC : tokClass){
					 temp = currState.get(stateI).getNextState(tokC);
					if (temp != null){
						if(Driver.DEBUG){System.out.println("nextStates " + temp);};
						nextState.addAll(temp);
					}
				}
			}
			
			if(nextState.isEmpty()){
				if(Driver.DEBUG){System.out.println("nextState is null");};
				error = true;
			}
			else{
				if(Driver.DEBUG){System.out.println("setting currState to nextState");};
				currState = nextState;
			}
			
			if(error && !accept){
				error = false;
				if(Driver.DEBUG){System.out.println("Error Return");};
				tokenOut.set(tokCount, tokenOut.get(tokCount).concat(" Token Error"));
				return tokenOut;
			}
			
			if(error && accept){
				error = false;
				accept = false;
				if(Driver.DEBUG){System.out.println("Accepted Token" + tokenOut.get(tokCount));};
				String tempStr = tokenOut.get(tokCount).substring(acceptTokPtr);
				tokenOut.set(tokCount,tokenOut.get(tokCount).substring(0,acceptTokPtr));
				
				input.pushToken(tempStr);
				tokCount++;
				tokenOut.add(new String());
			}
			
		}
		return tokenOut;
	}

	
	

	public StateTable(Lexical l){
		stateTable = new ArrayList<TableRow>(0);
		classChar = l.getCharacters().values().iterator();
		
		while(classChar.hasNext()){
			classCharList.add(classChar.next());
		}
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
