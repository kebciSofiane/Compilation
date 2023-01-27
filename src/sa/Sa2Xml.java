package sa;
import java.io.*;

public class Sa2Xml extends SaDepthFirstVisitor < Void > {
    private int indentation = 0;
    private String baseFileName;
    private String fileName;
    private PrintStream out;
    private String childName;


    public Sa2Xml(SaNode root, String baseFileName)
    {
	if (baseFileName == null){
	    this.out = System.out;	    
	}
	else{
	    try {
		this.baseFileName = baseFileName;
		this.fileName = baseFileName + ".sa";
		this.out = new PrintStream(this.fileName);
	    }
	    
	    catch (IOException e) {
		System.err.println("Error: " + e.getMessage());
	    }
	}
	childName = "programme";
	try{
	    root.accept(this);
	}
	catch(Exception e){}
    }

    public void printOpenTag(String TagName){
	printOpenTag(TagName, null, null);
    }

    public void printOpenTag(String TagName, SaNode node){
	printOpenTag(TagName, node, null);
    }
    
    public void printOpenTag(String TagName, String attrValList){
	printOpenTag(TagName, null, attrValList);
    }

    public void printOpenTag(String TagName, SaNode node, String attrValList){
	for(int i = 0; i < indentation; i++){this.out.print(" ");}
	indentation++;
	if(node != null)
	    this.out.print("<" + TagName + " type=" + "\"" + node.getClass().getSimpleName() + "\"");
	else
	    this.out.print("<" + TagName);
	if(attrValList != null)
	    this.out.print(attrValList);
	this.out.println(">");
    }

    public void printCloseTag(String TagName)
    {
	indentation--;
	for(int i = 0; i < indentation; i++){this.out.print(" ");}
	this.out.println("</" + TagName + ">");
    }

    public void printOpenCloseTag(String TagName){
	printOpenTag(TagName, null, null);
    }

    public void printOpenCloseTag(String TagName, SaNode node){
	printOpenCloseTag(TagName, node, null);
    }
    
    public void printOpenCloseTag(String TagName, String attrValList){
	printOpenCloseTag(TagName, null, attrValList);
    }

    public void printOpenCloseTag(String TagName, SaNode node, String attrValList){
	for(int i = 0; i < indentation; i++){this.out.print(" ");}
	//	indentation++;
	if(node != null)
	    this.out.print("<" + TagName + " type=" + "\"" + node.getClass().getSimpleName() + "\"");
	else
	    this.out.print("<" + TagName);
	if(attrValList != null)
	    this.out.print(attrValList);
	this.out.println("/>");
    }

