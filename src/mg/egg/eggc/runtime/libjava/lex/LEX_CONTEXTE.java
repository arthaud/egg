// contexte de lecture : necessaire pour un chgt de lexical efficace 
package mg.egg.eggc.runtime.libjava.lex;

import java.io.BufferedReader;
import java.io.StringReader;

import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.ISourceUnit;
import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

public class LEX_CONTEXTE {

	public int getLine() {
		return ligne;
	}

	public BufferedReader source;

	public String encoding;

	public char buffer[];

	public int b_size;

	public int b_read;

	public int b_start;

	public int b_end;

	public int b_index;

	public int consommes;

	public int ligne;

	public int car;

	public int errors = 0;

	public boolean onError() {
		return errors > 0;
	}

	public LEX_CONTEXTE(String src) {
		// System.err.println(" lex_contexte Contents=\n" +src.length() );
		// fileIn = fi;
		source = new BufferedReader(new StringReader(src));
		b_size = 512;
		buffer = new char[512];
		b_read = 0;
		b_start = 0;
		b_end = 0;
		b_index = 0;
		consommes = 0;
		ligne = 0;
		car = 0;
		errors = 0;
	}

	public LEX_CONTEXTE(ISourceUnit cu) throws EGGException {
		try {
			source = new BufferedReader(new StringReader(new String(
					cu.getContents())));
			b_size = 512;
			buffer = new char[b_size];
			b_read = 0;
			b_start = 0;
			b_end = 0;
			b_index = 0;
			consommes = 0;
			ligne = 0;
			car = 0;
			errors = 0;
		} catch (Exception e) {
			throw new EGGException(IProblem.Internal,
					ICoreMessages.id_EGG_source_read_error,
					CoreMessages.EGG_source_read_error, cu.getFileName());
		}

	}

	public String toString() {
		return "LC : Reader : " + source + "\n";

	}
}
