/** The purpose of this class is to do the necessary algebraic functions to solve for the variable */
import java.util.*;
public class Algebra_Solver {
	public ArrayList<Equation> equations;
	
	/** Default constructor that will create the class object even if no arguments are passed.
	Parameters: N/A */
	public Algebra_Solver(){
		equations = new ArrayList<Equation>(0);		
	}
	
	/** Class constructor that will create the class object with equations; mostly used for multi-equations.
	Parameters: (ArrayList<Equations> arg), to be solved */
	public Algebra_Solver(ArrayList<Equation> arg){
		equations = new ArrayList<Equation>(arg.size());
		for(Equation e: arg){
			equations.add(e);
		}
	}

	/**Class constructor that will create the class object with a single equation.
	Parameters: (Equation e), to be solved.*/
	public Algebra_Solver(Equation e){
		equations = new ArrayList<Equation>(1);
		equations.add(e);
	}
	
	/**Public class function that begins the distributive function for the equation.
	Parameters: (String var), the grouping that should be selected and
	(int side) the side of the distributive terms 
	Return: VOID*/
	public void getDistributiveTerms(String var, int side){
		int groupSize, groupLocation, numGrouped;
		String groupDetermination, testValue;
		ArrayList<Term> termHolder;
		ArrayList<Term> Ts;
		ArrayList<String> Gs;
		Term multiplier;
		boolean found = false;
		Equation eq;

		termHolder = new ArrayList<Term>();
		groupLocation = 0;
		eq = equations.get(0);
		groupSize = eq.getGroupLength(side);
		Ts = eq.eqTerms.get(side);
		for (int i = 0; i < groupSize; i++){
			groupDetermination = eq.getGroupingValue(side, i);
			if (groupDetermination.equals(var) && !(found)){
				groupLocation = i;
				found = true;
			}
		}
		multiplier = Ts.get(groupLocation-1);
		testValue = multiplier.printTerm();
		numGrouped = eq.getCodeNumber(var);
		for (int i = groupLocation; i < groupLocation + 2; i++){
			termHolder.add(Ts.get(i));
		}
	
		distribute(multiplier, termHolder, groupLocation, var, side);
	}
	
