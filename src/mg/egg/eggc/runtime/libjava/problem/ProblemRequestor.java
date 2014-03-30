package mg.egg.eggc.runtime.libjava.problem;

import java.util.Vector;

/*
 * basic implementation of ProblemRequestor
 */
public class ProblemRequestor implements IProblemRequestor {
	protected Vector<IProblem> problems;
	private int fatal = 0;
	private boolean showInternalErrors = false;

	public ProblemRequestor() {
		this(false);
	}

	public ProblemRequestor(boolean showInternalErrors) {
		this.showInternalErrors = showInternalErrors;
	}

	public void acceptProblem(IProblem problem) {
		problems.add(problem);
		if (problem.isError())
			fatal++;
	}

	public void beginReporting() {
		problems = new Vector<IProblem>();
	}

	public void endReporting() {
		for (IProblem problem : problems) {
			if (!showInternalErrors) {
				if (problem instanceof DefaultProblem
						&& IProblem.Internal == ((DefaultProblem) problem)
								.getCategory())
					continue;
			}
			System.err.println(problem);
		}
	}

	public boolean isActive() {
		return true;
	}

	public int getFatal() {
		return fatal;
	}

//	private boolean exists(String msg, int line) {
//		for (IProblem p : problems) {
//			// System.out.println("P = " + p);
//			if (p.getMessage().equals(msg) && p.getSourceLineNumber() == line)
//				return true;
//		}
//		return false;
//	}

}
