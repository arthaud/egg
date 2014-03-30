package mg.egg.eggc.compiler.libegg.base;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

class CLASSES {

	public CLASSES() {
		_classes = null;
		classes = new Hashtable<String, CLASSE>();
	}

	/**
	 * La liste des classes.
	 */
	private Vector<CLASSE> _classes;

	private Hashtable<String, CLASSE> classes;

	/**
	 * Retourne la classe de nom nom
	 * 
	 * @param nom
	 *            le nom de la classe &agrave; chercher
	 * @return la classe de nom nom, null si non trouv&eacute;e
	 */
	public CLASSE classe(String nom) {
		CLASSE c;
		if (nom.equals("String"))
			return classe("STRING");
		c = classes.get(nom);
		if (c != null) {
			return c;
		} else {
			/*
			 * System.err.println ( "liste des classes :" ) ; for ( Enumeration
			 * e = elements () ; e.hasMoreElements() ; ) { c =
			 * (CLASSE)e.nextElement() ; System.err.println ( "\t--> " +
			 * c.getNom() ) ; }
			 */
			return null;
		}
	}

	/**
	 * Retourne la classe de nom nom
	 * 
	 * @param nom
	 *            le nom de la classe &agrave; chercher
	 * @return la classe de nom nom, null si non trouv&eacute;e
	 */
	public CLASSE _classe(String nom) {
		if (nom.equals("String"))
			return _classe("STRING");
		for (Enumeration<CLASSE> e = _elements(); e.hasMoreElements();) {
			CLASSE c = e.nextElement();
			if (c.getNom().equals(nom)) {
				return c;
			}
		}
		System.err.println("liste des classes :");
		for (Enumeration<CLASSE> e = _elements(); e.hasMoreElements();) {
			CLASSE c = e.nextElement();
			System.err.println("\t--> " + c.getNom());
		}
		return null;
	}

	/**
	 * Renvoie la liste des classses
	 * 
	 * @return la liste des classes
	 */
	public Enumeration<CLASSE> elements() {
		return classes.elements();
	}

	/**
	 * Renvoie la liste des classses
	 * 
	 * @return la liste des classes
	 */
	public Enumeration<CLASSE> _elements() {
		return _classes.elements();
	}

	/**
	 * Ins&egrave;re la classe classe
	 * 
	 * @param classe
	 *            la classe &agrave; ins&eacute;rer
	 */
	public void inserer(CLASSE classe) {
		CLASSE c = classes.get(classe.getNom());
		if (c == null) {
			classes.put(classe.getNom(), classe);
		} else {
		}
	}

	/**
	 * Ins&egrave;re la classe classe
	 * 
	 * @param classe
	 *            la classe &agrave; ins&eacute;rer
	 */
	public void _inserer(CLASSE classe) {
		for (Enumeration<CLASSE> e = _elements(); e.hasMoreElements();) {
			CLASSE c = e.nextElement();
			if (c.getNom().equals(classe.getNom())) {
				return;
			}
		}
		_classes.addElement(classe);
	}

	/**
	 * Renvoie une repr&eacute;sentation textuelle des classes
	 * 
	 * @return une repr&eacute;sentation textuelle des classes
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Enumeration<CLASSE> e = elements(); e.hasMoreElements();) {
			sb.append(e.nextElement());
		}
		return sb.toString();
	}

	/**
	 * Renvoie une repr&eacute;sentation textuelle des classes
	 * 
	 * @return une repr&eacute;sentation textuelle des classes
	 */
	public String _toString() {
		StringBuffer sb = new StringBuffer();
		for (Enumeration<CLASSE> e = _elements(); e.hasMoreElements();) {
			sb.append(e.nextElement());
		}
		return sb.toString();
	}

}
