package mg.egg.eggc.compiler.libegg.base;

import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.IType;

public interface IVisiteurAction {
	public String insts();

	public String locales();

	public String aff(ENTREE entree, String code);

	public String appel(ENTREE entree, String f, Vector<String> args);

	public String nouveau(ENTREE entree, Vector<String> args);

	public String nouveau(IType type, Vector<String> args);

	public String ecrire(String code);

	public String fatal(String id, Vector<String> args);

	public String signaler(String id, Vector<String> args);

	public String ifExpr(String e, String a, String s);

	public String ifSinonSi(String e, String a, String s);

	public String ifSinon(String code);

	public String ifFin();

	public String matchVarAvec(ENTREE entree, String nom, String w, String ws);

	public String matchSi(ENTREE entree, String nom, String w, String ws);

	public String matchSinonSi(ENTREE entree, String nom, String w, String ws);

	public String matchSinonSi(ENTREE entree, IType t, String w, String ws);

	public String matchVarAvec(ENTREE entree, IType t, String w, String ws);

	public String matchSi(ENTREE entree, IType t, String w, String ws);

	public String matchSinon(String code);

	public String matchFin();

	public String vide();

	public String vrai();

	public String faux();

	public String entier(String txt);

	public String moins(String txt);

	public String reel(String txt);

	public String chaine(String ch);

	public String decl(ENTREE entree);

	public String instanceOf(String code, IType t);

	public void transtyper(ENTREE entree, IType t);

	public void detranstyper(ENTREE entree);

	public String var(ENTREE entree);

	public String fct(String code, String txt, Vector<String> args);

	public String non(String code);

	public String opBool(String avant, String nom, String code);

	public String opComp(String avant, String nom, String code);

	public String opAdd(String avant, String nom, String code);

	public String opMul(String avant, String nom, String code);

	// genere le code jflex d'un caractere
	public String car(String c);

	public String indent();

	public void incIndent();

	public void decIndent();

	public void resetIndent();
}
