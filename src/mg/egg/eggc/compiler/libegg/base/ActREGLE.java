package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

/**
 * Composant d'une règle de production
 *
 * @author MG2005
 * @version
 */
public class ActREGLE extends EltREGLE implements Serializable {
	private static final long serialVersionUID = 1L;

	public static int USER = 0;

	public static int INHS = 3;

	public static int SYNS = 4;

	private int sorte;

	public boolean isUser() {
		return sorte == USER;
	}

	public boolean isAutoInhs() {
		return sorte == INHS;
	}

	public boolean isAutoSyns() {
		return sorte == SYNS;
	}

	public void setUser() {
		sorte = USER;
	}

	public REGLE getRegle() {
		return regle;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String c) {
		code = c;
	}

	// utilisé pour la fabrication automatique de code
	private String code_src;

	public String getCodeSrc() {
		return code_src;
	}

	public void setCodeSrc(String c) {
		code_src = c;
	}

	public void setCodeSrc(TDS table, int offset, int length)
			throws EGGException {
		IEGGCompilationUnit cu = table.getCompilationUnit();
		try {
			code_src = new String(cu.getContents(), offset, length);
		} catch (Exception e) {
			throw new EGGException(IProblem.Internal,
					ICoreMessages.id_EGG_source_read_error,
					CoreMessages.EGG_source_read_error, cu.getFileName());
		}
	}

	public ActREGLE(REGLE r, String n, int p) {
		super(r, n, p);
		code = null;
		code_src = null;
		sorte = USER;
	}

	public ActREGLE(REGLE r, int p, int s) {
		super(r, "#auto_inh", p);
		sorte = s;
		code = null;
		code_src = null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(sorte + ":" + getNom() + "\n" + getCode());
		return sb.toString();
	}

	// INC_COMP
	transient private boolean nomChange = false;

	transient private boolean codeSrcChange = false;

	transient private boolean codeChange = false;

	public void setNomChange(boolean b) {
		nomChange = b;
	}

	public void setCodeSrcChange(boolean b) {
		codeSrcChange = b;
	}

	public void setCodeChange(boolean b) {
		codeChange = b;
	}

	public void setAllChange(boolean b) {
		nomChange = b;
		codeSrcChange = b;
		codeChange = b;
	}

	public boolean getNomChange() {
		return nomChange;
	}

	public boolean getCodeSrcChange() {
		return codeSrcChange;
	}

	public boolean getCodeChange() {
		return codeChange;
	}

	/**
	 * l'actregle a-t-il changé depuis la dernière compilation ?
	 *
	 * @param old
	 */
	public void compare(ActREGLE old) {
		nomChange = !nom.equals(old.nom);
		if (code != null)
			codeChange = !code.equals(old.code);
	}

	public int getSorte() {
		return sorte;
	}

}
