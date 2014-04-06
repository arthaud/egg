package mg.egg.eggc.runtime.libjava.lex;

public class UL {

	/**
	 * La cha&icirc;ne associ&eacute;e &agrave; la fen&ecirc;tre.
	 */
	public String nom;

	public String getNom() {
	    return nom;
	}

	/**
	 * Le code de la fen&egrave;tre.
	 */
	public int code;

	/**
	 * La ligne de la fen&ecirc;tre.
	 */
	public int ligne;

	/**
	 * La colonne de la fen&ecirc;tre.
	 */
	public int colonne;

	/**
	 * Retourne une cha&icirc;ne repr&eacute;sentant la fen&ecirc;tre courante.
	 *
	 * @return une cha&icirc;ne repr&eacute;sentant la fene&ecirc;tre courante
	 */
	public String toString() {
	    return "ul: >" + nom + "<, (" + code + ")";
	}

	/**
	 * Construit une nouvelle fen&egrave;tre.
	 *
	 * @param code
	 *            le code de la fen&ecirc;tre
	 * @param nom
	 *            le nom de la fen&ecirc;tre
	 */
	public UL(int code, String nom) {
	    this(code, nom, 0, 0);
	}

	/**
	 * Construit une nouvelle fen&egrave;tre.
	 *
	 * @param code
	 *            le code de la fen&ecirc;tre
	 * @param nom
	 *            le nom de la fen&ecirc;tre
	 * @param ligne
	 *            la ligne de la fen&ecirc;tre
	 * @param colonne
	 *            la colonne de la fen&ecirc;tre
	 */
	public UL(int code, String nom, int ligne, int colonne) {
	    this.code = code;
	    this.nom = nom;
	    this.ligne = ligne;
	    this.colonne = colonne;
	}
}
