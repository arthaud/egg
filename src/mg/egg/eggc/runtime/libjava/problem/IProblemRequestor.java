package mg.egg.eggc.runtime.libjava.problem;


public interface IProblemRequestor {
    void acceptProblem(IProblem problem);

    void beginReporting();

    void endReporting();

    boolean isActive();
    
    int getFatal();
}
