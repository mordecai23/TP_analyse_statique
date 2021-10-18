package step1;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

public class TopTenPercentAttributeVisitor extends ASTVisitor {
	HashMap<String, Integer> cm = new HashMap<>();

	public boolean visit(org.eclipse.jdt.core.dom.TypeDeclaration node) {
		SimpleName className = node.getName();
		String name = className.toString();
		int numberofAttributes = node.getFields().length;
		cm.put(name, numberofAttributes);

		return super.visit(node);
	}

	public void print() {
		HashMap<String, Integer> res = new HashMap<>();
		int toptenpercent = (int) (cm.size() * 0.1);
		System.out.println("Le top 10 pourcent des classes avec le plus d'attributs \n");
		for (int i = 1; i <= toptenpercent; i++) {
			String k = cm.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
					.get().getKey();
			System.out.println(i + " : " + k + "\n");
			cm.remove(k);

		}

	}

	public ArrayList<String> getTopTenAttributes() {
		ArrayList<String> res = new ArrayList<>();
		int toptenpercent = (int) (cm.size() * 0.1);
		for (int i = 1; i <= toptenpercent; i++) {
			String k = cm.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
					.get().getKey();
			res.add(k);
			cm.remove(k);
		}
		return res;
	}

}