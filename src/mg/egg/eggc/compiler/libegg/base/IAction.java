package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.compiler.libegg.type.Resolveur;

public interface IAction {
	// la position dans la regle
	public int getPos();

	public BLOC getBloc();

	public String getCode();

	public Resolveur getResolveur();

	public void setCode(String c);

	// public String getCode_src();

	public void maj_code(String code);

	// public void maj_code_src ( String src);

	// meta fonctions ... pour generer du code source
	// debut d'action
	public String mkStart();

	// fin d'action
	public String mkEnd();

	// debut des locales
	public String mkStartLocs();

	// declaration d'une locale
	public String mkLoc(ENTREE e);

	// affectation
	public String mkCopy(ENTREE e1, ENTREE e2);

	// debut des instructions
	public String mkStartInsts();

	// fin des instructions
	public String mkEndInsts();

	// appel de procedure
	public String mkCall();

	// debut de if
	public String mkStartIf();

	// debut de else d'un if
	public String mkElseIf();

	// debut de elsif d'un if
	public String mkElsifIf();

	// fin d'un if
	public String mkEndIf();

	// debut d'un match
	public String mkStartMatch();

	// cas d'un match
	public String mkWithMatch();

	// else d'un match
	public String mkElseMatch();

	// fin d'un match
	public String mkEndMatch();

	// acces a un attribut de la premiere occurence //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, ATTRIBUT att);

	// acces a un attribut de la i+1eme occurence //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, int i, ATTRIBUT att);

	// acces a un attribut de la premiere occurence (sans specification du type)
	// //@author : ettelcar
	public ENTREE mkAtt(String nom, String string);

	// acces a un attribut de la i+1eme occurence (sans specification du type)
	// //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, int i, String att);

	// l'expression reguliere pour retrouver une variable globale //@author
	// ettelcar
	public String mkPatternGlob_1var2(String var);

	// l'expression reguliere pour retrouver si un attribut du non terminal est
	// present //@author : ettelcar
	public String mkPatternAttribut_1nt2att(String non_terminal);

	// l'expression reguliere pour retrouver si un attribut du non terminal
	// d'indice i est present //@author : ettelcar
	public String mkPatternAttribut_1ntiatt(String non_terminal, int i);

	// l'expression reguliere pour retrouver si un attribut du non terminal est
	// present //@author : ettelcar
	public String mkPatternAttribut_1nt2att3(String non_terminal, String att);

	// l'expression reguliere pour retrouver si un attribut du non terminal
	// d'indice i est present //@author : ettelcar
	public String mkPatternAttribut_1ntiatt2(String non_terminal, int i,
			String att);
}
