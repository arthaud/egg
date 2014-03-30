package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.IType;

public interface IEgg {
	public void setCode(String c);

	public String getCode();

	public void maj_code(String code);

	// remplacer une occurence de code par une autre dans le code
	public void replace_code(String code_originel, String string);

	// meta fonctions ... pour generer du code source

	/**
	 * generer la ligne de code pour la declaration d'un attribut
	 * 
	 * @param att
	 *            attribut a declarer
	 * @return
	 */
	public String mkAttribut(ATTRIBUT att);

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
	public String mkPatternAttribut(int sorte, String name, IType type);

	// debut de declaration d'attribut herite
	public String mkInh();

	// debut de declaration d'attribut synthetise
	public String mkSyn();

	// le nom reserve de la regle d'heritage automatique
	public String mkAutoInh();

	// le nom reserve de l'attribut contenant l'analyseur lexical
	public String mkScanner();

	// partie gauche de la regle
	public String mkRuleLeft(String gauche);

	// element de la partie droite de la regle
	public String mkRuleContinue(String elt);

	// fin de la partie droite
	public String mkEndRule();

	/**
	 * definition d'une action
	 * 
	 * @param name
	 *            le nom de l'action (avec # devant)
	 * @param src
	 *            le source de l'action au format suivant IAction
	 * @return
	 */
	public String mkAction(String name, String src);

	// debut des globales
	public String mkStartGlobs();

	// declaration d'une globale
	public String mkGlob(ENTREE e);

	// fin des globales
	public String mkEndGlobs();
}
