package mips;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import ir.InitialConstVal;
import temp.Temp;

public class MipsGenerator
{
	private static final int WORD_SIZE=4;
	private PrintWriter fileWriter;
	private static final String STRCPY_LABEL = "strcpy";
	private static final String STRLEN_LABEL = "strlen";
	public static final String EOF = "end_of_file";


	// *** Singleton implementation ***
	private static MipsGenerator instance = null;

	protected MipsGenerator() {}

	public void setOutPath(String path){
		try {
			instance.fileWriter = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static MipsGenerator getInstance() {
		if (instance == null) {
			instance = new MipsGenerator();
		}
		return instance;
	}


	private void printf(String format, Object... args){
		this.fileWriter.printf("\t" + format +"\n", args);
	}


	public void data() {
		fileWriter.printf(".data\n");
	}


	public void text() {
		fileWriter.printf(".text\n");
	}


	public void finalizeFile() {
		label(EOF);
		printf("li $v0,10");
		printf("syscall");
		fileWriter.close();
	}


	public void builtInStrs(){
		instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
		instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
		instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
	}


	public void builtInFuncs(){
		defineStrlen();
		defineStrcpy();
	}


	public void wildcard(String command){
		printf(command);
	}


	/**
	 * Writes a definition for the function that gets string length.
	 * Uses a0 as input, and s0 as a temp, v0 as output.
	 */
	public void defineStrlen(){
		label(STRLEN_LABEL);
		printf("li $v0, 0");

		label("strlen_loop");
		printf("lb $s0, 0($a0)");
		printf("beq $s0, $zero, strlen_end");
		printf("addi $v0, $v0, 1");
		printf("addi $a0, $a0, 1");
		printf("j strlen_loop");

		label("strlen_end");
		printf("jr $ra");
	}


	/**
	 * Writes a definition for the function that copies the str in a1 into the address at a0.
	 * Uses a0 and a1 as input, s0 as temp.
	 */
	public void defineStrcpy(){
		label(STRCPY_LABEL);
		label("strcpy_loop");
		printf("lb $s0, 0($a1)");
		printf("sb $s0, 0($a0)");
		printf("beq $s0, $zero, strcpy_done");
		printf("addi $a0, $a0, 1");
		printf("addi $a1, $a1, 1");
		printf("j strcpy_loop");

		label("strcpy_done");
		printf("jr $ra");
	}


	/**
	 * Appends string in t1 to string in t2, allocates memory and saves in dst.
	 * DOES NOT PRESERVE s0, s2, s3, a0, a1, v0.
	 * Also does not preserve ra but should be fine since it is saved in stack for every non-builtin function call anyway.
	*/
	 public void appendStrs(Temp dst, Temp t1, Temp t2){
		// call strlen on t1, save in s2
		printf("move $a0, $t%d", t1.getSerialNumber());
		printf("jal %s", STRLEN_LABEL);
		printf("move $s2, $v0");

		// call strlen on t2, save in s3
		printf("move $a0, $t%d", t2.getSerialNumber());
		printf("jal %s", STRLEN_LABEL);
		printf("move $s3, $v0");

		// save t2 in $s4 to avoid it being overwritten
		printf("move $s4, $t%d", t2.getSerialNumber());

		// calculate total length, save in a0, allocate memory
		printf("add $a0, $s2, $s3");
		printf("addi $a0, $a0, 1"); // +1 for null terminator
		printf("li $v0, 9");
		printf("syscall");

		// set dst address
		printf("move $t%d, $v0", dst.getSerialNumber());

		// copy t1 into dst
		printf("move $a0, $t%d", dst.getSerialNumber()); // dest
		printf("move $a1, $t%d", t1.getSerialNumber());  // src = t1
		printf("jal %s", STRCPY_LABEL);

		// copy t2 into dst + len(t1)
		printf("add $a0, $t%d, $s2", dst.getSerialNumber()); // dest + len(t1)
		printf("move $a1, $s4"); // src = t2
		printf("jal %s", STRCPY_LABEL);
	}


	public void errorSection(String errLbl, String errMsgDataLbl){
		// errLbl = the label starting the error section
		// errMsgDataLbl = the .data string to print on error
		label(errLbl);
		printDataStr(errMsgDataLbl);
		printf("li $v0,10");
		printf("syscall");
	}


	public void printInt(Temp t) {
		// Does not preserve a0, v0
		int idx=t.getSerialNumber();
		// printf("addi $a0,$t%d,0",idx);
		printf("move $a0,$t%d",idx);
		printf("li $v0,1");
		printf("syscall");

		// Prints a " " character
		printf("li $a0,32");
		printf("li $v0,11");
		printf("syscall");
	}


	public void printDataStr(String dataLabel){
		// Does not preserve a0, v0
		printf("la $a0,%s",dataLabel);
		printf("li $v0,4");
		printf("syscall");
	}


	public void printStr(Temp addr){
		// Does not preserve a0, v0
		printf("move $a0, $t%d", addr.getSerialNumber());
		printf("li $v0, 4");
		printf("syscall");
	}


	public void stackPush(Temp src){
		int srcidx = src.getSerialNumber();
		stackAlloc(1);
		printf("sw $t%d,0($sp)", srcidx);
	}


	/**
	 * Sets dst to the address of parameter #offset.
	 * */
	public void getParameterAddress(Temp dst, int offset){
		printf("li $t%d, %d", dst.getSerialNumber(), (offset + 1) * WORD_SIZE);
		printf("add $t%d, $t%d, $fp", dst.getSerialNumber(), dst.getSerialNumber());
	}


	public void getLocalAddress(Temp dst, int offset){
		printf("li $t%d, %d", dst.getSerialNumber(), -offset * WORD_SIZE);
		printf("add $t%d, $t%d, $fp", dst.getSerialNumber(), dst.getSerialNumber());
	}


	/**
	 * Pushes stack amount*WORD_SIZE back
	 */
	public void stackAlloc(int amount){
		printf("subu $sp,$sp,%d", amount * WORD_SIZE);
	}


	/**
	 * Pushes stack amount*WORD_SIZE forward
	 */
	public void stackDealloc(int amount){
		printf("addu $sp,$sp,%d", amount * WORD_SIZE);
	}


	public void setMethodObjectReference(Temp ref){
		// $s1 holds the pointer to the object who called the current method.
		// Pushed previous $s1 val to stack and sets $s1 to ref
		stackAlloc(1);
		printf("sw $s1, 0($sp)");
		printf("lw $s1, 0($t%d)", ref.getSerialNumber());
	}


	public void restoreMethodObjectReference(){
		// Just pops stack into $s1
		printf("lw $s1, 0($sp)");
		stackDealloc(1);
	}


	public void getAttributeAddress(Temp dst, int offset){
		printf("addi $t%d, $s1, %d", dst.getSerialNumber(), offset * WORD_SIZE);
	}


	public void backupTemps(){
		stackAlloc(10);
		for (int i = 0; i <= 9; i++){
			printf("sw $t%d,%d($sp)", i, i * WORD_SIZE);
		}
	}


	/**
	 * Push ra and fp to the stack (start of function)
	 */
	public void pushFrame(){
		printf("subu $sp,$sp,4");
		printf("sw $fp,0($sp)");
		printf("subu $sp,$sp,4");
		printf("sw $ra,0($sp)");
		printf("move $fp, $sp");
	}


	/**
	 * Changes fp and sp to reflect the previous frame, jumps back to ra
	 */
	public void popFrameAndReturn(){
		printf("move $sp,$fp");
		printf("lw $ra,0($sp)");
		printf("lw $fp,4($sp)");
		printf("addu $sp,$sp,8");
		printf("jr $ra");
	}


	/**
	 * Creates an array and saves the pointer in dst.
	 * The size of the array is saved in offset 0, so all addresses are +1.
	 */
	public void allocateArray(Temp dst, Temp size){
		printf("li $v0, 9");
		printf("move $s0, $t%d", size.getSerialNumber());
		printf("move $a0, $s0");
		printf("add $a0, $a0, 1");
		printf("sll $a0, $a0, 2");
		printf("syscall");
		printf("sw $s0, 0($v0)");
		printf("move $t%d, $v0", dst.getSerialNumber());
	}


	public void initializeObject(Temp dst, String virtualPointer, List<InitialConstVal> initialValues, int totalFieldsCount){
		// Allocate memory based on the total number of fields, NOT the initial values list
		printf("li $v0, 9");
		printf("li $a0, %d", totalFieldsCount + 1); // +1 is for the virtual table pointer
		printf("mul $a0, $a0, 4");
		printf("syscall");

		// set vt pointer
		printf("move $t%d, $v0", dst.getSerialNumber());
		printf("la $s0, %s", virtualPointer);
		printf("sw $s0, 0($t%d)", dst.getSerialNumber());

		// set initial values
		for (int i = 0; i < initialValues.size(); i++){
			InitialConstVal val = initialValues.get(i);

			if (val.isString())
				printf("la $s0, str_%s", val.getValue());
			else
				printf("li $s0, %s", val.getValue());

			printf("sw $s0, %d($t%d)", (i+1) * 4, dst.getSerialNumber());
		}
	}


	public void restoreTemps(){
		for (int i = 0; i <= 9; i++){
			printf("lw $t%d,%d($sp)", i, i * WORD_SIZE);
		}
		stackDealloc(10);
	}


	public void setReturnVal(Temp src){
		// Moves val from src to v0
		printf("move $v0,$t%d", src.getSerialNumber());
	}


	public void getReturnVal(Temp dst){
		// Moves return val from v0 to dst
		printf("move $t%d,$v0",dst.getSerialNumber());
	}


	public void allocateGlobal(String var_name, String initVal, boolean isStringVal) {
		if (isStringVal) {
			initVal = "str_" + initVal;
		}
		printf("global_%s: .word %s", var_name, initVal);
	}


	public void load(Temp dst,String var_name) {
		int idx_dst = dst.getSerialNumber();
		printf("lw $t%d,global_%s",idx_dst,var_name);
	}


	public void storeGlobal(String var_name, Temp src) {
		int idx_src = src.getSerialNumber();
		printf("sw $t%d,global_%s",idx_src,var_name);
	}


	public void sw(Temp src, Temp dst, int offset){
		printf("sw $t%d, %d($t%d)", src.getSerialNumber(), offset * WORD_SIZE, dst.getSerialNumber());
	}


	/**
	 * Loads address src+tempOffset+immediateOffset, sets dst to it
	 */
	public void loadAt(Temp dst, Temp src, Temp tempOffset, int immediateOffset){
		if (tempOffset == null){
			printf("lw $t%d, %d($t%d)", dst.getSerialNumber(), immediateOffset * WORD_SIZE, src.getSerialNumber());
			return;
		}

		printf("move $s0, $t%d", tempOffset.getSerialNumber());

		if (immediateOffset != 0){
			printf("addi $s0, $s0, %d", immediateOffset);
		}

		printf("mul $s0, $s0, 4");
		printf("add $s0, $s0, $t%d", src.getSerialNumber());
		printf("lw $t%d, 0($s0)", dst.getSerialNumber());
	}


	public void loadAt(Temp dst, Temp src, Temp offset){
		loadAt(dst, src, offset, 0);
	}


	public void loadAt(Temp dst, Temp src, int offset){
		loadAt(dst, src, null, offset);
	}


	public void li(Temp t,int value) {
		int idx = t.getSerialNumber();
		printf("li $t%d,%d",idx,value);
	}


	public void sub(Temp dst, Temp op1, Temp op2){
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		printf("sub $t%d,$t%d,$t%d",dstidx,i1,i2);
	}


	public void addi(Temp dst, Temp op, int immediate){
		printf("addi $t%d, $t%d, %d", dst.getSerialNumber(), op.getSerialNumber(), immediate);
	}


	public void add(Temp dst,Temp op1,Temp op2) {
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();
		int dst_idx = dst.getSerialNumber();

		printf("add $t%d,$t%d,$t%d",dst_idx,i1,i2);
	}


	public void mul(Temp dst,Temp op1,Temp op2) {
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();
		int dst_idx = dst.getSerialNumber();

		printf("mul $t%d,$t%d,$t%d",dst_idx,i1,i2);
	}


	public void sll(Temp dst, Temp src, int shift){
		printf("sll $t%d, $t%d, %d", dst.getSerialNumber(), src.getSerialNumber(), shift);
	}


	public void getGlobalAddress(Temp dst, String varName){
		printf("la $t%d, global_%s", dst.getSerialNumber(), varName);
	}


	public void label(String label) {
		if (label.equals("main")) fileWriter.format(".globl main\n");
		fileWriter.format("%s:\n",label);
	}


	public void compareStr(Temp dst, Temp t1, Temp t2, String lblCompare, String lblNotEq, String lblEnd) {
		// DOES NOT PRESERVE s2, s3, s4, s5
		printf("li $t%d, 1", dst.getSerialNumber()); // assume equal

		printf("move $s2, $t%d", t1.getSerialNumber());
		printf("move $s3, $t%d", t2.getSerialNumber());

		// comparison loop
		label(lblCompare);
		printf("lb $s4, 0($s2)");
		printf("lb $s5, 0($s3)");
		printf("bne $s4, $s5, %s", lblNotEq); // break loop if unequal
		printf("beq $s4, $zero, %s", lblEnd); // break loop if reached null terminator
		printf("addi $s2, $s2, 1"); // advance both pointers by a single byte
		printf("addi $s3, $s3, 1");
		printf("j %s", lblCompare); // loop

		label(lblNotEq); // set to 0 if unequal
		printf("li $t%d, 0", dst.getSerialNumber());

		label(lblEnd);
	}


	public void asciiz(String strConst){
		fileWriter.format("str_%s: .asciiz \"%s\"\n", strConst, strConst);
	}


	public void loadStringConst(Temp dst, String strConst){
		printf("la $t%d, str_%s", dst.getSerialNumber(), strConst);
	}


	public void word(String pointer){
		printf(".word %s", pointer);
	}


	public void word(){
		printf(".word");
	}


	public void jump(String inlabel) {
		printf("j %s",inlabel);
	}


	public void blt(Temp op1,Temp op2,String label) {
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();
		printf("blt $t%d,$t%d,%s",i1,i2,label);
	}


	public void bltz(Temp t1, String label){
		printf("bltz $t%d, %s", t1.getSerialNumber(), label);
	}


	public void bge(Temp op1,Temp op2,String label) {
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();

		printf("bge $t%d,$t%d,%s",i1,i2,label);
	}


	public void bne(Temp op1,Temp op2,String label) {
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();

		printf("bne $t%d,$t%d,%s",i1,i2,label);
	}


	public void beq(Temp op1,Temp op2,String label) {
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();

		printf("beq $t%d,$t%d,%s",i1,i2,label);
	}


	public void beqz(Temp op1,String label) {
		int i1 = op1.getSerialNumber();

		printf("beq $t%d,$zero,%s",i1,label);
	}


	public void jal(String funcname){
		printf("jal %s", funcname);
	}


	public void jalr(Temp address){
		printf("jalr $t%d", address.getSerialNumber());
	}


	public void move(Temp dst, Temp src){
		int i1 = dst.getSerialNumber();
		int i2 = src.getSerialNumber();

		printf("move $t%d,$t%d", i1, i2);
	}


	public void divTo(Temp dst, Temp t1, Temp t2){
		int i1 = t1.getSerialNumber();
		int i2 = t2.getSerialNumber();
		int dst_idx = dst.getSerialNumber();

		printf("div $t%d,$t%d", i1, i2);
		printf("mflo $t%d", dst_idx);
	}
}
