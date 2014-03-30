package mg.egg.eggc.runtime.libjava;

import java.io.Serializable;
import java.util.Vector;

public class EGGOptions implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean auto_att;

	private String version;

	private int k;

	private boolean module;

	private String glang;

	private String lexer;

	private String directory;

	private String prefix;

	private boolean typage;

	private boolean dst;

	private boolean syntaxOnly;

	private boolean main;
	private boolean fatal;

	private Vector<String> libs;

	public EGGOptions() {
		k = 1;
		version = "0.0.0";
		module = false;
		glang = "java";
		lexer = "jlex";
		typage = false;
		dst = false;
		syntaxOnly = false;
		main = true;
		prefix = "";
		libs = new Vector<String>();
		fatal = false;
	}

	public String getVersion() {
		return version;
	}

	public int getK() {
		return k;
	}

	public boolean getAutoAtt() {
		return auto_att;
	}

	public void setAuto(boolean v) {
		auto_att = v;
	}

	public void setVersion(String v) {
		version = v;
	}

	public void setK(int k2) {
		k = k2;
	}

	public boolean getModule() {
		return module;
	}

	public void setModule(boolean module) {
		this.module = module;
	}

	public String getLang() {
		return glang;
	}

	public void setLang(String lang) {
		glang = lang;
	}

	public String getLexer() {
		return lexer;
	}

	public void setLexer(String lexer) {
		this.lexer = lexer;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setProject(String prefix) {
		this.prefix = prefix;
	}

	public boolean hasTypage() {
		return typage;
	}

	public void setTypage(boolean typage) {
		this.typage = typage;
	}

	public boolean getDst() {
		return dst;
	}

	public void setDst(boolean dst) {
		this.dst = dst;
	}

	public boolean syntaxOnly() {
		return syntaxOnly;
	}

	public void setSyntaxOnly(boolean syntaxOnly) {
		this.syntaxOnly = syntaxOnly;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public boolean getMain() {
		return main;
	}

	public boolean getFatal() {
		return fatal;
	}

	public void setFatal(boolean fatal) {
		this.fatal = fatal;
	}

	public void addLib(String lib) {
		if (!libs.contains(lib))
			libs.add(lib);
	}

	public Vector<String> getLibs() {
		return libs;
	}

	public boolean verifier() {
		return ("java".equals(glang) || "cpp".equals(glang)
				|| "latex".equals(glang) || "egg".equals(glang) || "no"
				.equals(glang))
				&& ("jlex".equals(lexer) || "external".equals(lexer)) && k > 0;
	}

	// COMP INC
	public boolean egal(EGGOptions old) {
		// System.err.println("Options old = " + old);
		// System.err.println("Options new = " + this);
		boolean eg = k == old.k && auto_att == old.auto_att
				&& version.equals(old.version) && glang.equals(old.glang)
				&& dst == old.dst && typage == old.typage
				&& syntaxOnly == old.syntaxOnly
				&& directory.equals(old.directory) && prefix.equals(old.prefix)
				&& lexer.equals(old.lexer) && module == old.module
				&& main == old.main && libs.equals(old.getLibs());
		// System.err.println("Options ************** eg = " + eg);
		return eg;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("k = " + k + "\n");
		sb.append("auto = " + auto_att + "\n");
		sb.append("version = " + version + "\n");
		sb.append("lang = " + glang + "\n");
		sb.append("dst = " + dst + "\n");
		sb.append("typage = " + typage + "\n");
		sb.append("syntaxOnly = " + syntaxOnly + "\n");
		sb.append("directory = " + directory + "\n");
		sb.append("prefix = " + prefix + "\n");
		sb.append("lexer = " + lexer + "\n");
		sb.append("module = " + module + "\n");
		sb.append("main = " + main + "\n");
		sb.append("libs = " + libs + "\n");

		return sb.toString();
	}
}
