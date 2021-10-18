package step1;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class LinesOfMethodVisitor extends ASTVisitor {
	int nblinesmethod = 0;
	CompilationUnit cu;

	public LinesOfMethodVisitor(CompilationUnit cu) {
		this.cu = cu;
	}

	public boolean visit(org.eclipse.jdt.core.dom.MethodDeclaration node) {
		int startLineNum = cu.getLineNumber(node.getStartPosition());
		int endLineNum = cu.getLineNumber(node.getStartPosition() + node.getLength());
		int numberoflines = (endLineNum - startLineNum) + 1;
		nblinesmethod += numberoflines;

		return super.visit(node);
	}

	public int getnblinesmethod() {
		return nblinesmethod;
	}

}
