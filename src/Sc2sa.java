import sc.analysis.*;
import sc.node.*;
import sa.*;
import util.Type;

public class Sc2sa extends DepthFirstAdapter {
    private SaNode returnValue;
    private Type returnType;

    public void defaultIn(@SuppressWarnings("unused") Node node) {
        //System.out.println("<" + node.getClass().getSimpleName() + ">");
    }

    public void defaultOut(@SuppressWarnings("unused") Node node) {
        //System.out.println("</" + node.getClass().getSimpleName() + ">");
    }

    public SaNode getRoot() {
        return this.returnValue;
    }

    public void inStart(Start node) {
        defaultIn(node);
    }

    public void outStart(Start node) {
        defaultOut(node);
    }

    public void inAProgramme(AProgramme node) {
        defaultIn(node);
    }

    public void outAProgramme(AProgramme node) {
        defaultOut(node);
    }



    //exp4 = {plus} exp4 plus exp5
    @Override
    public void caseAPlusExp4(APlusExp4 node) {
        SaExp op1 = null;
        SaExp op2 = null;
        inAPlusExp4(node);
        if (node.getExp4() != null) {
            node.getExp4().apply(this);
            op1 = (SaExp) this.returnValue;
        }
        if (node.getExp5() != null) {
            node.getExp5().apply(this);
            op2 = (SaExp) this.returnValue;

        }
        this.returnValue = new SaExpAdd(op1, op2);
        outAPlusExp4(node);
    }



    @Override
    public void caseStart(Start node) {
        inStart(node);
        node.getPProgramme().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }


    //programme = listedecvar listedecfonc ;
    @Override
    public void caseAProgramme(AProgramme node) {
        inAProgramme(node);
        SaLDecVar LdecVar = null;
        SaLDecFonc  LdecFonc = null;
        if (node.getListedecvar() != null) {
            node.getListedecvar().apply(this);
            LdecFonc = (SaLDecFonc) this.returnValue;
        }
        if (node.getListedecfonc() != null) {
            node.getListedecfonc().apply(this);
            LdecVar = (SaLDecVar) this.returnValue;

        }
        this.returnValue = new SaProg(LdecVar,LdecFonc);

        outAProgramme(node);
    }

    public void inADecvarListedecvar(ADecvarListedecvar node) {
        defaultIn(node);
    }

    public void outADecvarListedecvar(ADecvarListedecvar node) {
        defaultOut(node);
    }



    //listedecvar = {decvar} decvar listedecvarbis
    @Override
    public void caseADecvarListedecvar(ADecvarListedecvar node) {
        inADecvarListedecvar(node);
        SaDecVar decVar= null;
        SaLDecVar ldecVar = null;
        if (node.getDecvar() != null) {
            node.getDecvar().apply(this);
            decVar = (SaDecVar) this.returnValue;
        }
        if (node.getListedecvarbis() != null) {
            node.getListedecvarbis().apply(this);
            ldecVar = (SaLDecVar) this.returnValue;
        }
        this.returnValue = new SaLDecVar(decVar,ldecVar);
        outADecvarListedecvar(node);
    }


    public void inAVideListedecvar(AVideListedecvar node) {
        defaultIn(node);
    }

    public void outAVideListedecvar(AVideListedecvar node) {
        defaultOut(node);
    }

    @Override
    public void caseAVideListedecvar(AVideListedecvar node) {
        inAVideListedecvar(node);
        outAVideListedecvar(node);
    }


    //listedecvarbis = {listdecvar} virgule decvar listedecvarbis
    @Override
    public void caseAListdecvarListedecvarbis(AListdecvarListedecvarbis node) {
        inAListdecvarListedecvarbis(node);
        SaDecVar decVar = null;
        SaLDecVar lDecVar = null;

        if (node.getDecvar() != null) {
            node.getDecvar().apply(this);
            decVar = (SaDecVar) this.returnValue;
        }
        if (node.getListedecvarbis() != null) {
            node.getListedecvarbis().apply(this);
            lDecVar = (SaLDecVar) this.returnValue;
        }
        this.returnValue = new SaLDecVar(decVar,lDecVar);
        outAListdecvarListedecvarbis(node);
    }

