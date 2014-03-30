package mg.egg.eggc.compiler.libegg.java;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ATTRIBUT;
import mg.egg.eggc.compiler.libegg.base.ActREGLE;
import mg.egg.eggc.compiler.libegg.base.EltREGLE;
import mg.egg.eggc.compiler.libegg.base.Feuille;
import mg.egg.eggc.compiler.libegg.base.GLOB;
import mg.egg.eggc.compiler.libegg.base.NON_TERMINAL;
import mg.egg.eggc.compiler.libegg.base.REGLE;
import mg.egg.eggc.compiler.libegg.base.SYMBOLE;
import mg.egg.eggc.compiler.libegg.base.SymbREGLE;
import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.compiler.libegg.base.TERMINAL;
import mg.egg.eggc.runtime.libjava.EGGException;

public class NtJava implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	transient private TDS table;
	private StringBuffer visit;
	private StringBuffer analyser;
	private NON_TERMINAL nonTerminal;
	private StringBuffer entete;
	private HashMap<String, String> regles;
	private HashMap<String, String> globs;
	private HashMap<String, String> atts;
	private HashMap<String, String> acts;

	public boolean estExterne() {
		return nonTerminal.estExterne();
	}

	private String nom;

	public String getNom() {
		return nom;
	}

	public String getNomClasse() {
		return "S_" + nom + "_" + table.getNom();
	}

	public void setNom(String n) {
		nom = n;
	}

	public StringBuffer getEntete() {
		return entete;
	}

	public void setEntete(String pack, Vector<String> incs) {
		entete = new StringBuffer();
		entete.append("package " + pack + ";\n");
		// new
		for (Enumeration e = incs.elements(); e.hasMoreElements();) {
			entete.append("import " + (String) e.nextElement() + ";\n");
		}
		entete.append("import mg.egg.eggc.runtime.libjava.lex.*;\n");
		entete.append("import mg.egg.eggc.runtime.libjava.*;\n");
		entete.append("import mg.egg.eggc.runtime.libjava.messages.*;\n");
		entete.append("import mg.egg.eggc.runtime.libjava.problem.IProblem;\n");
		entete.append("import java.util.Vector;\n");
		if (table.getDst()) {
			entete.append("import java.util.List;\n");
			entete.append("import java.util.ArrayList;\n");
			entete.append("public class " + getNomClasse()
					+ " implements IDstNode {\n");
		} else
			entete.append("public class " + getNomClasse() + " {\n");
		entete.append("LEX_" + table.getNom() + " scanner;\n");
		// constructeur sans arg
		entete.append("  " + getNomClasse() + "() {\n");
		entete.append("    }\n");
		entete.append("  " + getNomClasse() + "(LEX_" + table.getNom()
				+ " scanner, boolean eval) {\n");
		entete.append("    this.scanner = scanner;\n");
		entete.append("    this.att_eval = eval;\n");
		if (table.getDst()) {
			entete.append("    offset = 0;\n");
			entete.append("    length = 0;\n");
		}
		if (!table.syntaxOnly())
			entete.append("    this.att_scanner = scanner;\n");
		entete.append("    }\n");
		// synchro
		// Arbre suivants = nonTerminal.getK_suivants();
		int nsync = 0;
		entete.append("int [] sync= new int[" + nsync + "];\n");

	}

	public StringBuffer getAnalyser() {
		return analyser;
	}

	private String fatalSyntaxe(int n) {
		StringBuffer sb = new StringBuffer();
		// sb
		// .append("       String as[]={scanner.fenetre[" + n
		// + "].getNom()};\n");
		sb.append("       scanner._interrompre(IProblem.Syntax, scanner.getBeginLine(), "
				+ "I"
				+ table.getNom()
				+ "Messages.id_"
				+ table.getNom()
				+ "_unexpected_token,"
				+ table.getNom()
				+ "Messages."
				+ table.getNom()
				+ "_unexpected_token,new String[]{scanner.fenetre["
				+ n
				+ "].getNom()});\n");
		return sb.toString();
	}

	// ////////////////////////////////////////////////////////:
	private void l(StringBuffer a, Feuille feuille, int n) {
		Feuille p = feuille;
		if (feuille.getFils() != null) {
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("scanner.lit ( " + (n + 1) + " ) ;\n");
			// a.append("scanner.lit ( " + (table.getK()) + " ) ;\n");
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("switch ( scanner.fenetre[" + n + "].code ) {\n");
		}

		while (p != null) {
			si(a, p, n);
			p = p.getFrere();
		}
		if (feuille.getFils() != null) {
			for (int i = 0; i < 2 * n + 3; i++)
				a.append("  ");
			a.append("default :\n");
			for (int i = 0; i < 2 * n + 4; i++)
				a.append("  ");
			a.append(fatalSyntaxe(n));
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("}\n");
		}
	}

	private void l2(StringBuffer a, Feuille parent, Feuille feuille, int n) {
		Feuille p = feuille;
		if (feuille.getFils() != null) {
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("scanner.lit ( " + (n + 1) + " ) ;\n");
			// a.append("scanner.lit ( " + (table.getK()) + " ) ;\n");
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("switch ( scanner.fenetre[" + n + "].code ) {\n");
		}

		while (p != null) {
			si(a, p, n);
			p = p.getFrere();
		}
		if (feuille.getFils() != null) {
			for (int i = 0; i < 2 * n + 3; i++)
				a.append("  ");
			a.append("default :\n");
			for (int i = 0; i < 2 * n + 4; i++)
				a.append("  ");
			a.append(fatalSyntaxe(n));
			a.append("scanner.accepter_sucre(LEX_" + table.getNom() + ".token_"
					+ parent.getValeur().getNom() + ");\n");
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("}\n");
		}
	}

	private void si(StringBuffer a, Feuille feuille, int n) {
		if (feuille.getFils() == null) {
			for (int i = 0; i < 2 * n + 2; i++)
				a.append("  ");
			a.append("regle" + feuille.getValeur().getNumero() + " () ;\n");
		} else {
			for (int i = 0; i < 2 * n + 3; i++)
				a.append("  ");
			switch (feuille.getValeur().getNumero()) {
			case -1:
				a.append("case LEX_" + table.getNom() + ".EOF :\n");
				break;
			case -2:
				System.err.println("Erreur interne : Conflit non detecte");
			default:
				a.append("case LEX_" + table.getNom() + ".token_"
						+ feuille.getValeur().getNom() + " : // "
						+ feuille.getValeur().getNumero() + "\n");
				break;
			}
			l2(a, feuille, feuille.getFils(), n + 1);
			for (int i = 0; i < 2 * n + 3; i++)
				a.append("  ");
			a.append("break ;\n");
		}
	}

	public void setAnalyser() {
		analyser = new StringBuffer();
		analyser.append("  public void analyser () throws Exception {\n");
		l(analyser, nonTerminal.getK_premiers().getDebut(), 0);
		analyser.append("  }\n");
	}

	public String getRegle(String k) {
		return (String) regles.get(k);
	}

	public void addRegle(String k, String reg) {
		regles.put(k, reg);
	}

	// genere le code de l'analyse de la regle
	public void setRegle(REGLE r) {
		StringBuffer reg = new StringBuffer();
		StringBuffer decl = new StringBuffer(100);
		StringBuffer appel = new StringBuffer(100);
		StringBuffer appel_action = new StringBuffer(100);
		boolean premier = true;
		String dxi = null;
		String pxi = null;
		reg.append("  private void regle" + r.getNumero()
				+ "() throws Exception {\n");
		// if (table.getDst()) {
		// reg.append(" offset = scanner.getOffset(0);\n");
		// // reg.append(" offset = scanner.getOffset();\n");
		// }
		for (Enumeration d = r.getDroite().elements(); d.hasMoreElements();) {
			EltREGLE er = (EltREGLE) d.nextElement();
			// un symbREGLE ou un ActREGLE
			if (er instanceof SymbREGLE) {
				String xi = UtilsJava.getPar(er.getPos());
				if (pxi == null)
					pxi = xi; // first
				dxi = xi; // last
				SYMBOLE s = ((SymbREGLE) er).getSymbole();
				if (s instanceof TERMINAL) {
					TERMINAL t = (TERMINAL) s;
					switch (t.getType()) {
					case TERMINAL.TERM:
						if (!premier) {
							appel_action.append(", ");
						} else {
							premier = false;
						}
						appel_action.append(xi);
						break;
					case TERMINAL.SPACE:
					default:
						break;
					}
				} // terminal
				else { // s instanceof NON_TERMINAL )
					if (!premier) {
						appel_action.append(", ");
					} else {
						premier = false;
					}
					appel_action.append(xi);
				} // non terminal
			} // pas un SymbREGLE
			else {// un ActREGLE
			}
		} // for
			// 2eme passe ...
			// boolean lambda = true; // regle vide ?
		for (Enumeration d = r.getDroite().elements(); d.hasMoreElements();) {
			EltREGLE er = (EltREGLE) d.nextElement();
			if (er instanceof SymbREGLE) {
				// lambda = false;
				SYMBOLE s = ((SymbREGLE) er).getSymbole();
				String xi = UtilsJava.getPar(er.getPos());
				if (s instanceof TERMINAL) {
					TERMINAL t = (TERMINAL) s;
					switch (t.getType()) {
					case TERMINAL.TERM:
					case TERMINAL.SUGAR:
						decl.append("    T_" + table.getNom() + " " + xi
								+ " = new " + "T_" + table.getNom()
								+ "(scanner ) ;\n");
						// appel.append(" "+ xi + ".setSorte("+
						// t.getType()+");\n");
						appel.append("    " + xi + ".analyser(LEX_"
								+ table.getNom() + ".token_" + t.getNom()
								+ ");\n");
						if (table.getDst())
							appel.append("      addChild(" + xi + ");\n");
						break;
					// decl.append(" T_" + t.getNom() + "_" + table.getNom() + "
					// " + xi + " = new " + "T_"
					// + t.getNom() + "_" + table.getNom() + "(scanner ) ;\n");
					// appel.append(" " + xi + ".analyser() ;\n");
					// if (table.getDst())
					// appel.append(" addChild(" + xi + ");\n");
					// break;
					// case TERMINAL.SUGAR:
					// appel.append(" scanner.accepter_sucre(LEX_" +
					// table.getNom() + ".token_" + t.getNom()
					// + " ) ;\n");
					// break;
					case TERMINAL.SPACE:
					case TERMINAL.MACRO:
					default:
						break;
					}
				} else { // if ( s instanceof NON_TERMINAL ) {
					NON_TERMINAL un_nt = (NON_TERMINAL) s;
					if (un_nt.estExterne()) {
						decl.append("    "
								+ un_nt.getNom()
								+ " "
								+ xi
								+ " = new "
								+ un_nt.getNom()
								+ "(scanner.getReporter(), scanner.contexte);\n");
						appel.append("    " + xi
								+ ".scanner.setSource(scanner) ;\n");
						appel.append("    " + xi + ".set_eval(true) ;\n");
						appel.append("    " + xi + ".compile() ;\n");
						appel.append("      scanner.setSource(" + xi
								+ ".scanner) ;\n");
						if (table.getDst())
							appel.append("      addChild(" + xi
									+ ".getAxiom());\n");
					} else {
						decl.append("    S_" + un_nt.getNom() + "_"
								+ table.getNom() + " " + xi + " = new S_"
								+ un_nt.getNom() + "_" + table.getNom()
								+ "(scanner,att_eval) ;\n");
						// if (table.getDst()) {
						// appel.append(" " + xi +
						// ".setOffset(scanner.getOffset()+
						// scanner.getLength());\n");
						// }
						appel.append("    " + xi + ".analyser() ;\n");
						if (table.getDst())
							appel.append("      addChild(" + xi + ");\n");
					}
				}
			} // un SymbREGLE
			else { // un actREGLE
				if (!table.syntaxOnly()) {
					// suppression de l'appel si l'action n'est pas definie
					String nact = er.getNom().substring(1) + "_"
							+ r.getNumero();
					if (getAct(nact) != null) {
						appel.append("if  (att_eval)      action_" + nact + "("
								+ appel_action.toString() + ");\n");
					} else {
						// System.err.println("Suppression de " + nact);
					}
				}
			}
		}
		reg.append("\n    //declaration\n");
		reg.append(decl.toString());
		reg.append("    //appel\n");
		reg.append(appel.toString());
		if (table.getDst()) {
			if (dxi == null)
				reg.append("   length = 0; offset = scanner.getPreviousOffset()+ scanner.getPreviousLength();");
			else {
				reg.append("     offset =" + pxi + ".getOffset();\n");
				reg.append("     length =" + dxi + ".getOffset() + " + dxi
						+ ".getLength() - offset;\n");
			}
		}
		reg.append("  }\n");
		addRegle(nonTerminal.getNom() + r.getNumero(), reg.toString());
	}

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
		// visible dans le package
		String gn = g.getRegle().getNumero() + "_" + g.getNom();
		asb.append("  " + VisiteurEggJava.getTypeJava(g.getType()) + " glob_"
				+ gn + ";\n");
		// ajout dans la liste
		// addGlob(g.getNom(), asb.toString());
		addGlob(gn, asb.toString());
	}

	public String getAtt(String att) {
		return (String) atts.get(att);
	}

	public void addAtt(String att, String c) {
		atts.put(att, c);
	}

	public void delAtt(String att) {
		atts.remove(att);
	}

	public void setAtt(ATTRIBUT att) {
		StringBuffer asb = new StringBuffer();
		// visible dans le package
		asb.append("  " + VisiteurEggJava.getTypeJava(att.getType()) + " att_"
				+ att.getNom() + ";\n");
		// ajout dans la liste
		addAtt(att.getNom(), asb.toString());
	}

	public String getAct(String act) {
		return (String) acts.get(act);
	}

	public void addAct(String act, String c) {
		acts.put(act, c);
	}

	public void delAct(String act) {
		acts.remove(act);
	}

	public void setAct(REGLE r, ActREGLE act) {
		StringBuffer asb = new StringBuffer();
		// l'entete d'une action
		asb.append("private void action_" + act.getNom().substring(1) + "_"
				+ r.getNumero() + "(");
		// parametres ...
		boolean premier = true;
		for (Enumeration e = r.getDroite().elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();
			if (er instanceof SymbREGLE) {
				String xi = UtilsJava.getPar(er.getPos());
				SYMBOLE s = ((SymbREGLE) er).getSymbole();
				if (s instanceof TERMINAL) {
					TERMINAL t = (TERMINAL) s;
					switch (t.getType()) {
					case TERMINAL.TERM:
						if (!premier) {
							asb.append(", ");
						} else {
							premier = false;
						}
						asb.append("T_" + table.getNom() + " " + xi);
						break;
					case TERMINAL.SUGAR:
					case TERMINAL.SPACE:
						break;
					default:
						break;
					}
				} else { // if ( s instanceof NON_TERMINAL ) {
					NON_TERMINAL nt = (NON_TERMINAL) s;
					if (!premier) {
						asb.append(", ");
					} else {
						premier = false;
					}
					if (nt.estExterne()) {
						asb.append(nt.getNom() + " " + xi);
					} else {
						asb.append("S_" + nt.getNom() + "_" + table.getNom()
								+ " " + xi);
					}
				}
			} // un SymbREGLE
			else { // un actREGLE
			}
		}
		asb.append(") throws Exception {\n");
		// le code
		if (act.getCode() != null) {
			asb.append("try {\n");
			asb.append(act.getCode());
			// asb.append("}catch(RuntimeException e) {\t\te.printStackTrace();\n\t}\n");
			// asb.append("}catch(RuntimeException e) {\t\tSystem.err.println(\"Runtime Exception \" + e);\n\t}\n");
			asb.append("}catch(RuntimeException e) {");
			asb.append("       att_scanner._interrompre(IProblem.Internal,att_scanner.getBeginLine()");
			asb.append(",ICoreMessages.id_EGG_runtime_error, CoreMessages.EGG_runtime_error,new Object[] { "
					+ "\""
					+ table.getNom()
					+ "\", "
					+ "\""
					+ act.getNom()
					+ "\""
					+ ",\""
					+ act.getRegle().toStringSyntaxAction()
					+ "\"});\n");
			asb.append("}\n");
		}
		asb.append("  }\n");
		// ajout dans la liste
		addAct(act.getNom().substring(1) + "_" + r.getNumero(), asb.toString());
	}

	// genere le code de visite
	public void setVisit() {
		visit.append("    private IDstNode parent;\n");
		visit.append("    public void setParent( IDstNode p){parent = p;}\n");
		visit.append("    public IDstNode getParent(){return parent;}\n");
		visit.append("    private List<IDstNode> children = null ;\n");
		visit.append("    public void addChild(IDstNode node){\n");
		visit.append("      if (children == null) {\n");
		visit.append("         children = new ArrayList<IDstNode>() ;}\n");
		visit.append("      children.add(node);\n");
		visit.append("      node.setParent(this);\n");
		visit.append("    }\n");
		visit.append("    public List<IDstNode> getChildren(){return children;}\n");
		visit.append("    public boolean isLeaf(){return children == null;}\n");
		visit.append("    public void accept(IDstVisitor visitor) {\n");
		// visit.append(" visitor.startVisit(this);\n");
		visit.append("      boolean visitChildren = visitor.visit(this);\n");
		visit.append("      if (visitChildren && children != null){\n");
		visit.append("        for(IDstNode node : children){\n");
		visit.append("          node.accept(visitor);\n");
		visit.append("        }\n");
		visit.append("    }\n");
		visit.append("    visitor.endVisit(this);\n");
		visit.append("  }\n");
		visit.append("  private int offset;\n");
		visit.append("  private int length;\n");
		visit.append("  public int getOffset(){return offset;}\n");
		visit.append("  public void setOffset(int o){offset = o;}\n");
		visit.append("  public int getLength(){return length;}\n");
		visit.append("  public void setLength(int l){length = l;}\n");
		visit.append("  private boolean malformed = false;\n");
		visit.append("  public void setMalformed(){malformed = true;}\n");
		visit.append("  public boolean isMalformed(){return malformed;}\n");
	}

	public NtJava(NON_TERMINAL nt, TDS t) {
		nonTerminal = nt;
		nom = nt.getNom();
		table = t;
		regles = new HashMap<String, String>();
		atts = new HashMap<String, String>();
		acts = new HashMap<String, String>();
		globs = new HashMap<String, String>();
		// change = false;
	}

	// appele si chgt dans le nt
	public void finaliser(String pack, Vector<String> incs) throws EGGException {
		// rien a faire pour les externes...
		if (nonTerminal.estExterne())
			return;
		// // le fichier
		// ------- analyse ----------
		// l'entete
		setEntete(pack, incs);
		if (!table.syntaxOnly()) {
			// les attributs semantiques
			Vector<ATTRIBUT> attributs = nonTerminal.getAttributs();
			for (Enumeration e = attributs.elements(); e.hasMoreElements();) {
				ATTRIBUT a = (ATTRIBUT) e.nextElement();
				setAtt(a);
			}
		}
		// les regles
		// appeler setRegle sur toutes les regles
		Vector<REGLE> regs = nonTerminal.getRegles();
		for (Enumeration e = regs.elements(); e.hasMoreElements();) {
			REGLE r = (REGLE) e.nextElement();
			// les actions de la partie droite
			for (Enumeration d = r.getDroite().elements(); d.hasMoreElements();) {
				EltREGLE er = (EltREGLE) d.nextElement();
				// un symbREGLE ou un ActREGLE
				if (er instanceof SymbREGLE)
					continue;
				if (!table.syntaxOnly()) {
					ActREGLE ar = (ActREGLE) er;
					setAct(r, ar);
				}
			}
			if (!table.syntaxOnly()) {
				// les globales de la regle
				for (Enumeration d = r.getGlobales().elements(); d
						.hasMoreElements();) {
					GLOB g = (GLOB) d.nextElement();
					setGlob(g);
				}
			}
			setRegle(r);
		}
		// ------- synthese ----------
		// les attributs
		for (Iterator<String> e = atts.values().iterator(); e.hasNext();) {
			entete.append(e.next());
		}
		// les globales
		for (Iterator<String> e = globs.values().iterator(); e.hasNext();) {
			entete.append(e.next());
		}
		// les regles
		for (Iterator<String> e = regles.values().iterator(); e.hasNext();) {
			entete.append(e.next());
		}
		// les actions semantiques
		for (Iterator<String> e = acts.values().iterator(); e.hasNext();) {
			entete.append(e.next());
		}
		// la fonction d'analyse
		setAnalyser();
		entete.append(analyser.toString());
		if (table.getDst()) {
			// la fonction de visite
			visit = new StringBuffer(200);
			setVisit();
			// System.err.println("visit code" + visit);
			entete.append(visit.toString());
		}
		entete.append("  }\n");
		table.getCompilationUnit().createFile(getNomClasse() + ".java",
				entete.toString());

	}
}
