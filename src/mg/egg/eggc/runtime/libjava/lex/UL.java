package mg.egg.eggc.runtime.libjava.lex;

public class UL {

	/**
	 * La chaîne associée à la fenêtre.
	 */
	public String nom;

	public String getNom() {
	    return nom;
	}

	/**
	 * Le code de la fenètre.
	 */
	public int code;

	/**
	 * La ligne de la fenêtre.
	 */
	public int ligne;

	/**
	 * La colonne de la fenêtre.
	 */
	public int colonne;

	/**
	 * Retourne une chaîne représentant la fenêtre courante.
	 *
	 * @return une chaîne représentant la feneêtre courante
	 */
	public String toString() {
	    return "ul: >" + nom + "<, (" + code + ")";
	}

	/**
	 * Construit une nouvelle fenètre.
	 *
	 * @param code
	 *            le code de la fenêtre
	 * @param nom
	 *            le nom de la fenêtre
	 */
	public UL(int code, String nom) {
	    this(code, nom, 0, 0);
	}

	/**
	 * Construit une nouvelle fenètre.
	 *
	 * @param code
	 *            le code de la fenêtre
	 * @param nom
	 *            le nom de la fenêtre
	 * @param ligne
	 *            la ligne de la fenêtre
	 * @param colonne
	 *            la colonne de la fenêtre
	 */
	public UL(int code, String nom, int ligne, int colonne) {
	    this.code = code;
	    this.nom = nom;
	    this.ligne = ligne;
	    this.colonne = colonne;
	}
}