    public void inAListdecvarListedecvarbis(AListdecvarListedecvarbis node) {
        defaultIn(node);
    }

    public void outAListdecvarListedecvarbis(AListdecvarListedecvarbis node) {
        defaultOut(node);
    }










    public void inAVideListedecvarbis(AVideListedecvarbis node) {
        defaultIn(node);
    }

    public void outAVideListedecvarbis(AVideListedecvarbis node) {
        defaultOut(node);
    }

    @Override
    public void caseAVideListedecvarbis(AVideListedecvarbis node) {
        inAVideListedecvarbis(node);
        outAVideListedecvarbis(node);
    }


    public void inAVarDecvar(AVarDecvar node) {
        defaultIn(node);
    }

    public void outAVarDecvar(AVarDecvar node) {
        defaultOut(node);
    }



   // decvar = {var} typevar id
    @Override
    public void caseAVarDecvar(AVarDecvar node) {
        inAVarDecvar(node);
        Type typeVar = null;
        String id =null;
        if (node.getTypevar() != null) {
            node.getTypevar().apply(this);
            typeVar= this.returnType;
        }
        if (node.getId() != null) {
            node.getId().apply(this);
            id= node.getId().getText();
        }
        this.returnValue=new SaDecVarSimple(id,typeVar);
        outAVarDecvar(node);
    }


    public void inAFoncDecvar(AFoncDecvar node) {
        defaultIn(node);
    }

    public void outAFoncDecvar(AFoncDecvar node) {
        defaultOut(node);
    }


    //decvar = {fonc} typevar id co nombre cf;
    @Override
    public void caseAFoncDecvar(AFoncDecvar node) {
        inAFoncDecvar(node);
        Type typeVar=null;
        String id = null;
        int nbr = -1;
        if (node.getTypevar() != null) {
            node.getTypevar().apply(this);
            typeVar=this.returnType;
        }
        if (node.getId() != null) {
            node.getId().apply(this);
            id=node.getId().getText();
        }
        if (node.getNombre() != null) {
            node.getNombre().apply(this);
            nbr= Integer.parseInt(node.getNombre().getText());
        }
        this.returnValue = new SaDecTab(id,typeVar,nbr);

        outAFoncDecvar(node);
    }

    public void inABooleanTypevar(ABooleanTypevar node) {
        defaultIn(node);
    }

    public void outABooleanTypevar(ABooleanTypevar node) {
        defaultOut(node);
    }


    //typevar = {boolean} boolean
    @Override
    public void caseABooleanTypevar(ABooleanTypevar node) {
        inABooleanTypevar(node);
        if (node.getBoolean() != null) {
            node.getBoolean().apply(this);
        }
        this.returnType = Type.BOOL;
        outABooleanTypevar(node);
    }

    public void inAEntierTypevar(AEntierTypevar node) {
        defaultIn(node);
    }

    public void outAEntierTypevar(AEntierTypevar node) {
        defaultOut(node);
    }


    //typevar =  {entier} entier;

    @Override
    public void caseAEntierTypevar(AEntierTypevar node) {
        inAEntierTypevar(node);
        if (node.getEntier() != null) {
            node.getEntier().apply(this);
        }
        this.returnType = Type.ENTIER;
        outAEntierTypevar(node);
    }

    public void inADecfoncListedecfonc(ADecfoncListedecfonc node) {
        defaultIn(node);
    }

    public void outADecfoncListedecfonc(ADecfoncListedecfonc node) {
        defaultOut(node);
    }


    //listedecfonc = {decfonc} decfonc listedecfonc

    @Override
    public void caseADecfoncListedecfonc(ADecfoncListedecfonc node) {
        inADecfoncListedecfonc(node);
        SaDecFonc saDecFonc = null;
        SaLDecFonc saLDecFonc =null;
        if (node.getDecfonc() != null) {
            node.getDecfonc().apply(this);
            saDecFonc= (SaDecFonc) this.returnValue;
        }
        if (node.getListedecfonc() != null) {
            node.getListedecfonc().apply(this);
            saLDecFonc = (SaLDecFonc) this.returnValue;
        }
        this.returnValue= new SaLDecFonc(saDecFonc,saLDecFonc);
        outADecfoncListedecfonc(node);
    }

