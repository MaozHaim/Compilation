import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.Ir;
import cfg.CFG
import java_cup.runtime.Symbol;
import ast.*;

public class Main
{
	public static String outputFileName = null;

	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AstProgram ast;
		CFG cfg;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		String outputFileName = argv[1];

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
			ast.SemantMe();

			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			ast.irMe();
			cfg = new CFG(Ir.getInstance().getCommands());
			writeUndeclaredToFile(cfg, outputFileName);
			System.out.println("End of declaration analysis");

			/**************************/
			/* [9] Close output file */
			/**************************/
			fileWriter.close();

			/*************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void writeUndeclaredToFile(CFG cfg, String path) {
		int undeclaredCount = cfg.uninitializedVars.size();
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter writer = new BufferedWriter(fw);
			if (undeclaredCount == 0) {
				writer.write("!OK");
				System.out.println("\n ALL VARIABLES DECLARED!");
			}
			else {
				System.out.println("\n UNDECLARED VARIABLES:");
				List<String> undeclaredVars = new ArrayList<>(cfg.uninitializedVars);
				Collections.sort(undeclaredVars); // Lex sort apparently
				for (int i = 0; i < undeclaredVars.size(); i++) {
					String undeclaredVar = undeclaredVars.get(i);
					System.out.println(undeclaredVar);
					writer.write(undeclaredVar);
					if (i < undeclaredVars.size() - 1) {
						writer.write("\n");
					}
				}
			}
			writer.close();
		}
		catch (IOException e) {
			System.out.println("ERROR WHEN OPENING FILE.");
			e.printStackTrace();
		}
	}
}


