package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

import mg.egg.eggc.compiler.libegg.type.IType;

public class ENTREE implements Serializable {
	private static final long serialVersionUID = 1L;

	protected boolean etat;

	protected String nom;

	protected IType typeReel;

	protected IType type;

	public boolean getEtat() {
		return etat;
	}

	public void setEtat(boolean b) {
		etat = b;
	}

	public String getNom() {
		return nom;
	}

	public IType getTypeReel() {
		return typeReel;
	}

	public void setTypeReel(IType t) {
		typeReel = t;
	}

	public void resetTypeReel() {
		typeReel = null;
	}

	public IType getType() {
		return type;
	}

	public void setType(IType t) {
		type = t;
	}

	public boolean utilisable(int pos) {
		return true;
	}

	public boolean affectable(int pos) {
		return true;
	}

	public ENTREE(String n, IType t) {
		nom = n;
		type = t;
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
