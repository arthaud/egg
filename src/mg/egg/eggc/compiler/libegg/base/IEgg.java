package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.IType;

public interface IEgg {
	public void setCode(String c);

	public String getCode();

	public void maj_code(String code);

	// remplacer une occurence de code par une autre dans le code
	public void replace_code(String code_originel, String string);

	/*
	 * meta fonctions pour générer du code source
	 */

	/**
	 * générer la ligne de code pour la déclaration d'un attribut
	 *
	 * @param att attribut à déclarer
	 * @return
	 */
	public String mkAttribut(ATTRIBUT att);

	/**
	 * générer l'expression regulière permettant de retrouver le code de
	 * déclaration d'un attribut
	 *
	 * @param sorte
	 *	        0 si herite, 1 si synthetise
	 * @param name
	 *	        nom de l'attribut
	 * @param type
	 *	        type de l'attribut
	 * @return la String contenant l'expression reguliere
	 */
	public String mkPatternAttribut(int sorte, String name, IType type);

	// début de déclaration d'attribut herite
	public String mkInh();

	// début de déclaration d'attribut synthetise
	public String mkSyn();

	// le nom réservé de la règle d'heritage automatique
	public String mkAutoInh();

	// le nom réservé de l'attribut contenant l'analyseur lexical
	public String mkScanner();

	// partie gauche de la règle
	public String mkRuleLeft(String gauche);

	// élément de la partie droite de la règle
	public String mkRuleContinue(String elt);

	// fin de la partie droite
	public String mkEndRule();

	/**
	 * définition d'une action
	 *
	 * @param name
	 *	        le nom de l'action (avec # devant)
	 * @param src
	 *	        le source de l'action au format suivant IAction
	 * @return
	 */
	public String mkAction(String name, String src);

	// début des globales
	public String mkStartGlobs();

	// déclaration d'une globale
	public String mkGlob(ENTREE e);

	// fin des globales
	public String mkEndGlobs();
}
