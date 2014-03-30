////////////////////////////////////////////////////////////
//// version prevue pour gerer un compilateur incremental
//// pour eclipse mais pour l'instant tout est regenere 
//// a chaque fois.
//////////////////////////// MG 04-05 /////////////////////
// Pour engendrer un .m avec juste la syntaxe
package mg.egg.eggc.compiler.libegg.egg;

import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ATTRIBUT;
import mg.egg.eggc.compiler.libegg.base.ActREGLE;
import mg.egg.eggc.compiler.libegg.base.GLOB;
import mg.egg.eggc.compiler.libegg.base.IVisiteurAction;
import mg.egg.eggc.compiler.libegg.base.IVisiteurEgg;
import mg.egg.eggc.compiler.libegg.base.NON_TERMINAL;
import mg.egg.eggc.compiler.libegg.base.REGLE;
import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.compiler.libegg.base.TERMINAL;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;

public class VisiteurEggEgg implements IVisiteurEgg {
    // un seul visiteur d'action par visiteurEgg
    private IVisiteurAction vact;

    public IVisiteurAction getVisAction() {
        return vact;
    }

    // les attributs deja engendres
    private Vector<AttEgg> atts;

    // les terminaux deja engendres
    private Vector<TEgg> terms;

    public TEgg getTerm(String n) {
        for (Enumeration e = terms.elements(); e.hasMoreElements();) {
            TEgg tj = (TEgg) e.nextElement();
            if (tj.getNom().equals(n))
                return tj;
        }
        return null;
    }

    // les regles deja engendrées
    private Vector<REgg> regles;

    public REgg getRegle(int n) {
        // rajouter le test de debordement
        if (n < regles.size())
            return (REgg) regles.elementAt(n);
        else
            return null;
    }

    // la grammaire
    private TDS table;

    public VisiteurEggEgg(TDS t) {
        vact = new VisiteurActionEgg();
        table = t;
        atts = new Vector<AttEgg>();
        // nterms = new Vector();
        terms = new Vector<TEgg>();
        regles = new Vector<REgg>();
    }

    // genere main() du compilo
    // sans interet
    public void racinec() {
    }

    // genere module
    // sans interet
    public void racine() {
    }

    // // genere messages
    // // sans interet
    // public void messages() {
    // }
    //
    // // genere message
    // // sans interet
    // public void m_entete(MessageInfo m) {
    // }

    // genere l'analyseur lexical
    // sans interet
    public void lexical() {
    }

    // genere l'entete d'un non terminal
    // appele à la creation du non terminal
    public void nt_entete(NON_TERMINAL nt) {
    }

    // appele a chaque creation de regle
    public void regle(REGLE r) {
        REgg c = new REgg(r);
        regles.add(c);
    }

    // appele à la declaration du non terminal externe
    public void ex_entete(NON_TERMINAL nt) {
    }

    // appele a chaque creation de regle
    public void nt_regle(REGLE r) {
        REgg c = getRegle(r.getNumero());
        c.setEntete();
    }

    // genere l'analyse syntaxique d'un terminal ( = accepter)
    // appele a la creation du terminal
    public void t_entete(TERMINAL t) {
        TEgg c = new TEgg(t);
        terms.addElement(c);
        c.setEntete();
    }

    // genere le code d'un attribut semantique
    // non appelé
    public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a) {
    }

    // genere le code d'un attribut semantique
    public void nt_attribut(ATTRIBUT a) {
        AttEgg c = new AttEgg(a);
        atts.addElement(c);
        c.setEntete();
    }

    public void globale(REGLE r, GLOB g) {
        REgg c = getRegle(r.getNumero());
        if (c == null)
            c = new REgg(r);
        c.setGlob(g);
    }

    // genere le code d'une var globale attribut semantique
    public void nt_globale(NON_TERMINAL nt, GLOB g) {
        // NtEgg c = getNterm(nt.getNom());
        // c.setGlob(g);
    }

    public void t_attribut(TERMINAL t, ATTRIBUT a) {
    }

    // genere le code d'une action semantique
    // appele a la creation de l'action
    // nom (n), regle(r), code (c)
    public void nt_action(ActREGLE a) {
        REGLE r = a.getRegle();
        // String n = a.getNom();
        // System.err.println("Visiting action " + n + "of rule#" +
        // r.getNumero());
        REgg c = getRegle(r.getNumero());
        if (c == null)
            c = new REgg(r);
        c.setAct(a);
    }

    public String car(String s) {
        // System.err.println("Appel visiteur car :" + s);
        return s;
    }

    public void finaliser() throws EGGException {
        StringBuffer sb = new StringBuffer();
        sb.append("------------------ ***** -----------------------------\n");
        sb.append("-- Regénération automatique de la grammaire de "
                + table.getNom());
        sb.append("-- permet de faire apparaitre les actions automatiques\n");
        sb.append("------------------ ***** -----------------------------\n");
        // les attributs
        sb.append("\\section{Attributs}\n\n");
        for (AttEgg a : atts) {
            sb.append(a.finaliser());
        }
        sb.append("\n");
        // les terminaux
        sb.append("\\section{Terminaux}\n\n");
        for (TEgg a : terms) {
            sb.append(a.finaliser());
        }
        sb.append("\n");
        // les regles
        sb.append("\\section{Règles de production}\n");
        for (REgg a : regles) {
            sb.append(a.finaliser());
        }
        sb.append("\\end{document}\n");
        // ecriture
        IEGGCompilationUnit cu = table.getCompilationUnit();
        cu.createFile(table.getNom() + ".egg", sb.toString());
    }

    protected static String getOpEgg(String op) {
        return op;
    }

    protected static String getTypeEgg(String type) {
        return type;
    }

    public void m_entete(String m) {
    }

}
