package my.compiler.processor;

import org.eijsink.annotation.HelloWorld;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes({
        "org.eijsink.annotation.HelloWorld"
})
public class HelloWorldProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

    Set<? extends Element> annotatedClasses = roundEnvironment.getElementsAnnotatedWith(HelloWorld.class);

        Messager messager = processingEnv.getMessager();

        for( Element annotatedClass: annotatedClasses){

            messager.printMessage(Diagnostic.Kind.NOTE, "Annotated class: " + annotatedClass.getEnclosingElement());
            String className = annotatedClass.getEnclosingElement().asType().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "classname : " + className);

            try {
                FileObject file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "resources", annotatedClass.getSimpleName() + "descriptor.json");
                Writer writer = file.openWriter();
                writer.write(
                        "{ \n\"" +
                        "   message\": \"Hello there....\"\n" +
                        "}");
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
       }

        return false;
    }

}
