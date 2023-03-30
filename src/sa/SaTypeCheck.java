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
				System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(SaNode node)
    {
	System.out.println("</" + node.getClass().getSimpleName() + ">");
    }



    @Override
//Done
    public Void visit(SaInstAffect node) throws Exception
    {
        defaultIn(node);
        if (node.getLhs().getTsItem().getType() != node.getRhs().getType()){
            throw new ErrorException(Error.TYPE, "type incorrect");

        }
        node.getLhs().accept(this);
        node.getRhs().accept(this);
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaDecTab node) throws Exception{
        defaultIn(node);
        if(node.getType() != Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }



    @Override
    //Done
    public Void visit(SaAppel node) throws Exception
    {
        defaultIn(node);
        this.fonctionCourante = node.tsItem;
        if(node.getArguments() != null){
            if(node.getArguments().getTete().getType() != this.fonctionCourante.saDecFonc.getParametres().getTete().getType()){
                throw new ErrorException(Error.TYPE, "Type incorrect");
            }
            SaLExp next = node.getArguments().getQueue();
            SaLDecVar nextF = this.fonctionCourante.saDecFonc.getParametres().getQueue();
            while(next != null) {
                if(next.getTete().getType() != nextF.getTete().getType()){
                    throw new ErrorException(Error.TYPE, "Type incorrect");
                }
                next = next.getQueue();
                nextF = nextF.getQueue();
            }
        }


        defaultOut(node);
        return null;
    }

    @Override
    //Done
    public Void visit(SaExpAdd node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() !=Type.ENTIER || node.getOp2().getType()!= Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaExpSub node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != Type.ENTIER || node.getOp2().getType()!= Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaExpMult node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != Type.ENTIER|| node.getOp2().getType()!= Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaExpDiv node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != Type.ENTIER || node.getOp2().getType()!= Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaExpInf node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType()!= Type.ENTIER || node.getOp2().getType()!= Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }
    @Override
    // Done
    public Void visit(SaExpEqual node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != node.getOp2().getType())
            throw new ErrorException(Error.TYPE, "type incorrect");
        node.getOp1().accept(this);
        node.getOp2().accept(this);
        defaultOut(node);
        return null;
    }



    @Override
    // Done
    public Void visit(SaInstRetour node) throws Exception
    {
        defaultIn(node);
        if (node.getVal().getType() != fonctionCourante.typeRetour)
            throw new ErrorException(Error.TYPE, "type incorrect");

        node.getVal().accept(this);
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaVarIndicee node) throws Exception
    {
        defaultIn(node);
        if (node.getIndice().getType() != Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaDecFonc node) throws Exception
    {
        defaultIn(node);

        this.fonctionCourante = node.tsItem;

        if(node.getCorps() != null) node.getCorps().accept(this);
        defaultOut(node);
        return null;
    }


    //Done

    @Override
    public Void visit(SaExpInt node) throws Exception
    {
        defaultIn(node);
        if(node.getType() != Type.ENTIER)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaExpVrai node) throws Exception
    {
        defaultIn(node);
        if(node.getType() != Type.BOOL)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaExpFaux node) throws Exception
    {
        defaultIn(node);
        if(node.getType() != Type.BOOL)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }

    @Override
    //Done
    public Void visit(SaInstTantQue node) throws Exception
    {
        defaultIn(node);
        if(node.getTest().getType() != Type.BOOL)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }


    @Override
    // Done
    public Void visit(SaExpAnd node) throws Exception
    {
        defaultIn(node);
        if(node.getOp1().getType()!= Type.BOOL || node.getOp2().getType()!= Type.BOOL )
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }

    @Override
    // Done
    public Void visit(SaExpOr node) throws Exception
    {
        defaultIn(node);
        if(node.getOp1().getType()!= Type.BOOL || node.getOp2().getType()!= Type.BOOL )
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    // Done
    public Void visit(SaExpNot node) throws Exception
    {
        defaultIn(node);
        if (node.getOp1().getType() != Type.BOOL)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }
    @Override
    //Done
    public Void visit(SaInstSi node) throws Exception
    {
        defaultIn(node);
        if(node.getTest().getType() != Type.BOOL)
            throw new ErrorException(Error.TYPE, "type incorrect");
        defaultOut(node);
        return null;
    }

}
