package mg.egg.eggc.compiler.libegg.java;

import java.io.Serializable;
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
import mg.egg.eggc.compiler.libegg.base.VisiteurActionNull;
import mg.egg.eggc.compiler.libegg.type.IType;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;

public class VisiteurEggJava implements IVisiteurEgg, Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * un seul visiteur d'action par visiteurEgg
	 * 
	 */
	private IVisiteurAction vact;

	public IVisiteurAction getVisAction() {
		return vact;
	}

	// les non-terminaux
	private Vector<NtJava> nterms;

	private NtJava getNterm(String n) {
		for (Enumeration e = nterms.elements(); e.hasMoreElements();) {
			NtJava ntj = (NtJava) e.nextElement();
			if (ntj.getNom().equals(n))
				return ntj;
		}
		return null;
	}

	// l'analyseur lexical
	private LexJava lex;

	// le main
	private CompJava comp;

	// le module
	private ModJava mod;

	// les messages
	private MJava mess;

	// la grammaire
	transient private TDS table;

	public VisiteurEggJava(TDS t) {
		table = t;
		nterms = new Vector<NtJava>();
		// terms = new Vector<TJava>();
		lex = new LexJava(t);
		mod = new ModJava(t);
		mess = new MJava(t);
		if (t.syntaxOnly())
			vact = new VisiteurActionNull(t);
		else
			vact = new VisiteurActionJava(t);
	}

	// genere main() du compilo
	public void racinec() {
		comp = new CompJava(table);
	}

	// genere module
	public void racine() {
	}

	// genere message
	public void m_entete(String m) {
		return;
	}

	// genere l'analyseur lexical
	public void lexical() {
	}

	// appele à la declaration du non terminal externe
	public void ex_entete(NON_TERMINAL nt) {
	}

	/**
	 * genere l'analyse syntaxique d'un terminal ( = accepter) appele a la
	 * creation du terminal
	 * 
	 * @param t
	 */
	public void t_entete(TERMINAL t) {
	}

	// genere l'entete d'un non terminal
	// appele à la creation du non terminal
	public void nt_entete(NON_TERMINAL nt) {
		// System.err.println("nt_entete : nom " + nt.getNom());
		NtJava c = getNterm(nt.getNom());
		if (c == null) {
			c = new NtJava(nt, table);
			nterms.add(c);
		}
	}

	// appele a chaque creation de regle
	public void regle(REGLE r) {
	}

	// appele à la fin de la description de la regle
	public void nt_regle(REGLE r) {
		// le non terminal de gauche
		NON_TERMINAL nt = (NON_TERMINAL) r.getGauche().getSymbole();
		// le nterm de gauche
		NtJava c = getNterm(nt.getNom());
		if (c == null) {
			// le creer
			c = new NtJava(nt, table);
			// l'ajouter à la liste
			nterms.add(c);
		}
	}

	// utile ?
	public void t_attribut(TERMINAL t, ATTRIBUT a) {
	}

	// genere le code d'un attribut semantique
	public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a) {
		NtJava c = getNterm(nt.getNom());
		if (c == null) {
			c = new NtJava(nt, table);
			nterms.add(c);
		}
	}

	// obsolete : genere le code d'un attribut semantique
	public void nt_attribut(ATTRIBUT a) {
	}

	// obsolete : genere le code d'une var globale
	public void nt_globale(NON_TERMINAL nt, GLOB g) {
	}

	//
	public void globale(REGLE r, GLOB g) {
	}

	// genere le code d'une action semantique
	// action_xxx
	// appele a la creation de l'action
	// nom (n), regle(r), code (c)
	public void nt_action(ActREGLE a) {
	}

	public String car(String s) {
		// System.err.println("Appel visiteur car :" + s);
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
		else if (s.equals("\\{"))
			return "\\{";
		else
			return s;
	}

	private void genVisitor(String pack, Vector<String> incs)
			throws EGGException {
		String nom = table.getNom() + "Visitor";
		StringBuffer sb = new StringBuffer();
		sb.append("package " + pack + ";\n");
		for (String e : incs) {
			sb.append("import " + e + ";\n");
		}
		sb.append("import mg.egg.eggc.runtime.libjava.*;\n");
		// creation de la classe
		sb.append("public class " + nom + " implements IDstVisitor {\n");
		sb.append("protected boolean visitNode(IDstNode node){\n  return true;}\n");
		sb.append("public boolean visit(IDstNode node){\n");
		// les visiteurs externes
		Vector<String> evs = new Vector<String>();
		// redirection vers les visiteurs specifiques
		for (NtJava nt : nterms) {
			if (nt.estExterne()) {
				String en = nt.getNom();
				evs.add(en);
				sb.append("  if (node.getClass() ==  c" + en + "){\n");
				sb.append("    node.accept(f" + en + "Visitor);\n");
				sb.append("  }\n");
			} else {
				sb.append("  if (node instanceof " + nt.getNomClasse() + ")\n");
				sb.append("    return visit((" + nt.getNomClasse()
						+ ") node);\n");
			}
		}
		// le terminal
		sb.append("  if (node instanceof T_" + table.getNom() + ")\n");
		sb.append("    return visit((T_" + table.getNom() + ") node);\n");
		sb.append("  return false;\n}\n");
		sb.append("public void endVisit(IDstNode node){\n");
		// redirection vers les visiteurs specifiques
		for (NtJava nt : nterms) {
			if (nt.estExterne()) {
			} else {
				sb.append("  if (node instanceof " + nt.getNomClasse() + ")\n");
				sb.append("    endVisit((" + nt.getNomClasse() + ") node);\n");
			}
		}
		sb.append("  }\n");
		// declaration des externes
		for (String ev : evs) {
			sb.append("private Class c" + ev + "= new " + ev
					+ "().getAxiom().getClass();\n");
			sb.append("public " + ev + "Visitor f" + ev + "Visitor;\n");
		}
		// creation du constructeur sans arg
		sb.append("public " + nom + "(");
		int i = 0;
		for (String ev : evs) {
			sb.append(ev + "Visitor v" + ++i);
			if (i < evs.size())
				sb.append(",");
		}
		i = 1;
		sb.append("){\n");
		for (String ev : evs) {
			sb.append("  f" + ev + "Visitor = v" + i++ + ";\n");
		}
		sb.append("  }\n");

		// creation du constructeur avec arg
		if (evs.size() != 0) {
			sb.append("public " + nom + "(){\n");
			for (String ev : evs) {
				sb.append("  f" + ev + "Visitor = new " + ev + "Visitor();\n");
			}

			sb.append("  }\n");
		}
		// creation des visiteurs specifiques pour les non-terminaux
		for (NtJava nt : nterms) {
			if (nt.estExterne())
				continue;
			sb.append("public boolean visit(" + nt.getNomClasse()
					+ " node ){\n  return visitNode(node);\n}\n");
			sb.append("public void endVisit(" + nt.getNomClasse()
					+ " node){\n}\n");
		}
		// idem pour les terminaux
		sb.append("public boolean visit(T_" + table.getNom()
				+ " node){\n  return false;}\n");
		sb.append("public void endVisit(T_" + table.getNom() + " node){\n}\n");
		sb.append("}\n");
		table.getCompilationUnit().createFile(nom + ".java", sb.toString());
	}

	// finaliser = ecrire ds un fichier par exemple
	public void finaliser() throws EGGException {
		IEGGCompilationUnit cu = table.getCompilationUnit();
//		System.err.println("etat compilation " + cu.isOk());
		if (!cu.isOk())
			return;
		// achanger puisqu'on ne serialise plus la tds
		cu.writeTds(table);
		// modif get package straight from prefix
		String ngen = table.directory().replace('\\', '/');
		String pack = ngen.replace('/', '.');
		if (!"".equals(table.prefix()))
			pack = table.prefix() + "." + pack;
		Vector<String> incs = table.getOptions().getLibs();
		lex.finaliser(pack, incs);
		if (comp != null) {
			comp.finaliser(pack, incs);
		}
		// pour les modules
		if (mod != null) {
			mod.finaliser(pack, incs);
		}
		for (NtJava nt : nterms) {
			nt.finaliser(pack, incs);
		}
		// finaliser les messages d'erreurs
		mess.finaliser(pack);
		// try {
		if (table.getDst())
			genVisitor(pack, incs);
		// } catch (EGGException ee) {
		// // return ee.getErreur();
		// }
	}

	public static String getOpJava(String op) {
		if (op.equals("+") || op.equals("+."))
			return "+";
		else if (op.equals("-") || op.equals("-."))
			return "-";
		else if (op.equals("*") || op.equals("*."))
			return "/";
		else if (op.equals("/") || op.equals("/."))
			return "/";
		else if (op.equals("and"))
			return "&&";
		else if (op.equals("or"))
			return "||";
		else if (op.equals("~"))
			return "!";
		else if (op.equals("@"))
			return "+";
		else if (op.equals("="))
			return "==";
		else if (op.equals("/="))
			return "!=";
		else
			return op;
	}

	public static String getTypeJava(String type) {
		if (type.equals("STRING"))
			return "String";
		else if (type.equals("STRINGS"))
			return "String []";
		else if (type.equals("BOOLEAN"))
			return "boolean";
		else if (type.equals("INTEGER"))
			return "int";
		else if (type.equals("CHARACTER"))
			return "char";
		else if (type.equals("VOID"))
			return "void";
		else if (type.equals("ANY"))
			return "Object";
		else if (type.equals("DOUBLE"))
			return "double";
		else if (type.equals("VECTOR"))
			return "Vector";
		else if (type.equals("Non determine"))
			return "/* Non determine */ Object";
		else
			return type;
	}

	public static String getTypeJava(IType type) {
		String ntype = type.getNom();
		StringBuffer stype = new StringBuffer();
		if (ntype.equals("STRING"))
			stype.append("String");
		else if (ntype.equals("STRINGS"))
			stype.append("String []");
		else if (ntype.equals("BOOLEAN"))
			stype.append("boolean");
		else if (ntype.equals("INTEGER"))
			stype.append("int");
		else if (ntype.equals("CHARACTER"))
			stype.append("char");
		else if (ntype.equals("VOID"))
			stype.append("void");
		else if (ntype.equals("ANY"))
			stype.append("Object");
		else if (ntype.equals("DOUBLE"))
			stype.append("double");
		else if (ntype.equals("Non determine"))
			stype.append("/* Non determine */ Object");
		else
			stype.append(ntype);
		Vector<IType> pars = type.getPars();
		if (pars != null) {
			// ajouter les parametres eventuels
			stype.append("<");
			boolean premier = true;
			for (IType p : pars) {
				if (!premier)
					stype.append(", ");
				else
					premier = false;
				stype.append(getTypeJava(p));
			}
			stype.append(">");
		}
		return stype.toString();
	}

	public static String getPar(int i) {
		if (i == 0)
			return "this";
		else
			return "x_" + i;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("VisiteurEggJava" + "\n");

		sb.append("lexical " + lex + "\n");
		sb.append("comp " + comp + "\n");
		sb.append("mod " + mod + "\n");
		sb.append("mess " + mess + "\n");
		for (Enumeration e = nterms.elements(); e.hasMoreElements();) {
			sb.append("non terminal " + (NtJava) e.nextElement() + "\n");
		}
		return sb.toString();
	}

}
