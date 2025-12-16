import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import errors.SemanticException;
import java_cup.runtime.Symbol;
import ast.AstGraphviz;
import ast.AstProgram;

public class Main
{
	public static String outputFileName = null;

	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AstProgram ast;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		outputFileName = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFileName);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			fileWriter = new PrintWriter(outputFileName);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(fileReader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			ast = (AstProgram) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			ast.printMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			try {
				System.out.println("=========================\n===Initiating Semantme===\n=========================");
				ast.SemantMe();
			}
			catch (Exception e) {
				if (e instanceof SemanticException) {
					SemanticException se = (SemanticException) e;
					writeStatusToFile(se.getLineNumber(), "ERROR", outputFileName);
				}
				else { writeStatusToFile(-1, "ERROR", outputFileName); }
				e.printStackTrace();
				System.exit(0);
			}
			
			/*************************/
			/* [8] Close output file */
			/*************************/
			fileWriter.close();

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
		writeStatusToFile(-1, "SUCCESS", outputFileName);
	}

	public static void writeStatusToFile(int lineNumber, String status, String PATH) {
		try {
			FileWriter fw = new FileWriter(PATH);
			BufferedWriter writer = new BufferedWriter(fw);

			if (status.equals("ERROR")) {
				if (lineNumber >= 0) {
					writer.write("ERROR(" + lineNumber + ")");
				} else {
					writer.write("ERROR");
				}
			}
			else if (status.equals("SUCCESS")) {
				writer.write("OK");
				System.out.println("\n SUCCESS!");
			}
			else {
				writer.close();
				throw new IOException("Incorrect status code when writing to file.");
			}

			writer.close();
		}
		catch (IOException e) {
			System.out.println("ERROR WHEN OPENING FILE.");
			e.printStackTrace();
		}
	}
}


