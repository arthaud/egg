/*
 * Partie commune à tous les resolveurs des differents langages d'action
 */
package mg.egg.eggc.compiler.libegg.type;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ENTREE;
import mg.egg.eggc.runtime.libjava.EGGException;

abstract public class Resolveur {
	protected String nom_gram;

	protected Vector<String> incs;

	/**
	 * la liste des types
	 */
	protected HashMap<String, Vector<IType>> types;

	/**
	 * accès à la liste des types
	 */
	public HashMap<String, Vector<IType>> getTypes() {
	    return types;
	}

	/**
	 * rechercher un type par son nom
	 */
	public IType getType(String n) {
	    return getType(n, null);
	}

	abstract public IType getType(String n, Vector<IType> args);

	/**
	 * création d'une variable de type
	 */
	abstract public IType getType();

	/**
	 * Algo de résolution de l'inférence de type
	 */
	abstract public boolean resoudre();

	/**
	 * Génération des types et classes
	 */
	abstract public void generer();

	public Resolveur() {
	    this("x");
	}

	public Resolveur(String gram) {
	    types = new HashMap<String, Vector<IType>>();
	    nom_gram = gram;
	}

	private Vector<String> readConf(String n) throws EGGException {
	    Vector<String> incs = new Vector<String>();

	    try {
	        System.err.println("Lecture du fichier de configuration " + n);

	        if (n == null)
	            return incs;

	        File cfile = new File(n);

	        if (cfile.exists()) {
	            BufferedReader br = new BufferedReader(new FileReader(cfile));

	            while (br.ready()) {
	                String t = br.readLine();
	                if (t.length() > 0) {
	                    incs.add(t);
	                }
	            }
	        } else {
	            System.err.println("warning: fichier de configuration introuvable");
	        }
	    } catch (IOException ee) {
	        // throw new EGGException("Erreur fichier conf");
	    }

	    return incs;
	}

	public Resolveur(String gram, String conf) {
	    types = new HashMap<String, Vector<IType>>();
	    nom_gram = gram;

	    try {
	        incs = readConf(conf);
	    } catch (EGGException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Les différentes relations possibles avec le type
	 */
	public static enum RELATION_TYPE {
	    /**
	     * La relation EGAL indique que la variable doit se trouver au même
	     * niveau que le type dans la hiérarchie des classes
	     */
	    EGAL,
	    /**
	     * La relation INFERIEUR_EGAL indique que la variable doit hériter ou se
	     * trouver au même niveau que le type dans la hiérarchie des classes
	     */
	    INFERIEUR_EGAL,
	    /** La relation INFERIEUR indique que la variable doit hériter du type */
	    INFERIEUR,
	    /**
	     * La relation SUPERIEUR_EGAL indique que la variable doit être une
	     * super classe ou se trouver
	     * au même niveau que le type dans la hiérarchie des classes
	     */
	    SUPERIEUR_EGAL,
	    /**
	     * La relation SUPERIEUR indique que la variable doit être une super
	     * classe du type
	     */
	    SUPERIEUR;

	    /**
	     * Renvoie la "somme" de 2 relations.
	     * Exemple : "<=" + "=>" = "="
	     *
	     * @param t1
	     *            La première relation
	     * @param t2
	     *            La seconde relation
	     * @return La "somme" des 2 relations
	     * @throws GrapheException
	     *             Si les relations ne sont pas sommables
	     */
	    public static RELATION_TYPE somme(RELATION_TYPE t1, RELATION_TYPE t2)
	            throws EGGException {
	        if (t1 == EGAL)
	            return t2;

	        if (t2 == EGAL)
	            return t1;

	        if (((t1 == INFERIEUR_EGAL || t1 == INFERIEUR) && t2 == SUPERIEUR)
	                || ((t1 == SUPERIEUR || t1 == SUPERIEUR_EGAL) && t2 == INFERIEUR)
	                || (t1 == INFERIEUR && t2 == SUPERIEUR_EGAL)
	                || (t1 == SUPERIEUR && t2 == INFERIEUR_EGAL))
	            //				throw new EGGException(1000,
	            //						"Relations non sommables");

	            if (t1 == INFERIEUR_EGAL && t2 == SUPERIEUR_EGAL
	                    || t1 == SUPERIEUR_EGAL && t2 == SUPERIEUR_EGAL)
	                return EGAL;

	        // cas possibles ici : < et <=, > et >=, cas d'égalité
	        if (t1 == INFERIEUR || t2 == INFERIEUR)
	            return INFERIEUR;

	        if (t1 == SUPERIEUR || t2 == SUPERIEUR)
	            return INFERIEUR;

	        // t1 et t2 sont égaux
	        return t1;
	    }

	    /**
	     * Renvoie la relation opposée.
	     * Exemple "<=" renverra ">="
	     *
	     * @return La relation opposée
	     */
	    public RELATION_TYPE getOppose() {
	        switch (this) {
	            case EGAL:
	                return EGAL;
	            case INFERIEUR_EGAL:
	                return SUPERIEUR_EGAL;
	            case INFERIEUR:
	                return SUPERIEUR;
	            case SUPERIEUR_EGAL:
	                return INFERIEUR_EGAL;
	            case SUPERIEUR:
	                return INFERIEUR;
	        }

	        return this;
	    }

	    public String toString() {
	        switch (this) {
	            case EGAL:
	                return "=";
	            case INFERIEUR_EGAL:
	                return "<=";
	            case INFERIEUR:
	                return "<";
	            case SUPERIEUR_EGAL:
	                return ">=";
	            case SUPERIEUR:
	                return ">";
	        }

	        return "?";
	    }
	}

	abstract public void contDecl(ENTREE e);

	abstract public void contAff(ENTREE e, IType t);

	abstract public void contBool(IType t);

	abstract public IType contOp(IType t1, String op, IType t2);

	abstract public IType contOpNon(IType t);

	abstract public IType contConst(IType t, Vector<IType> ts);

	abstract public IType contFct(ENTREE e, String m, Vector<IType> ts);

	abstract public void contProc(ENTREE e, String m, Vector<IType> ts);

	abstract public void contErr(Vector<IType> ts);
}
