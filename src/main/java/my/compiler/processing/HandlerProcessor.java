package my.compiler.processing;

import com.capgemini.annotationprocessor.Handler;

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
        "com.capgemini.annotationprocessor.Handler"
})
public class HandlerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        Set<? extends Element> annotatedClasses = roundEnvironment.getElementsAnnotatedWith(Handler.class);

        Messager messager = processingEnv.getMessager();
        for( Element annotatedClass: annotatedClasses){
            messager.printMessage(Diagnostic.Kind.NOTE, "Annotated class: " + annotatedClass.getSimpleName());

            Handler handler = annotatedClass.getAnnotation(Handler.class);

            try {
                FileObject file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "resources", annotatedClass.getSimpleName() + "Handler.java");
                Writer writer = file.openWriter();
                writer.write("\tvoid handle(){\n");
                writer.write("\t// " + handler.url() + "\n");
                writer.write("\t// " + handler.user() + "\n");
                writer.write("\t// " + handler.password() + "\n");
                writer.write("\t}\n");

                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }









        return false;
    }

}
