package twc.Automation.utils;

//import com.apps_auto.mobile.framework.core.*;

import java.util.*;

/**
 * Interface for constant configurations.
 * <p>
 * The values will be taken from System properties if not found then it will pick default value
 * <p>
 */
public class Constants {
	/**
	 * IOS device log
	 **/
	public static final boolean CAPTURE_DEVICE_LOG = Boolean.parseBoolean(System.getProperty("capture-device-log", "false"));
	public static final boolean CAPTURE_FULL_DEVICE_LOG = Boolean.parseBoolean(System.getProperty("capture-full-device-log", "false"));
	;
	public static final String DEVICE_LISTENER_PORT = "9876";

	public static final String LOCALE = System.getProperty("locale", "en-US");

	/**
	 * Charles specific configs
	 **/
	public final static String CHARLES_HOST = System.getProperty("charles_host", "0.0.0.0");
	public final static int CHARLES_PORT() {
		return Integer.parseInt(Optional.ofNullable(System.getenv(EnvironmentConstants.JENKINS_CHARLES_PORT_KEY)).orElse("8888")) + Integer
				.parseInt(Optional.ofNullable(System.getenv(EnvironmentConstants.JENKINS_EXECUTOR_ENV)).orElse("0"));
	}
	public final static String CHARLES_PATH = System.getProperty("charles_path", "/Applications/Charles.app/Contents/MacOS/Charles");
	public final static String CHARLES_CONFIG_PATH = System.getProperty("charles-config-location",
			"./src/main/java/charles_common.config");
	public final static boolean CHARLES_FILTER_ENABLE = Boolean.parseBoolean(System.getProperty("charles_filter", "true"));
	public final static String CHARLES_FILTER_LIST = System.getProperty("charles_filter_domain_list",
			"*weather.com;*localytics.com;*amazonaws.com");

	/* Extent reporting */
	public static final String KEY_CAPTURE_ALL_SNAPS = "CAPTURE_ALL_SNAPS";
	public static final boolean CAPTURE_ALL_SNAPS = Boolean.parseBoolean(System.getProperty(Constants.KEY_CAPTURE_ALL_SNAPS, "false"));
	public static final boolean DEFECTS_SEARCH_ENABLED = Boolean.parseBoolean(System.getProperty("defects-search-enabled", "true"));
	// May need for future reference
	// final static boolean CAPTURE_SNAPS_ON_FAIL = Boolean.parseBoolean(System.getProperty("snap-on-fail","true"));

	/* Capabilities */
	public final static String CAPS_PROP_NAME = "capability_file";
	public final static String ANDROID_CAPABILITIES = System.getProperty(CAPS_PROP_NAME, "android_caps.properties");
	public final static String IOS_CAPABILITIES = System.getProperty(CAPS_PROP_NAME, "ios_caps.properties");

	/*
	 * public final static String ANDROID_ID() { return
	 * Optional.ofNullable(System.getenv(EnvironmentConstants.
	 * JENKINS_ANDROID_UDID_KEY))
	 * .orElse(Optional.ofNullable(SessionInfo.getTemp(MobileTypes.ANDROID).
	 * getCapabilitiesConfig().get("udid")).orElse("")); }
	 */

	/* Browser Stack */

	/*
	 * public final static boolean BROWSER_STACK_ENABLE() { return
	 * Boolean.valueOf(Optional.ofNullable(System.getenv(EnvironmentConstants.
	 * JENKINS_BROWSER_STACK_ENABLE_KEY)).orElse("false")) ||
	 * Boolean.valueOf(Optional.ofNullable(SessionInfo.getTemp(MobileTypes.IOS).
	 * getCapabilitiesConfig().get("enableBrowserStack")).orElse("fasle")) ||
	 * Boolean.valueOf(Optional.ofNullable(SessionInfo.getTemp(MobileTypes.ANDROID).
	 * getCapabilitiesConfig().get("enableBrowserStack")).orElse("false")); }
	 */
	public final static String BROWSER_STACK_USER = "tonyhsu3";
	public final static String BROWSER_STACK_TOKEN = "vyd9nWNGLYvogeChxz6L";
	public final static String BROWSER_STACK_LOCAL_IDENTIFIER = "identifier-" + CHARLES_PORT();
	public static boolean BROWSER_STACK_PROXIED = false; // automatically set from CharlesProxy
	/* Timeouts */
	public final static int IMPLICIT_WAIT = Integer.parseInt(System.getProperty("implicit-timeout", "30"));
	public final static int ELEMENT_WAIT = Integer.parseInt(System.getProperty("element-timeout", "30"));
	public final static int ALERT_WAIT = Integer.parseInt(System.getProperty("element-timeout", "5"));
	public final static long POLLING_RATE = Integer.parseInt(System.getProperty("polling-rate", "500"));// 500
	// is default for WebDriver
	public final static long EXPLICIT_WAIT = Integer.parseInt(System.getProperty("explicit-timeout", "60"));
	public final static int APP_LAUNCH_WAIT = Integer.parseInt(System.getProperty("app-launch-timeout", "10"));
	// 5 minutes timeout for scrolling
	public final static int SWIPE_TIMEOUT = Integer.parseInt(System.getProperty("swipe-timeout", "300"));
	public final static boolean TEST_PARALLEL = Boolean.parseBoolean(System.getProperty("test-parallel", "false"));
	public final static boolean TEST_RETRY = Boolean.parseBoolean(System.getProperty("retry", "true"));
	public final static int TEST_RETRY_MAX = Integer.parseInt(System.getProperty("retry-max", "2"));

