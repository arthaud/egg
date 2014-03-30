package mg.egg.eggc.compiler;

import java.io.Serializable;

import mg.egg.eggc.compiler.egg.java.EGG;
import mg.egg.eggc.runtime.libjava.EGGSourceFile;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;
import mg.egg.eggc.runtime.libjava.problem.IProblem;
import mg.egg.eggc.runtime.libjava.problem.ProblemReporter;
import mg.egg.eggc.runtime.libjava.problem.ProblemRequestor;

public class EGGC implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			IEGGCompilationUnit cu = new EGGSourceFile(args[0]);
			ProblemReporter prp = new ProblemReporter(cu);
			ProblemRequestor prq = new ProblemRequestor();
			// new ProblemRequestor(true) to show internal errors
			EGG compilo = new EGG(prp);
			compilo.set_eval(true);
			compilo.set_source(cu);
			prq.beginReporting();
			compilo.set_eval(true);
			compilo.compile(cu);
			for (IProblem problem : prp.getAllProblems())
				prq.acceptProblem(problem);
			prq.endReporting();
//			System.exit(prq.getFatal());
		} catch (Exception e) {
			//e.printStackTrace();
			System.exit(1);
		}
	}
}
