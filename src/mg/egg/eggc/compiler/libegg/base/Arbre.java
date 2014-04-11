package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.runtime.libjava.EGGException;

/**
 * Classe décrivant un arbre de symbôles.
 *
 * @version 1.0
 * @author Gilles
 */
public class Arbre implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * La première feuille.
	 */
	private Feuille debut;

	public Feuille getDebut() {
		return debut;
	}

	/**
	 * La hauteur maximale de l'arbre.
	 */
	private int hauteur;

	public int getHauteur() {
		return hauteur;
	}

	/**
	 * La longueur courante de l'arbre.
	 */
	private int longueur;

	public int getLongueur() {
		return longueur;
	}

	/**
	 * Construit un arbre.
	 *
	 * @param hauteur
	 *	        la hauteur maximale de l'arbre
	 */
	public Arbre(int hauteur) {
		debut = null;
		this.hauteur = hauteur;
		longueur = 0;
	}

	public Arbre(Feuille f) {
		debut = f;
		this.hauteur = f.getHauteur() + 1;
		longueur = 1;
	}

	/**
	 * Ajoute une règle à la fin de chaque branche.
	 *
	 * @param regle
	 *	        le numéro de la règle à ajouter
	 */
	public void ajouterRegle(int regle) {
		debut.ajouterRegle(regle);
	}

	public boolean contient_EOF() {
		return debut.contient_EOF();
	}

	/**
	 * Réduit l'arbre.
	 */
	public void reduire() {
		int res = -100;
		Feuille f = debut;
		while (f != null) {
			int aux = f.reduit();
			if (res == -100 || aux == res) {
				res = aux;
			} else {
				res = -1;
			}
			f = f.getFrere();
		}
		if (res > 0) {
			debut = new Feuille(new SYMBOLE(res), hauteur);
		}
	}

	/**
	 * Construit un arbre.
	 *
	 * @param hauteur
	 *	        la hauteur maximale de l'arbre.
	 * @param valeur
	 *	        le symbôle associé à la première
	 *	        feuille
	 */
	public Arbre(int hauteur, SYMBOLE valeur) {
		this(hauteur);
		debut = new Feuille(valeur, hauteur);

		if (valeur.getNumero() == SYMBOLE.EOF) {
			for (int i = 0; i < hauteur; i++) {
				Feuille f = new Feuille(new SYMBOLE(SYMBOLE.EOF), hauteur);
				f.setFils(debut);
				debut = f;
			}
		} else {
			debut.setFils(new Feuille(new SYMBOLE(SYMBOLE.LAMBDA), hauteur));
			if (debut.getValeur().getNumero() == SYMBOLE.LAMBDA)
				longueur = 0;
			else
				longueur = 1;
		}
	}

	/**
	 * Détecte les conflits.
	 */
	public void detecterConflits(Enumeration<REGLE> e) throws EGGException {
		debut.detecterConflits(e);
	}

	/**
	 * Remplace les non_terminaux par leurs k_premiers
	 *
	 * @return true si des changements ont été effectués
	 */
	public boolean remplacer_non_terminaux() {
		Feuille f = debut;
		boolean res = false;
		Vector<Arbre> v = new Vector<Arbre>();

		while (f != null) {
			if (f.getValeur() instanceof NON_TERMINAL) {
				v.addElement(((NON_TERMINAL) f.getValeur()).getK_suivants());
			} else {
				res |= f.remplacer_non_terminaux(1);
			}
			f = f.getFrere();
		}

		for (Enumeration<Arbre> e = v.elements(); e.hasMoreElements();) {
			res |= ajouter(e.nextElement());
		}

		return res;
	}

	/**
	 * Supprime les non-terminaux.
	 */
	public void supprimer_non_terminaux() {
		while ((debut != null)
				&& ((debut.getValeur() instanceof NON_TERMINAL) || debut
						.getValeur().getNumero() == SYMBOLE.LAMBDA)) {
			debut = debut.getFrere();
	    }

		if (debut == null)
			return;

		Feuille f = debut;
		Feuille g = debut.getFrere();
		f.supprimer_non_terminaux();

		while (g != null) {
			if ((g.getValeur() instanceof NON_TERMINAL)
					|| g.getValeur().getNumero() == SYMBOLE.LAMBDA) {
				f.setFrere(g.getFrere());
			} else {
				g.supprimer_non_terminaux();
				f = g;
			}
			g = f.getFrere();
		}
	}

	/**
	 * Concatène un autre arbre.
	 *
	 * @param arbre l'autre arbre
	 * @return true si des changements ont été effectués
	 */
	public boolean concatener(Arbre arbre) {
		if (arbre.debut == null)
			return false;

		if (debut == null)
			return false;

		longueur += arbre.longueur;
		boolean res = false;
		boolean ajout = false;

		if (debut.getValeur().getNumero() == SYMBOLE.LAMBDA) {
			debut = debut.getFrere();
			ajout = true;
		}

		if (debut != null) {
			Feuille avant = debut;
			Feuille courant = debut.getFrere();
			res |= debut.concatener(arbre, 1);

			while (courant != null) {
				if (courant.getValeur().getNumero() == SYMBOLE.LAMBDA) {
					ajout = true;
					avant.setFrere(courant.getFrere());
				} else {
					res |= courant.concatener(arbre, 1);
					avant = courant;
				}
				courant = avant.getFrere();
			}
		}

		if (ajout) {
			res |= ajouter(arbre);
		}

		return res;
	}

	/**
	 * Ajoute un autre arbre sans contrôle sur la hauteur
	 *
	 * @param arbre l'autre arbre
	 */
	public void ajouterI(Arbre arbre) {
		if (arbre.debut == null)
			return;

		if (debut == null) {
			debut = (Feuille) arbre.debut.clone();
		} else {
			Feuille f = arbre.debut;

			while (f != null) {
				debut.ajouterI(f);
				f = f.getFrere();
			}
		}
	}

	/**
	 * Ajoute un autre arbre.
	 *
	 * @param arbre l'autre arbre
	 * @return true si des changements ont été effectués
	 */
	public boolean ajouter(Arbre arbre) {
		if (arbre.debut == null)
			return false;
		boolean res = false;
		if (arbre.longueur < longueur)
			longueur = arbre.longueur;
		if (debut == null) {
			debut = arbre.debut.clone(1);
			longueur = arbre.longueur;
			res = (debut != null);
		} else {
			Feuille f = arbre.debut;
			while (f != null) {
				res |= debut.ajouter(f, 1);
				f = f.getFrere();
			}
		}
		return res;
	}

	/**
	 * Renvoie une chaîne représentant l'arbre.
	 *
	 * @return une chaîne représentant l'arbre
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);

		if (debut == null) {
			sb.append("vide\n");
		} else {
			sb.append(debut.aff(0));
		}
		return sb.toString();
	}

	public Object clone() {
		Arbre a = new Arbre(hauteur);
		a.longueur = longueur;
		a.debut = (Feuille) debut.clone();
		return a;
	}

}
