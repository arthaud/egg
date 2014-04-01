package lib;

public class INFO {

	protected DTYPE type;

	public DTYPE getType() {
		return type;
	}

	public INFO(DTYPE t) {
		type = t;
	}

	public String toString() {
		return "INFO : type=" + type ;
	}
}
