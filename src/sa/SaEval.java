package sa;
import java.util.*;
import java.io.*;
import ts.*;
import util.Memory;
import util.Type;


public class SaEval extends SaDepthFirstVisitor <TypeVal> {
    private Ts tableGlobale;
    private SaEnvironment curEnv;
    private Memory varGlob;
    private ArrayList<String> programOutput = new ArrayList<String>();
    private boolean stop;

    public TypeVal getVar(SaVarSimple saVar){
	    TsItemVarSimple tsItem = saVar.getTsItem();
	    return getVar(tsItem);
    }
    
    public TypeVal getVar(TsItemVarSimple tsItem){
	    Memory container = null;
	    if(tsItem.portee == this.tableGlobale) // variable globale
		container = varGlob;
	    else
		container = (tsItem.isParam)? curEnv.args : curEnv.vars;
	    if(tsItem.type == Type.ENTIER)
		return new TypeVal(container.readInt(tsItem.adresse));
	    //	    if(tsItem.type == Type.BOOL)
	    return new TypeVal(container.readBoolean(tsItem.adresse));
    }

    public void setVar(SaVarSimple saVar, TypeVal typeVal){
	    TsItemVarSimple tsItem = saVar.getTsItem();
	    setVar(tsItem, typeVal);
    }

		   
    public void setVar(TsItemVarSimple tsItem, TypeVal typeVal){
	Memory container = null;
	// trois cas possibles : une variable locale, une variable globale ou un argument
	if(tsItem.portee == this.tableGlobale) // variable globale
	    container = varGlob;
	else if (tsItem.isParam)
	    container = curEnv.args;
	else
	    container = curEnv.vars;

	if(tsItem.type == Type.ENTIER)
	    container.writeInt(tsItem.adresse, typeVal.valInt);
	if(tsItem.type == Type.BOOL)
	    container.writeBoolean(tsItem.adresse, typeVal.valBool);
    }
    
    private TypeVal getVarGlobIndicee(SaVarIndicee saVarIndicee, TypeVal indice){
	TsItemVar tsItem = saVarIndicee.getTsItem();
	if(tsItem.type == Type.ENTIER)
	    return new TypeVal(varGlob.readInt(tsItem.adresse + tsItem.type.taille() * indice.valInt));
	//	if(tsItem.type == Type.BOOL)
	    return new TypeVal(varGlob.readBoolean(tsItem.adresse + tsItem.type.taille() * indice.valInt));
    }
		       
    private void setVarGlobIndicee(SaVarIndicee saVarIndicee, TypeVal indice, TypeVal typeVal){
	TsItemVar tsItem = saVarIndicee.getTsItem();
	if(tsItem.type == Type.ENTIER)
	    varGlob.writeInt(tsItem.adresse + tsItem.type.taille() * indice.valInt, typeVal.valInt);
	if(tsItem.type == Type.BOOL)
	    varGlob.writeBoolean(tsItem.adresse + tsItem.type.taille() * indice.valInt , typeVal.valBool);
    }
		       
    public SaEval(SaNode root, Ts tableGlobale){
	    this.tableGlobale = tableGlobale;
	    curEnv = null;
	    varGlob = new Memory(tableGlobale.getAdrVarCourante(), 0);
	    stop = false;

	    SaAppel appelMain = new SaAppel("main", null);
	    appelMain.tsItem = tableGlobale.getFct("main");
	    
	    try{
		appelMain.accept(this);
	    }
	    catch(Exception e){}
    }

    public void affiche(String baseFileName){
  	  String fileName;
  	  PrintStream out = System.out;
	  
  	  if (baseFileName != null){
	      try {
		  baseFileName = baseFileName;
		  fileName = baseFileName + ".saout";
		  out = new PrintStream(fileName);
	      }
	      
	      catch (IOException e) {
		  System.err.println("Error: " + e.getMessage());
	      }
  	  }
	  for (String line : programOutput)
	      out.println(line);
    }


    
    public void defaultIn(SaNode node)
    {
    }

    public void defaultOut(SaNode node)
    {
    }

    // P -> LDEC LDEC 
    public TypeVal visit(SaProg node) throws Exception
    {
	defaultIn(node);
	if(node.getVariables() != null)
	    node.getVariables().accept(this);
	if(node.getFonctions() != null)
	    node.getFonctions().accept(this);
	defaultOut(node);
	return null;
    }
    
