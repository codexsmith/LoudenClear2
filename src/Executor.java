import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Executor {
	
	private HashMap<String, ExecutorData> ids;
	private TreeNode root;
	
	/**
	 * Execute all the stuffs
	 * Processes AST in post order
	 * 
	 * @param root 
	 */
	public Executor(TreeNode root)
	{
		ids = new HashMap<String, ExecutorData>();
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
	public ArrayList<ExecutorData> recursiveExecutor(TreeNode node)
	{
		//init structures
		ArrayList<ExecutorData> returnVals = new ArrayList<ExecutorData>(); //returned results
		ArrayList<ArrayList<ExecutorData>> childVals = new ArrayList<ArrayList<ExecutorData>>(); //child return results
		int i = 0;
		TreeNode child;
		
		System.out.println("current node up: " + node.getName());
		
		//Perform Child operations recursively
		while((child = node.getChild(i)) != null)
		{
			childVals.add(recursiveExecutor(child));
			i++;
		}
		
		System.out.println("current node down: " + node.getName());
		
		/*
		 * Determine which operation to perform
		 */
		String nodeName = node.getName();
		if(nodeName.equals("find"))
		{
			String filename = childVals.get(0).get(0).getData(); //filename
			ArrayList<ExecutorData> found = findExpression(filename, node.getArg(0)); //filename & regex
			returnVals.addAll(found);
		}
//		else if(nodeName.equals("replace")) //arg0 = filename
//		{
//			String filename = childVals.get(0).get(0).getData();         //file1 name
//			String filename2 = childVals.get(0).get(1).getData();		   //file2 name
//			replace(filename, filename2, node.getArg(0), node.getArg(1)); //filename, regex, str
//		}
//		else if(nodeName.equals("union"))
//		{
//			ExecutorData ed = new ExecutorData();
//			ed.setData("union");
//			returnVals.add(ed);
//		}
//		else if(nodeName.equals("inters"))
//		{
//			ExecutorData ed = new ExecutorData();
//			ed.setData("inters");
//			returnVals.add(ed);
//		}
//		else if(nodeName.equals("diff"))
//		{
//			ExecutorData ed = new ExecutorData();
//			ed.setData("diff");
//			returnVals.add(ed);
//		}
//		else if(nodeName.equals("statement"))
//		{
//			if(node.getArg(1) != null && node.getArg(1).equals("#"))
//			{
//				ids.put(node.getArg(0), childVals.get(0).get(0));
//			}
//			else if(node.getArg(1) != null && !node.getArg(1).equals("#"))
//			{
//				ArrayList<ExecutorData> listCmp = childVals.get(0);
//				ids.put(node.getArg(0), assignMaxFrequentString(listCmp)); 
//			}
//			else
//			{
//				ids.put(node.getArg(0), childVals.get(0).get(0));
//			}	
//		}
//		else if(nodeName.equals("recursiveReplacement"))
//		{
//			
//		}
//		else if(nodeName.equals("print"))
//		{
//			if(node.getArg(0) != null) //has ID arg
//			{
//				System.out.println(Integer.parseInt(node.getArg(0)));
//			}
//			else // has child expression
//			{
//				ArrayList<ExecutorData> childExpression = childVals.get(0);
//				System.out.println(arrayListToString(childExpression));
//			}
//		}
//		else if(nodeName.equals("file-names")) //multiple files, get values from child
//		{
//			//loop through each filename child and get the filename from arg0
//			for(int j = 0; j < childVals.size(); j++)
//				returnVals.add(childVals.get(j).get(0));
//		}
//		else if(nodeName.equals("file-name")) //single file. has name as arg0
//		{
//			ExecutorData ed = new ExecutorData();
//			ed.setData(node.getArg(0)); //get file name
//			returnVals.add(ed);
//		}
//		else if(nodeName.equals("source-file") || nodeName.equals("destination-file"))
//		{
//			ExecutorData ed = new ExecutorData();
//			ed.setData(node.getArg(0));
//			returnVals.add(ed); //return filename
//		}
//		else if(nodeName.equals("exp-list")) //forward data
//		{
//			returnVals.addAll(childVals.get(0));
//		}
		else if(nodeName.equals("exp")) //child 0 - term, child 1 - exp_tail value (0 bin_op, 1+-term
		{
				ArrayList<ExecutorData> exp_tail_rtn = childVals.get(1);
				
				if(exp_tail_rtn.get(0).getData().equals("union"))
				{
					exp_tail_rtn.remove(0);
					ArrayList<ExecutorData> set1 = childVals.get(0); //string 1 to union
					ArrayList<ExecutorData> set2 = exp_tail_rtn; //string 2 to union
					returnVals.addAll(union(set1, set2));
				}
				else if(exp_tail_rtn.get(0).getData().equals("diff"))
				{
					exp_tail_rtn.remove(0);
					ArrayList<ExecutorData> set1 = childVals.get(0); //string 1 to union
					ArrayList<ExecutorData> set2 = exp_tail_rtn; //string 2 to union
					returnVals.addAll(difference(set1, set2));
				}
				else if(exp_tail_rtn.get(0).getData().equals("inters"))
				{
					exp_tail_rtn.remove(0);
					ArrayList<ExecutorData> set1 = childVals.get(0); //string 1 to union
					ArrayList<ExecutorData> set2 = exp_tail_rtn; //string 2 to union
					returnVals.addAll(intersect(set1, set2));
				}
		}
		else if(nodeName.equals("exp-tail")) //child 0 - bin_op, //child 1 - find, //child 2 - exp_tail value (0 bin_op, 1+-term)
		{
			try {
				ArrayList<ExecutorData> exp_tail_rtn = childVals.get(2);
				
				returnVals.addAll(childVals.get(0));
				
				//Won't reach here if has no right exp_tail recurse and will go to the 
				//catch stage instead
				if(exp_tail_rtn.get(0).getData().equals("union"))
				{
					exp_tail_rtn.remove(0);
					ArrayList<ExecutorData> set1 = childVals.get(1); //string 1 to union
					ArrayList<ExecutorData> set2 = exp_tail_rtn; //string 2 to union
					returnVals.addAll(union(set1, set2));
				}
				else if(exp_tail_rtn.get(0).getData().equals("diff"))
				{
					exp_tail_rtn.remove(0);
					ArrayList<ExecutorData> set1 = childVals.get(1); //string 1 to union
					ArrayList<ExecutorData> set2 = exp_tail_rtn; //string 2 to union
					returnVals.addAll(difference(set1, set2));
				}
				else if(exp_tail_rtn.get(0).getData().equals("inters"))
				{
					exp_tail_rtn.remove(0);
					ArrayList<ExecutorData> set1 = childVals.get(1); //string 1 to union
					ArrayList<ExecutorData> set2 = exp_tail_rtn; //string 2 to union
					returnVals.addAll(intersect(set1, set2));
				}
			} catch (IndexOutOfBoundsException e) { //This is the bottom of the tree, go up!
				returnVals.add(childVals.get(0).get(0)); //return bin_op type
				returnVals.addAll(childVals.get(1)); //add on all found children.
			}
		}
		
		return returnVals;
	}
	
	public ExecutorData assignMaxFrequentString(ArrayList<ExecutorData> list)
	{
		HashMap<String, Integer> frequency = new HashMap<String, Integer>();
		
		for(ExecutorData ed : list) //increment str frequency value
		{
			if(frequency.containsKey(ed.getData())) //exists, increment
				frequency.put(ed.getData(), frequency.get(ed.getData())+1);
			else
				frequency.put(ed.getData(), 1); //doesn't exist, put 1
		}
		
		//Find the most frequent item in the hashmap
		String mostFrequent = "";
		int mostFrequentCount = -1;
		for(String key : frequency.keySet())
		{
			if(mostFrequentCount < frequency.get(key))
			{
				mostFrequent = key;
				mostFrequentCount = frequency.get(key);
			}			
		}
		
		ExecutorData edCmp = new ExecutorData();
		edCmp.setData(mostFrequent);
		return list.get(list.indexOf(edCmp));
	}
	
	public ArrayList<ExecutorData> findExpression(String filename, String regex)
	{
		File readFile = new File(filename);
		if(!readFile.exists())
		{
			System.out.println("File " + filename + " was not found");
			return null;
		}
		
//		Tokenizer tokenizer = new Tokenizer(readFile, regex);
//		
//		ArrayList<ExecutorData> foundTokens = tokenizer.parse();
		
		ArrayList<ExecutorData> foundMatches = new ArrayList<ExecutorData>();
		Scanner snTemp;
		try {
			snTemp = new Scanner(readFile);
			
			int currLine = 0;
			int currStartIndex = 0;
			int currEndIndex = 0;
			while(snTemp.hasNextLine())
			{
				String tmp = snTemp.nextLine();
				String[] as = tmp.split(" ");
				for(String s : as)
				{
					currEndIndex = currStartIndex + s.length();
					if(Pattern.matches(regex, s))
						foundMatches.add(new ExecutorData(s, filename, currLine, currStartIndex, currEndIndex));
					
					currStartIndex = currEndIndex + 2;
				}
				currLine++;
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		
		return foundMatches;
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
		
//		Tokenizer tokenizer = new Tokenizer(readFile, regex);
//		ArrayList<String> foundMatches = tokenizer.parse();
		
		
		try {
			
			ArrayList<ExecutorData> foundMatches = new ArrayList<ExecutorData>();
			Scanner snTemp = new Scanner(readFile);
			
			int currLine = 0;
			int currStartIndex = 0;
			int currEndIndex = 0;
			while(snTemp.hasNextLine())
			{
				String tmp = snTemp.nextLine();
				String[] as = tmp.split(" ");
				for(String s : as)
				{
					currEndIndex = currStartIndex + s.length();
					if(Pattern.matches(regex, s))
						foundMatches.add(new ExecutorData(s, filename, currLine, currStartIndex, currEndIndex));
					
					currStartIndex = currEndIndex + 2;
				}
				currLine++;
			}		
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			Scanner sn = new Scanner(readFile);
			while(sn.hasNextLine())
			{
				String s = sn.nextLine();
				
				for(ExecutorData matches : foundMatches)
					s.replaceAll(matches.getData(), str);
				
				bw.write(s);
			}
			
			bw.close();
			sn.close();
		} catch (IOException e) {
			System.out.println("Can't continue due to problem writing file. Goodbye.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Unioning
	 */
	public ArrayList<ExecutorData> union(ArrayList<ExecutorData> set1, ArrayList<ExecutorData> set2)
	{
		for(int i = 0; i < set2.size(); i++)
		{
			if(!set1.contains(set2.get(i)))
				set1.add(set2.get(i));
		}
		return set1;
	}
	
	/**
	 * intersecting
	 */
	public ArrayList<ExecutorData> intersect(ArrayList<ExecutorData> set1, ArrayList<ExecutorData> set2)
	{
		ArrayList<ExecutorData> retSet = new ArrayList<ExecutorData>();
		
		for(ExecutorData ed : set1)
		{
			if(set2.contains(ed.getData()))
				retSet.add(ed);
		}
		
		return retSet;
	}
	
	public ArrayList<ExecutorData> difference(ArrayList<ExecutorData> set1, ArrayList<ExecutorData> set2)
	{
		ArrayList<ExecutorData> retSet = new ArrayList<ExecutorData>();
		
		for(ExecutorData ed : set1)
		{
			if(!set2.contains(ed))
				retSet.add(ed);
		}
		
		return retSet;
	}
	
	private String arrayListToString(ArrayList<ExecutorData> array)
	{
		String retStr = "";
		
		for(ExecutorData ed: array)
		{
			if(!retStr.isEmpty()) retStr += ", ";
			retStr += ed.getData();
		}
		
		return retStr;
	}
}
