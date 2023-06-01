package my.compiler.processing;

import com.capgemini.annotationprocessor.HelloWorld;

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
        "com.capgemini.annotationprocessor.HelloWorld"
})
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

    Set<? extends Element> annotatedClasses = roundEnvironment.getElementsAnnotatedWith(HelloWorld.class);

        Messager messager = processingEnv.getMessager();
        for( Element annotatedClass: annotatedClasses){
            messager.printMessage(Diagnostic.Kind.NOTE, "Annotated class: " + annotatedClass.getSimpleName());
            try {
                FileObject file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "resources", annotatedClass.getSimpleName() + "descriptor.xml");
                Writer writer = file.openWriter();
                writer.write("Hello there....");
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }









        return false;
    }

}
