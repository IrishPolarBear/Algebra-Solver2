/** The purpose of this class is to organize the terms into a form that the computer will be interpret as an equation. This class also provides functions that not only hold the equations and return it the full equation, but also some necessary functions to help isolate the variables */
import java.util.*;
public class Equation {
	// class variables;
	public ArrayList<ArrayList<Term>> eqTerms;
	private ArrayList<ArrayList<String>> eqFunctions;
	private ArrayList<ArrayList<String>> eqGroupings;
	private ArrayList<Integer> groupExponents;
	private HashMap<String,Integer> groupCode;
	public boolean init;
	public String returnValue;
	private String newEquation;
	public String testValue;
	
	/** Default constructor that will create the class object even if no arguments are passed.
	Parameters: N/A */
	public Equation(){
		init = false;
	}
	
	/**Class constructor that will create the class object with the equation variable
	Parameters: (String arg), string value of the equation */
	public Equation(String arg){
		init = true;
		String delim1 = "[=]";
		String delim2 = "[+\\-*/]+";
		boolean parenthesisCheck;
		int length;
		
		//initializes the ArrayLists
		groupCode = new HashMap<String,Integer>();
		groupExponents = new ArrayList<Integer>();
		eqTerms = new ArrayList<ArrayList<Term>>();
		eqFunctions = new ArrayList<ArrayList<String>>();
		eqGroupings = new ArrayList<ArrayList<String>>();

		parenthesisCheck = checkParenthesis(arg);
		if (parenthesisCheck){
			newEquation = rewriteEquationDistribution(arg);
		}else{
			newEquation = arg;
		}
		//checkTerms();
		String[] sides = newEquation.split(delim1);
		for (int i = 0; i < sides.length; i++){
			String[] terms = sides[i].split(delim2); //splits the terms into an array, one for each side
			determineGroupings(terms, i);
			getNewFunctions(sides[i],terms,i);
			ArrayList<Term> Ts = new ArrayList<Term>();
			length = terms.length;
			for (int j = 0; j < length; j++){
				if (terms[j].length() > 0) {
					Ts.add(new Term(terms[j]));
				}
			}
			eqTerms.add(Ts);
		}
	}
	
	/**Private function that is used by a constructor to determine the groupings within the distribution set
	Parameters: (String[] arg), String array of all of the terms
	(int num), integer value of the number of sides, almost always will be 2 (might be always)
	Return: VOID*/
	private void determineGroupings(String[] arg, int num){
		int groupNum = 0, groupValue;
		boolean grouped = false;
		String[] code = {"a","b","c","d","e","f","g","h","i","j","k","l"};
		
		groupValue = 0;
		groupValue = groupCode.size();
		ArrayList<String> gc = new ArrayList<String>();
		for(int i = 0; i < arg.length; i++){
			if (arg[i].contains("(")){
				grouped = true;
			}
			if (grouped && !arg[i].equals("(")){
				groupNum = groupNum + 1;
				gc.add(code[groupValue]);
			} else {
				if (!arg[i].equals("(")){
					gc.add("NULL");
				}
			}
			if (arg[i].contains(")")){
				grouped = false;
				groupCode.put(code[groupValue], new Integer(groupNum));
				groupExponents.add(1);
				groupNum = 0;
				groupValue = groupValue + 1;
			}
				
		}
		eqGroupings.add(gc);
	}
	
