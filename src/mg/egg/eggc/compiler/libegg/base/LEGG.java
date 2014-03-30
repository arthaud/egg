package mg.egg.eggc.compiler.libegg.base;

import java.util.Vector;
import java.util.regex.Pattern;

import mg.egg.eggc.compiler.libegg.type.IType;

public class LEGG implements IEgg {

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String c) {
		code = c;
	}

	public void maj_code(String code) {
		this.code = code;
	}

	// remplacer une occurence de code par une autre dans le code
	public void replace_code(String code_originel, String string) {
		this.code = this.code.replace(code_originel, string);
	}

	public LEGG() {
	}

	// meta fonctions ... pour generer du code
	// au format LEGG

	/**
	 * generer la ligne de code pour la declaration d'un attribut
	 * 
	 * @param att
	 *            attribut a declarer
	 * @return
	 */
	public String mkAttribut(ATTRIBUT att) {
		String sorte = "";
		if (att.getSorte() == 0)
			sorte = "inh";
		else
			sorte = "syn";
		Vector<String> idents = (Vector<String>) att.getIdents().clone();
		String code = sorte + " " + att.getNom() + " : "
				+ att.getType().getNom() + " for ";
		code += idents.get(0);
		// dans le cas d'une correction des recursivites gauches ,
		// att.getIdents() peut contenir des doublons du fait des non terminaux
		// refaits
		String elt_a_enlever = idents.get(0);
		while (idents.removeElement(elt_a_enlever))
			;
		while (idents.size() > 0) {
			String elt_a_traiter = idents.get(0);
			code += ", " + elt_a_traiter;
			while (idents.removeElement(elt_a_traiter))
				;
		}
		code += ";";
		return code;
	}

	/**
	 * generer l'expression reguliere permettant de retrouver le code de
	 * declaration d'un attribut
	 * 
	 * @param sorte
	 *            0 si herite, 1 si synthetise
	 * @param name
	 *            nom de l'attribut
	 * @param type
	 *            type de l'attribut
	 * @return la String contenant l'expression reguliere
	 */
	public String mkPatternAttribut(int sorte, String name, IType type) {
		String sorte_str = "";
		if (sorte == 0)
			sorte_str = mkInh();
		else
			sorte_str = mkSyn();
		return Pattern.quote(sorte_str) + "[ \t\n\r]*" + Pattern.quote(name)
				+ "[ \t\n\r]*" + Pattern.quote(":") + "[ \t\n\r]*"
				+ Pattern.quote(type.getNom()) + "[ \t\n\r]+"
				+ Pattern.quote("for") + "[ \t\n\r]+" + "([ ,a-zA-Z0-9]+)"
				+ Pattern.quote(";");
	}

	// debut de declaration d'attribut herite
	public String mkInh() {
		return "inh ";
	}

	// debut de declaration d'attribut synthetise
	public String mkSyn() {
		return "syn ";
	}

	// le nom reserve de la regle d'heritage automatique
	public String mkAutoInh() {
		return "#auto_inh";
	}

	// le nom reserve de l'attribut contenant l'analyseur lexical
	public String mkScanner() {
		return "scanner";
	}

	// partie gauche de la regle
	public String mkRuleLeft(String gauche) {
		return "\n" + gauche + " -> ";
	}

	// element de la partie droite de la regle
	public String mkRuleContinue(String elt) {
		return elt + " ";
	}

	// fin de la partie droite
	public String mkEndRule() {
		return ";\n";
	}

	/**
	 * definition d'une action
	 * 
	 * @param name
	 *            le nom de l'action (avec # devant)
	 * @param src
	 *            le source de l'action au format suivant IAction
	 * @return
	 */
	public String mkAction(String name, String src) {
		return name + "{\n" + src + "\n}";
	}

	// debut des globales
	public String mkStartGlobs() {
		return "global\n";
	}

	// declaration d'une globale
	public String mkGlob(ENTREE e) {
		return e.getNom() + " : " + e.getType().getNom() + ";\n";
	}

	// fin des globales
	public String mkEndGlobs() {
		return "";
	}

}
