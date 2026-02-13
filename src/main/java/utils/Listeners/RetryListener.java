package utils.Listeners;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

public class RetryListener implements IAnnotationTransformer {

    public RetryListener() {
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
       // Class<? extends IRetryAnalyzer> retryClass = annotation.getRetryAnalyzerClass();
         // Check if no retry analyzer is set
        System.out.println("[Transformer] applying to: " + (testMethod != null ? testMethod.getName() : "null"));
        annotation.setRetryAnalyzer(Retry.class);
    }

}