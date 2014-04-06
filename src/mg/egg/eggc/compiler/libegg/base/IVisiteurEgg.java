package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.runtime.libjava.EGGException;

public interface IVisiteurEgg {
	// un seul visiteur d'action par visiteurEgg
	public IVisiteurAction getVisAction();

	// génère main() du compilo
	public void racinec();

	// génère module
	public void racine();

	// génère message
	public void m_entete(String m);

	// génère l'analyseur lexical
	public void lexical();

	// appele à la déclaration du non terminal externe
	public void ex_entete(NON_TERMINAL nt);

	// appele à la création du non terminal
	public void nt_entete(NON_TERMINAL nt);

	// appele à chaque création de règle
	public void regle(REGLE r);

	// génère une règle d'un non terminal
	public void nt_regle(REGLE r);

	// génère le code d'une action semantique
	// appele à la création de l'action
	public void nt_action(ActREGLE a);

	// appele à la création du terminal
	public void t_entete(TERMINAL t);

	// génère le code d'un attribut semantique
	public void t_attribut(TERMINAL t, ATTRIBUT a);

	// génère le code d'un attribut semantique
	public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a);

	public void nt_attribut(ATTRIBUT a);

	// génère le code d'un attribut semantique
	public void nt_globale(NON_TERMINAL nt, GLOB g);

	// génère le code d'un attribut semantique
	public void globale(REGLE r, GLOB g);

	// génère le code jflex d'un caractère
	public String car(String c);

	public void finaliser() throws EGGException;
}
