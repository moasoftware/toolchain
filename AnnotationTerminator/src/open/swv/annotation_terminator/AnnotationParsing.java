package open.swv.annotation_terminator;
import java.util.ListIterator;

import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
//import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

public class AnnotationParsing {
	public String parsingClass(CompilationUnit cu) {

		NodeList<TypeDeclaration<?>> nodeTypes = cu.getTypes();
		for (TypeDeclaration<?> typeDeclaration : nodeTypes) {

			ListIterator<AnnotationExpr> classAnnot = typeDeclaration.getAnnotations().listIterator();
			while (classAnnot.hasNext()) {
				classAnnot.next();
				classAnnot.remove();
			}

			NodeList<BodyDeclaration<?>> nodeBodies = typeDeclaration.getMembers();
			for (BodyDeclaration<?> bodyDeclaration : nodeBodies) {
				if (bodyDeclaration instanceof FieldDeclaration) {
					FieldDeclaration variable = (FieldDeclaration) bodyDeclaration;
					parsingVariable(variable);
				} else if (bodyDeclaration instanceof MethodDeclaration) {
					MethodDeclaration method = (MethodDeclaration) bodyDeclaration;
					parsingMethod(method);
				}
			}
		}
		String code =  cu.toString();
		//generic type
		code = code.replaceAll("<([^<])*>", "");

		return code;
	}
	public void parsingVariable(FieldDeclaration variable) {
		ListIterator<AnnotationExpr> variableAnnot = variable.getAnnotations().listIterator();
		while (variableAnnot.hasNext()) {
			variableAnnot.next();
			variableAnnot.remove();
		}
	}

	public void parsingMethod(MethodDeclaration method) {

		ListIterator<AnnotationExpr> nodeAnnot = method.getAnnotations().listIterator();

		while (nodeAnnot.hasNext()) {
			nodeAnnot.next();
			nodeAnnot.remove();
			//method annotation
		}

		NodeList<Parameter> nodeParams = method.getParameters();
		for (Parameter parameter : nodeParams) {
			ListIterator<AnnotationExpr> annotationsIterator  = parameter.getAnnotations().listIterator();

			 while (annotationsIterator.hasNext()) {
		        annotationsIterator.next();
		        annotationsIterator.remove();
				//parameter annotation
			}
		}
	}
}
