/** The purpose of this class is to organize the terms into a form that the computer will be interpret as an equation. This class also provides functions that not only hold the equations and return it the full equation, but also some necessary functions to help isolate the variables */
import java.util.*;
public class Equation {
	// class variables;
	public ArrayList<Term> leftTerms;
	public ArrayList<Term> rightTerms;
	private ArrayList<String> leftFunctions;
	private ArrayList<String> rightFunctions;
	public boolean init;
	public String returnValue;
	
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
		
		//initializes the ArrayLists
		leftTerms = new ArrayList<Term>();
		rightTerms = new ArrayList<Term>(); 
		leftFunctions = new ArrayList<String>();
		rightFunctions = new ArrayList<String>();
		
		parenthesisCheck = checkParenthesis(arg);
		if (parenthesisCheck){
			newEquation = rewriteEquation(arg);
		}else{
			newEquation = arg;
		}
		//String[] sides = newEquation.split(delim1);
		//getFirstFunctions(sides);
		//for (int i = 0; i < sides.length; i++){
		//	String[] terms = sides[i].split(delim2); //splits the terms into an array, one for each side
		//	getFunctions(sides[i],terms,i);
		//	for (int j = 0; j < terms.length; j++){
		//		if (i == 0) {
		//			if (terms[j].length() > 0){
		//				leftTerms.add(new Term(terms[j]));
		//			}
		//		}
		//		else{
		//			if (terms[j].length() > 0){
		//				rightTerms.add(new Term(terms[j]));
		//			}
		//		}
		//	}
		//}
	}
	
	/* function that gets the functions for the beginning of each side of the equation */
	private void getFirstFunctions(String[] sides){
		String subSide1, subSide2;
		String test;
		test = sides[0];
		subSide1 = sides[0].substring(0,1);
		subSide2 = sides[1].substring(0,1);
		
		if (subSide1.equals("-")){
			leftFunctions.add("-");
		}
		else {
			leftFunctions.add("+");
		}
		if (subSide2.equals("-")){
			rightFunctions.add("-");
		}
		else {
			rightFunctions.add("+");
		}
	}
	
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

	/* gets the functions for the other terms */
	private void getFunctions(String side, String[] values, int k){
		int cursor = 0;
		String holder;
		char c;
		
		for (int i = 0; i < values.length-1; i++){
			if (i == 0){
				cursor = values[i].length();
			}
			else {
				cursor = cursor + values[i].length() + 1;
			}
			if (values[i].length() > 0) {
				if (k == 0){
					c = side.charAt(cursor);
					leftFunctions.add(new String(Character.toString(side.charAt(cursor))));
				}
				else {
					rightFunctions.add(new String(Character.toString(side.charAt(cursor))));
				}
			}
		}
	}
	
	/* prints the current equation in a debugging format */
	public String printEquation(){
		String data;
		data = "";
		for (Term t: leftTerms){
			data = data + t.printTerm();
		}
		data = data + "OTHERSIDE";
		for (Term u: rightTerms){
			data = data + u.printTerm();
		}
		data = data + "FUNCTIONS";
		for (String s: leftFunctions){
			data = data + s;
		}
		data = data + "OTHERSIDE";
		for (String r: rightFunctions){
			data = data + r;	
		}
		return data;
	}
	
	/* used to during the consolidation process. removes 2 terms and then adds the new one */
	public void consolidateLeftTerms(int num, int num2, Term t, int mag){
		leftTerms.remove(num);
		leftTerms.remove(num2-1);
		leftFunctions.remove(num);
		leftFunctions.remove(num2-1);
		leftTerms.add(t);
		if (mag < 0){
			leftFunctions.add("-");
		}	
		else{
			leftFunctions.add("+");
		}
	}
	
	public void consolidateRightTerms(int num, int num2, Term t,int mag){
		rightTerms.remove(num);
		rightTerms.remove(num2-1);
		rightTerms.add(t);
		rightFunctions.remove(num);
		rightFunctions.remove(num2-1);
		if (mag < 0) {
			rightFunctions.add("-");
		}
		else {
			rightFunctions.add("+");
		}
	}
	
	/* moves term from the left side to the right side of the equation or vice versa */
	public void moveTerm(int num, String str){
		Term t;
		String func;

		if (str.equals("left")){
			t = leftTerms.get(num);
			leftTerms.remove(num);
			rightTerms.add(t);
			func = leftFunctions.get(num);
			leftFunctions.remove(num);
			if (func.equals("+")) {
				rightFunctions.add("-");
			}
			else {	
				rightFunctions.add("+");
			}
		}
		else {
			t = rightTerms.get(num);
			rightTerms.remove(num);
			leftTerms.add(t);
			func = rightFunctions.get(num);
			rightFunctions.remove(num);
			if (func.equals("+")){
				leftFunctions.add("-");
			}
			else {
				leftFunctions.add("+");
			}
		}	
	}
	
	/* returns a string of the equation in an easy to read format */
	public String displayEquation(){
		String eq, sign;
		
		eq = "";
		sign = leftFunctions.get(0);
		if (sign.equals("-")){
			eq = sign;
		}
		for (int i = 0; i < leftTerms.size(); i ++){
			if (i == 0){
				eq=eq+leftTerms.get(i).getRepresentation();
			}
			else{
				eq =eq + leftFunctions.get(i) + leftTerms.get(i).getRepresentation();
			}
		}
		eq = eq + "=";
		sign = rightFunctions.get(0);
		if (sign.equals("-")){
			eq = eq + sign;
		}
		for (int j = 0; j < rightTerms.size(); j ++){
			if (j == 0){
				eq = eq+rightTerms.get(j).getRepresentation();
			}
			else {
				eq = eq + rightFunctions.get(j)+rightTerms.get(j).getRepresentation();
			}
		}
		return eq;
	}
	
	/* determines if the term is a negative or not */
	public int getFunctionValue(int num, String side){
		String func;
		if (side.equals("left")){
			func = leftFunctions.get(num);
		}
		else{
			func = rightFunctions.get(num);
		}
		if (func.equals("+")){
			return 1;
		}
		else{
			return -1;
		}
	}
}