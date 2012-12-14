import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Driver {

	/**
	 * @param args
	 */

	public static boolean DEBUG = false;
	public static boolean LLONE_DEBUG = false;
	public static boolean LEX_PARSE_DEBUG = false;
	public static boolean DEBUG_TOKENC = false;
  
	
	
	/**
	 * Main Driver for the application
	 * 
	 * @param args 0 - test dir,
	 */
	public static void main(String[] args) {
    LLoneGenerator leftOne;
    Tokenizer t;
    String testDir = "";
    
   //System.out.println(System.getProperty("user.dir"));
    
    if(args.length == 1)
    	testDir = args[0];
    
    //Load up script in testDir
    File scriptFile = new File(testDir + "script.txt");
    Scanner scriptScanner;
    String scriptStr = "";
	try {
		scriptScanner = new Scanner(scriptFile);
		while(scriptScanner.hasNextLine())
	    {
	    	scriptStr += scriptScanner.nextLine();
	    }
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    leftOne = new LLoneGenerator(testDir + "miniRE_spec.txt");
  
	
// Interpretor Test START
		//Interpretor inter = new Interpretor();
		
//		inter.replace("d", "xxx", "sanghun_test_input1.txt", "sanghun_test_output1.txt");
//		inter.recursivereplace("a", "ax", "sanghun_test_input1.txt", "sanghun_test_output2.txt");
//		inter.maxfreqstring("testset", "testget");
//		inter.setID("ID1", inter.getID("testget"));

//		ArrayList<String> testIDs = new ArrayList<String> ();
//		testIDs.add("testget");
//		testIDs.add("testset");
//		testIDs.add("IDtest");
//		inter.print(testIDs,null,null);
		
//		System.out.println("get ID length : " + inter.getIDLength("testget"));
		
//		System.out.println("find regex [a-z]+ : " + inter.findRegex("[a-z]+", "sanghun_test_input1.txt"));
		
//		System.out.println("diff(testget,testset) : " + inter.diff(inter.getID("testget"), inter.getID("testset")));
//		System.out.println("union(testget,testset) : " + inter.union(inter.getID("testget"), inter.getID("testset")));
//		System.out.println("inters(testget,testset) : " + inter.inters(inter.getID("testget"), inter.getID("testset")));
// Interpretor Test END		
		
		
		
// 		MiniREParser parser = new MiniREParser();
// 		parser.parse("begin\n " +
// 				"matches = find \'[A-Z a-z]*ment[A-Z a-z]*\' in \"file1.txt\" inters find \'(A|a) [A-Z a-z]*\' in \"file2.txt\";\n" +
// 				"n_matches = #matches;\n" +
// 				"print (n_matches);\n" +
// 				"replace '[A-Z a-z]*ment' with \"\" in \"file1.txt\" >! \"file3.txt\";\n" +
// 				"end");

//		System.out.println();
//		System.out.println(output);
// 		for(int i = 1; i<=7; i++){
// 			String test_case = Integer.toString(i);			
//  			t = new Tokenizer("C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\input","C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\spec");
// 			String output = t.parse();
// 			System.out.println(output);
// 		}

		MiniREParser parser = new MiniREParser();
//		TreeNode root = parser.parse("begin\n " +
//		"matches = find \'[A-Z a-z]*ment[A-Z a-z]*\' in \"file1.txt\" inters find \'[A-Z a-z]*ment[A-Z a-z]*\' in \"file2.txt\";\n" +
//		"n_matches = #matches;\n" +
//		"print (n_matches);\n" +
//		"print (matches);\n" +
//		"replace '[A-Z a-z]*ment' with \"waffle\" in \"file1.txt\" >! \"file3.txt\";\n" +
//		"end");
		
		TreeNode root = parser.parse(scriptStr);
		
//		System.out.println("");
//		System.out.println("*****************");
//		System.out.println("Testing Executor");
//		System.out.println("*****************");
//		System.out.println("");
		Executor ex = new Executor(root, testDir);

//		System.out.println();
//		System.out.println(output);
//		for(int i = 1; i<=7; i++){
//			String test_case = Integer.toString(i);			
// 			t = new Tokenizer("C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\input","C:\\Users\\Andrew\\workspace\\Parser\\test_inputs\\"+test_case+"\\spec");
//			String output = t.parse();
//			System.out.println(output);
//		}

	}

}
