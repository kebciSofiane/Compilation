package sa;
import util.Type;
import util.Error;
import ts.*;

public class SaTypeCheck extends SaDepthFirstVisitor <Void>{
    private TsItemFct fonctionCourante;

    public SaTypeCheck(SaNode root)
    {
	try{
	root.accept(this);
	}
	catch(ErrorException e){
	    System.err.print("ERREUR DE TYPAGE : ");
	    System.err.println(e.getMessage());
	    System.exit(e.getCode());
	}
	catch(Exception e){}
    }

    public void defaultIn(SaNode node)
    {
	//			System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(SaNode node)
    {
	//		System.out.println("</" + node.getClass().getSimpleName() + ">");
    }





    public Void visit(SaInstAffect node) throws Exception
    {
        defaultIn(node);
        node.getLhs().accept(this);
        node.getRhs().accept(this);
        defaultOut(node);
        return null;
    }

    // LDEC -> DEC LDEC
    // LDEC -> null
    /*    public T visit(SaLDec node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null) node.getQueue().accept(this);
	defaultOut(node);
	return null;
	}*/


    public Void visit(SaVarSimple node) throws Exception
    {
        defaultIn(node);
        defaultOut(node);
        return null;
    }

    public Void visit(SaAppel node) throws Exception
    {
        defaultIn(node);
        if(node.getArguments() != null) node.getArguments().accept(this);
        defaultOut(node);
        return null;
    }

    public Void visit(SaExpAppel node) throws Exception
    {
        defaultIn(node);
        node.getVal().accept(this);
        defaultOut(node);
        return null;
    }

    // EXP -> add EXP EXP
    public Void visit(SaExpAdd node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != node.getOp2().getType() || node.getType()!=node.getOp2().getType())
            throw new ErrorException(Error.TS, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }

    // EXP -> sub EXP EXP
    public Void visit(SaExpSub node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != node.getOp2().getType() || node.getType()!=node.getOp2().getType())
            throw new ErrorException(Error.TS, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }

    // EXP -> mult EXP EXP
    public Void visit(SaExpMult node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != node.getOp2().getType() || node.getType()!=node.getOp2().getType())
            throw new ErrorException(Error.TS, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }

    // EXP -> div EXP EXP
    public Void visit(SaExpDiv node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != node.getOp2().getType() || node.getType()!=node.getOp2().getType())
            throw new ErrorException(Error.TS, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }

    // EXP -> inf EXP EXP
    public Void visit(SaExpInf node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType()!= Type.ENTIER || node.getOp2().getType()!= Type.ENTIER)
            throw new ErrorException(Error.TS, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }

    // EXP -> eq EXP EXP
    public Void visit(SaExpEqual node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != node.getOp2().getType())
            throw new ErrorException(Error.TS, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }




    // INST -> ret EXP
    public Void visit(SaInstRetour node) throws Exception
    {
        defaultIn(node);
        if (node.getVal().getType() != fonctionCourante.typeRetour)
            throw new ErrorException(Error.TS, "type incorrect");
        node.getVal().accept(this);
        defaultOut(node);
        return null;
    }




}
