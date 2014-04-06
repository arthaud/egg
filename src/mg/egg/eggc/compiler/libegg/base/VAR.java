package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.IType;

public class VAR extends ENTREE {
	private static final long serialVersionUID = 1L;

	public VAR(String n, IType t) {
		super(n, t);
	}

	public String toString() {
		return "VAR " + " : " + nom + ", " + type + ", " + etat;
	}

	public boolean utilisable(int pos) {
		return etat;
	}

	public boolean affectable(int pos) {
		return true;
	}

}
