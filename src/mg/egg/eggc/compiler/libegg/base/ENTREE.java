package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

import mg.egg.eggc.compiler.libegg.type.IType;

public class ENTREE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	protected boolean etat;

	/**
	 * @return
	 */
	public boolean getEtat() {
		return etat;
	}

	/**
	 * @param b
	 */
	public void setEtat(boolean b) {
		etat = b;
	}

	/**
	 * 
	 */
	protected String nom;

	/**
	 * @return
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * 
	 */
	protected IType typeReel;

	/**
	 * @return
	 */
	public IType getTypeReel() {
		return typeReel;
	}

	/**
	 * @param t
	 */
	public void setTypeReel(IType t) {
		typeReel = t;
	}

	/**
	 * 
	 */
	public void resetTypeReel() {
		typeReel = null;
	}

	/**
	 * 
	 */
	protected IType type;

	/**
	 * @return
	 */
	public IType getType() {
		return type;
	}

	/**
	 * @param t
	 */
	public void setType(IType t) {
		type = t;
	}

	// public abstract boolean utilisable(int pos);
	//
	// public abstract boolean affectable(int pos);
	public boolean utilisable(int pos) {
		return true;
	}

	public boolean affectable(int pos) {
		return true;
	}

	public ENTREE(String n, IType t) {
		nom = n;
		type = t;
		// typeReel = null;
		typeReel = null;
		etat = false;
	}

	public ENTREE(String n) {
		this(n, null);
	}

	public String toString() {
		return "ENTREE " + " : " + nom + ", " + type + ", " + etat;
	}

}
