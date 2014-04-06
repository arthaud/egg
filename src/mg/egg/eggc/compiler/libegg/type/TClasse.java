package mg.egg.eggc.compiler.libegg.type;

import java.util.Vector;

/**
 * La classe DTypeClasse implémente le type classe.
 * Le type classe est représenté par le nom de la classe, ainsi qu'un lien vers
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

	private Vector<IType> pars;

	public Vector<IType> getPars() {
	    return pars;
	}

	public void addPar(IType autre) {
	    if (pars == null) {
	        pars = new Vector<IType>();
	    }
	    pars.add(autre);
	}

	/**
	 * Construit un type classe à partir de son nom.
	 * Le lien vers l'interface dans la hiérarchie des classes est nul.
	 *
	 * @param nomClasse Le nom de la classe
	 */
	public TClasse(String n) {
	    this(n, null);
	}

	public TClasse(String n, Vector<IType> args) {
	    nom = n;
	    pars = args;
	}

	/**
	 * @param type Le type à comparer
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
	 * comprenant
	 * le nom du type.
	 *
	 * @return Une représentation sous forme de chaîne de caractères du type
	 */
	public String toString() {
	    StringBuffer sb = new StringBuffer();
	    sb.append(getNom() + " ");
	    return sb.toString();
	}

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
