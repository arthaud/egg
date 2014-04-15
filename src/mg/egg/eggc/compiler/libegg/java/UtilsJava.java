package mg.egg.eggc.compiler.libegg.java;

import java.util.Vector;

import mg.egg.eggc.compiler.libegg.type.IType;

public class UtilsJava {
	   public static String getOpJava(String op) {
		    if (op.equals("+") || op.equals("+."))
		        return "+";
		    else if (op.equals("-") || op.equals("-."))
		        return "-";
		    else if (op.equals("*") || op.equals("*."))
		        return "*";
		    else if (op.equals("/") || op.equals("/."))
		        return "/";
		    else if (op.equals("and"))
		        return "&&";
		    else if (op.equals("or"))
		        return "||";
		    else if (op.equals("~"))
		        return "!";
		    else if (op.equals("@"))
		        return "+";
		    else if (op.equals("/="))
		        return "!=";
		    else
		        return op;
		}

		public static String getTypeJava(IType type) {
		    String ntype = type.getNom();
		    StringBuffer stype = new StringBuffer();

		    if (ntype.equals("STRING"))
		        stype.append("String");
		    else if (ntype.equalsIgnoreCase("strings"))
		        stype.append("String []");
		    else if (ntype.equals("BOOLEAN"))
		        stype.append("boolean");
		    else if (ntype.equals("INTEGER"))
		        stype.append("int");
		    else if (ntype.equals("CHARACTER"))
		        stype.append("char");
		    else if (ntype.equalsIgnoreCase("void"))
		        stype.append("void");
		    else if (ntype.equalsIgnoreCase("any"))
		        stype.append("Object");
		    else if (ntype.equals("DOUBLE"))
		        stype.append("double");
		    else if (ntype.equals("Non determine"))
		        stype.append("/* Non determine */ Object");
		    else
		        stype.append(ntype);

		    Vector<IType> pars = type.getPars();

		    if (pars != null) {
		        // ajouter les parametres eventuels
		        stype.append("<");
		        boolean premier = true;

		        for (IType p : pars) {
		            if (!premier)
		                stype.append(", ");
		            else
		                premier = false;

		            stype.append(getTypeJava(p));
		        }
		        stype.append(">");
		    }

		    return stype.toString();
		}

		public static String getPar(int i) {
		    if (i == 0)
		        return "this";
		    else
		        return "x_" + i;
		}

}
