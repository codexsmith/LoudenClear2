import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
/*
 * @Author Sanghun Oh
 * known issues:
 * - does not handle line change when writing a file
 * - findStr() is not required?
 */
import java.util.Scanner;

public class Interpretor {
	private Hashtable<String, ArrayList<String>> idHash;
	
	public Interpretor() {
		idHash = new Hashtable<String, ArrayList<String>>();
		
		// test
		ArrayList<String> testarray = new ArrayList<String>();
		testarray.add("xxx");
		testarray.add("yyy");
		testarray.add("xxx");
		idHash.put("testget", testarray);

	}
	
//	<MiniRE-program> ::= begin <statement-list> end
//	<statement-list> ::= <statement><statement-list-tail> 
//	<statement-list-tail> ::= <statement><statement-list-tail> | <epsilon>
//	<statement> ::= replace REGEX with ASCII-STR in  <file-names> ;	
	public void replace(String reg, String str, String src, String dst) {
		String fileContent;
		fileContent = fileRead(src);
		fileContent = fileContent.replaceAll(reg, str);
		fileWrite(fileContent, dst);
	}
	
//	<statement> ::= recursivereplace REGEX with ASCII-STR in  <file-names> ;
	public void recursivereplace (String reg, String str, String src, String dst) {
		String fileContent;
		fileContent = fileRead(src);

		for(int i=0; i<20; i++)
			fileContent = fileContent.replaceAll(reg, str);
		
		fileWrite(fileContent, dst);
	}
	
//	<statement> ::= ID = <statement-righthand> ;
//	<statement-righthand> ::= <exp> | # <exp> | maxfreqstring (ID)
	public void maxfreqstring(String setID, String getID) {
		ArrayList<String> IDArray = idHash.get(getID);
		Hashtable<String, Integer> maxFreqCalc = new Hashtable<String, Integer>();
		ArrayList<String> maxFreq = new ArrayList<String>();
		
		String key;
		int value;
		int counter=0;
		// put list of an ID into a hashtable and counts the most freq.
		for (int i=0; i<IDArray.size(); i++) {
			key = IDArray.get(i);
			if (!maxFreqCalc.containsKey(key))
				value = 1;
			else
				value = maxFreqCalc.get(key) + 1;

			maxFreqCalc.put(key, value);
			if (counter < value)
				counter = value;			
		}
				
		// create a new ID with most freq string
		Enumeration en = maxFreqCalc.keys();
		en = maxFreqCalc.keys();
		while (en.hasMoreElements())
		{
			key = en.nextElement().toString();
			if (maxFreqCalc.get(key) == counter)
				maxFreq.add(key);
		}

		idHash.put(setID, maxFreq);
	}
	
//	<file-names> ::=  <source-file>  >!  <destination-file>
//	<source-file> ::= ASCII-STR  
//	<destination-file> ::= ASCII-STR
//	<statement> ::= print ( <exp-list> ) ;
//	Print statement, which prints a list of expressions to the output. If an expression is an integer, it prints the integer and a newline. If it��s a string-match list, prints the elements in the list in order in some readable format (include the matched string, the filename, and the index), and a newline.	
	public void print(ArrayList<String> ids, ArrayList<ArrayList<String>> strLists, ArrayList<Integer> numList) {
		if(ids != null)
			for (int i=0; i < ids.size(); i++) {
				System.out.println("ID = " + ids.get(i) + " : " + idHash.get(ids.get(i)));
			}
		if(strLists != null)
			for (int i=0; i < strLists.size(); i++) {
				for (int j=0; j < strLists.size(); j++) {
					System.out.println("Array "+ i + " : " + strLists.get(i) );
				}
			}
		if(numList != null)
			for (int i=0; i < ids.size(); i++) {
				System.out.println("INT " + i + " : " + numList.get(i));
			}
	}
	
//	<exp-list> ::= <exp> <exp-list-tail>
//	<exp-list-tail> ::= , <exp> <exp-list-tail> | <epsilon>
//	<exp> ::= ID  | ( <exp> )
	public void setID(String ID, ArrayList<String> strList) {
		idHash.put(ID, strList);
	}
	
	public ArrayList<String> getID(String ID) {
		return idHash.get(ID);
	}
	
	public int getIDLength(String ID) {
		return idHash.get(ID).size();
	}
	
//	<exp> ::=  <term> <exp-tail>
//	<exp-tail> ::= <bin-op> <term> <exp-tail> | <epsilon>
//	<term> ::=  find REGEX in  <file-name>  
	public ArrayList<String> findRegex(String reg, String fileName) {
		ArrayList<String> retVal = new ArrayList<String>();
		String fileContent = fileRead(fileName);
		StringTokenizer token = new StringTokenizer(fileContent, " ");
		String curToken;
		while (token.hasMoreTokens()) {
			curToken = token.nextToken();
			if( curToken.matches(reg))
				retVal.add(curToken);	// add to retVal if matches to given regex
		}

		return retVal;
	}

