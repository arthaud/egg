package mg.egg.eggc.runtime.libjava;

public interface IEGGException {

    public int getCode();

    public Object[] getArgs();

    public Object getArg(int i);

}