	/**Private function that is used to rewrite the equation into a form for the class to use; used if distributor set is found
	Parameters: (String arg), string value of the old equation
	Return: String, string value of the newly reformatted equation*/
	private String rewriteEquationDistribution(String arg){
		int placement = 0, placement2 = 0, cursor;
		String nq, nt = "";
		
		placement = arg.indexOf("(");
		nq = "";
		while (placement != -1){
			cursor = placement - 1;
			if (arg.charAt(cursor) == '-'){
				nq = nq + arg.substring(placement2,placement) +"1";
			} else {
				nq = nq + arg.substring(placement2,placement);
			}
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

	/**Private function that checks to see if there are parenthesis in the equation
	Parameters: (String arg), String value of the equation
	Return: Boolean, returns true if any found, false otherwise */
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
	
	/**Private function that is use do get the first function of the equation
	Parameters: (String value), string value of a side of the equation
	Return: String, string value of the first function, positive or negative */
	private String getFirstFunction (String value){
		String subSide, retValue;
		
		subSide = value.substring(0,1);
		if (subSide.equals("-")){
			retValue = "-";
		} else {
			retValue = "+";
		}
		
		return retValue;
	}
	
	/**Private function that gets all of the functions within the equation
	Parameters:(String side), string value of a side of the equation
	(String[] values, all of the terms in a string array value
	(int k) numeric value of the side
	Return: VOID */
	private void getNewFunctions(String side, String[] values, int k){
		int cursor = 0, length;
		String value, subSide;
		char c;
		
		ArrayList<String> st = new ArrayList<String>();
		if (values.length == 1){
			value = getFirstFunction(side);
			st.add(value);
			length = 0;
		} else {
			length = values.length - 1;
		}
		for (int i = 0; i < length;i++){
			if (i == 0) {
				cursor = values[i].length();
				value = getFirstFunction(side);
				st.add(value);
			} else {
				cursor = cursor + values[i].length()+1;
			}
			if (values[i].length() > 0){
				if ((values[i+1].contains("(")) && (values[i+1].length()==1)){
					st.add("*");
					st.add("-");
					i++;
					cursor = cursor + values[i+1].length();
				} else {
					if (values[i+1].contains("(")) {
						st.add("*");
						st.add("+");
					} else {
						c = side.charAt(cursor);
						st.add(new String(Character.toString(side.charAt(cursor))));
					}
				}
			}			
		}
		eqFunctions.add(st);
	}
	
	/**Public function that is used to get the length of the groupings on one side
	Parameters: (int side), numerical value of the side
	Return: Integer value of the length of the groupings ArrayList*/
	public int getGroupLength(int side){
		int num;
		ArrayList<String> Gs;
		
		Gs = eqGroupings.get(side);
		num = Gs.size();
		
		return num;
	}
	
	/**Public function that gets a eqGrouping value at a specific place on specifc side of the equation
	Parameters: (int side), numerical value of the side of the equation
	(int num), numerical value of the grouping location
	Return: String value of the grouping at specified place*/
	public String getGroupingValue(int side, int num){
		String value;
		ArrayList<String> Gs;
		
		Gs = eqGroupings.get(side);
		value = Gs.get(num);
		
		return value;
	}
	
	/**Public function that returns the numerical value of the groupCode, ie how many erms wiht the grouping in the equation
	Parameters: (String var), the string value of the group code
	Return: numerical value of the groupings */
	public int getCodeNumber(String var){
		int num;
		
		num = groupCode.get(var);
		
		return num;
	}
	
	/**Public function that is used to consolidate the terms of the equation, elminates two and adds a new one
	Parameters: (int side), integer value of the side
	(int num), numerical value of the first term to be removed
	(int num2), numerical value of the second term to be removed
	(Term t), term object to replace the two to be removed
	(int mag), essentialy, is it positive or negative term
	Return: VOID*/
	public void consolidateTerms(int side, int num, int num2, Term t, int mag) {
		ArrayList<Term> Ts;
		ArrayList<String> Fs;

		Ts = eqTerms.get(side);
		
		Ts.remove(num);
		Ts.remove(num2-1);
		Ts.add(t);
		     
		Fs = eqFunctions.get(side);
		
		Fs.remove(num);
		Fs.remove(num2-1);
		
		if (mag < 0) {
			Fs.add("-");
		} else {
			Fs.add("+");
		}
		testValue = testingApplication();
	}
	
	/**Public function that will move the terms from one side to the other
	Parameters: (int num), numerical value of the term to be moved
	(int side), numerical value of the side equation to move the equation from 
	Return: VOID*/
	public void moveTerm(int num, int side){
		Term t;
		ArrayList<Term> Ts,ATs;
		ArrayList<String> Fs, AFs;
		int antiSide;
		String func;

		if (side == 0) {
			antiSide = 1;
		} else {
			antiSide = 0;
		}
		
		Ts = eqTerms.get(side);
		ATs = eqTerms.get(antiSide);
		t = Ts.get(num);
		Ts.remove(num);
		ATs.add(t);
		Fs = eqFunctions.get(side);
		AFs = eqFunctions.get(antiSide);
		func = Fs.get(num);
		Fs.remove(num);
		if (func.equals("+")) {
			AFs.add("-");
		} else {
			AFs.add("+");
		}
	}
	
	/**Public function that is used to get specific test values of the application
	Parameters: NONE 
	Return: the String value of what I want to see to do my testing*/
	public String testingApplication(){
		String retValue;
		int length, newLength;
	
		retValue = "";
		
		length = eqTerms.size();
		retValue = retValue + "\n\nNEW TERMS=";
		for (int i = 0; i < length; i++){
			ArrayList<Term> Ts = eqTerms.get(i);
			newLength = Ts.size();
			for (int j = 0; j < newLength; j++){
				retValue = retValue + Ts.get(j).getRepresentation()+"##";
			}
			retValue = retValue + " SOMETHING ";
		}
		length = eqFunctions.size();
		retValue = retValue + "\n\nNew Functions=";
		for (int i = 0; i <length; i++){
			ArrayList<String> st = eqFunctions.get(i);
			newLength = st.size();
			for (int j = 0; j < newLength; j++){
				retValue = retValue + st.get(j) + "$$";
			}
			retValue = retValue + "DIDDY";
		}
		length = eqGroupings.size();
		retValue = retValue + "\n\nNEW GROUPINGS";
		for (int i = 0; i < length; i++){
			ArrayList<String> gc = eqGroupings.get(i);
			newLength = gc.size();
			for (int j = 0; j < newLength; j++){
				retValue = retValue + gc.get(j) + "@@";
			}
			retValue = retValue + " BOOB";
		}
		return retValue;
	}
	
	/* returns a string of the equation in an easy to read format */
//	public String displayEquation(){
//		String eq, sign;
//		
//		eq = "";
//		sign = leftFunctions.get(0);
//		if (sign.equals("-")){
//			eq = sign;
//		}
//		for (int i = 0; i < leftTerms.size(); i ++){
//			if (i == 0){
//				eq=eq+leftTerms.get(i).getRepresentation();
//			}
//			else{
//				eq =eq + leftFunctions.get(i) + leftTerms.get(i).getRepresentation();
//			}
//		}
//		eq = eq + "=";
//		sign = rightFunctions.get(0);
//		if (sign.equals("-")){
//			eq = eq + sign;
//		}
//		for (int j = 0; j < rightTerms.size(); j ++){
//			if (j == 0){
//				eq = eq+rightTerms.get(j).getRepresentation();
//			}
//			else {
//				eq = eq + rightFunctions.get(j)+rightTerms.get(j).getRepresentation();
//			}
//		}
//		return eq;
//	}
	
	/**Public function that consolidates the distribution terms
	Parameters: (ArrayList<Term> newTerms), ArrayList of Terms to be added
	(ArrayList<String> newFunctions), ArrayList of new functions to be added
	(int cursor), value of the cursor
	(String groupVar), String value of the grouping variable that will be eliminated
	(int side), numerical value of the side of the equation
	Return: VOID */
	public void consolidateDistributiveTerms(ArrayList<Term> newTerms, ArrayList<String> newFunctions, int cursor, String groupVar, int side){
		int num, length, funcNum;
		ArrayList<Term> Ts;
		ArrayList<String> Fs;
		ArrayList<String> Gs;
		
		Ts = eqTerms.get(side);
		Fs = eqFunctions.get(side);
		Gs = eqGroupings.get(side);
		num = newTerms.size();	
		length = num + 1;
		funcNum = cursor-1;
		groupCode.remove(groupVar);
		for (int i = 0; i < length; i++) {
			Gs.remove(funcNum);
			Ts.remove(funcNum);
		}
		length = num + 2;
		for (int i = 0; i < length; i++){
			Fs.remove(funcNum);
		}
		for (int i = 0; i < num; i++){
			Ts.add(newTerms.get(i));
			Fs.add(newFunctions.get(i));
			Gs.add("NULL");
		}
		
	}
	
	/**Public function that is used to get the directional value of the term
	Parameters: (int side), numerical value of the side
	(int num), numerical value of the place in the terms
	Return: -1 or 1 if its negative or positive */
	/* determines if the term is a negative or not */
	public int getFunctionValue(int side, int num){
		String func;
		
		func = eqFunctions.get(side).get(num);
		if (func.equals("+")){
			return 1;
		}
		else{
			return -1;
		}
	}
}