    public void inAVideListedecfonc(AVideListedecfonc node) {
        defaultIn(node);
    }

    public void outAVideListedecfonc(AVideListedecfonc node) {
        defaultOut(node);
    }

    @Override
    public void caseAVideListedecfonc(AVideListedecfonc node) {
        inAVideListedecfonc(node);
        outAVideListedecfonc(node);
    }

    public void inADecfonc(ADecfonc node) {
        defaultIn(node);
    }

    public void outADecfonc(ADecfonc node) {
        defaultOut(node);
    }

    //decfonc = typeop id po [premier]:listedecvar pf [second]:listedecvar blocinst ;
    @Override
    public void caseADecfonc(ADecfonc node) {
        inADecfonc(node);
        Type typeOp= null;
        String id =null;
        SaLDecVar saLDecVarP = null;
        SaLDecVar saLDecVarS = null;
        SaInstBloc saInstBloc = null;

        if (node.getTypeop() != null) {
            node.getTypeop().apply(this);
            typeOp=this.returnType;
        }
        if (node.getId() != null) {
            node.getId().apply(this);
            id=node.getId().getText();
        }

        if (node.getPremier() != null) {
            node.getPremier().apply(this);
            saLDecVarP= (SaLDecVar) this.returnValue;
        }

        if (node.getSecond() != null) {
            node.getSecond().apply(this);
            saLDecVarS= (SaLDecVar) this.returnValue;
        }
        if (node.getBlocinst() != null) {
            node.getBlocinst().apply(this);
            saInstBloc = (SaInstBloc) this.returnValue;
        }
        this.returnValue=new SaDecFonc(id,typeOp,saLDecVarP,saLDecVarS,saInstBloc);
        outADecfonc(node);
    }

    public void inATypeopTypeop(ATypeopTypeop node) {
        defaultIn(node);
    }

    public void outATypeopTypeop(ATypeopTypeop node) {
        defaultOut(node);
    }

   // typeop = {typeop} typevar
    @Override
    public void caseATypeopTypeop(ATypeopTypeop node) {
        inATypeopTypeop(node);
        if (node.getTypevar() != null) {
            node.getTypevar().apply(this);
        }
        outATypeopTypeop(node);
    }

    public void inAVideTypeop(AVideTypeop node) {
        defaultIn(node);
    }

    public void outAVideTypeop(AVideTypeop node) {
        defaultOut(node);
    }


    @Override
    public void caseAVideTypeop(AVideTypeop node) {
        inAVideTypeop(node);
        outAVideTypeop(node);
        returnType = Type.NUL;
    }

    public void inABlocinst(ABlocinst node) {
        defaultIn(node);
    }

    public void outABlocinst(ABlocinst node) {
        defaultOut(node);
    }

    //blocinst = aco listeinst acf ;
    @Override
    public void caseABlocinst(ABlocinst node) {
        inABlocinst(node);
      SaLInst saLInst = null;
        if (node.getListeinst() != null) {
            node.getListeinst().apply(this);
            saLInst = (SaLInst) this.returnValue;
        }
      this.returnValue=new SaInstBloc(saLInst);
        outABlocinst(node);
    }

    public void inAListinstListeinst(AListinstListeinst node) {
        defaultIn(node);
    }

    public void outAListinstListeinst(AListinstListeinst node) {
        defaultOut(node);
    }

    //listeinst = {listinst} inst listeinst

    @Override
    public void caseAListinstListeinst(AListinstListeinst node) {
        SaInst saInst = null;
        SaLInst saLInst = null;
        inAListinstListeinst(node);
        if (node.getInst() != null) {
            node.getInst().apply(this);
            saInst= (SaInst) this.returnValue;
        }
        if (node.getListeinst() != null) {
            node.getListeinst().apply(this);
            saLInst = (SaLInst) this.returnValue;
        }
        this.returnValue = new SaLInst(saInst,saLInst);
        outAListinstListeinst(node);
    }

