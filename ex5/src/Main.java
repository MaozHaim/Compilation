import java.io.*;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.FileNotFoundException;

import mips.MipsGenerator;
import java_cup.runtime.Symbol;
import ast.*;
import ir.*;
import cfg.*;


public class Main {

	public static String outputFileName = null;

	static public void main(String argv[]) {
		Lexer l;
		Parser p;
		Symbol s;
		AstProgram AST;
		CFG cfg;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		outputFileName = argv[1];

		try {
			// Read file
			file_reader = new FileReader(inputFilename);
			file_writer = new PrintWriter(outputFileName);
			l = new Lexer(file_reader);
			System.out.println("\nLexical analysis successful.\n");
			p = new Parser(l);
			System.out.println("\nSyntactic analysis successful.\n");


//			try {
//				System.setOut(new PrintStream("output/Logger.txt"));
//			}
//			catch (FileNotFoundException e) {}
			// Semantic phase
			AST = (AstProgram) p.parse().value;
			AST.printMe();
			try {
				System.out.println("=========================\n===Initiating Semantme===\n=========================");
				AST.SemantMe();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			AST.IRme();
			System.out.println("Successful IR translation.");
			System.out.println("\n=========================\n===== Printing IRme =====\n=========================");
			for (IrCommand command : Ir.getInstance().getCommands()) {
				System.out.println(command);
			}


			System.out.println("\n=========================\n===== Simplifying IR =====\n=========================");
			try {
				cfg = new CFG(Ir.getInstance().getCommands());
			} catch (Exception e) {
				writeFailureToFile("Register Allocation Failed", outputFileName);
				System.out.println("Register Allocation Failed");
				System.exit(0);
			}

			System.out.println("Finished simplifying IR + register allocation");
			// end of temp simplification

			System.out.println("\n=========================\n===== MIPSing to file =====\n=========================");
			MipsGenerator.getInstance().setOutPath(outputFileName);
			Ir.getInstance().MIPSme();
			MipsGenerator.getInstance().finalizeFile();
			System.out.println("Successful MIPS translation.");

			AstGraphviz.getInstance().finalizeFile();

			file_writer.close();
			System.out.println("SUCCESS!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void writeFailureToFile(String message, String PATH) {
		try {
			FileWriter fw = new FileWriter(PATH);
			BufferedWriter writer = new BufferedWriter(fw);

			writer.write(message);
			writer.close();
		}
		catch (IOException e) {
			System.out.println("ERROR WHEN OPENING FILE.");
			e.printStackTrace();
		}
	}
}


