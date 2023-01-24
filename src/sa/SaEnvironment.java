package sa;
import java.util.*;
import ts.*;
import util.Memory;

public class SaEnvironment {
    public Memory vars;
    public Memory args;
    public TypeVal returnValue;

    public SaEnvironment (TsItemFct fct)
    {
	SaLExp lArgs = null;
	Ts localTable = fct.getTable();
	int i = 0;

	args = new Memory(localTable.getAdrArgCourante(), 0);
	vars = new Memory(localTable.getAdrVarCourante(), 0);

	//	args = new Memory(200, 0);
	//	vars = new Memory(200, 0);

	returnValue = null;
	//	System.out.println("allocation d'un nouvel environnement, fonction " + fct.getIdentif());
	//	System.out.println("dim var = " + localTable.getAdrVarCourante());
	//	System.out.println("dim arg = " + localTable.getAdrArgCourante());
	
    }


    public TypeVal getReturnValue(){return returnValue;}
    public void setReturnValue(TypeVal typeVal){returnValue = typeVal;}
}
