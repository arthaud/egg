package mg.egg.eggc.compiler.libegg.latex;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import mg.egg.eggc.runtime.libjava.Utils;

public class VisiteurEggLatex implements IVisiteurEgg {
	// un seul visiteur d'action par visiteurEgg
	private IVisiteurAction vact;

	public IVisiteurAction getVisAction() {
		return vact;
	}

	// les attributs déjà engendrés
	private Vector<AttLatex> atts;

	// les terminaux déjà engendrés
	private Vector<TLatex> terms;

	public TLatex getTerm(String n) {
		for (Enumeration e = terms.elements(); e.hasMoreElements();) {
			TLatex tj = (TLatex) e.nextElement();
			if (tj.getNom().equals(n))
				return tj;
		}
		return null;
	}

	// les règles déjà engendrées
	private Vector<RLatex> regles;

	public RLatex getRegle(int n) {
		// rajouter le test de débordement
		if (n < regles.size())
			return regles.elementAt(n);
		else
			return null;
	}

	// la grammaire
	private TDS table;

	public VisiteurEggLatex(TDS t) {
		vact = new VisiteurActionLatex();
		table = t;
		atts = new Vector<AttLatex>();
		terms = new Vector<TLatex>();
		regles = new Vector<RLatex>();
	}

	// génère main() du compilo
	public void racinec() {
	}

	// génère module
	public void racine() {
	}

	// génère l'analyseur lexical
	public void lexical() {
	}

	// appele à la declaration du non terminal externe
	public void ex_entete(NON_TERMINAL nt) {
	}

	// génère l'entête d'un non terminal
	// appelé à la creation du non terminal
	public void nt_entete(NON_TERMINAL nt) {
	}

	// appelé à chaque création de règle
	public void regle(REGLE r) {
		RLatex c = new RLatex(r);
		regles.add(c);
	}

	// appelé à chaque création de règle
	public void nt_regle(REGLE r) {
		RLatex c = getRegle(r.getNumero());
		c.setEntete();
	}

	// génère l'analyse syntaxique d'un terminal ( = accepter)
	// appelé à la création du terminal
	public void t_entete(TERMINAL t) {
		TLatex c = new TLatex(t, table);
		terms.addElement(c);
		c.setEntete();
	}

	// génère le code d'un attribut semantique
	public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a) {
		// AttLatex c = getAtt(a.getNom());
		// atts.addElement(c);
		// c.setEntete();
	}

	// génère le code d'un attribut semantique
	public void nt_attribut(ATTRIBUT a) {
		AttLatex c = new AttLatex(a);
		atts.addElement(c);
		c.setEntete();
	}

	public void globale(REGLE r, GLOB g) {
		RLatex c = getRegle(r.getNumero());
		if (c == null)
			c = new RLatex(r);
		c.setGlob(g);
	}

	// génère le code d'une var globale attribut semantique
	public void nt_globale(NON_TERMINAL nt, GLOB g) {
		// NtLatex c = getNterm(nt.getNom());
		// c.setGlob(g);
	}

	public void t_attribut(TERMINAL t, ATTRIBUT a) {
	}

	// génère le code d'une action semantique
	// appelé à la création de l'action
	// nom (n), regle(r), code (c)
	public void nt_action(ActREGLE a) {
		REGLE r = a.getRegle();
		RLatex c = getRegle(r.getNumero());
		if (c == null)
			c = new RLatex(r);
		c.setAct(a);
	}

	public String car(String s) {
		return s;
	}

	public void finaliser() throws EGGException {
		StringBuffer sb = new StringBuffer();
		sb.append("%------------------ ***** -----------------------------\n");
		sb.append("%-- génération automatique de la grammaire de "
				+ table.getNom() + "\n");
		sb.append("%-- au format latex\n");
		sb.append("%------------------ ***** -----------------------------\n");
		sb.append("\\documentclass[12pt, a4paper]{article}\n");
		sb.append("\\usepackage{fancyhdr}\n");
		sb.append("\\usepackage{egg}\n");
		sb.append("\\lhead{}\n");
		sb.append("\\chead{Grammaire de " + table.getNom() + "}\n");
		sb.append("\\rhead{}\n");
		sb.append("\\lfoot{}\n");
		sb.append("\\cfoot{}\n");
		sb.append("\\rfoot{\\hrule \\thepage}\n");
		sb.append("\\pagestyle{fancy}\n");
		sb.append("\\title{Grammaire de " + table.getNom() + " }\n");
		sb.append("\\date{\\today}\n");
		sb.append("\\begin{document}\n");
		sb.append("\n");

		// les attributs
		if (atts.size() > 0) {
			sb.append("\\section*{Attributs}\n");
			sb.append("\\begin{egg}\n");
			for (AttLatex a : atts) {
				sb.append(a.finaliser());
			}
			sb.append("\\end{egg}\n");
		}
		sb.append("\n");

		// les terminaux
		if (terms.size() > 0) {
			sb.append("\\section*{Terminaux}\n");
			sb.append("\\begin{egg}\n");
			for (TLatex a : terms) {
				sb.append(a.finaliser());
			}
			sb.append("\\end{egg}\n");
		}
		sb.append("\n");

		// les règles
		if (regles.size() > 0) {
			sb.append("\\section*{R\\`egles de production}\n");
			sb.append("\\begin{egg}\n");
			for (RLatex a : regles) {
				sb.append(a.finaliser());
			}
			sb.append("\\end{egg}\n");
		}
		sb.append("\n");

		sb.append("\\end{document}\n");

		sb.append("% Fin du fichier auto-généré\n");

		// écriture
		IEGGCompilationUnit cu = table.getCompilationUnit();
		cu.createFile(table.getNom() + ".tex", sb.toString());

		// on copie le style dans le répertoire cible
		// repertoire de destination
		String rep = cu.getOptions().getDirectory();

		// si le style n'est pas déjà présent on le copie
		InputStream style = ClassLoader
			.getSystemResourceAsStream("latex/egg.sty");
		try {
			char[] styleContent = Utils.getInputStreamAsCharArray(style, -1, null);
			cu.createFile("egg.sty", String.copyValueOf(styleContent));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static String getOpLatex(String op) {
		return op;
	}

	protected static String getTypeLatex(String type) {
		return type;
	}

	public void m_entete(String m) {
	}

}
