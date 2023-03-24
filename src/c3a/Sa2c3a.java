package c3a;
import java.util.*;
import ts.*;
import sa.*;

public class Sa2c3a extends SaDepthFirstVisitor <C3aOperand> {
    private C3a c3a;
    int indentation;
    public C3a getC3a(){return this.c3a;}
    
    public Sa2c3a(SaNode root, Ts tableGlobale){
	c3a = new C3a();
	C3aTemp result = c3a.newTemp();
	C3aFunction fct = new C3aFunction(tableGlobale.getFct("main"));
	c3a.ajouteInst(new C3aInstCall(fct, result, ""));
	c3a.ajouteInst(new C3aInstStop(result, ""));
	
	indentation = 0;
	try{
	    root.accept(this);
	}
	catch(Exception e){}
    }

    public void defaultIn(SaNode node)
    {
	//for(int i = 0; i < indentation; i++){System.out.print(" ");}
	//indentation++;
	//System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(SaNode node)
    {
	//indentation--;
	//	for(int i = 0; i < indentation; i++){System.out.print(" ");}
	//	System.out.println("</" + node.getClass().getSimpleName() + ">");
    }





    //Done
    public C3aOperand visit(SaDecFonc node) throws Exception
    {
        defaultIn(node);
        c3a.ajouteInst(new C3aInstFBegin(node.tsItem, ""));
        if(node.getCorps() != null) node.getCorps().accept(this);
        c3a.ajouteInst(new C3aInstFEnd( ""));
        defaultOut(node);
        return null;
    }

    //Done
    public C3aOperand visit(SaExpAdd node) throws Exception {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstAdd(op1, op2, result, ""));
        defaultOut(node);
        return result;
    }

    //Done
    public C3aOperand visit(SaExpSub node) throws Exception
    {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstSub(op1,op2,result,""));
        defaultOut(node);
        return null;
    }

   //Done
    public C3aOperand visit(SaExpMult node) throws Exception
    {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstMult(op1,op2,result,""));
        defaultOut(node);
        return null;
    }

    // Done
    public C3aOperand visit(SaExpDiv node) throws Exception
    {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstDiv(op1,op2,result,""));
        defaultOut(node);
        return null;
    }
}
