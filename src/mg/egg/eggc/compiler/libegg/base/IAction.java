package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.Resolveur;

public interface IAction {
	// la position dans la règle
	public int getPos();

	public BLOC getBloc();

	public String getCode();

	public Resolveur getResolveur();

	public void setCode(String c);

	public void maj_code(String code);

	/*
	 * meta fonctions pour générer du code source
	 */

	// début d'action
	public String mkStart();

	// fin d'action
	public String mkEnd();

	// début des locales
	public String mkStartLocs();

	// déclaration d'une locale
	public String mkLoc(ENTREE e);

	// affectation
	public String mkCopy(ENTREE e1, ENTREE e2);

	// début des instructions
	public String mkStartInsts();

	// fin des instructions
	public String mkEndInsts();

	// appel de procedure
	public String mkCall();

	// début de if
	public String mkStartIf();

	// début de else d'un if
	public String mkElseIf();

	// début de elsif d'un if
	public String mkElsifIf();

	// fin d'un if
	public String mkEndIf();

	// début d'un match
	public String mkStartMatch();

	// cas d'un match
	public String mkWithMatch();

	// else d'un match
	public String mkElseMatch();

	// fin d'un match
	public String mkEndMatch();

	// accès à un attribut de la premiere occurence
	public ENTREE mkAtt(String non_terminal, ATTRIBUT att);

	// accès à un attribut de la i+1eme occurence
	public ENTREE mkAtt(String non_terminal, int i, ATTRIBUT att);

	// accès à un attribut de la premiere occurence (sans specification du type)
	public ENTREE mkAtt(String nom, String string);

	// accès à un attribut de la i+1eme occurence (sans specification du type)
	public ENTREE mkAtt(String non_terminal, int i, String att);

	// l'expression regulière pour retrouver une variable globale
	public String mkPatternGlob_1var2(String var);

	// l'expression regulière pour retrouver si un attribut du non terminal est
	// present
	public String mkPatternAttribut_1nt2att(String non_terminal);

	// l'expression regulière pour retrouver si un attribut du non terminal
	// d'indice i est présent
	public String mkPatternAttribut_1ntiatt(String non_terminal, int i);

	// l'expression regulière pour retrouver si un attribut du non terminal est
	// présent
	public String mkPatternAttribut_1nt2att3(String non_terminal, String att);

	// l'expression regulière pour retrouver si un attribut du non terminal
	// d'indice i est present
	public String mkPatternAttribut_1ntiatt2(String non_terminal, int i,
			String att);
}
