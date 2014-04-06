package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

public abstract class EltREGLE implements Serializable {
	private static final long serialVersionUID = 1L;
	protected REGLE regle;
	protected String nom;
	protected int pos;

	public String getNom() {
		return nom;
	}

	public EltREGLE(REGLE regle, String nom, int pos) {
		super();
		this.regle = regle;
		this.nom = nom;
		this.pos = pos;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int i) {
		pos = i;
	}

	public void shiftLeft() {
	}

	public void shiftRight() {
	}

	public void remove() {
	}

}
