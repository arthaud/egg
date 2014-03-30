// partie commune a tous les resolveurs
// des differents langages d'action
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
     * acces a la liste des types
     * 
     * @return
     */
    public HashMap<String, Vector<IType>> getTypes() {
        return types;
    }

    /**
     * rechercher un type par son nom
     * 
     * @param n
     * @return
     */
    public IType getType(String n) {
        return getType(n, null);
    }

    abstract public IType getType(String n, Vector<IType> args);

    /**
     * creation d'une variable de type
     * 
     * @return
     */
    abstract public IType getType();

    /**
     * Algo de resolution de l'inference de type
     * 
     * @return
     */
    abstract public boolean resoudre();

    /**
     * Generation des types et classes
     */
    abstract public void generer();

    /**
     * construction et initialisation
     */
    public Resolveur() {
        this("x");
    }

    /**
     * construction et initialisation
     */
    public Resolveur(String gram) {
        // les contraintes
        // la liste des types
        types = new HashMap<String, Vector<IType>>();
        nom_gram = gram;
    }

    private Vector<String> readConf(String n) throws EGGException {
        Vector<String> incs = new Vector<String>();
        try {
            System.err.println("Lecture du fichier de conf " + n);
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
                System.err.println("fichier de conf introuvable");
            }
        } catch (IOException ee) {
            // ee.printStackTrace();
            // throw new EGGException("Erreur fichier conf");
        }
        return incs;
    }

    /**
     * construction et initialisation
     * 
     * @param gram
     * @param i
     */
    public Resolveur(String gram, String conf) {
        // les contraintes
        // la liste des types
        types = new HashMap<String, Vector<IType>>();
        nom_gram = gram;
        // lecture du fichier de conf
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
         * niveau que le type<br>
         * dans la hiérarchie des classes
         */
        EGAL,
        /**
         * La relation INFERIEUR_EGAL indique que la variable doit hériter ou se
         * trouver<br>
         * au même niveau que le type dans la hiérarchie des classes
         */
        INFERIEUR_EGAL,
        /** La relation INFERIEUR indique que la variable doit hériter du type */
        INFERIEUR,
        /**
         * La relation SUPERIEUR_EGAL indique que la variable doit être une
         * super classe ou se trouver<br>
         * au même niveau que le type dans la hiérarchie des classes
         */
        SUPERIEUR_EGAL,
        /**
         * La relation SUPERIEUR indique que la variable doit être une super
         * classe du type
         */
        SUPERIEUR;

        /**
         * Renvoie la "somme" de 2 relations.<br>
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
         * Renvoie la relation opposée.<br>
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

    private void System_err_println(String m) {
        // System.err.println(m);
    }
}
