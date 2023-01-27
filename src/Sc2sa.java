import sc.analysis.*;
import sc.node.*;
import sa.*;
import util.Type;

public class Sc2sa extends DepthFirstAdapter
{
    private SaNode returnValue;
    private Type returnType;

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
	//System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
	//System.out.println("</" + node.getClass().getSimpleName() + ">");
    }
    
    public SaNode getRoot()
    {
	return this.returnValue;
    }


    // un exemple de méthode associé à la règle 
    // exp3 = {plus} exp3 plus exp4 
    @Override
    public void caseAPlusExp3(APlusExp3 node)
    {
	SaExp op1 = null;
	SaExp op2 = null;
        inAPlusExp3(node);
	node.getExp3().apply(this);
	op1 = (SaExp) this.returnValue;
	node.getExp4().apply(this);
	op2 = (SaExp) this.returnValue;
	this.returnValue = new SaExpAdd(op1, op2);
        outAPlusExp3(node);
    }
}
