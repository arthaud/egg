package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.runtime.libjava.EGGException;

public class VisiteurEggNull implements IVisiteurEgg {

	private IVisiteurAction vact;

	public VisiteurEggNull(TDS t) {
		vact = new VisiteurActionNull(t);
	}

	public IVisiteurAction getVisAction() {
		return vact;
	}

	public void racinec() {
	}

	public void racine() {
	}

	public void m_entete(String m) {
	}

	public void lexical() {
	}

	public void ex_entete(NON_TERMINAL nt) {
	}

	public void nt_entete(NON_TERMINAL nt) {
	}

	public void regle(REGLE r) {
	}

	public void nt_regle(REGLE r) {
	}

	public void nt_action(ActREGLE a) {
	}

	public void t_entete(TERMINAL t) {
	}

	public void t_attribut(TERMINAL t, ATTRIBUT a) {
	}

	public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a) {
	}

	public void nt_attribut(ATTRIBUT a) {
	}

	public void nt_globale(NON_TERMINAL nt, GLOB g) {
	}

	public void globale(REGLE r, GLOB g) {
	}

	public String car(String c) {
		return null;
	}

	public void finaliser() throws EGGException {
	}

}
