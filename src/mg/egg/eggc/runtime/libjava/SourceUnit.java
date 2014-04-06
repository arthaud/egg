package mg.egg.eggc.runtime.libjava;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import mg.egg.eggc.runtime.libjava.messages.CoreMessages;
import mg.egg.eggc.runtime.libjava.messages.ICoreMessages;
import mg.egg.eggc.runtime.libjava.problem.IProblem;

public class SourceUnit implements ISourceUnit {
	private String fSourceName;
	private boolean ok;

	public SourceUnit(String name) {
		fSourceName = name;
		ok = true;
	}

	public char[] getContents() throws Exception {
		InputStream fSource;
		try {
			fSource = new FileInputStream(fSourceName);
			return Utils.getInputStreamAsCharArray(fSource, -1, null);
		} catch (IOException e) {
			throw new EGGException(IProblem.Internal,
					ICoreMessages.id_EGG_source_read_error,CoreMessages.EGG_source_read_error, fSourceName);
		}
	}

	public String getFileName() {
		return fSourceName;
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
