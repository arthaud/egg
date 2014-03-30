package mg.egg.eggc.runtime.libjava;


public interface IDstVisitor {
    public boolean visit(IDstNode node);

    public void endVisit(IDstNode node);
}
