package mg.egg.eggc.compiler.libegg.egg;

import java.util.Enumeration;

import mg.egg.eggc.compiler.libegg.base.ATTRIBUT;
import mg.egg.eggc.runtime.libjava.EGGException;

public class AttEgg {
	private ATTRIBUT attribut;

	public String getNom() {
	    return attribut.getNom();
	}

	private StringBuffer entete;

	public StringBuffer getEntete() {
	    return entete;
	}

	public void setEntete() {
	    entete = new StringBuffer();
	    String c = attribut.getComm();

	    if (c != null)
	        entete.append(c + '\n');

	    switch (attribut.getSorte()) {
	        case ATTRIBUT.HER:
	            entete.append("inh ");
	            break;
	        case ATTRIBUT.SYN:
	            entete.append("syn ");
	            break;
	    }

	    entete.append(attribut.getNom() + " : " + attribut.getType().getNom()
	            + " for\n  ");
	    Enumeration<String> e = attribut.getIdents().elements();

	    if (e.hasMoreElements())
	        entete.append(e.nextElement());

	    for (; e.hasMoreElements();) {
	        entete.append(" , ");
	        entete.append(e.nextElement());
	    }
	    entete.append(";\n");
	}

	public AttEgg(ATTRIBUT a) {
	    attribut = a;
	}

	public String finaliser() throws EGGException {
	    return entete.toString();
	}
}
