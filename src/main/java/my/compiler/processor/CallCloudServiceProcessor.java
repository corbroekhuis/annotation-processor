package my.compiler.processor;

import org.eijsink.annotation.CallCloudService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes({
        "org.eijsink.annotation.CallCloudService"
})
public class CallCloudServiceProcessor extends AbstractProcessor {
    Messager messager;
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        Set<? extends Element> annotatedClasses = roundEnvironment.getElementsAnnotatedWith(CallCloudService.class);

        messager = processingEnv.getMessager();
        for( Element annotatedClass: annotatedClasses){

            CallCloudService handler = annotatedClass.getAnnotation(CallCloudService.class);

            String className = annotatedClass.toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "Classname class: " + annotatedClass.toString());
            messager.printMessage(Diagnostic.Kind.NOTE, "Enclosing class: " + annotatedClass.getEnclosingElement());
            messager.printMessage(Diagnostic.Kind.NOTE, "SimpleName class: " + annotatedClass.getEnclosingElement().getSimpleName());

            try {
               writeDecoratedFile( className, handler);
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Annotated class: " + className + " cannot write to file\n " + e.getMessage());
            }
        }

        return false;
    }
    private void writeDecoratedFile( String className, CallCloudService handler) throws IOException {

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }
        String decoratedClassName = className + "Decorated";
        messager.printMessage(Diagnostic.Kind.NOTE, "Classname class: " + className);
        String decoratedSimpleClassName = decoratedClassName.substring(lastDot + 1);

        JavaFileObject decoratedFile = processingEnv.getFiler()
                .createSourceFile(decoratedClassName);

        try (PrintWriter out = new PrintWriter(decoratedFile.openWriter())) {

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }
            out.println("import java.io.IOException;");
            out.println("import org.eijsink.annotation.Autowired;");
            out.println("import org.eijsink.model.Ticket;");
            out.println("import org.eijsink.service.cloud.CloudService;");
            out.println("import org.eijsink.service.cloud.CloudServiceImpl;");
            out.println();
            out.println("public class  " + decoratedSimpleClassName + " {");
            out.println();
//            out.println("    @Autowired");
//            out.println("    CloudService cloudService;");
            out.println("    // Added by annotationprocessor");
            out.println("    public String callCloudService( Ticket ticket) throws IOException{");
            out.println();
            out.println("        String url = \"" + handler.url() + "\";");
            out.println("        String user = \"" + handler.user() + "\";");
            out.println("        String password = \"" + handler.password() + "\";");
            out.println();
            out.println("        CloudService cloudService = new CloudServiceImpl();");
            out.println();
            out.println("        String token = cloudService.login( url + \"/login\", user, password);");
            out.println("        String response = cloudService.callApi( url + \"/ticket\", token, ticket);");
            out.println("        cloudService.logout( url + \"/logout\");");
            out.println();
            out.println("        return response;");
            out.println("    }");
            out.println("}");
        }
    }
}
