package mg.egg.eggc.compiler.libegg.latex;

import java.util.Enumeration;

import mg.egg.eggc.compiler.libegg.base.ATTRIBUT;
import mg.egg.eggc.runtime.libjava.EGGException;

public class AttLatex {
    private ATTRIBUT attribut;

    /**
     * 
     * @return
     */
    public String getNom() {
        return attribut.getNom();
    }

    private StringBuffer entete;

    /**
     * 
     * @return
     */
    public StringBuffer getEntete() {
        return entete;
    }

    /**
     * 
     */
    public void setEntete() {
        entete = new StringBuffer();
        String c = attribut.getComm();
        if (c != null)
            entete.append(c + "\\\\\n\\\\\n");

        // on remplace les caractères spéciaux pour LaTeX
        String nom = attribut.getNom();
        nom = nom.replace("_", "\\_");

        String type = attribut.getType().getNom();
        type = type.replace("_", "\\_");

        switch (attribut.getSorte()) {
        case ATTRIBUT.HER:
            entete.append("\\egginh");
            //			entete.append("inh ");
            break;
        case ATTRIBUT.SYN:
            entete.append("\\eggsyn");
            //			entete.append("syn ");
            break;
        }
        entete.append("{" + nom + "}{" + type + " }{");
        Enumeration<String> e = attribut.getIdents().elements();
        if (e.hasMoreElements())
            entete.append("\\eggnt{" + e.nextElement() + "}");
        for (; e.hasMoreElements();) {
            entete.append(" , ");
            entete.append(e.nextElement());
        }
        entete.append("}\n");
    }

    /**
     * 
     * @param a
     * @param tbl
     */
    public AttLatex(ATTRIBUT a) {
        attribut = a;
    }

    /**
     * 
     * @param pw
     * @throws EGGException
     */
    public String finaliser() throws EGGException {
        // le fichier
        return entete.toString();
    }
}
