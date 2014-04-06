package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

public class Feuille implements Serializable {

	private static final long serialVersionUID = 1L;

	private int hauteur;

	public int getHauteur() {
		return hauteur;
	}

	private SYMBOLE valeur;

	public SYMBOLE getValeur() {
		return valeur;
	}

	private Feuille frere;

	public Feuille getFrere() {
		return frere;
	}

	public void setFrere(Feuille f) {
		frere = f;
	}

	private Feuille fils;

	public Feuille getFils() {
		return fils;
	}

	public void setFils(Feuille f) {
		fils = f;
	}

	public Feuille(SYMBOLE valeur, int hauteur) {
		this.valeur = valeur;
		this.hauteur = hauteur;
		frere = null;
		fils = null;
	}

	public boolean contient_EOF() {
		if (valeur.getNumero() == SYMBOLE.EOF) {
			return true;
		} else {
			if (fils != null) {
				if (fils.contient_EOF()) {
					return true;
				}
			}
			if (frere != null) {
				return frere.contient_EOF();
			} else {
				return false;
			}
		}
	}

	public int reduit() {
		int res = -100;
		if (fils == null) {
			if (res == -100 || res == valeur.getNumero()) {
				res = valeur.getNumero();
			} else {
				res = -1;
			}
		} else {
			Feuille f = fils;
			int aux;
			while (f != null) {
				aux = f.reduit();
				if (res == -100 || res == aux) {
					res = aux;
				} else {
					res = -1;
				}
				f = f.frere;
			}
			if (res != -1) {
				fils = new Feuille(new SYMBOLE(res), hauteur);
			}
		}
		return res;
	}

	public void detecterConflits(Enumeration<REGLE> e) throws EGGException {
		if (fils == null) {
			if (frere != null) {
				// "Conflit entre les regles " + valeur.getNumero() + " et " +
				// frere.getValeur().getNumero()
				String s1 = "", d1 = "", s2 = "", d2 = "";
				Arbre a1 = null, a2 = null;

				for (; e.hasMoreElements();) {
					REGLE r = (REGLE) e.nextElement();
					if (r.getNumero() == valeur.getNumero()) {
						s1 = r.toStringSyntax();
						d1 = r.getK_premiers().toString();
						a1 = r.getK_premiers();
					}
					if (r.getNumero() == frere.getValeur().getNumero()) {
						s2 = r.toStringSyntax();
						d2 = r.getK_premiers().toString();
						a2 = r.getK_premiers();
					}
				}

				throw new EGGException(IProblem.Syntax,
						ICoreMessages.id_EGG_conflict,
						CoreMessages.EGG_conflict, new Object[] { s1, a1, s2, a2 });
			}
		} else {
			fils.detecterConflits(e);
			if (frere != null) {
				frere.detecterConflits(e);
			}
		}
	}

	public void ajouterRegle(int regle) {
		if (fils == null) {
			fils = new Feuille(new SYMBOLE(regle), hauteur);
		} else {
			fils.ajouterRegle(regle);
		}

		if (frere != null) {
			frere.ajouterRegle(regle);
		}
	}

	public Feuille smallClone(int h) {
		if (h > hauteur)
			return null;

		Feuille f = new Feuille(valeur, hauteur);

		if (fils != null) {
			f.fils = fils.clone(h + 1);
		}
		return f;
	}

	public void supprimer_non_terminaux() {
		while ((fils != null)
				&& ((fils.getValeur() instanceof NON_TERMINAL) || fils
						.getValeur().getNumero() == SYMBOLE.LAMBDA)) {
			fils = fils.frere;
	    }

		if (fils == null)
			return;

		Feuille g = fils;
		g.supprimer_non_terminaux();

		while (g.frere != null) {
			if ((g.frere.getValeur() instanceof NON_TERMINAL)
					|| g.frere.getValeur().getNumero() == SYMBOLE.LAMBDA) {
				g.frere = g.frere.frere;
			} else {
				g.frere.supprimer_non_terminaux();
				g = g.frere;
			}
		}
	}

