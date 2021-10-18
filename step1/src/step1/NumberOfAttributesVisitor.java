package step1;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class NumberOfAttributesVisitor extends ASTVisitor {
	int nbfields = 0;

	public boolean visit(org.eclipse.jdt.core.dom.FieldDeclaration node) {
		nbfields++;
		return super.visit(node);
	}

	public int getnbfields() {
		return nbfields;
	}

}
