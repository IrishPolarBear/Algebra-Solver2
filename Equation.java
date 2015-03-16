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
	
	//default constructor
	public Equation(){
		init = false;
	}
	
	//constructor with the string of the equation argument
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
	
	
	/* prints the current equation in a debugging format */
//	public String printEquation(){
//		String data;
//		data = "";
//		for (Term t: leftTerms){
//			data = data + t.printTerm();
//		}
//		data = data + "OTHERSIDE";
//		for (Term u: rightTerms){
//			data = data + u.printTerm();
//		}
//		data = data + "FUNCTIONS";
//		for (String s: leftFunctions){
//			data = data + s;
//		}
//		data = data + "OTHERSIDE";
//		for (String r: rightFunctions){
//			data = data + r;	
//		}
//		return data;
//	}
	
	/* used to during the consolidation process. removes 2 terms and then adds the new one */
//	public void consolidateLeftTerms(int num, int num2, Term t, int mag){
//		leftTerms.remove(num);
//		leftTerms.remove(num2-1);
//		leftFunctions.remove(num);
//		leftFunctions.remove(num2-1);
//		leftTerms.add(t);
//		if (mag < 0){
//			leftFunctions.add("-");
//		}	
//		else{
//			leftFunctions.add("+");
//		}
//	}
	
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
	
//	public void consolidateRightTerms(int num, int num2, Term t,int mag){
//		rightTerms.remove(num);
//		rightTerms.remove(num2-1);
//		rightTerms.add(t);
//		rightFunctions.remove(num);
//		rightFunctions.remove(num2-1);
//		if (mag < 0) {
//			rightFunctions.add("-");
//		}
//		else {
//			rightFunctions.add("+");
//		}
//	}
	
	/* moves term from the left side to the right side of the equation or vice versa */
//	public void moveTerm(int num, String str){
//		Term t;
//		String func;
//
//		if (str.equals("left")){
//			t = leftTerms.get(num);
//			leftTerms.remove(num);
//			rightTerms.add(t);
//			func = leftFunctions.get(num);
//			leftFunctions.remove(num);
//			if (func.equals("+")) {
//				rightFunctions.add("-");
//			}
//			else {	
//				rightFunctions.add("+");
//			}
//		}
//		else {
//			t = rightTerms.get(num);
//			rightTerms.remove(num);
//			leftTerms.add(t);
//			func = rightFunctions.get(num);
//			rightFunctions.remove(num);
//			if (func.equals("+")){
//				leftFunctions.add("-");
//			}
//			else {
//				leftFunctions.add("+");
//			}
//		}	
//	}
	
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