	public boolean ajouter(Arbre arbre, int h) {
		if (h > hauteur)
			return false;

		boolean res = false;

		if (fils == null) {
			if (arbre.getDebut() != null) {
				fils = arbre.getDebut().clone(h + 1);
				res = (fils != null);
			}
		} else {
			Feuille f = arbre.getDebut();
			while (f != null) {
				res |= fils.ajouter(f, h + 1);
				f = f.frere;
			}
		}
		return res;
	}

	public boolean ajouter(Feuille feuille, int h) {
		if (feuille == null || h > hauteur)
			return false;

		boolean res = false;

		if (valeur.getNumero() == feuille.getValeur().getNumero()) {
			if (feuille.fils != null) {
				if (fils == null) {
					fils = feuille.fils.clone(h + 1);
					res = (fils != null);
				} else {
					Feuille g = feuille.fils;
					while (g != null) {
						res |= fils.ajouter(g, h + 1);
						g = g.frere;
					}
				}
			}
		} else {
			if (frere != null) {
				res |= frere.ajouter(feuille, h);
			} else {
				frere = feuille.smallClone(h);
				res = (frere != null);
			}
		}
		return res;
	}

	public void ajouterI(Feuille feuille) {
		if (feuille == null)
			return;

		if (valeur.getNumero() == feuille.getValeur().getNumero()) {
			if (feuille.fils != null) {
				if (fils == null) {
					fils = (Feuille) feuille.fils.clone();
				} else {
					Feuille g = feuille.fils;
					while (g != null) {
						fils.ajouterI(g);
						g = g.frere;
					}
				}
			}
		} else {
			if (frere != null) {
				frere.ajouterI(feuille);
			} else {
				frere = feuille.smallClone();
			}
		}
	}

	public Object clone() {
		Feuille f = new Feuille(valeur, hauteur);
		if (frere != null) {
			f.frere = (Feuille) frere.clone();
		}
		if (fils != null) {
			f.fils = (Feuille) fils.clone();
		}
		return f;
	}

	public Feuille smallClone() {
		Feuille f = new Feuille(valeur, hauteur);
		if (fils != null) {
			f.fils = (Feuille) fils.clone();
		}
		return f;
	}

	protected Feuille clone(int h) {
		if (h > hauteur) {
			return null;
		}

		Feuille f = new Feuille(valeur, hauteur);

		if (frere != null) {
			f.frere = frere.clone(h);
		}
		if (fils != null) {
			f.fils = fils.clone(h + 1);
		}
		return f;
	}

	public String aff(int n) {
		StringBuffer sb = new StringBuffer(100);
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		// le nom du symbole
		String vn = valeur.getNom();
		if (vn != null)
			sb.append(vn);
		if (fils != null) {
			sb.append("\n");
			sb.append(fils.aff(n + 1));
		}
		if (frere != null) {
			sb.append("\n");
			sb.append(frere.aff(n));
		}
		return sb.toString();
	}

	public boolean concatener(Arbre arbre, int h) {
		if (h > hauteur)
			return false;

		if (arbre.getDebut() == null)
			return false;

		if (fils == null)
			return false;

		boolean res = false;
		boolean ajout = false;

		if (fils.getValeur().getNumero() == SYMBOLE.LAMBDA) {
			fils = fils.frere;
			ajout = true;
		}
		if (fils != null) {
			res |= fils.concatener(arbre, h + 1);
			Feuille avant = fils;
			Feuille courant = fils.frere;
			while (courant != null) {
				if (courant.getValeur().getNumero() == SYMBOLE.LAMBDA) {
					ajout = true;
					avant.frere = courant.frere;
				} else {
					res |= courant.concatener(arbre, h + 1);
					avant = courant;
				}
				courant = avant.frere;
			}
		}
		if (ajout)
			res |= ajouter(arbre, h);
		return res;
	}

	public boolean remplacer_non_terminaux(int h) {
		boolean res = false;
		Feuille f;
		f = fils;
		Vector<Arbre> v = new Vector<Arbre>();

		while (f != null) {
			if (f.getValeur() instanceof NON_TERMINAL) {
				v.addElement(((NON_TERMINAL) f.getValeur()).getK_suivants());
			} else {
				res |= f.remplacer_non_terminaux(h + 1);
			}
			f = f.frere;
		}

		for (Enumeration<Arbre> e = v.elements(); e.hasMoreElements();) {
			res |= ajouter(e.nextElement(), h);
		}
		return res;
	}

}
