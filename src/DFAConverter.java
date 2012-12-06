import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DFAConverter {
	private StateTable nfa;
	private StateTable dfa;
	private ArrayList<State> ready;
	private HashMap<String,State> processed;
	private HashMap<String,ArrayList<State>> epsilons;
	
	public DFAConverter(StateTable input){
		nfa = input;
		dfa = new StateTable();
		processed = new HashMap<String,State>();
		epsilons = new HashMap<String,ArrayList<State>>();
	}

	public StateTable convert(){
		ready = new ArrayList<State>();
		ready.add(nfa.getTable().get(0));
		while(!ready.isEmpty()){
			State current = ready.get(0);
			ready.remove(0);
			process(current);
		}
		cleanup();
		return dfa;
	}
	
	private void epsilon_closure(State s){
		if(epsilons.containsKey(s.toString())){
			return;
		}
		if(!s.getTable().containsKey("\\@")){
			ArrayList<State> temp = new ArrayList<State>();
			temp.add(s);
			epsilons.put(s.toString(), temp);
			return;
		}
		for(Entry<String,ArrayList<State>> e:s.getTable().entrySet()){
			ArrayList<State> temp = new ArrayList<State>();
			if(e.getKey()=="\\@"){
				for(State st:e.getValue()){
					epsilon_closure(st);
//					System.out.println("EC:"+epsilons.get(st.toString()));
					for(State state:epsilons.get(st.toString())){
						if(!temp.contains(state)){
							temp.add(state);
						}
					}
//					temp.addAll(epsilons.get(st.toString()));
//					System.out.println("Temp:"+temp);
					epsilons.put(s.toString(), temp);
				}
			}
		}
	}
	
	private void process(State s){
		State newCurr = new State();
		if(processed.containsKey(s.toString())&&s.getAccept()){
			processed.get(s.toString()).accept(s.getToken());
		}
		
		if(processed.containsKey(s.toString())){
			return;
		}
		//epsilon closure
		epsilon_closure(s);
		State cf = new State(epsilons.get(s.toString()));
		newCurr.setName(s.getName());
		//transition
		for(Entry<String,ArrayList<State>> e:cf.getTable().entrySet()){
			ArrayList<State> temp = new ArrayList<State>();
			if(e.getKey()!="\\@"){
				for(State st:e.getValue()){
					if(!temp.contains(st)){
						temp.add(st);
					}
				}//end for loop
			}//end if
			//got all possible transitions for this key
			State newState = new State();
//			System.out.println("Temp:"+temp);
			NFAGen(newState,temp);
			epsilon_closure(newState);
			newCurr.setName(s.getName());
//			System.out.println("Debug:"+epsilons.get(newState.toString()));
			newState = new State(epsilons.get(newState.toString()));
			newCurr.addTrans(e.getKey(), newState);
	
			ready.add(newState);
		}
		if(s.getAccept()){
			newCurr.accept(s.getToken());
		}
		processed.put(newCurr.toString(), newCurr);
		dfa.getTable().add(newCurr);
	}
	
	private void cleanup(){
		int i = 0;
		for(State s:dfa.getTable()){
			for(Entry<String,ArrayList<State>>e:s.getTable().entrySet()){
				s.replaceTrans(e.getKey(), processed.get(e.getValue().get(0).toString()));
			}
			s.setName(Integer.toString(i));
			i++;
		}
	}
	
	private void NFAGen(State state,ArrayList<State> list){
		state.setTable(new HashMap<String,ArrayList<State>>());
		String name = "";
		for(State s:list){
			if(name.length()==0){
				name+=s.toString();
			}
			else{
				name+=","+s.toString();
			}
			if(s.getAccept()){
				state.accept(s.getToken());
			}
			for(Entry<String,ArrayList<State>> e:s.getTable().entrySet()){
				if(state.getTable().containsKey(e.getKey())){//old so concat
					for(State st:s.getTable().get(e.getKey())){
						state.getTable().get(e.getKey()).add(st);
					}
				}
				else{
					state.getTable().put(e.getKey(), e.getValue());
				}
			}
		}
		state.setName(name);
//		state.getTable().putAll(s.getTable());

	}
}