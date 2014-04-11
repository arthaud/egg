package mg.egg.eggc.compiler.libegg.base;

import java.io.File;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.Resolveur;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.EGGOptions;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

public class TDS implements Serializable {

	private static final long serialVersionUID = 1L;

	private NON_TERMINAL axiome;

	public NON_TERMINAL getAxiome() {
		return axiome;
	}

	public void setAxiome(NON_TERMINAL nt) {
		axiome = nt;
	}

	public boolean estAxiome(NON_TERMINAL nt) {
		return axiome == nt;
	}

	Attributs attributs;

	public String all_atts() {
		return attributs.toString();
	}

	private String nom;

	public String getNom() {
		return nom;
	}

	private void setNom() {
		String s = cu.getFileName();
		int i = s.lastIndexOf(File.separatorChar);

		if (i != -1)
			s = s.substring(i + 1);

		if (s.endsWith(".m")) {
			s = s.substring(0, s.length() - 2);
		} else if (s.endsWith(".egg")) {
			s = s.substring(0, s.length() - 4);
		}

		nom = s;
	}

	/**
	 * les options.
	 */
	private EGGOptions options;

	public EGGOptions getOptions() {
		return options;
	}

	// lien avec le système de types
	transient private Resolveur resolveur;

	public Resolveur getResolveur() {
		return resolveur;
	}

	public void setResolveur(Resolveur resolveur) {
		this.resolveur = resolveur;
	}

	/**
	 * la table.
	 */
	public Hashtable<String, SYMBOLE> get_table() {
		return htable;
	}

	// table des règles dans l'ordre de déclaration
	public int compteur_regles = 0;

	public int getCompteur_regles() {
		return compteur_regles++;
	}

	private Vector<REGLE> regles;

	public Vector<REGLE> get_regles() {
		return regles;
	}

	public void add_regle(REGLE regle) {
		regles.add(regle);
	}

	private Vector<SYMBOLE> lexicaux;

	public Vector<SYMBOLE> getLexicaux() {
		return lexicaux;
	}

	public void addProperty(String message, int nbargs) {
		if (properties.get(message) == null) {
			StringBuffer sb = new StringBuffer();
			sb.append(message.toLowerCase());

			for (int i = 0; i < nbargs; i++) {
				sb.append("{" + i + "} ");
			}

			properties.put(message, sb.toString());
		}
	}

	public void addProperty(String message) {
		addProperty(message, 0);
	}

	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	private Hashtable<String, SYMBOLE> htable;

	public String directory() {
		return options.getDirectory();
	}

	public String prefix() {
		return options.getPrefix();
	}

	/**
	 * retourne une représentation de la table des symbôles.
	 *
	 * @return une chaîne représentant la TDS
	 */
	public String toString() {
		StringBuffer r = new StringBuffer();

		for (Enumeration<SYMBOLE> e = htable.elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			r.append(s);
		}

		return r.toString();
	}

	/**
	 * insere un nouvel élément.
	 * l'élément est inséré à la fin de table
	 *
	 * @param i
	 *	        l'élément à insérer
	 */
	public void inserer(SYMBOLE i) {
		SYMBOLE o = htable.get(i.getNom());
		if (o != null) {
		} else {
			htable.put(i.getNom(), i);
			i.setTable(nom);
		}
	}

	/**
	 * insére un nouvel élément.
	 * l'élément est inséré à la fin de table
	 *
	 * @param i
	 *	        l'élément à insérer
	 */
	public boolean inserer_lexical(SYMBOLE i) {
		SYMBOLE o = htable.get(i.getNom());
		if (o != null) {
			return false;
		} else {
			htable.put(i.getNom(), i);
			lexicaux.addElement(i);
			i.setTable(nom);
			return true;
		}
	}

	/**
	 * recherche un Symbole par son nom.
	 * et positionne le booléen ok en cas de succès
	 *
	 * @param n
	 *	        le nom de l'info à rechercher
	 * @return l'info de nom n si elle est présente ou null sinon
	 * @see SYMBOLE
	 * @see #ok
	 */
	public SYMBOLE chercher(String n) {
		return htable.get(n);
	}

	/**
	 * remplace une info par une info de même nom.
	 * l'info est inséré si la recherche échoue
	 *
	 * @param i
	 *	        l'info à mettre à jour
	 */
	void remplacer(SYMBOLE i) {
		htable.remove(i.getNom());
		htable.put(i.getNom(), i);
	}

	/**
	 * supprime l'info de mème nom.
	 * ne fait rien si la recherche échoue
	 *
	 * @param i
	 *	        l'info à supprimer
	 */
	void supprimer(SYMBOLE i) {
		htable.remove(i.getNom());
	}

	transient private IEGGCompilationUnit cu;

	public IEGGCompilationUnit getCompilationUnit() {
		return cu;
	}

	public TDS(IEGGCompilationUnit unit) throws EGGException {
		cu = unit;
		htable = new Hashtable<String, SYMBOLE>();
		lexicaux = new Vector<SYMBOLE>();
		compteur_regles = 0;
		regles = new Vector<REGLE>();
		properties = new Properties();
		attributs = new Attributs();
		options = cu.getOptions();
		setNom();

		// lire la tds de la precedente compilation
		// oldtds = cu.readTds();
	}

	public void setAuto(boolean v) {
		options.setAuto(v);
	}

	public void setVersion(String v) {
		options.setVersion(v);
	}

	public void setK(String v) {
		int k = Integer.parseInt(v);
		options.setK(k);
	}

