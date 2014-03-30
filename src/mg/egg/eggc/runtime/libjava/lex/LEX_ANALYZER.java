package mg.egg.eggc.runtime.libjava.lex;

import java.io.BufferedReader;
import java.io.IOException;

public interface LEX_ANALYZER {

    public void fromContext(LEX_CONTEXTE ul);

    public void toContext(LEX_CONTEXTE ul);

    public Yytoken yylex() throws IOException;

    public void setReader(BufferedReader r);
}
