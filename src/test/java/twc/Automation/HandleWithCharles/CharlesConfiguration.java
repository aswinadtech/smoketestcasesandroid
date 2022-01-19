package twc.Automation.HandleWithCharles;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import  twc.Automation.utils.Constants;
import  twc.Automation.Driver.Drivers;
import twc.Automation.utils.XMLParserLib;

//import com.apps_auto.mobile.framework.config.Constants;
//import com.apps_auto.mobile.framework.lib.MobileAutomationLogger;
//import com.apps_auto.mobile.framework.utils.XMLParserLib;

/**
 * Class to modify Charles to include Rewrite, Map Local, and Network Throttling
 * Rules. </br>
 *
 *
 * Note: When using throttling rule, running a speed test does not show it is
 * being throttled. Use the twc app and utilizing a network monitoring tool (ie:
 * https://play.google.com/store/apps/details?id=com.internet.speed.meter.lite)
 * to asserts that it is actually being throttled.
 *
 * @author tony
 *
 */
public class CharlesConfiguration extends Drivers{
	//protected static final MobileAutomationLogger LOGGER = new MobileAutomationLogger(CharlesConfiguration.class);

	/**
	 * Types of configuration available to modify
	 */
	public enum ToolType {
		REWRITE("rewrite"),
		MAP_LOCAL("mapLocal"),
		NETWORK_THROTTLING("throttlingConfiguration"),
		BLACK_LIST("blacklist");

		private String xmlName;

		private ToolType(String xmlName) {
			this.xmlName = xmlName;
		}

		public String getXmlName() {
			return this.xmlName;
		}
	}

	public static enum Protocol {
		HTTP,
		HTTPS,
		NONE;

		public static Protocol getType(String str) {
			switch (str.toUpperCase()) {
				case "HTTP" :
					return HTTP;
				case "HTTPS" :
					return HTTPS;
				default :
					return NONE;
			}
		}
	}

	public enum RewriteRuleType {
		ADD_HEADER("1"),
		MODIFY_HEADER("3"),
		REMOVE_HEADER("2"),
		HOST("4"),
		PATH("5"),
		URL("6"),
		ADD_QUERY_PARAM("8"),
		MODIFY_QUERY_PARAM("9"),
		REMOVE_QUERY_PARAM("10"),
		RESPONSE_STATUS("11"),
		BODY("7");
		String ruleNumber;

		private   RewriteRuleType(String ruleNumber) {
			this.ruleNumber = ruleNumber;
		}

		public String getRuleNumber() {
			return this.ruleNumber;
		}
	}

	public enum RewriteRuleReplaceType {
		ONLY_FIRST("1"),
		ALL("2");

		private String replaceNum;

		private RewriteRuleReplaceType(String replaceNum) {
			this.replaceNum = replaceNum;
		}

		public String getReplaceNum() {
			return this.replaceNum;
		}
	}

	public enum ThrottlingPresets {
		TWO_G,
		THREE_G,
		FOUR_G,
	};

	protected Document doc;
	private Element root;

	private Node rewriteNode;
	private Element locationPattern;
	private Element ruleNode;

	private Node mapLocalNode;
	private Node mappingSetNode;

