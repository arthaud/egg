package mg.egg.eggc.compiler.libegg.java;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ENTREE;
import mg.egg.eggc.compiler.libegg.base.GLOB;
import mg.egg.eggc.compiler.libegg.base.IVisiteurAction;
import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.compiler.libegg.base.UN_ATTRIBUT;
import mg.egg.eggc.compiler.libegg.base.VAR;
import mg.egg.eggc.compiler.libegg.type.IType;

public class VisiteurActionJava implements IVisiteurAction, Serializable {
	private static final long serialVersionUID = 1L;

	private TDS table;

	public VisiteurActionJava(TDS table) {
		this.table = table;
	}

	public String insts() {
		return "// instructions\n";
	}

	public String locales() {
		return "// locales\n";
	}

	public String aff(ENTREE entree, String code) {
		return var(entree) + "=" + code + ";";
	}

	public String appel(ENTREE entree, String f, Vector<String> args) {
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

		return "	" + var(entree) + "." + f + "(" + cargs.toString() + ");";
	}

	public String nouveau(ENTREE entree, Vector<String> args) {
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

		return var(entree) + " = new "
				+ UtilsJava.getTypeJava(entree.getType()) + "("
				+ cargs.toString() + ");";
	}

	public String nouveau(IType type, Vector<String> args) {
		StringBuffer cargs = new StringBuffer();

		if (args != null) {
			boolean premier = true;

			for (Enumeration e = args.elements(); e.hasMoreElements();) {
				if (!premier) {
					cargs.append(", ");
				} else {
					premier = false;
				}

				cargs.append((String) e.nextElement());
			}
		}

		return " new " + UtilsJava.getTypeJava(type) + "(" + cargs.toString()
				+ ")";
	}

	public String ecrire(String code) {
		return "System.out.print(" + "\"\"+" + code + ");";
	}

	public String fatal(String id, Vector<String> args) {
		StringBuffer sb = new StringBuffer();
		sb.append("att_scanner._interrompre(IProblem.Semantic, att_scanner.getBeginLine(), "
				+ "I"
				+ table.getNom()
				+ "Messages.id_"
				+ id
				+ ", "
				+ table.getNom() + "Messages." + id + ",new Object[]{");
		boolean premier = true;

		for (Enumeration e = args.elements(); e.hasMoreElements();) {
			if (!premier) {
				sb.append(", ");
			} else {
				premier = false;
			}

			sb.append("\"\"+" + (String) e.nextElement());
		}

		sb.append("});\n");
		return sb.toString();
	}

	public String signaler(String id, Vector<String> args) {
		StringBuffer sb = new StringBuffer();
		sb.append("{ String as[]={\n");
		boolean premier = true;

		for (Enumeration e = args.elements(); e.hasMoreElements();) {
			if (!premier) {
				sb.append(", ");
			} else {
				premier = false;
			}

			sb.append((String) e.nextElement());
		}

		sb.append("}\n;");
		sb.append("att_scanner._signaler(IProblem.Semantic, att_scanner.getBeginLine(), "
				+ "I"
				+ table.getNom()
				+ "Messages.id_"
				+ id
				+ ", "
				+ table.getNom() + "Messages." + id + ",as);\n}\n");

		return sb.toString();
	}

	public String ifExpr(String e, String a, String s) {
		return "if (" + e + "){\n" + a + "}\n" + s;
	}

	public String ifSinonSi(String e, String a, String s) {
		return "else if (" + e + "){\n" + a + "}\n" + s;
	}

	public String ifSinon(String s) {
		return "else {\n" + s + "}";
	}

	public String ifFin() {
		return "";
	}

	public String matchVarAvec(ENTREE entree, String nom, String w, String ws) {
		// return "if (" + var(entree) + " instanceof " + nom +" ){\n" + w +
		// "}\n" + ws ;
		return "";
	}

