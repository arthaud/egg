package mg.egg.eggc.compiler.libegg.base;

import java.util.regex.Pattern;

import mg.egg.eggc.compiler.libegg.type.Resolveur;

public class LACT implements IAction {
	// la position dans la regle
	private int pos;

	public int getPos() {
		return pos;
	}

	private BLOC bloc;

	public BLOC getBloc() {
		return bloc;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public Resolveur getResolveur() {
		return bloc.getLocs().getResolveur();
	}

	public void setCode(String c) {
		code = c;
	}

	// private String code_src ;
	// public String getCode_src(){
	// return code_src;
	// }

	public void maj_code(String code) {
		this.code = code;
	}

	// public void maj_code_src ( String src ) {
	// this.code_src = src ;
	// }

	public LACT(TDS_ACTION m, int p) {
		pos = p;
		bloc = new BLOC(m);
		code = null;
		// code_src = null;
	}

	public LACT() {
	}

	// meta fonctions ... pour generer du code
	// au format LACTION

	// debut d'action
	public String mkStart() {
		return "";
	}

	// fin d'action
	public String mkEnd() {
		return "";
	}

	// debut des locales
	public String mkStartLocs() {
		return "local";
	}

	// declaration d'une locale
	public String mkLoc(ENTREE e) {
		return e.getNom() + " : " + e.getType().getNom() + ";";
	}

	// affectation
	public String mkCopy(ENTREE e1, ENTREE e2) {
		return e1.getNom() + " := " + e2.getNom() + ";\n";
	}

	// debut des instructions
	public String mkStartInsts() {
		return "do";
	}

	// fin des instructions
	public String mkEndInsts() {
		return "end";
	}

	// appel de procedure
	public String mkCall() {
		return "call ";
	}

	// debut de if
	public String mkStartIf() {
		return "if ";
	}

	// debut de else d'un if
	public String mkElseIf() {
		return "else ";
	}

	// debut de elsif d'un if
	public String mkElsifIf() {
		return "elseif  ";
	}

	// fin d'un if
	public String mkEndIf() {
		return "end";
	}

	// debut d'un match
	public String mkStartMatch() {
		return "match ";
	}

	// cas d'un match
	public String mkWithMatch() {
		return "with ";
	}

	// else d'un match
	public String mkElseMatch() {
		return "else ";
	}

	// fin d'un match
	public String mkEndMatch() {
		return "end";
	}

	// acces a un attribut de la premiere occurence //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, ATTRIBUT att) {
		return new ENTREE(non_terminal + "^" + att.getNom(), att.getType());
	}

	// acces a un attribut de la i+1eme occurence //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, int i, ATTRIBUT att) {
		if (i == 0)
			return new ENTREE(non_terminal + "^" + att.getNom(), att.getType());
		else
			return new ENTREE(non_terminal + i + "^" + att.getNom(), att
					.getType());
	}

	// acces a un attribut de la premiere occurence (sans specification du type)
	// //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, String att) {
		return new ENTREE(non_terminal + "^" + att);
	}

	// acces a un attribut de la i+1eme occurence (sans specification du type)
	// //@author : ettelcar
	public ENTREE mkAtt(String non_terminal, int i, String att) {
		if (i == 0)
			return new ENTREE(non_terminal + "^" + att);
		else
			return new ENTREE(non_terminal + i + "^" + att);
	}

	// l'expression reguliere pour retrouver une variable globale //@author
	// ettelcar
	public String mkPatternGlob_1var2(String var) {
		return "([^a-zA-Z0-9\\^])" + Pattern.quote(var) + "([^a-zA-Z0-9])";
	}

	// l'expression reguliere pour retrouver si un attribut du non terminal est
	// present //@author : ettelcar
	public String mkPatternAttribut_1nt2att(String non_terminal) {
		return "([^a-zA-Z0-9])" + Pattern.quote(non_terminal) + "([1-9][0-9]*)"
				+ Pattern.quote("^");
	}

	// l'expression reguliere pour retrouver si un attribut du non terminal
	// d'indice i est present //@author : ettelcar
	public String mkPatternAttribut_1ntiatt(String non_terminal, int i) {
		return "([^a-zA-Z0-9])" + Pattern.quote(non_terminal + i + "^");
	}

	// l'expression reguliere pour retrouver si un attribut du non terminal est
	// present //@author : ettelcar
	public String mkPatternAttribut_1nt2att3(String non_terminal, String att) {
		return "([^a-zA-Z0-9])" + Pattern.quote(non_terminal) + "([1-9][0-9]*)"
				+ Pattern.quote("^" + att) + "([^a-zA-Z0-9])";
	}

	// l'expression reguliere pour retrouver si un attribut du non terminal
	// d'indice i est present //@author : ettelcar
	public String mkPatternAttribut_1ntiatt2(String non_terminal, int i,
			String att) {
		if (i == 0)
			return "([^a-zA-Z0-9])" + Pattern.quote(non_terminal + "^" + att)
					+ "([^a-zA-Z0-9])";
		else
			return "([^a-zA-Z0-9])"
					+ Pattern.quote(non_terminal + i + "^" + att)
					+ "([^a-zA-Z0-9])";
	}
}
