import java.util.ArrayList;


public class ExecutorData {
	private String data;
	private String filename;
	private ArrayList<Integer> indices;
	
	public ExecutorData(){};
	
	public ExecutorData(String data, String filename)
	{
		this.data = data;
		this.filename = filename;
		this.indices = new ArrayList<Integer>();
	}
	
	public String getData()
	{
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public ArrayList<Integer> getIndices(){
		return indices;
	}

	public void addIndex(int i){
		this.indices.add(i);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this.data.equals(((ExecutorData)obj).data))
			return true;
		
		return false;
	}
	
	public String toString()
	{
		if(filename.equals(""))
			return "\"" + data + "\"";
			
		return "\"" + data + "\" <" + filename + ", " +indices+ ">";
		
	}
}
