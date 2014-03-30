package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

public class SymbREGLE extends EltREGLE implements Serializable {
	private static final long serialVersionUID = 1L;


	private SYMBOLE symbole;

	public SYMBOLE getSymbole() {
		return symbole;
	}

	public SymbREGLE(REGLE r, String n, int p, SYMBOLE s) {
		super(r, n, p);
		symbole = s;
	}

	public String toString() {
		return nom + '(' + pos + ')';
	}
}
