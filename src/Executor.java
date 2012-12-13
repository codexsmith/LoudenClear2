import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Executor {
	
	private HashMap<String, String> ids;
	private TreeNode root;
	
	/**
	 * Execute all the stuffs
	 * Processes AST in post order
	 * 
	 * @param root 
	 */
	public Executor(TreeNode root)
	{
		ids = new HashMap<String, String>();
		this.root = root;
		
		/*
		 * Start at the root of the AST and start parsing
		 */
		recursiveExecutor(root);
			
	}
	
	/**
	 * Execute in post order
	 * 
	 * 
	 * @param node current node
	 */
	public ArrayList<String> recursiveExecutor(TreeNode node)
	{
		//init structures
		ArrayList<String> returnVals = new ArrayList<String>(); //returned results
		ArrayList<ArrayList<String>> childVals = new ArrayList<ArrayList<String>>(); //child return results
		int i = 0;
		TreeNode child;
		
		//Perform Child operations
		while((child = node.getChild(i)) != null)
		{
			childVals.add(recursiveExecutor(child));
			i++;
		}
		
		/*
		 * Determine which operation to perform
		 */
		String nodeName = node.getName();
		if(nodeName.equals("find"))
		{
			String filename = childVals.get(0).get(0); //filename
			findExpression(filename, node.getArg(0)); //filename & regex
		}
		else if(nodeName.equals("replace")) //arg0 = filename
		{
			String filename = childVals.get(0).get(0);         //file1 name
			String filename2 = childVals.get(1).get(0);		   //file2 name
			replace(filename, filename2, node.getArg(0), node.getArg(1)); //filename, regex, str
		}
		else if(nodeName.equals("filename"))
		{
			returnVals.add(node.getArg(0)); //return filename
		}
		else if(nodeName.equals("union"))
		{
			ArrayList<String> set1 = childVals.get(0); //string 1 to union
			ArrayList<String> set2 = childVals.get(1); //string 2 to union
			returnVals.add(union(set1, set2));
		}
		else if(nodeName.equals("intersect"))
		{
			ArrayList<String> set1 = childVals.get(0); //string 1 to union
			ArrayList<String> set2 = childVals.get(1); //string 2 to union
			returnVals.add(intersect(set1, set2));
		}
		else if(nodeName.equals("diff"))
		{
			ArrayList<String> set1 = childVals.get(0); //string 1 to union
			ArrayList<String> set2 = childVals.get(1); //string 2 to union
			returnVals.add(difference(set1, set2));
		}
		else if(nodeName.equals("assign"))
		{
			ids.put(node.getArg(0), childVals.get(0).get(0));
		}
		else if(nodeName.equals("assignLength"))
		{
			ids.put(node.getArg(0), Integer.toString(childVals.get(0).get(0).length()));
		}
		else if(nodeName.equals("recursiveReplacement"))
		{
			
		}
		else if(nodeName.equals("print"))
		{
			
		}
		
		
		return returnVals;
	}
	
	/*
	 * Assignment-expression
	 * assign-length
	 * assign-maxFrequentString
	 * replacement
	 * replacement-maxFrequentString
	 * recursive-replacement
	 * print
	 * find-expression
	 * union
	 * intersection
	 * difference
	 * 
	 */
	
	public String findExpression(String filename, String regex)
	{
		File readFile = new File(filename);
		if(!readFile.exists())
		{
			System.out.println("File " + filename + " was not found");
			return "";
		}
		
		return "";
	}
	
	public void replace(String filename, String filename2, String regex, String str)
	{
		File readFile = new File(filename);
		File outFile = new File(filename2); //if exists, overwrite file.
		
		if(filename.equals(filename2)) 
		{
			System.out.println("Parse Error: Can't replace in same file");
		}
		
		if(!readFile.exists()) 
		{ 
			System.out.println("Can't continue due to problem reading file. Goodbye.");
		}
		
		//TODO: Search and replace all strings in file 1 matching regex with str
		
		try {
			FileWriter fw = new FileWriter(outFile);
		} catch (IOException e) {
			System.out.println("Can't continue due to problem writing file. Goodbye.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Unioning
	 */
	public String union(ArrayList<String> set1, ArrayList<String> set2)
	{
		for(int i = 0; i < set2.size(); i++)
		{
			if(!set1.contains(set2.get(i)))
				set1.add(set2.get(i));
		}
		return arrayListToString(set1);
	}
	
	/**
	 * intersecting
	 */
	public String intersect(ArrayList<String> set1, ArrayList<String> set2)
	{
		ArrayList<String> retSet = new ArrayList<String>();
		
		for(String str : set1)
		{
			if(set2.contains(str))
				retSet.add(str);
		}
		
		return arrayListToString(retSet);
	}
	
	public String difference(ArrayList<String> set1, ArrayList<String> set2)
	{
		ArrayList<String> retSet = new ArrayList<String>();
		
		for(String str : set1)
		{
			if(!set2.contains(str))
				retSet.add(str);
		}
		
		return arrayListToString(retSet);
	}
	
	private String arrayListToString(ArrayList<String> array)
	{
		String retStr = "";
		
		for(String str : array)
		{
			if(!retStr.isEmpty()) retStr += ", ";
			retStr += str;
		}
		
		return retStr;
	}
}
