package utils.Listeners;
import newcrm.global.GlobalMethods;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
public class Retry implements IRetryAnalyzer {
    private int retryCount = 0;
    @Override
    public boolean retry(ITestResult result) {
        int maxRetryCount = 1;
        if (retryCount < maxRetryCount) {
            retryCount++;
            GlobalMethods.printDebugInfo("Retry #" + retryCount + " for test: " + result.getMethod().getMethodName() + ", on thread: " + Thread.currentThread().getName());
            return true;
        }
        return false;
    }
}