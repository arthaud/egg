package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.Resolveur;
import mg.egg.eggc.runtime.libjava.EGGException;

public class REGLE implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * le symbôle à gauche de la règle de production
	 */
	private SymbREGLE gauche;

	public SymbREGLE getGauche() {
		return gauche;
	}

	/**
	 * la liste de symbôles en partie droite de la règle de
	 * producion.
	 */
	private Vector<EltREGLE> droite;

	public Vector<EltREGLE> getDroite() {
		return droite;
	}

	/**
	 * cache des variables globales pour faciliter la gestion
	 */
	private Vector<GLOB> globales;

	public Vector<GLOB> getGlobales() {
		return globales;
	}

	/**
	 * recherche une globale par son nom
	 */
	public GLOB getGlobale(String n) {
		for (Enumeration<GLOB> e = globales.elements(); e.hasMoreElements();) {
			GLOB g = e.nextElement();
			if (g.getNom().equals(n)) {
				return g;
			}
		}
		return null;
	}

	/**
	 * pre : la globale n'existe pas déjà
	 *
	 * @param g
	 */
	public void ajouter_globale(GLOB g) {
		table.ajouter_globale(g);
		// pour optimiser l'INC
		globales.add(g);
	}

	transient private TDS_ACTION table;

	public TDS_ACTION getTable() {
		return table;
	}

	private int lastpos;

	private int deja_fait;

	/**
	 * le numéro de la règle de production.
	 */
	private int numero;

	public int getNumero() {
		return numero;
	}

	/**
	 * les k_premiers de la règle de production.
	 */
	transient private Arbre k_premiers;

	public Arbre getK_premiers() {
		return k_premiers;
	}

	private String comm;

	public String getComm() {
		return comm;
	}

	public REGLE(TDS t, NON_TERMINAL g, Resolveur r) {
		this(t, g, r, "");
	}

	/**
	 * Construit une régle de production avec gauche en gauche.
	 *
	 * @param gauche
	 *	        la partie gauche de la règle de production
	 */
	public REGLE(TDS t, NON_TERMINAL g, Resolveur r, String c) {
		numero = t.getCompteur_regles();
		table = new TDS_ACTION(r);
		lastpos = 0;
		gauche = new SymbREGLE(this, g.getNom(), lastpos++, g);
		// le premier symbole a droite a le numero 2, pour laisser la place
		// a auto_inh
		droite = new Vector<EltREGLE>(5, 5);
		globales = new Vector<GLOB>();
		k_premiers = null;
		deja_fait = 0;
		comm = c;
	}

	/**
	 * Renvoie une énumération des symbôles en partie droite
	 * de la règle de production.
	 */
	public Enumeration<EltREGLE> elements() {
		return droite.elements();
	}

	/**
	 * Calcule les premiers d'une chaîne de symbôles à
	 * l'ordre k.
	 *
	 * @param n
	 *	        l'ordre de l'arbre résultat
	 * @param k
	 *	        l'ordre des premiers
	 * @param v
	 *	        la chaîne de symbôles
	 * @param debut
	 *	        l'indice du premier élément de la chaîne
	 * @return les k_premiers
	 */
	public Arbre calcule_premiers_chaine(int n, int k, Vector<EltREGLE> v,
			int debut) throws EGGException {
		Arbre res = new Arbre(n, new SYMBOLE(SYMBOLE.LAMBDA));

		for (int i = debut; i < v.size(); i++) {
			EltREGLE e = (EltREGLE) v.elementAt(i);

			// action ou non ?
			if (e instanceof SymbREGLE) { // pas une action
				SYMBOLE s = ((SymbREGLE) e).getSymbole();

				// terminal ?
				if (s instanceof TERMINAL) {
					TERMINAL un_t = (TERMINAL) s;
					res.concatener(new Arbre(n, un_t));
				}
				else {
					if (s instanceof NON_TERMINAL) {
						NON_TERMINAL un_nt = (NON_TERMINAL) s;
						if (un_nt.estExterne()) {
							Arbre aux = new Arbre(n, un_nt);
							aux.concatener(new Arbre(n,
									new SYMBOLE(SYMBOLE.ALL)));
							res.concatener(aux);
						} else {
							res.concatener(un_nt.calcule_les_premiers(n, k
									- res.getLongueur()));
						}
					}
				}

				if (res.getLongueur() >= k) {
					break;
				}
			}
		}

		return res;
	}

	/**
	 * Calcule les suivants à l'ordre k.
	 *
	 * @param n
	 *	        la hauteur maximale de l'arbre résultat
	 * @param k
	 *	        l'ordre des suivants
	 */
	public void calcule_les_suivants(int n, int k) throws EGGException {
		for (int i = 0; i < droite.size(); i++) {
			EltREGLE e = (EltREGLE) droite.elementAt(i);

			if (e instanceof SymbREGLE) {
				SYMBOLE s = ((SymbREGLE) e).getSymbole();

				if (s instanceof NON_TERMINAL) {
					NON_TERMINAL un_nt = (NON_TERMINAL) s;
					Arbre res = calcule_premiers_chaine(n, k, droite, i + 1);

					if (res.getLongueur() < k) {
						Arbre aux = new Arbre(n, gauche.getSymbole());
						res.concatener(aux);
					}

					if (un_nt.getK_suivants() == null)
						un_nt.setK_suivants(new Arbre(n));

					un_nt.getK_suivants().ajouter(res);
				}
			}
		}
	}

	/**
	 * Calcule les premiers de la règle de production.
	 *
	 * @param n
	 *	        la hauteur maximale de l'arbre
	 * @param k
	 *	        l'ordre des premiers
	 * @return les k_premiers de la règle de production.
	 */
	public Arbre calcule_les_premiers(int n, int k) throws EGGException {
		if (k_premiers == null)
			k_premiers = new Arbre(n);

		Arbre res = calcule_premiers_chaine(n, k, droite, 0);
		// k_premiers.ajouter(res);
		// k_premiers = (Arbre)res.clone() ;

		if (k > deja_fait) {
			deja_fait = k;
			k_premiers = (Arbre) res.clone();
		}

		return res;
	}

	/**
	 * Calcule les symbôles directeurs de la règle de production.
	 */
	public void calcule_symboles_directeurs() {
		k_premiers.concatener(((NON_TERMINAL) gauche.getSymbole())
				.getK_suivants());
	}

	/**
	 * Ajoute un symbôle à droite de la règle de production.
	 *
	 * @param s le symbôle à ajouter
	 */
	public void add_droite(SYMBOLE s) {
		int p = ++lastpos;
		int occ = 0;

		String n = s.getNom();
		if (n.equals(gauche.getNom())) {
			occ++;
		}

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er instanceof SymbREGLE) {
				if (n.equals(((SymbREGLE) er).getSymbole().getNom())) {
					occ++;
				}
			}
		}

		if (occ != 0)
			n = n + occ;

		SymbREGLE sr = new SymbREGLE(this, n, p, s);
		droite.addElement(sr);
	}

	/**
	 * Ajouter une action à droite de la règle de production.
	 *
	 * @param n l'action à ajouter
	 */
	public boolean add_action(String n) {
		int p = ++lastpos;
		ActREGLE a = new ActREGLE(this, n, p);

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE s = (EltREGLE) e.nextElement();
			if (s.getNom().equals(n)) {
				return false;
			}
		}

		droite.addElement(a);
		return true;
	}

	public boolean add_action_inh() {
		int p = ++lastpos;
		ActREGLE a = new ActREGLE(this, p, ActREGLE.INHS);

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE s = (EltREGLE) e.nextElement();

			if (s.getNom().equals(a.getNom())) {
				return false;
			}
		}

		droite.addElement(a);
		return true;
	}

	public boolean add_action_inhs() {
		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE s = (EltREGLE) e.nextElement();

			if (s.getNom().equals("#auto_inh")) {
				return false;
			}
		}

		for (EltREGLE e : droite) {
			e.setPos(e.getPos() + 1);
		}

		ActREGLE a = new ActREGLE(this, 1, ActREGLE.INHS);
		droite.add(0, a);
		return true;
	}

	public boolean add_action_syns() {
		int p = ++lastpos;
		ActREGLE a = new ActREGLE(this, p, ActREGLE.SYNS);

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE s = (EltREGLE) e.nextElement();

			if (s.getNom().equals(a.getNom())) {
				return false;
			}
		}

		droite.addElement(a);
		return true;
	}

	/**
	 * Retourne l'action de nom nom.
	 *
	 * @param nom
	 *	        de l'action à chercher
	 * @return l'action de nom nom, null si inexistante
	 */
	public ActREGLE action(String nom) {
		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er instanceof ActREGLE && er.getNom().equals(nom)) {
				return (ActREGLE) er;
			}
		}

		return null;
	}

	/**
	 * Renvoie une représentation de la règle de production.
	 *
	 * @return une représentation de la règle de production.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		sb.append("#" + numero + " :  ");
		// sb.append("-- Attributs de " + gauche.getNom() + " :\n") ;
		// sb.append("-- " + gauche.getNom() + "\n" +
		// gauche.getSymbole().les_attributs());
		sb.append(gauche.getNom());
		sb.append(" -> ");

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();
			sb.append(er.getNom() + " ");
		}
		sb.append(";\n");

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er instanceof ActREGLE) {
				ActREGLE a = (ActREGLE) er;
				sb.append(a.getNom() + "{\n");
				if (a.getCode() != null) {
					sb.append(a.getCode());
				}
				sb.append("  }\n\n");
			}
		}
		sb.append("\n");

		return sb.toString();
	}

	public String toStringSyntax() {
		StringBuffer sb = new StringBuffer(100);
		sb.append(gauche.getNom());
		sb.append(" -> ");

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er instanceof ActREGLE) {
				continue;
			}

			sb.append(er.getNom() + " ");
		}
		sb.append(";");

		return sb.toString();
	}

	public String toStringSyntaxAction() {
		StringBuffer sb = new StringBuffer(100);
		sb.append(gauche.getNom());
		sb.append(" -> ");

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er instanceof ActREGLE && er.getNom().endsWith("auto_inh")) {
				continue;
			}

			sb.append(er.getNom() + " ");
		}
		sb.append(";");

		return sb.toString();
	}

	private void init_symb(SymbREGLE er) {
		SYMBOLE s = er.getSymbole();
		Vector<ATTRIBUT> attributs = s.getAttributs();

		for (Enumeration<ATTRIBUT> es = attributs.elements(); es
				.hasMoreElements();) {
			ATTRIBUT a = (ATTRIBUT) es.nextElement();
			UN_ATTRIBUT ua = new UN_ATTRIBUT(er.getNom() + '^' + a.getNom(),
					a.getType(), a.getSorte(), er.getPos());

			if (er.getPos() == 0)
				ua.setEtat(a.getSorte() == ATTRIBUT.HER);
			else
				ua.setEtat(a.getSorte() == ATTRIBUT.SYN);

			// cas particuliers de txt et scanner plus generalement des
			// built-ins ???
			if (a.getNom().equals("scanner") || a.getNom().equals("txt")
					|| a.getNom().equals("eval")) {
				ua.setEtat(true);
	        }

			ua = table.ajouter_attribut(ua);
		}
	}

	public void init() {
		/* pour chaque SymbREGLE s de la règle
	     * insérer les attributs de s dans la table de la règle
	     * initialiser les hérités de gauche à "initialisé"
	     * initialiser les synthétisés de gauche à "non initialisé"
	     * initialiser les hérités de droite à "non initialisé"
	     * initialiser les synthétisés de droite à "initialisé"
	     */
		init_symb(gauche);

		for (Enumeration<EltREGLE> e = droite.elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();
			if (er instanceof SymbREGLE) {
				init_symb((SymbREGLE) er);
			}
		}
	}

	// accès au symbole de pos p
	public SymbREGLE getElt(int p) {
		for (Enumeration<EltREGLE> e = droite.elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er.getPos() <= p)
				continue;

			if (er instanceof ActREGLE)
				continue;

			// NON_TERMINAL ou COMPIL
			return ((SymbREGLE) er);
		}

		return null;
	}

	// recherche des actions non définies
	public String verifierActions() {
		StringBuffer sb = new StringBuffer();

		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();
			if (er instanceof ActREGLE) {
				ActREGLE ar = (ActREGLE) er;
				if (!ar.isAutoInhs()) {
					if (ar.getCode() == null)
						sb.append(" " + ar.getNom());
				}
			}
		}

		if (sb.length() != 0)
			return sb.toString();

		return null;
	}

	// génération du code des autos dans le langage IAction
	public void autos(IAction l) {
		/*
	     * pour chaque ActRegle qui est auto
		 * engendrer le code de mise à jour des attributs
	     */
		for (Enumeration<EltREGLE> e = elements(); e.hasMoreElements();) {
			EltREGLE er = (EltREGLE) e.nextElement();

			if (er instanceof ActREGLE) {
				ActREGLE ar = (ActREGLE) er;

				// transmission des attributs synthétisés du symbole précédent
				if (ar.isAutoInhs()) {
					/*
	                 * transmission des attributs hérités aux symboles suivants
					 * éventuellement : existant et non déjà affecté
	                 */
					String c = CodeAutoInhs(ar, l);

					if (c != null) { // stocker le code
						// ar.setCode(c);
						ar.setCodeSrc(c);
					} else { // supprimer l'action
						droite.remove(ar);
					}
				}
			}
		}
	}

	private String CodeAutoInhs(ActREGLE ar, IAction l) {
		boolean code_non_vide = false;
		StringBuffer sb = new StringBuffer();
		sb.append("\n  do\n");
		sb.append("   -- auto generated code from inherited attributes\n");

		// Les attributs hérités du symbole de gauche
		SymbREGLE gs = gauche;
		SYMBOLE g = gs.getSymbole();
		Vector<ATTRIBUT> g_attributs = g.getAttributs();

		for (Enumeration<ATTRIBUT> eg = g_attributs.elements(); eg
				.hasMoreElements();) {
			ATTRIBUT a = (ATTRIBUT) eg.nextElement();
			UN_ATTRIBUT ga = (UN_ATTRIBUT) (table.chercher(gs.getNom() + '^'
					+ a.getNom()));

			if (a.getSorte() == ATTRIBUT.SYN)
				continue;

			for (Enumeration<EltREGLE> e = droite.elements(); e.hasMoreElements();) {
				EltREGLE er = (EltREGLE) e.nextElement();

				if (er instanceof SymbREGLE) {
					SymbREGLE sr = (SymbREGLE) er;

					// a est-il un attribut hérité du symbole courant de la
					// partie droite ?
					UN_ATTRIBUT sa = (UN_ATTRIBUT) (table.chercher(sr.getNom()
							+ '^' + a.getNom()));

					if (sa != null) { // oui
						if (sa.getEtat()) {
							sb.append("	--" + sr.getNom() + '^' + a.getNom()
									+ " affecte par ailleurs\n");
						} else {
							sb.append("	" + l.mkCopy(sa, ga));
							code_non_vide = true;
						}
					}
				} // symbRegle
			}
		}
		sb.append("  end\n");

		if (code_non_vide)
			return sb.toString();
		else
			return null;
	}

	public void shiftElementLeft(EltREGLE elt) {
	}

	public void shiftElementRight(EltREGLE elt) {
	}

	public void removeElement(EltREGLE elt) {

	}
}
