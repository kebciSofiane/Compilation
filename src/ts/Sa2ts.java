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
	@Override
	public Void visit(SaDecVar node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();
		Type type = node.getType();
		if(this.context == Context.GLOBAL){
			if(this.tableGlobale.getVar(node.getNom()) != null){
				throw new ErrorException(Error.TS, "La variable existe déjà");
			}
			node.setTsItem( tableGlobale.addVar(identif, type));
		}
		if(this.context == Context.LOCAL){
			if(this.tableLocaleCourante.getVar(node.getNom())!= null){
				throw new ErrorException(Error.TS, "La variable existe déjà");
			}
			node.setTsItem(tableLocaleCourante.addVar(identif, type));
		}
		if(this.context == Context.PARAM){
			if(this.tableLocaleCourante.getVar(node.getNom())!= null){
				throw new ErrorException(Error.TS, "La variable existe déjà");
			}
			node.setTsItem(tableLocaleCourante.addParam(identif, type));
		}
		defaultOut(node);
		return null;
	}



	//Done
	@Override

	public Void visit(SaDecTab node) throws Exception{

		defaultIn(node);
		String identif = node.getNom();
		Type type = node.getType();
		int taille = node.getTaille();
		if(this.context == Context.GLOBAL){
			if(this.tableGlobale.getVar(node.getNom()) != null){
				throw new ErrorException(Error.TS, "La variable existe déjà");
			}
			node.tsItem = tableGlobale.addTab(identif, type, taille);
		}
		defaultOut(node);
		return null;
	}

	//Done
	@Override

	public Void visit(SaDecFonc node) throws Exception
	{
		defaultIn(node);

		if(this.getTableGlobale().getFct(node.getNom()) != null){
			throw new ErrorException(Error.TS, "La fonction existe déjà");
		}
		this.tableLocaleCourante = new Ts();
		this.context = Context.PARAM;
		String identif = node.getNom();
		Type typeDeRetour = node.getTypeRetour();

		int nbArgs;



		if (node.getParametres() == null){
			nbArgs = 0;
		}
		else {
				nbArgs = node.getParametres().length();
		}

		if(node.getParametres() != null) node.getParametres().accept(this);

		context = Context.LOCAL;
		if(node.getVariable() != null) node.getVariable().accept(this);



		node.tsItem  = tableGlobale.addFct(identif, typeDeRetour, nbArgs,this.tableLocaleCourante,node);

		if(node.getCorps() != null) node.getCorps().accept(this);
		defaultOut(node);
		return null;
	}

	//Done
	@Override

	public Void visit(SaVarSimple node) throws Exception
	{
		defaultIn(node);
		TsItemVar item = null;
		String identif = node.getNom();
		if( tableLocaleCourante.getVar(identif ) != null){
			if(tableLocaleCourante.getVar(identif) instanceof TsItemVarTab){
				throw new ErrorException(Error.TS, "Ce n'est pas une variable simple");
			}
			item = tableLocaleCourante.getVar(node.getNom());
		}
		else if( tableGlobale.getVar(identif ) != null){
			if(tableGlobale.getVar(identif) instanceof TsItemVarTab){
				throw new ErrorException(Error.TS, "Ce n'est pas une variable simple..");
			}
			item = tableGlobale.getVar(node.getNom());
		}
		else throw new ErrorException(Error.TS, "La variable n'existe pas");
		node.tsItem = (TsItemVarSimple) item;
		defaultOut(node);
		return null;
	}

	//Done
	@Override

	public Void visit(SaVarIndicee node) throws Exception
	{
		defaultIn(node);
		String identif = node.getNom();
		TsItemVar item ;
		if( tableGlobale.getVar(identif ) != null){
			if( ! (tableGlobale.getVar(identif) instanceof TsItemVarTab)){
				throw new ErrorException(Error.TS, "Ce n'est pas une variable indicee");
			}
			if(node.getIndice() == null){
				throw new ErrorException(Error.TS, "L'indice ne doit pas ete null");

			}
			item = tableGlobale.getVar(node.getNom());
		}
		else throw new ErrorException(Error.TS, "La variable n'existe pas");
		node.getIndice().accept(this); //
		node.tsItem =  item;
		defaultOut(node);
		return null;
	}


	//Done
	@Override


	public Void visit(SaAppel node) throws Exception {
		defaultIn(node);
		String identif = node.getNom();
		TsItemFct item = null;
		int nbArgs = 0;
		if (node.getArguments() != null)
			nbArgs = node.getArguments().length();

		//System.out.println("nombre de parametres = " + nbArgs);

		TsItemFct fonction = tableGlobale.getFct(identif);
		//System.out.println("nombre de parametres dans la ts = " + fonction.getNbArgs());
		if (fonction != null) {
			node.tsItem = fonction;
				if(nbArgs != fonction.getNbArgs())
					throw new ErrorException(Error.TS, "Mauvais nombre d'arguments ");
			}
		else throw new ErrorException(Error.TS,"La fonction n'existe pas");
		node.tsItem =  item;
		defaultOut(node);
		return null;
	}
}
