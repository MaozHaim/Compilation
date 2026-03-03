package ir;

import mips.MipsGenerator;
import temp.*;

public class IrCommandGt extends IrCommandBinop {
    public Temp t1;
    public Temp t2;
    public Temp dst;


    public IrCommandGt(Temp dst, Temp t1, Temp t2) {
        super(dst, t1, t2);
    }


    @Override
    public String toString() {
        return dst + " := " + t1 + " > " + t2;
    }


    public void MIPSme() {
        /*******************************/
        /* [1] Allocate 2 fresh labels */
        /*******************************/
        String label_end        = getFreshLabel("end");
        String label_AssignOne  = getFreshLabel("AssignOne");
        String label_AssignZero = getFreshLabel("AssignZero");

        /******************************************/
        /* [2] if (t1>t2) goto label_AssignOne;  */
        /*     if (t1<=t2) goto label_AssignZero; */
        /******************************************/
        MipsGenerator.getInstance().blt(t2,t1,label_AssignOne);
        MipsGenerator.getInstance().bge(t2,t1,label_AssignZero);

        /************************/
        /* [3] label_AssignOne: */
        /*                      */
        /*         t3 := 1      */
        /*         goto end;    */
        /*                      */
        /************************/
        MipsGenerator.getInstance().label(label_AssignOne);
        MipsGenerator.getInstance().li(dst,1);
        MipsGenerator.getInstance().jump(label_end);

        /*************************/
        /* [4] label_AssignZero: */
        /*                       */
        /*         t3 := 1       */
        /*         goto end;     */
        /*                       */
        /*************************/
        MipsGenerator.getInstance().label(label_AssignZero);
        MipsGenerator.getInstance().li(dst,0);
        MipsGenerator.getInstance().jump(label_end);

        /******************/
        /* [5] label_end: */
        /******************/
        MipsGenerator.getInstance().label(label_end);}
}
