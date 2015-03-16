/** The class creates the GUI and then runs the main function to help solve the algebra function */

//package layout;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.*;
import javax.swing.Box;

public class MathApplication extends JFrame implements ActionListener{
	protected JTextArea outputText, equationText;
	
	public MathApplication (){
		createAndShowGUI();
	}

	public void addComponentsToPane (Container pane){
		//creates the GUI
		pane.setLayout(new BoxLayout (pane, BoxLayout.Y_AXIS));
		
		JLabel equationLabel = new JLabel("Equation(s)[Separate/End the equation(s) with a semi-colon]");
		pane.add(equationLabel);
		equationText = new JTextArea("Enter equations here."
			+ " Separate each equation with a semi-colon.");
		equationText.setLineWrap(true);
		equationText.setWrapStyleWord(true);

		JScrollPane equationScroll = new JScrollPane(equationText);
		equationScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		equationScroll.setPreferredSize(new Dimension(300,200));
		pane.add(equationScroll);

		JLabel outputLabel = new JLabel("Output:");
		pane.add(outputLabel);

		outputText = new JTextArea("Output");
		outputText.setLineWrap(true);
		outputText.setWrapStyleWord(true);
		//outputText.setEditable(false);
		
		JScrollPane outputScroll = new JScrollPane(outputText);
		outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		outputScroll.setPreferredSize(new Dimension(300,200));
		pane.add(outputScroll);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout (panel2, BoxLayout.X_AXIS));
		
		JButton solve = new JButton("Solve");
		solve.setActionCommand("solve");
		solve.addActionListener(this);
		panel2.add(solve);

		panel2.add(Box.createHorizontalGlue());

		JButton quit = new JButton("Quit");
		quit.addActionListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		panel2.add(quit);
		pane.add(panel2);		
		
	}
	
	 /* public void actionPerformed(ActionEvent e){
		String eqText,returnValue = "";
		//ArrayList<Term> leftTerms = new ArrayList<Term>();
		//ArrayList<Term> rightTerms = new ArrayList<Term>();
		//Algebra_Solver algebra;
		//int interations;
		//String display;
		//String tester;
		
		//display = "";
		//eqText = equationText.getText(); //gets the equation in the input field
		//display = eqText + "\n";
		//Equation eq = new Equation(eqText); //initializes it
		//returnValue = eq.printEquation();
		
		//algebra = new Algebra_Solver(eq); //initializes the algebra_solver class
		//algebra.combineLikeTerms(); //does the first step of combining like terms
		//display = display + algebra.displayEquation() + "\n";
		//returnValue = algebra.isolateVariableTerm("x"); //isolates the variables
		//display = display + algebra.displayEquation();
		//returnValue = algebra.printEquation();
		//returnValue = "Test";
		//tester = display +"\n\n" + returnValue;
		//outputText.setText(display); //displays the output
	} */

	public void actionPerformed(ActionEvent e){
		String eqText, output;
		boolean eqCheck;		
		
		output = "";
		eqText = equationText.getText();
		EquationCheck ec = new EquationCheck(eqText);
		eqCheck = ec.startChecking();
		if (eqCheck) {
			Equation eq = new Equation(eqText);
			Algebra_Solver algebra = new Algebra_Solver(eq);
			algebra.combineLikeTerms();
			output = eq.testingApplication();
			output = output + "\n\n SOMETHING \n";
			output = output + eq.testValue;
		} else {
			output = ec.getErrorExplanation();
		}
		outputText.setText(output);
	}

	private void createAndShowGUI(){
		JFrame frame = new JFrame("Math Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		addComponentsToPane(frame.getContentPane());
		frame.setSize(300,500);
		//frame.pack();
		frame.setVisible(true);
	}
	
	//main function
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				MathApplication math = new MathApplication();
				math.createAndShowGUI();
			}
		});
	}
}