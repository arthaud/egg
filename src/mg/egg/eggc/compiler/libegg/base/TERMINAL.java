package mg.egg.eggc.compiler.libegg.base;

import java.util.ArrayList;
import java.util.List;

public class TERMINAL extends SYMBOLE {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> names;

	public List<String> getNames() {
		return names;
	}

	public void addName(String name) {
		int l = name.length();
		names.add(name.substring(1, l - 1));
	}

	private String expreg;

	public String get_expreg() {
		return expreg;
	}

	private int type;

	public int getType() {
		return type;
	}

	/**
	 * Un s&eacute;parateur.
	 */
	public final static int SPACE = 0;

	/**
	 * Du sucre.
	 */
	public final static int SUGAR = 1;

	/**
	 * Un terminal.
	 */
	public final static int TERM = 2;

	/**
	 * Un compilateur
	 */
	public final static int COMPIL = 3;

	/**
	 * Une macro.
	 */
	public final static int MACRO = 4;

	/**
	 * Un commentaire.
	 */
	public final static int COMM = 5;

	/**
	 * Construit un nouveau terminal.
	 * 
	 * @param type
	 *            le type
	 * @param nom
	 *            le nom
	 * @param expreg
	 *            l'expression r&eacute;guli&egrave;re EGG associ&eacute;e
	 */
	public TERMINAL(int type, String nom, String expreg) {
		super(nom);
		this.type = type;
		this.expreg = expreg;
		this.names = new ArrayList<String>();
	}

	public TERMINAL(int type, String nom, String expreg, String comm) {
		super(nom, comm);
		this.type = type;
		this.expreg = expreg;
		this.names = new ArrayList<String>();
	}

	/**
	 * Construit un nouveau terminal.
	 * 
	 * @param type
	 *            le type
	 * @param nom
	 *            le nom
	 * @param expreg
	 *            l'expression r&eacute;guli&egrave;re associ&eacute;e
	 */
	// public TERMINAL(int type, String nom, String expreg) {
	// this(type, nom, expreg, "");
	// }
	/**
	 * Retourne une cha&icirc;ne d&eacute;crivant le terminal courant.
	 * 
	 * @return une cha&icirc;ne d&eacute;crivant le terminal courant
	 */
	public String toString() {
		return "Symbole #"
				+ numero
				+ " : "
				+ nom
				+ "("
				+ ((type == SPACE) ? "Separateur"
						: ((type == SUGAR) ? "Sucre"
								: ((type == TERM) ? "Terminal"
										: ((type == COMPIL) ? "Compilateur externe"
												: ((type == MACRO) ? "macro definition"
														: ((type == COMM) ? "commentaire"
																: "Incorrect"))))))
				+ ")" + "\n" + "\texpression reguliere : " + expreg + "\n"
				+ les_attributs();
	}

	public String ecrire_def() {
		StringBuffer sb = new StringBuffer();
		switch (type) {
		case SPACE:
			sb.append("space\t");
			sb.append(nom + "\t\tis\t\"" + expreg + "\";\n");
			break;
		case SUGAR:
			sb.append("sugar\t");
			sb.append(nom + "\t\tis\t\"" + expreg + "\";\n");
			break;
		case TERM:
			sb.append("term\t");
			sb.append(nom + "\t\tis\t\"" + expreg + "\";\n");
			break;
		case MACRO:
			sb.append("macro\t");
			sb.append(nom + "\t\tis\t\"" + expreg + "\";\n");
			break;
		case COMPIL:
			sb.append("compil\t");
			sb.append(nom + ";\n");
			break;
		case COMM:
			sb.append("comment\t");
			sb.append(nom + ";\n");
			break;
		}
		return sb.toString();
	}

	/**
	 * Retourne le type du terminal courant.
	 * 
	 * @return le type du terminal courant
	 */
	public int get_type() {
		return type;
	}

	// INC_COMP
	transient private boolean nomChange = false;

	transient private boolean typeChange = false;

	transient private boolean expregChange = false;

	transient private boolean commChange = false;
	transient private boolean namesChange = false;

	public void setNomChange(boolean b) {
		nomChange = b;
	}

	public void setTypeChange(boolean b) {
		typeChange = b;
	}

	public void setExpregChange(boolean b) {
		expregChange = b;
	}

	public void setNamesChange(boolean b) {
		namesChange = b;
	}

	public void setCommChange(boolean b) {
		commChange = b;
	}

	public boolean getNomChange() {
		return nomChange;
	}

	public boolean getTypeChange() {
		return typeChange;
	}

	public boolean getExpregChange() {
		return expregChange;
	}

	public boolean getNamesChange() {
		return namesChange;
	}

	public boolean getCommChange() {
		return commChange;
	}

	public void setAllChange(boolean b) {
		nomChange = b;
		typeChange = b;
		expregChange = b;
		namesChange = b;
		commChange = b;
	}

	/**
	 * le terminal a-t-il change depuis la derniere compil ?
	 * 
	 * @param old
	 */
	public void compare(TERMINAL old) {

		nomChange = !nom.equals(old.nom);
		typeChange = type != old.type;
		expregChange = !expreg.equals(old.expreg);
		namesChange = !names.equals(old.names);
		if (comm != null)
			commChange = !comm.equals(old.comm);
		// System.err.println("compare Terminal " + getNom() + " avec " +
		// old.getNom() + " : " +
		// (nomChange || typeChange || expregChange || commChange));
	}

}
