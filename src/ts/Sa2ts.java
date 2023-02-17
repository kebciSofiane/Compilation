package ts;
import sa.*;
import sc.node.Node;
import util.Error;

import java.util.Map;

public class Sa2ts extends SaDepthFirstVisitor <Void> {
    enum Context {
	LOCAL,
	GLOBAL,
	PARAM
    }
    
    private Ts tableGlobale;
    private Ts tableLocaleCourante;
    private Context context;
    
    public Ts getTableGlobale(){return this.tableGlobale;}

    public Sa2ts(SaNode root)
    {
	tableGlobale = new Ts();
	tableLocaleCourante = null;
	context = Context.GLOBAL;
	try{
	    root.accept(this);
	    if(tableGlobale.getFct("main") == null)
		throw new ErrorException(Error.TS, "la fonction main n'est pas définie");
	}
	catch(ErrorException e){
	    System.err.print("ERREUR TABLE DES SYMBOLES : ");
	    System.err.println(e.getMessage());
	    System.exit(e.getCode());
	}
	catch(Exception e){}
    }

	//todo  DEC -> var id
	public Void visit(SaDecVar node) throws Exception
	{
		defaultIn(node);
		defaultOut(node);
		return null;
	}



	//todo DEC -> var id taille
	public Void visit(SaDecTab node) throws Exception{
		defaultIn(node);
		defaultOut(node);
		return null;
	}

	//todo DEC -> fct id LDEC LDEC LINST
	public Void visit(SaDecFonc node) throws Exception
	{
		defaultIn(node);
		if(node.getParametres() != null) node.getParametres().accept(this);
		if(node.getVariable() != null) node.getVariable().accept(this);
		if(node.getCorps() != null) node.getCorps().accept(this);
		defaultOut(node);
		return null;
	}

	//todo
	public Void visit(SaVarSimple node) throws Exception
	{
		defaultIn(node);
		defaultOut(node);
		return null;
	}

	//todo
	public Void visit(SaVarIndicee node) throws Exception
	{
		defaultIn(node);
		node.getIndice().accept(this);
		defaultOut(node);
		return null;
	}


	//todo
	public Void visit(SaAppel node) throws Exception
	{
		defaultIn(node);
		if(node.getArguments() != null) node.getArguments().accept(this);
		defaultOut(node);
		return null;
	}
}
