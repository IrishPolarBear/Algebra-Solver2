/** This class is used to store the value of a term so that it can be used to for the other classes to help solve the algebra equation.
*/
public class Term {
	// class variables
	private int magnitude, order;
	private boolean undeterminate;
	private String representation, variable;
	
	//default constructor
	public Term(){
		order =0;
		magnitude = 0;
		undeterminate = false;
		representation = "";
		variable = "";
	}
	
	/* Constructor that takes a string of "3x" as an argument */
	public Term(String arg){
		String magn,var,ord;
		boolean exp = false;
		magn = "";
		var = "";
		ord = "";
		undeterminate = false;
		char[] argArray = arg.toCharArray(); 
		for (int i = 0; i <argArray.length; i++){
			if (Character.isDigit(argArray[i]) && !exp){
				magn = magn + argArray[i];
			}
			if (Character.isLetter(argArray[i])){
				var = var + argArray[i];
				undeterminate = true; //set to true with an undetermined variable
			}
			if (Character.isDigit(argArray[i]) && exp){
				ord = ord + argArray[i];
			}
			if (argArray[i] == '^'){
				exp = true;
			}
		}
		if (var == ""){ //if no letter in the term it does not have variable
			var = "NULL";
		}
		variable = var;
		if (ord == ""){
			order = 1;
		} else {
			order = Integer.parseInt(ord);
		}
		representation = arg;
		magnitude = Integer.parseInt(magn);
	}
	
	/* Constructor that will take the values of each variable */
	public Term(int mag, String var, boolean undet, int ord){
		undeterminate = undet;
		order = ord;
		magnitude = mag;
		variable = var;
		if (var.equals("NULL")){
			representation = Integer.toString(mag);
		}
		else {
			representation = Integer.toString(mag)+ var;
		}
	}
	
	/* returns the Term's values in a string */
	public String printTerm(){
		String returnVal;
		returnVal = "";
		returnVal = "Magnitude: " + Integer.toString(magnitude);
		returnVal = returnVal + "##Order: " + Integer.toString(order);
		returnVal = returnVal + "##Undeterminate: " + Boolean.toString(undeterminate);
		returnVal = returnVal + "##Variable: " + variable;
		returnVal = returnVal + "##Representation: " + representation;
		return returnVal;
	}
	
	public String getVariable(){
		return variable;
	}
	
	public int getMagnitude(){
		return magnitude;
	}
	
	public boolean getUndeterminate(){
		return undeterminate;
	}
	
	public int getOrder(){
		return order;
	}
	
	public String getRepresentation(){
		return representation;
	}	
}