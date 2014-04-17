import mg.egg.eggc.runtime.libjava.SourceUnit;
import mg.egg.eggc.runtime.libjava.problem.IProblem;
import mg.egg.eggc.runtime.libjava.problem.ProblemReporter;
import mg.egg.eggc.runtime.libjava.problem.ProblemRequestor;

import compiled.TEST;

public class Main {
	public static void main(String[] args) {
		if(args.length < 1)
		{
			System.err.println("error: too few arguments");
			System.exit(1);
		}

		try {
			SourceUnit cu = new SourceUnit(args[0]);
			ProblemReporter prp = new ProblemReporter(cu);
			ProblemRequestor prq = new ProblemRequestor();

			TEST compilo = new TEST(prp);
			prq.beginReporting();
			compilo.set_eval(true);
			compilo.set_source(cu);
			compilo.compile(cu);

			for (IProblem problem : prp.getAllProblems())
				prq.acceptProblem(problem);

			prq.endReporting();
            System.exit(prq.getFatal());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
