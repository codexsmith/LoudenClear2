import java.util.ArrayList;

/**
 * 
 * @author nick
 *
 */


public class ParseTree {
	//examples. we must generate these from the input file
	public enum DIGIT {ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE}
	public enum NONZERO {ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE}
	public enum SMALLCASE {a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,s,t,u,v,w,x,y,z}
	public enum UPPERCASE {A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,X,Y,Z}
	public enum LETTER {SMALLCASE, UPPERCASE}
//	IDENTIFIER = LETTER (LETTER|DIGIT)*

	
	public enum NODE {STMT,EXP}
	public enum EXP {Op,Const,Id}
	public enum STMT {}
	
	
	private static parseNode treeHead;
	
	
	
	public static ArrayList<String> characterClass(String line){
		ArrayList<String> charClass = null;
		
		return charClass;
	}
	
	public static ArrayList<String> tokenClass(String line){
		ArrayList<String> tokenClass = null;
		
		return tokenClass;
	}
	

	//parse tree is composed of parseNodes
	private class parseNode{
		//list[nuChildren] of children
		//
		int lineno;
		
	}
	
}
