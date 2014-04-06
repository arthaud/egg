package mg.egg.eggc.compiler.libegg.base;

import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.Resolveur;

public class TDS_ACTION extends Vector<ENTREE> {

	private static final long serialVersionUID = 1L;

	protected boolean unreachable;

	private Resolveur resolveur;

	public Resolveur getResolveur() {
		return resolveur;
	}

	/**
	 * La m&egrave;re sup&eacute;rieure
	 */
	protected TDS_ACTION mere;

	/**
	 * Construit une nouvelle table des symb&ocirc;les.
	 */
	public TDS_ACTION(TDS_ACTION m) {
		super(3, 3);
		mere = m;

		if (mere != null)
			resolveur = mere.getResolveur();

		unreachable = false;
	}

	public TDS_ACTION(Resolveur r) {
		super(3, 3);
		mere = null;
		resolveur = r;
		unreachable = false;
	}

	public String verifier_initialisations() {
		StringBuffer atts = new StringBuffer();

		for (Enumeration<ENTREE> e = elements(); e.hasMoreElements();) {
			ENTREE en = (ENTREE) e.nextElement();
			if (!en.getEtat()) {
				atts.append(en.getNom() + " ");
			}
		}

		if (atts.length() != 0) {
			return atts.toString();
		}

		return null;
	}

	/**
	 * Recherche le symbole de nom nom.
	 *
	 * @param nom le nom du symbole
	 * @return le symbole, null sinon
	 */
	public ENTREE chercher(String nom) {
		ENTREE res = chercher_loc(nom);

		if (res == null && mere != null) {
			return mere.chercher(nom);
		} else {
			return res;
	    }
	}

	/**
	 * Recherche localement l'entr&eacute;e de nom nom.
	 *
	 * @param nom le nom de l'entr&eacute;e
	 * @return l'entr&eacute;e de nom nom, null sinon
	 */
	public ENTREE chercher_loc(String nom) {
		for (Enumeration<ENTREE> e = elements(); e.hasMoreElements();) {
			ENTREE en = (ENTREE) e.nextElement();
			if (en.getNom().equals(nom)) {
				return en;
			}
		}

		return null;
	}

	public GLOB ajouter_globale(GLOB v) {
		addElement(v);
		return v;
	}

	public VAR ajouter_locale(VAR v) {
		addElement(v);
		return v;
	}

	public ENTREE ajouter_entree(ENTREE v) {
		addElement(v);
		return v;
	}

	public VAR ajouter_locale(String nom, String type) {
		return ajouter_locale(new VAR(nom, resolveur.getType(type)));
	}

	public UN_ATTRIBUT ajouter_attribut(UN_ATTRIBUT a) {
		resolveur.contDecl(a);
		addElement(a);
		return a;
	}

	public UN_ATTRIBUT ajouter_attribut(String nom, String type, int sorte,
			int pos) {
		return ajouter_attribut(new UN_ATTRIBUT(nom, resolveur.getType(type),
				sorte, pos));
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		sb.append("TDS_ACTION :\n");

		for (Enumeration e = elements(); e.hasMoreElements();)
			sb.append("\t" + e.nextElement() + "\n");

		return sb.toString();
	}

	public void set_unreachable() {
		unreachable = true;
	}

	public boolean is_unreachable() {
		return unreachable;
	}

}
