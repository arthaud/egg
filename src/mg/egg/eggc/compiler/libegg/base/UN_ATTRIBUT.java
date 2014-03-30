package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.IType;

public class UN_ATTRIBUT extends ENTREE {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sorte;

	public int getSorte() {
		return sorte;
	}

	private int pos;

	public int getPos() {
		return pos;
	}

	public UN_ATTRIBUT(String n, IType t, int s, int p) {
		super(n, t);
		sorte = s;
		pos = p;
	}

	public String toString() {
		return "UN ATTRIBUT "
				+ nom
				+ " : "
				+ type.getNom()
				+ " < "
				+ ((sorte == ATTRIBUT.HER) ? "Herite"
						: ((sorte == ATTRIBUT.SYN) ? "Synthetise" : "incorrect"))
				+ " > ( " + ((etat) ? "initialise" : "non_initialise") + " )\n";
	}

	// utilisable si : acces autorisé en lecture
	public boolean utilisable(int p) {
		// System.err.println(nom + " UTILISABLE action en " + p + " attribut en
		// " + pos);
		if (pos == 0) {
			// attribut du symbole de gauche
			// herite : oui
			// synthetise : non
			return (sorte == ATTRIBUT.HER);
		} else {
			// attribut d'un symbole de droite
			// herite : non
			// synthetise : oui si p > pos
			if (sorte == ATTRIBUT.HER)
				return false;
			else
				return (p > pos);
			// return !(sorte == ATTRIBUT.HER) || ((sorte == ATTRIBUT.SYN) && p
			// > pos);
		}
	}

	// affectable si : acces autorisé en ecriture
	public boolean affectable(int p) {
		// System.err.println(nom + " AFFECTABLE action en " + p + " attribut en
		// " + pos);
		if (pos == 0) {
			// attribut du symbole de gauche
			// herite : non
			// synthetise : oui
			return (sorte == ATTRIBUT.SYN);
		} else {
			// attribut d'un symbole de droite
			// herite : oui si p < pos
			// synthetise :non
			if (sorte == ATTRIBUT.SYN)
				return false;
			else
				return (p < pos);
			// return !(sorte == ATTRIBUT.SYN) || ((sorte == ATTRIBUT.HER) && p
			// < pos);
		}
	}

}
