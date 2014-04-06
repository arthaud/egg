package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

import mg.egg.eggc.compiler.libegg.type.IType;

public class GLOB extends ENTREE implements Serializable {

	private static final long serialVersionUID = 1L;
	private REGLE regle;

	public GLOB(String n, IType t) {
		super(n, t);
	}

	public GLOB(REGLE r, String n, IType t) {
		super(n, t);
		regle = r;
	}

	public String toString() {
		return "GLOB " + " : " + nom + ", " + type.getNom() + ", " + etat;
	}

	public boolean utilisable(int pos) {
		return etat;
	}

	public boolean affectable(int pos) {
		return true;
	}

	// INC_COMP
	transient private boolean nomChange = false;

	transient private boolean typeChange = false;

	public void setNomChange(boolean b) {
		nomChange = b;
	}

	public void setTypeChange(boolean b) {
		typeChange = b;
	}

	public void setAllChange(boolean b) {
		nomChange = b;
		typeChange = b;
	}

	public boolean getNomChange() {
		return nomChange;
	}

	public boolean getTypeChange() {
		return typeChange;
	}

	/**
	 * la globale a-t-elle changé depuis la dernière compilation ?
	 *
	 * @param old
	 */
	public void compare(GLOB old) {
		nomChange = !nom.equals(old.nom);
		typeChange = !type.getNom().equals(old.type.getNom());
	}

	public REGLE getRegle() {
		return regle;
	}

}
