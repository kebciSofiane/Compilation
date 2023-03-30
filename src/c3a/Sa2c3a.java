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
    @Override
    //Done
    public C3aOperand visit(SaProg node) throws Exception
    {
        defaultIn(node);
        if(node.getVariables() != null)
            node.getVariables().accept(this);
        if(node.getFonctions() != null)
            node.getFonctions().accept(this);
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public C3aOperand visit(SaExpInt node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = new C3aConstant(node.getVal());
        defaultOut(node);
        return result;
    }

    @Override
    //Done
    public C3aOperand visit(SaExpVrai node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = new C3aConstant(1);
        defaultOut(node);
        return result;
    }
    @Override
    //Done
    public C3aOperand visit(SaExpFaux node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = new C3aConstant(0);

        defaultOut(node);
        return result;
    }

    @Override
    //Done
    public C3aOperand visit(SaExpVar node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = node.getVar().accept(this);
        defaultOut(node);
        return result ;
    }
    @Override
    //Done
    public C3aOperand visit(SaInstEcriture node) throws Exception
    {
        defaultIn(node);
        C3aOperand op =   node.getArg().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst( new C3aInstWrite(op,""));
        defaultOut(node);
        return result;
    }

    @Override
    //Done
    public C3aOperand visit(SaInstTantQue node) throws Exception {
        defaultIn(node);
        C3aLabel labelTest = c3a.newAutoLabel();
        C3aLabel labF = c3a.newAutoLabel();

        c3a.addLabelToNextInst(labelTest);
        C3aOperand test = node.getTest().accept(this);
        c3a.ajouteInst(new C3aInstJumpIfEqual(test,c3a.False,labF,""));

        C3aOperand t =node.getFaire().accept(this);

        c3a.ajouteInst(new C3aInstJump(labelTest,""));
        c3a.addLabelToNextInst(labF);
        defaultOut(node);

        return null;
    }

    @Override
    //Done
    public C3aOperand visit(SaExpAppel node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = node.getVal().accept(this);
        C3aFunction op = new C3aFunction(node.getVal().tsItem);
        c3a.ajouteInst(new C3aInstCall(op,result,""));
        defaultOut(node);
        return result;

    }

    //Done
    @Override
    public C3aOperand visit(SaAppel node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = c3a.newTemp();
        if(node.getArguments() != null) node.getArguments().accept(this);
        C3aFunction op = new C3aFunction(node.tsItem);
        c3a.ajouteInst(new C3aInstCall(op,result,""));
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public C3aOperand visit(SaDecFonc node) throws Exception
    {
        defaultIn(node);
        c3a.ajouteInst(new C3aInstFBegin(node.tsItem, "entree fonction"));
        if(node.getCorps() != null)
        node.getCorps().accept(this);
        c3a.ajouteInst(new C3aInstFEnd( ""));
        defaultOut(node);
        return null;
    }

    @Override
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
    @Override
    //Done
    public C3aOperand visit(SaExpSub node) throws Exception
    {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstSub(op1,op2,result,""));
        defaultOut(node);
        return result;
    }
    @Override
   //Done
    public C3aOperand visit(SaExpMult node) throws Exception
    {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstMult(op1,op2,result,""));
        defaultOut(node);
        return result;
    }
    @Override
    // Done
    public C3aOperand visit(SaExpDiv node) throws Exception
    {
        defaultIn(node);
        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstDiv(op1,op2,result,""));
        defaultOut(node);
        return result;
    }
