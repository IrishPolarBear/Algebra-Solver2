public class EquationCheck {
	private String equation;
	private boolean validEquation;
	private String errorExplanation;

	public EquationCheck(String arg){
		equation = arg;
	}
	
	public boolean startChecking() {
		int num = 0;
		String retValue;
		errorExplanation = "none";
		
		validEquation = true;
		while (num < 4 && validEquation){
			switch (num) {
				case 0:
					validEquation = checkCharacters();
					num++;
					break;
				case 1:
					validEquation = checkDistribution();
					rewriteEquationDistribution();
					num++;
					break;
				case 2:
					validEquation = checkFunctions();
					num++;
					break;
				case 3:
					validEquation = checkTerms();
					num++;
					break;
			}
		}

		return validEquation;
	}
		
	private void rewriteEquationDistribution(){
		int placement = 0, placement2 = 0, cursor;
		String nq, nt = "", arg;
		
		arg = equation;
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
		
		equation = nq;		
	}
	
	private boolean checkTerms(){
		String delim1 = "[=]";
		String delim2 = "[+\\-*/]+";
		boolean check = true, var, num, exp;
		int termLength, sideLength, length;
		
		String[] sides = equation.split(delim1);
		sideLength = sides.length;
		for (int i = 0; i < sideLength; i++){
			String[] terms = sides[i].split(delim2);
			termLength = terms.length;
			for (int j = 0; j < termLength; j++){
				char[] termArray = terms[j].toCharArray();
				length = termArray.length;
				num = false;
				var = false;
				exp = false;
				for (int k = 0; k < length; k++){
					if (Character.isDigit(termArray[k])) {
						if (var && !exp) {
							num = true;
							errorExplanation = "ERROR: Invalid term formatting. Cannot have a number after a variable.";
							check = false;
							break;
						}
					}
					if (Character.isLetter(termArray[k])){
						if (!var) {
							var = true;
						} else {
							errorExplanation = "ERROR: Invalid term formatting. Cannot have multiple letter variables in the term... for now!";
							check = false;
							break;
						}
					} 
					if (termArray[k] == '^'){
						exp = true;
					}
				}
				if (exp && !num) {
					errorExplanation = "ERROR: Invalid term formatting. Need to have number after an exponent marker.";
					check = false;
					break;
				}
				if (!check) {
					break;
				}
			}
			if (!check) {
				break;
			}
		}
		return check;
	}
		
	private boolean checkFunctions(){
		boolean check = true;
		String delim1 = "[=]";
		String delim2 = "[+\\-*/]+";
		String search = "+\\-*/";
		int length, sideLength, checker;
		String checkValue = "";
		char c;

		String[] sides = equation.split(delim1);
		sideLength = sides.length;
		checker = 0;
		for (int i = 0; i < sideLength; i++){
			length = sides[i].length();
			for (int k = 0; k < length; k++){
				c = sides[i].charAt(k);
				checkValue = checkValue + String.valueOf(c) + "##";
				if (search.contains(String.valueOf(c))){
					checker++;
				} else {
					checker = 0;
				}
				if (checker == 2){
					check = false;
					break;
				}		
			}
			if (!check) {
				break;
			}
		}
		
		if (!check){
			errorExplanation = "ERROR: Invalid function formatting. You cannot repeat functions.";
		}
		return check;
	}
		
	private boolean checkDistribution(){
		int length, sideLength;
		boolean openerFound, check = true;
		char c;
		String delim1 = "[=]";
		
		String[] sides = equation.split(delim1);
		sideLength = sides.length;	
		openerFound = false;
		for (int k = 0; k < sideLength; k++){
			length = sides[k].length();
			for (int i = 0; i < length; i++){
				c = sides[k].charAt(i);
				if ((openerFound) && (c == '(')) {
					check = false;
					break;
				}
				if (c == '(') {
					openerFound = true;
				}
				if ((openerFound) && (c == ')')) {
					openerFound = false;
				}
			}
			if (!check) {
				errorExplanation = "ERROR: Improper formatting for the distribution property. The distributive terms are not properly enclosed.";
				break;
			}
			if (openerFound) {
				check = false;
				errorExplanation = "ERROR: Improper formatting for the distribution property. Cannot have a distributive term within another one";
				break;
			}
		}
		return check;
	}
	
	private boolean checkCharacters(){
		int asciiCheck, length;
		char c;
		boolean check = true;

		length = equation.length();
		for (int i = 0; i < length; i++){
			c = equation.charAt(i);
			asciiCheck = (int) c; 
			if ((asciiCheck < 40) || (asciiCheck > 57)){
				if ((asciiCheck < 97 || asciiCheck > 122)) {
					if (asciiCheck != 61) {
						check = false;
						break;
					}
				}
			} else {
				if ((asciiCheck == 44) || (asciiCheck == 46)) {
					check = false;
					break;
				}
			}
		}
		if (!check) {
			errorExplanation = "ERROR: Invalid character(s) found in the equation. Please use alphanumeric characters and valid algebraic functions.";
		}
		return check;
	}
	
	public String getErrorExplanation(){
		return errorExplanation;
	}
	
}