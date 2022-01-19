package twc.Automation.HandleWithCharles;





import twc.Automation.utils.CommandRunner;
import com.google.common.io.Files;
import twc.Automation.utils.Constants;
import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;

import org.apache.commons.io.*;


import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;


public class CharlesProxy extends Drivers{
	//private static final MobileAutomationLogger LOGGER = new MobileAutomationLogger(CharlesProxy.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public static enum CharlesOption {
		RECORDING("/recording/"),
		REWRITE("/tools/rewrite/"),
		MAP_LOCAL("/tools/map-local/"),
		SESSION("/session/"),
		THROTTLING("/throttling/"),
		BLACK_LIST("/tools/black-list/");

		private String path;

		private CharlesOption(String path) {
			this.path = path;
		}

		public String getPath() {
			return this.path;
		}
	}

	private final String username = "vikas";
	private final String password = "vikas";
	private String CHARLES_CONFIG_PATH;
	private int port;
	private String host;

	private final String CHARLES_URL = "http://control.charles";
	private final String CHARLES_HEADLESS_PARAM = " -headless ";
	private final String CHARLES_CONFIGURATION_PARAM = " -config ";

	/**
	 * Initializes Charles with default configurations.
	 * <p>
	 * Host: localhost
	 * <p>
	 * Port: 8888
	 * <p>
	 * Configurations: default
	 */
	public CharlesProxy() {
		this(Constants.CHARLES_HOST, Constants.CHARLES_PORT(), useConfigCopy(Constants.CHARLES_CONFIG_PATH));
		// modify copied xml file for recording settings
		if (Constants.CHARLES_FILTER_ENABLE) {
			final CharlesConfiguration config = new CharlesConfiguration(this.CHARLES_CONFIG_PATH);
			config.addRecordingUrls(Constants.CHARLES_FILTER_LIST.split(";"));
			config.saveConfigurations(this.CHARLES_CONFIG_PATH);
		}
	}

	/**
	 * Initializes Charles with passed in configurations. Uses configurations
	 * directly.
	 * <p>
	 * Host: localhost
	 * <p>
	 * Port: 8888
	 *
	 * @param configPath
	 *            Configuration file to use directly
	 */
	public CharlesProxy(String configPath) {
		this(Constants.CHARLES_HOST, Constants.CHARLES_PORT(), configPath);
	}

	/**
	 * Initializes Charles with passed in arguments.
	 *
	 * @param host
	 *            Host Ip
	 * @param configPath
	 *            Charles configuration file, used directly
	 */
	public CharlesProxy(String host, String configPath) {
		this(host, Constants.CHARLES_PORT(), configPath);
	}

	/**
	 * Visibility modified from private to public by Narasimha on 11th Aug 2020
	 * @param host
	 * @param port
	 * @param configPath
	 */
	public CharlesProxy(String host, int port, String configPath) {
		this.host = host;
		this.port = port;
		CharlesConfiguration config = new CharlesConfiguration(configPath);
		config.setPort(port);
		config.saveConfigurations(configPath + ".port" + port);
		new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
		this.CHARLES_CONFIG_PATH = configPath + ".port" + port;
	
		Constants.BROWSER_STACK_PROXIED = true;
	}

	/**
	 * Make a copy of file.
	 *
	 * @param srcPath
	 *            Path of configuration file
	 * @return Path of copied configuration file
	 */
	private static String useConfigCopy(String srcPath) {
		try {
			final File src = new File(srcPath);
			final File des = new File(srcPath + "_temp");
			Files.copy(src, des);
			return des.getPath();
		} catch (final IOException e) {
			System.out.println("Failed to copy configurations: + " + e.getMessage());
			logStep("Failed to copy configurations: + " + e.getMessage());
		}
		return srcPath;
	}

	public boolean isCharlesRunning() {
		String response = "";
		try {
			response = this.executeCommand(this.CHARLES_URL);
		} catch (final Exception e) {
			// Could be IO, Connect timeout, Read timeout, etc.
			// Assume if anything goes wrong that Charles isn't running so it's killed, or we check again
			return false;
		}
		return !response.isEmpty();
	}

	public boolean isCharlesRecording() {
		final String response = this
				.executeCommand(this.CHARLES_URL + CharlesOption.RECORDING.getPath());
		return this.getStatus(response).equalsIgnoreCase("Recording");
	}

	private String getStatus(String response) {
		String status = "NOT FOUND";
		final Pattern pattern = Pattern.compile(".*<p>Status: (.*?)<");
		final Matcher matcher = pattern.matcher(response);
		if (matcher.find()) {
			status = matcher.group(1);
		}
		return status;
	}

	/**
	 * Start Charles in headless mode, will wait maximum 10 seconds for Charles
	 *
	 * @throws CharlesProxyException
	 *             Thrown if unable to launch harles or timed out on waiting for Charles
	 */
	public void startCharlesProxy() throws CharlesProxyException {
		this.startCharlesProxy(true);
	}

	/**
	 * Start Charles with UI, used for debugging purposes, will wait maximum 10 seconds for Charles
	 *
	 * @throws CharlesProxyException
	 *             Thrown if unable to launch Charles or timed out on waiting for Charles
	 */
	public void startCharlesProxyWithUI() throws CharlesProxyException {
		this.startCharlesProxy(false);
	}

	/**
	 * Start Charles in either modes, will wait maximum 10 seconds for Charles
	 *
	 * @param isHeadless
	 *            True means headless, False for UI
	 * @throws CharlesProxyException
	 *             Thrown if unable to launch Charles or timed out on waiting for Charles
	 */
	private void startCharlesProxy(boolean isHeadless) throws CharlesProxyException {
		if (!java.nio.file.Files.isRegularFile(Paths.get(this.CHARLES_CONFIG_PATH),
				LinkOption.NOFOLLOW_LINKS)) {
			System.out.println(
					"Configuration Path: " + this.CHARLES_CONFIG_PATH + " is an invalid file.");
			logStep("Configuration Path: " + this.CHARLES_CONFIG_PATH + " is an invalid file.");
		}
		System.out.println("Starting Charles headless: " + isHeadless + " using port: " + this.port);
		logStep("Starting Charles headless: " + isHeadless + " using port: " + this.port);
		// add headless arg if isHeadless is true
		final String command = Constants.CHARLES_PATH
				+ (isHeadless ? this.CHARLES_HEADLESS_PARAM : "") + this.CHARLES_CONFIGURATION_PARAM
				+ this.CHARLES_CONFIG_PATH;
		try {
			if (this.isCharlesRunning()) {
				this.quitCharlesProxy();
			}
			CommandRunner.runCommand(command);
			//BaseTest.wait(1, true);
			
			this.waitForCharles(30);
		} catch (final CharlesProxyException e) {
			System.out.println("Failed to launch charles server, Error: " + e.getMessage());
			logStep("Failed to launch charles server, Error: " + e.getMessage());
			throw new CharlesProxyException(e.getMessage());
		}
	}

	public void startRecording() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.RECORDING.getPath() + "start");
	}

	public void stopRecording() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.RECORDING.getPath() + "stop");
	}

	public File getHar() {
		// http://control.charles/session/export-har
		final String url = this.CHARLES_URL + CharlesOption.SESSION.getPath() + "export-har";
		final String filePath = Constants.TMP_DIR;
		try {
			return this.downloadFile(url, filePath);
		} catch (final IOException e) {
			System.out.println("Failed to get har file. " + e.getMessage());
			logStep("Failed to get har file. " + e.getMessage());
			// adding to HTML report as well
			System.out.println("Failed to get har file. " + e.getMessage());
			logStep("Failed to get har file. " + e.getMessage());
			throw new RuntimeException("Failed to get har file. " + e.getMessage());
		}
	}
	public File getXml() throws Exception {
		// http://control.charles/session/export-xml
		final String url = this.CHARLES_URL + CharlesOption.SESSION.getPath() + "export-xml";
		//final String filePath = Constants.PATH_USER_HOME+"/CapturedSessionFile/";
		FileUtils.cleanDirectory(new File(properties.getProperty("downloadPath"))); 
		String[][] paths = read_excel_data.exceldataread("Paths");
		final String filePath=paths[4][2];
		System.out.println("Charles Download path is: "+filePath);
		logStep("Charles Download path is: "+filePath);
		try {
			return this.downloadFile(url, filePath);
		} catch (final IOException e) {
			System.out.println("Failed to get har file. " + e.getMessage());
			logStep("Failed to get har file. " + e.getMessage());
			// adding to HTML report as well
			logStep("Failed to get har file. " + e.getMessage());
			System.out.println("Failed to get har file. " + e.getMessage());
			throw new RuntimeException("Failed to get har file. " + e.getMessage());
		}
	}

	public void enableRewriting() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.REWRITE.getPath() + "enable");
	}

	public void disableRewriting() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.REWRITE.getPath() + "disable");
	}

	public void enableMapLocal() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.MAP_LOCAL.getPath() + "enable");
	}

	public void disableMapLocal() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.MAP_LOCAL.getPath() + "disable");
	}

	public void enableThrottling() {
		this.executeCommand(
				this.CHARLES_URL + CharlesOption.THROTTLING.getPath() + "activate?preset=");
	}

	public void disableThrottling() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.THROTTLING.getPath() + "deactivate");
	}

	public void enableBlackList() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.BLACK_LIST.getPath() + "enable");
	}

	public void disableBlackList() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.BLACK_LIST.getPath() + "disable");
	}

	public void quitCharlesProxy() {
		this.executeCommand(this.CHARLES_URL + "/quit");
	}

	public void clearCharlesSession() {
		this.executeCommand(this.CHARLES_URL + CharlesOption.SESSION.getPath() + "clear");
	}

	private String executeCommand(String url) {
		final StringBuffer response = new StringBuffer();
		URL obj;
		try {
			obj = new URL(url);

			final String userPassword = this.username + ":" + this.password;
			final String encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
			final Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(this.host, this.port));
			final HttpURLConnection con = (HttpURLConnection)obj.openConnection(proxy);
			con.setReadTimeout(60000);
			con.setConnectTimeout(60000);
			con.setRequestProperty("Authorization", "Basic " + encoding);
			// optional default is GET
			con.setRequestMethod("GET");

			final int responseCode = con.getResponseCode();
			System.out.println("Url :" + url + " Responded with status code : " + responseCode);
			logStep("Url :" + url + " Responded with status code : " + responseCode);
			final BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (final MalformedURLException e) {
			System.out.println("Error while executing: " + url);
			logStep("Error while executing: " + url);
			System.out.println("Error: " + e.getMessage());
			logStep("Error: " + e.getMessage());
		} catch (final IOException e) {
			logStep("Error while executing: " + url);
			logStep("Error: " + e.getMessage());
		}
		return response.toString();
	}

	private File downloadFile(String fileURL, String saveDir) throws IOException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String currentTimeStamp = sdf.format(timestamp);
		final URL url = new URL(fileURL);
		//final String saveFilePath = saveDir + "charles.har";
		final String saveFilePath = saveDir + "Charles"+currentTimeStamp+".chlsx";
		System.out.println("To be saved file: "+saveFilePath);
		logStep("To be saved file: "+saveFilePath);
		final File savedFile = new File(saveFilePath);
		
		if (savedFile.exists()) {
			System.out.println(
					"Found an exisiting HAR file at this path. Deleting before downloading new one.");
			logStep(
					"Found an exisiting HAR file at this path. Deleting before downloading new one.");
			savedFile.delete();
		}
		System.out.println("Create new file that is empty and writable");
		logStep("Create new file that is empty and writable");
		savedFile.createNewFile();
		savedFile.setWritable(true);

		// Save to replace after
		final String origHost = System.getProperty("http.proxyHost");
		final String origPort = System.getProperty("http.proxyPort");

		// Change for this download
		System.setProperty("http.proxyHost", this.host);
		System.setProperty("http.proxyPort", this.port + "");

		// One minute timeout each for connection and download
		System.out.println("Downloading Charles HAR file to: " + saveFilePath);
		logStep("Downloading Charles HAR file to: " + saveFilePath);
		FileUtils.copyURLToFile(url, savedFile, 60000, 60000);
		System.out.println("File download complete");
		logStep("File download complete");
		System.out.println(String.format("Har file size (mb): %02f", savedFile.length() / 1024.0 / 1024));
		logStep(String.format("Har file size (mb): %02f", savedFile.length() / 1024.0 / 1024));

		// Replace/clear proxy
		if (origHost != null) {
			System.setProperty("http.proxyHost", origHost);
		} else {
			System.clearProperty("http.proxyHost");
		}
		if (origPort != null) {
			System.setProperty("http.proxyPort", origPort);
		} else {
			System.clearProperty("http.proxyPort");
		}
		return savedFile;
	}

	/**
	 * Stops any running instances of the Charles
	 */
	public static void stopAllInstances() {
		if (Constants.OS_NAME.contains("Win")) {
			CommandRunner.runCommand("taskkill /F IM Charles.exe");
		} else {
			CommandRunner.runCommandBash("killall Charles");
		}
	}

	/**
	 * Wait for Charles to finish booting up.
	 *
	 * @param timeout
	 *            Seconds to wait before throwing an error
	 * @throws CharlesProxyException
	 */
	public void waitForCharles(int timeout) throws CharlesProxyException {
		int seconds = 0;
		while (!this.isCharlesRunning()) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (final InterruptedException e) {
				System.out.println("Failed to wait for " + seconds + " seconds");
				logStep("Failed to wait for " + seconds + " seconds");
			}
			if (seconds >= timeout) {
				throw new CharlesProxyException(
						"Charles is still not running after " + timeout + " second(s)");
			}
			seconds++;
		}
	}
}
