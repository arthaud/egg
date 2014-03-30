package mg.egg.eggc.compiler.libegg.latex;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import mg.egg.eggc.compiler.libegg.base.ActREGLE;
import mg.egg.eggc.compiler.libegg.base.EltREGLE;
import mg.egg.eggc.compiler.libegg.base.GLOB;
import mg.egg.eggc.compiler.libegg.base.REGLE;
import mg.egg.eggc.compiler.libegg.base.SYMBOLE;
import mg.egg.eggc.compiler.libegg.base.SymbREGLE;
import mg.egg.eggc.compiler.libegg.base.TERMINAL;
import mg.egg.eggc.runtime.libjava.EGGException;

public class RLatex {

    // private NON_TERMINAL nonTerminal;
    private REGLE regle;

    private StringBuffer entete;

    public StringBuffer getEntete() {
        return entete;
    }

    // genere le code de la regle
    public void setEntete() {
        entete = new StringBuffer();
        String comm = regle.getComm();
        if (comm != null)
            entete.append(regle.getComm() + "\\\\\n");
        entete.append("% regle #" + regle.getNumero() + "\n");
        // entete.append ( "-- regle #" + regle.getNumero() +"\n" ) ;
        entete.append("\\eggrule{" + regle.getGauche().getNom() + "}{");
        if (!regle.getDroite().elements().hasMoreElements()) {
            // dans ce cas il n'y a pas d'element dans la partie droite
            entete.append("$\\Lambda$");
        }
        for (Enumeration d = regle.getDroite().elements(); d.hasMoreElements();) {
            EltREGLE er = (EltREGLE) d.nextElement();
            if (er instanceof SymbREGLE) {
                SYMBOLE s = ((SymbREGLE) er).getSymbole();
                String nom;
                if (s instanceof TERMINAL) {
                    nom = s.getNom();
                    nom = nom.replace("_", "\\_");
                    entete.append(" \\eggt{");
                } else {
                    nom = s.getNom();
                    entete.append(" \\eggnt{");
                }
                entete.append(nom + "}");
            } else {
                ActREGLE ar = (ActREGLE) er;
                if (ar.getCode() == null) {
                    // System.err.println("Suppression de " + er.getNom());
                } else {
                    String nom = er.getNom();
                    nom = nom.replace("#", "\\#");
                    nom = nom.replace("_", "\\_");
                    entete.append(" \\eggaction{" + nom + "}");
                }
            }
        }
        entete.append("}\n\\\\");
    }

    // les actions
    private HashMap<String, String> acts;

    public String getAct(String act) {
        return (String) acts.get(act);
    }

    public void addAct(String act, String c) {
        acts.put(act, c);
    }

    public void delAct(String act) {
        acts.remove(act);
    }

    public void setAct(ActREGLE act) {
        StringBuffer asb = new StringBuffer();

        // remplacement des caractères spéciaux
        String nom = act.getNom();
        nom = nom.replace("_", "\\_");
        nom = nom.replace("#", "\\#");

        // l'entete d'une action
        asb.append("\\egglaction{" + nom + "}{");
        // le code
        asb.append(act.getCode());
        asb.append("}\n}\n\\\\\n\\\\");
        // ajout dans la liste
        // System.out.println("setAct " + act.getNom());
        addAct(act.getNom(), asb.toString());
    }

    private HashMap<String, String> globs;

    public String getGlob(String g) {
        return (String) globs.get(g);
    }

    public void addGlob(String g, String c) {
        globs.put(g, c);
    }

    public void delGlob(String g) {
        globs.remove(g);
    }

    public void setGlob(GLOB g) {
        StringBuffer asb = new StringBuffer();
        // remplacement des caractères spéciaux
        String nom = g.getTypeReel().getNom();
        nom = nom.replace("_", "\\_");
        String type = g.getType().getNom();
        type = type.replace("_", "\\_");
        type = type.replace("<", "$<$");
        type = type.replace(">", "$>$");

        // visible dans le package
        asb.append("\\eggglobal{" + nom + "}{" + type + "}");
        //		asb.append(g.getNom() + " : "
        //				+ VisiteurEggLatex.getTypeLatex(g.getType().getNom()) + ";\n");
        // ajout dans la liste
        addGlob(g.getNom(), asb.toString());
    }

    public RLatex(REGLE r) {
        regle = r;
        acts = new HashMap<String, String>();
        globs = new HashMap<String, String>();
    }

    // appele si chgt dans le nt
    public String finaliser() throws EGGException {
        // les globales
        if (globs.values().size() != 0) {
            entete.append("eggglobals{\n");
            for (Iterator e = globs.values().iterator(); e.hasNext();) {
                entete.append(e.next() + "\n");
            }
            entete.append("}\n\\\\\n\\\\");
        }

        // les actions semantiques
        for (Iterator e = acts.values().iterator(); e.hasNext();) {
            entete.append(e.next() + "\n");
        }
        return entete.toString();
    }
}
