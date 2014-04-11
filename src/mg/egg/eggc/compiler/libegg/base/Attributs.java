package mg.egg.eggc.compiler.libegg.base;

import java.util.Enumeration;
import java.util.Vector;

public class Attributs extends Vector<ATTRIBUT> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit une liste d'attributs.
	 *
	 * @param n
	 *	        la taille et l'incrément du tableau dynamique
	 */
	public Attributs(int n) {
		super(n, n);
	}

	public Attributs() {
		super();
	}

	/**
	 * Recherche un attribut de nom nom.
	 *
	 * @param nom
	 *	        le nom de l'attribut à rechercher
	 * @return l'attribut de nom nom, null sinon
	 */
	public ATTRIBUT attribut(String nom) {
		ATTRIBUT a;
		for (Enumeration<ATTRIBUT> e = elements(); e.hasMoreElements();) {
			a = (ATTRIBUT) e.nextElement();

			if (a.getNom().equals(nom)) {
				return a;
			}
		}

		return null;
	}

	/**
	 * Insère un attribut a dans la liste. si aucun attribut de
	 * même nom n'existe déjà
	 *
	 * @return 1 si un ajout a ete effectue, 0 sinon
	 */
	public boolean ajouter_att(ATTRIBUT a) {
		ATTRIBUT aux = attribut(a.getNom());

		if (aux == null) {
			addElement(a);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Renvoie une chaîne décrivant les attributs.
	 *
	 * @return une chaîne décrivant les attributs
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);

		for (Enumeration<ATTRIBUT> e = elements(); e.hasMoreElements();)
			sb.append(e.nextElement());

		return sb.toString();
	}

}
