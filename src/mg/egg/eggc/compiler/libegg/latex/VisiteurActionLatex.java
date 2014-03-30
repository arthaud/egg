////////////////////////////////////////////////////////////
//// version prevue pour gerer un compilateur incremental
//// pour eclipse mais pour l'instant tout est regenere 
//// a chaque fois.
//////////////////////////// MG 04-05 /////////////////////

package mg.egg.eggc.compiler.libegg.latex;

import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ENTREE;
import mg.egg.eggc.compiler.libegg.base.IVisiteurAction;
import mg.egg.eggc.compiler.libegg.type.IType;

public class VisiteurActionLatex implements IVisiteurAction {
    // niveau d'indentation
    private int nindent;

    private boolean locals;

    public void resetIndent() {
        nindent = 0;
    }

    public VisiteurActionLatex() {
        nindent = 0;
        locals = false;
    }

    public String insts() {
        StringBuffer sb = new StringBuffer();
        if (locals) {
            sb.append("}\n");
        }
        sb.append("\\actinsts{\n");
        locals = false;
        return sb.toString();
    }

    public String locales() {
        locals = true;
        return "\\actlocals{\n";
    }

    public String aff(ENTREE entree, String code) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actaffect{" + var(entree) + "}{" + code + "}");
        //		sb.append(var(entree) + ":=" + code + ";");
        return sb.toString();
    }

    public String appel(ENTREE entree, String f, Vector<String> args) {
        StringBuffer sb = new StringBuffer();
        StringBuffer cargs = new StringBuffer();
        boolean premier = true;
        for (Enumeration e = args.elements(); e.hasMoreElements();) {
            if (!premier) {
                cargs.append(", ");
            } else {
                premier = false;
            }
            cargs.append((String) e.nextElement());
        }
        sb.append("\\actcall{" + var(entree) + "." + f + "}{"
                + cargs.toString() + "}");
        //		sb.append("call " + var(entree) + "." + f + "(" + cargs.toString()
        //				+ ");");
        return sb.toString();
    }

    public String nouveau(ENTREE entree, Vector<String> args) {
        StringBuffer sb = new StringBuffer();

        // remplacement des caractères spéciaux
        String t = entree.getType().getNom();
        t = t.replace("_", "\\_");
        t = t.replace("<", "$<$");
        t = t.replace(">", "$>$");

        StringBuffer cargs = new StringBuffer();
        boolean premier = true;
        for (Enumeration e = args.elements(); e.hasMoreElements();) {
            if (!premier) {
                cargs.append(", ");
            } else {
                premier = false;
            }
            cargs.append((String) e.nextElement());
        }
        sb.append("\\actnew{" + t + "}{" + cargs.toString() + "};");
        //		sb.append("new " + getTypeLatex(entree.getType().getNom()) + ".make("
        //				+ cargs.toString() + ");");
        return sb.toString();
    }

    public String nouveau(IType type, Vector<String> args) {
        StringBuffer cargs = new StringBuffer();

        // remplacement des caractères spéciaux
        String t = type.getNom();
        t = t.replace("_", "\\_");
        t = t.replace("<", "$<$");
        t = t.replace(">", "$>$");

        boolean premier = true;
        for (Enumeration e = args.elements(); e.hasMoreElements();) {
            if (!premier) {
                cargs.append(", ");
            } else {
                premier = false;
            }
            cargs.append((String) e.nextElement());
        }
        return "\\actnew{" + t + "}{" + cargs.toString() + "}";
        //		return " new " + getTypeLatex(type.getNom()) + "(" + cargs.toString()
        //				+ ")";
    }

    public String ecrire(String code) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actwrite{" + code + "}");
        //		sb.append("write " + code + ";");
        return sb.toString();
    }

    public String fatal(String id, Vector<String> args) {
        StringBuffer sb = new StringBuffer();

        // remplacement des caractères spéciaux
        String i = id;
        i = i.replace("_", "\\_");

        sb.append("\\acterror{" + i + "}{");
        //		sb.append("error(\\verb!" + id + "!");
        for (Enumeration e = args.elements(); e.hasMoreElements();) {
            sb.append(", ");
            sb.append((String) e.nextElement());
        }
        sb.append("}");
        return sb.toString();
    }

    public String signaler(String id, Vector<String> args) {
        StringBuffer sb = new StringBuffer();

        // remplacement des caractères spéciaux
        String i = id;
        i = i.replace("_", "\\_");

        sb.append("\\actwarning{" + i + "}{");
        //		sb.append("warning(\\verb!" + id + "!");
        for (Enumeration e = args.elements(); e.hasMoreElements();) {
            sb.append(", ");
            sb.append((String) e.nextElement());
        }
        sb.append("}");
        return sb.toString();
    }

    public String ifExpr(String e, String a, String s) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actif{" + e + "}{\n" + a + "}{\n" + s + "}");
        //		sb.append("if " + e + " then\n");
        //		sb.append(a);
        //		sb.append(s);
        return sb.toString();
    }

    public String ifSinonSi(String e, String a, String s) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actelseif{" + e + "}{\n" + a + "}{\n" + s + "}\n");
        //		sb.append("elseif " + e + " then\n");
        //		sb.append(a);
        //		sb.append(s);
        return sb.toString();
    }

    public String ifSinon(String s) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actelse{" + s + "}\n");
        //		sb.append("else\n");
        //		sb.append(s);
        return sb.toString();
    }

    public String ifFin() {
        //		StringBuffer sb = new StringBuffer();
        //		sb.append("end");
        return "";
    }

    public String matchVarAvec(ENTREE entree, String nom, String w, String ws) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actmatch{" + var(entree) + "}{\n");
        //		sb.append("match " + var(entree) + "\n");
        return sb.toString();
    }

    public String matchSi(ENTREE entree, String nom, String w, String ws) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actwith{" + nom + "}{" + w + "}\n" + ws);
        //		sb.append("with " + nom + " then\n");
        //		sb.append(w);
        //		sb.append(ws);
        return sb.toString();
    }

    public String matchSinonSi(ENTREE entree, String nom, String w, String ws) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actwith{" + nom + "}{" + w + "}\n" + ws);
        //		sb.append("with " + nom + " then\n");
        //		sb.append(w);
        //		sb.append(ws);
        return sb.toString();
    }

    public String matchSinon(String code) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\actelse{" + code + "}\n");
        //		sb.append("else\n");
        //		sb.append(code);
        return sb.toString();
    }

    public String matchFin() {
        StringBuffer sb = new StringBuffer();
        sb.append("}");
        return sb.toString();
    }

    public String opAdd(String avant, String nom, String code) {
        return avant + getOpLatex(nom) + code;
    }

    public String vide() {
        return "\\nil";
    }

    public String vrai() {
        return "\\true";
    }

    public String faux() {
        return "\\false";
    }

    public String entier(String txt) {
        return txt;
    }

    public String moins(String txt) {
        return "-" + txt;
    }

    public String reel(String txt) {
        return txt;
    }

    // la version avec %N et %T
    public String chaine(String ch) {
        return ch;
    }

    public String decl(ENTREE entree) {

        // remplacement des caractères spéciaux
        String type = entree.getType().getNom();
        type = type.replace("_", "\\_");
        type = type.replace("<", "$<$");
        type = type.replace(">", "$>$");

        return "\\actlocal{" + var(entree) + "}{" + type + "}";
        //		return var(entree) + " : " + getTypeLatex(entree.getType().getNom())
        //				+ ";";
    }

    public void transtyper(ENTREE entree, IType t) {
        // entree.setTypeReel(t);
    }

    public void detranstyper(ENTREE entree) {
        // entree.setTypeReel(null);
    }

    public String var(ENTREE entree) {
        String nom = entree.getNom();
        nom = nom.replace("_", "\\_");
        nom = nom.replace("^", "\\^{ }");
        System.out.println();
        return "\\eggvar{" + nom + "}";
    }

    public String fct(ENTREE entree, String f, Vector<String> args) {
        StringBuffer cargs = new StringBuffer();
        boolean premier = true;
        for (Enumeration e = args.elements(); e.hasMoreElements();) {
            if (!premier) {
                cargs.append(", ");
            } else {
                premier = false;
            }
            cargs.append((String) e.nextElement());
        }
        return "\\actfct{" + var(entree) + "." + f + "}{" + cargs.toString()
                + "}\n";
        //		return var(entree) + "." + f + "(" + cargs.toString() + ")";
    }

    public String non(String code) {
        return "~" + code;
    }

    public String opMul(String avant, String nom, String code) {
        return avant + getOpLatex(nom) + code;
    }

    public String car(String s) {
        return s;
    }

    protected static String getOpLatex(String op) {
        return op;
    }

    protected static String getTypeLatex(String type) {
        return type;
    }

    public String indent() {
        StringBuffer sb = new StringBuffer();
        // sb.append(nindent + "\t\t");
        for (int i = nindent; i > 0; i--) {
            sb.append("  ");
        }
        return sb.toString();
    }

    public void incIndent() {
        //nindent++;
    }

    public void decIndent() {
        //nindent--;
    }

    public String matchSinonSi(ENTREE entree, IType t, String w, String ws) {
        // TODO Auto-generated method stub
        return null;
    }

    public String matchVarAvec(ENTREE entree, IType t, String w, String ws) {
        // TODO Auto-generated method stub
        return null;
    }

    public String matchSi(ENTREE entree, IType t, String w, String ws) {
        // TODO Auto-generated method stub
        return null;
    }

    public void m_entete(String m) {

    }

    public String exc(String code, String cinsts) {
        // TODO Auto-generated method stub
        return null;
    }
}
