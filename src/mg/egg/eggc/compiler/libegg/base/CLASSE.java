package mg.egg.eggc.compiler.libegg.base;

public class CLASSE {
	private String nom;

	public String getNom() {
		return nom;
	}

	/**
	 * Construit une nouvelle constante classe.
	 * 
	 * @param nom
	 *            le nom de la constante
	 */
	public CLASSE(String nom) {
		this.nom = nom;
	}

	/**
	 * Renvoie une repr&eacute;sentation textuelle de la classe courante.
	 * 
	 * @param une
	 *            repr&eacute;sentation textuelle de la classe courante
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		sb.append("classe : " + nom + "\n");
		return sb.toString();
	}

	boolean predefinie() {
		return nom.equals("STRING") || nom.equals("INTEGER")
				|| nom.equals("DOUBLE") || nom.equals("BOOLEAN")
				|| nom.equals("CHARACTER");
	}

}
