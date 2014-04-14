package mg.egg.eggc.compiler.libegg.base;

import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.IType;

public class VisiteurActionNull implements IVisiteurAction {

	public VisiteurActionNull(TDS t) {
	}

	public String insts() {
		return "";
	}

	public String locales() {
		return "";
	}

	public String aff(ENTREE entree, String code) {
		return "";
	}

	public String appel(ENTREE entree, String f, Vector<String> args) {
		return "";
	}

	public String nouveau(ENTREE entree, Vector<String> args) {
		return "";
	}

	public String nouveau(IType type, Vector<String> args) {
		return "";
	}

	public String ecrire(String code) {
		return "";
	}

	public String fatal(String id, Vector<String> args) {
		return "";
	}

	public String signaler(String id, Vector<String> args) {
		return "";
	}

	public String ifExpr(String e, String a, String s) {
		return "";
	}

	public String ifSinonSi(String e, String a, String s) {
		return "";
	}

	public String ifSinon(String code) {
		return "";
	}

	public String ifFin() {
		return "";
	}

	public String matchVarAvec(ENTREE entree, String nom, String w, String ws) {
		return "";
	}

	public String matchSi(ENTREE entree, String nom, String w, String ws) {
		return "";
	}

	public String matchSinonSi(ENTREE entree, String nom, String w, String ws) {
		return "";
	}

	public String matchSinonSi(ENTREE entree, IType t, String w, String ws) {
		return "";
	}

	public String matchVarAvec(ENTREE entree, IType t, String w, String ws) {
		return "";
	}

	public String matchSi(ENTREE entree, IType t, String w, String ws) {
		return "";
	}

	public String matchSinon(String code) {
		return "";
	}

	public String matchFin() {
		return "";
	}

	public String opAdd(String avant, String nom, String code) {
		return "";
	}

	public String vide() {
		return "";
	}

	public String vrai() {
		return "";
	}

	public String faux() {
		return "";
	}

	public String entier(String txt) {
		return "";
	}

	public String moins(String txt) {
		return "";
	}

	public String reel(String txt) {
		return "";
	}

	public String chaine(String ch) {
		return "";
	}

	public String decl(ENTREE entree) {
		return "";
	}

	public void transtyper(ENTREE entree, IType t) {
	}

	public void detranstyper(ENTREE entree) {
	}

	public String var(ENTREE entree) {
		return "";
	}

	public String fct(String code, String txt, Vector<String> args) {
		return "";
	}

	public String non(String code) {
		return "";
	}

	public String opMul(String avant, String nom, String code) {
		return "";
	}

	public String car(String c) {
		return "";
	}

	public String indent() {
		return "";
	}

	public void incIndent() {
	}

	public void decIndent() {
	}

	public void resetIndent() {
	}

	public String exc(String code, String cinsts) {
		return "";
	}

}
