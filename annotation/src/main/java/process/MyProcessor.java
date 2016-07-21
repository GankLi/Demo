package process;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import annotation.MyAnnotation;

@SupportedAnnotationTypes("annotation.MyAnnotation")
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)){
            if(element.getKind() == ElementKind.METHOD){
                ExecutableElement executableElement = (ExecutableElement)element;

                System.out.println(executableElement.getSimpleName());

                System.out.println(executableElement.getReturnType().toString());

                List<? extends VariableElement> params = executableElement.getParameters();
                for(VariableElement variableElement : params){
                    System.out.println(variableElement.getSimpleName());
                }

                System.out.println(executableElement.getAnnotation(MyAnnotation.class).value());
            }
            System.out.println("------------------------------");
        }
        return false;
    }

}
