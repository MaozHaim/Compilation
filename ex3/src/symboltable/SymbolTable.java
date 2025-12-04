/***********/
/* PACKAGE */
/***********/
package symboltable;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import types.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SymbolTable
{
	private int hashArraySize = 13;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SymbolTableEntry[] table = new SymbolTableEntry[hashArraySize];
	private SymbolTableEntry top;
	private int topIndex = 0;
	
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	private final int GLOBALSCOPE = 0;
	private Type expectedReturnType;
	private int scopeCounter = 0; // for each scope we enter, this increments. for each scope exited, this decrements.
	public TypeClass currentClass; // for searching for inherited fields inside subclass methods


	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, Type t) {
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/* will be the next entry of the new entry we are inserting */
		/******************************************************************************/
		SymbolTableEntry next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SymbolTableEntry newEntry = new SymbolTableEntry(name, t, hashValue, next, top, topIndex++, scopeCounter); // [3] Prepare a new symbol table entry with name, type, next and prevtop

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = newEntry;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = newEntry;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		printMe();
	}


	/***********************************************/
	/* Find the innermost scope element with name */
	/***********************************************/
	public Type find(String name) {
		SymbolTableEntry found = findEntryInTable(name);

		// If we're in a class and either nothing was found yet
		// or the found symbol is from the global scope,
		// try resolving as a member of the current class.
		if (currentClass != null && (found == null || found.scope == GLOBALSCOPE)) {
			Type memberType = findMemberType(currentClass, name);
			if (memberType != null) {
				return memberType;
			}
		}

		if (found != null) {
			return found.type;
		}

		return null;
	}


	/**
	 *  scan table to find the first occurrence on name
	 **/
	private SymbolTableEntry findEntryInTable(String name) {
		SymbolTableEntry current_entry = table[hash(name)];

		while (current_entry != null) {
			if (name.equals(current_entry.name)) {
				break;
			}
			current_entry = current_entry.next;
		}

		return current_entry;
	}


	/**
	 *  check if a variable is in the current scope
	 **/
	public boolean isInCurrentScope(String name){
		SymbolTableEntry entry = findEntryInTable(name);
		if (entry == null){
			return false;
		}
		return entry.scope == scopeCounter;
	}


	/**
	 * @return true iff table is currently in global scope
	 */
	public boolean inGlobalScope(){
		return scopeCounter == GLOBALSCOPE;
	}


	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope()
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		enter("SCOPE-BOUNDARY", new TypeForScopeBoundaries("NONE"));
		scopeCounter++;
		printMe(); // Print the symbol table after every change
	}


	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			topIndex = topIndex -1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */
		/**************************************/
		table[top.index] = top.next;
		topIndex = topIndex -1;
		top = top.prevtop;

		printMe(); // Print the symbol table after every change
	}


	/**
	 * Returns the type of attribute, or null on failure.
	 */
	public Type findMemberType(TypeClass classType, String memberName) {
		TypeClass currentClass = classType;

		while (currentClass != null) {
			TypeClassMemberDec desiredMember = findMemberInClass(currentClass, memberName);
			if (desiredMember != null) {
				return desiredMember.t;
			}
			currentClass = currentClass.father;
		}

		return null;
	}


	/**
	 * Finds a member of a class
	 */
	private TypeClassMemberDec findMemberInClass(TypeClass currentClass, String memberName) {
		List<TypeClassMemberDec> dataMembers = currentClass.dataMembers;
		for (TypeClassMemberDec member : dataMembers) {
			if (member.name.equals(memberName)) {
				return member;
			}
		}
		return null;
	}


	public void setExpectedReturnType(Type type){
		expectedReturnType = type;
	}


	public Type getExpectedReturnType(){
		return expectedReturnType;
	}


	public static int n=0;
	public void printMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SymbolTableEntry it = table[i]; it!=null; it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtopIndex);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}


	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SymbolTable instance = null;


	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SymbolTable() {}


	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SymbolTable getInstance() {
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SymbolTable();

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TypeInt.getInstance());
			instance.enter("string", TypeString.getInstance());
			instance.enter("void", TypeVoid.getInstance()); /* [2] How should we handle void ??? */
			instance.enter("nil", TypeVoid.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
					"PrintInt",
					new TypeFunction(
							TypeVoid.getInstance(),
							"PrintInt",
							Arrays.asList(TypeInt.getInstance())));
			instance.enter(
					"PrintString",
					new TypeFunction(
							TypeVoid.getInstance(),
							"PrintString",
							Arrays.asList(TypeString.getInstance())));
		}
		return instance;
	}


	/***************************************/
	/* Debugging purposes                  */
	/***************************************/
	public void printSymbolTable() {
		System.out.println("!Printing symbol table!");
		for (SymbolTableEntry entry: table) {
			SymbolTableEntry current = entry;
			while (current != null){
				System.out.println("Index: " + current.index);
				System.out.println("Name: " + current.name);
				System.out.println("Type: " + current.type.name);
				System.out.println();
				current = current.next;
			}
		}
	}
}
