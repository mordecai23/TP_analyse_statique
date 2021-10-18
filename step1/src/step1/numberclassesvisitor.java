package step1;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class numberclassesvisitor extends ASTVisitor {
	int nbclasse = 0;

	public boolean visit(org.eclipse.jdt.core.dom.TypeDeclaration node) {
		nbclasse++;
		return super.visit(node);
	}

	public int getnbclasses() {
		return nbclasse;
	}

}