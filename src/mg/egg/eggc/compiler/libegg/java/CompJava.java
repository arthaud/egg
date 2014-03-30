package mg.egg.eggc.compiler.libegg.java;

import java.io.Serializable;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.EGGOptions;

public class CompJava implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    transient private TDS table;

    private String nom;

    public String getNom() {
        return nom;
    }

    public String getNomClasse() {
        return nom + 'C';
    }

    public void setNom(String n) {
        nom = n;
    }

    private StringBuffer corps;

    public StringBuffer getCorps() {
        return corps;
    }

    public void setCorps(String pack, Vector<String> incs) {
        EGGOptions options = table.getOptions();
        corps = new StringBuffer();
        corps.append("package " + pack + ";\n");
        corps.append("import mg.egg.eggc.runtime.libjava.ISourceUnit;\n");
        corps.append("import mg.egg.eggc.runtime.libjava.SourceUnit;\n");
        corps.append("import mg.egg.eggc.runtime.libjava.problem.IProblem;\n");
        corps
                .append("import mg.egg.eggc.runtime.libjava.problem.ProblemReporter;\n");
        corps
                .append("import mg.egg.eggc.runtime.libjava.problem.ProblemRequestor;\n");
        corps.append("import java.io.*;\n");
        corps.append("public class " + getNomClasse()
                + " implements Serializable {\n");
        corps.append(" 	private static final long serialVersionUID = 1L;\n");
        corps.append("  public static void main(String[] args){\n");
        corps.append("    System.err.println(\"version \" + \""
                + options.getVersion() + "\");\n");
        corps.append("    try {\n");
        corps.append("      ISourceUnit cu = new SourceUnit(args[0]);\n");
        corps.append("      ProblemReporter prp = new ProblemReporter(cu);\n");
        corps.append("      ProblemRequestor prq = new ProblemRequestor();\n");
        corps.append("      //new EGGOptionsAnalyzer(cu).analyse();\n");
        corps.append("      " + table.getNom() + " compilo = new " + getNom()
                + "(prp);\n");
        corps.append("      prq.beginReporting();\n");
        corps.append("      compilo.set_eval(true);\n");
        corps.append("      compilo.compile(cu);\n");
        corps.append("      for(IProblem problem : prp.getAllProblems())\n");
        corps.append("      	prq.acceptProblem(problem );\n");
        corps.append("      prq.endReporting();\n");
        corps.append("      System.exit(0);\n");
        corps.append("      }\n");
        corps.append("    catch(Exception e){\n");
        corps.append("      e.printStackTrace();\n");
        corps.append("      System.exit(1);\n");
        corps.append("      }\n");
        corps.append("    }\n");
        corps.append("  }\n");
    }

    public CompJava(TDS t) {
        table = t;
        nom = t.getNom();
//        change = false;
    }

    public void finaliser(String pack, Vector<String> incs) throws EGGException {
        // le fichier
        setCorps(pack, incs);
        table.getCompilationUnit().createFile(getNomClasse() + ".java",
                corps.toString());
    }
}
