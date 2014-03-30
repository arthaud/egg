package mg.egg.eggc.compiler.libegg.type;

import java.util.Vector;

/**
 * La classe DTypeClasse implémente le type classe.<br>
 * Le type classe est représenté par le nom de la classe, ainsi qu'un lien vers<br>
 * une interface dans la hiérarchie des classes.
 * 
 * @author CD2005
 * @see DInterface
 */
public class TClasse implements IType {

    private static final long serialVersionUID = 1L;

    /**
     * Le nom du type
     */
    protected String nom;

    /**
     * Renvoie le nom du type
     * 
     * @return Le nom du type
     */
    public String getNom() {
        return nom;
    }

    /**
     * 
     */
    private Vector<IType> pars;

    /*
     * (non-Javadoc)
     * 
     * @see mg.egg.eggc.compiler.libegg.type.IType#getPars()
     */
    public Vector<IType> getPars() {
        return pars;
    }

    /*
     * (non-Javadoc)
     * 
     * @see mg.egg.eggc.compiler.libegg.type.IType#addPar(mg.egg.eggc.compiler.libegg.type.IType)
     */
    public void addPar(IType autre) {
        if (pars == null) {
            pars = new Vector<IType>();
        }
        pars.add(autre);
    }

//    /**
//     * 
//     */
//    protected Vector<DMethode> methodes;
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see mg.egg.eggc.compiler.libegg.type.IType#getMethodes()
//     */
//    public Vector<DMethode> getMethodes() {
//        return methodes;
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see mg.egg.eggc.compiler.libegg.type.IType#addMethode(mg.egg.eggc.compiler.libegg.type.DMethode)
//     */
//    public void addMethode(DMethode autre) {
//        if (methodes == null) {
//            methodes = new Vector<DMethode>();
//        }
//        methodes.add(autre);
//
//    }

    /**
     * Construit un type classe à partir de son nom.<br>
     * Le lien vers l'interface dans la hiérarchie des classes est nul.
     * 
     * @param nomClasse
     *            Le nom de la classe
     */
    public TClasse(String n) {
        this(n, null);
    }

//    /**
//     * Construit un type classe à partir de son interface.<br>
//     * 
//     * @param di
//     *            L'interface à associer au type
//     */
//    public TClasse(IDInterface di) {
//        nom = di.getNom();
//        methodes = null;
//        pars = null;
//    }

    /**
     * @param n
     * @param args
     */
    public TClasse(String n, Vector<IType> args) {
        nom = n;
//        methodes = null;
        pars = args;
    }

    /**
     * @param type
     *            Le type à comparer
     * @return true si les types sont égaux, false sinon
     */
    public boolean equals(IType type) {
        return (this == type);
    }

    /** Renvoie false car le type classe n'est pas un type primitif */
    public boolean estPrimitif() {
        return false;
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères du type,
     * comprenant<br>
     * le nom du type.
     * 
     * @return Une représentation sous forme de chaîne de caractères du type
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getNom() + " ");
//        if (methodes != null)
//            sb.append(methodes.toString());
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return 0;
    }

    private Vector<IType> surClasses;

    public Vector<IType> getSurclasses() {
        return surClasses;
    }

    public void setSurClasses(Vector<IType> s) {
        surClasses = s;
    }

    public void addSurclasse(TClasse top) {
        if (surClasses == null)
            surClasses = new Vector<IType>();
        surClasses.add(top);
    }
}
