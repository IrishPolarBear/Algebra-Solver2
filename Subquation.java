import java.util.*;

public class SubQuation {
	private ArrayList<Term> listTerms;
	private ArrayList<String> termFunctions;
	private ArrayList<String> groupings;
	private ArrayList<String> groupCode;
	private ArrayList<Integer> groupExponents;
	private boolean test;
	private String newEquation;
	public String testValue;
	
	public SubQuation(){
		listTerms = new ArrayList<Term>(0);
		termFunctions = new ArrayList<String>(0);
		groupExponents = new ArrayList<Integer>(0);
	} 
	
	public SubQuation(String arg){
		listTerms = new ArrayList<Term>(0);
		termFunctions = new ArrayList<String>(0);
		groupings = new ArrayList<String>(0);
		groupCode = new ArrayList<String>(0);
		groupExponents = new ArrayList<Integer>(0);
		String delim2 = "[+\\-*/]+";
		
		test = checkParenthesis(arg);	
		if (test){
			newEquation = rewriteEquationDistribution(arg);
		}
		else {
			newEquation = arg;
		}
		getFirstFunctions(newEquation);
		String[] terms = newEquation.split(delim2);
		determineGroupings(terms);
		getFunctions(newEquation,terms);
		for (int i = 0; i < terms.length; i++){
			listTerms.add(new Term(terms[i]));
		}
	}

	private void getFirstFunctions(String sides){
		String subSide;
		
		subSide = sides.substring(0,1);
		if (subSide.equals("-")){
			termFunctions.add("-");
		} else {
			termFunctions.add("+");
		}
		//String subSide1, subSide2;
		//String test;
		//test = sides[0];
		//subSide1 = sides[0].substring(0,1);
		//subSide2 = sides[1].substring(0,1);
		
		//if (subSide1.equals("-")){
		//	leftFunctions.add("-");
		//}
		//else {
		//	leftFunctions.add("+");
		//}
		//if (subSide2.equals("-")){
		//	rightFunctions.add("-");
		//}
		//else {
		//	rightFunctions.add("+");
		//}
	}
	
	private void determineGroupings(String[] arg){
		int groupNum = 0, groupValue;
		boolean grouped = false;
		String[] code = {"a","b","c","d","e","f","g","h","i","j","k","l"};
		
		testValue = "";
		groupValue = groupCode.size();
		for(int i = 0; i < arg.length; i++){
			if (arg[i].contains("(")){
				grouped = true;
				//groupings.add(code[groupValue]);
			}
			testValue = testValue + arg[i] + "##" + String.valueOf(grouped);
			if (grouped){
				groupNum = groupNum + 1;
				groupings.add(code[groupValue]);
				testValue = testValue + "Yo";
			} else {
				groupings.add("null");
				testValue = testValue + "ho";
			}
			if (arg[i].contains(")")){
				grouped = false;
				String translatedValue = String.valueOf(groupNum) + code[groupValue];
				groupCode.add(translatedValue);
				groupExponents.add(1);
				groupNum = 0;
				groupValue = groupValue + 1;
			}
				
		}
	}
	
	public String returnTest(){
		int val;
		String returnValue;
		
		returnValue = "";
		val = groupings.size();
		for (int i = 0; i < val; i++){
			returnValue = returnValue + groupings.get(i);	
		}
		val = groupCode.size();
		for (int j = 0; j < val; j++){
			returnValue = returnValue + groupCode.get(j);
		}
		returnValue = returnValue + "\n";
		val = termFunctions.size();
		for (int k = 0; k < val; k++){
			returnValue = returnValue + termFunctions.get(k);
		}
		return returnValue;
	}
	
	private void getFunctions(String side, String[] values){
		int cursor = 0;
		char c;

		for (int i = 0; i < values.length-1; i++) {
			if (i == 0){
				cursor = values[i].length();
			} else {
				cursor = cursor + values[i].length() + 1;
			}
			if (values[i].length() > 0) {
				c = side.charAt(cursor);
				termFunctions.add(new String(Character.toString(side.charAt(cursor))));
			}
		}
	}

//	private void getFunctions(String side, String[] values, int k){
//		int cursor = 0;
//		String holder;
//		char c;		
//		
//		
//		for (int i = 0; i < values.length-1; i++){
//			if (i == 0){
//				cursor = values[i].length();
//			}
//			else {
//				cursor = cursor + values[i].length() + 1;
//			}
//			if (values[i].length() > 0) {
//				if (k == 0){
//					c = side.charAt(cursor);
//					leftFunctions.add(new String(Character.toString(side.charAt(cursor))));
//				}
//				else {
//					rightFunctions.add(new String(Character.toString(side.charAt(cursor))));
//				}
//			}
//		}
//	}

	private String rewriteEquationDistribution(String arg){
		int placement = 0, placement2 = 0;
		String nq;
		
		placement = arg.indexOf("(");
		nq = "";
		while (placement != -1){
			nq = nq + arg.substring(placement2,placement);
			nq = nq +"*";

			placement2 = arg.indexOf("(",placement+1);
			if (placement2 == -1) {
				nq = nq + arg.substring(placement);
			}
			else {
				nq = nq + arg.substring(placement,placement2);
			}
			placement = placement2;
		}
		return nq;		
	}
	
	private void distributiveProperty(){
		
	}
	
	//private boolean checkCompleteTerm(String arg){
//	}
	
//	private String removeOuterParenthesis(String arg){
//	}
	
	private boolean checkParenthesis(String arg){
		boolean found = false;
		int openParenthesis = 0, closeParenthesis = 0;
		int index = 0;
		
		index = arg.indexOf("(");
		while (index != -1){
			openParenthesis = openParenthesis + 1;
			index = arg.indexOf("(",index+1);
		}
		index = 0;
		index = arg.indexOf(")");
		while (index != -1) {
			closeParenthesis = closeParenthesis + 1;
			index = arg.indexOf(")",index+1);
		}
		
		if ((openParenthesis > 0) && (openParenthesis == closeParenthesis)){
			found = true;
		}
		return found;
		
	}
	
	public String breakIntoTerms(){
		String retValue;
		String delim2 = "[+\\-*/]+";
		
		retValue = "";
		String[] terms = newEquation.split(delim2);
		for (int i = 0; i < terms.length; i++){
			retValue = retValue + terms[i] + "##";
		}
		return retValue;
	}
	
	public boolean getBoolean(){
		return test;
	}
	
	public String getEquation(){
		return newEquation;
	}
}