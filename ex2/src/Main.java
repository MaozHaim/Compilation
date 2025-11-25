import java.io.*;

import ast.AstProgram;
import java_cup.runtime.Symbol;
import ast.*;

public class Main{

	public static String outputFileName = null;

	static public void main(String argv[]) {
		Lexer l;
		Parser p;
		Symbol s;
		AstProgram ast;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		outputFileName = argv[1];

		try {
			/* ********************** */
			/* [0] PRINT LEXER TOKENS */
			/* ********************** */
			printLexer(inputFileName);

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
			writeStatusToFile("OK", outputFileName); // if it makes it to this line, parsing succeeded

//			/*************************/
//			/* [6] Print the AST ... */
//			/*************************/
//			ast.PrintMe();

			/*************************/
			/* [7] Close output file */
			/*************************/
			fileWriter.close();

//			/*************************************/
//			/* [8] Finalize AST GRAPHIZ DOT file */
//			/*************************************/
//			AST_GRAPHVIZ.getInstance().finalizeFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		writeStatusToFile(-1, "OK", outputFileName);
	}


	public static void writeStatusToFile(String status, String PATH) {
		writeStatusToFile(-1, status, PATH);
	}


	public static void writeStatusToFile(int lineNumber, String status, String PATH) {
		try {
			FileWriter fw = new FileWriter(PATH);
			BufferedWriter writer = new BufferedWriter(fw);

			if (status.equals("ERROR")) {
				if (lineNumber == -1) {
					// Lexical error - just write "ERROR"
					writer.write("ERROR");
				} else {
					// Syntax error - write "ERROR(lineNumber)"
					writer.write("ERROR(" + lineNumber + ")");
				}
			}
			else if (status.equals("OK")) {
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


	public static void logLexerSymbol(Symbol symbol) {
		if (symbol != null) {
			String tokenName = TokenNames.terminalNames[symbol.sym];
			System.out.println("Token: " + tokenName + " (line: " + symbol.left + ")");
		}
	}


	public static void printLexer(String inputFilename) {
		try {
			FileReader file_reader = new FileReader(inputFilename); // Reopen the file from the start
			Lexer l = new Lexer(file_reader);
			System.out.println("Tokens:");
			Symbol symbol;
			while ((symbol = l.next_token()).sym != 0) { // 0 represents EOF
				logLexerSymbol(symbol);
				// Check for ERROR token (lexical error)
				if (symbol.sym == TokenNames.ERROR) {
					writeStatusToFile(-1, "ERROR", outputFileName);
					System.out.println("Exiting due to LEXICAL error at line " + (symbol.left + 1));
					System.exit(0);
				}
			}
			file_reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}