    // P -> LDEC LDEC 
    public Void visit(SaProg node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	if(node.getVariables() != null){
	    this.childName = "variables";
	    node.getVariables().accept(this);
	}
	
	if(node.getFonctions() != null){
	    this.childName = "fonctions";
	    node.getFonctions().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }


    // DEC -> var id taille 
    public Void visit(SaDecTab node) throws Exception{
	String nodeName = this.childName;
	String attrValList = " nom=\"" + node.getNom() + "\"" + " taille=\"" + node.getTaille() + "\"" + " typeVariable=\"" + node.getType().nom() + "\"";
	printOpenTag(nodeName, node, attrValList);
	printCloseTag(nodeName);
	return null;
    }
    
    // EXP -> entier
    public Void visit(SaExpInt node) throws Exception
    {
	String nodeName = this.childName;
	String attrValList = " val=\"" + node.getVal() + "\"";
	printOpenCloseTag(nodeName, node, attrValList);
	//	printCloseTag(nodeName);
	//	printOpenCloseTag(nodeName, node, attrValList);
	return null;
    }

    // EXP -> vrai
    public Void visit(SaExpVrai node) throws Exception
    {
	String nodeName = this.childName;
	//	String attrValList = " val=\"" + node.getVal() + "\"";
	String attrValList = null;
	printOpenCloseTag(nodeName, node, attrValList);
	return null;
    }

    // EXP -> faux
    public Void visit(SaExpFaux node) throws Exception
    {
	String nodeName = this.childName;
	//	String attrValList = " val=\"" + node.getVal() + "\"";
	String attrValList = null;
	printOpenCloseTag(nodeName, node, attrValList);
	return null;
    }

    public Void visit(SaExpVar node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "var";
	node.getVar().accept(this);
	printCloseTag(nodeName);
	return null;
    }
    
    public Void visit(SaInstEcriture node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "arg";
	node.getArg().accept(this);
	printCloseTag(nodeName);
	return null;
    }
    
    public Void visit(SaInstTantQue node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "test";
	node.getTest().accept(this);
	if (node.getFaire() != null){
	    childName = "faire";
	    node.getFaire().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }
    
    public Void visit(SaLInst node) throws Exception
    {
	if(node == null) return null;
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "tete";
	node.getTete().accept(this);
	
	if(node.getQueue() != null){
	    childName = "queue";
	    node.getQueue().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }

    // DEC -> fct id LDEC LDEC LINST 

    public Void visit(SaDecFonc node) throws Exception
    {
	String nodeName = this.childName;
	String attrValList = " nom=\"" + node.getNom() + "\"" + " typeRetour=\"" + node.getTypeRetour().nom() + "\"";
	
	printOpenTag(nodeName, node, attrValList);
	if(node.getParametres() != null){
	    childName = "parametres";
	    node.getParametres().accept(this);
	}

	if(node.getVariable() != null){
	    childName = "variables";
	    node.getVariable().accept(this);
	}
	if (node.getCorps() != null){
	    childName = "corps";
	    node.getCorps().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }
    
    // DEC -> var id 
    public Void visit(SaDecVar node) throws Exception
    {
	String nodeName = this.childName;
	String attrValList = " nom=\"" + node.getNom() + "\"" + " typeVariable=\"" + node.getType().nom() + "\"";
	printOpenCloseTag(nodeName, node, attrValList);
	return null;
    }
    
    public Void visit(SaInstAffect node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	this.childName = "lhs";
	node.getLhs().accept(this);
	this.childName = "rhs";
	node.getRhs().accept(this);
	printCloseTag(nodeName);
	return null;
    }
    
    // LDEC -> DEC LDEC 
    // LDEC -> null 
    /*    public Void visit(SaLDec node)
    {
	String nodeName = this.childName;

	printOpenTag(nodeName, node);
	childName="tete";
	node.getTete().accept(this);

	if(node.getQueue() != null){
	    childName="queue";
	    node.getQueue().accept(this);
	}
	printCloseTag(nodeName);

	return null;
	}*/

    public Void visit(SaLDecVar node) throws Exception
    {
	String nodeName = this.childName;

	printOpenTag(nodeName, node);
	childName="tete";
	node.getTete().accept(this);

	if(node.getQueue() != null){
	    childName="queue";
	    node.getQueue().accept(this);
	}
	printCloseTag(nodeName);

	return null;
    }

    public Void visit(SaLDecFonc node) throws Exception
    {
	String nodeName = this.childName;

	printOpenTag(nodeName, node);
	childName="tete";
	node.getTete().accept(this);

	if(node.getQueue() != null){
	    childName="queue";
	    node.getQueue().accept(this);
	}
	printCloseTag(nodeName);

	return null;
    }


    public Void visit(SaVarSimple node) throws Exception
    {
	String nodeName = this.childName;
	String attrValList = " nom=\"" + node.getNom() + "\"";

	printOpenCloseTag(nodeName, node, attrValList);
	return null;
    }

    
    public Void visit(SaAppel node) throws Exception
    {
	String nodeName = this.childName;
	String attrValList = " nom=\"" + node.getNom() + "\"";
	
	printOpenTag(nodeName, node, attrValList);
	if(node.getArguments() != null){
	    childName = "arguments";
	    node.getArguments().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }
    
    public Void visit(SaExpAppel node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "val";
	node.getVal().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    // EXP -> add EXP EXP
    public Void visit(SaExpAdd node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    // EXP -> sub EXP EXP
    public Void visit(SaExpSub node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;

    }

    // EXP -> mult EXP EXP
    public Void visit(SaExpMult node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    // EXP -> div EXP EXP
    public Void visit(SaExpDiv node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }
    
    // EXP -> inf EXP EXP
    public Void visit(SaExpInf node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    // EXP -> eq EXP EXP
    public Void visit(SaExpEqual node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    // EXP -> and EXP EXP
    public Void visit(SaExpAnd node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }
    

    // EXP -> or EXP EXP
    public Void visit(SaExpOr node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	childName = "op2";
	node.getOp2().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    // EXP -> not EXP
    public Void visit(SaExpNot node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "op1";
	node.getOp1().accept(this);
	printCloseTag(nodeName);
	return null;
    }


    public Void visit(SaExpLire node) throws Exception
    {
	String nodeName = this.childName;
	printOpenCloseTag(nodeName, node);
	return null;
    }

    public Void visit(SaInstBloc node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "val";
	node.getVal().accept(this);
	printCloseTag(nodeName);
	return null;
    }
    
    public Void visit(SaInstSi node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "test";
	node.getTest().accept(this);
	if (node.getAlors() != null){
	    childName = "alors";
	    node.getAlors().accept(this);
	}
	if(node.getSinon() != null){
	    childName = "sinon";
	    node.getSinon().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }

// INST -> ret EXP 
    public Void visit(SaInstRetour node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "val";
	node.getVal().accept(this);
	printCloseTag(nodeName);
	return null;
    }

    
    public Void visit(SaLExp node) throws Exception
    {
	String nodeName = this.childName;
	printOpenTag(nodeName, node);
	childName = "tete";
	node.getTete().accept(this);
	if(node.getQueue() != null){
	    childName = "queue";
	    node.getQueue().accept(this);
	}
	printCloseTag(nodeName);
	return null;
    }
    public Void visit(SaVarIndicee node) throws Exception
    {
	String nodeName = this.childName;
	String attrValList = " nom=\"" + node.getNom() + "\"";

	printOpenTag(nodeName, node, attrValList);
	childName = "indice";
	node.getIndice().accept(this);
	printCloseTag(nodeName);
	return null;
    }




    
}
