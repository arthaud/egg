package mg.egg.eggc.runtime.libjava.problem;

import mg.egg.eggc.runtime.libjava.messages.CoreMessages;

public class DefaultProblem implements IProblem, ProblemSeverities {
	private static String[] kind = {
			CoreMessages.EGG_category_internal,
			CoreMessages.EGG_category_syntax,
			CoreMessages.EGG_category_semantics
	};

	private static String[] sev = {
			CoreMessages.EGG_severity_warning,
			CoreMessages.EGG_severity_error
	};

	private String fileName;

	private int id;

	private int cat;

	private int startPosition, endPosition, line;

	private int severity;

	private Object[] arguments;

	private String message;

	public DefaultProblem(String originatingFileName, String message, int cat,
			int id, int severity, int startPosition, int endPosition, int line,
			Object[] arguments) {
		this.fileName = originatingFileName;
		this.message = message;
		this.cat = cat;
		this.id = id;
		this.severity = severity;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.line = line;
		this.arguments = arguments;
	}

	public String errorReportSource(char[] unitSource) {
		StringBuffer errorBuffer = new StringBuffer(" "); //$NON-NLS-1$
		return errorBuffer.toString();
	}

	/**
	 * Answer the type of problem.
	 *
	 * @see org.eclipse.jdt.core.compiler.IProblem#getID()
	 * @return int
	 */
	public int getID() {
		return this.id;
	}

	public int getCategory() {
		return cat;
	}

	/**
	 * Answer a localized, human-readable message string which describes the
	 * problem.
	 *
	 * @return java.lang.String
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Answer the file name in which the problem was found.
	 *
	 * @return char[]
	 */
	public String getOriginatingFileName() {
		return this.fileName;
	}

	/**
	 * Answer the end position of the problem (inclusive), or -1 if unknown.
	 *
	 * @return int
	 */
	public int getSourceEnd() {
		return this.endPosition;
	}

	/**
	 * Answer the line number in source where the problem begins.
	 *
	 * @return int
	 */
	public int getSourceLineNumber() {
		return this.line;
	}

	/**
	 * Answer the start position of the problem (inclusive), or -1 if unknown.
	 *
	 * @return int
	 */
	public int getSourceStart() {
		return this.startPosition;
	}

	/*
	 * Helper method: checks the severity to see if the Error bit is set.
	 *
	 * @return boolean
	 */
	public boolean isError() {
		return (this.severity & ProblemSeverities.Error) != 0;
	}

	/*
	 * Helper method: checks the severity to see if the Error bit is not set.
	 *
	 * @return boolean
	 */
	public boolean isWarning() {
		return (this.severity & ProblemSeverities.Error) == 0;
	}

	public void setOriginatingFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Set the end position of the problem (inclusive), or -1 if unknown.
	 *
	 * Used for shifting problem positions.
	 *
	 * @param sourceEnd
	 *	        the new value of the sourceEnd of the receiver
	 */
	public void setSourceEnd(int sourceEnd) {
		this.endPosition = sourceEnd;
	}

	/**
	 * Set the line number in source where the problem begins.
	 *
	 * @param lineNumber
	 *	        the new value of the line number of the receiver
	 */
	public void setSourceLineNumber(int lineNumber) {
		this.line = lineNumber;
	}

	/**
	 * Set the start position of the problem (inclusive), or -1 if unknown.
	 *
	 * Used for shifting problem positions.
	 *
	 * @param sourceStart
	 *	        the new value of the source start position of the receiver
	 */
	public void setSourceStart(int sourceStart) {
		this.startPosition = sourceStart;
	}

	public String toString() {
		String s = getOriginatingFileName() + ":" + line + ":"
				+ sev[this.severity] + "(" + kind[this.cat]+")";
		if (this.message != null) {
			s += ":" + this.message;
		}
		return s;
	}

	public Object[] getArguments() {
		return this.arguments;
	}
}