    public void inAVideListeinst(AVideListeinst node) {
        defaultIn(node);
    }

    public void outAVideListeinst(AVideListeinst node) {
        defaultOut(node);
    }

    @Override
    public void caseAVideListeinst(AVideListeinst node) {
        inAVideListeinst(node);
        outAVideListeinst(node);
    }

    public void inAInstInst(AInstInst node) {
        defaultIn(node);
    }

    public void outAInstInst(AInstInst node) {
        defaultOut(node);
    }

    //inst = {inst} var egale exp pvirgule
    @Override
    public void caseAInstInst(AInstInst node) {
        inAInstInst(node);
        SaVar saVar = null;
        SaExp saExp = null;
        if (node.getVar() != null) {
            node.getVar().apply(this);
            saVar = (SaVar) this.returnValue;
        }

        if (node.getExp() != null) {
            node.getExp().apply(this);
            saVar = (SaVar) this.returnValue;
        }
        this.returnValue = new SaInstAffect(saVar,saExp);
        outAInstInst(node);
    }

    public void inATanqueInst(ATanqueInst node) {
        defaultIn(node);
    }

    public void outATanqueInst(ATanqueInst node) {
        defaultOut(node);
    }

    //inst ={tanque} tantque exp faire blocinst

    @Override
    public void caseATanqueInst(ATanqueInst node) {
        inATanqueInst(node);
        SaExp saExp =null;
        SaInstBloc saInstBloc =null;

        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp= (SaExp) this.returnValue;
        }

        if (node.getBlocinst() != null) {
            node.getBlocinst().apply(this);
            saInstBloc = (SaInstBloc) this.returnValue;
        }

