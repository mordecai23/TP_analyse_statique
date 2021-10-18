package step1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Parser {

	public static final String projectPath = "/home/yashveer/Bureau/projets/Loca-immo/android/app/";
	public static final String projectSourcePath = projectPath + "/src";
	public static final String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar";

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);

		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
		System.out.println("Le nombre de classes = " + NumberOfClasses(javaFiles));
		System.out.println("Le nombre de lignes = " + NumberOfLines(javaFiles));
		System.out.println("Le nombre de classes = " + NumberOfMethods(javaFiles));
		NumberOfPackages(javaFiles);
		AverageNumberOfMethods(javaFiles);

		System.out.println("Le nombre de lignes moyen par méthode est : "
				+ NumberOfLinesOfMethod(javaFiles) / NumberOfMethods(javaFiles));
		System.out.println("Le nombre d'attributs moyen par classe est : "
				+ NumberOfAttributes(javaFiles) / NumberOfClasses(javaFiles));
		TopTenPercentMethods(javaFiles);
		TopTenPercentAttributes(javaFiles);
		TopTenPercentAttributesAndMethods(javaFiles);
		MoreThanXMethods(javaFiles, 5);
		MaxParameters(javaFiles);

	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	public static int NumberOfLinesOfMethod(ArrayList<File> javaFiles) throws IOException {
		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		LinesOfMethodVisitor lm = new LinesOfMethodVisitor(parse);
		parse.accept(lm);

		return lm.getnblinesmethod();

	}

	public static void AverageNumberOfMethods(ArrayList<File> javaFiles) throws IOException {
		System.out.println("Le nombre moyen de méthodes par classe est : "
				+ (NumberOfMethods(javaFiles) / NumberOfClasses(javaFiles)));
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { projectSourcePath };
		String[] classpath = { jrePath };

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		parser.setSource(classSource);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	public static int NumberOfLines(ArrayList<File> javaFiles) {
		int lines = 0;
		for (File f : javaFiles) {
			Path fil = Paths.get(f.toURI());
			try {
				lines += Files.lines(fil).count();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return lines;
	}

	public static int NumberOfClasses(ArrayList<File> javaFiles) throws IOException {
		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		numberclassesvisitor nc = new numberclassesvisitor();
		parse.accept(nc);

		return nc.getnbclasses();
	}

	public static int NumberOfMethods(ArrayList<File> javaFiles) throws IOException {

		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		numbermethods nm = new numbermethods();

		parse.accept(nm);
		return nm.getnbmethods();
	}

	public static void NumberOfPackages(ArrayList<File> javaFiles) throws IOException {

		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		numberpackagesvisitor np = new numberpackagesvisitor();

		parse.accept(np);

		System.out.println("Le nombre de packages est : " + np.getnbpackages());
	}

	public static int NumberOfAttributes(ArrayList<File> javaFiles) throws IOException {

		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		NumberOfAttributesVisitor na = new NumberOfAttributesVisitor();

		parse.accept(na);

		return na.getnbfields();
	}

	public static void TopTenPercentMethods(ArrayList<File> javaFiles) throws IOException {

		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		TopTenPercentMethodVisitor tt = new TopTenPercentMethodVisitor();

		parse.accept(tt);

		tt.print();
	}

	public static void TopTenPercentAttributes(ArrayList<File> javaFiles) throws IOException {

		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		TopTenPercentAttributeVisitor ta = new TopTenPercentAttributeVisitor();

		parse.accept(ta);

		ta.print();

	}

	public static void TopTenPercentAttributesAndMethods(ArrayList<File> javaFiles) throws IOException {
		ArrayList<String> res = new ArrayList<>();
		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		TopTenPercentAttributeVisitor ta = new TopTenPercentAttributeVisitor();
		TopTenPercentMethodVisitor tt = new TopTenPercentMethodVisitor();

		parse.accept(ta);
		parse.accept(tt);

		for (String k : ta.getTopTenAttributes()) {
			for (String j : tt.getTopTenMethods()) {
				if (k.equals(j)) {
					res.add(k);
				}
			}
		}

		if (res.size() == 0) {
			System.out.println("Aucune classe n'est dans le top 10 pourcent des attributs et des méthodes à la fois\n");
		} else {
			System.out.println("Classes dans le top 10 pourcent d'attributs et méthodes \n");
			for (String p : res) {
				System.out.println(p + "\n");

			}

		}
	}

	public static void MoreThanXMethods(ArrayList<File> javaFiles, int x) throws IOException {

		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		TopTenPercentMethodVisitor tt = new TopTenPercentMethodVisitor();

		parse.accept(tt);
		ArrayList<String> mtx = tt.moreThanX(x);
		if (mtx.size() == 0) {
			System.out.println("Aucune classe ne possède plus de" + x + "méthodes\n");
		} else {
			System.out.println("Les classes avec plus de " + x + " méthodes \n");
			for (String p : mtx) {
				System.out.println(p + "\n");

			}

		}
	}

	public static void MaxParameters(ArrayList<File> javaFiles) throws IOException {
		String content = "";
		for (File fileEntry : javaFiles) {

			content += FileUtils.readFileToString(fileEntry);

		}
		CompilationUnit parse = parse(content.toCharArray());
		numbermethods nm = new numbermethods();
		parse.accept(nm);
		System.out.println(
				"Le nombre maximal de paramètres est " + nm.maxParam + "\n La méthode est : " + nm.nomMethodMax + "\n");
	}
}
