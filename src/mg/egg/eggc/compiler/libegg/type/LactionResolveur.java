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
	 * @return
	 */
	public IType getType(String n, Vector<IType> args) {
		// System.out.println("getType" + n);
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
				// a reecrire mieux ?
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

	// construction et initialisation
	public LactionResolveur() {
		super();
	}

	public void contDecl(ENTREE e) {
	}

	// e <= t
	public void contAff(ENTREE e, IType t) {
	}

	// t doit etre booleen
	public void contBool(IType t) {
	}

	// t1 et t2 comparables
	// op permet d'affiner la contrainte
	public IType contOp(IType t1, String op, IType t2) {
		return null;
	}

	// t doit etre booleen
	public IType contOpNon(IType t) {
		return null;
	}

	// e est l'instance
	// m la methode fonction
	// ts les types des args
	// le retour est le type de la m fonction
	// La signature de m est conservee
	public IType contFct(ENTREE e, String m, Vector<IType> ts) {
		return null;
	}

	// e est l'instance
	// m la methode proc
	// ts les types des args
	// La signature de m est conservee
	public void contProc(ENTREE e, String m, Vector<IType> ts) {
	}

	public void contErr(Vector<IType> ts) {
	}

	public IType contConst(IType t, Vector<IType> ts) {
		return t;
	}

}
