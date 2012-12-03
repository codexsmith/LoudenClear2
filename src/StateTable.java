import java.util.LinkedList;

public class StateTable{
	private LinkedList<State> table;
	
	public StateTable(){
		table = new LinkedList<State>();
	}
	
	public LinkedList<State> getTable(){
		return table;
	}
	
	public void concat(State s1, State s2){
		s1.addTrans("@", s2);
	}
	
	public String toString(){
		String result = "";
		int i = 1;
		for(State s: table){
			result+="State"+s+": "+s.getTable();
			if(s.getAccept()){
				result+=" *"+s.getToken()+"\n";
			}
			else{
				result+="\n";
			}
			i++;
		}
		return result;
	}
}
