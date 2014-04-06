package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.IType;

public class ATTRIBUT implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean builtin;

	public boolean get_builtin() {
		return builtin;
	}

	public void set_builtin() {
		builtin = true;
	}

	private String comm;

	public String getComm() {
		return comm;
	}

	/**
	 * Le nom de l'attribut.
	 */
	private String nom;

	public String getNom() {
		return nom;
	}

	/**
	 * La sorte de l'attribut. <BR>
	 * peut valoir HER ou SYN
	 *
	 * @see ATTRIBUT#HER
	 * @see ATTRIBUT#SYN
	 */
	private int sorte;

	public int getSorte() {
		return sorte;
	}

	/**
	 * Le type de l'attribut.
	 */
	private IType type;

	public IType getType() {
		return type;
	}

	/**
	 * Le num&eacute;ro de l'attribut.
	 */
	private int numero;

	public int getNumero() {
		return numero;
	}

	/**
	 * La liste des symboles dont il est un attribut.
	 */
	private Vector<String> idents;

	public Vector<String> getIdents() {
		return idents;
	}

	/**
	 * Attribut h&eacute;rit&eacute;.
	 */
	public final static int HER = 0;

	/**
	 * Attribut synth&eacute;tis&eacute;.
	 */
	public final static int SYN = 1;

	public ATTRIBUT(int sorte, String nom, IType type) {
		this(sorte, nom, type, "");
	}

	/**
	 * Construit un attribut.
	 *
	 * @param numero
	 *	        le num&eacute;ro
	 * @param sorte
	 *	        la sorte de l'attribut
	 * @param nom
	 *	        le nom de l'attribut
	 * @param type
	 *	        le type de l'attribut
	 */
	public ATTRIBUT(int sorte, String nom, IType type, String c) {
		this.builtin = false;
		this.sorte = sorte;
		this.nom = nom;
		this.type = type;
		this.comm = c;
		idents = new Vector<String>();
		numero = 0;
	}

	/**
	 * Renvoie une cha&icirc;ne repr&eacute;sentant l'attribut.
	 *
	 * @return la cha&icirc;ne repr&eacute;sentant l'attribut.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		Enumeration<String> e = idents.elements();
		if (e.hasMoreElements()) {
			sb.append(e.nextElement());
			while (e.hasMoreElements())
				sb.append(" , " + e.nextElement());
		} else {
		}
		return "Attribut #"
				+ numero
				+ " : "
				+ nom
				+ " ( "
				+ ((sorte == HER) ? ("herite")
						: ((sorte == SYN) ? ("synthetise") : ("incorrect")))
				+ " )\n" + "\ttype : " + type.getNom() + "\n" + "\tpour : "
				+ sb.toString() + "\n";
	}

	/**
	 * Ajoute un nouvel &eacute;l&eacute;ment &agrave; la liste des
	 * symb&ocirc;les dont il est un attribut.
	 *
	 * @param nom
	 *	        le nom du symb&ocirc;le
	 */
	public void ajouter_ident(String nom) {
		idents.addElement(nom);
	}

	public boolean ajouter_identificateur(String nom) {
		for (Enumeration<String> e = idents.elements(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof String) {
				if (((String) o).equals(nom)) {
					return false;
				}
			}
		}
		idents.addElement(nom);
		return true;
	}

	/**
	 * Retourne l'attribut courant si nom est un symb&ocirc;le ayant l'attribut
	 * courant comme attribut.
	 *
	 * @param nom
	 *	        le nom du symb&ocirc;le
	 */
	public ATTRIBUT renvoie_attribut(String nom) {
		for (Enumeration<String> e = idents.elements(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof String) {
				String s = (String) o;
				if (s.equals(nom))
					return this;
			}
		}
		return null;
	}

	// INC_COMP
	transient private boolean nomChange = false;

	transient private boolean sorteChange = false;

	transient private boolean typeChange = false;

	transient private boolean commChange = false;

	public void setNomChange(boolean b) {
		nomChange = b;
	}

	public void setSorteChange(boolean b) {
		sorteChange = b;
	}

	public void setTypeChange(boolean b) {
		typeChange = b;
	}

	public void setCommChange(boolean b) {
		commChange = b;
	}

	public void setAllChange(boolean b) {
		nomChange = b;
		sorteChange = b;
		typeChange = b;
		commChange = b;
	}

	public boolean getNomChange() {
		return nomChange;
	}

	public boolean getSorteChange() {
		return sorteChange;
	}

	public boolean getTypeChange() {
		return typeChange;
	}

	public boolean getCommChange() {
		return commChange;
	}

	/**
	 * l'attribut a-t-il change depuis la derniere compil ?
	 *
	 * @param old
	 */
	public void compare(ATTRIBUT old) {
		nomChange = !nom.equals(old.nom);
		sorteChange = sorte != old.sorte;
		typeChange = !type.getNom().equals(old.type.getNom());
		if (comm != null)
			commChange = !comm.equals(old.comm);
	}

}
