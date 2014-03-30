package mg.egg.eggc.compiler.libegg.base;

import mg.egg.eggc.runtime.libjava.EGGException;

public interface IVisiteurEgg {
	// un seul visiteur d'action par visiteurEgg
	public IVisiteurAction getVisAction();

	// genere main() du compilo
	public void racinec();

	// genere module
	public void racine();

	// // genere messages
	// public void messages();

	// genere message
	public void m_entete(String m);

	// genere l'analyseur lexical
	public void lexical();

	// appele à la declaration du non terminal externe
	public void ex_entete(NON_TERMINAL nt);

	// appele à la creation du non terminal
	public void nt_entete(NON_TERMINAL nt);

	// appele a chaque creation de regle
	public void regle(REGLE r);

	// genere une regle d'un non terminal
	public void nt_regle(REGLE r);

	// genere le code d'une action semantique
	// appele a la creation de l'action
	public void nt_action(ActREGLE a);

	// appele a la creation du terminal
	public void t_entete(TERMINAL t);

	// genere le code d'un attribut semantique
	public void t_attribut(TERMINAL t, ATTRIBUT a);

	// genere le code d'un attribut semantique
	public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a);

	public void nt_attribut(ATTRIBUT a);

	// genere le code d'un attribut semantique
	public void nt_globale(NON_TERMINAL nt, GLOB g);

	// genere le code d'un attribut semantique
	public void globale(REGLE r, GLOB g);

	// genere le code jflex d'un caractere
	public String car(String c);

	// finaliser = ecrire ds un fichier par exemple
	public void finaliser() throws EGGException;
}
