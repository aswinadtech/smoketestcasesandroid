package twc.Automation.RetryAnalyzer;


import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

//public class RetryAnalyzer implements IRetryAnalyzer{
//	
//	public static int count = 0; 
//	public static int maxCount = 4; // set your count to re-run test
//	protected Logger log;
//	private static Logger testbaseLog;
//
//	static {
//	    PropertyConfigurator.configure("/Users/vishal.pathania/Documents/android/ads-android/log4j.properties");
//	    testbaseLog = Logger.getLogger("TestclassName");
//	}
//
//	public RetryAnalyzer()
//	{
//	    testbaseLog.trace( " ModeledRetryAnalyzer constructor " + this.getClass().getName() );
//	    log = Logger.getLogger("TestclassName");
//	}
//
//	public boolean retry(ITestResult result) { 
//	    testbaseLog.trace("running retry logic for  '" 
//	            + result.getName() 
//	            + "' on class " + this.getClass().getName() );
//	    result.getTestContext().getFailedTests().removeResult(result.getMethod());
//	        if(count < maxCount) {                     
//	                count++;                                    
//	                return true; 
//	               
//	        } 
//	        return false; 
//	}
//
//}

public class RetryAnalyzer implements IRetryAnalyzer {
	//public static int retryCount = 0;
	//public static int maxRetryCount = 1;
	
	public static int count = 0;
	public static int maxCount = 1;

// Below method returns 'true' if the test method has to be retried else 'false' 
//and it takes the 'Result' as parameter of the test method that just ran
    public boolean retry(ITestResult result) {
        if (count < maxCount) {
            System.out.println("Retrying test " + result.getName() + " with status "
                    + getResultStatusName(result.getStatus()) + " for the " + (count+1) + " time(s).");
            count++;
            return true;
        }else {
        	RetryAnalyzer.count=0;
        }
        return false;
    }
    
    public String getResultStatusName(int status) {
    	String resultName = null;
    	if(status==1)
    		resultName = "SUCCESS";
    	if(status==2)
    		resultName = "FAILURE";
    	if(status==3)
    		resultName = "SKIP";
		return resultName;
    }


}

