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

	//Done

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



	//Done
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

	//Done
	public Void visit(SaDecFonc node) throws Exception
	{
		defaultIn(node);
		this.tableLocaleCourante = new Ts();
		String identif = node.getNom();
		Type typeDeRetour = node.getTypeRetour();


		int nbArgs;

		this.context = Context.PARAM;

		if (node.getParametres() == null){ //pas sur
			nbArgs = 0;
		}
		else {
				nbArgs = node.getParametres().length();
		}

		if(node.getParametres() != null) node.getParametres().accept(this);

		context = Context.LOCAL;
		if(node.getVariable() != null) node.getVariable().accept(this);


		if(this.tableGlobale.getFct(node.getNom()) != null){
				throw new ErrorException(Error.TS, "La fonction existe déja");
		}
		tableGlobale.addFct(identif, typeDeRetour, nbArgs,this.tableLocaleCourante,node);

		if(node.getCorps() != null) node.getCorps().accept(this);
		defaultOut(node);
		return null;
	}

	//Done
	public Void visit(SaVarSimple node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();
		if( tableLocaleCourante.getVar(identif ) != null){
			if(tableLocaleCourante.getVar(identif) instanceof TsItemVarTab){
				throw new ErrorException(Error.TS, "Ce n'est pas une variable simple");
			}
		}
		else if( tableGlobale.getVar(identif ) != null){
			if(tableGlobale.getVar(identif) instanceof TsItemVarTab){
				throw new ErrorException(Error.TS, "Ce n'est pas une variable simple..");
			}
		}
		else throw new ErrorException(Error.TS, "La variable n'existe pas");

		defaultOut(node);
		return null;
	}

	//Done
	public Void visit(SaVarIndicee node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();

		if( tableGlobale.getVar(identif ) != null){
			if( ! (tableGlobale.getVar(identif) instanceof TsItemVarTab)){
				throw new ErrorException(Error.TS, "Ce n'est pas une variable indicee");
			}
			if(node.getIndice() == null){
				throw new ErrorException(Error.TS, "L'indice ne doit pas ete null");

			}
		}
		else throw new ErrorException(Error.TS, "La variable n'existe pas");
		node.getIndice().accept(this);
		defaultOut(node);
		return null;
	}


	//todo
	public Void visit(SaAppel node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();
		int nbArg = node.getArguments().length();
		if( tableGlobale.getFct(identif ) != null){
			if(nbArg != tableGlobale.getFct(identif).getNbArgs()){
				throw new ErrorException(Error.TS, "Mauvais nombre d'arguments");
			}
		}
		else {
			throw new ErrorException(Error.TS, "La fonction n'existe pas");
		}
		defaultOut(node);
		return null;
	}
}
