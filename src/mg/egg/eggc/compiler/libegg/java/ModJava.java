package mg.egg.eggc.compiler.libegg.java;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ATTRIBUT;
import mg.egg.eggc.compiler.libegg.base.NON_TERMINAL;
import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.EGGOptions;

public class ModJava implements Serializable {
	private static final long serialVersionUID = 1L;

	transient TDS table;
	private StringBuffer corps;
	private HashMap<String, String> atts;

	private String nom;

	public String getNom() {
		return nom;
	}

	public void setNom(String n) {
		nom = n;
	}

	public StringBuffer getCorps() {
		return corps;
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

		// pour affecter l'attribut de l'exterieur (herite)
		asb.append("  public void set_" + att.getNom() + "("
				+ VisiteurEggJava.getTypeJava(att.getType()) + " a_"
				+ att.getNom() + "){\n");
		asb.append("	att_" + att.getNom() + " = a_" + att.getNom()
				+ ";\n  }\n");

		// pour recuperer (synthetise)
		asb.append("  public " + VisiteurEggJava.getTypeJava(att.getType())
				+ " get_" + att.getNom() + "(){\n");
		asb.append("	return att_" + att.getNom() + ";\n  }\n");

		// ajout dans la liste
		addAtt(att.getNom(), asb.toString());
	}

	public void setCorps(String pack, Vector<String> incs) {
		EGGOptions options = table.getOptions();
		corps = new StringBuffer();
		corps.append("package " + pack + ";\n");

		// new
		for (Enumeration<String> e = incs.elements(); e.hasMoreElements();) {
			corps.append("import " + (String) e.nextElement() + ";\n");
		}

		corps.append("import mg.egg.eggc.runtime.libjava.EGGException;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.ISourceUnit;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.lex.LEX_CONTEXTE;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.problem.IProblemReporter;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.problem.IProblem;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.problem.ProblemSeverities;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.messages.NLS;\n");
		corps.append("public class " + getNom() + " {\n");
		corps.append("  LEX_" + table.getNom() + " scanner;\n");
		corps.append("  protected IProblemReporter problemReporter;\n");
		NON_TERMINAL ax = table.getAxiome();
		String axiome = ax.getNom();
		corps.append("	private S_" + axiome + "_" + table.getNom()
				+ " axiome ;\n");
		corps.append("	public  S_" + axiome + "_" + table.getNom()
				+ " getAxiom(){return axiome;}\n");

		if (options.getModule()) {
			// constructeur non module 1
			corps.append("  public " + getNom() + "() {\n");
			corps.append("	axiome = new S_" + axiome + "_" + table.getNom()
					+ "();\n");
			corps.append("	}\n");

			// constructeur non module 2
			corps.append("  public " + getNom()
					+ "(IProblemReporter pr, LEX_CONTEXTE lc){\n");
			corps.append("	  scanner = new LEX_" + table.getNom()
					+ " (pr, lc, " + options.getK() + ");\n");
			if (!table.syntaxOnly()) {
				corps.append("	  att_scanner = scanner;\n");
			}
			corps.append("	}\n");

			// compile module
			corps.append("  public void compile() throws Exception {\n");
			corps.append("	axiome = new S_" + axiome + "_" + table.getNom()
					+ "(scanner,att_eval);\n");
			if (!table.syntaxOnly()) {
				corps.append("	att_scanner = scanner;\n");
			}
			if (!table.syntaxOnly()) {
				for (Enumeration<ATTRIBUT> atts = ax.getAttributs().elements(); atts
						.hasMoreElements();) {
					ATTRIBUT a = (ATTRIBUT) atts.nextElement();
					if (a.getSorte() == ATTRIBUT.HER) {
						corps.append("	axiome.att_" + a.getNom()
								+ " = this.att_" + a.getNom() + " ;\n");
					}
				}
			}
			corps.append("	axiome.analyser() ;\n");
			if (!table.syntaxOnly()) {
				for (Enumeration<ATTRIBUT> atts = ax.getAttributs().elements(); atts
						.hasMoreElements();) {
					ATTRIBUT a = (ATTRIBUT) atts.nextElement();
					if (a.getSorte() == ATTRIBUT.SYN) {
						corps.append("	this.att_" + a.getNom()
								+ " = axiome.att_" + a.getNom() + " ;\n");
					}
				}
			}
			corps.append("	}\n");
		} else {
			// constructeur non module
			corps.append("  public " + getNom() + "(IProblemReporter pr){\n");
			corps.append("	   this.problemReporter = pr;\n");
			corps.append("  }\n");

			// compile non module
			corps.append("  public void compile(ISourceUnit cu) throws Exception {\n");

			// corps.append("  public void compile(ISourceUnit cu) {\n");
			corps.append("	try{\n");
			corps.append("	  System.err.println(\"" + table.getNom()
					+ " Version " + options.getVersion() + "\");\n");
			corps.append("	  LEX_CONTEXTE contexte;\n");
			corps.append("	  contexte = new LEX_CONTEXTE(cu);\n");
			corps.append("	  scanner = new LEX_" + table.getNom()
					+ "(problemReporter, contexte, " + options.getK() + ");\n");
			corps.append("	  scanner.analyseur.fromContext(contexte);\n");
			if (!table.syntaxOnly()) {
				corps.append("	  att_scanner = scanner;\n");
			}
			corps.append("	  axiome = new S_" + axiome + "_"
					+ table.getNom() + "(scanner,att_eval);\n");

			if (!table.syntaxOnly()) {
				for (Enumeration<ATTRIBUT> atts = ax.getAttributs().elements(); atts
						.hasMoreElements();) {
					ATTRIBUT a = (ATTRIBUT) atts.nextElement();
					if (a.getSorte() == ATTRIBUT.HER) {
						corps.append("	  axiome.att_" + a.getNom()
								+ " = this.att_" + a.getNom() + " ;\n");
					}
				}
			}

			corps.append("	  axiome.analyser() ;\n");

			if (!table.syntaxOnly()) {
				for (Enumeration<ATTRIBUT> atts = ax.getAttributs().elements(); atts
						.hasMoreElements();) {
					ATTRIBUT a = (ATTRIBUT) atts.nextElement();

					if (a.getSorte() == ATTRIBUT.SYN) {
						corps.append("	  this.att_" + a.getNom()
								+ " = axiome.att_" + a.getNom() + " ;\n");
					}
				}
			}

			corps.append("	  scanner.accepter_fds() ;\n");
			corps.append("	}catch (EGGException e){\n");
			corps.append("	  problemReporter.handle(e.getCategory(), e.getCode(),0, NLS.bind(e.getId(),e.getArgs()),ProblemSeverities.Error,0,0,e.getArgs());\n");
			corps.append("	}\n");
			corps.append("	}\n");
		}
	}

	public ModJava(TDS t) {
		table = t;
		nom = t.getNom();
		atts = new HashMap<String, String>();
	}

	public void finaliser(String pack, Vector<String> incs) throws EGGException {
		// le fichier
		setCorps(pack, incs);

		if (!table.syntaxOnly()) {
			// les attributs de l'axiome
			Vector<ATTRIBUT> attributs = table.getAxiome().getAttributs();
			for (Enumeration<ATTRIBUT> e = attributs.elements(); e
					.hasMoreElements();) {
				ATTRIBUT a = (ATTRIBUT) e.nextElement();
				setAtt(a);
			}
		}

		for (Iterator<String> e = atts.values().iterator(); e.hasNext();) {
			corps.append(e.next());
		}

		corps.append("  }\n");

		// ecriture
		table.getCompilationUnit().createFile(getNom() + ".java",
				corps.toString());
	}
}