	// OS Specific properties
	public final static String TMP_DIR = System.getProperty("java.io.tmpdir", "/tmp");
	public final static String OS_NAME = System.getProperty("os.name");
	public final static String PATH_USER_HOME = System.getProperty("user.dir");
	public static final String X_INSERT_KEY = "vTrSeE3eVpWqXnpu9QZV4rd7sgI8hJg4";
	public static final String X_QUERY_KEY = "EIM1c6t2m1SKZfUbauTIN2jOphAeg2LW";
	public static final String NEW_RELIC_ACCOUNT = "1386191";
	/* Test Rail Test */
	public final static String TESTRAIL_URL = System.getProperty("testrail-host", "https://twcqa.testrail.com");// 760134
	public final static String TESTRAIL_TESTCASE_URI = "/index.php?/cases/view/%s";
	public final static String TESTRAIL_OUT_FILE = "testrailURLs.json";
	public final static long TESTRAIL_PROJECT_ID = 2; // Digital - Mobile Apps
	public final static long TESTRAIL_SUITE_ID_ANDROID = 3036;
	public static final String TESTRAIL_ANDROID_JENKINS_ENV_KEY = "ANDROID_TESTRAIL_RUN_IDS";
	public static final String TESTRAIL_IOS_JENKINS_ENV_KEY = "IOS_TESTRAIL_RUN_IDS";
	public final static long TESTRAIL_SUITE_ID_IOS = 992;

	/* Database Configuration */
	public final static String DB_SERVER = "appsautomationdbinstance.cxzjy9tfd6ql.us-east-1.rds.amazonaws.com";
	public final static String DB_NAME = "apps_automation";
	public final static String USERNAME = "apps_automation";
	public final static String PASSWORD = "hfbD48jwpSS3";
	public final static String DEFECTS_API = "https://apimetrics.twcmobile.weather.com/metricsapi/v1/api/qa_get_defects.php?token=M869518642a1895198966P521151212i1330208127";

	/* Upgrade Test Params */
	public static final String DEFAULT_UPGRADE_PATH = "UPGRADE APP PATH NOT GIVEN";
	public static final String UPGRADE_APP_PATH = System.getProperty("upgrade-app-path", DEFAULT_UPGRADE_PATH);
	/* Constant location for BAR schema */
	public final static String BAR_SCHEMA_JSON = "/unit_test/bar/bar_message_schema.json";

	/* Constants for starting Android Weather app via startActivity method */
	public final static String TWC_APP_PACKAGE_RELEASE = "com.weather.Weather";
	public final static String TWC_APP_PACKAGE = TWC_APP_PACKAGE_RELEASE;// + ".qa";
	public final static String TWC_ANDROID_FINDBY_ID = TWC_APP_PACKAGE + ":id/";
	public final static String TWC_APP_ACTIVITY = TWC_APP_PACKAGE_RELEASE + ".app.SplashScreenActivity";

	/* Web hook for logging */
	public static final String WEB_HOOK_LOGGER_URL = "localhost:9876";

	public final static String IOS_APP_BUNDLE_ID = "com.weather.TWC";
	public final static String SETTINGS_APP_BUNDLE = "com.apple.Preferences";
	public final static String WDA_BUNDLE_ID = "com.apple.test.WebDriverAgentRunner-Runner";
	public final static String IOS_APP_NAME = "TheWeather";
	public final static String IOS_APP_DISPLAY_NAME = "The Weather";

	/* Email Settings */
	public final static String EMAIL_USER_NAME = "twcqamobileautomation"; // GMail user name (just the part before "@gmail.com")
	public final static String EMAIL_PASSWORD = "Weathertest2"; // GMail password
	public final static String[] EMAIL_TO_RECIPIENT = {"vikas.thange@weather.com", "ramkumar.purushothaman@weather.com",
			"doug.uno@weather.com", "karthik.velappan@weather.com"};

	/* Elastic Search configuration */
	public final static boolean SAVE_TEST_STATS_DASHBOARD = Boolean.parseBoolean(System.getProperty("saveTestStat", "true"));
	public final static String ELASTIC_SEARCH_INDEX_NAME = System.getProperty("elastic-search-index", "apps");
	public final static String ELASTIC_SEARCH_HOST = System.getProperty("elastic-search-host", "9.105.226.152");
	public final static int ELASTIC_SEARCH_PORT = Integer.parseInt(System.getProperty("elastic-search-port", "9200"));
	public final static String ELASTIC_SEARCH_PERF_INDEX_NAME = System.getProperty("elastic-search-index", "performance");

	/* MySQL Configuration */
	public final static String MYSQL_HOST = "174.37.18.76";
	public final static int MYSQL_PORT = 3306;
	public final static String MYSQL_DB_NAME = "automation";
	public final static String MYSQL_USER = "automation";
	public final static String MYSQL_PASS = "h6jU7k8P60x";

	/* Constants for starting Android Weather app via startActivity method */
	public final static String TWC_APP_SENSORKIT_PACKAGE = "com.weather.sensorkit.test";
	public final static String TWC_APP_SENSORKIT_ACTIVITY = ".AutomationTestActivity";

	/* Filters for extent Report */
	public final static Set<Class<?>> EXTENT_REPORT_FILTER = new HashSet<Class<?>>();

	/* Lite */
	public static final String LITE_APP_PACKAGE = "com.weather.alps";
	public static final String LITE_APP_ACTIVITY = ".app.SplashScreenActivity";
}
