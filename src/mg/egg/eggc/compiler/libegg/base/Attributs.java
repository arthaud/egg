package mg.egg.eggc.compiler.libegg.base;

import java.util.Enumeration;
import java.util.Vector;

public class Attributs extends Vector<ATTRIBUT> {

	/**
	 * La liste des attributs.
	 */
	// protected Vector table ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit une liste d'attributs.
	 * 
	 * @param n
	 *            la taille et l'incr&eacute;ment du tableau dynamique
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
	 *            le nom de l'attribut &agrave; rechercher
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
	 * Ins&egrave;re un attribut a dans la liste. si aucun attribut de
	 * m&ecirc;me nom n'existe d&eacute;j&agrave;
	 * 
	 * @return 1 si un ajout a ete effectue, 0 sinon
	 */
	public boolean ajouter_att(ATTRIBUT a) {
		ATTRIBUT aux = attribut(a.getNom());
		if (aux == null) {
			addElement(a);
			// System.out.println(this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Renvoie une cha&icirc;ne d&eacute;crivant les attributs.
	 * 
	 * @return une cha&icicc;ne d&eacute;crivant les attributs
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		for (Enumeration<ATTRIBUT> e = elements(); e.hasMoreElements();)
			sb.append(e.nextElement());
		return sb.toString();
	}

}