    // DEC -> var id taille 
    public TypeVal visit(SaDecTab node) throws Exception{
	defaultIn(node);
	defaultOut(node);
	return null;
    }
    
    public TypeVal visit(SaExp node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }
    
    // EXP -> entier
    public TypeVal visit(SaExpInt node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return new TypeVal(node.getVal());
    }


    // EXP -> vrai
    public TypeVal visit(SaExpVrai node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return new TypeVal(true);
    }
    
    // EXP -> faux
    public TypeVal visit(SaExpFaux node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return new TypeVal(false);
    }
        
    public TypeVal visit(SaExpVar node) throws Exception
    {
	defaultIn(node);
	TypeVal typeVal = node.getVar().accept(this);
	defaultOut(node);
	return typeVal;
    }
    
    public TypeVal visit(SaInstEcriture node) throws Exception
    {
	defaultIn(node);
	TypeVal arg = node.getArg().accept(this);
	if(arg.type == Type.ENTIER)
	    programOutput.add(String.valueOf(arg.valInt));
	else if(arg.type == Type.BOOL){
	    if(arg.valBool == true)
		programOutput.add(String.valueOf(1));
	    else
		programOutput.add(String.valueOf(0));
	}
	defaultOut(node);
	return null;
    }
    
    public TypeVal visit(SaInstTantQue node) throws Exception
    {
	defaultIn(node);
	TypeVal test = node.getTest().accept(this);
	while (test.valBool == true){
	    if (node.getFaire() != null)
		node.getFaire().accept(this);
	    else{
		System.out.println("Infinite loop detected, breaking out");
		break;
	    }
	    test = node.getTest().accept(this);
	}
	defaultOut(node);
	return null;
    }
    
    public TypeVal visit(SaLInst node) throws Exception
    {
	defaultIn(node);
	stop = false;
	TypeVal valRet = node.getTete().accept(this);
	if(!stop && node.getQueue() != null) node.getQueue().accept(this);
	defaultOut(node);
	return null;
    }

    // DEC -> fct id LDEC LDEC LINST 
    public TypeVal visit(SaDecFonc node) throws Exception
    {
	defaultIn(node);
	if(node.getParametres() != null) node.getParametres().accept(this);
	if(node.getVariable() != null) node.getVariable().accept(this);
	node.getCorps().accept(this);
	defaultOut(node);
	return null;
    }
    
    // DEC -> var id 
    public TypeVal visit(SaDecVar node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }
    
    public TypeVal visit(SaInstAffect node) throws Exception
    {
	defaultIn(node);
	TypeVal typeVal = node.getRhs().accept(this);
	if(node.getLhs() instanceof SaVarIndicee){ // c'est une case de tableau, donc forcément globale
	    SaVarIndicee lhsIndicee = (SaVarIndicee) node.getLhs();
	    TypeVal indice = lhsIndicee.getIndice().accept(this);
	    setVarGlobIndicee(lhsIndicee, indice, typeVal);
	}
	else{
	    setVar((SaVarSimple) node.getLhs(), typeVal);
	}
	
	defaultOut(node);
	return null;
    }
    
    // LDEC -> DEC LDEC 
    // LDEC -> null 
    /*    public TypeVal visit(SaLDec node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null) node.getQueue().accept(this);
	defaultOut(node);
	return null;
	}*/
    
    public TypeVal visit(SaVarSimple node) throws Exception
    {
	defaultIn(node);
	TypeVal typeVal = getVar(node);
	defaultOut(node);
	return typeVal;
    }
    
