package mg.egg.eggc.compiler.libegg.base;

import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

public class NON_TERMINAL extends SYMBOLE {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<REGLE> regles;

	public Vector<REGLE> getRegles() {
		return regles;
	}

	private Arbre k_premiers;

	public Arbre getK_premiers() {
		return k_premiers;
	}

	/**
	 * Si on connait deja les premiers ...
	 * 
	 * @param p
	 *            l'ancienne valeur de s premiers
	 */
	public void setK_premiers(Arbre p) {
		k_premiers = p;
	}

	transient private Arbre k_suivants;

	public Arbre getK_suivants() {
		return k_suivants;
	}

	public void setK_suivants(Arbre s) {
		k_suivants = s;
	}

	transient private int deja_fait;

	transient private boolean[] en_cours;

	public REGLE getRegle(int numero) {
		for (Enumeration<REGLE> e = elements(); e.hasMoreElements();) {
			REGLE r = e.nextElement();
			if (r.getNumero() == numero)
				return r;
		}
		return null;
	}

	private boolean externe;

	public boolean estExterne() {
		return externe;
	}

	public void setExterne() {
		externe = true;
	}

	/**
	 * Construit un nouveau non-terminal.
	 * 
	 * @param nom
	 *            le nom du non-terminal
	 */
	public NON_TERMINAL(String n) {
		super(n);
		regles = new Vector<REGLE>(3, 3);
		k_premiers = null;
		k_suivants = null;
		deja_fait = 0;
		externe = false;
	}

	public NON_TERMINAL(String n, String comm) {
		super(n, comm);
		regles = new Vector<REGLE>(3, 3);
		k_premiers = null;
		k_suivants = null;
		deja_fait = 0;
		externe = false;
	}

	/**
	 * Construit un nouveau non-terminal.
	 * 
	 * @param nom
	 *            le nom du non-terminal
	 */
	public NON_TERMINAL(boolean e, String n, String comm) {
		super(n, comm);
		regles = new Vector<REGLE>(3, 3);
		k_suivants = null;
		deja_fait = 0;
		externe = e;
		k_premiers = null;
	}

	/**
	 * Ajoute une r&egrave;gle de production.
	 * 
	 * @param regle
	 *            la r&egrave;gle de production
	 */
	public void add_regle(REGLE regle) {
		regles.addElement(regle);
	}

	/**
	 * Retourne la liste des r&egrave;gles de production. return la liste des
	 * r&egrave;gles de production
	 */
	public Enumeration<REGLE> elements() {
		return regles.elements();
	}

	/**
	 * Calcule les k_premiers.
	 * 
	 * @param n
	 *            la hauteur maximale de l'arbre
	 * @param k
	 *            l'ordre du calcul
	 * @return les k_premiers
	 */
	public Arbre calcule_les_premiers(int n, int k) throws EGGException {
		if (k_premiers == null) {
			k_premiers = new Arbre(n);
			en_cours = new boolean[n];
			for (int i = 0; i < n; i++)
				en_cours[i] = false;
		}
		if (k <= deja_fait) {
			return k_premiers;
		}
		if (en_cours[k - 1]) {
			// rec gauche
			 throw new EGGException(IProblem.Syntax,
			 ICoreMessages.id_EGG_left_recursion,
			 CoreMessages.EGG_left_recursion, nom);
//			throw new EGGException(IEGGErrors.left_recursion, nom);
		}
		en_cours[k - 1] = true;

		Arbre temp = new Arbre(n);
		for (Enumeration<REGLE> e = elements(); e.hasMoreElements();) {
			REGLE r = e.nextElement();
			Arbre aux = r.calcule_les_premiers(n, k);
			temp.ajouter(aux);
		}
		k_premiers = temp;
		if (k > deja_fait)
			deja_fait = k;
		return k_premiers;
	}

	/**
	 * Calcule les k_suivants.
	 * 
	 * @param n
	 *            la hauteur maximale de l'arbre
	 * @param k
	 *            l'ordre du calcul
	 */
	public void calcule_les_suivants(int n, int k) throws EGGException {
		if (k_suivants == null)
			k_suivants = new Arbre(n);
		for (Enumeration<REGLE> e = elements(); e.hasMoreElements();) {
			REGLE r = e.nextElement();
			r.calcule_les_suivants(n, k);
		}
	}

	/**
	 * Remplace les non-terminaux par leurs k_suivants. return true si des
	 * ajouts ont &eacute;t&eacute; effectu&eacute;s
	 */
	public boolean remplace_non_terminaux() {
		return k_suivants.remplacer_non_terminaux();
	}

	/**
	 * Retourne une cha&icirc;ne repr&eacute;sentant le non-terminal courant.
	 * 
	 * @return une cha&icirc;ne repr&eacute;sentant le non-terminal courant
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		for (Enumeration<REGLE> e = regles.elements(); e.hasMoreElements();) {
			sb.append(e.nextElement());
		}
		return "Symbole #" + numero + " : " + nom + "(Non Terminal )\n"
		// + sb.toString() + les_attributs();
				+ getK_premiers().toString();
	}

	/**
	 * Calcule les symboles directeurs.
	 */
	public void calcule_symboles_directeurs() {
		for (Enumeration<REGLE> e = elements(); e.hasMoreElements();) {
			e.nextElement().calcule_symboles_directeurs();
		}
	}

	/**
	 * D&eacute;tecte les conflits.
	 * 
	 * @param k
	 *            l'ordre des conflits
	 */
	public void detecterConflits(int k) throws EGGException {
		k_premiers = new Arbre(k);
		for (Enumeration<REGLE> e = elements(); e.hasMoreElements();) {
			REGLE r = e.nextElement();
			r.getK_premiers().ajouterRegle(r.getNumero());
			k_premiers.ajouterI(r.getK_premiers());
		}
		k_premiers.detecterConflits(elements());
	}

	public boolean contient_EOF() {
		return k_premiers.contient_EOF();
	}

	public void afficher_premiers_regles() {
		for (Enumeration<REGLE> e = elements(); e.hasMoreElements();) {
			REGLE r = e.nextElement();
			// System.err.println(r);
			System.err.println(r.getK_premiers().getDebut().getValeur());
			System.err.println("//\\\\//\\\\//\\\\//\\\\//\\\\");
		}
	}

	// INC_COMP
	transient private boolean nomChange = false;

	public void setNomChange(boolean b) {
		nomChange = b;
	}

	public boolean getNomChange() {
		return nomChange;
	}

	transient private boolean commChange = false;

	public boolean getCommChange() {
		return commChange;
	}

	public void setCommChange(boolean b) {
		commChange = b;
	}

	public void setAllChange(boolean b) {
		nomChange = b;
		commChange = b;
	}

	/**
	 * le terminal a-t-il change depuis la derniere compil ?
	 * 
	 * @param old
	 */
	public void compare(NON_TERMINAL old) {
		nomChange = !nom.equals(old.nom);
		if (comm != null)
			commChange = !comm.equals(old.comm);
		// System.err.println("compare Non Terminal " + getNom() + " avec " +
		// old.getNom() + " : " + (nomChange || commChange));
	}
}
