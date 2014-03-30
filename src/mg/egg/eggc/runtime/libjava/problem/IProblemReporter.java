package mg.egg.eggc.runtime.libjava.problem;

import java.util.List;

import mg.egg.eggc.runtime.libjava.ISourceUnit;

public interface IProblemReporter {
    //	public IProblem createProblem(int cat, int id, int line, String message,
    //			int start, int end, int severity);
    //
    public IProblem createProblem(int cat, int id, int line, String message,
            int start, int end, int severity, Object[] args);

    public void setSourceUnit(ISourceUnit su);

    public void record(IProblem problem);

    //	public void handle(int cat, int code, int line, String message, int severity);
    //
    //	public void handle(int cat, int code, int line, String message, int start,
    //			int end, int severity);
    //
    public void handle(int cat, int code, int line, String message, int start,
            int end, int severity, Object[] args);

    public List<IProblem> getAllProblems();

}
