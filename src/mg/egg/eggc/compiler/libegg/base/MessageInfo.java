package mg.egg.eggc.compiler.libegg.base;

import java.io.Serializable;

public class MessageInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	public String getId() {
		return id;
	}

	public MessageInfo(String c) {
		id = c;
	}

	public String toString() {
		return id;
	}

	transient private boolean idChange = false;

	public boolean getIdChange() {
		return idChange;
	}

	public void setIdChange(boolean b) {
		idChange = b;
	}

	public void setAllChange(boolean b) {
		idChange = b;
	}

	public void compare(MessageInfo old) {
		idChange = !id.equals(old.id);
	}
}