	public String matchSi(ENTREE entree, String nom, String w, String ws) {
		return "if (" + var(entree) + " instanceof " + nom + " ){\n" + w
				+ "}\n" + ws;
	}

	public String matchSinonSi(ENTREE entree, String nom, String w, String ws) {
		return "else if (" + var(entree) + " instanceof " + nom + " ){\n" + w
				+ "}\n" + ws;
	}

	public String matchSinonSi(ENTREE entree, IType t, String w, String ws) {
		return "else if (" + var(entree) + " instanceof "
				+ UtilsJava.getTypeJava(t) + " ){\n" + w + "}\n" + ws;
	}

	public String matchVarAvec(ENTREE entree, IType t, String w, String ws) {
		return "";
	}

	public String matchSi(ENTREE entree, IType t, String w, String ws) {
		return "if (" + var(entree) + " instanceof " + UtilsJava.getTypeJava(t)
				+ " ){\n" + w + "}\n" + ws;
	}

	public String matchSinon(String code) {
		return "else {\n" + code + "\n}";
	}

	public String matchFin() {
		return "";
	}

	public String opAdd(String avant, String nom, String code) {
		return avant + UtilsJava.getOpJava(nom) + code;
	}

	public String vide() {
		return "null";
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
		return "-(" + txt + ")";
	}

	public String reel(String txt) {
		return txt;
	}

	// la version avec %N et %T
	public String chaine(String ch) {
		char[] cch = ch.toCharArray();
		int lch = ch.length();
		for (int i = 0; i < ch.length(); i++) {
			if (cch[i] == '%') {
				if (i + 1 < lch) {
					if (cch[i + 1] == 'N') {
						cch[i] = '\\';
						cch[i + 1] = 'n';
					} else if (cch[i + 1] == 'T') {
						cch[i] = '\\';
						cch[i + 1] = 't';
					}
				}
			}
		}
		return new String(cch);
	}

	public String decl(ENTREE entree) {
		return UtilsJava.getTypeJava(entree.getType()) + " " + var(entree)
				+ ";";
	}

	public void transtyper(ENTREE entree, IType t) {
		entree.setTypeReel(t);
	}

	public void detranstyper(ENTREE entree) {
		entree.resetTypeReel();
	}

	public String var(ENTREE entree) {
		String n = null;
		if (entree instanceof UN_ATTRIBUT) {
			UN_ATTRIBUT ua = (UN_ATTRIBUT) entree;
			n = UtilsJava.getPar(ua.getPos()) + ".att_"
					+ ua.getNom().substring(ua.getNom().indexOf('^') + 1);
		} else if (entree instanceof VAR) {
			VAR v = (VAR) entree;
			n = "loc_" + v.getNom();
		} else if (entree instanceof GLOB) {
			GLOB g = (GLOB) entree;
			n = "glob_" + g.getRegle().getNumero() + "_" + g.getNom();
		}
		// transtypage

		IType tr = entree.getTypeReel();
		if (tr != null)
			n = "((" + UtilsJava.getTypeJava(tr) + ")" + n + ")";

		return n;
	}

	public String fct(String code, String f, Vector<String> args) {
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

		return "(" + code + ")." + f + "(" + cargs.toString() + ")";
	}

	public String non(String code) {
		return "!(" + code + ")";
	}

	public String opMul(String avant, String nom, String code) {
		return avant + UtilsJava.getOpJava(nom) + code;
	}

	public String car(String s) {
		if (s.equals(" "))
			return "\\ ";
		else if (s.equals("\\$"))
			return "\"$\"";
		else if (s.equals("\\^"))
			return "\"^\"";
		else if (s.equals("\\("))
			return "\\(";
		else if (s.equals("\\)"))
			return "\\)";
		else if (s.equals("\\{"))
			return "\\{";
		else if (s.equals("\\}"))
			return "\\}";
		else
			return s;
	}

	public String indent() {
		return "";
	}

	public void incIndent() {
	}

	public void decIndent() {
	}

	public void resetIndent() {
	}
}
