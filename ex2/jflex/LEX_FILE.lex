/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/

import java_cup.runtime.*;

/******************************/
/* DOLLAR DOLLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/

/*****************************************************/
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */
/*****************************************************************************/
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; }
%}

/***********************/
/* MACRO DECLARATIONS */
/***********************/
LineTerminator	            = \r | \n | \r\n
WhiteSpace		            = {LineTerminator} | [\s\t]
LEADING_ZERO 	            = 0[0-9]+
INTEGER			            = 0 | [1-9][0-9]*
LETTER                      = [a-zA-Z]
ID				            = {LETTER}([a-zA-Z0-9]*)
STRING                      = \"{LETTER}*\"  /* "Strings that contain non-letter characters are lexical errors" */
CommentAllowedWithNewline   = [A-Za-z0-9 \t\r\n(){}\[\]?!+\-*/.;]
CommentAllowedNoNewline     = [A-Za-z0-9 \t(){}\[\]?!+\-*/.;]
T1_COMMENT 		            = \/\/({CommentAllowedNoNewline})*{LineTerminator}
T1_ERROR                    = \/\/({CommentAllowedNoNewline} | [^A-Za-z0-9 \t\r\n(){}\[\]?!+\-*/.;])*{LineTerminator}
T2_COMMENT		            = "/*" ( [a-zA-Z0-9\s\t\r\n(){}\[\]?!+\-./;] | \*+[^*/] )* \*+ "/"
T2_ERROR                    = "/*" ( [a-zA-Z0-9\s\t\r\n(){}\[\]?!+\-./;] | \*+[^*/] )* (\*+)?
COMMENT_ERROR               = {T1_ERROR} | {T2_ERROR}
SKIP  			            = {WhiteSpace} | {T1_COMMENT} | {T2_COMMENT}

/******************************/
/* DOLLAR DOLLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
    "("                 { return symbol(TokenNames.LPAREN); }              // LPAREN: "("
    ")"                 { return symbol(TokenNames.RPAREN); }              // RPAREN: ")"
    "["                 { return symbol(TokenNames.LBRACK); }              // LBRACK: "["
    "]"                 { return symbol(TokenNames.RBRACK); }              // RBRACK: "]"
    "{"                 { return symbol(TokenNames.LBRACE); }              // LBRACE: "{"
    "}"                 { return symbol(TokenNames.RBRACE); }              // RBRACE: "}"
    "+"                 { return symbol(TokenNames.PLUS); }                // PLUS: "+"
    "-"                 { return symbol(TokenNames.MINUS); }               // MINUS: "-"
    "*"                 { return symbol(TokenNames.TIMES); }               // TIMES: "*"
    "/"                 { return symbol(TokenNames.DIVIDE); }              // DIVIDE: "/"
    ","                 { return symbol(TokenNames.COMMA); }               // COMMA: ","
    "."                 { return symbol(TokenNames.DOT); }                 // DOT: "."
    ";"                 { return symbol(TokenNames.SEMICOLON); }           // SEMICOLON: ";"
    "int"               { return symbol(TokenNames.TYPE_INT); }            // TYPE_INT: "int"
    "string"			{ return symbol(TokenNames.TYPE_STRING); }         // TYPE_STRING: "string"
    "void"              { return symbol(TokenNames.TYPE_VOID); }           // TYPE_VOID: "void"
    ":="                { return symbol(TokenNames.ASSIGN); }              // ASSIGN: ":="
    "="                 { return symbol(TokenNames.EQ); }                  // EQ: "="
    "<"                 { return symbol(TokenNames.LT); }                  // LT: "<"
    ">"                 { return symbol(TokenNames.GT); }                  // GT: ">"
    "array"             { return symbol(TokenNames.ARRAY); }               // ARRAY: "array"
    "class"             { return symbol(TokenNames.CLASS); }               // CLASS: "class"
    "return"            { return symbol(TokenNames.RETURN); }              // RETURN: "return"
    "while"             { return symbol(TokenNames.WHILE); }               // WHILE: "while"
    "if"                { return symbol(TokenNames.IF); }                  // IF: "if"
    "else"              { return symbol(TokenNames.ELSE); }                // ELSE: "else"
    "new"               { return symbol(TokenNames.NEW); }                 // NEW: "new"
    "extends"           { return symbol(TokenNames.EXTENDS); }             // EXTENDS: "extends"
    "nil"               { return symbol(TokenNames.NIL); }                 // NIL: "nil"
    {LEADING_ZERO}      { return symbol(TokenNames.ERROR); }               // ERROR: Number with leading-zero
    {INTEGER}           { int MAX_NUMBER = (int)Math.pow(2,15) - 1;  // Maximal int - 32767
                          int number;
                          try{
                            String numberAsString = yytext();
                            number = Integer.parseInt(numberAsString);
                            if ((0 <= number) && (number <= MAX_NUMBER))
                                return symbol(TokenNames.INT, new Integer(yytext()));
                            else
                                return symbol(TokenNames.ERROR);
                          }
                          catch (Exception e){
                            return symbol(TokenNames.ERROR); } }                    // INTEGER: Number with value (check range in Main)
    {STRING}            { return symbol(TokenNames.STRING, new String(yytext()));}  // String: String with value
    {ID}                { return symbol(TokenNames.ID, new String(yytext()));}      // ID: ID with value
    {SKIP}              { /* just skip what was found, do nothing */}               // SKIP: Skip these tokens
    {COMMENT_ERROR}     { return symbol(TokenNames.ERROR); }                        // ERROR: Comment error
    <<EOF>>             { return symbol(TokenNames.EOF); }
    .                   { return symbol(TokenNames.ERROR); }
}