	/**
	 * Programmatically modify CharlesProxy configurations.
	 *
	 * @throws CharlesProxyException
	 *             Throws exception if file error
	 */
	public CharlesConfiguration() throws CharlesProxyException {
		this(Constants.CHARLES_CONFIG_PATH);
	}
	/**
	 * Programmatically modify CharlesProxy configurations.
	 *
	 * @param xmlConfigPath Configuration to load
	 * @throws CharlesProxyException
	 *             Throws exception if file error
	 */
	public CharlesConfiguration(String xmlConfigPath) throws CharlesProxyException {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			final DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(new File(xmlConfigPath));
			this.root = this.doc.getDocumentElement();
		} catch (final IOException e) {
			throw new CharlesProxyException("File error: " + e.getMessage());
		} catch (ParserConfigurationException | SAXException e) {
			throw new CharlesProxyException(
					"Something went wrong with parsing config file: " + e.getMessage());
		}
	}

	/**
	 * Saves configurations to destination path.
	 *
	 * @param destPath
	 *            Save file path
	 * @throws CharlesProxyException
	 *             Throws exception if file error
	 */
	public void saveConfigurations(String destPath) throws CharlesProxyException {
		try {
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			final Result output = new StreamResult(new File(destPath));
			this.doc.setXmlStandalone(true);
			final Source input = new DOMSource(this.doc);
			transformer.transform(input, output);
		} catch (final TransformerException e) {
			throw new CharlesProxyException("Unable to save configurations: " + e.getMessage());
		}
	}

	/**
	 * @return list of tool nodes
	 */
	private List<Node> getToolConfigurationNodes() {
		if (this.root.getNodeType() != Node.ELEMENT_NODE) {
			System.out.println("Root Node of type Element");
			logStep("Root Node of type Element");
			return null;
		}

		List<Node> configNodes = new ArrayList<Node>();
		final NodeList toolConfigList = this.root.getElementsByTagName("toolConfiguration");

		if (toolConfigList.getLength() == 1) {
			final Node toolConfigNode = toolConfigList.item(0);
			final Node configs = XMLParserLib.findFirstChildNode("configs", toolConfigNode);
			configNodes = XMLParserLib.findChildNodes("entry", configs);
		}
		return configNodes;
	}

	/**
	 * Get the node of the specific setting to modify
	 *
	 * @param name
	 *            ToolType
	 * @return Node of toolType requested
	 */
	protected Node getToolConfiguration(ToolType name) {
		// first lookup in tool configuration nodes
		final List<Node> toolsNode = this.getToolConfigurationNodes();
		for (final Node e : toolsNode) {
			final NodeList list = e.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				final Node type = list.item(i);
				if (type.getNodeName().equals(name.getXmlName())) {
					return type;
				}
			}
		}
		// if not not found in tool configuration nodes then direct search
		// e.g. network throttling or recordHosts

		return XMLParserLib.findFirstChildNode(name.getXmlName(), this.root);
	}

	/**
	 * Set decryptSSL to true/false in Charles configuration. Should be true for
	 * most normal cases, but sometimes SSL decryption does not work even if the
	 * root cert is installed. One example is the Facebook app. SSL decryption
	 * needs to be false in order for the app to work.
	 */
	public void setSSLDecrypt(boolean decrypt) {
		final Node proxyConfig = XMLParserLib.findFirstChildNode("proxyConfiguration", this.root);
		XMLParserLib.findFirstChildNode("decryptSSL", proxyConfig)
				.setTextContent(Boolean.toString(decrypt));
	}

	/**
	 * Turn on Debug in Error Log to get some debugging information printed in
	 * the Error Log accessed from the Window menu in Charles.
	 *
	 * @param name
	 * @param flag
	 */
	public void setDebuggingForTool(ToolType name, boolean flag) {
		final Node tool = this.getToolConfiguration(name);
		final NodeList toolNodes = tool.getChildNodes();
		for (int i = 0; i < toolNodes.getLength(); i++) {
			final Node n = toolNodes.item(i);
			if (n.getNodeName().equals("debugging")) {
				n.setTextContent(flag + "");
			}
		}
	}

	// Might be useful in future instead of enabling tool from web public
	// void setEnabledForTool(ToolType name, boolean flag) {
	// LOGGER.log("setEnabledForTool: " + name.name());
	// Node tool = getToolConfiguration(name);
	// NodeList toolNodes = tool.getChildNodes();
	// for (int i = 0; i < toolNodes.getLength(); i++) {
	// Node n = toolNodes.item(i);
	// if (n.getNodeName().equals("toolEnabled")) {
	// n.setTextContent(flag + "");
	// }
	// }
	// }
	// ---------- Charles Map Local ---------- //

	private void initMappingNode() {
		this.mapLocalNode = this.getToolConfiguration(ToolType.MAP_LOCAL);

		// default configuration contains empty set, need to remove it
		final List<Node> n = XMLParserLib.findChildNodes("mappings", this.mapLocalNode);
		if (n.size() == 1) {
			this.mapLocalNode.removeChild(n.get(0));
		}
	}

	/**
	 * Atomically splits an URL and store it into proper fields. Can be called
	 * multiple times to add additional maps. Must call saveConfigurations() to
	 * save to file.
	 *
	 * @param urlStr
	 *            URL to parse
	 * @param destination
	 *            Path to load data from
	 * @throws CharlesProxyException
	 *             Unable to parse URL or if unable to save file
	 */
	public void addMapping(String urlStr, String destination) throws CharlesProxyException {
		try {
			final URL url = new URL(urlStr);

			final String protocol = url.getProtocol();
			final String host = url.getHost();
			final String port = url.getPort() == -1 ? "" : url.getPort() + "";
			final String path = url.getPath();
			final String query = url.getQuery() == null ? "" : url.getQuery();

			this.addMapping(Protocol.getType(protocol), host, port, path, query, destination, true);
		} catch (final MalformedURLException e) {
			throw new CharlesProxyException("Unable to parse url: " + e.getMessage());
		}
	}

	/**
	 * Store the mappings into file. Can be called multiple times to add
	 * additional maps. Must call saveConfigurations() to save to file.
	 *
	 * @param protocol
	 *            Protocol
	 * @param host
	 *            Host name, can be empty
	 * @param port
	 *            Port number, can be empty
	 * @param path
	 *            Path, can be empty
	 * @param query
	 *            Query, can be empty
	 * @param destPath
	 *            Path to load from
	 * @param isCaseSensitive
	 *            Path case sensitive
	 */
	public void addMapping(Protocol protocol, String host, String port, String path, String query,
			String destPath, boolean isCaseSensitive) {
		if (this.mapLocalNode == null) {
			this.initMappingNode();
		}
		if (this.mappingSetNode == null) {
			this.mappingSetNode = this.doc.createElement("mappings");
			this.mapLocalNode.appendChild(this.mappingSetNode);
		}

		final File f = new File(destPath);
		if ((f == null) || !f.isFile()) {
			System.out.println(
					"Destination Path: " + destPath + " is an invalid file. Map Local WILL fail.");
			logStep(
					"Destination Path: " + destPath + " is an invalid file. Map Local WILL fail.");
		}

		final Element mapLocalMapping = this.doc.createElement("mapLocalMapping");

		final Element sourceLocation = this.doc.createElement("sourceLocation");
		mapLocalMapping.appendChild(sourceLocation);

		XMLParserLib.appendNewTextNode(this.doc, sourceLocation, "protocol",
				!protocol.equals(Protocol.NONE) ? protocol.toString().toLowerCase() : "");
		XMLParserLib.appendNewTextNode(this.doc, sourceLocation, "host", host);
		XMLParserLib.appendNewTextNode(this.doc, sourceLocation, "port", port);
		XMLParserLib.appendNewTextNode(this.doc, sourceLocation, "path", path);
		XMLParserLib.appendNewTextNode(this.doc, sourceLocation, "query", query);

		XMLParserLib.appendNewTextNode(this.doc, mapLocalMapping, "dest", f.getAbsolutePath());
		XMLParserLib.appendNewTextNode(this.doc, mapLocalMapping, "caseSensitive",
				isCaseSensitive + "");

		XMLParserLib.appendNewTextNode(this.doc, mapLocalMapping, "enabled", "true");
		this.mappingSetNode.appendChild(mapLocalMapping);
	}

	// ---------- Charles Rewriting ---------- //

	private void initRewriteNode() {
		this.rewriteNode = this.getToolConfiguration(ToolType.REWRITE);

		// default configuration contains empty set, need to remove it
		final List<Node> n = XMLParserLib.findChildNodes("sets", this.rewriteNode);
		if (n.size() == 1) {
			this.rewriteNode.removeChild(n.get(0));
		}

		final Node setsNode = this.doc.createElement("sets");
		this.rewriteNode.appendChild(setsNode);

		final Element rewriteSet = this.doc.createElement("rewriteSet");
		setsNode.appendChild(rewriteSet);

		// add location rules
		final Element hostNode = this.doc.createElement("hosts");
		rewriteSet.appendChild(hostNode);

		this.locationPattern = this.doc.createElement("locationPatterns");
		hostNode.appendChild(this.locationPattern);

		this.ruleNode = this.doc.createElement("rules");
		rewriteSet.appendChild(this.ruleNode);

		XMLParserLib.appendNewTextNode(this.doc, rewriteSet, "active", "true");
		XMLParserLib.appendNewTextNode(this.doc, rewriteSet, "name", "GeneratedSet");
	}

	/**
	 * Atomically splits an URL and store it into proper fields. Auto Saves. Can
	 * be called multiple times to add additional locations.
	 *
	 * @param urlStr
	 *            URL to parse
	 * @throws CharlesProxyException
	 *             Unable to parse URL or if unable to save file
	 */
	public void addLocation(String urlStr) throws CharlesProxyException {
		try {
			final URL url = new URL(urlStr);

			final String protocol = url.getProtocol();
			final String host = url.getHost();
			final String port = url.getPort() == -1 ? "" : url.getPort() + "";
			final String path = url.getPath();
			final String query = url.getQuery() == null ? "" : url.getQuery();

			this.addLocation(Protocol.getType(protocol), host, port, path, query);
		} catch (final MalformedURLException e) {
			throw new CharlesProxyException("Unable to parse url: " + e.getMessage());
		}
	}

	/**
	 * Create a new location rule. Must call saveConfigurations() to save to
	 * file.
	 *
	 * @param protocol
	 *            Protocol
	 * @param host
	 *            Host name, can be empty
	 * @param port
	 *            Port number, can be empty
	 * @param path
	 *            Path, can be empty
	 * @param query
	 *            Query, can be empty
	 */
	public void addLocation(Protocol protocol, String host, String port, String path,
			String query) {
		if (this.locationPattern == null) {
			this.initRewriteNode();
		}
		final Element locationMatch = this.doc.createElement("locationMatch");

		final Element location = this.doc.createElement("location");
		locationMatch.appendChild(location);

		XMLParserLib.appendNewTextNode(this.doc, location, "protocol",
				!protocol.equals(Protocol.NONE) ? protocol.toString().toLowerCase() : "");
		XMLParserLib.appendNewTextNode(this.doc, location, "host", host);
		XMLParserLib.appendNewTextNode(this.doc, location, "port", port);
		XMLParserLib.appendNewTextNode(this.doc, location, "path", path);
		XMLParserLib.appendNewTextNode(this.doc, location, "query", query);
		XMLParserLib.appendNewTextNode(this.doc, locationMatch, "enabled", "true");

		this.locationPattern.appendChild(locationMatch);
	}

	/**
	 * Creates a new rule. Some fields are not used for the specific RuleType,
	 * any value is fine for those. Must call saveConfigurations() to save to
	 * file.
	 *
	 * @param type
	 *            RuleType
	 * @param checkRequest
	 *            Should find in request
	 * @param checkResponse
	 *            Should find in response
	 * @param matchName
	 *            Name to match, can be empty
	 * @param isMatchNameRegex
	 *            Does matchName contain regex
	 * @param matchValue
	 *            Value to match, can be empty
	 * @param isMatchValueRegex
	 *            Does matchValue contain regex
	 * @param shouldMatchWholeValue
	 *            Only match whole value
	 * @param shouldMatchCaseSensitive
	 *            Match with case sensitive
	 * @param replaceName
	 *            Name to replace, can be empty
	 * @param isReplaceNameRegex
	 *            Does replaceName contain regex
	 * @param replaceValue
	 *            Value to replace, can be empty
	 * @param isReplaceValueRegex
	 *            Does replaceValue contain regex
	 * @param replaceType
	 *            RuleReplaceType
	 */
	public void addRule(RewriteRuleType type, boolean checkRequest, boolean checkResponse,
			String matchName, boolean isMatchNameRegex, String matchValue,
			boolean isMatchValueRegex, boolean shouldMatchWholeValue,
			boolean shouldMatchCaseSensitive, String replaceName, boolean isReplaceNameRegex,
			String replaceValue, boolean isReplaceValueRegex, RewriteRuleReplaceType replaceType) {
		if (this.ruleNode == null) {
			this.initRewriteNode();
		}
		final Element rewriteRule = this.doc.createElement("rewriteRule");

		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "active", "true");
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "ruleType", type.getRuleNumber());

		// Where //
		// if it shouldn't override and do checkRequest, return true
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchRequest",
				Boolean.toString(!this.shouldOverrideWhereRule(type) && checkRequest));
		// if it shouldn't override and do checkResponse, return true
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchResponse",
				Boolean.toString(!this.shouldOverrideWhereRule(type) && checkResponse));

		// Match //
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchHeader",
				!this.shouldOverrideMatchName(type) ? matchName : "");
		// if it shouldn't override and contains regex, return true
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchHeaderRegex",
				Boolean.toString(!this.shouldOverrideMatchName(type) && isMatchNameRegex));
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchValue", matchValue);
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchValueRegex",
				Boolean.toString(isMatchValueRegex));
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "matchWholeValue",
				Boolean.toString(shouldMatchWholeValue));
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "caseSensitive",
				Boolean.toString(shouldMatchCaseSensitive));

		// Replace //
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "newHeader", replaceName);
		// if it shouldn't override and contains regex, return true
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "newHeaderRegex",
				Boolean.toString(!this.shouldOverrideReplaceName(type) && isReplaceNameRegex));
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "newValue", replaceValue);
		// if it shouldn't override and contains regex, return true
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "newValueRegex",
				Boolean.toString(!this.shouldOverrideReplaceValue(type) && isReplaceValueRegex));
		XMLParserLib.appendNewTextNode(this.doc, rewriteRule, "replaceType",
				replaceType.getReplaceNum());

		this.ruleNode.appendChild(rewriteRule);
	}

	/**
	 * Check if the field is used for the specific RuleType
	 *
	 * @param t
	 *            RuleTyle
	 * @return true if field not used
	 */
	private boolean shouldOverrideWhereRule(RewriteRuleType t) {
		switch (t) {
			case HOST :
				// fall through
			case PATH :
				// fall through
			case URL :
				// fall through
			case ADD_QUERY_PARAM :
				// fall through
			case MODIFY_QUERY_PARAM :
				// fall through
			case REMOVE_QUERY_PARAM :
				// fall through
			case RESPONSE_STATUS :
				return true;
			default :
				return false;
		}
	}

	/**
	 * Check if the field is used for the specific RuleType
	 *
	 * @param t
	 *            RuleTyle
	 * @return true if field not used
	 */
	private boolean shouldOverrideMatchName(RewriteRuleType t) {
		switch (t) {
			case HOST :
				// fall through
			case PATH :
				// fall through
			case URL :
				// fall through
			case RESPONSE_STATUS :
				// fall through
			case BODY :
				return true;
			default :
				return false;
		}
	}

	/**
	 * Check if the field is used for the specific RuleType
	 *
	 * @param t
	 *            RuleTyle
	 * @return true if field not used
	 */
	private boolean shouldOverrideReplaceName(RewriteRuleType t) {
		switch (t) {
			case REMOVE_HEADER :
				// fall through
			case HOST :
				// fall through
			case PATH :
				// fall through
			case URL :
				// fall through
			case REMOVE_QUERY_PARAM :
				// fall through
			case RESPONSE_STATUS :
				// fall through
			case BODY :
				return true;
			default :
				return false;
		}
	}

	/**
	 * Check if the field is used for the specific RuleType
	 *
	 * @param t
	 *            RuleTyle
	 * @return true if field not used
	 */
	private boolean shouldOverrideReplaceValue(RewriteRuleType t) {
		switch (t) {
			case REMOVE_HEADER :
				// fall through
			case REMOVE_QUERY_PARAM :
				return true;
			default :
				return false;
		}
	}

	// ---------- Charles Network Throttling ---------- //

	/**
	 * Values modified from Charles Presets. Must call saveConfigurations() to
	 * save to file. Using
	 * https://developers.google.com/web/tools/chrome-devtools/network-
	 * performance/network-conditions
	 *
	 * @param presets
	 *            Different mobile connection types
	 */
	public void usePreset(ThrottlingPresets presets) {
		try {
			switch (presets) {
				case FOUR_G :
					this.setThrottling(4096, 3072, 100, 100, 20, 576, 100, 100, 100, 100);
					break;
				case THREE_G :
					this.setThrottling(1024, 750, 100, 100, 100, 576, 100, 100, 100, 100);
					break;
				case TWO_G :
					this.setThrottling(250, 50, 100, 100, 300, 576, 100, 100, 100, 100);
					break;
				default :
					break;
			}
		} catch (final CharlesProxyException e) {
			// will never hit because all values are pre-defined
		}
	}

	/**
	 * Set the throttling configurations. Must call saveConfigurations() to save
	 * to file.
	 *
	 * @param bandwidthDown
	 *            in kbps
	 * @param bandwidthUp
	 *            in kbps
	 * @param utilizationDown
	 *            between 0 - 100%
	 * @param utilizationUp
	 *            between 0 - 100%
	 * @param roundTripLatency
	 *            in ms
	 * @param mtu
	 *            in bytes
	 * @param reliability
	 *            between 0 - 100%
	 * @param stability
	 *            between 0 - 100%
	 * @param unstableQualityRangeLow
	 *            between 0 - 100%
	 * @param unstableQualityRangeHigh
	 *            between 0 - 100%
	 * @throws CharlesProxyException
	 *             Thrown if invalid numbers
	 */
	public void setThrottling(double bandwidthDown, double bandwidthUp, int utilizationDown,
			int utilizationUp, int roundTripLatency, int mtu, int reliability, int stability,
			int unstableQualityRangeLow, int unstableQualityRangeHigh)
			throws CharlesProxyException {
		if ((bandwidthDown < 0) || (bandwidthUp < 0) || (utilizationDown < 0)
				|| (utilizationDown > 100) || (utilizationUp < 0) || (utilizationUp > 100)
				|| (roundTripLatency < 0) || (mtu < 0) || (reliability < 0) || (reliability > 100)
				|| (stability < 0) || (stability > 100) || (unstableQualityRangeLow < 0)
				|| (unstableQualityRangeLow > 100) || (unstableQualityRangeHigh < 0)
				|| (unstableQualityRangeHigh > 100)) {
			throw new CharlesProxyException(
					"One of the arguments is either negative or above 100%");
		}
		final Node throttlingNode = this.getToolConfiguration(ToolType.NETWORK_THROTTLING);

		XMLParserLib.findFirstChildNode("bandwidthDown", throttlingNode)
				.setTextContent(bandwidthDown + "");
		XMLParserLib.findFirstChildNode("bandwidthUp", throttlingNode)
				.setTextContent(bandwidthUp + "");
		XMLParserLib.findFirstChildNode("utilisationDown", throttlingNode)
				.setTextContent(utilizationDown + "");
		XMLParserLib.findFirstChildNode("utilisationUp", throttlingNode)
				.setTextContent(utilizationUp + "");
		XMLParserLib.findFirstChildNode("latency", throttlingNode)
				.setTextContent(roundTripLatency + "");
		XMLParserLib.findFirstChildNode("mtu", throttlingNode).setTextContent(mtu + "");
		XMLParserLib.findFirstChildNode("reliability", throttlingNode)
				.setTextContent(reliability + "");
		XMLParserLib.findFirstChildNode("stability", throttlingNode).setTextContent(stability + "");
		XMLParserLib.findFirstChildNode("lowQuality", throttlingNode)
				.setTextContent(unstableQualityRangeLow + "");
		XMLParserLib.findFirstChildNode("highQuality", throttlingNode)
				.setTextContent(unstableQualityRangeHigh + "");
	}

	/**
	 * Configures Charles to drop or return 403 forbidden the given URL pattern
	 *
	 * @param protocol
	 *            Use Protocol enum to send HTTP or HTTPS
	 * @param host
	 *            URL host
	 * @param port
	 *            URL port. Can be blank to match all. 443 is default forr https
	 *            calls
	 * @param path
	 *            URL path. Can be blank to match all.
	 * @param query
	 *            URL query. Can be blank to match all.
	 * @param actionString
	 *            "0" to drop connection for the given URL, "1" to return a 403
	 *            forbidden
	 */
	public void addLocationToBlackList(Protocol protocol, String host, String port, String path,
			String query, String actionString) {
		// Get blacklist root node
		final Element blackListNode = (Element)this.getToolConfiguration(ToolType.BLACK_LIST);

		// Get down to locationPatterns node so we can add a locationMatch
		final Element locations = (Element)blackListNode.getElementsByTagName("locations").item(0);
		final Element locationPatterns = (Element)locations.getElementsByTagName("locationPatterns")
				.item(0);

		// Create locationMatch and child location nodes. Add the info to the
		// location node
		final Element locationMatch = this.doc.createElement("locationMatch");
		final Element location = this.doc.createElement("location");
		XMLParserLib.appendNewTextNode(this.doc, location, "protocol",
				!protocol.equals(Protocol.NONE) ? protocol.toString().toLowerCase() : "");
		XMLParserLib.appendNewTextNode(this.doc, location, "host", host);
		XMLParserLib.appendNewTextNode(this.doc, location, "port", port);
		XMLParserLib.appendNewTextNode(this.doc, location, "path", path);
		XMLParserLib.appendNewTextNode(this.doc, location, "query", query);
		XMLParserLib.appendNewTextNode(this.doc, locationMatch, "enabled", "true");

		// Append location to locationMatch and locationMatch to
		// locationPatterns
		locationMatch.appendChild(location);
		locationPatterns.appendChild(locationMatch);

		// Enable the tool itself, using the selected locations, and set action
		// to 0
		// action = 0 -> drop connections
		// action = 1 -> return 403
		final Element toolEnabled = (Element)blackListNode.getElementsByTagName("toolEnabled")
				.item(0);
		final Element useSelectedLocations = (Element)blackListNode
				.getElementsByTagName("useSelectedLocations").item(0);
		final Element action = (Element)blackListNode.getElementsByTagName("action").item(0);
		toolEnabled.setTextContent("true");
		useSelectedLocations.setTextContent("true");
		action.setTextContent(actionString);
	}

	/**
	 * Enable/Disable starting the Mac OSX proxy at Charles startup
	 *
	 * @param enable
	 *            true to enable at startup, false to disable. Mac OSX proxy is
	 *            off in the default config file.
	 */
	public void enableMacOsxProxy(boolean enable) {
		final Node proxyConfig = XMLParserLib.findFirstChildNode("proxyConfiguration", this.root);
		final Node macOsxConfig = XMLParserLib.findFirstChildNode("macOSXConfiguration",
				proxyConfig);
		XMLParserLib.findFirstChildNode("enableAtStartup", macOsxConfig)
				.setTextContent(Boolean.toString(enable));
	}

	public void setPort(int port) {
		Node proxyConfig = XMLParserLib.findFirstChildNode("proxyConfiguration", this.root);
		Node portConfig = XMLParserLib.findFirstChildNode("port", proxyConfig);
		portConfig.setTextContent(Integer.toString(port));
	}

	/**
	 * Method to create charles configuration to only record given hosts.
	 *
	 * @param hosts
	 *            - Hosts names to report in HAR.
	 */
	public void addRecordingUrls(String[] hosts) {
		final Element recordingConfiguration = (Element)XMLParserLib
				.findFirstChildNode("recordingConfiguration", this.root);
		final Element recordHosts = (Element)recordingConfiguration
				.getElementsByTagName("recordHosts").item(0);
		final Element locationPatterns = (Element)recordHosts
				.getElementsByTagName("locationPatterns").item(0);
		final Element child = (Element)locationPatterns.getElementsByTagName("locationMatch")
				.item(0);
		if (child != null) {
			locationPatterns.removeChild(child);
		}

		for (int i = 0; i < hosts.length; i++) {
			final Element locationMatch = this.doc.createElement("locationMatch");
			final Element location = this.doc.createElement("location");
			XMLParserLib.appendNewTextNode(this.doc, location, "protocol", "*");
			XMLParserLib.appendNewTextNode(this.doc, location, "host", hosts[i]);
			XMLParserLib.appendNewTextNode(this.doc, location, "port", "*");
			locationMatch.appendChild(location);
			XMLParserLib.appendNewTextNode(this.doc, locationMatch, "enabled", "true");
			locationPatterns.appendChild(locationMatch);
		}
		recordHosts.appendChild(locationPatterns);
	}
}