	public int findRegexLength(String reg, String fileName) {
		ArrayList<String> retVal = new ArrayList<String>();
		String fileContent = fileRead(fileName);
		StringTokenizer token = new StringTokenizer(fileContent, " ");
		String curToken;
		while (token.hasMoreTokens()) {
			curToken = token.nextToken();
			if( curToken.matches(reg))
				retVal.add(curToken);	// add to retVal if matches to given regex
		}

		return retVal.size();
	}

	
//	<file-name> ::=  ASCII-STR
//	<bin-op> ::=  diff | union | inters
	public ArrayList<String> diff(ArrayList<String> strListA, ArrayList<String> strListB) {
		ArrayList<String> strListRet = new ArrayList<String>();
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		String key;
		int value;

		for (int i=0; i<strListA.size(); i++) {
			key = strListA.get(i);
			if (!hash.containsKey(key))
				value = 1;
			else
				value = hash.get(key) + 1;

			hash.put(key, value);
		}

		for (int i=0; i<strListB.size(); i++) {
			key = strListB.get(i);
			if (!hash.containsKey(key))
				value = 1;
			else
				value = hash.get(key) + 1;

			hash.put(key, value);
		}

		// create a new ID with most freq string
		Enumeration en = hash.keys();
		en = hash.keys();
		while (en.hasMoreElements())
		{
			key = en.nextElement().toString();
			if (hash.get(key) == 1)
				strListRet.add(key);
		}
		
		return strListRet;
	}

	public int diffLength(ArrayList<String> strListA, ArrayList<String> strListB) {
		ArrayList<String> strListRet = new ArrayList<String>();
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		String key;
		int value;

		for (int i=0; i<strListA.size(); i++) {
			key = strListA.get(i);
			if (!hash.containsKey(key))
				value = 1;
			else
				value = hash.get(key) + 1;

			hash.put(key, value);
		}

		for (int i=0; i<strListB.size(); i++) {
			key = strListB.get(i);
			if (!hash.containsKey(key))
				value = 1;
			else
				value = hash.get(key) + 1;

			hash.put(key, value);
		}

		// create a new ID with most freq string
		Enumeration en = hash.keys();
		en = hash.keys();
		while (en.hasMoreElements())
		{
			key = en.nextElement().toString();
			if (hash.get(key) == 1)
				strListRet.add(key);
		}
		
		return strListRet.size();
	}

	public ArrayList<String> union(ArrayList<String> strListA, ArrayList<String> strListB) {
		ArrayList<String> strListRet = new ArrayList<String>();
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		String key;
		int value=0;

		for (int i=0; i<strListA.size(); i++) {
			key = strListA.get(i);

			hash.put(key, value);
		}

		for (int i=0; i<strListB.size(); i++) {
			key = strListB.get(i);

			hash.put(key, value);
		}

		// create a new ID with most freq string
		Enumeration en = hash.keys();
		en = hash.keys();
		while (en.hasMoreElements())
		{
			key = en.nextElement().toString();
			strListRet.add(key);
		}
		
		return strListRet;
	}

	public int unionLength(ArrayList<String> strListA, ArrayList<String> strListB) {
		ArrayList<String> strListRet = new ArrayList<String>();
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		String key;
		int value=0;

		for (int i=0; i<strListA.size(); i++) {
			key = strListA.get(i);

			hash.put(key, value);
		}

		for (int i=0; i<strListB.size(); i++) {
			key = strListB.get(i);

			hash.put(key, value);
		}

		// create a new ID with most freq string
		Enumeration en = hash.keys();
		en = hash.keys();
		while (en.hasMoreElements())
		{
			key = en.nextElement().toString();
			strListRet.add(key);
		}
		
		return strListRet.size();
	}

	public ArrayList<String> inters(ArrayList<String> strListA, ArrayList<String> strListB) {
		ArrayList<String> strListRet = new ArrayList<String>();
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		String key;
		int value=0;

		for (int i=0; i<strListA.size(); i++) {
			key = strListA.get(i);

			hash.put(key, value);
		}

		for (int i=0; i<strListB.size(); i++) {
			key = strListB.get(i);
			if (hash.containsKey(key))
				strListRet.add(key);
			hash.put(key, value);
		}

		return strListRet;
	}

	public int intersLength(ArrayList<String> strListA, ArrayList<String> strListB) {
		ArrayList<String> strListRet = new ArrayList<String>();
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		String key;
		int value=0;

		for (int i=0; i<strListA.size(); i++) {
			key = strListA.get(i);

			hash.put(key, value);
		}

		for (int i=0; i<strListB.size(); i++) {
			key = strListB.get(i);
			if (hash.containsKey(key))
				strListRet.add(key);
			hash.put(key, value);
		}

		return strListRet.size();
	}
	
	
	
	private String fileRead(String fileName) {
		Scanner scan;
		String retVal = "";
//		String scanLine;
//		StringTokenizer token;
//		ArrayList<String> retVal = new ArrayList<String>();
		try {
			scan = new Scanner(new File(".\\test\\"+fileName));
			while(scan.hasNext()) {
				retVal += scan.nextLine();
			}
			
			
//			while(scan.hasNext()) {
//				scanLine = scan.nextLine();
//				token = new StringTokenizer(scanLine, " ");
//				while (token.hasMoreTokens()) {
//					retVal.add(token.nextToken());
//				}
//			}

			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	private void fileWrite(String fileContent, String fileName) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(".\\test\\"+fileName));
			out.write(fileContent);
			
//			for (int i=0; i<fileContent.size(); i++) {
//				out.write(fileContent.get(i)+" ");
//			}
			out.close();
		} catch (IOException w) {
			System.err.println(w);
		}
	}
	
	
	
	
	
	
// No findStr???????????????????	
/*
	public String findStr(){
		
		return null;
	}
*/
	
}
