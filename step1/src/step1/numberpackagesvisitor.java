package step1;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class numberpackagesvisitor extends ASTVisitor {
	int nbpackages = 0;

	public boolean visit(org.eclipse.jdt.core.dom.PackageDeclaration node) {
		nbpackages++;
		return super.visit(node);
	}

	public int getnbpackages() {
		return nbpackages;
	}

}