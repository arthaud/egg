package mg.egg.eggc.compiler.libegg.egg;

import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ENTREE;
import mg.egg.eggc.compiler.libegg.base.IVisiteurAction;
import mg.egg.eggc.compiler.libegg.type.IType;

public class VisiteurActionEgg implements IVisiteurAction {
	private int nindent;

	public void resetIndent() {
	    nindent = 0;
	}

	public VisiteurActionEgg() {
	    nindent = 0;
	}

	public String insts() {
	    return "\n";
	}

	public String locales() {
	    return "-- locals\n";
	}

	public String aff(ENTREE entree, String code) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(var(entree) + ":=" + code + ";");
	    return sb.toString();
	}

	public String appel(ENTREE entree, String f, Vector args) {
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
	    sb.append("call " + var(entree) + "." + f + "(" + cargs.toString()
	            + ");");

	    return sb.toString();
	}

	public String nouveau(ENTREE entree, Vector args) {
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

	    sb.append("new " + getTypeEgg(entree.getType().getNom()) + ".make("
	            + cargs.toString() + ");");
	    return sb.toString();
	}

	public String nouveau(IType type, Vector args) {
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

	    return " new " + getTypeEgg(type.getNom()) + "(" + cargs.toString()
	            + ")";
	}

	public String ecrire(String code) {
	    return "write " + code + ";";
	}

	public String fatal(String id, Vector args) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("error(" + id);

	    for (Enumeration e = args.elements(); e.hasMoreElements();) {
	        sb.append(", ");
	        sb.append((String) e.nextElement());
	    }

	    sb.append(");");
	    return sb.toString();
	}

	public String signaler(String id, Vector args) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("warning(" + id);

	    for (Enumeration e = args.elements(); e.hasMoreElements();) {
	        sb.append(", ");
	        sb.append((String) e.nextElement());
	    }

	    sb.append(");");
	    return sb.toString();
	}

	public String ifExpr(String e, String a, String s) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("if (" + e + ") {\n");
	    sb.append(a);
	    sb.append("\n}\n");
	    sb.append(s);
	    return sb.toString();
	}

	public String ifSinonSi(String e, String a, String s) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("elseif (" + e + ") {\n");
	    sb.append(a);
	    sb.append("\n}\n");
	    sb.append(s);
	    return sb.toString();
	}

	public String ifSinon(String s) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("else {\n");
	    sb.append(s);
	    sb.append("\n}\n");
	    return sb.toString();
	}

	public String ifFin() {
        return "";
	}

	public String matchVarAvec(ENTREE entree, String nom, String w, String ws) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("match " + var(entree) + "\n");
	    return sb.toString();
	}

	public String matchSi(ENTREE entree, String nom, String w, String ws) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("with " + nom + " then\n");
	    sb.append(w);
	    sb.append(ws);
	    return sb.toString();
	}

	public String matchSinonSi(ENTREE entree, String nom, String w, String ws) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("with " + nom + " then\n");
	    sb.append(w);
	    sb.append(ws);
	    return sb.toString();
	}

	public String matchVarAvec(ENTREE entree, IType t, String w, String ws) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("match " + var(entree) + "\n");
	    return sb.toString();
	}

	public String matchSi(ENTREE entree, IType t, String w, String ws) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("with " + getTypeEgg(t.getNom()) + " then\n");
	    sb.append(w);
	    sb.append(ws);
	    return sb.toString();
	}

	public String matchSinonSi(ENTREE entree, IType t, String w, String ws) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("with " + getTypeEgg(t.getNom()) + " then\n");
	    sb.append(w);
	    sb.append(ws);
	    return sb.toString();
	}

	public String matchSinon(String code) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("else\n");
	    sb.append(code);
	    return sb.toString();
	}

	public String matchFin() {
        return "";
	}

	public String opAdd(String avant, String nom, String code) {
	    return avant + getOpEgg(nom) + code;
	}

	public String vide() {
	    return "nil";
	}

	public String vrai() {
	    return "true";
	}

	public String faux() {
	    return "false";
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
	    return var(entree) + " : " + getTypeEgg(entree.getType().getNom())
	            + ";";
	}

	public void transtyper(ENTREE entree, IType t) {
	}

	public void detranstyper(ENTREE entree) {
	}

	public String var(ENTREE entree) {
	    return entree.getNom();
	}

	public String fct(ENTREE entree, String f, Vector args) {
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

	    return var(entree) + "." + f + "(" + cargs.toString() + ")";
	}

	public String non(String code) {
	    return "~" + code;
	}

	public String opMul(String avant, String nom, String code) {
	    return avant + getOpEgg(nom) + code;
	}

	public String car(String s) {
	    return s;
	}

	protected static String getOpEgg(String op) {
	    return op;
	}

	protected static String getTypeEgg(String type) {
	    return type;
	}

	public String indent() {
	    StringBuffer sb = new StringBuffer();

	    for (int i = nindent; i > 0; i--) {
	        sb.append("  ");
	    }

	    return sb.toString();
	}

	public void incIndent() {
	    nindent++;
	}

	public void decIndent() {
	    nindent--;
	}

	public void m_entete(String m) {
	}

	public String exc(String code, String cinsts) {
	    return null;
	}

}