    public TypeVal visit(SaAppel node) throws Exception
    {
	defaultIn(node);
	TsItemFct fct = node.tsItem;
	SaLExp lArgs = null;
	SaLDecVar lParam = null; 
	Ts localTable = fct.getTable();

	// on construit la liste des arguments d'appel de la fonction
	ArrayList<TypeVal> listeArguments = new ArrayList<TypeVal>();
	for(lArgs = node.getArguments(); lArgs != null; lArgs = lArgs.getQueue()){
	    listeArguments.add(lArgs.getTete().accept(this));
	}
	
	//on sauvegarde de l'environnement courant pour le restaurer après l'appel
	SaEnvironment oldEnv = curEnv;
	// on crée un nouvel environnement pour la fonction appelée
	// le nouvel environnement est l'environnement courant
	curEnv = new SaEnvironment(fct);

	// on stocke les valeurs des arguments dans l'environnement
	int n = 0;
	for(lParam = fct.saDecFonc.getParametres(); lParam != null; lParam = lParam.getQueue()){
	    setVar((TsItemVarSimple)((SaDecVar)lParam.getTete()).getTsItem(), listeArguments.get(n));
	    n++;
	}
	// on exécute le corps de la fonction
	if(fct.saDecFonc.getCorps() != null)
	    fct.saDecFonc.getCorps().accept(this);

	// on récupère la valeur de retour
	TypeVal returnValue = curEnv.getReturnValue();
	
	//restauration de l'environnement d'avant appel
	curEnv = oldEnv;
	defaultOut(node);
	return returnValue;
    }
    
    public TypeVal visit(SaExpAppel node) throws Exception
    {
	defaultIn(node);
	TypeVal typeVal = node.getVal().accept(this);
	defaultOut(node);
	return typeVal;
    }

    // EXP -> add EXP EXP
    public TypeVal visit(SaExpAdd node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal(op1.valInt + op2.valInt);
    }

    // EXP -> sub EXP EXP
    public TypeVal visit(SaExpSub node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal(op1.valInt - op2.valInt);
    }

    // EXP -> mult EXP EXP
    public TypeVal visit(SaExpMult node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal(op1.valInt * op2.valInt);
    }

    // EXP -> div EXP EXP
    public TypeVal visit(SaExpDiv node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal((int) op1.valInt / op2.valInt);
    }
    
    // EXP -> inf EXP EXP
    public TypeVal visit(SaExpInf node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal((op1.valInt < op2.valInt)? true : false);
    }

    // EXP -> eq EXP EXP
    public TypeVal visit(SaExpEqual node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	if(op1.type == Type.ENTIER)
	    return new TypeVal((op1.valInt == op2.valInt)? true : false);
	else
	    return new TypeVal((op1.valBool == op2.valBool)? true : false);
    }

    // EXP -> and EXP EXP
    public TypeVal visit(SaExpAnd node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal(op1.valBool && op2.valBool);
    }
    
    // EXP -> or EXP EXP
    public TypeVal visit(SaExpOr node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	TypeVal op2 = node.getOp2().accept(this);
	defaultOut(node);
	return new TypeVal(op1.valBool || op2.valBool);
    }

    // EXP -> not EXP
    public TypeVal visit(SaExpNot node) throws Exception
    {
	defaultIn(node);
	TypeVal op1 = node.getOp1().accept(this);
	defaultOut(node);
	return new TypeVal(!op1.valBool);
    }


    public TypeVal visit(SaExpLire node) throws Exception
    {
	defaultIn(node);
	defaultOut(node);
	return null;
    }

    public TypeVal visit(SaInstBloc node) throws Exception
    {
	defaultIn(node);
	node.getVal().accept(this);
	defaultOut(node);
	return null;
    }
    
    public TypeVal visit(SaInstSi node) throws Exception
    {
	defaultIn(node);
	TypeVal test = node.getTest().accept(this);
	if(test.valBool && node.getAlors() != null)
	    node.getAlors().accept(this);
	if(!test.valBool && node.getSinon() != null)
	    node.getSinon().accept(this);
	defaultOut(node);
	return null;
    }

// INST -> ret EXP 
    public TypeVal visit(SaInstRetour node) throws Exception
    {
	defaultIn(node);
	curEnv.setReturnValue(node.getVal().accept(this));
	stop = true;
	defaultOut(node);
	return null;
    }

    
    public TypeVal visit(SaLExp node) throws Exception
    {
	defaultIn(node);
	node.getTete().accept(this);
	if(node.getQueue() != null)
	    node.getQueue().accept(this);
	defaultOut(node);
	return null;
    }

    public TypeVal visit(SaVarIndicee node) throws Exception
    {
	defaultIn(node);
	node.getIndice().accept(this);
	TypeVal indice = node.getIndice().accept(this);
	defaultOut(node);
	return getVarGlobIndicee(node, indice);
    }
    
}
