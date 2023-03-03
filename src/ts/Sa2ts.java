package ts;
import sa.*;
import sc.node.Node;
import util.Error;
import util.Type;

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


	public Void visit(SaDecVar node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();
		Type type = node.getType();
		if(this.context == Context.GLOBAL){
			if(this.tableGlobale.getVar(node.getNom()) != null){
				throw new ErrorException(Error.TS, "La vairiable existe déja");
			}
			tableGlobale.addVar(identif, type);
		}
		if(this.context == Context.LOCAL){
			if(this.tableLocaleCourante.getVar(node.getNom())!= null){
				throw new ErrorException(Error.TS, "La vairiable existe déja");
			}
			tableLocaleCourante.addVar(identif, type);
		}
		if(this.context == Context.PARAM){
			if(this.tableLocaleCourante.getVar(node.getNom())!= null){
				throw new ErrorException(Error.TS, "La vairiable existe déja");
			}
			tableLocaleCourante.addParam(identif, type);
		}
		defaultOut(node);
		return null;
	}



	//todo DEC -> var id taille fini
	public Void visit(SaDecTab node) throws Exception{

		defaultIn(node);
		String identif = node.getNom();
		Type type = node.getType();
		int taille = node.getTaille();
		if(this.context == Context.GLOBAL){
			if(this.tableGlobale.getVar(node.getNom()) != null){
				throw new ErrorException(Error.TS, "La vairiable existe déja");
			}
			tableGlobale.addTab(identif, type, taille);
		}
		defaultOut(node);
		return null;
	}

	//todo DEC -> fct id LDEC LDEC LINST
	public Void visit(SaDecFonc node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();

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
