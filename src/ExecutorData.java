
public class ExecutorData {
	private String data;
	private String filename;
	private int line;
	private int startIndex;
	private int endIndex;
	
	public ExecutorData(){};
	
	public ExecutorData(String data, String filename, int line, int startIndex, int endIndex)
	{
		this.data = data;
		this.filename = filename;
		this.line = line;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
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

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
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
			
		return "\"" + data + "\" <" + filename + ", " + line + ", " + startIndex + ", " + endIndex + ">";
		
	}
}
