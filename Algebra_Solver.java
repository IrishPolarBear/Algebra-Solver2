/** The purpose of this class is to do the necessary algebraic functions to solve for the variable */
import java.util.*;
public class Algebra_Solver {
	public ArrayList<Equation> equations;
	
	//default constructor
	public Algebra_Solver(){
		equations = new ArrayList<Equation>(0);		
	}
	
	//constructor that will take an array list of equations
	public Algebra_Solver(ArrayList<Equation> arg){
		equations = new ArrayList<Equation>(arg.size());
		for(Equation e: arg){
			equations.add(e);
		}
	}

	//constructor that will take only one equation
	public Algebra_Solver(Equation e){
		equations = new ArrayList<Equation>(1);
		equations.add(e);
	}
	
	public void combineLikeTerms(){
		Equation item;
		String var1, var2;
		int count = 0, length, length2;
		boolean brkLoop;
		
		brkLoop = false;
		length = 2;
		Equation eq = equations.get(0);
		for (int i = 0; i < length; i++){
			ArrayList<Term> t = eq.eqTerms.get(i);
			length2 = t.size();
			for (int j = 0; j < length2; j++){
				var1 = t.get(j).getVariable();
				for (int k = j; k < length2; k++){
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
	
	//runs te isolatevaraibleterm function for each side
//	public String isolateVariableTerm(String var){
//		String check;
//
//		check = runIsolateVariableTerm(var,"left");
//		check = runIsolateVariableTerm(var,"right");
//		combineLikeTerms();
//		
//		return check;
//	}
	
	//the purpose of this function is to move the term to solve for to the left and the other terms to the right side of the equation	
//	private String runIsolateVariableTerm(String var, String side){
//		ArrayList<Term> terms;
//		String compVar;
//		Equation item;
//		boolean brkLoop;
//		String check;
//				
//		item = equations.get(0);
//		terms = getTerms(side);
//		brkLoop = false;
//		check = "";
//		for (int i = 0; i < terms.size(); i++){ //for loop that cycles through all of the terms
//			compVar = terms.get(i).getVariable();
//			check = check + " " + compVar;
//			if (compVar.equals(var)){ 
//				check = check + "here";
//				if (side.equals("right")) { //moves terms to the right side if the variable is not present
//					item.moveTerm(i,side);
//					brkLoop = true;
//					check = check + " right";
//				}
//			}
//			else {  // moves terms to the left side if the term has the variable
//				if (side.equals("left")) {
//					item.moveTerm(i,side);
//					brkLoop = true;
//					check = check + " left";
//				}
//			}
//			if (brkLoop) {
//				break;
//			}
//		}
//		if (brkLoop) { //re-runs the function to keep cycling through all of the terms
//			runIsolateVariableTerm(var,side);
//		}
//		return check;
//	}
	
	//displays the equations
//	public String displayEquation(){
//		String display;
//		display = equations.get(0).displayEquation();
//		return display;
//	}

//	private void runCombineLikeTerms(String side){
//		Equation item;
//		String var1, var2;
//		int count = 0;
//		boolean brkLoop;
//		ArrayList<Term> terms;
//		
//		brkLoop = false;
//		terms = getTerms(side);
//		for(int i = 0;i < terms.size() - 1;i++){
//			var1 = terms.get(i).getVariable();
//			for (int j = i; j < terms.size();j++){
//				if ( i != j){
//					var2 = terms.get(j).getVariable();
//					if (var1.equals(var2)){
//						newTerm(side, i, j, var1);
//						brkLoop = true;
//					}
//				}
//				if (brkLoop){
//					break;
//				}
//			}
//			if (brkLoop){
//				break;
//			}
//		}
//		if (brkLoop){
//			combineLikeTerms();
//		}
//	}
	
	//used by the combineliketerms function to combine the terms
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