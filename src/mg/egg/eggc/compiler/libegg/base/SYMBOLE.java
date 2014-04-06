package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Classe d&eacute;crivant un symb&ocirc;le.
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
	 * Le symb&ocirc;le lambda.
	 */
	public final static int LAMBDA = 0;

	/**
	 * La fin de fichier.
	 */
	public final static int EOF = -1;

	/**
	 * N'import quel symb&ocirc;le.
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
	 * Le num&eacute;ro du symb&ocirc;le.
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
	 * Le num&eacute;ro du symb&ocirc;le suivant.
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
	 * @param nom le nom du symb&ocirc;le
	 */
	public SYMBOLE(String nom) {
		this(nom, "");
	}

	/**
	 * Construit un nouveau SYMBOLE de nom nom
	 *
	 * @param nom le nom du symb&ocirc;le
	 */
	public SYMBOLE(String nom, String comm) {
		this.nom = nom;
		this.comm = comm;
		numero = compteur++;
		attributs = new Vector<ATTRIBUT>(3, 3);
		table = null;
	}

	/**
	 * Construit un nouveau symb&ocirc;le de num&eacute;ro numero
	 *
	 * @param numero le num&eacute;ro du symb&ocirc;le
	 */
	public SYMBOLE(int numero) {
		this(numero, "");
	}

	/**
	 * Construit un nouveau symb&ocirc;le de num&eacute;ro numero
	 *
	 * @param numero le num&eacute;ro du symb&ocirc;le
	 */
	public SYMBOLE(int numero, String comm) {
		this.numero = numero;
		nom = null;
		this.comm = comm;
		attributs = new Vector<ATTRIBUT>(3, 3);
		table = null;
	}

	/**
	 * Teste l'&eacute;galit&eacute; des noms et des num&eacute;ros.
	 *
	 * @param o l'objet &agrave; tester
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
	 * Renvoie une cha&icirc;ne d&eacute;crivant les attributs du symb&ocirc;le
	 * courant.
	 *
	 * @return la cha&icirc;ne d&eacute;crivant les attributs du symb&ocirc;le
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
