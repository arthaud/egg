package lib;

public class DTYPE {
	protected int taille;

	public int getTaille() {
		return taille;
	}

	protected String nom;

	public String getNom() {
		return nom;
	}

	public DTYPE(String n, int t) {
		nom = n;
		taille = t;
	}

	public boolean compareTo(DTYPE autre) {
		return nom.equals(autre.nom);
	}

	public String toString() {
		return nom + "(" + taille + ")";
	}
}
