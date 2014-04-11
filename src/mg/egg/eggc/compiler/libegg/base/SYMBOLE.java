package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Classe décrivant un symbôle.
 *
 * @see NON_TERMINAL
 * @see TERMINAL
 * @see ACT
 * @version 1.0
 * @author Gilles
 */
public class SYMBOLE implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * Symboles virtuels
	 */

	/**
	 * Le symbôle lambda.
	 */
	public final static int LAMBDA = 0;

	/**
	 * La fin de fichier.
	 */
	public final static int EOF = -1;

	/**
	 * N'importe quel symbôle.
	 */
	public final static int ALL = -2;

	private String table;

	public String getTable() {
		return table;
	}

	public void setTable(String tds) {
		table = tds;
	}

	/**
	 * Le numéro du symbôle.
	 */
	protected int numero;

	public int getNumero() {
		return numero;
	}

	protected String nom;

	public String getNom() {
		return nom;
	}

	protected String comm;

	public String getComm() {
		return comm;
	}

	/**
	 * Le numéro du symbôle suivant.
	 */
	private static int compteur = 1;

	/**
	 * La liste des attributs.
	 */
	protected Vector<ATTRIBUT> attributs;

	public Vector<ATTRIBUT> getAttributs() {
		return attributs;
	}

	public void add_attribut(ATTRIBUT a) {
		attributs.add(a);
		a.ajouter_ident(nom);
	}

	public ATTRIBUT attribut(String n) {
		for (Enumeration<ATTRIBUT> e = attributs.elements(); e
				.hasMoreElements();) {
			ATTRIBUT a = e.nextElement();
			if (n.equals(a.getNom()))
				return a;
		}
		return null;
	}

	/**
	 * Construit un nouveau SYMBOLE de nom nom
	 *
	 * @param nom le nom du symbôle
	 */
	public SYMBOLE(String nom) {
		this(nom, "");
	}

	/**
	 * Construit un nouveau SYMBOLE de nom nom
	 *
	 * @param nom le nom du symbôle
	 */
	public SYMBOLE(String nom, String comm) {
		this.nom = nom;
		this.comm = comm;
		numero = compteur++;
		attributs = new Vector<ATTRIBUT>(3, 3);
		table = null;
	}

	/**
	 * Construit un nouveau symbôle de numéro numero
	 *
	 * @param numero le numéro du symbôle
	 */
	public SYMBOLE(int numero) {
		this(numero, "");
	}

	/**
	 * Construit un nouveau symbôle de numéro numero
	 *
	 * @param numero le numéro du symbôle
	 */
	public SYMBOLE(int numero, String comm) {
		this.numero = numero;
		nom = null;
		this.comm = comm;
		attributs = new Vector<ATTRIBUT>(3, 3);
		table = null;
	}

	/**
	 * Teste l'égalité des noms et des numéros.
	 *
	 * @param o l'objet à tester
	 */
	public boolean equals(Object o) {
		if (o instanceof SYMBOLE) {
			return nom.equals(((SYMBOLE) o).getNom())
					&& numero == ((SYMBOLE) o).numero;
		} else {
			return this == o;
		}
	}

	/**
	 * Renvoie une chaîne décrivant les attributs du symbôle
	 * courant.
	 *
	 * @return la chaîne décrivant les attributs du symbôle
	 */
	public String les_attributs() {
		if (attributs == null)
			return "";

		StringBuffer sb = new StringBuffer(100);

		for (Enumeration<ATTRIBUT> e = attributs.elements(); e.hasMoreElements();) {
			ATTRIBUT a = e.nextElement();
			sb.append("\t" + a.getNom() + "\t:\t" + a.getType() + "\n");
		}

		return sb.toString();
	}

	public String toString() {
		return "SYMBOLE #" + numero + " (" + nom + ")" + les_attributs();
	}

}
