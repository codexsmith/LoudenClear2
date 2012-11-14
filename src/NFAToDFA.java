
public class NFAToDFA {
	
	private static StateTable inputStateTable;
	private static StateTable outputStateTable;
	
	public static StateTable convert(StateTable table)
	{
		inputStateTable = table;
		outputStateTable = new StateTable();
		
		StateTable.tableRow currState = inputStateTable.getState();
		
		return outputStateTable;
	}

}
