package ts;

import ts.Ts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import util.Type;

public class TsParser {
    private Ts tableCourante = null;
    private Ts tableGlobale = null;

    public TsParser(){

    }
    
    public Ts parse(String filePath){
        try {
            Files.lines(Paths.get(filePath)).forEachOrdered(this::processLine);;
        } catch(IOException e){
            e.printStackTrace();
        }
        return tableGlobale;
    }

    private void processLine(String line){
        var tokens = line.split("([ ]|[\t])+");
	if(tokens[1].equals("GLOBALE")){
	    tableGlobale = new Ts();
	    tableCourante = tableGlobale;
	    return;
	}
	if(tokens[1].equals("LOCALE")){
	    tableCourante = tableGlobale.getFct(tokens[3]).getTable();
	    return;
	}
	if(tokens[1].equals("VAR")){
	    tableCourante.addVar(tokens[0], Type.fromString(tokens[2]));
	    return;
	}
	if(tokens[1].equals("PARAM")){
	    tableCourante.addParam(tokens[0], Type.fromString(tokens[2]));
	    return;
	}
	if(tokens[1].equals("TAB")){
	    tableCourante.addTab(tokens[0], Type.fromString(tokens[2]), Integer.parseInt(tokens[4]));
	    return;
	}
	if(tokens[1].equals("FCT")){
	    tableGlobale.addFct(tokens[0], Type.fromString(tokens[2]), Integer.parseInt(tokens[3]), new Ts(), null);
	    return;
	}
    }
    /*
    public static void main(String args[]){
        var parser = new SymbolsTableParser();
        var r = parser.parse("function.ts");
        System.out.println("---Table Globale---");
        r.affiche(System.out);
        System.out.println("---Tables Locales---");
        r.afficheTablesLocales(System.out);
	}*/
}
