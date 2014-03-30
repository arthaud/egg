package mg.egg.eggc.internal.dst;

import java.util.ArrayList;
import java.util.List;

import mg.egg.eggc.compiler.egg.java.S_ACTS_EGG;
import mg.egg.eggc.compiler.egg.java.S_GLOBALES_EGG;
import mg.egg.eggc.compiler.egg.java.S_LACTION_LACTION;
import mg.egg.eggc.compiler.egg.java.S_LOCALES_LACTION;
import mg.egg.eggc.compiler.egg.java.S_LOCS_LACTION;
import mg.egg.eggc.compiler.egg.java.S_S_EGG;
import mg.egg.eggc.compiler.egg.java.T_LACTION;
import mg.egg.eggc.runtime.libjava.IDstNode;

// collection of access routines

public class DstUtils {
	// return Statement node containing node 'node'
	public static IDstNode getStatement(IDstNode node) {
		if (node == null)
			return null;
		IDstNode stat = null;
		return stat;
	}

	// return action node containing node 'node'
	public static IDstNode getAction(IDstNode node) {
		if (node == null)
			return null;
		IDstNode action = node;
		do {
			action = action.getParent();
		} while (!(action instanceof S_LACTION_LACTION));
		return action;
	}

	// return actions rule node containing node 'node'
	public static IDstNode getActsRule(IDstNode node) {
		if (node == null)
			return null;
		IDstNode actions = node;
		do {
			actions = actions.getParent();
		} while (!(actions instanceof S_ACTS_EGG));
		return actions;
	}

	public static IDstNode getGlobalsRule(IDstNode node) {
		if (node == null)
			return null;
		IDstNode globals = node;
		do {
			globals = globals.getParent();
		} while (!(globals instanceof S_GLOBALES_EGG));
		return globals;
	}

	// Return list of local declarations of action containing node 'node'
	public static List<String> getLocalVarsList(IDstNode node) {
		IDstNode action = getAction(node);
		if (action == null)
			return null;
		List<String> names = new ArrayList<String>();
		List<IDstNode> ls = action.getChildren();
		if (ls == null)
			return names;
		IDstNode l = ls.get(0); // locales ?
		if (!(l instanceof S_LOCALES_LACTION))
			return names;
		ls = l.getChildren();
		if (ls == null)
			return names;
		l = ls.get(1); // locs ?
		try {
			while (l instanceof S_LOCS_LACTION
					&& ((ls = l.getChildren()) != null)) {
				names.add(((T_LACTION) ls.get(0)).getTxt()); // ident var
				l = ls.get(3);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names;
	}

	// Return list of global declarations of action containing node 'node'
	public static List<String> getGlobalVarsList(IDstNode node) {
		IDstNode rule = getActsRule(node);
		if (rule == null)
			return null;
		List<String> names = new ArrayList<String>();
		return names;
	}

	// Return list of declarations of action containing node 'node'
	public static List<String> getDeclarationList(IDstNode node) {
		// List<String> globs = getGlobalVarsList(node);
		List<String> locs = getLocalVarsList(node);
		List<String> names = new ArrayList<String>();
		// names.addAll(globs);
		names.addAll(locs);
		return names;
	}

	public static IDstNode getAxiom(IDstNode node) {
		IDstNode axiome = node;
		while (!(axiome instanceof S_S_EGG)) {
			axiome = axiome.getParent();
		}
		return axiome;
	}
	
//	public static IDstNode getAttributes(IDstNode node) {
//		IDstNode axiome = node;
//		while (!(axiome instanceof S_S_EGG)) {
//			axiome = axiome.getParent();
//		}
//		return axiome;
//	}
	
	
}
