package lib;

public class CHAMP {
	private String nom;
	private DTYPE type;

	public String getNom() {
		return nom;
	}

	public DTYPE getType() {
		return type;
	}

	public CHAMP(String nom, DTYPE type) {
		super();
		this.nom = nom;
		this.type = type;
	}

	public String toString(){
		return nom + ": " + type;
	}
}
