import java.io.*;

import java_cup.runtime.Symbol;

public class Main
{
	final static int MAX_NUMBER = (int)Math.pow(2,15) - 1;  // Maximal int

	public static final String[] id_to_name = { /* Array mapping token IDs to their names */
			"EOF",          // 0
			"LPAREN",       // 1
			"RPAREN",       // 2
			"LBRACK",       // 3
			"RBRACK",       // 4
			"LBRACE",       // 5
			"RBRACE",       // 6
			"PLUS",         // 7
			"MINUS",        // 8
			"TIMES",        // 9
			"DIVIDE",       // 10
			"COMMA",        // 11
			"DOT",          // 12
			"SEMICOLON",    // 13
			"TYPE_INT",     // 14
			"TYPE_STRING",  // 15
			"TYPE_VOID",    // 16
			"ASSIGN",       // 17
			"EQ",           // 18
			"LT",           // 19
			"GT",           // 20
			"ARRAY",        // 21
			"CLASS",        // 22
			"RETURN",       // 23
			"WHILE",        // 24
			"IF",           // 25
			"ELSE",         // 26
			"NEW",          // 27
			"EXTENDS",      // 28
			"NIL",          // 29
			"INT",       	// 30
			"STRING",       // 31
			"ID",           // 32
			"ERROR"			// 33
	};

	static private void overwriteOutputFile(String filename) throws IOException {
		System.out.println("Error encountered, overwriting file");
		PrintWriter fw = new PrintWriter(filename);
		fw.print("ERROR");
		fw.close();
	}

	static private boolean numIsValid(Object numberAsObject) {
		int number;
		try{
			String numberAsString = numberAsObject.toString();
			if (numberAsString.length() >= 10) { // Larger than 1 billion
				return false;
			}
			number = Integer.parseInt(numberAsString);
			return (0 <= number) && (number <= MAX_NUMBER);
		}
		catch (Exception e){ // length check to prevent overflow
			return false;
		}
	}

	static public void main(String argv[]) throws IOException
	{
		Lexer l;
		Symbol s;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		boolean err = false;
		boolean firstIter = true;

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

		/***********************/
		/* [4] Read next token */
		/***********************/
		try {
			s = l.next_token();
		}
		catch (Error e) {
			l.yyclose();
			fileWriter.close();
			overwriteOutputFile(outputFileName);
			return;
		}
		/********************************/
		/* [5] Main reading tokens loop */
		/********************************/
		while (s.sym != TokenNames.EOF)
		{
			/************************/
			/* [6] Print to console */
			/************************/
			String tokenType = id_to_name[s.sym];
			System.out.print(tokenType);
			System.out.print("[");
			System.out.print(l.getLine());
			System.out.print(",");
			System.out.print(l.getTokenStartPosition());
			System.out.println("]");
			if (tokenType.equals("ERROR")){
				err = true;
				break;
			}

			if (firstIter)
				firstIter = false;
			else
				fileWriter.print("\n");
			fileWriter.print(tokenType);

			boolean isNumber = tokenType.equals("INT");
			boolean isString = tokenType.equals("STRING");
			boolean isID = tokenType.equals("ID");
			if (isNumber || isString || isID) {
				if (isNumber && !numIsValid(s.value)) {
					err = true;
					break;
				}
				fileWriter.print("(");
				fileWriter.print(s.value);
				fileWriter.print(")");
			}
			fileWriter.print("[");
			fileWriter.print(l.getLine());
			fileWriter.print(",");
			fileWriter.print(l.getTokenStartPosition());
			fileWriter.print("]");

			/***********************/
			/* [7] Read next token */
			/***********************/
			try {
				s = l.next_token();
			}
			catch (Error e) {
				l.yyclose();
				fileWriter.close();
				overwriteOutputFile(outputFileName);
				return;
			}
		}

		/******************************/
		/* [8] Close lexer input file */
		/******************************/
		l.yyclose();

		/**************************/
		/* [9] Close output file */
		/**************************/
		fileWriter.close();

		/*************************************/
		/* [9] Overwrite in case of an error */
		/*************************************/
		if (err)
			overwriteOutputFile(outputFileName);
		else
			System.out.println("Successful Lexical Analysis");
	}
}


