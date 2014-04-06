package mg.egg.eggc.compiler.libegg.latex;

import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.compiler.libegg.base.TERMINAL;
import mg.egg.eggc.runtime.libjava.EGGException;

public class TLatex {

	private TERMINAL terminal;

	public String getNom() {
		return terminal.getNom();
	}

	private StringBuffer entete;

	public StringBuffer getEntete() {
		return entete;
	}

	public void setEntete() {
		entete = new StringBuffer();
		String comm = terminal.getComm();
		if (comm != null)
			entete.append(terminal.getComm() + "\\\\\n\\\\\n");

		// on remplace les caractères spéciaux pour LaTeX
		String exp_reg = terminal.get_expreg();
		exp_reg = exp_reg.replace("\\", "\\textbackslash ");
		exp_reg = exp_reg.replace("_", "\\_");
		exp_reg = exp_reg.replace("^", "\\^{ }");
		exp_reg = exp_reg.replace("{", "\\{");
		exp_reg = exp_reg.replace("}", "\\}");
		exp_reg = exp_reg.replace("$", "\\$");
		String nom = terminal.getNom();
		nom = nom.replace("_", "\\_");

		switch (terminal.getType()) {
			case TERMINAL.SPACE:
				entete.append("\\eggspace{" + nom + "}{\"" + exp_reg + "\"}");
				//			entete.append("space " + terminal.getNom() + " is \\verb!\""
				//					+ terminal.get_expreg() + "\"!;\n");
				break;
			case TERMINAL.SUGAR:
				entete.append("\\eggsugar{" + nom + "}{\"" + exp_reg + "\"}");
				//			entete.append("sugar " + terminal.getNom() + " is \\verb!\""
				//					+ terminal.get_expreg() + "\"!;\n");
				break;
			case TERMINAL.TERM:
				entete.append("\\eggterm{" + nom + "}{\"" + exp_reg + "\"}");
				//			entete.append("term " + terminal.getNom() + " is \\verb!\""
				//					+ terminal.get_expreg() + "\"!;\n");
				break;
			case TERMINAL.MACRO:
				entete.append("\\eggmacro{" + nom + "}{\"" + exp_reg + "\"}");
				//			entete.append("macro " + terminal.getNom() + " is \\verb!\""
				//					+ terminal.get_expreg() + "\"!;\n");
				break;
			case TERMINAL.COMM:
				entete.append("\\eggcom{" + nom + "}{\"" + exp_reg + "\"}");
				//			entete.append("comment " + terminal.getNom() + " is \\verb!\""
				//					+ terminal.get_expreg() + "\"!;\n");
				break;
		}
		entete.append("\n");
	}

	public TLatex(TERMINAL t, TDS tbl) {
		terminal = t;
	}

	public String finaliser() throws EGGException {
		// le fichier
		return entete.toString();
	}
}