//Done
    public C3aOperand visit(SaExpInf node) throws Exception
    {
        defaultIn(node);
        C3aLabel resultF = c3a.newAutoLabel();
        C3aOperand result = c3a.newTemp();
        C3aOperand op1 =  node.getOp1().accept(this);
        C3aOperand op2 =  node.getOp2().accept(this);
        c3a.ajouteInst(new C3aInstAffect(c3a.True, result,""));
        c3a.ajouteInst(new C3aInstJumpIfLess(op1,op2,resultF,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.False,result,""));
        c3a.addLabelToNextInst(resultF);
        defaultOut(node);
        return result;
    }

    //Done
    public C3aOperand visit(SaExpEqual node) throws Exception
    {
        defaultIn(node);
        C3aLabel resultF = c3a.newAutoLabel();
        C3aOperand result = c3a.newTemp();
        C3aOperand op1 =  node.getOp1().accept(this);
        C3aOperand op2 =  node.getOp2().accept(this);
        c3a.ajouteInst(new C3aInstAffect(c3a.True, result,""));
        c3a.ajouteInst(new C3aInstJumpIfEqual(op1,op2,resultF,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.False,result,""));
        c3a.addLabelToNextInst(resultF);
        defaultOut(node);
        return result;
    }

    //Done
    public C3aOperand visit(SaExpAnd node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = c3a.newTemp();
        C3aLabel l1 = c3a.newAutoLabel();
        C3aLabel l0 = c3a.newAutoLabel();
        C3aOperand op = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        c3a.ajouteInst(new C3aInstJumpIfEqual(op, c3a.False,l0,""));
        c3a.ajouteInst(new C3aInstJumpIfEqual(op2, c3a.False,l0,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.True, result, ""));
        c3a.ajouteInst(new C3aInstJump(l1,""));
        c3a.addLabelToNextInst(l0);
        c3a.ajouteInst(new C3aInstAffect(c3a.False, result, ""));
        c3a.addLabelToNextInst(l1);
        defaultOut(node);
        return result;
    }


    // Done
    public C3aOperand visit(SaExpOr node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = c3a.newTemp();
        C3aLabel l1 = c3a.newAutoLabel();
        C3aLabel l0 = c3a.newAutoLabel();
        C3aOperand op = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);
        c3a.ajouteInst(new C3aInstJumpIfNotEqual(op, c3a.False,l0,""));
        c3a.ajouteInst(new C3aInstJumpIfNotEqual(op2, c3a.False,l0,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.False, result, ""));
        c3a.ajouteInst(new C3aInstJump(l1,""));
        c3a.addLabelToNextInst(l0);
        c3a.ajouteInst(new C3aInstAffect(c3a.True, result, ""));
        c3a.addLabelToNextInst(l1);
        defaultOut(node);
        return result;
    }

    // Done
    public C3aOperand visit(SaExpNot node) throws Exception
    {
        defaultIn(node);
        C3aLabel l0 = c3a.newAutoLabel();
        C3aOperand result = c3a.newTemp();

        C3aOperand op1 =  node.getOp1().accept(this);

        c3a.ajouteInst(new C3aInstAffect(c3a.True, result,""));
        c3a.ajouteInst(new C3aInstJumpIfEqual(op1, c3a.False,l0,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.False,result,""));

        c3a.addLabelToNextInst(l0);
        defaultOut(node);
        return result;
    }

    @Override
    // Done
    public C3aOperand visit(SaExpLire node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstRead(result,""));
        defaultOut(node);
        return result;
    }

    //Done
    public C3aOperand visit(SaLExp node) throws Exception {
        defaultIn(node);

        SaLExp saLExp = node;

        while (saLExp != null) {
            C3aOperand arg = saLExp.getTete().accept(this);
            c3a.ajouteInst(new C3aInstParam(arg,""));
            saLExp = saLExp.getQueue();
        }
        defaultOut(node);

        return null;
    }


    @Override
    // Done
    public C3aOperand visit(SaInstAffect node) throws Exception
    {
        defaultIn(node);
        //C3aOperand result = c3a.newTemp();
        C3aOperand result = node.getLhs().accept(this);
        C3aOperand left = node.getRhs().accept(this);
        c3a.ajouteInst(new C3aInstAffect(left,result ,""));
        defaultOut(node);
        return result;
    }

    @Override
    // Done
        public C3aOperand visit(SaInstSi node) throws Exception {
        C3aOperand test = node.getTest().accept(this);
        C3aLabel sinon = null;
        if (node.getAlors() != null) sinon = c3a.newAutoLabel();
        C3aLabel labFin = c3a.newAutoLabel();

        C3aLabel jump = (node.getSinon() != null) ? sinon : labFin;
        c3a.ajouteInst(new C3aInstJumpIfEqual(test,c3a.False,jump,""));
        node.getAlors().accept(this);

        if (node.getSinon() != null) {
            c3a.ajouteInst(new C3aInstJump(labFin,""));
            c3a.addLabelToNextInst(sinon);
            node.getSinon().accept(this);
        }
        c3a.addLabelToNextInst(labFin);

        return null;
    }


    @Override
    // Done
    public C3aOperand visit(SaInstRetour node) throws Exception
    {
        defaultIn(node);
        C3aOperand op =node.getVal().accept(this);
        C3aOperand result = c3a.newTemp();
        c3a.ajouteInst(new C3aInstReturn(op," ") );
        c3a.ajouteInst(new C3aInstFEnd(""));
        defaultOut(node);
        return result;
    }

    @Override
    //Done
    public C3aOperand visit(SaVarSimple node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = new C3aVar(node.tsItem, null);
        defaultOut(node);
        return result ;
    }

    @Override
    //Done
    public C3aOperand visit(SaVarIndicee node) throws Exception
    {
        defaultIn(node);
        C3aOperand result = new C3aVar(node.tsItem, node.getIndice().accept(this));
        defaultOut(node);
        return result ;
    }
}
