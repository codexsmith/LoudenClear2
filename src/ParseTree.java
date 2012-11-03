import java.util.ArrayList;

/**for regex
 * 
 * @author nick
 *
 */


public class ParseTree {
	
	public enum NODE {STMT,EXP}
	public enum EXP {Op,Const,Id}
	public enum STMT {}
	
	
	private static parseNode treeHead;
	
	//parse tree is composed of parseNodes
	private class parseNode{
		//list[nuChildren] of children
		//
		int lineno;
		
	}
	
}
