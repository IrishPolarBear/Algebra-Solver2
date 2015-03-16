import java.util.*;

public class SubQuation {
	private ArrayList<Term> listTerms;
	private ArrayList<String> termFunctions;
	private ArrayList<String> groupings;
	private HashMap<String, Integer> groupCode;
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
		groupCode = new HashMap<String,Integer>();
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
			if (terms[i].contains("(") && (terms[i].length() == 1)){
				continue;
			} 
			listTerms.add(new Term(terms[i]));
		}
	}
	
	public String printAllTerms(){
		int val;
		String checkValue;

		val = listTerms.size();
		checkValue = "";
		for (int i = 0; i < val; i++){
			checkValue = checkValue + listTerms.get(i).printTerm();
		}
		return checkValue;
	}

	private void getFirstFunctions(String sides){
		String subSide;
		
		subSide = sides.substring(0,1);
		if (subSide.equals("-")){
			termFunctions.add("-");
		} else {
			termFunctions.add("+");
		}
	}
	
	public String displayEquation(){
		String eq, sign, groupVar, varKey;
		int length, groupNum, funcNum;

		eq = "";
		eq = signCheck(0);
		length = groupings.size();
		groupNum = 0;
		funcNum = 0;
		for (int i = 0; i < length; i++){
			varKey = groupings.get(i);
			if (varKey.equals("NULL")){
				if (i == 0) {
					eq = eq + listTerms.get(i).getRepresentation();
					funcNum++;
				} else {
					eq = eq + termFunctions.get(funcNum) + listTerms.get(i).getRepresentation();
					funcNum++;
				}
			} else {
				if (groupNum == 0){
					eq = eq + "*(" + signCheck(funcNum+1) + listTerms.get(i).getRepresentation();
					funcNum += 2;
					groupNum = 1;
				} else {
					groupNum++;
					eq = eq + termFunctions.get(funcNum) + listTerms.get(i).getRepresentation();
					if (groupNum == groupCode.get(varKey)){
						eq = eq + ")";
					}
				}
			}
		}	
		
		return eq;
	}
	
	private String signCheck(int num){
		String sign, retValue;
		
		retValue = "";
		sign = termFunctions.get(num);
		if (sign.equals("-")){
			retValue = "-";
		}
		
		return retValue;
	}
	
	private void determineGroupings(String[] arg){
		int groupNum = 0, groupValue;
		boolean grouped = false;
		String[] code = {"a","b","c","d","e","f","g","h","i","j","k","l"};
		
		testValue = "";
		groupValue = 0;
		groupValue = groupCode.size();
		for(int i = 0; i < arg.length; i++){
			if (arg[i].contains("(")){
				grouped = true;
			}
			testValue = testValue + arg[i] + "##" + String.valueOf(grouped);
			if (grouped && !arg[i].equals("(")){
				groupNum = groupNum + 1;
				groupings.add(code[groupValue]);
				testValue = testValue + "Yo";
			} else {
				if (!arg[i].equals("(")){
					groupings.add("NULL");
					testValue = testValue + "ho";
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
	}
	
	public String returnTest(){
		int val;
		String returnValue;
		
		returnValue = "";
		val = groupings.size();
		for (int i = 0; i < val; i++){
			returnValue = returnValue + groupings.get(i);	
		}
		returnValue = returnValue + "\n";
		val = termFunctions.size();
		for (int k = 0; k < val; k++){
			returnValue = returnValue + termFunctions.get(k);
		}
		return returnValue;
	}
	
	public void getDistributiveTerms(String var){
		int groupSize, groupLocation, numGrouped;
		String groupDetermination;
		ArrayList<Term> termHolder;
		Term multiplier;
		boolean found = false;

		termHolder = new ArrayList<Term>();
		groupSize = groupings.size();
		groupLocation = 0;
		for (int i = 0; i < groupSize; i++){
			groupDetermination = groupings.get(i);
			if (groupDetermination.equals(var) && !(found)){
				groupLocation = i;
				found = true;
			}
		}
		multiplier = listTerms.get(groupLocation-1);
	//	testValue = multiplier.printTerm();
		numGrouped = groupCode.get(var);
		for (int i = groupLocation; i < groupLocation + 2; i++){
			termHolder.add(listTerms.get(i));
		}
	//	testValue = testValue + "$$" + String.valueOf(numGrouped);
	//	for (int i = 0; i < termHolder.size(); i ++){
	//		testValue = testValue + termHolder.get(i).printTerm();
	//	}
	
		distribute(multiplier, termHolder, groupLocation, var);
		
	//	return testValue;
	}
	
	private void distribute(Term distributor, ArrayList<Term> terms, int cursor, String groupVar){
		int numTerms, newMagnitude, order = 1, directionMag1, directionMag2;
		String variable, tempVariable, newVariable, retValue;
		boolean undet;
		ArrayList<Term> newTerms;
		ArrayList<String> newFunctions;
		
		newMagnitude = 0;
		numTerms = terms.size();
		variable = distributor.getVariable();
		newTerms = new ArrayList<Term>();
		newFunctions = new ArrayList<String>();
		directionMag1 = getFunctionValue (cursor-1);
		for (int i = 0; i < numTerms; i++){
			directionMag2 = getFunctionValue(cursor + i + 1);
			newMagnitude = ((directionMag1 * distributor.getMagnitude()) * (directionMag2 * terms.get(i).getMagnitude()));
			tempVariable = terms.get(i).getVariable();
			if (variable.equals("NULL")){
				if (tempVariable.equals("NULL")){
					newVariable = "NULL";
				} else {
					newVariable = tempVariable;
				}
			} else {
				if (tempVariable.equals("NULL")){
					newVariable = variable;
				} else {
					newVariable = variable + tempVariable;
				}
			}
			if (newVariable.equals("NULL")){
				undet = false;
			} else {
				undet = true;
			}
			if (newMagnitude < 0) {
				newFunctions.add("-");
				newMagnitude = Math.abs(newMagnitude);
			} else {
				newFunctions.add("+");
			}
			newTerms.add(new Term(newMagnitude,newVariable,undet,order));		
		}
		
		numTerms = newTerms.size();
	//	retValue = "";
	//	for (int i = 0; i < numTerms; i++) {
	//		retValue = retValue + newTerms.get(i).printTerm();
	//	}
		consolidateTerms(newTerms, newFunctions,cursor, groupVar);
	//	return retValue;
	}
	
	private void consolidateTerms(ArrayList<Term> newTerms, ArrayList<String> newFunctions, int cursor, String groupVar){
		int num;
		
		num = newTerms.size();		
		removeFunctions(cursor, num);
		removeTerms(cursor, num);
		removeGroupingVariable(groupVar, cursor, num);
		for (int i = 0; i < num; i++){
			listTerms.add(newTerms.get(i));
			termFunctions.add(newFunctions.get(i));
			groupings.add("NULL");
		}
		
	}
	
	private void removeGroupingVariable(String groupVar, int cursor, int num){
		int length, funcNum;
		
		length = num + 1;
		funcNum = cursor - 1;
		groupCode.remove(groupVar);
		for (int i = 0; i < length; i++){
			groupings.remove(funcNum);	
		}
	}
	
	private void removeTerms(int cursor, int num){
		int length, funcNum;
		
		length = num + 1;
		funcNum = cursor -1;
		for (int i = 0; i < length; i++){
			listTerms.remove(funcNum);
		}
	}
		
	private void removeFunctions(int cursor, int num){
		int length, funcNum;
		
		length = num + 2;
		funcNum = cursor - 1;
		for (int i = 0; i < length; i ++){
			termFunctions.remove(funcNum);
		}
	}
	
	private void getFunctions(String side, String[] values){
		int cursor = 0, distributeCheck;
		char c;
		String value;
		
		testValue = "";
		for (int i = 0; i < values.length-1; i++) {
			if (i == 0){
				cursor = values[i].length();
			} else {
				cursor = cursor + values[i].length() + 1;
			}
			testValue = testValue + values[i] + "$$" + values[i+1] + "##";
			if (values[i].length() > 0) {
				if ((values[i+1].contains("(")) && (values[i+1].length() == 1)){
						termFunctions.add("*");
						termFunctions.add("-");
						i++;
						cursor = cursor + values[i+1].length();
						testValue = testValue + "HERE";
				} else {
					if (values[i+1].contains("(")){
						termFunctions.add("*");
						termFunctions.add("+");
						testValue = testValue + "BADA";
					} else {
						c = side.charAt(cursor);
						termFunctions.add(new String(Character.toString(side.charAt(cursor))));
						testValue = testValue + "BING";
					}
				}
			}
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
	
	public int getFunctionValue(int num){
		String func;
		
		func = termFunctions.get(num);
		if (func.equals("-")){
			return -1;
		}
		else{
			return 1;
		}
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