	/**
	 * Renvoie les éléments de la TDS.
	 *
	 * @return les éléments de la TDS
	 */
	public Enumeration<SYMBOLE> elements() {
		return htable.elements();
	}

	/**
	 * Retourne l'attribut de nom nom
	 *
	 * @param nom
	 *	        le nom de l'attribut a rechercher
	 * @return l'attribut recherché, null si inconnu
	 */
	public ATTRIBUT attribut(String nom) {
		return attributs.attribut(nom);
	}

	/**
	 * Ajoute un attribut. @ param a l'attribut à ajouter return 1 si
	 * l'attribut a été ajouté, 0 sinon
	 */
	public boolean ajouter_att(ATTRIBUT a) {
		return attributs.ajouter_att(a);
	}

	/**
	 * Calcule les premiers de tous les non-terminaux.
	 *
	 * @param k l'ordre des premiers
	 */
	private void calcule_les_premiers(int k) throws EGGException {
		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL nt = (NON_TERMINAL) s;
				if (nt.estExterne())
					continue;
				nt.calcule_les_premiers(k, k);
			}
		}
	}

	/**
	 * Calcule les suivants de tous les non-terminaux.
	 *
	 * @param k l'ordre des suivants
	 */
	private void calcule_les_suivants(int k) throws EGGException {
		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL un_nt = (NON_TERMINAL) s;
				if (un_nt.estExterne())
					continue;
				un_nt.calcule_les_suivants(k, k);
			}
		}

		for (boolean changes = true; changes;) {
			changes = false;
			for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
				SYMBOLE s = e.nextElement();
				if (s instanceof NON_TERMINAL) {
					NON_TERMINAL un_nt = (NON_TERMINAL) s;
					if (un_nt.estExterne())
						continue;
					boolean aux = un_nt.remplace_non_terminaux();
					changes |= aux;
				}
			}
		}

		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL un_nt = (NON_TERMINAL) s;
				if (un_nt.estExterne())
					continue;
				un_nt.getK_suivants().supprimer_non_terminaux();
			}
		}

		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL un_nt = (NON_TERMINAL) s;
				if (un_nt.estExterne())
					continue;
			}
		}

	}

	/**
	 * L'axiome reçoit le premier non-terminal.
	 */
	public void ajouter_axiome() {
		axiome.setK_suivants(new Arbre(options.getK()));
		axiome.getK_suivants().ajouter(
				new Arbre(options.getK(), new SYMBOLE(SYMBOLE.EOF)));
	}

	/**
	 * Calcule les symbôles directeurs de tous les non-terminaux.
	 */
	private void calcule_symboles_directeurs() {
		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL nt = (NON_TERMINAL) s;
				if (nt.estExterne())
					continue;
				nt.calcule_symboles_directeurs();
			}
		}
	}

	/**
	 * Détecte les conflits sur toutes les régles de production.
	 *
	 * @param k l'ordre du conflit
	 */
	private void detecter_les_conflits(int k) throws EGGException {
		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL nt = (NON_TERMINAL) s;
				if (nt.estExterne())
					continue;
				nt.detecterConflits(k);
			}
		}
	}

	/**
	 * Réduit l'arbre des k_premiers de tous les non-terminaux.
	 */
	private void reduire() {
		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL nt = (NON_TERMINAL) s;
				if (nt.estExterne())
					continue;
				nt.getK_premiers().reduire();
			}
		}
	}

	public boolean getModule() {
		return options.getModule();
	}

	/**
	 * Détecte un non-terminal sans règle de production.
	 */
	void detecte_nt_sans_prod() throws EGGException {
		for (Enumeration<SYMBOLE> e = elements(); e.hasMoreElements();) {
			SYMBOLE s = e.nextElement();
			if (s instanceof NON_TERMINAL) {
				NON_TERMINAL nt = (NON_TERMINAL) s;
				if (nt.estExterne())
					continue;
				if (nt.getRegles().size() == 0) {
					// recupérée par analyser_grammaire de TDS
					throw new EGGException(IProblem.Syntax,
							ICoreMessages.id_EGG_nt_undefined_error,
							CoreMessages.EGG_nt_undefined_error, nt.getNom());
				}
			}
		}
	}

	/**
	 * Analyse syntaxiquement le source * LA fonction qui communique avec le .m
	 *
	 * @throws EGGException
	 */
	public void analyser_syntaxe() throws EGGException {
		// si la syntaxe à changé, la revérifier
		detecte_nt_sans_prod();

		calcule_les_premiers(options.getK());

		calcule_les_suivants(options.getK());

		calcule_symboles_directeurs();

		detecter_les_conflits(options.getK());

		reduire();

		// cu.writeTds(this);
	}

	public boolean getAutoAtt() {
		return options.getAutoAtt();
	}

	public boolean est_axiome(NON_TERMINAL nt) {
		return axiome == nt;
	}

	public boolean typage() {
		return options.hasTypage();
	}

	public boolean getDst() {
		return options.getDst();
	}

	public boolean getMain() {
		return options.getMain();
	}

	public boolean syntaxOnly() {
		return options.syntaxOnly();
	}

	public int getK() {
		return options.getK();
	}

	public boolean verifierOptions() {
		return options.verifier();
	}

	public ATTRIBUT getAttribut(SYMBOLE s, String n) {
		for (Enumeration<ATTRIBUT> e = s.getAttributs().elements(); e
				.hasMoreElements();) {
			ATTRIBUT a = (ATTRIBUT) e.nextElement();
			if (a.getNom().equals(n))
				return a;
		}
		return null;
	}

}
