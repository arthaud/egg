package mg.egg.eggc.compiler.libegg.java;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.SYMBOLE;
import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.compiler.libegg.base.TERMINAL;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.EGGOptions;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

public class LexJava implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	transient TDS table;

	// le fichier LEX_xxx
	private String nom;

	public String getNom() {
		return "LEX_" + nom;
	}

	public void setNom(String n) {
		nom = "LEX_" + n;
	}

	private StringBuffer corps;

	public StringBuffer getCorps() {
		return corps;
	}

	public void setCorps(String pack, Vector<String> incs) {
		EGGOptions options = table.getOptions();
		// LEX_
		corps = new StringBuffer();
		corps.append("package " + pack + ";\n");
		corps.append("import java.util.Arrays;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.EGGException;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.messages.NLS;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.lex.LEXICAL4;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.lex.LEX_CONTEXTE;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.problem.IProblemReporter;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.problem.IProblem;\n");
		corps.append("import mg.egg.eggc.runtime.libjava.problem.ProblemSeverities;\n");
		// new
		for (Enumeration<String> e = incs.elements(); e.hasMoreElements();) {
			corps.append("import " + (String) e.nextElement() + ";\n");
		}
		corps.append("public class LEX_" + table.getNom()
				+ " extends LEXICAL4  {\n");
		int cpt = 1;
		corps.append(" public static final int EOF = 0 ;\n");
		for (Enumeration<SYMBOLE> e = table.elements(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) o;
				if (t.getType() != TERMINAL.MACRO) {
					corps.append(" public static final int token_" + t.getNom()
							+ " = " + (cpt++) + " ;\n");
				}
			}
		}
		corps.append("  static final int token_autre = " + (cpt++) + " ;\n");

		corps.append("  public static final String[][] tokenImages = {\n");
		corps.append("    {\"<EOF>\"} ,");
		for (Enumeration<SYMBOLE> e = table.elements(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) o;
				switch (t.getType()) {
				case TERMINAL.MACRO:
					break;
				case TERMINAL.SUGAR:
					String er = "{\"" + t.getNom() + "\"}";
					if (t.getNames().size() != 0) {
						StringBuffer sb = new StringBuffer();
						sb.append("   {");
						for (String n : t.getNames())
							sb.append("\"" + n + "\", ");
						sb.append("}");
						er = sb.toString();
					}
					corps.append(er + ",\n");
					break;
				default:
					corps.append("    {\"" + t.getNom() + "\"} ,\n");
				}
				// if (t.getType() != TERMINAL.MACRO) {
				// corps.append(" \"" + t.getNom() + "\" ,\n");
				// }
			}
		}
		corps.append("  } ;\n");
		// corps.append("  int dernier_accepte ;\n");
		corps.append("  private int [] separateurs = { \n");
		boolean premier = true;
		for (Enumeration<SYMBOLE> e = table.elements(); e.hasMoreElements();) {
			SYMBOLE s = (SYMBOLE) e.nextElement();
			if (s instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) s;
				if (t.getType() == TERMINAL.SPACE) {
					if (premier) {
						premier = false;
					} else {
						corps.append(", ");
					}
					corps.append("token_" + t.getNom() + "\n");
				}
			}
		}
		corps.append("    } ;\n");
		corps.append("  public int[] getSeparateurs(){\n    return separateurs;\n    }\n");
		//
		corps.append("  private int [] comments = { \n");
		premier = true;
		for (Enumeration<SYMBOLE> e = table.elements(); e.hasMoreElements();) {
			SYMBOLE s = (SYMBOLE) e.nextElement();
			if (s instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) s;
				if (t.getType() == TERMINAL.COMM) {
					if (premier) {
						premier = false;
					} else {
						corps.append(", ");
					}
					corps.append("token_" + t.getNom() + "\n");
				}
			}
		}
		corps.append("    } ;\n");
		corps.append("  public int[] getComments(){\n    return comments;\n    }\n");
		String le_comment = null;
		for (Enumeration<SYMBOLE> e = table.elements(); e.hasMoreElements();) {
			SYMBOLE s = (SYMBOLE) e.nextElement();
			if (s instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) s;
				if (t.getType() == TERMINAL.COMM) {
					le_comment = t.getNom();
					break;
				}
			}
		}
		if (le_comment != null) {
			corps.append("  private int le_comment = token_" + le_comment
					+ ";\n");
		} else {
			corps.append("  private int le_comment = -1;\n");
		}
		corps.append("  public int getComment(){\n    return le_comment;\n    }\n");
		String lexer_name = options.getLexer().toUpperCase();
		if (!options.getModule()) {
			// le bon
			corps.append("  public LEX_" + table.getNom()
					+ "(IProblemReporter pr, LEX_CONTEXTE lc, int k) {\n");
			corps.append("    super(pr, lc,k);\n");
			corps.append("    dernier_accepte = 0 ;\n");
			corps.append("    analyseur = new " + lexer_name + "_"
					+ table.getNom() + "();\n");
			corps.append("  }\n");
		} else {
			corps.append("  public LEX_" + table.getNom()
					+ "(IProblemReporter pr, LEX_CONTEXTE lc, int k) {\n");
			corps.append("    super(pr , lc,k);\n");
			corps.append("    dernier_accepte = 0 ;\n");
			corps.append("    analyseur = new " + lexer_name + "_"
					+ table.getNom() + "();\n");
			corps.append("  }\n");
		}
		corps.append("  public void setSource ( LEXICAL4 scanner) throws EGGException{\n");
		corps.append("    scanner.analyseur.toContext(scanner.contexte);\n");
		corps.append("    analyseur.fromContext(scanner.contexte);\n");
		corps.append("  }\n");

		corps.append("  public void setReader ( LEXICAL4 scanner) {\n");
		corps.append("    scanner.analyseur.setReader(scanner.contexte.source);\n");
		corps.append("  }\n");

		corps.append("  public void accepter_sucre ( int t ) throws EGGException {\n");
		corps.append("    if ( fenetre[0].code == t ) {\n");
		corps.append("      dernier_accepte = fenetre[0].ligne ;\n");
		corps.append("      recovery = false;\n");
		corps.append("      decaler () ;\n");
		corps.append("    }else {\n");
		// corps
		// .append("      Object as[] = {fenetre[0].getNom(), Arrays.asList(tokenImages[t])};\n");
		corps.append("      _interrompre(IProblem.Syntax,getBeginLine(), "
				+ "I"
				+ nom
				+ "Messages.id_"
				+ nom
				+ "_expected_token, "
				+ nom
				+ "Messages."
				+ nom
				+ "_expected_token,new Object[]{fenetre[0].getNom(), Arrays.asList(tokenImages[t])});\n");
		corps.append("    }\n");
		corps.append("  }\n");
		corps.append("  public void accepter_fds() throws EGGException {\n");
		corps.append("    lit ( 1 ) ;\n");
		corps.append("    if ( fenetre[0].code != EOF ) {\n");
		// corps.append("      Object as[] = {fenetre[0].getNom()};\n");
		corps.append("      _interrompre(IProblem.Syntax,getBeginLine(), "
				+ "I" + nom + "Messages.id_" + nom + "_expected_token, " + nom
				+ "Messages." + nom
				+ "_expected_eof, new Object[]{fenetre[0].getNom()});\n");
		corps.append("      }\n     else {\n");
		corps.append("      dernier_accepte = fenetre[0].ligne ;\n");
		corps.append("      }\n");
		corps.append("    }\n");
		corps.append("  public int ligneDepart () {\n");
		corps.append("    return getBeginLine() + getEndLine() ;\n");
		corps.append("    }\n");
		corps.append("  public void _interrompre (int cat,  int line, int id,  String c , Object [] m )  {\n");
		corps.append("      //recovery = true;\n");
		corps.append("      recovery = false;\n");
		corps.append("      contexte.errors++;\n");
		corps.append("        problemReporter.handle(cat, id,line  , NLS.bind(c,m), getOffset(), getOffset() + getLength() - 1,ProblemSeverities.Error,m);\n");
		corps.append("    }\n");
		corps.append("    public void _signaler ( int cat, int line, int id, String c , Object [] m )  {\n");
		corps.append("        problemReporter.handle(cat , id, line  , NLS.bind(c,m),getOffset(), getOffset() + getLength() - 1,ProblemSeverities.Warning,m);\n");
		corps.append("    }\n");

		corps.append("  }\n");
		// JLEX_
		corpsjlex = new StringBuffer();
		corpsjlex.append("package " + pack + ";\n");
		corpsjlex.append("import mg.egg.eggc.runtime.libjava.lex.*;\n");
		corpsjlex.append("%%\n");
		corpsjlex.append("%{\n");
		corpsjlex.append("%}\n");
		corpsjlex.append("%full\n");
		corpsjlex.append("%line\n");
		corpsjlex.append("%char\n");
		corpsjlex.append("%egg " + table.getNom() + "\n");
		corpsjlex.append("%public\n");
		corpsjlex.append("%eofval{\n");
		corpsjlex.append("\treturn new Yytoken(LEX_" + table.getNom()
				+ ".EOF , \"EOF\" , yyline , yychar , yychar+1 ) ;\n");
		corpsjlex.append("%eofval}\n");
		corpsjlex.append("%%\n");
		for (Enumeration<SYMBOLE> e = table.getLexicaux().elements(); e
				.hasMoreElements();) {
			SYMBOLE s = (SYMBOLE) e.nextElement();
			if (s instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) s;
				switch (t.getType()) {
				case TERMINAL.SPACE:
				case TERMINAL.SUGAR:
				case TERMINAL.TERM:
				case TERMINAL.COMM:
					corpsjlex.append(t.get_expreg()
							+ "\t\t\t {return new Yytoken(LEX_"
							+ table.getNom() + ".token_" + t.getNom()
							+ ", yytext(), yyline, yychar, yychar+1);}\n");
					break;
				default:
					break;
				}
			}
		}
		corpsjlex.append(". \t\t\t{return new Yytoken(LEX_" + table.getNom()
				+ ".token_autre, yytext(), yyline, yychar, yychar+1);}\n");
	}

	// le fichier JLEX_xxx
	public String getJlex() {
		return "JLEX_" + nom;
	}

	private StringBuffer corpsjlex;

	public StringBuffer getCorpsjlex() {
		return corpsjlex;
	}

	/**
	 * 
	 * @param t
	 */
	public LexJava(TDS t) {
		table = t;
		nom = t.getNom();
		// change = false;
	}

	private StringBuffer corpsTerm;

	public StringBuffer getCorpsTerm() {
		return corpsTerm;
	}

	public void setTermCorps(String pack, Vector<String> incs) {
		// EGGOptions options = table.getOptions();
		corpsTerm = new StringBuffer();
		corpsTerm.append("package " + pack + ";\n");
		// new
		for (Enumeration e = incs.elements(); e.hasMoreElements();) {
			corpsTerm.append("import " + (String) e.nextElement() + ";\n");
		}
		corpsTerm.append("import mg.egg.eggc.runtime.libjava.*;\n");
		corpsTerm.append("import mg.egg.eggc.runtime.libjava.lex.*;\n");
		if (table.getDst()) {
			corpsTerm.append("import java.util.List;\n");
			corpsTerm.append("public class T_" + nom
					+ " implements IDstNode {\n");
			corpsTerm.append("  private int sorte;\n");
		} else
			corpsTerm.append("public class T_" + nom + " {\n");
		if (!table.syntaxOnly()) {
			corpsTerm.append("  LEX_" + nom + " att_scanner;\n");
			corpsTerm.append("  String att_txt;\n");
		}
		corpsTerm.append("  LEX_" + nom + " scanner;\n");
		corpsTerm.append("  private String txt;\n");
		corpsTerm.append("  public String getTxt(){return txt;}\n");
		corpsTerm
				.append("  public T_" + nom + "(LEX_" + nom + " scanner ) {\n");
		corpsTerm.append("    this.scanner = scanner ;\n");
		if (!table.syntaxOnly()) {
			corpsTerm.append("    this.att_scanner = scanner ;\n");
		}
		corpsTerm.append("    }\n");
		corpsTerm
				.append("  public void analyser(int t) throws EGGException {\n");
		corpsTerm.append("    scanner.lit ( 1 ) ;\n");
		corpsTerm.append("    txt = scanner.fenetre[0].getNom() ;\n");
		if (!table.syntaxOnly()) {
			corpsTerm.append("    att_txt = txt ;\n");
		}
		if (table.getDst()) {
			corpsTerm.append("    sorte=t;\n");
			corpsTerm.append("    offset= scanner.getOffset(0);\n");
			corpsTerm.append("    length= scanner.getLength(0);\n");
		}
		corpsTerm.append("    scanner.accepter_sucre ( t ) ;\n");
		corpsTerm.append("    }\n");
		// nodes
		if (table.getDst()) {
			corpsTerm.append("  public int getSorte(){return sorte;}\n");
			corpsTerm.append("    private IDstNode parent;\n");
			corpsTerm
					.append("    public void setParent( IDstNode p){parent = p;}\n");
			corpsTerm
					.append("    public IDstNode getParent(){return parent;}\n");
			corpsTerm.append("  public void addChild(IDstNode node){}\n");
			corpsTerm
					.append("  public List<IDstNode> getChildren(){return null;}\n");
			corpsTerm.append("  public boolean isLeaf(){return true;}\n");
			// visit
			corpsTerm
					.append("  public void accept(IDstVisitor visitor){visitor.visit(this);}\n");
			corpsTerm.append("  private int offset;\n");
			corpsTerm.append("  private int length;\n");
			corpsTerm.append("  public int getOffset(){return offset;}\n");
			corpsTerm.append("  public void setOffset(int o){offset = o;}\n");
			corpsTerm.append("  public int getLength(){return length;}\n");
			corpsTerm.append("  public void setLength(int l){length = l;}\n");
			corpsTerm.append("  private boolean malformed = false;\n");
			corpsTerm
					.append("  public void setMalformed(){malformed = true;}\n");
			corpsTerm
					.append("  public boolean isMalformed(){return malformed;}\n");
		}
		corpsTerm.append("    }\n");
	}

	/**
	 * 
	 * @param dir
	 * @throws EGGException
	 */
	public void finaliser(String pack, Vector<String> incs) throws EGGException {
		IEGGCompilationUnit cu = table.getCompilationUnit();
		// le fichier T_lang
		setTermCorps(pack, incs);
		cu.createFile("T_" + nom + ".java", corpsTerm.toString());
		// le fichier JLEX_
		String njlex = getJlex();
		setCorps(pack, incs);
		try {
			mg.egg.eggc.compiler.libegg.jlex.Main jl = new mg.egg.eggc.compiler.libegg.jlex.Main();
			jl.jLexGen(cu, njlex, corpsjlex.toString(), "java");
		} catch (IOException ioe) {
			throw new EGGException(IProblem.Internal,
					ICoreMessages.id_EGG_scanner_error,
					CoreMessages.EGG_scanner_error, "jlex");
		}
		cu.createFile(getNom() + ".java", corps.toString());
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (Enumeration<SYMBOLE> e = table.getLexicaux().elements(); e
				.hasMoreElements();) {
			SYMBOLE s = (SYMBOLE) e.nextElement();
			if (s instanceof TERMINAL) {
				TERMINAL t = (TERMINAL) s;
				sb.append(" --- " + t.getNom() + ", " + t.get_expreg() + "\n");
			}
		}
		return sb.toString();
	}

}
