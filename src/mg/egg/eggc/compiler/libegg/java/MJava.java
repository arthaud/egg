package mg.egg.eggc.compiler.libegg.java;

import java.io.Serializable;
import java.util.Properties;

import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.runtime.libjava.EGGException;

public class MJava implements Serializable {
	private static final long serialVersionUID = 1L;
	Properties properties;
	transient TDS table;
	private String nom;
	public String getNom() {
		return nom;
	}

	private StringBuffer entete;

	public StringBuffer getEntete() {
		return entete;
	}

	private StringBuffer ientete;

	public StringBuffer getIEntete() {
		return ientete;
	}

	public void setEntete(String pack) {
		entete = new StringBuffer();
		entete.append("package " + pack + ";\n");
		entete.append("import java.util.ResourceBundle;\n");
		entete.append("import mg.egg.eggc.runtime.libjava.messages.NLS;\n");
		entete.append("public class " + nom + " extends NLS {\n");
		entete.append("  private static final long serialVersionUID = 1L;\n");
		entete.append("  private static final String BUNDLE_NAME = \"");
		if (!"".equals(table.prefix()))
			entete.append(table.prefix() + ".");
		entete.append(table.getNom() + "\";\n");
		entete.append("  private " + nom + "() {\n");
		entete.append("  // Do not instantiate\n");
		entete.append("  }\n");
		entete.append("  static {\n");
		entete.append("  NLS.initializeMessages(BUNDLE_NAME, " + pack + "."
				+ nom + ".class);\n");
		entete.append("  }\n");
		entete.append("  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);\n");
		entete.append("  public static ResourceBundle getResourceBundle() {\n");
		entete.append("  return RESOURCE_BUNDLE;\n");
		entete.append("  }		\n");
		entete.append("\n");
		ientete = new StringBuffer();
		ientete.append("package " + pack + ";\n");
		ientete.append("public interface I" + nom + " {\n");
		ientete.append("\n");
	}

	public MJava(TDS t) {
		table = t;
		nom = t.getNom() + "Messages";
		properties = t.getProperties();
		properties.put(t.getNom() + "_expected_token", "Terminal attendu {1} au lieu de {0}.");
		properties.put(t.getNom() + "_unexpected_token",
				"Terminal inattendu {0}.");
		properties.put(t.getNom() + "_expected_eof", "Fin de source attendue pres de {0}.");
	}

	public void finaliser(String pack) throws EGGException {
		setEntete(pack);
		// 0 reserved for CoreMessages
		int i = (Math.abs(nom.hashCode() % 100) + 1) << 16;
		for (Object m : properties.keySet()) {
			entete.append("  public static  String " + m + ";\n");
			ientete.append("  public static final int id_" + m + " = " + i++ + ";\n");
		}
		entete.append("  }\n");
		ientete.append("  }\n");
		table.getCompilationUnit().createFile(nom + ".java", entete.toString());
		table.getCompilationUnit().createFile("I" + nom + ".java",
				ientete.toString());
		table.getCompilationUnit().updatePropFile(properties);
	}
}
