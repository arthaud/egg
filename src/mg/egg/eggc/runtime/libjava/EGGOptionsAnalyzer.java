package mg.egg.eggc.runtime.libjava;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class EGGOptionsAnalyzer {
	private static final long serialVersionUID = 1L;

	private EGGOptions options;

	IEGGCompilationUnit cu;

	public EGGOptionsAnalyzer(IEGGCompilationUnit unit) throws EGGException {
		cu = unit;
		options = cu.getOptions();
		// in = cu.getFileName();
	}

	public class PrintError implements ErrorHandler {
		public void error(SAXParseException exception) throws SAXException {
			System.err.println("error: " + exception);
			exception.printStackTrace();
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			System.err.println("fatal error: " + exception);
		}

		public void warning(SAXParseException exception) throws SAXException {
			System.err.println("warning: " + exception);
		}
	}

	class BasketHandler extends DefaultHandler {
		private StringBuffer result = new StringBuffer();

		public void startDocument() throws SAXException {
		}

		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) {
			// System.err.println("start element : " + qName);
			if (qName.equals("egg")) {
				// System.err.println("egg : ");
				for (int i = 0; i < atts.getLength(); i++) {
					String aname = atts.getQName(i);
					String avalue = atts.getValue(i);
					if ("scanner".equals(aname)) {
						options.setLexer(avalue);
					} else if ("module".equals(aname)) {
						options.setModule("true".equals(avalue));
					} else if ("lang".equals(aname)) {
						options.setLang(avalue);
					} else if ("gen".equals(aname)) {
						options.setDirectory(avalue);
					} else if ("prefix".equals(aname)) {
						options.setProject(avalue);
					} else if ("dst".equals(aname)) {
						options.setDst("true".equals(avalue));
					} else if ("so".equals(aname)) {
						options.setSyntaxOnly("true".equals(avalue));
//					} else if ("typage".equals(aname)) {
//						options.setTypage("true".equals(avalue));
					} else if ("main".equals(aname)) {
						options.setMain("true".equals(avalue));
					}
				}
			} else if (qName.equals("import")) {
				// System.err.println("import : ");
				for (int i = 0; i < atts.getLength(); i++) {
					String aname = atts.getQName(i);
					String avalue = atts.getValue(i);
					if ("lib".equals(aname)) {
						options.addLib(avalue);
					}
				}
			}
		}

		public void endElement(String namespaceURI, String localName,
				String qName) {

		}

		public void characters(char[] ch, int start, int length) {
			result.append(new String(ch, start, length));
		}

	}

	public void analyse(InputSource is) throws ParserConfigurationException,
			SAXException, IOException {
		// System.err.println("Analyse_xml ");
		// try {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		// activation de la validation (egg.dtd)
		// factory.setValidating(true);
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		BasketHandler handler = new BasketHandler();
		XMLReader reader = parser.getXMLReader();
		reader.setEntityResolver(handler);
		reader.setContentHandler(handler);
		// mise en place du gestionnaire d'erreur
		reader.setErrorHandler(new PrintError());
		reader.parse(is);
		// System.err.println("Options = " + options);
//		cu.setState();
	}

	public String getUsage() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nusage : egg.java.EGGC <file>");
		sb.append("(see configuration file .ecf)\n");
		return sb.toString();
	}

	public String getHelp() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nusage : egg.java.EGGC <file>");
		sb.append("[-?] : this help.\n");
		return sb.toString();
	}

}
