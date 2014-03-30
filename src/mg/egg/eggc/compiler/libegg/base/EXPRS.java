package mg.egg.eggc.compiler.libegg.base;

import java.util.Vector;

public class EXPRS extends Vector<EXPR> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EXPR getElt(int i) {
		return (EXPR) elementAt(i);
	}

}
