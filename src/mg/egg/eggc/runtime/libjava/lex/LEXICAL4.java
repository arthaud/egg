package mg.egg.eggc.runtime.libjava.lex;

import java.io.IOException;

import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;
import mg.egg.eggc.runtime.libjava.problem.IProblemReporter;

public abstract class LEXICAL4 {

	public UL[] fenetre;

	protected int k;

	protected int lus;

	protected int dernier_accepte;

	protected IProblemReporter problemReporter;

	public IProblemReporter getReporter() {
		return problemReporter;
	}

	// contexte de lecture : necessaire pour un chgt de lexical efficace
	public LEX_CONTEXTE contexte;

	public int getPreviousOffset() {
		if (prevToken == null)
			return 0;
		return prevToken.charBegin;
	}

	public int getPreviousLength() {
		if (prevToken == null)
			return 0;
		return prevToken.text.length();
	}

	public int getOffset(int n) {
		if (fenetre[n] == null)
			return 0;
		return fenetre[n].colonne;
	}

	public int getLength(int n) {
		if (fenetre[n] == null)
			return 0;
		return fenetre[n].nom.length();
	}

	public int getOffset() {
		if (token == null)
			return 0;
		return token.charBegin;
	}

	public int getLength() {
		if (token == null)
			return 0;
		return token.text.length();
	}

	public LEX_ANALYZER analyseur;

	protected int ligneDebut;

	public LEXICAL4(LEX_CONTEXTE lc, int k) {
		contexte = lc;
		this.k = k;
		fenetre = new UL[k];
		lus = 0;
		contexte.consommes = 0;
		for (int i = 0; i < k; i++)
			fenetre[i] = null;
		token = null;
		prevToken = null;
	}

	public LEXICAL4(IProblemReporter pr, LEX_CONTEXTE lc, int k) {
		contexte = lc;
		this.k = k;
		fenetre = new UL[k];
		lus = 0;
		contexte.consommes = 0;
		for (int i = 0; i < k; i++)
			fenetre[i] = null;
		token = null;
		prevToken = null;
		problemReporter = pr;
	}

	private Yytoken token;

	private Yytoken prevToken;

	private UL suivant() throws EGGException {
		try {

			loop: do {
				prevToken = token;
				token = analyseur.yylex();
				// System.err.println(" UL(" + token.index + "): '" + token.text
				// + "'");
				contexte.consommes += token.text.length();
				if (estUnSeparateur(token.index)) {
					continue loop;
				}
				if (estUnCommentaire(token.index)) {
					setCommentaire(token.text);
					continue loop;
				}
				return new UL(token.index, token.text, token.line,
						token.charBegin);
			} while (true);
		} catch (IOException ioe) {
			throw new EGGException(IProblem.Syntax,
					ICoreMessages.id_EGG_scanner_error,
					CoreMessages.EGG_scanner_error, "IOException dans suivant");
			// throw new EGGException(IEGGErrors.scanner_error,
			// "IOException dans suivant");
		}
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	private boolean estUnSeparateur(int n) {
		int seps[] = getSeparateurs();
		for (int i = 0; i < seps.length; i++) {
			if (seps[i] == n) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	protected void decaler() {
		// System.err.println("decaler()");
		// if (recovery) {
		// lus++;
		// return;
		// }
		if (!recovery) {
			for (int i = 1; i < lus; i++)
				fenetre[i - 1] = fenetre[i];
			fenetre[lus - 1] = null;
		}
		lus--;
		if (lus < 0)
			lus = 0;
	}

	protected boolean recovery = false;

	protected boolean onRecovery() {
		return recovery;
	}

	protected boolean onError() {
		return contexte.errors > 0;
	}

	/**
	 * 
	 * @param n
	 * @throws EGGException
	 */
	public void lit(int n) throws EGGException {
		if (n > k)
			n = k;
		for (int i = lus; i < n; i++) {
			fenetre[i] = suivant();
//			System.err.println("fenetre [" + i + "]=" + fenetre[i]);
		}
		// System.err.println("fenetre " + fenetre[0]);
		if (n > lus)
			lus = n;
	}

	private static boolean contains(int[] sync, int t) {
		for (int i : sync) {
			if (i == t)
				return true;
		}
		return false;
	}

	/**
	 * @param n
	 * @param synchro
	 * @throws EGGException
	 */
	public void synchronise(int n, int[] sync) throws EGGException {
		if (sync.length == 0){
			recovery = false;
			decaler();
			return;
		}
//		// for (int i : sync)
//		// System.err.println("ul = " + i);
//		// rechercher une synchro
//		while (!contains(sync, fenetre[0].code)) {
//			// consommer
//			decaler();
//			// remplir
//			lit(n);
//		}
//		recovery = false;
//		lit(n);

		System.err.println("synchro sur " + fenetre[0]);
	}

	/**
	 * 
	 */
	private String comment = null;

	/**
	 * 
	 * @param c
	 */
	public void setCommentaire(String c) {
		// System.err.println("setComm : " + c);
		comment = c;
	}

	/**
	 * 
	 * @return
	 */
	public String getCommentaire() {
		// System.err.println("getComm : ");
		String c = comment;
		comment = null;
		return c;
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	private boolean estUnCommentaire(int n) {
		int seps[] = getComments();
		for (int i = 0; i < seps.length; i++) {
			if (seps[i] == n) {
				return true;
			}
		}
		return false;
	}

	// les abstract ...

	protected abstract int[] getSeparateurs();

	protected abstract int[] getComments();

	protected abstract int getComment();

	public abstract void _interrompre(int cat, int line, int id, String c,
			Object[] m);

	public abstract void _signaler(int cat, int line, int id, String c,
			Object[] m);

	public abstract void accepter_sucre(int t) throws EGGException;

	public abstract void accepter_fds() throws EGGException;

	public int getBeginLine() {
		if (fenetre[0] != null) {
			return fenetre[0].ligne + 1;
		} else {
			return dernier_accepte + 1;
		}
	}

	public int getEndLine() {
		if (fenetre[0] != null) {
			return fenetre[0].ligne + 1;
		} else {
			return dernier_accepte + 1;
		}
	}

	protected abstract int ligneDepart();
}
