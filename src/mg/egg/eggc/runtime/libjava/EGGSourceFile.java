package mg.egg.eggc.runtime.libjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
				// System.err.println(oldtds);
			} catch (FileNotFoundException e) {
				oldtds = null;
			} catch (IOException e) {
				// Erreur de lecture.
				// e.printStackTrace();
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_tds_read_error,
						CoreMessages.EGG_tds_read_error, ntds);
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				// Classe serialisee non trouvee
				// throw new EGGException(IProblem.Internal,
				// ICoreMessages.id_EGG_file_read_error,
				// CoreMessages.EGG_file_read_error, ntds);
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_tds_deserialisation_error,
						CoreMessages.EGG_tds_deserialisation_error, ntds);
			}
			return oldtds;
		}

		private void writeTds(TDS tds) throws EGGException {
			// try {
			// creation du repertoire de generation, s'il n'existe pas deja
			createGen();
			// FileOutputStream f = new FileOutputStream(ntds);
			// ObjectOutputStream s = new ObjectOutputStream(f);
			// s.writeObject(tds);
			// s.flush();
			// f.close();
			// } catch (IOException e) {
			// // e.printStackTrace();
			// // System.err.println("Erreur ecriture de serialisation" +
			// // ntds);
			// // throw new EGGException(IProblem.Internal,
			// // ICoreMessages.id_EGG_file_write_error,
			// // CoreMessages.EGG_file_write_error, ntds);
			// throw new EGGException(IProblem.Internal,
			// ICoreMessages.id_EGG_tds_serialisation_error,
			// CoreMessages.EGG_tds_serialisation_error, ntds);
			// }
		}

		public void createGen() throws EGGException {
			// String n = options.getDirectory();
			String gen = genPackageFolder;
//			System.err.println("create gen package " + gen);
			File fgen = new File(gen);
			if (!fgen.exists()) {
				// creation
				System.err.println("Creation du repertoire " + gen);
				fgen.mkdirs();
			} else if (!fgen.isDirectory()) {
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_not_a_folder_error,
						CoreMessages.EGG_not_a_folder_error, gen);
			}
			// // create properties package
			// String prop = "properties";
			// System.err.println("create properties package " + prop);
			// File fprop = new File(prop);
			// if (!fprop.exists()) {
			// // creation
			// // System.err.println("Creation du repertoire " + prop);
			// fprop.mkdirs();
			// } else if (!fprop.isDirectory()) {
			// throw new EGGException(IEGGErrors.not_a_folder_error, prop);
			// }
		}

		public void createFile(String name, String contents)
				throws EGGException {
			// String filename = options.getDirectory() + File.separatorChar
			// + name;
			String filename = genPackageFolder + File.separatorChar + name;
			System.err.println("Creation de " + filename);
			try {
				PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
				pw.print(contents);
				pw.close();
			} catch (IOException e) {
				// e.printStackTrace();
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_file_creation_error,
						CoreMessages.EGG_file_creation_error, filename);
			}
		}

		public void updatePropFile(String name, Properties contents)
				throws EGGException {

			// String filename = options.getDirectory() + File.separatorChar
			// + name;
			String filename = "properties" + File.separatorChar + name;
			System.err
					.println("Maj ou Creation de properties file " + filename);
			Properties old = new Properties();
			try {
				old.load(new FileReader(filename));
			} catch (FileNotFoundException e1) {
			} catch (IOException e1) {
			}
			try {
				PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
				for (Object m : contents.keySet()) {
					Object om = old.get(m);
					if (om != null) {
						pw.println(m + "=" + om);
					} else {
						pw.println(m + "=" + contents.get(m));
					}
				}
				pw.close();
			} catch (IOException e) {
				// e.printStackTrace();
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_file_creation_error,
						CoreMessages.EGG_file_creation_error, filename);
				// throw new EGGException(IEGGErrors.file_creation_error,
				// filename);
			}
		}

		public void updatePropFile(Properties contents) throws EGGException {

			String filename = lang + ".properties";
			// System.err
			// .println("Maj ou Creation de properties file " + filename);
			Properties old = new Properties();
			try {
				old.load(new FileReader(filename));
			} catch (FileNotFoundException e1) {
			} catch (IOException e1) {
			}
			try {
				PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
				for (Object m : contents.keySet()) {
					Object om = old.get(m);
					if (om != null) {
						pw.println(m + "=" + om);
					} else {
						pw.println(m + "=" + contents.get(m));
					}
				}
				pw.close();
			} catch (IOException e) {
				// e.printStackTrace();
				throw new EGGException(IProblem.Internal,
						ICoreMessages.id_EGG_file_creation_error,
						CoreMessages.EGG_file_creation_error, filename);
				// throw new EGGException(IEGGErrors.file_creation_error,
				// filename);
			}
		}

		public void deleteFile(String name) throws EGGException {
			// String filename = options.getDirectory() + File.separatorChar
			// + name;
			String filename = genPackageFolder + File.separatorChar + name;
			// System.err.println("Suppression de " + filename);
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
		// get options
		try {
			// get contents
			fEgg = new FileInputStream(negg);
			contents = getInputStreamAsCharArray(fEgg, -1, null);
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

	// from Eclipse ...

	private static char[] getInputStreamAsCharArray(InputStream stream,
			int length, String encoding) throws IOException {
		InputStreamReader reader = null;
		reader = encoding == null ? new InputStreamReader(stream)
				: new InputStreamReader(stream, encoding);
		char[] contents;
		if (length == -1) {
			contents = new char[0];
			int contentsLength = 0;
			int amountRead = -1;
			do {
				int amountRequested = Math.max(stream.available(), 8192);
				// resize contents if needed
				if (contentsLength + amountRequested > contents.length) {
					System.arraycopy(contents, 0,
							contents = new char[contentsLength
									+ amountRequested], 0, contentsLength);
				}

				// read as many chars as possible
				amountRead = reader.read(contents, contentsLength,
						amountRequested);

				if (amountRead > 0) {
					// remember length of contents
					contentsLength += amountRead;
				}
			} while (amountRead != -1);

			// Do not keep first character for UTF-8 BOM encoding
			int start = 0;
			if (contentsLength > 0 && "UTF-8".equals(encoding)) { //$NON-NLS-1$
				if (contents[0] == 0xFEFF) { // if BOM char then skip
					contentsLength--;
					start = 1;
				}
			}
			// resize contents if necessary
			if (contentsLength < contents.length) {
				System.arraycopy(contents, start,
						contents = new char[contentsLength], 0, contentsLength);
			}
		} else {
			contents = new char[length];
			int len = 0;
			int readSize = 0;
			while ((readSize != -1) && (len != length)) {
				// See PR 1FMS89U
				// We record first the read size. In this case len is the actual
				// read size.
				len += readSize;
				readSize = reader.read(contents, len, length - len);
			}
			// Do not keep first character for UTF-8 BOM encoding
			int start = 0;
			if (length > 0 && "UTF-8".equals(encoding)) { //$NON-NLS-1$
				if (contents[0] == 0xFEFF) { // if BOM char then skip
					len--;
					start = 1;
				}
			}

			if (len != length)
				System.arraycopy(contents, start, (contents = new char[len]),
						0, len);
		}
		// System.err.println(contents);
		reader.close();
		return contents;
	}

	// public char[] getContents() throws EGGException {
	// // get cache
	// if (contents != null)
	// return contents;
	// try {
	// fEgg = new FileInputStream(negg);
	// return getInputStreamAsCharArray(fEgg, -1, null);
	// } catch (IOException e) {
	// throw new EGGException(IEGGErrors.contents_error, negg);
	// }
	// }

	public char[] getContents() throws IOException {
		return contents;
	}

	// public char[] getContents(InputStream f) throws EGGException {
	// try {
	// return getInputStreamAsCharArray(f, -1, null);
	// } catch (IOException e) {
	// throw new EGGException(IEGGErrors.contents_error, f.toString());
	// }
	// }

	public void createGen() throws EGGException {
		// try {
		fState.createGen();
		// } catch (EGGException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void createFile(String name, String contents) throws EGGException {
		// try {
		fState.createFile(name, contents);
		// } catch (EGGException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void updatePropFile(String name, Properties contents)
			throws EGGException {
		fState.updatePropFile(name, contents);
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
