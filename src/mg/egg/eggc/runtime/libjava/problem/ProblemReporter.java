package mg.egg.eggc.runtime.libjava.problem;

import java.util.ArrayList;
import java.util.List;

import mg.egg.eggc.runtime.libjava.ISourceUnit;

public class ProblemReporter implements IProblemReporter {
	protected List<IProblem> problems;

	protected ISourceUnit cu;

	public ProblemReporter() {
		this(null);
	}

	public ProblemReporter(ISourceUnit cu) {
		this.cu = cu;
		problems = new ArrayList<IProblem>();
	}

	public List<IProblem> getAllProblems() {
		return problems;
	}

	public IProblem createProblem(int cat, int id, int line, String message,
			int start, int end, int severity, Object[] args) {
		switch (cat) {
		case DefaultProblem.Internal:
			return createInternal(cat, id, line, message, start, end, severity,
					args);
		case DefaultProblem.Syntax:
			return createSyntax(cat, id, line, message, start, end, severity,
					args);
		case DefaultProblem.Semantic:
			return createSemantic(cat, id, line, message, start, end, severity,
					args);
		}
		return new DefaultProblem(cu.getFileName(), message, cat, id, severity,
				start, end, line, args);
	}

	private DefaultProblem createSemantic(int cat, int id, int line,
			String message, int start, int end, int severity, Object[] args) {
		return new DefaultProblem(cu.getFileName(), message, cat, id, severity,
				start, end, line, args);
	}

	private DefaultProblem createSyntax(int cat, int id, int line,
			String message, int start, int end, int severity, Object[] args) {
		return new DefaultProblem(cu.getFileName(), message, cat, id, severity,
				start, end, line, args);
	}

	private DefaultProblem createInternal(int cat, int id, int line,
			String message, int start, int end, int severity, Object[] args) {
		
		return new DefaultProblem(cu.getFileName(), message, cat, id, severity,
				start, end, line, args);
	}

	public void record(IProblem problem) {
		problems.add(problem);
		cu.setOk(false);
	}

	private boolean exists(String msg, int line) {
		for (IProblem p : problems) {
			// System.out.println("P = " + p);
			if (p.getMessage().equals(msg) && p.getSourceLineNumber() == line)
				return true;
		}
		return false;
	}

	public void setSourceUnit(ISourceUnit su) {
		cu = su;
	}

	public void handle(int cat, int id, int line, String message, int start,
			int end, int severity, Object[] args) {
		// discard similar message
		if (exists(message, line))
			return;
		IProblem pb = createProblem(cat, id, line, message, start, end,
				severity, args);
		record(pb);
	}
}
