import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class State {
	private HashMap<String,ArrayList<State>> next;
	private boolean accept;
	private String name;
	private String token;
	private boolean handled;
	
	public State(){
		next = new HashMap<String,ArrayList<State>>();
		accept = false;
		name = "";
		token = "";
	}
	
	public State(ArrayList<State> list){
		next = new HashMap<String,ArrayList<State>>();
		name = "";
		token = "";
		accept = false;
		for(State s:list){
			if(name.length()==0){
				name+=s.toString();
			}
			else{
				name+=","+s.toString();
			}
			if(s.getAccept()){
				accept = true;
				token = s.getToken();
			}
			for(Entry<String,ArrayList<State>> e:s.getTable().entrySet()){
				if(next.containsKey(e.getKey())){//old so concat
					for(State st:s.getTable().get(e.getKey())){
						next.get(e.getKey()).add(st);
					}
				}
				else{
					next.put(e.getKey(), e.getValue());
				}
			}
		}
//		System.out.println(next);
		next.remove("@");
	}
	
	public void accept(String s){
		accept = true;
		token = s;
	}
	
	public void addTrans(String key, State state){
		if(this.next.get(key)==null){
			ArrayList<State> newArray = new ArrayList<State>();
			newArray.add(state);
			next.put(key, newArray);
		}
		else{
			next.get(key).add(state);
		}
	}
	
	public void replaceTrans(String key, State state){
		ArrayList<State> newArray = new ArrayList<State>();
		newArray.add(state);
		next.put(key, newArray);
	}
	
	public HashMap<String,ArrayList<State>> getTable(){
		return next;
	}
	
	public void setTable(HashMap<String,ArrayList<State>> map){
		next = map;
	}
	
	public String getToken(){
		return token;
	}
	
	public void setToken(String s){
		token = s;
	}
	
	public boolean getAccept(){
		return accept;
	}
	
	public void setName(String s){
		name = s;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean handled(){
		return handled;
	}
	
	public String toString(){
		return name;
	}
}
