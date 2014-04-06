package mg.egg.eggc.runtime.libjava.problem;

public interface IProblem {

	int Internal = 0;

	int Syntax = 1;

	int Semantic = 2;

	/**
	 * Returns the arguments
	 *
	 * @return the arguments
	 */
	Object[] getArguments();

	/**
	 * Returns the problem id
	 *
	 * @return the problem id
	 */
	int getID();

	/**
	 * Answer a localized, human-readable message string which describes the
	 * problem.
	 *
	 * @return a localized, human-readable message string which describes the
	 *	     problem
	 */
	String getMessage();

	/**
	 * Answer the file name in which the problem was found.
	 *
	 * @return the file name in which the problem was found
	 */
	String getOriginatingFileName();

	/**
	 * Answer the end position of the problem (inclusive), or -1 if unknown.
	 *
	 * @return the end position of the problem (inclusive), or -1 if unknown
	 */
	int getSourceEnd();

	/**
	 * Answer the line number in source where the problem begins.
	 *
	 * @return the line number in source where the problem begins
	 */
	int getSourceLineNumber();

	/**
	 * Answer the start position of the problem (inclusive), or -1 if unknown.
	 *
	 * @return the start position of the problem (inclusive), or -1 if unknown
	 */
	int getSourceStart();

	/**
	 * Checks the severity to see if the Error bit is set.
	 *
	 * @return true if the Error bit is set for the severity, false otherwise
	 */
	boolean isError();

	/**
	 * Checks the severity to see if the Error bit is not set.
	 *
	 * @return true if the Error bit is not set for the severity, false
	 *	     otherwise
	 */
	boolean isWarning();

	/**
	 * Set the end position of the problem (inclusive), or -1 if unknown. Used
	 * for shifting problem positions.
	 *
	 * @param sourceEnd
	 *	        the given end position
	 */
	void setSourceEnd(int sourceEnd);

	/**
	 * Set the line number in source where the problem begins.
	 *
	 * @param lineNumber
	 *	        the given line number
	 */
	void setSourceLineNumber(int lineNumber);

	/**
	 * Set the start position of the problem (inclusive), or -1 if unknown. Used
	 * for shifting problem positions.
	 *
	 * @param sourceStart
	 *	        the given start position
	 */
	void setSourceStart(int sourceStart);

}
