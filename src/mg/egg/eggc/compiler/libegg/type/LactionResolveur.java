package mg.egg.eggc.compiler.libegg.type;

/**
 * Resolveur qui ne fait rien ...
 */
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ENTREE;

public class LactionResolveur extends Resolveur {
	public IType getType() {
		return null;
	}

	/**
	 * rechercher un type par son nom et args
	 *
	 * @param n
	 * @param args
	 */
	public IType getType(String n, Vector<IType> args) {
		Vector<IType> vt = types.get(n);
		IType it = null;

		// le nom est trouvé. Et les pars ?
		if (vt != null) {
			boolean trouve = false;
			for (IType t : vt) {
				// si pas de pars pour t
				if (args == null && t.getPars() == null) {
					it = t;
					trouve = true;
					break;
				}

				if (args != null && args.equals(t.getPars())) {
					it = t;
					trouve = true;
					break;
				}
			}

			if (!trouve) {
				it = new TClasse(n, args);
				vt.add(it);
			}

			// si meme nom et args differents l'ajouter
			// TODO
		} else {
			// nom pas trouvé ? On le crée
			// System.out.println("creation type " + n);
			it = new TClasse(n, args);
			Vector<IType> v = new Vector<IType>();
			v.add(it);
			types.put(n, v);
		}
		return it;
	}

	public boolean resoudre() {
		return true;
	}

	public void generer() {
	}

	public LactionResolveur() {
		super();
	}

	public void contDecl(ENTREE e) {
	}

	public void contAff(ENTREE e, IType t) {
	}

	public void contBool(IType t) {
	}

	public IType contOp(IType t1, String op, IType t2) {
		return null;
	}

	public IType contOpNon(IType t) {
		return null;
	}

	public IType contFct(IType e, String m, Vector<IType> ts) {
		return null;
	}

	public void contProc(IType e, String m, Vector<IType> ts) {
	}

	public void contErr(Vector<IType> ts) {
	}

	public IType contConst(IType t, Vector<IType> ts) {
		return t;
	}

}
