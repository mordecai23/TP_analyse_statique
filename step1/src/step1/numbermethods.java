package step1;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class numbermethods extends ASTVisitor {
	int nbmethod = 0;
	int maxParam = -1;
	String nomMethodMax = "";

	public boolean visit(org.eclipse.jdt.core.dom.MethodDeclaration node) {
		nbmethod++;
		int nbparam = node.parameters().size();
		if (nbparam > maxParam) {
			maxParam = nbparam;
			nomMethodMax = node.getName().toString();
		}
		return super.visit(node);
	}

	public int getnbmethods() {
		return nbmethod;
	}

}