        this.returnValue= new SaInstTantQue(saExp,saInstBloc);
    }

    public void inASiInst(ASiInst node) {
        defaultIn(node);
    }

    public void outASiInst(ASiInst node) {
        defaultOut(node);
    }

   // inst ={si} si exp alors blocinst
    @Override
    public void caseASiInst(ASiInst node) {
        inASiInst(node);
        SaExp saExp =null;
        SaInstBloc saInstBloc =null;

        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp = (SaExp) this.returnValue;
        }
        if (node.getBlocinst() != null) {
            node.getBlocinst().apply(this);
            saInstBloc= (SaInstBloc) this.returnValue;
        }
        this.returnValue=new SaInstSi(saExp,saInstBloc,null);
        outASiInst(node);
    }

    public void inASisinonInst(ASisinonInst node) {
        defaultIn(node);
    }

    public void outASisinonInst(ASisinonInst node) {
        defaultOut(node);
    }

    //inst ={sisinon} si exp alors [premier]:blocinst sinon [second]:blocinst
    @Override
    public void caseASisinonInst(ASisinonInst node) {
        inASisinonInst(node);
        SaExp saExp =null;
        SaInstBloc saInstBlocP =null;
        SaInstBloc saInstBlocS =null;


        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp= (SaExp) this.returnValue;
        }
        if (node.getPremier() != null) {
            node.getPremier().apply(this);
            saInstBlocP= (SaInstBloc) this.returnValue;
        }

        if (node.getSecond() != null) {
            node.getSecond().apply(this);
            saInstBlocS= (SaInstBloc) this.returnValue;
        }
        this.returnValue=new SaInstSi(saExp,saInstBlocP,saInstBlocS);
        outASisinonInst(node);
    }

    public void inARetournInst(ARetournInst node) {
        defaultIn(node);
    }

    public void outARetournInst(ARetournInst node) {
        defaultOut(node);
    }

    //  inst= {retourn} retourne exp pvirgule
    @Override
    public void caseARetournInst(ARetournInst node) {
        inARetournInst(node);
        SaExp saExp =null;
        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp = (SaExp) this.returnValue;
        }
        this.returnValue = new SaInstRetour(saExp);
        outARetournInst(node);
    }

    public void inAAppfoncInst(AAppfoncInst node) {
        defaultIn(node);
    }

    public void outAAppfoncInst(AAppfoncInst node) {
        defaultOut(node);
    }

    //inst ={appfonc} id po listeexp pf pvirgule
    @Override
    public void caseAAppfoncInst(AAppfoncInst node) {
        inAAppfoncInst(node);
        String id = null;
        SaLExp saLExp = null;

        if (node.getId() != null) {
            node.getId().apply(this);
            id = node.getId().getText();
        }

        if (node.getListeexp() != null) {
            node.getListeexp().apply(this);
            saLExp = (SaLExp) this.returnValue;
        }
        this.returnValue=new SaAppel(id,saLExp);
        outAAppfoncInst(node);
    }

    public void inAEcrireInst(AEcrireInst node) {
        defaultIn(node);
    }

    public void outAEcrireInst(AEcrireInst node) {
        defaultOut(node);
    }

   // inst =     | {ecrire} ecrire po exp pf pvirgule;
    @Override
    public void caseAEcrireInst(AEcrireInst node) {
        inAEcrireInst(node);
        SaExp saExp =null;

        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp = (SaExp) this.returnValue;
        }
        outAEcrireInst(node);
        this.returnValue = new SaInstEcriture(saExp);
    }

    public void inAListexpListeexp(AListexpListeexp node) {
        defaultIn(node);
    }

    public void outAListexpListeexp(AListexpListeexp node) {
        defaultOut(node);
    }

    //listeexp = {listexp} exp listeexpbis
    @Override
    public void caseAListexpListeexp(AListexpListeexp node) {
        inAListexpListeexp(node);
        SaExp saExp =null;
        SaLExp saLExp =null;
        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp = (SaExp) this.returnValue;
        }
        if (node.getListeexpbis() != null) {
            node.getListeexpbis().apply(this);
            saLExp = (SaLExp) this.returnValue;
        }
        this.returnValue = new SaLExp(saExp,saLExp);
        outAListexpListeexp(node);
    }

    public void inAVideListeexp(AVideListeexp node) {
        defaultIn(node);
    }

    public void outAVideListeexp(AVideListeexp node) {
        defaultOut(node);
    }


    @Override
    public void caseAVideListeexp(AVideListeexp node) {
        inAVideListeexp(node);
        outAVideListeexp(node);
    }

    public void inAListexpbisListeexpbis(AListexpbisListeexpbis node) {
        defaultIn(node);
    }

    public void outAListexpbisListeexpbis(AListexpbisListeexpbis node) {
        defaultOut(node);
    }
    //listeexpbis = {listexpbis} virgule exp listeexpbis
    @Override
    public void caseAListexpbisListeexpbis(AListexpbisListeexpbis node) {
        inAListexpbisListeexpbis(node);
        SaExp saExp =null;
        SaLExp saLExp =null;
        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp = (SaExp) this.returnValue;
        }
        if (node.getListeexpbis() != null) {
            node.getListeexpbis().apply(this);
            saLExp = (SaLExp) this.returnValue;
        }
        this.returnValue = new SaLExp(saExp,saLExp);
        outAListexpbisListeexpbis(node);
    }

    public void inAVideListeexpbis(AVideListeexpbis node) {
        defaultIn(node);
    }

    public void outAVideListeexpbis(AVideListeexpbis node) {
        defaultOut(node);
    }

    @Override
    public void caseAVideListeexpbis(AVideListeexpbis node) {
        inAVideListeexpbis(node);
        outAVideListeexpbis(node);
    }

    public void inAOuExp(AOuExp node) {
        defaultIn(node);
    }

    public void outAOuExp(AOuExp node) {
        defaultOut(node);
    }


    //exp = {ou} exp ou exp2
    @Override
    public void caseAOuExp(AOuExp node) {
        inAOuExp(node);
        SaExp saExp =null;
        SaExp saExp1 = null;

        if (node.getExp() != null) {
            node.getExp().apply(this);
            saExp= (SaExp) this.returnValue;
        }
        if (node.getExp2() != null) {
            node.getExp2().apply(this);
            saExp1= (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpOr(saExp,saExp1);
        outAOuExp(node);
    }

    public void inAExp2Exp(AExp2Exp node) {
        defaultIn(node);
    }

    public void outAExp2Exp(AExp2Exp node) {
        defaultOut(node);
    }

    //   exp = | {exp2} exp2 ;
    @Override
    public void caseAExp2Exp(AExp2Exp node) {
        inAExp2Exp(node);
        if (node.getExp2() != null) {
            node.getExp2().apply(this);
        }
        outAExp2Exp(node);
    }

    public void inAEtExp2(AEtExp2 node) {
        defaultIn(node);
    }

    public void outAEtExp2(AEtExp2 node) {
        defaultOut(node);
    }

    //exp2 = {et} exp2 et exp3
    @Override
    public void caseAEtExp2(AEtExp2 node) {
        inAEtExp2(node);
        SaExp saExp1 =null;
        SaExp saExp2 = null;
        if (node.getExp2() != null) {
            node.getExp2().apply(this);
            saExp1= (SaExp) this.returnValue;
        }
        if (node.getExp3() != null) {
            node.getExp3().apply(this);
            saExp2= (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpAnd(saExp1, saExp2);
        outAEtExp2(node);
    }
    public void inAExp3Exp2(AExp3Exp2 node) {
        defaultIn(node);
    }

    public void outAExp3Exp2(AExp3Exp2 node) {
        defaultOut(node);
    }

    //exp2=exp3
    @Override
    public void caseAExp3Exp2(AExp3Exp2 node) {
        inAExp3Exp2(node);
        if (node.getExp3() != null) {
            node.getExp3().apply(this);
        }
        outAExp3Exp2(node);
    }

    public void inAEgaleExp3(AEgaleExp3 node) {
        defaultIn(node);
    }

    public void outAEgaleExp3(AEgaleExp3 node) {
        defaultOut(node);
    }

    //exp3 = {egale} exp3 egale exp4
    @Override
    public void caseAEgaleExp3(AEgaleExp3 node) {
        inAEgaleExp3(node);
        SaExp saExp1 =null;
        SaExp saExp2 = null;

        if (node.getExp3() != null) {
            node.getExp3().apply(this);
            saExp1= (SaExp) this.returnValue;
        }
        if (node.getExp4() != null) {
            node.getExp4().apply(this);
            saExp2= (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpEqual(saExp1,saExp2);
        outAEgaleExp3(node);
    }

    public void inAInfExp3(AInfExp3 node) {
        defaultIn(node);
    }

    public void outAInfExp3(AInfExp3 node) {
        defaultOut(node);
    }


    //    | {inf} exp3 inf exp4
    @Override
    public void caseAInfExp3(AInfExp3 node) {
        inAInfExp3(node);
        SaExp saExp1 =null;
        SaExp saExp2 = null;
        if (node.getExp3() != null) {
            node.getExp3().apply(this);
            saExp1= (SaExp) this.returnValue;
        }
        if (node.getExp4() != null) {
            node.getExp4().apply(this);
            saExp2= (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpInf(saExp1,saExp2);
        outAInfExp3(node);
    }

    public void inAExp4Exp3(AExp4Exp3 node) {
        defaultIn(node);
    }

    public void outAExp4Exp3(AExp4Exp3 node) {
        defaultOut(node);
    }

    @Override
    public void caseAExp4Exp3(AExp4Exp3 node) {
        inAExp4Exp3(node);
        if (node.getExp4() != null) {
            node.getExp4().apply(this);
        }
        outAExp4Exp3(node);
    }

    public void inAPlusExp4(APlusExp4 node) {
        defaultIn(node);
    }

    public void outAPlusExp4(APlusExp4 node) {
        defaultOut(node);
    }


    public void inAMoinsExp4(AMoinsExp4 node) {
        defaultIn(node);
    }

    public void outAMoinsExp4(AMoinsExp4 node) {
        defaultOut(node);
    }

    //    |{moins} exp4 moins exp5
    @Override
    public void caseAMoinsExp4(AMoinsExp4 node) {
        inAMoinsExp4(node);
        SaExp saExp1 =null;
        SaExp saExp2 = null;

        if (node.getExp4() != null) {
            node.getExp4().apply(this);
            saExp1= (SaExp) this.returnValue;
        }
        if (node.getExp5() != null) {
            node.getExp5().apply(this);
            saExp2= (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpSub(saExp1,saExp2);
        outAMoinsExp4(node);
    }

    public void inAExp5Exp4(AExp5Exp4 node) {
        defaultIn(node);
    }

    public void outAExp5Exp4(AExp5Exp4 node) {
        defaultOut(node);
    }

    @Override
    public void caseAExp5Exp4(AExp5Exp4 node) {
        inAExp5Exp4(node);
        if (node.getExp5() != null) {
            node.getExp5().apply(this);
        }
        outAExp5Exp4(node);
    }

    public void inADivExp5(ADivExp5 node) {
        defaultIn(node);
    }

    public void outADivExp5(ADivExp5 node) {
        defaultOut(node);
    }

    //exp5 = {div} exp5 div exp6
    @Override
    public void caseADivExp5(ADivExp5 node) {
        inADivExp5(node);
        SaExp saExp1 =null;
        SaExp saExp2 = null;

        if (node.getExp5() != null) {
            node.getExp5().apply(this);
        }
        if (node.getExp6() != null) {
            node.getExp6().apply(this);
        }
        this.returnValue = new SaExpDiv(saExp1,saExp2);
        outADivExp5(node);
    }

    public void inAMultExp5(AMultExp5 node) {
        defaultIn(node);
    }

    public void outAMultExp5(AMultExp5 node) {
        defaultOut(node);
    }

    //    | {mult} exp5 mult exp6
    @Override
    public void caseAMultExp5(AMultExp5 node) {
        inAMultExp5(node);
        SaExp saExp1 =null;
        SaExp saExp2 = null;

        if (node.getExp5() != null) {
            node.getExp5().apply(this);
            saExp1 = (SaExp) this.returnValue;
        }

        if (node.getExp6() != null) {
            node.getExp6().apply(this);
            saExp2= (SaExp) this.returnValue;
        }
        this.returnValue = new SaExpMult(saExp1,saExp2);
        outAMultExp5(node);
    }

    public void inAExp6Exp5(AExp6Exp5 node) {
        defaultIn(node);
    }

    public void outAExp6Exp5(AExp6Exp5 node) {
        defaultOut(node);
    }

    @Override
    public void caseAExp6Exp5(AExp6Exp5 node) {
        inAExp6Exp5(node);
        if (node.getExp6() != null) {
            node.getExp6().apply(this);
        }
        outAExp6Exp5(node);
    }

    public void inANonExp6(ANonExp6 node) {
        defaultIn(node);
    }

    public void outANonExp6(ANonExp6 node) {
        defaultOut(node);
    }

//exp6 = {non} non exp6
    @Override
    public void caseANonExp6(ANonExp6 node) {
        inANonExp6(node);
        SaExp saExp1 =null;

        if (node.getExp6() != null) {
            node.getExp6().apply(this);
            saExp1= (SaExp) this.returnValue;
        }
        this.returnValue=new SaExpNot(saExp1);
        outANonExp6(node);
    }

    public void inAExp7Exp6(AExp7Exp6 node) {
        defaultIn(node);
    }

    public void outAExp7Exp6(AExp7Exp6 node) {
        defaultOut(node);
    }


    @Override
    public void caseAExp7Exp6(AExp7Exp6 node) {
        inAExp7Exp6(node);
        if (node.getExp7() != null) {
            node.getExp7().apply(this);
        }
        outAExp7Exp6(node);
    }

    public void inAExpbExp7(AExpbExp7 node) {
        defaultIn(node);
    }

    public void outAExpbExp7(AExpbExp7 node) {
        defaultOut(node);
    }



    //exp7= {expb} po exp pf
    //TODO
    @Override
    public void caseAExpbExp7(AExpbExp7 node) {
        inAExpbExp7(node);
        if (node.getExp() != null) {
            node.getExp().apply(this);
        }
        outAExpbExp7(node);
    }

    public void inAVarExp7(AVarExp7 node) {
        defaultIn(node);
    }

    public void outAVarExp7(AVarExp7 node) {
        defaultOut(node);
    }


    //    | {var} var
    @Override
    public void caseAVarExp7(AVarExp7 node) {
        inAVarExp7(node);
        if (node.getVar() != null) {
            node.getVar().apply(this);
        }
        outAVarExp7(node);
    }

    public void inANombreExp7(ANombreExp7 node) {
        defaultIn(node);
    }

    public void outANombreExp7(ANombreExp7 node) {
        defaultOut(node);
    }

       // {nombre} nombre
    @Override
    public void caseANombreExp7(ANombreExp7 node) {
        inANombreExp7(node);
        if (node.getNombre() != null) {
            node.getNombre().apply(this);
        }
        outANombreExp7(node);
    }

    public void inAAppelfoncExp7(AAppelfoncExp7 node) {
        defaultIn(node);
    }

    public void outAAppelfoncExp7(AAppelfoncExp7 node) {
        defaultOut(node);
    }

    // {appelfonc} id po listeexp pf
    @Override
    public void caseAAppelfoncExp7(AAppelfoncExp7 node) {
        inAAppelfoncExp7(node);
        String id= null;
        SaLExp saLExp=null;

        if (node.getId() != null) {
            node.getId().apply(this);
            id = node.getId().getText();
        }
        if (node.getListeexp() != null) {
            node.getListeexp().apply(this);
            saLExp= (SaLExp) this.returnValue;
        }
        this.returnValue=new SaAppel(id,saLExp);
        outAAppelfoncExp7(node);
    }
    public void inALireExp7(ALireExp7 node) {
        defaultIn(node);
    }

    public void outALireExp7(ALireExp7 node) {
        defaultOut(node);
    }

    //TODO
    //    | {lire} lire po pf
    @Override
    public void caseALireExp7(ALireExp7 node) {
        inALireExp7(node);
        if (node.getLire() != null) {
            node.getLire().apply(this);
        }
        if (node.getPo() != null) {
            node.getPo().apply(this);
        }
        if (node.getPf() != null) {
            node.getPf().apply(this);
        }
        outALireExp7(node);
    }

    public void inAVraiExp7(AVraiExp7 node) {
        defaultIn(node);
    }

    public void outAVraiExp7(AVraiExp7 node) {
        defaultOut(node);
    }


    //    | {vrai} vrai
    @Override
    public void caseAVraiExp7(AVraiExp7 node) {
        inAVraiExp7(node);

        this.returnValue=new SaExpVrai();
       outAVraiExp7(node);
    }

    public void inAFauxExp7(AFauxExp7 node) {
        defaultIn(node);
    }

    public void outAFauxExp7(AFauxExp7 node) {
        defaultOut(node);
    }

    @Override
    public void caseAFauxExp7(AFauxExp7 node) {
        inAFauxExp7(node);
        this.returnValue=new SaExpFaux();
        outAFauxExp7(node);
    }

    public void inAIdVar(AIdVar node) {
        defaultIn(node);
    }

    public void outAIdVar(AIdVar node) {
        defaultOut(node);
    }

    @Override
    public void caseAIdVar(AIdVar node) {
        inAIdVar(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAIdVar(node);
    }

    public void inAIdbVar(AIdbVar node) {
        defaultIn(node);
    }

    public void outAIdbVar(AIdbVar node) {
        defaultOut(node);
    }

        //| {idb} id co exp cf;
    @Override
    public void caseAIdbVar(AIdbVar node) {
        inAIdbVar(node);
        SaExp saExp =null;
        String id = null;
        if (node.getId() != null) {
            node.getId().apply(this);
            saExp= (SaExp) this.returnValue;
        }
        if (node.getExp() != null) {
            node.getExp().apply(this);
            id=node.getId().getText();
        }
        this.returnValue=new SaVarIndicee(id,saExp);
        outAIdbVar(node);
    }

}
