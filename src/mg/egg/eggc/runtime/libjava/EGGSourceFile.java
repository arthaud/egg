package mg.egg.eggc.runtime.libjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Properties;

import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

import org.xml.sax.InputSource;

public class EGGSourceFile implements IEGGCompilationUnit {
	private class EGGCompilationState {
		String ntds = null;

		public EGGCompilationState() {
			ntds = genPackageFolder + File.separatorChar + lang + ".tds";
		}

		public void clean() {
		}

		private TDS readTds() throws EGGException {
			TDS oldtds = null;
			try {
				FileInputStream f = new FileInputStream(ntds);
				ObjectInputStream s = new ObjectInputStream(f);
				oldtds = (TDS) s.readObject();
				f.close();
			} catch (FileNotFoundException e) {
				oldtds = null;
			} catch (IOException e) {
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_tds_read_error,
						CoreMessages.EGG_tds_read_error, ntds);
			} catch (ClassNotFoundException e) {
				// Classe serialisée non trouvée
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_tds_deserialisation_error,
						CoreMessages.EGG_tds_deserialisation_error, ntds);
			}
			return oldtds;
		}

		private void writeTds(TDS tds) throws EGGException {
			createGen();
			/* TODO */
		}

		public void createGen() throws EGGException {
			File fgen = new File(genPackageFolder);
			if (!fgen.exists()) {
				System.err.println("Création du repertoire " + genPackageFolder);
				fgen.mkdirs();
			} else if (!fgen.isDirectory()) {
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_not_a_folder_error,
						CoreMessages.EGG_not_a_folder_error, genPackageFolder);
			}
		}

		public void createFile(String name, String contents)
				throws EGGException {
			String filename = genPackageFolder + File.separatorChar + name;
			System.err.println("Création de " + filename);
			try {
				PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
				pw.print(contents);
				pw.close();
			} catch (IOException e) {
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_file_creation_error,
						CoreMessages.EGG_file_creation_error, filename);
			}
		}

		public void updatePropFile(String filename, Properties contents)
				throws EGGException {
			System.err.println("Vérification du fichier de properties " + filename);
			Properties old = new Properties();

			try {
				old.load(new FileInputStream(filename));
			} catch (FileNotFoundException e1) {
			} catch (IOException e1) {
			}

			try {
				PrintWriter pw = new PrintWriter(filename, "ISO-8859-1");
				for (String m : contents.stringPropertyNames()) {
					String om = old.getProperty(m);
					if (om != null) {
						pw.println(m + "=" + om);
					} else {
						pw.println(m + "=" + contents.getProperty(m));
					}
				}
				pw.close();
			} catch (IOException e) {
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_file_creation_error,
						CoreMessages.EGG_file_creation_error, filename);
			}
		}

		public void updatePropFile(Properties contents) throws EGGException {
			updatePropFile(lang + ".properties", contents);
		}

		public void deleteFile(String name) throws EGGException {
			String filename = genPackageFolder + File.separatorChar + name;
			System.err.println("Suppression de " + filename);
			File f = new File(filename);
			f.delete();
		}
	}

	private InputStream fEgg;
	private char[] contents = null;
	private InputStream fXml;
	private String negg;
	private String genPackageFolder;
	private String lang;
	private EGGCompilationState fState;
	private EGGOptions options;
	private boolean ok;

	public EGGSourceFile(String egg) throws EGGException {
		this.negg = egg;
		this.ok = true;

		try {
			// get contents
			fEgg = new FileInputStream(negg);
			contents = Utils.getInputStreamAsCharArray(fEgg, -1, null);

			// create config file
			String xml = getFileName();
			xml = xml.substring(0, xml.length() - 4) + ".ecf";
			fXml = new FileInputStream(xml);

			// get lang
			String aux = negg.substring(0, negg.length() - 4);
			String[] segments = aux.split(File.separator);
			int nbsegments = segments.length;
			lang = segments[nbsegments - 1];

			// create options and analyze
			options = new EGGOptions();
			analyzeOptions();

			// get directory
			genPackageFolder = options.getDirectory().replace('.',
					File.separatorChar);

			// create inital state
			fState = new EGGCompilationState();
		} catch (IOException e) {
			throw new EGGException(IProblem.Internal,
					ICoreMessages.id_EGG_source_contents_error,
					CoreMessages.EGG_source_contents_error, negg);
		}
	}

	public EGGOptions getOptions() {
		return options;
	}

	public void clean() {
		fState.clean();
	}

	public void setState() {
		fState = new EGGCompilationState();
	}

	public TDS readTds() throws EGGException {
		return fState.readTds();
	}

	public void writeTds(TDS tds) throws EGGException {
		fState.writeTds(tds);
	}

	public String getFileName() {
		return negg;
	}

	public char[] getContents() throws IOException {
		return contents;
	}

	public void createGen() throws EGGException {
		fState.createGen();
	}

	public void createFile(String name, String contents) throws EGGException {
		fState.createFile(name, contents);
	}

	public void updatePropFile(String filename, Properties contents)
			throws EGGException {
		fState.updatePropFile(filename, contents);
	}

	public void updatePropFile(Properties contents) throws EGGException {
		fState.updatePropFile(contents);
	}

	public void deleteFile(String name) throws EGGException {
		fState.deleteFile(name);
	}

	public void analyzeOptions() throws EGGException {
		try {
			InputSource is = new InputSource(fXml);
			new EGGOptionsAnalyzer(this).analyse(is);
		} catch (Exception e) {
			throw new EGGException(IProblem.Internal,
					ICoreMessages.id_EGG_config_file_error,
					CoreMessages.EGG_config_file_error, lang + ".ecf");
		}
	}

	@Override
	public boolean isOk() {
		return ok;
	}

	@Override
	public void setOk(boolean ok) {
		this.ok = ok;
	}
}
