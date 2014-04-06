package mg.egg.eggc.runtime.libjava.lex;

public class Yytoken {
	public Yytoken(int index, String text, int line, int charBegin, int charEnd) {
	    this.index = index;
	    this.text = text;
	    this.line = line;
	    this.charBegin = charBegin;
	    this.charEnd = charEnd;
	}

	public int index;

	public String text;

	public int line;

	public int charBegin;

	public int charEnd;

	public String toString() {
	    return index + " , " + text + " , " + line + " , " + charBegin + " , "
	            + charEnd;
	}
}