	/**Private class function that continues the distributive function for the class
	Parameters: (Term distributor), the term distributing the terms within the equation, eg 3 of 3(4x+5)
	(ArrayList<Term> terms), the ArrayList of the terms to be distributed on, eg 4x and 5 of 3(4x+5)
	(int cursor), integer of where the cursor is at within the equation
	(String groupVar), String value of the grouping variable
	(int side), integer value of the side of the equation 
	Return: VOID*/
	private void distribute(Term distributor, ArrayList<Term> terms, int cursor, String groupVar, int side){
		int numTerms, newMagnitude, order = 1, directionMag1, directionMag2;
		String variable, tempVariable, newVariable, retValue;
		boolean undet;
		ArrayList<Term> newTerms;
		ArrayList<String> newFunctions;
		Equation eq;
			
		eq = equations.get(0);
		newMagnitude = 0;
		numTerms = terms.size();
		variable = distributor.getVariable();
		newTerms = new ArrayList<Term>();
		newFunctions = new ArrayList<String>();
		directionMag1 = eq.getFunctionValue(side,cursor-1);
		for (int i = 0; i < numTerms; i++){
			directionMag2 = eq.getFunctionValue(side,(cursor+i+1));
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
		eq.consolidateDistributiveTerms(newTerms, newFunctions,cursor,groupVar,side);
	}
	
	/**Public class function that begins to combine all like terms on the equation.
	This function will call upon itself, similar to being recursive, until all like terms are combined.
	Parameters: N/A 
	Return: VOID*/
	public void combineLikeTerms(){
		String var1, var2;
		String gr1, gr2, gr3;
		int count = 0, length, length2, grLimit;
		boolean brkLoop;
		
		brkLoop = false;
		length = 2;
		Equation eq = equations.get(0);
		for (int i = 0; i < length; i++){
			ArrayList<Term> t = eq.eqTerms.get(i);
			length2 = t.size();
			grLimit = eq.getGroupLength(i);
			for (int j = 0; j < length2-1; j++){
				var1 = t.get(j).getVariable();
				gr1 = eq.getGroupingValue(i,j);
				for (int k = j; k < length2; k++){
					gr2 = eq.getGroupingValue(i,k);
					if (k < grLimit-1) {
						gr3 = eq.getGroupingValue(i,k+1);
					} else {
						gr3 = gr2;
					}
					if (gr1.equals("NULL") && gr2.equals("NULL") && gr3.equals("NULL")){
						if ( j != k) {
							var2 = t.get(k).getVariable();
							if (var1.equals(var2)){
								newTerm(i,j,k,var1);
								brkLoop=true;
							}
						}
						if (brkLoop) {
							break;
						}
					}
				}
				if (brkLoop) {
					break;
				}
			}
			if (brkLoop) {
				break;
			}
		}
		if (brkLoop) {
			combineLikeTerms();
		}		
	}
	
	/**Public function that will isolate a variable onto the left side of the equation with the other terms on the right side of the equation.
	Parameters: (String var), value of the variable that needs to be isolated
	Return: VOID*/	
	public String runIsolateVariableTerm(String var){
		String compVar;
		Equation eq;
		boolean brkLoop;
		String check;
		int length, sides = 2;
				
		eq = equations.get(0);
		brkLoop = false;
		check = "";
		for (int i = 0; i < sides; i++) {
			ArrayList<Term> Ts = eq.eqTerms.get(i);
			length = Ts.size();
			for (int j = 0; j < length; j++){ //for loop that cycles through all of the terms
				compVar = Ts.get(j).getVariable();
				check = check + " " + compVar;
				if (compVar.equals(var)){
					if (i == 1) {
						eq.moveTerm(j,i);
						brkLoop = true;
					} 
				} else {  // moves terms to the left side if the term has the variable
					if (i == 0) {
						eq.moveTerm(j,i);
						brkLoop = true;
					}
				}
				if (brkLoop) {
					break;
				}
			}
			if (brkLoop) { //re-runs the function to keep cycling through all of the terms
				runIsolateVariableTerm(var);
			}
		}
	}

	//displays the equations
//	public String displayEquation(){
//		String display;
//		display = equations.get(0).displayEquation();
//		return display;
//	}
	
	/**Private function that is used by the combineLikeTerms function to create a new term that needs to be combined.
	Parameters: (int side), integer value of the side of the equation
	(int num), integer value of the first term to be combined
	(int num2), integer value of the second term to be combined
	(String var), String value of the like term component
	Return: VOID*/
	private void newTerm(int side, int num, int num2, String var){
		int newMagnitude, order, absMagnitude;
		boolean undet;
		Term t1, t2, newT;
		int dir1, dir2;
		
		Equation eq = equations.get(0);
		t1 = eq.eqTerms.get(side).get(num);
		t2 = eq.eqTerms.get(side).get(num2);
		
		dir1 = eq.getFunctionValue(side,num);
		dir2 = eq.getFunctionValue(side,num2);
		newMagnitude = (dir1*t1.getMagnitude()) + (dir2*t2.getMagnitude());
		
		undet = t1.getUndeterminate();
		order = t1.getOrder();
		
		absMagnitude = Math.abs(newMagnitude);
		
		newT = new Term(absMagnitude, var, undet, order);
		
		eq.consolidateTerms(side, num, num2, newT, newMagnitude);
	}
	
	//calls the function to print the debugging version of the equation
//	public String printEquation(){
//		String returnValue = "";
//		returnValue = equations.get(0).printEquation();
//		return returnValue;
//	}
                                                                                                                                                                                     
}