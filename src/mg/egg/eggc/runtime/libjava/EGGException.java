package mg.egg.eggc.runtime.libjava;


public class EGGException extends Exception implements IEGGException {

    private static final long serialVersionUID = 1L;

    private Object[] args;

    private int category;

    private String id;

    private int code;

//    public EGGException(int code, Object[] args) {
//    	this(IProblem.Internal, code, EGGErrors.unspecified_error, args);
//     }
//
//    public EGGException(int code, Object arg) {
//        this(code, new Object[] { arg });
//    }

    public EGGException(int category, int code, String id, Object[] args) {
        super();
        this.category = category;
        this.code = code;
        this.id = id;
        this.args = args;
    }

    public EGGException(int category, int code, String id, Object arg) {
        this(category, code, id, new Object[] { arg });
    }

    public int getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getArg(int i) {
        if (i < args.length)
            return args[i];
        else
            return null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("EGGException code = " + code + " category = " + category
                + " id = " + id + " args =");
        if (args != null)
            for (Object a : args)
                sb.append(a + " ");
        return sb.toString();
    }

}
