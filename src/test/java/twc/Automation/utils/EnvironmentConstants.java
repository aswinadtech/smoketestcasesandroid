package twc.Automation.utils;
/**
 * Constants relating to environment such as files or system
 */
public interface EnvironmentConstants {
	// EnvironmentConstants

	String CAPABILITY_PROPERTIES_PREFIX = "capability.";
	String APPIUM_PROPERTIES_PREFIX = "appium.";

	// Appium
	String APPIUM_HOST = APPIUM_PROPERTIES_PREFIX + "host";
	String APPIUM_PORT = APPIUM_PROPERTIES_PREFIX + "port";
	String APPIUM_NODE_PATH = APPIUM_PROPERTIES_PREFIX + "node.path";
	String APPIUM_NODE_JSFILE = APPIUM_PROPERTIES_PREFIX + "node.jsfile";
	String APPIUM_LOG = APPIUM_PROPERTIES_PREFIX + "log";
	String APPIUM_LOG_LEVEL = APPIUM_PROPERTIES_PREFIX + "log_level";

	// Jenkins Env
	String JENKINS_APPIUM_PORT_KEY = "APPIUM_PORT";
	String JENKINS_ADB_PORT_KEY = "ADB_PORT";
	String JENKINS_WDA_PORT_KEY = "WDA_PORT";
	String JENKINS_ANDROID_UDID_KEY = "ANDROID_UDID";
	String JENKINS_IOS_UDID_KEY = "DEVICE_NUMBER";
	String JENKINS_ANDROID_DEVICE_NAME = "ANDROID_DEVICE_NAME";
	String JENKINS_ANDROID_DEVICE_OS_VER = "ANDROID_DEVICE_OS_VERSION";
	String JENKINS_BROWSER_STACK_ENABLE_KEY = "BROWSER_STACK_ENABLED";
	String JENKINS_PLATFORM = "platform";
	String JENKINS_BROWSER_STACK_IOS_APP = "remote_app_ios_flagship";
	String JENKINS_BROWSER_STACK_ANDROID_APP = "remote_app_android_flagship";
	String JENKINS_CHARLES_PORT_KEY = "CHARLES_PORT";
	String JENKINS_EXECUTOR_ENV = "EXECUTOR_NUMBER";

	// SoftAssertResults
	String SOFTASSERT_REPORT_FILENAME = "softAssertResults.txt";
	String SOFTASSERT_ENV_PASS = "passedTestCount";
	String SOFTASSERT_ENV_FAIL = "failedTestCount";
	String SOFTASSERT_ENV_TOTAL = "totalTestCount";
	String SOFTASSERT_ENV_UNTESTED = "untestedCount";
}
