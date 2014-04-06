package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.IType;

public class UN_ATTRIBUT extends ENTREE {
	private static final long serialVersionUID = 1L;

	private int sorte;

	private int pos;

	public int getSorte() {
		return sorte;
	}


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

	// utilisable si : accès autorisé en lecture
	public boolean utilisable(int p) {
		if (pos == 0) {
			// attribut du symbole de gauche
			// hérité : oui
			// synthétisé : non
			return (sorte == ATTRIBUT.HER);
		} else {
			// attribut d'un symbole de droite
			// hérité : non
			// synthétisé : oui si p > pos
			if (sorte == ATTRIBUT.HER)
				return false;
			else
				return (p > pos);
		}
	}

	// affectable si : accès autorisé en ecriture
	public boolean affectable(int p) {
		if (pos == 0) {
			// attribut du symbole de gauche
			// hérité : non
			// synthétisé : oui
			return (sorte == ATTRIBUT.SYN);
		} else {
			// attribut d'un symbole de droite
			// hérité : oui si p < pos
			// synthétisé :non
			if (sorte == ATTRIBUT.SYN)
				return false;
			else
				return (p < pos);
		}
	}

}
