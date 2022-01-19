package  twc.Automation.General;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import io.appium.java_client.MobileElement;
import twc.Automation.HandleWithAppium.AppiumFunctions;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.HandleWithCharles.CharlesProxy;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;
import twc.Automation.utils.StringUtils;
import twc.Automation.General.DeviceStatus;

public class Utils extends Functions {

	public static File outfile = null;
	public static List<String> customParamsList = new ArrayList<String>();
	public static List<String> listOf_b_Params = new ArrayList<String>();
	public static List<String> listOf_criteo_Params = new ArrayList<String>();
	public static int aaxbidErrorCount = 0;
	public static int criteoparamErrorCount = 0;
	public static int aaxcallsSize = 0;
	public static boolean isaaxgampadcallexists = false;
	public static int criteocallsSize = 0;
	public static int aaxcallsResponseSize = 0;
	public static int criteocallsResponseSize = 0;
	public static int criteogampadcallcount = 0;
	public static String previous_IPAddress = null;
	public static String current_IPAddress = null;
	public static LinkedHashMap<String, String> feedCardsMap = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> feedAdCardsMap = new LinkedHashMap<String, String>();
	public static int feedCardCurrentPosition = 3;
	public static String[] initialCardNameCheckList = { "anchor_tempLabel", "anchor_weatherImage",
			"anchor_feelsLikeLabel", "anchor_hiLowLabel", "TodayTab", "HourlyTab", "DailyTab", "Morning", "Afternoon",
			"Evening", "Overnight" };
	public static boolean nextGenIMadDisplayed = false;
	public static boolean interStitialChecked = false;
	public static boolean interStitialDisplayed = false;
	public static boolean interStitialAdcallPresent = false;
	public static boolean unlimitedInterstitial = false;
	public static String Exception = null;
	public static int feedAdCount = 0;
	public static String videoIUValue = "iu=%2F7646%2Fapp_android_us%2Fvideo";
//public static String videoIUValue = "iu=%2F7646%2Fapp_android_us%2Fweather%2Fsevere%2Ftropical";
	//public static String videoIUValue = "iu=%2F7646%2Fapp_android_us%2Fweather%2Fsevere%2Fwinter";
	//public static String videoIUValue =null;
	public static String iuId = null;
	public enum CardNames {
		video, today, news, aq, maps, daily
	}


	
	public static void set_PreviousIPAddress() throws Exception {
		FileOutputStream fos = new FileOutputStream(properties.getProperty("dataFilePath"));

		properties.setProperty("previous_IPAddress", current_IPAddress);
		properties.store(fos, "Previous IP Address stotred");
		fos.close();
	}

	/*
	 * Below method generates an xml file for the generated charles session file of
	 * extension .chlsx
	 */
	
	public static boolean createXMLFileForCharlesSessionFile() throws Exception {
		FileInputStream instream = null;
		FileOutputStream outstream = null;
		read_excel_data.exceldataread("Paths");
		String[][] paths = read_excel_data.exceldataread("Paths");
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		// String path = System.getProperty("user.dir") + "/CapturedSessionFile/";
		// Read the file name from the folder
		File folder = new File(paths[4][1]);
		// File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		String fileName = null;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileName = file.getName();
				System.out.println("File Name is : " + fileName);
				logStep("File Name is : " + fileName);
			}
		}
		try {
			// File file = new File(System.getProperty("user.dir")+"/DataFile.Properties");
			// File infile = new File(path + fileName);
			File infile = new File(paths[4][1] + fileName);
			// File infile = new
			// File("/Users/narasimhanukala/git/ads-automation/ios_Smoke_Automation/ArchivedSessions/charles202002212053.chlsx");
			// File outfile = new
			// File("/Users/narasimhanukala/git/ads-automation/ios_Smoke_Automation/charles/myoutputFile.xml");
			outfile = new File(System.getProperty("user.dir") + "/myoutputFile.xml");

			instream = new FileInputStream(infile);
			outstream = new FileOutputStream(outfile);

			byte[] buffer = new byte[1024];

			int length;
			/*
			 * copying the contents from input stream to output stream using read and write
			 * methods
			 */
			while ((length = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, length);
			}

			// Closing the input/output file streams
			instream.close();
			outstream.close();

			System.out.println("Successfully Generated XML file from Charles session file!!");
			logStep("Successfully Generated XML file from Charles session file!!");
			return true;
		} catch (Exception e) {
			System.out.println("Failed to Generate XML file from Charles session file");
			logStep("Failed to Generate XML file from Charles session file");
			e.printStackTrace();
			return false;
		}

	}
	public static void verifyPubadCal(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		// outfile = new File(System.getProperty("user.dir") + "/myoutputFile.xml");
		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
		//	iuId = videoIUValue;
		} else if (sheetName.equalsIgnoreCase("IDD")) {
			//String today = dailyDetailsDayOfWeek.concat("1");
			iuId = data[11][1];
			//iuId = iuId.concat("_") + today;
		} 
		else if (sheetName.equalsIgnoreCase("DailyDetails")) {
			//String today = dailyDetailsDayOfWeek.concat("1");
			iuId = data[11][1]+currentday1;
			//iuId = iuId.concat("_") + today;
		}else {
			iuId = data[11][1];
		}
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");
			Assert.fail(iuId + " ad call is not present");

		}

	}

	public static void verifyPubadCal(String expiuId) throws Exception {

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		String iuId = expiuId;
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");
			Assert.fail(iuId + " ad call is not present");

		}

	}

	public static boolean isInterStitialAdCalExists(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		String iuId = data[17][1];
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
			interStitialAdcallPresent = true;
		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");
			// Assert.fail(iuId + " ad call is not present");
			interStitialAdcallPresent = false;
		}

		return interStitialAdcallPresent;
	}

	public static void verifyAAX_SlotId(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");

		// Read JSONs and get b value
		// List<String> jsonBValuesList = new ArrayList<String>();

		// String slotId = "153f5936-781f-4586-8fdb-040ce298944a";

		// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";
		String slotId = data[12][1];

		boolean flag = false;
		// List<String> istofRequestBodies = new ArrayList<String>();
		// List<String> istofResponseBodies = new ArrayList<String>();
		// List<String> listOf_b_Params = new ArrayList<String>();

		nodeList: for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Node) {
				Node node = nodeList.item(i);
				if (node.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					NodeList: for (int j = 0; j < nl.getLength(); j++) {
						Node innernode = nl.item(j);
						if (innernode != null) {
							if (innernode.getNodeName().equals("request")) {
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												if (eElement.getNodeName().equals("body")) {
													String content = eElement.getTextContent();
													if (content.contains(slotId)) {
														flag = true;
														// istofRequestBodies.add(content);

														break nodeList;

														// System.out.println("request body "+content);
													}
												}
											}
										}
									}

								}
							}

						}
					}

				}
			}

		}
		if (flag) {
			System.out.println("slot id: " + slotId + " is present");
			logStep("slot id: " + slotId + " is present");

		} else {
			System.out.println("slot id: " + slotId + " is not present");
			logStep("slot id: " + slotId + " is not present");
			Assert.fail("slot id: " + slotId + " is not present");
		}

	}

	public static void verifyAAX_SlotId(String excelName, String sheetName, boolean expected) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");

		// Read JSONs and get b value
		// List<String> jsonBValuesList = new ArrayList<String>();

		// String slotId = "153f5936-781f-4586-8fdb-040ce298944a";

		// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";
		String slotId = data[21][1];

		boolean flag = false;
		// List<String> istofRequestBodies = new ArrayList<String>();
		// List<String> istofResponseBodies = new ArrayList<String>();
		// List<String> listOf_b_Params = new ArrayList<String>();

		nodeList: for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Node) {
				Node node = nodeList.item(i);
				if (node.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					NodeList: for (int j = 0; j < nl.getLength(); j++) {
						Node innernode = nl.item(j);
						if (innernode != null) {
							if (innernode.getNodeName().equals("request")) {
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												if (eElement.getNodeName().equals("body")) {
													String content = eElement.getTextContent();
													if (content.contains(slotId)) {
														flag = true;
														// istofRequestBodies.add(content);

														break nodeList;

														// System.out.println("request body "+content);
													}
												}
											}
										}
									}

								}
							}

						}
					}

				}
			}

		}
		if (flag) {
			System.out.println("slot id: " + slotId + " is present");
			logStep("slot id: " + slotId + " is present");

		} else {
			System.out.println("slot id: " + slotId + " is not present");
			logStep("slot id: " + slotId + " is not present");
			// Assert.fail("slot id: " + slotId + " is not present");
		}
		/*
		 * if (flag != expected) { System.out.println(sheetName +
		 * " :AAX slot id Verification is failed"); logStep(sheetName +
		 * " :AAX slot id Verification is failed"); Assert.fail(sheetName +
		 * " :AAX slot id Verification is failed"); }
		 */

		if (expected == flag) {
			System.out.println(sheetName + " : AAX slot id Verification is successfull");
			logStep(sheetName + " : AAX slot id Verification is successfull");

		} else {
			System.out.println(sheetName + " :AAX slot id Verification is failed");
			logStep(sheetName + " :AAX slot id Verification is failed");

			if (expected) {
				System.out.println(sheetName + " :AAX slot id expected to present but it not exists");
				logStep(sheetName + " :AAX slot id expected to present but it not exists");
				Assert.fail(sheetName + " :AAX slot id expected to present but it not exists");
			} else {
				System.out.println(sheetName + " :AAX slot id is not expected to present but it exists");
				logStep(sheetName + " :AAX slot id is not expected to present but it exists");
				Assert.fail(sheetName + " :AAX slot id is not expected to present but it exists");
			}
		}

	}

	private static List<String> evaluateXPath(Document document, String xpathExpression) throws Exception {
		// Create XPathFactory object
		XPathFactory xpathFactory = XPathFactory.newInstance();
		// Create XPath object
		XPath xpath = xpathFactory.newXPath();
		List<String> values = new ArrayList<String>();
		try {
			// Create XPathExpression object
			XPathExpression expr = xpath.compile(xpathExpression);
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				values.add(nodes.item(i).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return values;
	}
	private static String getCustomParamBy_iu_value(String qryValue, String cust_param) {
		List<String> listOfUisQrys = new ArrayList<String>();
		String cust_params = "";
		String[] key = null;
		// if (qryValue != null && qryValue.contains("cust_params")) {
		if (qryValue != null && qryValue.contains("cust_params")) {
			cust_params = qryValue.substring(qryValue.indexOf("cust_params"));
			cust_params = cust_params.replace("%26", "&");
			cust_params = cust_params.replace("%2C", ",");
			cust_params = cust_params.replace("%3D", "=");
		}
		if (cust_params.indexOf(cust_param) > 0) {
			try {
				cust_params = cust_params.substring(cust_params.indexOf("&" + cust_param + "="));
				cust_params = cust_params.substring(cust_params.indexOf(cust_param));
			} catch (Exception e) {
				cust_params = cust_params.substring(cust_params.indexOf("=" + cust_param + "="));
				cust_params = cust_params.substring(cust_params.indexOf(cust_param));
			}
			// cust_params = cust_params.substring(cust_params.indexOf(cust_param));
			String b[] = cust_params.split("&");
			cust_params = b[0];
			key = cust_params.split("=");
			cust_params = key[1];
		} else {
			cust_params = "";
		}
		if (cust_param.equalsIgnoreCase("ct")) {
			cust_params = cust_params.replaceAll("_", " ");
		}

		return cust_params;
	}

	private static String getNonCustomParamBy_iu_value(String qryValue, String cust_param) {
		List<String> listOfUisQrys = new ArrayList<String>();
		String cust_params = "";
		String[] key = null;
		// if (qryValue != null && qryValue.contains("cust_params")) {
		if (qryValue != null && qryValue.contains(cust_param)) {
			cust_params = qryValue.substring(qryValue.indexOf(cust_param));
			cust_params = cust_params.replace("%26", "&");
			cust_params = cust_params.replace("%2C", ",");
			cust_params = cust_params.replace("%3D", "=");
			cust_params = cust_params.replace("%2F", "/");
			cust_params = cust_params.replace("%3A", ":");
			cust_params = cust_params.replace("%3F", "?");
		}
		if (cust_params.indexOf(cust_param) >= 0) {
			try {
				cust_params = cust_params.substring(cust_params.indexOf(cust_param + "="));
				cust_params = cust_params.substring(cust_params.indexOf(cust_param));
			} catch (Exception e) {
				cust_params = cust_params.substring(cust_params.indexOf("&" + cust_param + "="));
				cust_params = cust_params.substring(cust_params.indexOf(cust_param));
			}
			// cust_params = cust_params.substring(cust_params.indexOf(cust_param));
			String b[] = cust_params.split("&");
			cust_params = b[0];
			key = cust_params.split("=");
			cust_params = key[1];
		} else {
			cust_params = "";
		}
		return cust_params;
	}

	private static String get_b_value_inJsonResponseBody(String qryValue) {
		String b_paramValue = "";
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(qryValue);
			Object obj = checkKey(json, "b");
			if (obj != null) {
				b_paramValue = obj.toString();
			} else {
				// inorder to not to disturb the existing method structure, when there is no
				// bidding happens i.e. response contains error, returning error explicitly
				b_paramValue = "error";
			}
		} catch (ParseException e) {
			// inorder to not to disturb the existing method structure, when there is no
			// bidding happens i.e. response contains error, returning error explicitly
			b_paramValue = "error";
			e.printStackTrace();
		}

		return b_paramValue;
	}

	private static String get_Param_Value_inJsonBody(String qryValue, String param) {
		String b_paramValue = "";
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(qryValue);
			Object obj = checkKey(json, param);
			if (obj != null) {
				b_paramValue = obj.toString();
			} else {
				// inorder to not to disturb the existing method structure, when there is no
				// bidding happens i.e. response contains error, returning error explicitly
				b_paramValue = "error";
			}
		} catch (ParseException e) {
			// inorder to not to disturb the existing method structure, when there is no
			// bidding happens i.e. response contains error, returning error explicitly
			b_paramValue = "error";
			e.printStackTrace();
		}

		return b_paramValue;
	}
	
	// To get the value for "b". Here key is -> 'b'
	public static Object checkKey(JSONObject object, String searchedKey) {
		boolean exists = object.containsKey(searchedKey);
		Object obj = null;
		if (exists) {
			obj = object.get(searchedKey);
		}
		if (!exists) {
			Set<String> keys = object.keySet();
			for (String key : keys) {
				if (object.get(key) instanceof JSONObject) {
					obj = checkKey((JSONObject) object.get(key), searchedKey);
				}
			}
		}
		return obj;
	}

	public static void verifyAPICal(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@host";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		String adURL = data[2][1];
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(adURL)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println(adURL + " ad call is present");
			logStep(adURL + " ad call is present");
		} else {
			System.out.println(adURL + " ad call is not present");
			logStep(adURL + " ad call is not present");
			Assert.fail(adURL + " ad call is not present");

		}

	}

	public static void verifyAPICal(String excelName, String sheetName, boolean expected) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@host";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		String adURL = data[2][1];
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(adURL)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println(adURL + " ad call is present");
			logStep(adURL + " ad call is present");
		} else {
			System.out.println(adURL + " ad call is not present");
			logStep(adURL + " ad call is not present");

		}

		if (expected == iuExists) {
			System.out.println(sheetName + " :API Call Verification is successfull");
			logStep(sheetName + " :API Call Verification is successfull");

		} else {
			System.out.println(sheetName + " :API Call Verification is failed");
			logStep(sheetName + " :API Call Verification is failed");
			if (expected) {
				System.out.println(sheetName + " :API Call expected to present but it not exists");
				logStep(sheetName + " :API Call expected to present but it not exists");
				Assert.fail(sheetName + " :API Call expected to present but it not exists");
			} else {
				System.out.println(sheetName + " :API Call is not expected to present but it exists");
				logStep(sheetName + " :API Call is not expected to present but it exists");
				Assert.fail(sheetName + " :API Call is not expected to present but it exists");
			}
		}

	}

	public static void verifyBGAd_byCallResponse(String excelName, String sheetName, String adType) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String bgAssetCall = null;

		if (adType.equalsIgnoreCase("Static")) {
			String staticbgAssetCall = data[8][1];
			bgAssetCall = staticbgAssetCall;
			/*
			 * if (sheetName.equalsIgnoreCase("IntegratedFeedCard")) { // as Integrated Feed
			 * Card Static BG Asset call is dynamic based on cnd and // dynght parameters,
			 * hence retrieving the parameters and framing asset call String cndValue =
			 * get_custom_param_val_of_gampad(excelName, sheetName, "cnd"); String
			 * dynghtValue = get_custom_param_val_of_gampad(excelName, sheetName, "dynght");
			 * bgAssetCall = staticbgAssetCall.concat("-").concat(cndValue).concat("-")
			 * .concat(dynghtValue.toLowerCase()).concat(".jpg"); }
			 */

		} else if (adType.equalsIgnoreCase("Video")) {
			String videobgAssetCall = data[10][1];
			bgAssetCall = videobgAssetCall;

		}

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@path";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		// String adURL = data[2][1];
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(bgAssetCall)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println("Charles response contains " + sheetName + " " + adType + " Asset Call: " + bgAssetCall);
			logStep("Charles response contains " + sheetName + " " + adType + " Asset Call: " + bgAssetCall);
		} else {
			System.out.println(
					"Charles response doesn't contains " + sheetName + " " + adType + " Asset Call: " + bgAssetCall);
			logStep("Charles response doesn't contains " + sheetName + " " + adType + " Asset Call: " + bgAssetCall);
			Assert.fail(
					"Charles response doesn't contains " + sheetName + " " + adType + " Asset Call: " + bgAssetCall);
		}

	}

	public static void verifyFGAd_byCallResponse(String excelName, String sheetName, String adType) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String fgAssetCall = null;

		if (adType.equalsIgnoreCase("Static")) {
			String staticfgAssetCall = data[9][1];
			fgAssetCall = staticfgAssetCall;

		} else if (adType.equalsIgnoreCase("Video")) {
			String videofgAssetCall = data[11][1];
			fgAssetCall = videofgAssetCall;

		}

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@path";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		// String adURL = data[2][1];
		boolean iuExists = false;

		for (String qry : getQueryList) {
			if (qry.contains(fgAssetCall)) {

				iuExists = true;

				break;

			}
		}
		if (iuExists) {
			System.out.println("Charles response contains " + sheetName + " " + adType + " Asset Call: " + fgAssetCall);
			logStep("Charles response contains " + sheetName + " " + adType + " Asset Call: " + fgAssetCall);
		} else {
			System.out.println(
					"Charles response doesn't contains " + sheetName + " " + adType + " Asset Call:" + fgAssetCall);
			logStep("Charles response doesn't contains " + sheetName + " " + adType + " Asset Call: " + fgAssetCall);
			Assert.fail(
					"Charles response doesn't contains " + sheetName + " " + adType + " Asset Call: " + fgAssetCall);
		}

	}

	public static boolean isNextGenIMorIFCall_hasResponse(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("IntegratedForecast")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		} else if (sheetName.equalsIgnoreCase("NextGenIM")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fhome_screen%2Fmarquee";
		} else {
			iuId = data[18][1];
		}

		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				iuExists = true;
				break;
			}
		}
		boolean flag = false;
		boolean resflag = false;
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
			outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
				// System.out.println("Total transactions: "+nodeList.getLength());
				if (nodeList.item(p) instanceof Node) {
					Node node = nodeList.item(p);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							// System.out.println("node1 length is: "+nl.getLength());
							Node innernode = nl.item(j);
							if (innernode != null) {
								// System.out.println("Innernode name is: "+innernode.getNodeName());
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											// System.out.println("node2 length is: "+n2.getLength());
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													// System.out.println("Innernode2 element name is:
													// "+eElement.getNodeName());
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			if (content.contains(iuId)) {
																				flag = true;
																				// System.out.println("request body
																				// "+content);
																				// istofRequestBodies.add(content);
																				// System.out.println("request body
																				// found "+content);
																				// break;
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}

								if (flag) {
									// System.out.println("Exiting after found true ");
									// System.out.println("checking innernode name is: "+innernode.getNodeName());
									if (innernode.getNodeName().equals("response")) {
										// System.out.println(innernode.getNodeName());
										if (innernode.hasChildNodes()) {
											NodeList n2 = innernode.getChildNodes();
											for (int k = 0; k < n2.getLength(); k++) {
												Node innernode2 = n2.item(k);
												if (innernode2 != null) {
													if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
														Element eElement = (Element) innernode2;
														if (eElement.getNodeName().equals("body")) {
															String content = eElement.getTextContent();
															// System.out.println("response body "+content);
															if (content.contains(data[13][1])) {
																resflag = true;
																break outerloop;

															}
														}
													}
												}
											}
										}
									}

								}
								// break;
							}
						}
					}
				}
			flag = false;
			}

		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");

		}

		// return flag;

		// Get Pubad call from

		if (resflag) {
			System.out.println(sheetName + " call has response, hence " + sheetName + " Ad to be displayed");
			logStep(sheetName + " call has response, hence " + sheetName + " Ad to be displayed");
			return resflag;
		} else {
			System.out
					.println(sheetName + " call doesnt have response, hence " + sheetName + " Ad not to be displayed");
			logStep(sheetName + " call doesnt have response, hence " + sheetName + " Ad not to be displayed");
			return resflag;
		}

	}

	public static void verify_Ad_Size(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String expSize = data[12][1];

		

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		// String iuId = data[18][1];

		if (sheetName.contains("NextGenIM")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fhome_screen%2Fmarquee";
		} else if (sheetName.equalsIgnoreCase("IDD")) {
			//String today = dailyDetailsDayOfWeek.concat("1");
			//iuId = data[18][1];
		//	iuId = iuId.concat("_") + today;
		} else {
			iuId = data[18][1];
		}

		boolean iuExists = false;
		boolean sizeExists = false;
		String actualSize = null;

		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {

				iuExists = true;
				String[] qrylist = qry.split("&");

				for (int i = 0; i < qrylist.length; i++) {
					if (qrylist[i].contains("sz=")) {
						actualSize = qrylist[i];
						break;
					}
				}

				/*
				 * if (qry.contains(expSize)) {
				 * 
				 * sizeExists = true;
				 * 
				 * break; break;
				 * 
				 * }
				 */
				if (expSize.contains(actualSize)) {
					sizeExists = true;
				}
				break;

			} else {
				if (sheetName.contains("Feed3")) {
					String iuId1 = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Ffeed%2Fhpvar_3";
					if (qry.contains(iuId1)) {

						iuExists = true;
						String[] qrylist = qry.split("&");

						for (int i = 0; i < qrylist.length; i++) {
							if (qrylist[i].contains("sz=")) {
								actualSize = qrylist[i];
								break;
							}
						}

						if (expSize.contains(actualSize)) {
							sizeExists = true;
						}
						iuId = iuId1;
						break;

					}
				}
			}
		}

		/*
		 * if (iuExists) { System.out.println(iuId + " ad call is present");
		 * logStep(iuId + " ad call is present"); } else { System.out.println(iuId +
		 * " ad call is not present"); logStep(iuId + " ad call is not present");
		 * Assert.fail(iuId + " ad call is not present");
		 * 
		 * }
		 * 
		 */

		/*
		 * boolean sizeExists = false;
		 * 
		 * for (String qry : getQueryList) {
		 * 
		 * if (qry.contains(size)) {
		 * 
		 * sizeExists = true;
		 * 
		 * break;
		 * 
		 * } }
		 */

		/*
		 * if (sizeExists) { System.out.println("IM/IF Ad Size in Charles Request is: "
		 * + size.replaceAll("%7C", "|"));
		 * logStep("IM/IF Ad Size in Charles Request is: " + size.replaceAll("%7C",
		 * "|")); } else {
		 * System.out.println("Charles Request doesn't not contains IM/IF Ad Size : " +
		 * size.replaceAll("%7C", "|"));
		 * logStep("Charles Request doesn't not contains IM/IF Ad Size : " +
		 * size.replaceAll("%7C", "|"));
		 * Assert.fail("Charles Request doesn't not contains IM/IF Ad Size : " +
		 * size.replaceAll("%7C", "|"));
		 * 
		 * }
		 */

		if (iuExists && sizeExists) {
			System.out.println(sheetName + " Ad Size in Charles Request is: " + actualSize.replaceAll("%7C", "|")
					+ " is matched with expected Size: " + expSize.replaceAll("%7C", "|"));
			logStep(sheetName + " Ad Size in Charles Request is: " + actualSize.replaceAll("%7C", "|")
					+ " is matched with expected Size: " + expSize.replaceAll("%7C", "|"));
		} else {
			System.out.println(sheetName + " ad size validation failed");
			logStep(sheetName + " ad size validation failed");
			if (!iuExists) {
				System.out.println(iuId + " ad call is not present");
				logStep(iuId + " ad call is not present");
				Assert.fail(iuId + " ad call is not present");

			} else {
				System.out.println(iuId + " ad call is present & Size of ad: " + actualSize
						+ " is not matched with expected Size: " + expSize.replaceAll("%7C", "|"));
				logStep(iuId + " ad call is present & Size of ad: " + actualSize
						+ " is not matched with expected Size: " + expSize.replaceAll("%7C", "|"));
				Assert.fail(iuId + " ad call is present & Size of ad: " + actualSize
						+ " is not matched with expected Size: " + expSize.replaceAll("%7C", "|"));
			}

		}

	}

	public static boolean isIDDCall_hasResponse(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		File fXmlFile = new File(outfile.getName());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;

		String sdir = "dir: 'https://s.w-x.co/cl/'";
		String sdirClient = "dirClient: 'wxcl/'";
		String sdirFolder = "dirFolder: 'prototype/idd/'";
		String simgID = "imgID: 'example-static/500x600-bg-guides-green.jpg'";

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("IntegratedForecast")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		} else if (sheetName.equalsIgnoreCase("NextGenIM")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fhome_screen%2Fmarquee";
		} else if (sheetName.equalsIgnoreCase("IDD")) {
			/*String today = dailyDetailsDayOfWeek.concat("1");
			iuId = data[18][1];
			iuId = iuId.concat("_") + today;*/
			// iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fdetails%2F10day_fri1";

		} else {
			iuId = data[18][1];
		}

		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				iuExists = true;
				break;
			}
		}
		boolean flag = false;
		boolean resflag = false;
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
			outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
				// System.out.println("Total transactions: "+nodeList.getLength());
				if (nodeList.item(p) instanceof Node) {
					Node node = nodeList.item(p);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							// System.out.println("node1 length is: "+nl.getLength());
							Node innernode = nl.item(j);
							if (innernode != null) {
								// System.out.println("Innernode name is: "+innernode.getNodeName());
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											// System.out.println("node2 length is: "+n2.getLength());
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													// System.out.println("Innernode2 element name is:
													// "+eElement.getNodeName());
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			if (content.contains(iuId)) {
																				flag = true;
																				// System.out.println("request body
																				// "+content);
																				// istofRequestBodies.add(content);
																				// System.out.println("request body
																				// found "+content);
																				// break;
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}

								if (flag) {
									// System.out.println("Exiting after found true ");
									// System.out.println("checking innernode name is: "+innernode.getNodeName());
									if (innernode.getNodeName().equals("response")) {
										// System.out.println(innernode.getNodeName());
										if (innernode.hasChildNodes()) {
											NodeList n2 = innernode.getChildNodes();
											for (int k = 0; k < n2.getLength(); k++) {
												Node innernode2 = n2.item(k);
												if (innernode2 != null) {
													if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
														Element eElement = (Element) innernode2;
														if (eElement.getNodeName().equals("body")) {
															String content = eElement.getTextContent();
															// System.out.println("response body "+content);
															if (content.contains(sdir)) {
																System.out.println(sdir);
																if (content.contains(sdirClient)) {
																	System.out.println(sdirClient);
																	if (content.contains(sdirFolder)) {
																		System.out.println(sdirFolder);
																		if (content.contains(simgID)) {
																			System.out.println(simgID);
																			// System.out.println("response body
																			// "+content);
																			resflag = true;
																			break outerloop;
																		}
																	}
																}

															}
														}
													}
												}
											}
										}
									}

								}
								// break;
							}
						}
					}
				}
			flag = false;
			}

		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");

		}

		// return flag;

		// Get Pubad call from

		if (resflag) {
			System.out
					.println(sheetName + " call response contains IM URL, hence " + sheetName + " Ad to be displayed");
			logStep(sheetName + " call response contains IM URL, hence " + sheetName + " Ad to be displayed");
			return resflag;
		} else {
			System.out.println(
					sheetName + " call response doesnt have IM URL, hence " + sheetName + " Ad not to be displayed");
			logStep(sheetName + " call response doesnt have IM URL, hence " + sheetName + " Ad not to be displayed");
			return resflag;
		}

	}

	public static void Verify_Gampad_Call_ByResponseText(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String expectedValue = data[13][1];
		File fXmlFile = new File(outfile.getName());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("IntegratedForecast")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		} else if (sheetName.equalsIgnoreCase("NextGenIM")) {
			iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fhome_screen%2Fmarquee";
		} else if (sheetName.equalsIgnoreCase("IDD")) {
			/*String today = dailyDetailsDayOfWeek.concat("1");
			iuId = data[18][1];
			iuId = iuId.concat("_") + today;*/
			// iuId = "iu=%2F7646%2Ftest_app_iphone_us%2Fdb_display%2Fdetails%2F10day_fri1";

		} else {
			iuId = data[18][1];
		}

		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				iuExists = true;
				break;
			}
		}
		boolean flag = false;
		boolean resflag = false;
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
			outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
				// System.out.println("Total transactions: "+nodeList.getLength());
				if (nodeList.item(p) instanceof Node) {
					Node node = nodeList.item(p);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							// System.out.println("node1 length is: "+nl.getLength());
							Node innernode = nl.item(j);
							if (innernode != null) {
								// System.out.println("Innernode name is: "+innernode.getNodeName());
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											// System.out.println("node2 length is: "+n2.getLength());
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													// System.out.println("Innernode2 element name is:
													// "+eElement.getNodeName());
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			if (content.contains(iuId)) {
																				flag = true;
																				// System.out.println("request body
																				// "+content);
																				// istofRequestBodies.add(content);
																				// System.out.println("request body
																				// found "+content);
																				// break;
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}

								if (flag) {
									// System.out.println("Exiting after found true ");
									// System.out.println("checking innernode name is: "+innernode.getNodeName());
									if (innernode.getNodeName().equals("response")) {
										// System.out.println(innernode.getNodeName());
										if (innernode.hasChildNodes()) {
											NodeList n2 = innernode.getChildNodes();
											for (int k = 0; k < n2.getLength(); k++) {
												Node innernode2 = n2.item(k);
												if (innernode2 != null) {
													if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
														Element eElement = (Element) innernode2;
														if (eElement.getNodeName().equals("body")) {
															String content = eElement.getTextContent();
															// System.out.println("response body "+content);
															if (content.contains(expectedValue)) {
																System.out.println(expectedValue);
																resflag = true;
																break outerloop;

															}
														}
													}
												}
											}
										}
									}

								}
								// break;
							}
						}
					}
				}
			flag = false;
			}

			if (resflag) {
				System.out.println(sheetName + " call response contains " + expectedValue);
				logStep(sheetName + " call response contains " + expectedValue);

			} else {
				System.out.println(sheetName + " call response doesn't contains " + expectedValue);
				logStep(sheetName + " call response doesn't contains " + expectedValue);
				Assert.fail(sheetName + " call response doesn't contains " + expectedValue);
			}

		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");
			Assert.fail(iuId + " ad call is not present hence response validation is failed");
		}

	}

	// Verify Feed calls
	public static void Verify_feedcalls(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";

		String[] feedCards = data[7][1].toString().split(",");

		int feedCount = feedCards.length;
		String iuId = null;
		String cardsNotPresent = "";
		int failCount = 0;

		for (int i = 0; i < feedCount; i++) {
			iuId = data[5][1] + feedCards[i];
			if (i == 0) {
				iuId = data[1][1] + feedCards[i];
				boolean iuExists = false;

				for (String qry : getQueryList) {
					if (qry.contains(iuId)) {

						iuExists = true;

						break;

					}
				}
				if (iuExists) {
					System.out.println(feedCards[i] + " ad call is present");
					logStep(feedCards[i] + " ad call is present");
				} else {
					System.out.println(feedCards[i] + " ad call is not present");
					logStep(feedCards[i] + " ad call is not present");
					failCount++;
					// Assert.fail(feedCards[i] + " ad call is not present");
					cardsNotPresent = cardsNotPresent.concat(feedCards[i] + ", ");

				}
			} else {

				boolean iuExists = false;

				for (String qry : getQueryList) {
					if (qry.contains(iuId)) {

						iuExists = true;

						break;

					}
				}
				if (iuExists) {
					System.out.println(feedCards[i] + " ad call is present");
					logStep(feedCards[i] + " ad call is present");
				} else {
					System.out.println(feedCards[i] + " ad call is not present");
					logStep(feedCards[i] + " ad call is not present");
					failCount++;
					// Assert.fail(feedCards[i] + " ad call is not present");
					cardsNotPresent = cardsNotPresent.concat(feedCards[i] + ", ");

				}
			}
		}
		if (failCount > 0) {
			System.out.println(cardsNotPresent + " ad call is not present");
			logStep(cardsNotPresent + " ad call is not present");
			Exception = cardsNotPresent + " ad call is not present";
			Assert.fail(Exception);
		}

	}

	// Verify Feed calls of new feed ads IOSFLAG-3229
	public static void Verify_newfeedAdcalls(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";

		String[] feedCards = data[7][1].toString().split(",");

		int feedCount = feedCards.length;
		// String iuId = null;
		boolean iuExists = false;
		String cardsNotPresent = "";
		int failCount = 0;

		for (int i = 0; i < feedCount; i++) {

			if (i == 0) {
				if (!nextGenIMadDisplayed) {
					String iuId = data[1][1] + feedCards[i];
					System.out.println("iu  value is: " + iuId);
					logStep("iu  value is: " + iuId);
					iuExists = false;

					for (String qry : getQueryList) {
						if (qry.contains(iuId)) {

							iuExists = true;

							break;

						}
					}
					if (iuExists) {
						System.out.println(feedCards[i] + " ad call is present");
						logStep(feedCards[i] + " ad call is present");
					} else {
						System.out.println(feedCards[i] + " ad call is not present");
						logStep(feedCards[i] + " ad call is not present");
						failCount++;
						// Assert.fail(feedCards[i] + " ad call is not present");
						cardsNotPresent = cardsNotPresent.concat(feedCards[i] + ", ");

					}
				}

			} else {
				for (int j = 1; j <= feedAdCount; j++) {
					String iuId = data[5][1] + feedCards[i] + j;
					System.out.println("iu  value is: " + iuId);
					logStep("iu  value is: " + iuId);
					iuExists = false;

					for (String qry : getQueryList) {
						if (qry.contains(iuId)) {

							iuExists = true;

							break;

						} else {
							if (j == 3) {
								String currentFeedAd_Card1 = "hpvar_3";
								String iuId1 = data[5][1] + currentFeedAd_Card1;
								if (qry.contains(iuId1)) {
									iuExists = true;
									iuId = iuId1;
									break;
								}
							}
						}
					}
					if (iuExists) {
						System.out.println(feedCards[i] + j + " ad call is present");
						logStep(feedCards[i] + j + " ad call is present");
					} else {
						System.out.println(feedCards[i] + j + " ad call is not present");
						logStep(feedCards[i] + j + " ad call is not present");
						failCount++;
						// Assert.fail(feedCards[i] + " ad call is not present");
						cardsNotPresent = cardsNotPresent.concat(feedCards[i] + j + ", ");

					}
				}

			}
		}
		if (failCount > 0) {
			System.out.println(cardsNotPresent + " ad call is not present");
			logStep(cardsNotPresent + " ad call is not present");
			Exception = cardsNotPresent + " ad call is not present";
			Assert.fail(Exception);
		}

	}

	public static void verify_newfeeds_Ad_Size(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String size = data[10][1];



		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		// String iuId = data[18][1];

		boolean iuExists = false;
		boolean sizeExists = false;

		int feedAdCountConsidered = feedAdCount - 4;
		int feedCardNo = 5;
		String feedCardsSizeNotMatched = "";
		int failCount = 0;
		if (feedAdCountConsidered > 0) {
			for (int p = 0; p < feedAdCountConsidered; p++) {
				String iuId = data[5][1] + "feed_" + feedCardNo;
				iuExists = false;
				sizeExists = false;

				for (String qry : getQueryList) {
					if (qry.contains(iuId)) {

						iuExists = true;

						if (qry.contains(size)) {

							sizeExists = true;

							break;

						}

					}
				}
				if (iuExists && sizeExists) {
					System.out.println(iuId + " ad call is present & Size of ad matched with expected Size: " + size);
					logStep(iuId + " ad call is present & Size of ad matched with expected Size: " + size);
				} else {
					System.out.println("New feed feed_" + feedCardNo + "ad size validation failed");
					logStep("New feed feed_" + feedCardNo + "ad size validation failed");
					failCount++;
					// Assert.fail(feedCards[i] + " ad call is not present");
					feedCardsSizeNotMatched = feedCardsSizeNotMatched.concat("feed_" + feedCardNo + ", ");
					if (!iuExists) {
						System.out.println(iuId + " ad call is not present");
						logStep(iuId + " ad call is not present");

					} else {
						System.out.println(
								iuId + " ad call is present & Size of ad is not matched with expected Size: " + size);
						logStep(iuId + " ad call is present & Size of ad is not matched with expected Size: " + size);
					}

				}
				feedCardNo++;

			}
		} else {
			System.out.println("There are no more than " + feedCardNo + " feed ad cards");
			logStep("There are no more than " + feedCardNo + " feed ad cards");
			Assert.fail("There are no more than " + feedCardNo + " feed ad cards");
		}

		if (failCount > 0) {
			System.out.println(feedCardsSizeNotMatched + " Ads Size validation failed");
			logStep(feedCardsSizeNotMatched + " Ads Size validation failed");
			Exception = feedCardsSizeNotMatched + " Ads Size validation failed";
			Assert.fail(Exception);
		}

	}

	/*
	 * Till Feed_4 calls are preloaded, from feed_5 onwards amazon calls are
	 * generated after swiping through corresponding card.
	 */
	public static void verifyFeedAds_AAX_SlotIds(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");

		// Read JSONs and get b value
		// List<String> jsonBValuesList = new ArrayList<String>();

		// String slotId = "153f5936-781f-4586-8fdb-040ce298944a";

		// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";
		String[] feedCardsUUIDs = data[9][1].toString().split(",");

		int uuidCount = feedCardsUUIDs.length;
		int feedAdCountConsidered = feedAdCount - 4;
		int feedCardNo = 5;
		String feedCardsUUIDNotPresent = "";
		int failCount = 0;
		// for(int p = 0; p < uuidCount; p++) {
		for (int p = 0; p < feedAdCountConsidered; p++) {
			String slotId = feedCardsUUIDs[p];

			boolean flag = false;
			// List<String> istofRequestBodies = new ArrayList<String>();
			// List<String> istofResponseBodies = new ArrayList<String>();
			// List<String> listOf_b_Params = new ArrayList<String>();

			nodeList: for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i) instanceof Node) {
					Node node = nodeList.item(i);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						NodeList: for (int j = 0; j < nl.getLength(); j++) {
							Node innernode = nl.item(j);
							if (innernode != null) {
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("body")) {
														String content = eElement.getTextContent();
														if (content.contains(slotId)) {
															flag = true;
															// istofRequestBodies.add(content);

															break nodeList;

															// System.out.println("request body "+content);
														}
													}
												}
											}
										}

									}
								}

							}
						}

					}
				}

			}
			if (flag) {
				System.out.println("feed_" + feedCardNo + " slot id: " + slotId + " is present");
				logStep("feed_" + feedCardNo + " slot id: " + slotId + " is present");

			} else {
				System.out.println("slot id: " + slotId + " is not present");
				logStep("slot id: " + slotId + " is not present");
				// Assert.fail("slot id: " + slotId + " is not present");
				System.out.println("feed_" + feedCardNo + " slot id not present");
				logStep("feed_" + feedCardNo + " slot id not present");
				failCount++;
				// Assert.fail(feedCards[i] + " ad call is not present");
				feedCardsUUIDNotPresent = feedCardsUUIDNotPresent.concat("feed_" + feedCardNo + ", ");
			}
			feedCardNo++;
		}

		if (failCount > 0) {
			System.out.println(feedCardsUUIDNotPresent + " UUID is not present");
			logStep(feedCardsUUIDNotPresent + " UUID is not present");
			Exception = feedCardsUUIDNotPresent + " UUID is not present";
			Assert.fail(Exception);
		}

	}

	public static void validate_rdp_val_in_gampad_url(String excelName, String sheetName, boolean expected)
			throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			iuId = videoIUValue;
		} else {
			iuId = data[18][1];
		}
		boolean rdpExists = false;
		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				iuExists = true;
				if (qry.contains("rdp=1")) {
					rdpExists = true;
					// if (!"".equals(tempCustmParam))
					// customParamsList.add(getCustomParamsBy_iu_value(qry));
					break;
				}

			}

		}

		if (expected) {
			System.out.println("rdp keyword expected to present in URL");
			logStep("rdp keyword expected to present in URL");
		} else {
			System.out.println("rdp keyword not expected to present in URL");
			logStep("rdp keyword not expected to present in URL");

		}
		if (iuExists) {
			if (rdpExists == expected) {
				System.out.println("rdp keyword validation successful");
				logStep("rdp keyword validation successful");
			} else {
				System.out.println("rdp keyword validation failed");
				logStep("rdp keyword validation failed");
				if (expected) {
					System.out.println("rdp keyword expected to present in URL but it not exist");
					logStep("rdp keyword expected to present in URL but it not exist");
					Assert.fail("rdp keyword expected to present in URL but it not exist");
				} else {
					System.out.println("rdp keyword not expected to present in URL but it exists");
					logStep("rdp keyword not expected to present in URL but it exists");
					Assert.fail("rdp keyword not expected to present in URL but it exists");
				}

			}
		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");
			Assert.fail(iuId + " ad call is not present");
		}

	}

	public static void validate_npa_val_in_gampad_url(String excelName, String sheetName, boolean expected)
			throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			iuId = videoIUValue;
		} else {
			iuId = data[18][1];
		}
		boolean rdpExists = false;
		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				iuExists = true;
				if (qry.contains("npa=1")) {
					rdpExists = true;
					// if (!"".equals(tempCustmParam))
					// customParamsList.add(getCustomParamsBy_iu_value(qry));
					break;
				}

			}

		}

		if (expected) {
			System.out.println("npa keyword expected to present in URL");
			logStep("npa keyword expected to present in URL");
		} else {
			System.out.println("npa keyword not expected to present in URL");
			logStep("npa keyword not expected to present in URL");

		}
		if (iuExists) {
			if (rdpExists == expected) {
				System.out.println("npa keyword validation successful");
				logStep("npa keyword validation successful");
			} else {
				System.out.println("npa keyword validation failed");
				logStep("npa keyword validation failed");
				if (expected) {
					System.out.println("npa keyword expected to present in URL but it not exist");
					logStep("npa keyword expected to present in URL but it not exist");
					Assert.fail("npa keyword expected to present in URL but it not exist");
				} else {
					System.out.println("npa keyword not expected to present in URL but it exists");
					logStep("npa keyword not expected to present in URL but it exists");
					Assert.fail("npa keyword not expected to present in URL but it exists");
				}

			}
		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");
			Assert.fail(iuId + " ad call is not present");
		}

	}

	// get b values from amazon aax calls of XML File and store to list
		public static void get_amazon_bid_values_from_aaxCalls(String slotID, boolean clearList) throws Exception {

			// String[][] data = read_excel_data.exceldataread(sheetName);

			// Read the content form file
			File fXmlFile = new File(outfile.getName());
			if (clearList) {
				listOf_b_Params.clear();
				aaxcallsSize = 0;
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);
			dbFactory.setNamespaceAware(true);
			dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			dbFactory.setFeature("http://xml.org/sax/features/validation", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);
			// Getting the transaction element by passing xpath expression
			NodeList nodeList = doc.getElementsByTagName("transaction");

			// Read JSONs and get b value
			// List<String> jsonBValuesList = new ArrayList<String>();

			// String slotId = data[21][1];

			// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";

			boolean flag = false;
			List<String> istofRequestBodies = new ArrayList<String>();
			List<String> istofResponseBodies = new ArrayList<String>();
			// List<String> listOf_b_Params = new ArrayList<String>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i) instanceof Node) {
					Node node = nodeList.item(i);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							Node innernode = nl.item(j);
							if (innernode != null) {
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("body")) {
														String content = eElement.getTextContent();
														if (content.contains(slotID)) {
															flag = true;
															aaxcallsSize++;
															istofRequestBodies.add(content);
															// System.out.println("request body "+content);
														}
													}
												}
											}
										}
									}
								}

								if (flag) {
									if (innernode.getNodeName().equals("response")) {
										// System.out.println(innernode.getNodeName());
										if (innernode.hasChildNodes()) {
											NodeList n2 = innernode.getChildNodes();
											for (int k = 0; k < n2.getLength(); k++) {
												Node innernode2 = n2.item(k);
												if (innernode2 != null) {
													if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
														Element eElement = (Element) innernode2;
														if (eElement.getNodeName().equals("body")) {
															String content = eElement.getTextContent();
															istofResponseBodies.add(content);
															String tempBparam = get_b_value_inJsonResponseBody(content);
															if (!"".contentEquals(tempBparam)) {
																listOf_b_Params.add(tempBparam);
															}
															// System.out.println("response body "+content);
														}
													}
												}
											}
										}
									}

								}

							}
						}
					}
				}
				flag = false;
			}
			System.out.println(listOf_b_Params);
			logStep(listOf_b_Params.toString());

			aaxcallsResponseSize = listOf_b_Params.size();
			aaxbidErrorCount = 0;
			for (String b : listOf_b_Params) {
				System.out.println(" b values from JSON-----------> " + b);
				if (b.contentEquals("error")) {
					aaxbidErrorCount++;
				}
			}
			System.out.println("aaxcalls Size is: " + aaxcallsSize);
			System.out.println("aaxcallsResponse Size is: " + aaxcallsResponseSize);
			System.out.println("aaxbidErrorCount size is: " + aaxbidErrorCount);

		}

		// this retrives amazon bid values of specific call from amazon calls and add to
		// list
		public static void load_amazon_bid_values_from_aaxCalls(String excelName, String sheetName, boolean clearList)
				throws Exception {
			String[][] data = read_excel_data.exceldataread(sheetName);
			String slotID = data[21][1];
			get_amazon_bid_values_from_aaxCalls(slotID, clearList);
		}

		// get b value from gampad calls of XML File and store to list
		public static void get_amazon_bid_values_from_gampadCalls(String excelName, String sheetName,String feedCall, String cust_param) throws Exception {
			// String[][] data = read_excel_data.exceldataread(sheetName);

			// Read the content form file
			File fXmlFile = new File(outfile.getName());
			customParamsList.clear();
			isaaxgampadcallexists = false;
	       int gampadcallcount = 0;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);
			dbFactory.setNamespaceAware(true);
			dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			dbFactory.setFeature("http://xml.org/sax/features/validation", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);
			// Getting the transaction element by passing xpath expression
			// NodeList nodeList = doc.getElementsByTagName("transaction");
			String xpathExpression = "charles-session/transaction/@query";
			List<String> getQueryList = evaluateXPath(doc, xpathExpression);

			// Getting custom_params amzn_b values
			// List<String> customParamsList = new ArrayList<String>();

			// String iuId =
			// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
			// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
			String queryIU = null;
			for (String qry : getQueryList) {
				if (qry.contains(feedCall)) {
					if(sheetName.equalsIgnoreCase("Hourly")) {
						queryIU = return_iu_value_from_query_parameter_of_Feedcall(qry);
						if(queryIU.equalsIgnoreCase(feedCall)) {
							gampadcallcount++;
							isaaxgampadcallexists = true;
							String tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
							if (!"".equals(tempCustmParam)) {
								customParamsList.add(getCustomParamBy_iu_value(qry, cust_param));
							} else {
								
							}
						}
					}else {
						gampadcallcount++;
						isaaxgampadcallexists = true;
						String tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
						if (!"".equals(tempCustmParam)) {
							customParamsList.add(getCustomParamBy_iu_value(qry, cust_param));
						} else {

						}
					}
					
					

				}
			}
			
			if (!isaaxgampadcallexists) {
				System.out.println("Corresponding gampad call " + feedCall + " is not generated..");
				logStep("Corresponding gampad call " + feedCall + " is not generated..");
			} else {
				System.out.println("No of times the gampad call found is: " + gampadcallcount);
				logStep("No of times the gampad call found is: " + gampadcallcount);
				System.out.println(customParamsList);
				logStep(customParamsList.toString());
			}

		}

		// this retrives amazon bid values from aax calls and gampad calls of
		// correponding add calls and verifies any one matching.
		public static void validate_aax_bid_value_with_gampad_bid_value(String excelName, String sheetName,
				boolean clearList) throws Exception {
			String[][] data = read_excel_data.exceldataread(sheetName);
			String slotID = data[21][1];
			// String feedCall = data[18][1];
			String feedCall = null;
			// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
			if (sheetName.equalsIgnoreCase("PreRollVideo")) {
				feedCall = videoIUValue;
			} else if (sheetName.equalsIgnoreCase("News(details)")) {
				feedCall = return_iu_value_of_Feedcall(excelName, sheetName);
			} else {
				feedCall = data[18][1];
			}

			boolean testpass = false;
			String cust_param = "amzn_b";

			if (sheetName.contains("PreRollVideo")) {
				cust_param = "amzn_vid";
			}

			get_amazon_bid_values_from_aaxCalls(slotID, clearList);

			if (aaxcallsSize == 0) {
				System.out.println("amazon aax " + sheetName
						+ " call is not generated in current session, so skipping the bid value verification");
				logStep("amazon aax " + sheetName
						+ " call is not generated in current session, so skipping the bid value verification");

			} else if (aaxbidErrorCount == aaxcallsResponseSize) {
				System.out.println("amazon aax " + sheetName
						+ " call response contains error i.e. bidding is not happened in current session, so skipping the bid value verification");
				logStep("amazon aax " + sheetName
						+ " call response contains error i.e. bidding is not happened in current session, so skipping the bid value verification");

			} else if (nextGenIMadDisplayed && sheetName.equalsIgnoreCase("Pulltorefresh")) {
				/*
				 * There may be chances that gampad call might not generated.. for ex: when IM
				 * ad displayed on home screen, then homescreen today call doesnt generate
				 * 
				 */
				System.out
						.println("Since IM Ad displayed on App Launch, Homescreen Today call bid id validation is skipped");
				logStep("Since IM Ad displayed on App Launch, Homescreen Today call bid id validation is skipped");
			} else {
				get_amazon_bid_values_from_gampadCalls(excelName, sheetName,feedCall, cust_param);
				/*
				 * below checks whether the gampad call exists or not before validating for
				 * amazon bid value..
				 */
				if (!isaaxgampadcallexists) {
					System.out.println("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: "
							+ cust_param + " validation failed");
					logStep("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: " + cust_param
							+ " validation failed");
					Assert.fail("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: "
							+ cust_param + " validation failed");
				} else if (customParamsList.size() == 0) {
					System.out.println("Ad Call :" + feedCall + " not contains the Custom Parameter: " + cust_param
							+ ", hence Custom Parameter: " + cust_param + " validation failed");
					logStep("Ad Call :" + feedCall + " not contains the Custom Parameter: " + cust_param
							+ ", hence Custom Parameter: " + cust_param + " validation failed");
					Assert.fail("Ad Call :" + feedCall + " not contains the Custom Parameter: " + cust_param
							+ ", hence Custom Parameter: " + cust_param + " validation failed");
				} else {
					for (int i = 0; i < listOf_b_Params.size(); i++) {
						for (int j = 0; j < customParamsList.size(); j++) {
							if (listOf_b_Params.get(i).equalsIgnoreCase(customParamsList.get(j))) {
								testpass = true;
								System.out.println("amazon aax " + sheetName
										+ " call bid value is matched with corresponding gampad call bid value");
								logStep("amazon aax " + sheetName
										+ " call bid value is matched with corresponding gampad call bid value");
								break;

							}

						}
						if (testpass == true) {
							break;
						}
					}
				}
				if (testpass == false) {
					System.out.println("amazon aax " + sheetName
							+ " call bid value is not matched with corresponding gampad call bid value");
					logStep("amazon aax " + sheetName
							+ " call bid value is not matched with corresponding gampad call bid value");
					Assert.fail("amazon aax " + sheetName
							+ " call bid value is not matched with corresponding gampad call bid value");
				}

			}
		}

		public static boolean verifyAPICalWithHostandPath(String host, String path) throws Exception {
			// readExcelValues.excelValues(excelName, sheetName);
			File fXmlFile = new File(CharlesFunctions.outfile.getName());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);
			dbFactory.setNamespaceAware(true);
			dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			// dbFactory.setNamespaceAware(true);
			dbFactory.setFeature("http://xml.org/sax/features/validation", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
			NodeList nodeList = doc.getElementsByTagName("transaction");
			String xpathExpression = "charles-session/transaction/@host";
			List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
			List<String> customParamsList = new ArrayList<String>();

			// String iuId = null;

			boolean iuExists = false;
			for (String qry : getQueryList) {
				if (qry.contains(host)) {
					iuExists = true;
					break;
				}
			}
			boolean hflag = false;
			boolean pflag = false;
			boolean resflag = false;

			if (iuExists) {
				System.out.println(host + "  call is present");
				logStep(host + "  call is present");
				outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
					// System.out.println("Total transactions: "+nodeList.getLength());
					if (nodeList.item(p) instanceof Node) {
						Node node = nodeList.item(p);
						if (node.hasChildNodes()) {
							NodeList nl = node.getChildNodes();
							for (int j = 0; j < nl.getLength(); j++) {
								// System.out.println("node1 length is: "+nl.getLength());
								Node innernode = nl.item(j);
								if (innernode != null) {
									// System.out.println("Innernode name is: "+innernode.getNodeName());
									if (innernode.getNodeName().equals("request")) {
										if (innernode.hasChildNodes()) {
											NodeList n2 = innernode.getChildNodes();
											for (int k = 0; k < n2.getLength(); k++) {
												// System.out.println("node2 length is: "+n2.getLength());
												Node innernode2 = n2.item(k);
												if (innernode2 != null) {
													// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
													if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
														Element eElement = (Element) innernode2;
														// System.out.println("Innernode2 element name is:
														// "+eElement.getNodeName());
														if (eElement.getNodeName().equals("headers")) {
															if (innernode2.hasChildNodes()) {
																NodeList n3 = innernode2.getChildNodes();
																for (int q = 0; q < n3.getLength(); q++) {
																	// System.out.println("node3 length is:
																	// "+n3.getLength());
																	Node innernode3 = n3.item(q);
																	if (innernode3 != null) {
																		// System.out.println("Innernode3 name is:
																		// "+innernode3.getNodeName());
																		if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																			Element eElement1 = (Element) innernode3;
																			// System.out.println("Innernode3 element name
																			// is: "+eElement1.getNodeName());
																			if (eElement1.getNodeName().equals("header")) {
																				String content = eElement1.getTextContent();
																				// System.out.println("request body
																				// "+content);

																				if (content.contains(host)) {
																					hflag = true;
																					// System.out.println("request body
																					// found "
																					// + content);

																				} else if (content.contains(path)) {
																					pflag = true;
																					// System.out.println("request body
																					// found "
																					// + content);
																				}
																			}
																			//if(hflag && !pflag) {
																				if (eElement1.getNodeName().equals("first-line")) {
																					String content = eElement1.getTextContent();
																					// System.out.println("request body
																					// "+content);

																					if (content.contains(path)) {
																						pflag = true;
																						// System.out.println("request body
																						// found "
																						// + content);
																					}
																				}
																			//}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}

									/*
									 * if (flag) { // System.out.println("Exiting after found true "); //
									 * System.out.println("checking innernode name is: "+innernode.getNodeName());
									 * if (innernode.getNodeName().equals("response")) { //
									 * System.out.println(innernode.getNodeName()); if (innernode.hasChildNodes()) {
									 * NodeList n2 = innernode.getChildNodes(); for (int k = 0; k < n2.getLength();
									 * k++) { Node innernode2 = n2.item(k); if (innernode2 != null) { if
									 * (innernode2.getNodeType() == Node.ELEMENT_NODE) { Element eElement =
									 * (Element) innernode2; if (eElement.getNodeName().equals("body")) { String
									 * content = eElement.getTextContent(); //
									 * System.out.println("response body "+content); if
									 * (content.contains(readExcelValues.data[13][Cap])) { resflag = true; break
									 * outerloop;
									 * 
									 * } } } } } } }
									 * 
									 * }
									 */
									if (hflag && pflag) {
										resflag = true;
										break outerloop;
									}
								}
							}
						}
					}
					// flag = false;
				}

			} else {
				System.out.println(host + " ad call is not present");
				logStep(host + " ad call is not present");

			}

			return resflag;

			// Get Pubad call from

			/*
			 * if (resflag) { System.out.println(host + path
			 * +" call is present in Charles session"); logStep(host + path
			 * +" call is present in Charles session"); return resflag;
			 * 
			 * } else { System.out .println(host + path
			 * +" call is not present in Charles session"); logStep(host + path
			 * +" call is not present in Charles session"); return resflag;
			 * //Assert.fail(host + path +" call is not present in Charles session");
			 * 
			 * }
			 */

		}
	public static void waitForMinute() {
		long start = System.nanoTime();
		// ...
		long finish = System.nanoTime();
		long timeElapsed = finish - start;

		// long start = System.currentTimeMillis();
		// ...
		// long finish = System.currentTimeMillis();
		// long timeElapsed = finish - start;

		// float sec = (end - start) / 1000F; System.out.println(sec + " seconds");
	}




	// Tap on Home tab
	public static void navigateToHomeTab_toGetInterStitialAd() {
		try {
			Ad.findElementByName("feedTab").click();
			System.out.println("Clicked on Home tab ");
			logStep("Clicked on Home tab");
			// Ad.findElementByAccessibilityId(data[6][1].toString()).click();
		} catch (Exception e) {
			// Ad.findElementByName(data[6][1].toString()).click();
			System.out.println("Home Tab is not displayed, to get interstitial ad");
			logStep("Home Tab is not displayed, to get interstitial ad");
			Assert.fail("Home Tab is not displayed, to get interstitial ad");

		}

	}

	

	
	public static void loadFeedAdCardsToMap() {
		for (Map.Entry m : feedCardsMap.entrySet()) {
			// System.out.println(m.getKey()+" : "+m.getValue());
			String currentKey = m.getKey().toString();
			if (currentKey.contains("feed")) {
				String[] cKeySplit = currentKey.split("\\.");
				String feed = cKeySplit[1];
				String feedPos = m.getValue().toString();
				System.out.println("Feed Ad Card Name: " + feed);
				System.out.println("Feed Ad Card pos value is: " + feedPos);
				feedAdCardsMap.put(feed, feedPos);
			}
		}
	}

	// Verify Specific custom parameter value of all existed new feed ads (i.e.
	// feed1, feed2,....etc)
	public static void validate_custom_param_val_of_newFeedAds(String excelName, String sheetName, String cust_param)
			throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		// File fXmlFile = outfile;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";

		// String iuId = null;
		boolean iuExists = false;
		String cardsNotPresent = "";
		int failCount = 0;
		String cardsCustParamNotMatched = "";
		int custParamFailCount = 0;
		String tempCustmParam = null;
		loadFeedAdCardsToMap();
		for (Map.Entry m : feedAdCardsMap.entrySet()) {
			String feedCardNo = m.getKey().toString().trim().substring(4);
			String currentFeedAd_Card = "feed_" + feedCardNo;
			String iuId = data[5][1] + currentFeedAd_Card;
			String expCustParam_val = m.getValue().toString();
			tempCustmParam = null;
			iuExists = false;
			for (String qry : getQueryList) {
				if (qry.contains(iuId)) {
					tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
					// if (!"".equals(tempCustmParam))
					// customParamsList.add(getCustomParamsBy_iu_value(qry));
					iuExists = true;
					break;
				} else {
					if (feedCardNo.contentEquals("3")) {
						String currentFeedAd_Card1 = "hpvar_3";
						String iuId1 = data[5][1] + currentFeedAd_Card1;
						if (qry.contains(iuId1)) {
							tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
							// if (!"".equals(tempCustmParam))
							// customParamsList.add(getCustomParamsBy_iu_value(qry));
							iuExists = true;
							iuId = iuId1;
							currentFeedAd_Card = currentFeedAd_Card1;
							break;
						}
					}
				}

			}
			if (iuExists) {
				// System.out.println(currentFeedAd_Card + " ad call is present");
				// logStep(currentFeedAd_Card + " ad call is present");
				System.out.println(cust_param + " value of from gampad call  of : " + iuId + " is " + tempCustmParam);

				if (tempCustmParam.equalsIgnoreCase(expCustParam_val)) {
					System.out.println(currentFeedAd_Card + " custom Parameter :" + cust_param + " value: "
							+ tempCustmParam + " is matched with the expected value " + expCustParam_val);
					logStep(currentFeedAd_Card + " custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is matched with the expected value " + expCustParam_val);
				} else {
					System.out.println(currentFeedAd_Card + " custom Parameter :" + cust_param + " value: "
							+ tempCustmParam + " is not matched with the expected value " + expCustParam_val);
					logStep(currentFeedAd_Card + " custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expCustParam_val);
					custParamFailCount++;
					cardsCustParamNotMatched = cardsCustParamNotMatched.concat(currentFeedAd_Card + ", ");

					// Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
					// + " is not matched with the expected value " + expCustParam_val);
				}

			} else {
				System.out.println(currentFeedAd_Card + " ad call is not present");
				logStep(currentFeedAd_Card + " ad call is not present");
				failCount++;
				// Assert.fail(feedCards[i] + " ad call is not present");
				cardsNotPresent = cardsNotPresent.concat(currentFeedAd_Card + ", ");

			}
		}

		if (failCount > 0) {
			Exception = null;
			System.out.println(cardsNotPresent + " ad call is not present");
			logStep(cardsNotPresent + " ad call is not present");
			Exception = cardsNotPresent + " ad call is not present";
			// Assert.fail(Exception);
		}
		if (custParamFailCount > 0) {
			Exception = null;
			System.out.println(
					cardsCustParamNotMatched + " cards " + cust_param + " value is not matched with expected values");
			logStep(cardsCustParamNotMatched + " cards " + cust_param + " value is not matched with expected values");
			Exception = cardsCustParamNotMatched + " cards " + cust_param
					+ " value is not matched with expected values";
			Assert.fail(Exception);
		}

	}

	/**
	 * This method returns the parameter value from the corresponding API response
	 * @param cust_param
	 * @return
	 * @throws Exception
	 */
	public static String get_param_value_from_APICalls(String cust_param) throws Exception {

		// String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());
		listOf_b_Params.clear();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
	//	readExcelValues.excelValues("Cust_Param", "PramNaming");
		
		String[][]  data = read_excel_data.exceldataread_Custom_Parameters("Cust_Param", "PramNaming");
	
		int custParamCount =read_excel_data.rowCount;
		// Read JSONs and get b value
		// List<String> jsonBValuesList = new ArrayList<String>();

		// String slotId = data[21][1];

		// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";

		boolean flag = false;
		boolean hflag = false;
		String ApiParamValue = null;
		List<String> istofRequestBodies = new ArrayList<String>();
		List<String> istofResponseBodies = new ArrayList<String>();
		// List<String> listOf_b_Params = new ArrayList<String>();

		outerloop: for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Node) {
				Node node = nodeList.item(i);
				if (node.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					for (int j = 0; j < nl.getLength(); j++) {
						Node innernode = nl.item(j);
						if (innernode != null) {
							
							if (innernode.getNodeName().equals("request")) {
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												if (eElement.getNodeName().equals("headers")) {
													if (innernode2.hasChildNodes()) {
														NodeList n3 = innernode2.getChildNodes();
														for (int q = 0; q < n3.getLength(); q++) {
															// System.out.println("node3 length is:
															// "+n3.getLength());
															Node innernode3 = n3.item(q);
															if (innernode3 != null) {
																// System.out.println("Innernode3 name is:
																// "+innernode3.getNodeName());
																if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																	Element eElement1 = (Element) innernode3;
																	// System.out.println("Innernode3 element name
																	// is: "+eElement1.getNodeName());
																	if (eElement1.getNodeName().equals("header")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
																			if (cust_param.equals(data[paramtype][0]) && content.contains(data[paramtype][3])) 
																			{
																				flag = true;
																				break;
																			}
																		}
																	}
																	
																	// this condition especially for android since its
																	// file has path value under first-line element
																	if (eElement1.getNodeName().equals("first-line")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
																	
																			if (cust_param.equals(data[paramtype][0]) && content.contains(data[paramtype][3])) {
																				flag = true;
																				break;
																			}
																		}
																	}

																}
															}
														}
													}
												}

												
											}
										}
									}
								}
							}
							
							
						if (flag) {
							if (innernode.getNodeName().equals("response")) {
								// System.out.println(innernode.getNodeName());
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												if (eElement.getNodeName().equals("body")) {
													String content = eElement.getTextContent();
													String[] JsonValues = null;
													String JsonParam = null;

													for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
														if (cust_param.equals(data[paramtype][0])) {
															if (data[paramtype][2].contains(",")) {
																JsonValues = data[paramtype][2]
																		.split(",");
																JsonParam = JsonValues[0].trim();

															} /*
																 * else if(exceldata[paramtype][2].contains(
																 * "direct")) { //mainTag = (JSONObject) obj; }
																 */else {
																JsonParam = data[paramtype][2].trim();
																// mainTag = (JSONObject) jsonObject.get(JsonParam);
															}
															break;
														}
													}

													if (content.contains(JsonParam)) {
														// content.replaceAll(":null", ":nl");
														// flag = true;
														// istofRequestBodies.add(content);
														// System.out.println("request body " + content);
														// ApiParamValue = getValueFromJsonResponseBody(content,
														// jsonParam,jsonNode);
														ApiParamValue = get_Expected_Value_From_APIResponseBody(
																"Cust_Param", "PramNaming", cust_param, content);
														break outerloop;

														/*
														 * JSONParser parser = new JSONParser();
														 * //System.out.println("adreq1 is : "+adreq1.toString());
														 * Object obj = parser.parse(new String(content)); String
														 * JsonParam="v3-wx-observations-current"; JSONObject jsonObject
														 * = (JSONObject) obj; JSONObject mainTag = (JSONObject)
														 * jsonObject.get(JsonParam);
														 * //System.out.println("obj : "+obj);
														 * 
														 * String ApiParamValue= mainTag.get("iconCode").toString();
														 * System.out.println("value is "+ApiParamValue);
														 */

													}
													/*
													 * istofResponseBodies.add(content); String tempBparam =
													 * get_b_value_inJsonResponseBody(content); if
													 * (!"".contentEquals(tempBparam)) {
													 * listOf_b_Params.add(tempBparam); }
													 */
													// System.out.println("response body "+content);
												}
											}
										}
									}
								}
							}
						}

						}
					}
				}
			}
			 flag = false;
		}
		
		return ApiParamValue;
	}
	
	/**
	 * This method returns the parameter value from the corresponding API response based on given zipcode.
	 * @param cust_param
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
		public static String get_param_value_from_APICalls(String cust_param, String zipCode) throws Exception {

			// String[][] data = read_excel_data.exceldataread(sheetName);

			// Read the content form file
			File fXmlFile = new File(CharlesFunctions.outfile.getName());
			listOf_b_Params.clear();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);
			dbFactory.setNamespaceAware(true);
			dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			dbFactory.setFeature("http://xml.org/sax/features/validation", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);
			// Getting the transaction element by passing xpath expression
			NodeList nodeList = doc.getElementsByTagName("transaction");
		//	readExcelValues.excelValues("Cust_Param", "PramNaming");
			String[][] exceldata = read_excel_data.exceldataread_Custom_Parameters("Cust_Param", "PramNaming");
			int custParamCount =read_excel_data.rowCount;
			// Read JSONs and get b value
			// List<String> jsonBValuesList = new ArrayList<String>();

			// String slotId = data[21][1];

			// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";

			boolean flag = false;
			boolean hflag = false;
			String ApiParamValue = null;
			List<String> istofRequestBodies = new ArrayList<String>();
			List<String> istofResponseBodies = new ArrayList<String>();
			// List<String> listOf_b_Params = new ArrayList<String>();

			outerloop: for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i) instanceof Node) {
					Node node = nodeList.item(i);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							Node innernode = nl.item(j);
							if (innernode != null) {
								
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
																				if (cust_param.equals(exceldata[paramtype][0]) && content.contains(exceldata[paramtype][3])) {
																					flag = true;
																					break;
																				}
																			}
																		}
																		
																		// this condition especially for android since its
																		// file has path value under first-line element
																		if (eElement1.getNodeName().equals("first-line")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
																				if (cust_param.equals(exceldata[paramtype][0]) && content.contains(exceldata[paramtype][3])) {
																					flag = true;
																					break;
																				}
																			}
																		}

																	}
																}
															}
														}
													}

													
												}
											}
										}
									}
								}
								
								
							if (flag) {
								if (innernode.getNodeName().equals("response")) {
									// System.out.println(innernode.getNodeName());
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("body")) {
														String content = eElement.getTextContent();
														String[] JsonValues = null;
														String JsonParam = null;

														for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
															if (cust_param.equals(exceldata[paramtype][0])) {
																if (exceldata[paramtype][2].contains(",")) {
																	JsonValues = exceldata[paramtype][2]
																			.split(",");
																	JsonParam = JsonValues[0].trim();

																} /*
																	 * else if(exceldata[paramtype][2].contains(
																	 * "direct")) { //mainTag = (JSONObject) obj; }
																	 */else {
																	JsonParam = exceldata[paramtype][2].trim();
																	// mainTag = (JSONObject) jsonObject.get(JsonParam);
																}
																break;
															}
														}

														if (content.contains(JsonParam)) {
															// content.replaceAll(":null", ":nl");
															// flag = true;
															// istofRequestBodies.add(content);
															// System.out.println("request body " + content);
															// ApiParamValue = getValueFromJsonResponseBody(content,
															// jsonParam,jsonNode);
															ApiParamValue = get_Expected_Value_From_APIResponseBody(
																	"Cust_Param", "PramNaming", zipCode, cust_param, content);
															break outerloop;

															/*
															 * JSONParser parser = new JSONParser();
															 * //System.out.println("adreq1 is : "+adreq1.toString());
															 * Object obj = parser.parse(new String(content)); String
															 * JsonParam="v3-wx-observations-current"; JSONObject jsonObject
															 * = (JSONObject) obj; JSONObject mainTag = (JSONObject)
															 * jsonObject.get(JsonParam);
															 * //System.out.println("obj : "+obj);
															 * 
															 * String ApiParamValue= mainTag.get("iconCode").toString();
															 * System.out.println("value is "+ApiParamValue);
															 */

														}
														/*
														 * istofResponseBodies.add(content); String tempBparam =
														 * get_b_value_inJsonResponseBody(content); if
														 * (!"".contentEquals(tempBparam)) {
														 * listOf_b_Params.add(tempBparam); }
														 */
														// System.out.println("response body "+content);
													}
												}
											}
										}
									}
								}
							}

							}
						}
					}
				}
				 flag = false;
			}
			
			return ApiParamValue;
		}
	
	public static String getValueFromJsonResponseBody(String jsonString, String jsonParam, String jsonNode) {
		JSONParser parser = new JSONParser();
		// String jsonParam="v3-wx-observations-current";
		JSONObject mainTag = null;
		String ApiParamValue = null;
		// System.out.println("adreq1 is : "+adreq1.toString());
		Object obj;
		try {
			obj = parser.parse(new String(jsonString));
			JSONObject jsonObject = (JSONObject) obj;
			mainTag = (JSONObject) jsonObject.get(jsonParam);
			ApiParamValue = mainTag.get(jsonNode).toString();
			System.out.println("value from json response body is " + ApiParamValue);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			ApiParamValue = "Error";
			e.printStackTrace();
		}

		return ApiParamValue;
	}

	/**
	 * This method returns the parameter value from API response body after parsing the response
	 * @param Excelname
	 * @param sheetName
	 * @param cust_param
	 * @param apiData
	 * @return
	 * @throws Exception
	 */
	public static String get_Expected_Value_From_APIResponseBody(String Excelname, String sheetName, String cust_param,
			String apiData) throws Exception {

	
		String[][] data =  read_excel_data.exceldataread_Custom_Parameters(Excelname,sheetName);
		JSONParser parser = new JSONParser();
		// System.out.println("adreq1 is : "+adreq1.toString());
		Object obj = parser.parse(new String(apiData));
		// System.out.println("obj : "+obj);
		JSONObject jsonObject = (JSONObject) obj;
		String ApiParams = null;
		String ApiParamValue = null;
		String paramName = null;
		String JsonValues[] = null;
		String JsonParam = null;

		JSONObject mainTag = null;
		int custParamCount = read_excel_data.rowCount;

		for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
			if (cust_param.equals(data[paramtype][0])) {

				if (data[paramtype][1].toString().equals("hardcode")) {
					System.out.println("Param type is Hard Code");
				} else {
					System.out.println("main tag is : " + data[paramtype][2] + ", param type is : "
							+ data[paramtype][1]);
					if (data[paramtype][2].contains("direct")) {
						mainTag = (JSONObject) obj;
					} else if (data[paramtype][2].contains(",")) {
						JsonValues = data[paramtype][2].split(",");
						JsonParam = JsonValues[0].trim();
						mainTag = (JSONObject) jsonObject.get(JsonParam);
						try {
							mainTag = (JSONObject) mainTag.get(JsonValues[1].trim());
						} catch (Exception e) {
							if (cust_param.equalsIgnoreCase("fcnd") || cust_param.equalsIgnoreCase("fdynght")) {
								JSONArray eleArray = (JSONArray) mainTag.get(JsonValues[1].trim());
								ArrayList<String> Ingredients_names = new ArrayList<>();
								for (int i = 0; i < eleArray.size(); i++) {
									String arrayElement = String.valueOf(eleArray.get(i));

									Ingredients_names.add(arrayElement);
									obj = parser.parse(new String(arrayElement));
									jsonObject = (JSONObject) obj;
									mainTag = (JSONObject) obj;
									String[] arEls = arrayElement.split(",");
								}
								// System.out.println(Ingredients_names);
							}

						}

					} else {
						JsonParam = data[paramtype][2].trim();
						mainTag = (JSONObject) jsonObject.get(JsonParam);
					}

					if (cust_param.equalsIgnoreCase("fcnd") || cust_param.equalsIgnoreCase("fdynght")) {
						JSONArray dayPartElementValues = (JSONArray) mainTag.get(data[paramtype][1]);
						if (String.valueOf(dayPartElementValues.get(0)).equalsIgnoreCase("null")) {
							ApiParamValue = String.valueOf(dayPartElementValues.get(1));
						} else {
							ApiParamValue = String.valueOf(dayPartElementValues.get(0));
						}

					} else {
						try {
							JSONArray arrayElementValues = (JSONArray) mainTag
									.get(data[paramtype][1]);
							ApiParamValue = String.valueOf(arrayElementValues.get(0));
						} catch (Exception e) {
							ApiParamValue = String.valueOf(mainTag.get(data[paramtype][1]));
						}
					}

					if (cust_param.equalsIgnoreCase("tmpc")) {
						int fahrenheit = Integer.parseInt(ApiParamValue);
						int celsius = ((fahrenheit - 32) * 5) / 9;
						ApiParamValue = String.valueOf(celsius);
					} else if (cust_param.equalsIgnoreCase("fltmpc")) {
						int fahrenheit = Integer.parseInt(ApiParamValue);
						int celsius = ((fahrenheit - 32) * 5) / 9;
						ApiParamValue = String.valueOf(celsius);
					}
					// ApiParamValue= mainTag.get(exceldata[paramtype][1]).toString();

					if (data[paramtype][1].toString().equalsIgnoreCase("iconCode")) {
						paramName = "cnd";
					} else {
						paramName = data[paramtype][0].toString();
						System.out.println(paramName);
					}
					// ads.add(ApiParams);
					if (data[paramtype][4].toString().equalsIgnoreCase("Yes")) {
						//readExcelValues.excelValues("Cust_Param_Result", cust_param);
						 data = read_excel_data.exceldataread_Custom_Parameters("Cust_Param_Result", cust_param);

						for (int CustParamValues = 1; CustParamValues <= read_excel_data.rowCount; CustParamValues++) {
							if (data[CustParamValues][1].contains("and more")) {
								System.out.println(data[CustParamValues][1]);
								String CellParam = data[CustParamValues][1].toString();
								CellParam = CellParam.replaceAll("and more", "");
								CellParam = CellParam.replaceAll(" ", "");
								int celparamValue = Integer.parseInt(CellParam);
								int ApiParamNumber = Integer.parseInt(ApiParamValue);
								if (ApiParamNumber >= celparamValue) {
									ApiParamValue = data[CustParamValues][2].toString();
									break;
								}
							} else if (data[CustParamValues][1].contains("and less")) {
								String CellParam = data[CustParamValues][1].toString();
								CellParam = CellParam.replaceAll("and less", "");
								CellParam = CellParam.replaceAll(" ", "");
								int celparamValue = Integer.parseInt(CellParam);
								int ApiParamNumber = Integer.parseInt(ApiParamValue);
								if (ApiParamNumber <= celparamValue) {
									ApiParamValue = data[CustParamValues][2].toString();
									break;
								}
							} else if (data[CustParamValues][1].contains(ApiParamValue)) {
								ApiParamValue = data[CustParamValues][2].toString();
								break;
							}
						}
					}
					ApiParams = paramName + "=" + ApiParamValue;
					break;
				}
			}
		}
		System.out.println(cust_param + " Param Values from API Call is : " + ApiParamValue);
		if (ApiParamValue.equalsIgnoreCase("null")) {
			ApiParamValue = "nl";
		}
		return ApiParamValue;
	}
	/**
	 * This method returns the parameter value from API response body after parsing the response by zipcode
	 * @param Excelname
	 * @param sheetName
	 * @param zipCode
	 * @param cust_param
	 * @param apiData
	 * @return
	 * @throws Exception
	 */
	public static String get_Expected_Value_From_APIResponseBody(String Excelname, String sheetName, String zipCode, String cust_param,
			String apiData) throws Exception {

		// Functions.Read_Turbo_api("Cust_Param", readSheet);

		//String[][] data = read_excel_data.exceldataread(sheetName);
		String[][] data = read_excel_data.exceldataread_Custom_Parameters("Cust_Param", sheetName);
		JSONParser parser = new JSONParser();
		// System.out.println("adreq1 is : "+adreq1.toString());
		Object obj = parser.parse(new String(apiData));
		// System.out.println("obj : "+obj);
		JSONObject jsonObject = (JSONObject) obj;
		String ApiParams = null;
		String ApiParamValue = null;
		String paramName = null;
		String JsonValues[] = null;
		String JsonParam = null;

		JSONObject mainTag = null;
		int custParamCount = read_excel_data.rowCount;

		for (int paramtype = 1; paramtype <= custParamCount; paramtype++) {
			if (cust_param.equals(data[paramtype][0])) {

				if (data[paramtype][1].toString().equals("hardcode")) {
					System.out.println("Param type is Hard Code");
				} else {
					System.out.println("main tag is : " + data[paramtype][2] + ", param type is : "
							+ data[paramtype][1]);
					if (data[paramtype][2].contains("direct")) {
						mainTag = (JSONObject) obj;
					} else if (data[paramtype][2].contains(",")) {
						JsonValues = data[paramtype][2].split(",");
						JsonParam = JsonValues[0].trim();
						mainTag = (JSONObject) jsonObject.get(JsonParam);
						try {
							mainTag = (JSONObject) mainTag.get(JsonValues[1].trim());
						} catch (Exception e) {
							if (cust_param.equalsIgnoreCase("fcnd") || cust_param.equalsIgnoreCase("fdynght")) {
								JSONArray eleArray = (JSONArray) mainTag.get(JsonValues[1].trim());
								/*
								 * Note that, this JSONArray has only one json object in it, hence loop ends after one iteration.
								 */
								ArrayList<String> Ingredients_names = new ArrayList<>();
								for (int i = 0; i < eleArray.size(); i++) {
									String arrayElement = String.valueOf(eleArray.get(i));

									Ingredients_names.add(arrayElement);
									obj = parser.parse(new String(arrayElement));
									jsonObject = (JSONObject) obj;
									mainTag = (JSONObject) obj;
									String[] arEls = arrayElement.split(",");
								}
								// System.out.println(Ingredients_names);
							}else if(cust_param.equalsIgnoreCase("cxtg") || cust_param.equalsIgnoreCase("zcs") || cust_param.equalsIgnoreCase("hzcs") || cust_param.equalsIgnoreCase("nzcs")){
								JSONArray eleArray = (JSONArray) mainTag.get(JsonValues[1].trim());
								/*
								 * Generally Scatteredtags returns 3 objects, zcs, hzcs and nzcs in order
								 * in turn each object zcs, hzcs and nzcs is an array of json objects
								 */
								ArrayList<String> Ingredients_names = new ArrayList<>();
								//String arrayElement="";
								outerloop:for (int i = 0; i < eleArray.size(); i++) {
									String arrayElement = String.valueOf(eleArray.get(i));

									Ingredients_names.add(arrayElement);
									obj = parser.parse(new String(arrayElement));
									jsonObject = (JSONObject) obj;
									mainTag = (JSONObject) obj;
									
									try {
										/*
										 * since same block being used for cxtg, zcs, hzcs and nzcs, there are possibilities that 
										 * we may get null value in first iteration for hzcs and nzcs as these are elements of second object onwards, hence exception. 
										 * so when we get exception continuing to next iteration
										 */
										JSONArray eleArrays = (JSONArray) mainTag.get(JsonValues[2].trim());
										ArrayList<String> Ingredients_namess = new ArrayList<>();
										//String[] arEls = arrayElement.split(",");
										for (int j = 0; j < eleArrays.size(); j++) {
											String arrayElementt = String.valueOf(eleArrays.get(j));

											Ingredients_namess.add(arrayElementt);
											obj = parser.parse(new String(arrayElementt));
											jsonObject = (JSONObject) obj;
											mainTag = (JSONObject) obj;
											String zip ="";
											try{
												zip = String.valueOf(mainTag.get("zip"));
												
											}catch(Exception e1) {
												System.out.println("An Exception while fetching zip value");
												logStep("An Exception while fetching zip value");
											}
											/*
											 * since the parameters are mapped to respective zip codes and selection to be based on zip code, 
											 * hence zip code comparision introduced
											 */
											if(zip.equalsIgnoreCase(zipCode)) {
												
												break outerloop;
											}else {
												/*
												 * this else block is written to check whether zip code found in json objects. 
												 * if not printing a log message, as the exception being automatically handled and returns null value in the step
												 * JSONArray arrayElementValues = (JSONArray) mainTag.get(exceldata[paramtype][1]);
												 */
												if(j == eleArrays.size()-1) {
													System.out.println("Expected Zip Code :"+zipCode+ "is not found in JSON Array Objects");
													logStep("Expected Zip Code :"+zipCode+ "is not found in JSON Array Objects");
													mainTag = null;
												}
											}
											
										}
										break outerloop;
									}catch(Exception e2) {
										continue;
									}
									
									
								}
								
							}

						}

					} else {
						JsonParam = data[paramtype][2].trim();
						mainTag = (JSONObject) jsonObject.get(JsonParam);
					}

					if (cust_param.equalsIgnoreCase("fcnd") || cust_param.equalsIgnoreCase("fdynght")) {
						JSONArray dayPartElementValues = (JSONArray) mainTag.get(exceldata[paramtype][1]);
						if (String.valueOf(dayPartElementValues.get(0)).equalsIgnoreCase("null")) {
							ApiParamValue = String.valueOf(dayPartElementValues.get(1));
						} else {
							ApiParamValue = String.valueOf(dayPartElementValues.get(0));
						}

					} else {
						try {
							JSONArray arrayElementValues = (JSONArray) mainTag
									.get(data[paramtype][1]);
							/**
							 * Certain custom parameters needed single element out of array and certain parameters needed complete array
							 */
							if(cust_param.equalsIgnoreCase("wfxtg") || cust_param.equalsIgnoreCase("cxtg") || cust_param.equalsIgnoreCase("nzcs") || cust_param.equalsIgnoreCase("zcs") || cust_param.equalsIgnoreCase("hzcs")) {
								ApiParamValue = StringUtils.jSONArrayToString(arrayElementValues);
								
							}else {
								ApiParamValue = String.valueOf(arrayElementValues.get(0));
							}
							
						} catch (Exception e) {
							try{
								ApiParamValue = String.valueOf(mainTag.get(data[paramtype][1]));
							}catch(Exception e3) {
								/*
								 * This is handled because for cxtg, zcs, hzcs and nzcs if the expected zip code is not there then throwing an exception 
								 * and setting ApiParamValue to nl
								 */
								ApiParamValue = "nl";
							}
						}

					}

					if (cust_param.equalsIgnoreCase("tmpc")) {
						int fahrenheit = Integer.parseInt(ApiParamValue);
						int celsius = ((fahrenheit - 32) * 5) / 9;
						ApiParamValue = String.valueOf(celsius);
					} else if (cust_param.equalsIgnoreCase("fltmpc")) {
						int fahrenheit = Integer.parseInt(ApiParamValue);
						int celsius = ((fahrenheit - 32) * 5) / 9;
						ApiParamValue = String.valueOf(celsius);
					}
					// ApiParamValue= mainTag.get(exceldata[paramtype][1]).toString();

					if (data[paramtype][1].toString().equalsIgnoreCase("icon")) {
						paramName = "cnd";
					} else {
						paramName = data[paramtype][0].toString();
					}
					// ads.add(ApiParams);
					if (data[paramtype][4].toString().equalsIgnoreCase("Yes")) {
					data = read_excel_data.exceldataread_Custom_Parameters("Cust_Param_Result", cust_param);

						for (int CustParamValues = 1; CustParamValues <= read_excel_data.rowCount; CustParamValues++) {
							if (data[CustParamValues][1].contains("and more")) {
								String CellParam = data[CustParamValues][1].toString();
								CellParam = CellParam.replaceAll("and more", "");
								CellParam = CellParam.replaceAll(" ", "");
								int celparamValue = Integer.parseInt(CellParam);
								int ApiParamNumber = Integer.parseInt(ApiParamValue);
								if (ApiParamNumber >= celparamValue) {
									ApiParamValue = data[CustParamValues][2].toString();
									break;
								}
							} else if (data[CustParamValues][1].contains("and less")) {
								String CellParam = data[CustParamValues][1].toString();
								CellParam = CellParam.replaceAll("and less", "");
								CellParam = CellParam.replaceAll(" ", "");
								int celparamValue = Integer.parseInt(CellParam);
								int ApiParamNumber = Integer.parseInt(ApiParamValue);
								if (ApiParamNumber <= celparamValue) {
									ApiParamValue = data[CustParamValues][2].toString();
									break;
								}
							} else if (data[CustParamValues][1].contains(ApiParamValue)) {
								ApiParamValue = data[CustParamValues][2].toString();
								break;
							}
						}
					}
					ApiParams = paramName + "=" + ApiParamValue;
					break;
				}
			}
		}
		System.out.println(cust_param + " Param Values from API Call is : " + ApiParamValue);
		if (ApiParamValue.equalsIgnoreCase("null")) {
			ApiParamValue = "nl";
		}
		return ApiParamValue;
	}

	public static String get_custom_param_val_of_gampad(String excelName, String sheetName, String cust_param)
			throws Exception {
		/*
		 * Calendar calendar = Calendar.getInstance(); Date d = new Date();
		 * SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of
		 * the week abbreviated String today = simpleDateformat.format(d); today =
		 * today.toLowerCase().concat("1");
		 */
		//String today = dailyDetailsDayOfWeek.concat("1");

		boolean adCallFound = false;

		// Read the content form file
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		/*
		 * if(expected.equalsIgnoreCase("null")) { expected = "nl"; }
		 */
		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		String iuId = null;
		String[][] data = read_excel_data.exceldataread(sheetName);
		if (cust_param.equalsIgnoreCase("fcnd")) {
			iuId = data[18][1];
		//	iuId = iuId.concat("_") + today;
		} else {
			iuId = data[18][1];
		}

		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				adCallFound = true;
				tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}

		}

		return tempCustmParam;

	}
	
	
	/**
	 * This method validates the Custom Parameter of gampad call with the expected value sent as parameter. This requires both Custom Parameter and expected value as input
	 * @param excelName
	 * @param sheetName
	 * @param cust_param
	 * @param expected
	 * @throws Exception
	 */
	public static void validate_custom_param_val_of_gampad(String excelName, String sheetName, String cust_param,
			String expected) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		boolean adCallFound = false;

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			iuId = videoIUValue;
		} else {
			iuId = data[11][1];
			System.out.println(data[11][1]);
			//iuId = "iu=%2F7646%2Fapp_android_us%2Fdb_display%2Fdetails%2Fhourly";
		}
		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				adCallFound = true;
				tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}

		}

		if (nextGenIMadDisplayed && sheetName.equalsIgnoreCase("Pulltorefresh")) {
			System.out.println("Since IM Ad displayed on App Launch, Homescreen Today call validation is skipped");
			logStep("Since IM Ad displayed on App Launch, Homescreen Today call validation is skipped");
		} else {

			if (!adCallFound) {
				System.out.println("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: "
						+ cust_param + " validation skipped");
				logStep("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation skipped");
				Assert.fail("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation skipped");
			} else if (adCallFound && !tempCustmParam.isEmpty()) {
				System.out.println(cust_param + " value of from gampad call  of : " + iuId + " is " + tempCustmParam);
				if (expected.equalsIgnoreCase("NotNull")) {
					if (!tempCustmParam.equalsIgnoreCase("nl")) {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
					} else {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
					}
				} else {
					if (tempCustmParam.equalsIgnoreCase(expected)) {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
					} else {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
					}
				}

			} else if (tempCustmParam == null || tempCustmParam.isEmpty()) {
				System.out.println(
						"Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
								+ cust_param + " validation skipped");
				logStep("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
						+ cust_param + " validation skipped");
				Assert.fail(
						"Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
								+ cust_param + " validation skipped");
			}
		}
	}
	
	
	/**
	 * This method validates the Custom Parameter of gampad call with the
	 * corresponding parameter in respective API Call. This requires only Custom
	 * Parameter as input
	 * 
	 * @param excelName
	 * @param sheetName
	 * @param cust_param
	 * @throws Exception
	 */
	public static void validate_custom_param_val_of_gampad(String excelName, String sheetName, String cust_param)
			throws Exception {
		/*
		 * Calendar calendar = Calendar.getInstance(); Date d = new Date();
		 * SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of
		 * the week abbreviated String today = simpleDateformat.format(d); today =
		 * today.toLowerCase().concat("1");
		 */
		String today = null;

		boolean adCallFound = false;

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);
		String expected = null;
		if (cust_param.equalsIgnoreCase("dt")) {
			//expected = dailyDetailsDateOfDay;
		} else if (cust_param.equalsIgnoreCase("mnth")) {
			//expected = dailyDetailsMonthOfDate;
		} else {
			expected = get_param_value_from_APICalls(cust_param);
		}

		/*
		 * if(expected.equalsIgnoreCase("null")) { expected = "nl"; }
		 */
		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		String iuId = null;
	//	readExcelValues.excelValues(excelName, sheetName);
		String[][] data = read_excel_data.exceldataread(sheetName);
		if (cust_param.equalsIgnoreCase("fcnd")) {
			try {
			//	today = dailyDetailsDayOfWeek.concat("1");
			} catch (Exception e) {
				System.out.println("An exception while parsing today value");
				logStep("An exception while parsing today value");
			}

			iuId = data[11][1];
			iuId = iuId.concat("_") + today;
		} else if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			iuId = videoIUValue;
		} else {
			iuId = data[11][1];
		}

		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				adCallFound = true;
				tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}

		}
		if (nextGenIMadDisplayed && sheetName.equalsIgnoreCase("Pulltorefresh")) {
			System.out.println("Since IM Ad displayed on App Launch, Homescreen Today call validation is skipped");
			logStep("Since IM Ad displayed on App Launch, Homescreen Today call validation is skipped");
		} else {
			if (expected == null) {
				System.out.println(
						"Either Parameter value is empty or API Call is not generated, hence Custom Parameter validation skipped");
				logStep("Either Parameter value is empty or API Call is not generated, hence Custom Parameter validation skipped");
				System.out.println("Custom Parameter :" + cust_param + " validation is failed");
				logStep("Custom Parameter :" + cust_param + " validation is failed");
				Assert.fail(
						"Either Parameter value is empty or API Call is not generated, hence Custom Parameter validation skipped");
			} else if (!adCallFound) {
				System.out.println("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: "
						+ cust_param + " validation skipped");
				logStep("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation skipped");
				System.out.println("Custom Parameter :" + cust_param + " validation is failed");
				logStep("Custom Parameter :" + cust_param + " validation is failed");
				Assert.fail("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation skipped");
			} else if (adCallFound && !tempCustmParam.isEmpty()) {
				System.out
						.println(cust_param + " Param value from gampad call  of : " + iuId + " is " + tempCustmParam);
				if (expected.equalsIgnoreCase("NotNull")) {
					if (!tempCustmParam.equalsIgnoreCase("nl")) {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
						System.out.println("Custom Parameter :" + cust_param + " validation is successful");
						logStep("Custom Parameter :" + cust_param + " validation is successful");
					} else {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						System.out.println("Custom Parameter :" + cust_param + " validation is failed");
						logStep("Custom Parameter :" + cust_param + " validation is failed");
						Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
					}
				} else {
					if (tempCustmParam.equalsIgnoreCase(expected)) {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value: " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value: " + expected);
						System.out.println("Custom Parameter :" + cust_param + " validation is successful");
						logStep("Custom Parameter :" + cust_param + " validation is successful");
					} else {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value: " + expected);
						System.out.println("Custom Parameter :" + cust_param + " validation is failed");
						logStep("Custom Parameter :" + cust_param + " validation is failed");
						Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value: " + expected);
					}
				}

			} else if (tempCustmParam == null || tempCustmParam.isEmpty()) {
				System.out.println(
						"Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
								+ cust_param + " validation skipped");
				logStep("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
						+ cust_param + " validation skipped");
				System.out.println("Custom Parameter :" + cust_param + " validation is failed");
				logStep("Custom Parameter :" + cust_param + " validation is failed");
				Assert.fail(
						"Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
								+ cust_param + " validation skipped");
			}
		}

	}

	/**
	 * This method validates the Custom Parameter of gampad call with the corresponding parameter in respective API Call by retrieving the value based on zipcode . This requires Custom Parameter and zipcode as input
	 * @param excelName
	 * @param sheetName
	 * @param cust_param
	 * @param zipCode
	 * @throws Exception
	 */
	public static void validate_custom_param_val_of_gampad_with_zip(String sheetName, String cust_param, String zipCode)
			throws Exception {
		/*
		 * Calendar calendar = Calendar.getInstance(); Date d = new Date();
		 * SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of
		 * the week abbreviated String today = simpleDateformat.format(d); today =
		 * today.toLowerCase().concat("1");
		 */
		//String today = dailyDetailsDayOfWeek.concat("1");

		boolean adCallFound = false;

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);
		String expected = null;
		if (cust_param.equalsIgnoreCase("dt")) {
			//expected = dailyDetailsDateOfDay;
		} else if (cust_param.equalsIgnoreCase("mnth")) {
			//expected = dailyDetailsMonthOfDate;
		} else {
			expected = get_param_value_from_APICalls(cust_param, zipCode);
		}

		/*
		 * if(expected.equalsIgnoreCase("null")) { expected = "nl"; }
		 */
		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		String iuId = null;
		String[][] data = read_excel_data.exceldataread(sheetName);
		if (cust_param.equalsIgnoreCase("fcnd")) {
			iuId = data[11][1];
		//	iuId = iuId.concat("_") + today;
		}
		else if(sheetName.equalsIgnoreCase("PreRollVideo")){
			iuId = videoIUValue;
		}
		else {
			iuId = data[11][1];
		}

		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				adCallFound = true;
				tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}

		}
		if (nextGenIMadDisplayed && sheetName.equalsIgnoreCase("Pulltorefresh")) {
			System.out.println("Since IM Ad displayed on App Launch, Homescreen Today call validation is skipped");
			logStep("Since IM Ad displayed on App Launch, Homescreen Today call validation is skipped");
		} else {
			if (expected == null) {
				System.out.println(
						"Either Parameter value is empty or API Call is not generated, hence Custom Parameter validation skipped");
				logStep("Either Parameter value is empty or API Call is not generated, hence Custom Parameter validation skipped");
				Assert.fail(
						"Either Parameter value is empty or API Call is not generated, hence Custom Parameter validation skipped");
			} else if (!adCallFound) {
				System.out.println("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: "
						+ cust_param + " validation skipped");
				logStep("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation skipped");
				Assert.fail("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation skipped");
			} else if (adCallFound && !tempCustmParam.isEmpty()) {
				System.out
						.println(cust_param + " Param value from gampad call  of : " + iuId + " is " + tempCustmParam);
				if (expected.equalsIgnoreCase("NotNull")) {
					if (!tempCustmParam.equalsIgnoreCase("nl")) {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value " + expected);
					} else {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
					}
				} else {
					if (tempCustmParam.equalsIgnoreCase(expected)) {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value: " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is matched with the expected value: " + expected);
					} else {
						System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value " + expected);
						logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value: " + expected);
						Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
								+ " is not matched with the expected value: " + expected);
					}
				}

			} else if (tempCustmParam == null || tempCustmParam.isEmpty()) {
				System.out.println(
						"Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
								+ cust_param + " validation skipped");
				logStep("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
						+ cust_param + " validation skipped");
				Assert.fail(
						"Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
								+ cust_param + " validation skipped");
			}
		}

	}

	public static void validate_Noncustom_param_val_of_gampad(String excelName, String sheetName, String cust_param,
			String expected) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		boolean adCallFound = false;

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			iuId = videoIUValue;
		}else if(sheetName.equalsIgnoreCase("DailyDetails")) {
			iuId = data[11][1]+currentday1;
		}
		else {
			iuId = data[11][1];
		} 
		/*else if(sheetName.equalsIgnoreCase("pullrefresh")) {
			iuId = data[11][1];
			iuId = "iu=%2F7646%2Fapp_android_us%2Fdb_display%2Fhome_screen%2Fhourly";
		}
		else if(sheetName.equalsIgnoreCase("Marquee")){
			iuId = "iu=%2F7646%2Fapp_android_us%2Fdb_display%2Fhome_screen%2Fmarquee";
		}else if(sheetName.equalsIgnoreCase("Feed1")) {
			iuId = "iu=%2F7646%2Fapp_android_us%2Fdb_display%2Ffeed%2Ffeed_1";
		}else if(sheetName.equalsIgnoreCase("Hourly")) {
			iuId = "iu=%2F7646%2Fapp_android_us%2Fdb_display%2Fdetails%2Fhourly";
		}*/
		
		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				adCallFound = true;
				tempCustmParam = getNonCustomParamBy_iu_value(qry, cust_param);
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}

		}

		if (!adCallFound) {
			System.out.println("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: "
					+ cust_param + " validation skipped");
			logStep("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
					+ " validation skipped");
			Assert.fail("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
					+ " validation skipped");
		} else if (adCallFound && !tempCustmParam.isEmpty()) {
			System.out.println(cust_param + " value of from gampad call  of : " + iuId + " is " + tempCustmParam);
			if (expected.equalsIgnoreCase("NotNull")) {
				if (!tempCustmParam.equalsIgnoreCase("nl")) {
					System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is matched with the expected value " + expected);
					logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is matched with the expected value " + expected);
				} else {
					System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expected);
					logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expected);
					Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expected);
				}
			} else {
				if (tempCustmParam.equalsIgnoreCase(expected)) {
					System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is matched with the expected value " + expected);
					logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is matched with the expected value " + expected);
				} else {
					System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expected);
					logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expected);
					Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
							+ " is not matched with the expected value " + expected);
				}
			}

		} else if (tempCustmParam == null || tempCustmParam.isEmpty()) {
			System.out.println("Custom parameter :" + cust_param
					+ " not found/no value in ad call, hence Custom Parameter: " + cust_param + " validation skipped");
			logStep("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
					+ cust_param + " validation skipped");
			Assert.fail("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
					+ cust_param + " validation skipped");
		}

	}

	public static void get_iu_value_of_Feedcall(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		boolean adCallFound = false;
		videoIUValue = null;
		outfile = new File(System.getProperty("user.dir") + "/myoutputFile.xml");
		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		iuId = null;
		String iuValue = null;

		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains("iu=")) {
				adCallFound = true;
				tempCustmParam = getNonCustomParamBy_iu_value(qry, "iu");
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}
		}
		try {
			iuValue = tempCustmParam.replace("/", "%2F");
			iuValue = "iu=" + iuValue;
			if (sheetName.equalsIgnoreCase("PreRollVideo")) {
				videoIUValue = iuValue;
				iuId = iuValue;
			} else {
				iuId = iuValue;
			}
		} catch (Exception e) {
			System.out.println("There is an exception while framing iu value");
			logStep("There is an exception while framing iu value");
		}

		if (!adCallFound) {
			System.out.println("Ad Call not found in charles session");
			logStep("Ad Call not found in charles session");
			Assert.fail("Ad Call not found in charles session");
		} else if (iuValue == null || iuValue.isEmpty()) {
			System.out.println("Ad Call not found/no value in ad call");
			logStep("Ad Call not found/no value in ad call");
			Assert.fail("Ad Call not found/no value in ad call");
		} else {
			System.out.println("Ad Call " + iuId + " found in charles session");
			logStep("Ad Call " + iuId + " found in charles session");
		}

	}

	public static String return_iu_value_of_Feedcall(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		boolean adCallFound = false;
		videoIUValue = null;
		outfile = new File(System.getProperty("user.dir") + "/myoutputFile.xml");
		// Read the content form file
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		//String iuId = null;
		iuId = null;
		String iuValue = null;

		String tempCustmParam = null;
		for (String qry : getQueryList) {
			if (qry.contains("iu=")) {
				adCallFound = true;
				tempCustmParam = getNonCustomParamBy_iu_value(qry, "iu");
				// if (!"".equals(tempCustmParam))
				// customParamsList.add(getCustomParamsBy_iu_value(qry));
				break;
			}
		}
		try {
			iuValue = tempCustmParam.replace("/", "%2F");
			iuValue = "iu=" + iuValue;
			if (sheetName.equalsIgnoreCase("PreRollVideo")) {
				videoIUValue = iuValue;
				iuId = iuValue;
			} else {
				iuId = iuValue;
			}
		} catch (Exception e) {
			System.out.println("There is an exception while framing iu value");
			logStep("There is an exception while framing iu value");
		}

		if (!adCallFound) {
			System.out.println("Ad Call not found in charles session");
			logStep("Ad Call not found in charles session");
			//Assert.fail("Ad Call not found in charles session");
		} else if (iuValue == null || iuValue.isEmpty()) {
			System.out.println("Ad Call not found/no value in ad call");
			logStep("Ad Call not found/no value in ad call");
			//Assert.fail("Ad Call not found/no value in ad call");
		} else {
			System.out.println("Ad Call " + iuId + " found in charles session");
			logStep("Ad Call " + iuId + " found in charles session");
		}
		return iuId;

	}
	
public static String return_iu_value_from_query_parameter_of_Feedcall(String query) throws Exception {
		
		String iuValue = null;
		String tempCustmParam = null;
		tempCustmParam = getNonCustomParamBy_iu_value(query, "iu");
		
		try {
			iuValue = tempCustmParam.replace("/", "%2F");
			iuValue = "iu=" + iuValue;
						
		} catch (Exception e) {
			System.out.println("There is an exception while framing iu value");
			logStep("There is an exception while framing iu value");
		}

		
		return iuValue;

	}
	
	

	

	public static void twcAppInstalledCheck() throws Exception {
		/*
		 * try { boolean twcAppInstalled = Ad.isAppInstalled("com.weather.TWC");
		 * }catch(Exception e) { Utils.setAbortTestSuite(true); }
		 */
		boolean twcAppInstalled = false;
		System.out.println("****************TWC App Installed Check Started****************");
		logStep("****************TWC App Installed Check Started****************");
		for (int i = 0; i <= 24; i++) {
			twcAppInstalled = Ad.isAppInstalled("com.weather.TWC");
			Thread.sleep(10000);
			if (twcAppInstalled) {
				System.out.println("TWC App Found Installed at milli seconds : " + i * 10000);
				logStep("TWC App Found Installed at milli seconds : " + i * 10000);
				break;
			}
		}

		if (twcAppInstalled) {
			System.out.println("TWC App Installed");
			logStep("TWC App Installed");
		} else {
			System.out.println("TWC App Not Installed");
			logStep("TWC App Not Installed");
			Assert.fail("TWC App Not Installed");
		}

	}

	public static String shortCardName(String cardName) {
		if (cardName.contains("-card")) {
			// adcardname = cardName;
			cardName = cardName.replaceAll("-card", "");
			System.out.println("Current Card Name is : " + cardName);
			if (cardName.contains("health-and-activities")) {
				cardName = "lifestyle";
			} else if (cardName.contains("air-quality")) {
				cardName = "aq";
			} else if (cardName.contains("map")) {
				cardName = "radar.largead";
			} else if (cardName.contains("seasonal-hub")) {
				cardName = "seasonalhub";
			} else if (cardName.contains("breaking-news")) {
				cardName = "breakingnews";
			} else if (cardName.contains("daily")) {
				cardName = "daily";
			} else if (cardName.contains("hurricane-central")) {
				cardName = "hurricane-central";
			} else if (cardName.contains("today")) {
				cardName = "today";
			} else if (cardName.contains("video")) {
				cardName = "video";
			} else if (cardName.contains("news")) {
				cardName = "news";
			} else if (cardName.contains("privacy-card")) {
				cardName = "privacy";
			}
		}

		return cardName;
	}

	


	public static void captureScreen() {
		try {
			attachScreen();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	public static void verifyCriteo_inapp_v2_Call(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String host = data[2][1];
		String path = data[3][1];
		boolean flag = verifyAPICalWithHostandPath(host, path);
		if (flag) {
			System.out.println(host + path + " call is present in Charles session");
			logStep(host + path + " call is present in Charles session");

		} else {
			System.out.println(host + path + " call is not present in Charles session");
			logStep(host + path + " call is not present in Charles session");

			Assert.fail(host + path + " call is not present in Charles session");

		}
	}

	public static void verifyCriteo_inapp_v2_Call(String excelName, String sheetName, boolean expected)
			throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String host = data[2][1];
		String path = data[3][1];
		boolean flag = verifyAPICalWithHostandPath(host, path);
		if (flag) {
			System.out.println(host + path + " call is present in Charles session");
			logStep(host + path + " call is present in Charles session");

		} else {
			System.out.println(host + path + " call is not present in Charles session");
			logStep(host + path + " call is not present in Charles session");
		}

		if (expected == flag) {
			System.out.println(host + path + " :API Call Verification is successfull");
			logStep(host + path + " :API Call Verification is successfull");

		} else {
			System.out.println(host + path + " :API Call Verification is failed");
			logStep(host + path + " :API Call Verification is failed");
			if (expected) {
				System.out.println(host + path + " :API Call expected to present but it not exists");
				logStep(host + path + " :API Call expected to present but it not exists");
				Assert.fail(host + path + " :API Call expected to present but it not exists");
			} else {
				System.out.println(host + path + " :API Call is not expected to present but it exists");
				logStep(host + path + " :API Call is not expected to present but it exists");
				Assert.fail(host + path + " :API Call is not expected to present but it exists");
			}
		}
	}

	public static void verifyCriteo_config_app_Call(String excelName, String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String host = data[2][1];
		String path = data[4][1];
		boolean flag = verifyAPICalWithHostandPath(host, path);
		if (flag) {
			System.out.println(host + path + " call is present in Charles session");
			logStep(host + path + " call is present in Charles session");

		} else {
			System.out.println(host + path + " call is not present in Charles session");
			logStep(host + path + " call is not present in Charles session");

			Assert.fail(host + path + " call is not present in Charles session");

		}
	}

	public static void verifyCriteo_config_app_Call(String excelName, String sheetName, boolean expected)
			throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String host = data[2][1];
		String path = data[4][1];
		boolean flag = verifyAPICalWithHostandPath(host, path);
		if (flag) {
			System.out.println(host + path + " call is present in Charles session");
			logStep(host + path + " call is present in Charles session");

		} else {
			System.out.println(host + path + " call is not present in Charles session");
			logStep(host + path + " call is not present in Charles session");
		}
		if (expected == flag) {
			System.out.println(host + path + " :API Call Verification is successfull");
			logStep(host + path + " :API Call Verification is successfull");

		} else {
			System.out.println(host + path + " :API Call Verification is failed");
			logStep(host + path + " :API Call Verification is failed");
			if (expected) {
				System.out.println(host + path + " :API Call expected to present but it not exists");
				logStep(host + path + " :API Call expected to present but it not exists");
				Assert.fail(host + path + " :API Call expected to present but it not exists");
			} else {
				System.out.println(host + path + " :API Call is not expected to present but it exists");
				logStep(host + path + " :API Call is not expected to present but it exists");
				Assert.fail(host + path + " :API Call is not expected to present but it exists");
			}
		}

	}

	public static String get_param_value_from_APIRequest(String host, String path, String cust_param) throws Exception {
		// readExcelValues.excelValues(excelName, sheetName);
		File fXmlFile = new File(CharlesFunctions.outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@host";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;

		boolean iuExists = false;
		for (String qry : getQueryList) {
			
			if (qry.contains(host)) {
				iuExists = true;
				break;
			}
		}
		boolean hflag = false;
		boolean pflag = false;
		boolean resflag = false;
		String ApiParamValue = null;

		if (iuExists) {
			System.out.println(host + "  call is present");
			logStep(host + "  call is present");
			outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
				// System.out.println("Total transactions: "+nodeList.getLength());
				if (nodeList.item(p) instanceof Node) {
					Node node = nodeList.item(p);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							// System.out.println("node1 length is: "+nl.getLength());
							Node innernode = nl.item(j);
							if (innernode != null) {
								// System.out.println("Innernode name is: "+innernode.getNodeName());
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											// System.out.println("node2 length is: "+n2.getLength());
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													// System.out.println("Innernode2 element name is:
													// "+eElement.getNodeName());
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			if (content.contains(host)) {
																				hflag = true;
																				// System.out.println("request body
																				// found "
																				// + content);

																			} else if (content.contains(path)) {
																				pflag = true;
																				// System.out.println("request body
																				// found "
																				// + content);
																			}
																		}
																		
																		//if(hflag && !pflag) {
																			if (eElement1.getNodeName().equals("first-line")) {
																				String content = eElement1.getTextContent();
																				// System.out.println("request body
																				// "+content);

																				if (content.contains(path)) {
																					pflag = true;
																					// System.out.println("request body
																					// found "
																					// + content);
																				}
																			}
																	//	}
																	}
																}
															}
														}
													}
													if (hflag && pflag) {
														if (eElement.getNodeName().equals("body")) {
															String scontent = eElement.getTextContent();
															if (scontent.contains(cust_param)) {
																// System.out.println("request body " + scontent);
																ApiParamValue = get_Param_Value_inJsonBody(scontent,
																		cust_param);
																break outerloop;

															}

														}

													}

												}
											}
										}
									}
								}

								/*
								 * if (hflag && pflag) { resflag = true; break outerloop; }
								 */
							}
						}
					}
				}
				// flag = false;
			}

		} else {
			System.out.println(host + " ad call is not present");
			logStep(host + " ad call is not present");

		}

		// return resflag;
		// System.out.println("Parameter value obtined from criteo request is :" +
		// ApiParamValue);
		return ApiParamValue;

	}


	public static void validate_Criteo_SDK_config_app_call_parameter(String excelName, String sheetName,
			String cust_param, String expected) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String host = data[2][1];
		String path = data[4][1];

		boolean flag = verifyAPICalWithHostandPath(host, path);
		if (flag) {
			System.out.println(host + path + " call is present in Charles session");
			logStep(host + path + " call is present in Charles session");

			String actual = get_param_value_from_APIRequest(host, path, cust_param);

			if (actual.equalsIgnoreCase(expected)) {
				System.out.println("Custom Parameter :" + cust_param + " value: " + actual
						+ " is matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + actual + " is matched with the expected value "
						+ expected);
			} else {
				System.out.println("Custom Parameter :" + cust_param + " value: " + actual
						+ " is not matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + actual
						+ " is not matched with the expected value " + expected);
				Assert.fail("Custom Parameter :" + cust_param + " value: " + actual
						+ " is not matched with the expected value " + expected);
			}

		} else {
			System.out.println(host + path + " call is not present in Charles session, hence Custom Parameter: "
					+ cust_param + " validation skipped");
			logStep(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
					+ " validation skipped");

			Assert.fail(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
					+ " validation skipped");

		}

	}

	// get b values from amazon aax calls of XML File and store to list
	public static void get_Criteo_SDK_inapp_v2_call_response_parameter_by_placementId(String excelName,
			String sheetName, String placementId, String cust_param, boolean clearList) throws Exception {

		String[][] data = read_excel_data.exceldataread(sheetName);
		String host = data[2][1];
		String path = data[3][1];
		// Read the content form file
		File fXmlFile = new File(outfile.getName());
		if (clearList) {
			listOf_criteo_Params.clear();
			criteocallsSize = 0;
		}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");

		// Read JSONs and get b value
		// List<String> jsonBValuesList = new ArrayList<String>();

		// String slotId = data[21][1];

		// String slotId = "c4dd8ec4-e40c-4a63-ae81-8f756793ac5e";
		// weather.hourly

		boolean flag = false;
		boolean hflag = false;
		boolean pflag = false;
		boolean resflag = false;
		List<String> istofRequestBodies = new ArrayList<String>();
		List<String> istofResponseBodies = new ArrayList<String>();
		// List<String> listOf_b_Params = new ArrayList<String>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			hflag = false;
			pflag = false;
			if (nodeList.item(i) instanceof Node) {
				Node node = nodeList.item(i);
				if (node.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					for (int j = 0; j < nl.getLength(); j++) {
						Node innernode = nl.item(j);
						if (innernode != null) {
							if (innernode.getNodeName().equals("request")) {
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												if (eElement.getNodeName().equals("headers")) {
													if (innernode2.hasChildNodes()) {
														NodeList n3 = innernode2.getChildNodes();
														for (int q = 0; q < n3.getLength(); q++) {
															// System.out.println("node3 length is:
															// "+n3.getLength());
															Node innernode3 = n3.item(q);
															if (innernode3 != null) {
																// System.out.println("Innernode3 name is:
																// "+innernode3.getNodeName());
																if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																	Element eElement1 = (Element) innernode3;
																	// System.out.println("Innernode3 element name
																	// is: "+eElement1.getNodeName());
																	if (eElement1.getNodeName().equals("header")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		if (content.contains(host)) {
																			hflag = true;
																			// System.out.println("request body found "
																			// + content);

																		} else if (content.contains(path)) {
																			pflag = true;
																			// System.out.println("request body found "
																			// + content);
																		}
																	}

																	// this condition especially for android since its
																	// file has path value under first-line element
																	if (eElement1.getNodeName().equals("first-line")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		if (content.contains(path)) {
																			pflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);
																		}
																	}

																}
															}
														}
													}
												}

												if (hflag && pflag) {
													if (eElement.getNodeName().equals("body")) {
														String scontent = eElement.getTextContent();

														/*
														 * if (scontent.contains(placementId)) { flag = true;
														 * criteocallsSize++; istofRequestBodies.add(scontent); //
														 * System.out.println("request body "+scontent);
														 * 
														 * }
														 */
														boolean tempFlag = verify_criteo_request_for_given_placementId_inJsonRequestBody(
																placementId, scontent);
														if (tempFlag) {
															flag = true;
															criteocallsSize++;
															istofRequestBodies.add(scontent);
															// System.out.println("request body "+scontent);

														}

													}

												}
											}
										}
									}
								}
							}

							if (flag) {
								if (innernode.getNodeName().equals("response")) {
									// System.out.println(innernode.getNodeName());
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("body")) {
														String content = eElement.getTextContent();
														istofResponseBodies.add(content);
														// String tempBparam = get_b_value_inJsonResponseBody(content);
														String tempBparam = get_criteo_response_parameter_value_by_placementId_inJsonResponseBody(
																placementId, cust_param, content);
														if (!"".contentEquals(tempBparam)) {
															listOf_criteo_Params.add(tempBparam);
														} else {
															listOf_criteo_Params.add("-1");
														}
														// System.out.println("response body "+content);
													}
												}
											}
										}
									}
								}

							}

						}
					}
				}
			}
			flag = false;
		}
		System.out.println(listOf_criteo_Params);
		logStep(listOf_criteo_Params.toString());

		criteocallsResponseSize = listOf_criteo_Params.size();
		criteoparamErrorCount = 0;
		for (String b : listOf_criteo_Params) {
			System.out.println(cust_param + " value from JSON-----------> " + b);
			if (b.contentEquals("error")) {
				criteoparamErrorCount++;
			}
		}
		System.out.println("Criteo calls Size is: " + criteocallsSize);
		logStep("Criteo calls Size is: " + criteocallsSize);
		System.out.println("Criteo callsResponse Size is: " + criteocallsResponseSize);
		logStep("Criteo callsResponse Size is: " + criteocallsResponseSize);
		System.out.println("Criteo Param ErrorCount size is: " + criteoparamErrorCount);
		logStep("Criteo Param ErrorCount size is: " + criteoparamErrorCount);

	}

	public static String get_criteo_response_parameter_value_by_placementId_inJsonResponseBody(String placementId,
			String cust_param, String apiData) throws Exception {

		// Functions.Read_Turbo_api("Cust_Param", readSheet);

		JSONParser parser = new JSONParser();
		// System.out.println("adreq1 is : "+adreq1.toString());
		Object obj = parser.parse(new String(apiData));
		// System.out.println("obj : "+obj);
		JSONObject jsonObject = (JSONObject) obj;
		String ApiParamValue = "";
		String JsonParam = null;

		JSONObject mainTag = null;
		JSONArray eleArray = null;

		try {
			JsonParam = "slots".trim();
			eleArray = (JSONArray) jsonObject.get(JsonParam);
			// System.out.println(eleArray);
			try {

				ArrayList<String> Ingredients_names = new ArrayList<>();
				for (int i = 0; i < eleArray.size(); i++) {

					String arrayElement = String.valueOf(eleArray.get(i));

					Ingredients_names.add(arrayElement);
					obj = parser.parse(new String(arrayElement));
					jsonObject = (JSONObject) obj;
					mainTag = (JSONObject) obj;

					try {
						String cApiParamValue = String.valueOf(mainTag.get("placementId"));
						if (cApiParamValue.equalsIgnoreCase(placementId)) {
							if (cust_param.equalsIgnoreCase("size")) {
								String width = String.valueOf(mainTag.get("width"));
								String height = String.valueOf(mainTag.get("height"));
								ApiParamValue = width.concat("x").concat(height);
							} else {
								ApiParamValue = String.valueOf(mainTag.get(cust_param));
							}

						} else {
							// System.out.println("... noticed");
							continue;
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}

				}
			} catch (Exception e) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * try { JSONArray arrayElementValues = (JSONArray) mainTag .get(cust_param);
		 * ApiParamValue = String.valueOf(arrayElementValues.get(0)); } catch (Exception
		 * e) { ApiParamValue = String.valueOf(mainTag.get(cust_param)); }
		 */
		System.out.println(cust_param + " Param Values from Criteo API Call is : " + ApiParamValue);
		logStep(cust_param + " Param Values from Criteo API Call is : " + ApiParamValue);
		/*
		 * if (ApiParamValue.equalsIgnoreCase("null")) { ApiParamValue = "nl"; }
		 */
		return ApiParamValue;
	}

	/**
	 * This method iterates through charles session file and look for all instances
	 * of given feed call and retrive the given custom parameter value and add to
	 * list
	 * 
	 * @param feedCall
	 * @param cust_param
	 * @throws Exception
	 */
	public static void get_custom_param_values_from_gampadCalls(String excelName,
			String sheetName, String feedCall, String cust_param) throws Exception {
		// String[][] data = read_excel_data.exceldataread(sheetName);

		// Read the content form file
		File fXmlFile = new File(CharlesFunctions.outfile.getName());
		customParamsList.clear();
		criteogampadcallcount = 0;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		// NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		// List<String> customParamsList = new ArrayList<String>();

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		// String iuId = "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fcard%2Fdaily";
		String queryIU = null;
		for (String qry : getQueryList) {
			if (qry.contains(feedCall)) {
				// after it checks for contains, now performing exact validation of IU value,
				// for ex: hourly contains in hourly1, hourly2, hourly3
				if (sheetName.equalsIgnoreCase("Marquee")) {
					queryIU = return_iu_value_from_query_parameter_of_Feedcall(qry);
					if (queryIU.equalsIgnoreCase(feedCall)) {
						criteogampadcallcount++;
						String tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
						if (!"".equals(tempCustmParam)) {
							customParamsList.add(getCustomParamBy_iu_value(qry, cust_param));
						} else {
							customParamsList.add("-1");
						}
					}
				} else {
					criteogampadcallcount++;
					String tempCustmParam = getCustomParamBy_iu_value(qry, cust_param);
					if (!"".equals(tempCustmParam)) {
						customParamsList.add(getCustomParamBy_iu_value(qry, cust_param));
					} else {
						customParamsList.add("-1");
					}
				}

			}
		}
		System.out.println("Criteo Parameters found in gampad call are: ");
		logStep("Criteo Parameters found in gampad call are: ");
		System.out.println(customParamsList);
		logStep(customParamsList.toString());
		System.out.println("No of times the gampad call found is: " + criteogampadcallcount);
		logStep("No of times the gampad call found is: " + criteogampadcallcount);

	}

	/**
	 * Store the Criteo parameter values to a List
	 * @param excelName
	 * @param sheetName
	 * @param cust_param
	 * @param clearList
	 * @throws Exception
	 */
	public static void load_Criteo_SDK_inapp_v2_call_response_parameter_by_placementId(String excelName,
			String sheetName, String cust_param, boolean clearList) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		String placementId = null;
		if (sheetName.equalsIgnoreCase("Pulltorefresh")) {
			placementId = "weather.homescreen.standardAd0";
		} else if (sheetName.equalsIgnoreCase("Hourly")) {
			placementId = "weather.hourly";
		} else if (sheetName.equalsIgnoreCase("Feed1")) {
			placementId = "weather.feed1";
		} else if (sheetName.equalsIgnoreCase("Feed2")) {
			placementId = "weather.feed2";
		} else if (sheetName.equalsIgnoreCase("Feed3")) {
			placementId = "weather.feed3";
		} else if (sheetName.equalsIgnoreCase("Feed4")) {
			placementId = "weather.feed4";
		} else if (sheetName.equalsIgnoreCase("Feed5")) {
			placementId = "weather.feed5";
		} else if (sheetName.equalsIgnoreCase("Feed6")) {
			placementId = "weather.feed6";
		} else if (sheetName.equalsIgnoreCase("Feed7")) {
			placementId = "weather.feed7";
		} else if (sheetName.equalsIgnoreCase("Air Quality(Content)")) {
			placementId = "weather.aq";
		} else if (sheetName.equalsIgnoreCase("SeasonalHub(Details)")) {
			placementId = "weather.seasonlhub";
		} else if (sheetName.equalsIgnoreCase("Today")) {
			placementId = "weather.trending";
		} else if (sheetName.equalsIgnoreCase("Daily(10day)")) {
			placementId = "weather.10day.largeAds";
		} else if (sheetName.equalsIgnoreCase("Map")) {
			placementId = "weather.maps";
		} else if (sheetName.equalsIgnoreCase("MyAlerts")) {
			placementId = "weather.alerts.center";
		} else if (sheetName.equalsIgnoreCase("weather.alerts")) {
			placementId = "weather.alerts";
		} else if (sheetName.equalsIgnoreCase("Health(coldAndFluArticles)")) {
			placementId = "weather.articles";
		} else if (sheetName.equalsIgnoreCase("Health(goRun)")) {
			placementId = "weather.running.largeAds";
		} else if (sheetName.equalsIgnoreCase("Health(boatAndBeach)")) {
			placementId = "content.beach.largeAds";
		}else if (sheetName.equalsIgnoreCase("Hourly1")) {
			placementId = "weather.hourly.bigad";
		}else if (sheetName.equalsIgnoreCase("Hourly2")) {
			placementId = "weather.hourly.bigad";
		}else if (sheetName.equalsIgnoreCase("Hourly3")) {
			placementId = "weather.hourly.bigad";
		}
		String feedCall = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			feedCall = videoIUValue;
		} /*
			 * else if (sheetName.equalsIgnoreCase("IDD")) { String today =
			 * dailyDetailsDayOfWeek.concat("1"); feedCall = data[18][1];
			 * feedCall = feedCall.concat("_") + today; }
			 */else {
			feedCall = data[18][1];
		}

		get_Criteo_SDK_inapp_v2_call_response_parameter_by_placementId("Smoke", "Criteo", placementId, cust_param,
				clearList);
	}

	/**
	 * To Validate Criteo Parameter values against corresponding gampad call values
	 * @param excelName
	 * @param sheetName
	 * @param cust_param
	 * @param clearList
	 * @throws Exception
	 */
	public static void validate_Criteo_SDK_inapp_v2_call_param_value_with_gampad_param_value(String excelName,
			String sheetName, String cust_param, boolean clearList) throws Exception {

		String[][] data = read_excel_data.exceldataread(sheetName);
		String placementId = null;
		if (sheetName.equalsIgnoreCase("Pulltorefresh")) {
			placementId = "weather.homescreen.standardAd0";
		} else if (sheetName.equalsIgnoreCase("Hourly")) {
			placementId = "weather.hourly";
		} else if (sheetName.equalsIgnoreCase("Feed1")) {
			placementId = "weather.feed1";
		} else if (sheetName.equalsIgnoreCase("Feed2")) {
			placementId = "weather.feed2";
		} else if (sheetName.equalsIgnoreCase("Feed3")) {
			placementId = "weather.feed3";
		} else if (sheetName.equalsIgnoreCase("Feed4")) {
			placementId = "weather.feed4";
		} else if (sheetName.equalsIgnoreCase("Feed5")) {
			placementId = "weather.feed5";
		} else if (sheetName.equalsIgnoreCase("Feed6")) {
			placementId = "weather.feed6";
		} else if (sheetName.equalsIgnoreCase("Feed7")) {
			placementId = "weather.feed7";
		} else if (sheetName.equalsIgnoreCase("Air Quality(Content)")) {
			placementId = "weather.aq";
		} else if (sheetName.equalsIgnoreCase("SeasonalHub(Details)")) {
			placementId = "weather.seasonlhub";
		} else if (sheetName.equalsIgnoreCase("Today")) {
			placementId = "weather.trending";
		} else if (sheetName.equalsIgnoreCase("Daily(10day)")) {
			placementId = "weather.10day.largeAds";
		} else if (sheetName.equalsIgnoreCase("Map")) {
			placementId = "weather.maps";
		} else if (sheetName.equalsIgnoreCase("MyAlerts")) {
			placementId = "weather.alerts.center";
		} else if (sheetName.equalsIgnoreCase("weather.alerts")) {
			placementId = "weather.alerts";
		} else if (sheetName.equalsIgnoreCase("Health(coldAndFluArticles)")) {
			/*
			 * News detils, flu articles, alergy articles has same placement Id, here
			 * anything can be used
			 */
			placementId = "weather.articles";
		} else if (sheetName.equalsIgnoreCase("Health(goRun)")) {
			placementId = "weather.running.largeAds";
		} else if (sheetName.equalsIgnoreCase("Health(boatAndBeach)")) {
			placementId = "content.beach.largeAds";
		}else if (sheetName.equalsIgnoreCase("Hourly1")) {
			placementId = "weather.hourly.bigad";
		}else if (sheetName.equalsIgnoreCase("Hourly2")) {
			placementId = "weather.hourly.bigad";
		}else if (sheetName.equalsIgnoreCase("Hourly3")) {
			placementId = "weather.hourly.bigad";
		}
		String feedCall = null;
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		if (sheetName.equalsIgnoreCase("PreRollVideo")) {
			//feedCall = videoIUValue;
		} /*
			 * else if (sheetName.equalsIgnoreCase("IDD")) { String today =
			 * dailyDetailsDayOfWeek.concat("1"); feedCall = data[18][1];
			 * feedCall = feedCall.concat("_") + today; }
			 */else {
			feedCall =data[18][1];
		}

		boolean testpass = false;
		int failCount = 0;

		get_Criteo_SDK_inapp_v2_call_response_parameter_by_placementId("Smoke", "Criteo", placementId, cust_param,
				clearList);

		if (criteocallsSize == 0) {
			System.out.println("Criteo call is not generated in current session, so skipping the " + cust_param
					+ " value verification");
			logStep("Criteo call is not generated in current session, so skipping the " + cust_param
					+ " value verification");

		} else if (criteocallsResponseSize == 0) {
			System.out.println("Criteo call response doesn't have the placement id: " + placementId
					+ "  i.e. bidding is not happened in current session, so skipping the " + cust_param
					+ " value verification");
			logStep("Criteo call response doesn't have the placement id: " + placementId
					+ "  i.e. bidding is not happened in current session, so skipping the " + cust_param
					+ " value verification");

		} else if (criteoparamErrorCount == criteocallsResponseSize) {
			System.out.println(
					"Criteo call response contains error i.e. bidding is not happened in current session, so skipping the "
							+ cust_param + " value verification");
			logStep("Criteo call response contains error i.e. bidding is not happened in current session, so skipping the "
					+ cust_param + " value verification");

		} else if (nextGenIMadDisplayed && sheetName.equalsIgnoreCase("Pulltorefresh")) {
			/*
			 * There may be chances that gampad call might not generated.. for ex: when IM
			 * ad displayed on home screen, then homescreen today call doesnt generate
			 * 
			 */
			System.out.println("Since IM Ad displayed on App Launch, Homescreen Today call: " + cust_param
					+ " id validation is skipped");
			logStep("Since IM Ad displayed on App Launch, Homescreen Today call: " + cust_param
					+ " id validation is skipped");
		} else {
			if (cust_param.contentEquals("displayUrl")) {
				cust_param = "displayurl";
			}
			get_custom_param_values_from_gampadCalls(excelName, sheetName, feedCall, "crt_" + cust_param);
			if (criteogampadcallcount == 0) {
				System.out.println("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: "
						+ cust_param + " validation failed");
				logStep("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: " + cust_param
						+ " validation failed");
				Assert.fail("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: "
						+ cust_param + " validation failed");
			} else if (customParamsList.size() == 0) {
				System.out.println("Ad Call :" + feedCall + " not having Custom Parameter: " + cust_param
						+ ", hence Custom Parameter: " + cust_param + " validation failed");
				logStep("Ad Call :" + feedCall + " not having Custom Parameter: " + cust_param
						+ ", hence Custom Parameter: " + cust_param + " validation failed");
				Assert.fail("Ad Call :" + feedCall + " not having Custom Parameter: " + cust_param
						+ ", hence Custom Parameter: " + cust_param + " validation failed");
			} else {

				int maxIterations = 0;
				if (listOf_criteo_Params.size() > customParamsList.size()) {
					maxIterations = customParamsList.size();
				} else {
					maxIterations = listOf_criteo_Params.size();
				}

				if (!sheetName.equalsIgnoreCase("Health(goRun)")
						&& !sheetName.equalsIgnoreCase("Health(boatAndBeach)")) {
					for (int i = 0; i < maxIterations; i++) {

						if (listOf_criteo_Params.get(i).equalsIgnoreCase("-1")) {
							if (listOf_criteo_Params.size() == 1) {
								System.out.println(
										"It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
								logStep("It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
							} else {
								System.out.println("It looks that: " + i
										+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
								logStep("It looks that: " + i
										+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
							}

						} else {

							if (cust_param.equalsIgnoreCase("displayurl")) {

								if (customParamsList.get(i).equalsIgnoreCase("-1")) {
									System.out.println(i + " Occurance of corresponding " + sheetName + " gampad call: "
											+ feedCall + " not having parameter " + cust_param);
									logStep(i + " Occurance of corresponding " + sheetName + " gampad call: " + feedCall
											+ " not having parameter " + cust_param);
									failCount++;
								} else {
									System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is  matched with " + i
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i));
									logStep(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is  matched with " + i
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i));

								}

							} else {
								if (listOf_criteo_Params.get(i).equalsIgnoreCase(customParamsList.get(i))) {

									System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is matched with " + i
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i));
									logStep(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is matched with " + i
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i));
								} else {
									if (customParamsList.get(i).equalsIgnoreCase("-1")) {
										System.out.println(i + " Occurance of corresponding " + sheetName
												+ " gampad call: " + feedCall + " not having parameter " + cust_param);
										logStep(i + " Occurance of corresponding " + sheetName + " gampad call: "
												+ feedCall + " not having parameter " + cust_param);
										failCount++;
									} else {
										System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is not matched with " + i
												+ " Occurance of corresponding " + sheetName + " gampad call "
												+ cust_param + " value: " + customParamsList.get(i));
										logStep(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is not matched with " + i
												+ " Occurance of corresponding " + sheetName + " gampad call "
												+ cust_param + " value: " + customParamsList.get(i));
										failCount++;
									}

								}
							}

						}

					}

				} else {
					for (int i = 0; i < maxIterations / 2; i++) {

						if (listOf_criteo_Params.get(i).equalsIgnoreCase("-1")) {
							if (listOf_criteo_Params.size() == 1) {
								System.out.println(
										"It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
								logStep("It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
							} else {
								System.out.println("It looks that: " + i
										+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
								logStep("It looks that: " + i
										+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
							}

						} else {
							if (cust_param.equalsIgnoreCase("displayurl")) {

								if (customParamsList.get(i + 1).equalsIgnoreCase("-1")) {
									System.out.println(i + 1 + " Occurance of corresponding " + sheetName
											+ " gampad call: " + feedCall + " not having parameter " + cust_param);
									logStep(i + 1 + " Occurance of corresponding " + sheetName + " gampad call: "
											+ feedCall + " not having parameter " + cust_param);
									failCount++;
								} else {
									System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is  matched with " + i + 1
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i + 1));
									logStep(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is  matched with " + i + 1
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i + 1));

								}

							} else {
								if (listOf_criteo_Params.get(i).equalsIgnoreCase(customParamsList.get(i + 1))) {

									System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is matched with " + i + 1
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i + 1));
									logStep(i + " Occurance of Criteo call " + cust_param + " value: "
											+ listOf_criteo_Params.get(i) + " is matched with " + i + 1
											+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
											+ " value: " + customParamsList.get(i + 1));
								} else {
									if (customParamsList.get(i + 1).equalsIgnoreCase("-1")) {
										System.out.println(i + 1 + " Occurance of corresponding " + sheetName
												+ " gampad call: " + feedCall + " not having parameter " + cust_param);
										logStep(i + 1 + " Occurance of corresponding " + sheetName + " gampad call: "
												+ feedCall + " not having parameter " + cust_param);
										failCount++;
									} else {
										System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is not matched with " + i + 1
												+ " Occurance of corresponding " + sheetName + " gampad call "
												+ cust_param + " value: " + customParamsList.get(i + 1));
										logStep(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is not matched with " + i + 1
												+ " Occurance of corresponding " + sheetName + " gampad call "
												+ cust_param + " value: " + customParamsList.get(i + 1));
										failCount++;
									}

								}
							}

						}

					}
				}

			}
		}

		if (failCount > 0) {
			System.out.println("Criteo call " + cust_param + " values  not matched with corresponding " + sheetName
					+ " gampad call " + cust_param + " values");
			logStep("Criteo call " + cust_param + " values  not matched with corresponding " + sheetName
					+ " gampad call " + cust_param + " values");
			Assert.fail("Criteo call " + cust_param + " values  not matched with corresponding " + sheetName
					+ "  gampad call " + cust_param + " values");
		}

	}
	
	/**
	 * When there is same placement id given for multiple gampad calls, then criteo index is sending as parameter to map criteo value with gampad call. EX: Hourly1, Hourly2 & Hourly3 big ads
	 * @param excelName
	 * @param sheetName
	 * @param cust_param
	 * @param considerCriteoIndex
	 * @param criteoIndex
	 * @param clearList
	 * @throws Exception
	 */
		public static void validate_Criteo_SDK_inapp_v2_call_param_value_with_gampad_param_value(String excelName,
				String sheetName, String cust_param, boolean considerCriteoIndex, int criteoIndex, boolean clearList) throws Exception {

			String[][] data = read_excel_data.exceldataread(sheetName);
			String placementId = null;
			if (sheetName.equalsIgnoreCase("Pulltorefresh")) {
				placementId = "weather.homescreen.standardAd0";
			} else if (sheetName.equalsIgnoreCase("Hourly")) {
				placementId = "weather.hourly";
			} else if (sheetName.equalsIgnoreCase("Feed1")) {
				placementId = "weather.feed1";
			} else if (sheetName.equalsIgnoreCase("Feed2")) {
				placementId = "weather.feed2";
			} else if (sheetName.equalsIgnoreCase("Feed3")) {
				placementId = "weather.feed3";
			} else if (sheetName.equalsIgnoreCase("Feed4")) {
				placementId = "weather.feed4";
			} else if (sheetName.equalsIgnoreCase("Feed5")) {
				placementId = "weather.feed5";
			} else if (sheetName.equalsIgnoreCase("Feed6")) {
				placementId = "weather.feed6";
			} else if (sheetName.equalsIgnoreCase("Feed7")) {
				placementId = "weather.feed7";
			} else if (sheetName.equalsIgnoreCase("Air Quality(Content)")) {
				placementId = "weather.aq";
			} else if (sheetName.equalsIgnoreCase("SeasonalHub(Details)")) {
				placementId = "weather.seasonlhub";
			} else if (sheetName.equalsIgnoreCase("Today")) {
				placementId = "weather.trending";
			} else if (sheetName.equalsIgnoreCase("Daily(10day)")) {
				placementId = "weather.10day.largeAds";
			} else if (sheetName.equalsIgnoreCase("Map")) {
				placementId = "weather.maps";
			} else if (sheetName.equalsIgnoreCase("MyAlerts")) {
				placementId = "weather.alerts.center";
			} else if (sheetName.equalsIgnoreCase("weather.alerts")) {
				placementId = "weather.alerts";
			} else if (sheetName.equalsIgnoreCase("Health(coldAndFluArticles)")) {
				/*
				 * News detils, flu articles, alergy articles has same placement Id, here
				 * anything can be used
				 */
				placementId = "weather.articles";
			} else if (sheetName.equalsIgnoreCase("Health(goRun)")) {
				placementId = "weather.running.largeAds";
			} else if (sheetName.equalsIgnoreCase("Health(boatAndBeach)")) {
				placementId = "content.beach.largeAds";
			} else if (sheetName.equalsIgnoreCase("Hourly1")) {
				placementId = "weather.hourly.bigad";
			} else if (sheetName.equalsIgnoreCase("Hourly2")) {
				placementId = "weather.hourly.bigad";
			} else if (sheetName.equalsIgnoreCase("Hourly3")) {
				placementId = "weather.hourly.bigad";
			}
			String feedCall = null;
			// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
			if (sheetName.equalsIgnoreCase("PreRollVideo")) {
				feedCall = videoIUValue;
			} /*
				 * else if (sheetName.equalsIgnoreCase("IDD")) { String today =
				 * dailyDetailsDayOfWeek.concat("1"); feedCall = data[18][1];
				 * feedCall = feedCall.concat("_") + today; }
				 */else {
				feedCall = data[18][1];
			}

			boolean testpass = false;
			int failCount = 0;

			get_Criteo_SDK_inapp_v2_call_response_parameter_by_placementId("Smoke", "Criteo", placementId, cust_param,
					clearList);

			if (criteocallsSize == 0) {
				System.out.println("Criteo call is not generated in current session, so skipping the " + cust_param
						+ " value verification");
				logStep("Criteo call is not generated in current session, so skipping the " + cust_param
						+ " value verification");

			} else if (criteocallsResponseSize == 0) {
				System.out.println("Criteo call response doesn't have the placement id: " + placementId
						+ "  i.e. bidding is not happened in current session, so skipping the " + cust_param
						+ " value verification");
				logStep("Criteo call response doesn't have the placement id: " + placementId
						+ "  i.e. bidding is not happened in current session, so skipping the " + cust_param
						+ " value verification");

			} else if (criteoparamErrorCount == criteocallsResponseSize) {
				System.out.println(
						"Criteo call response contains error i.e. bidding is not happened in current session, so skipping the "
								+ cust_param + " value verification");
				logStep("Criteo call response contains error i.e. bidding is not happened in current session, so skipping the "
						+ cust_param + " value verification");

			} else if (nextGenIMadDisplayed && sheetName.equalsIgnoreCase("Pulltorefresh")) {
				/*
				 * There may be chances that gampad call might not generated.. for ex: when IM
				 * ad displayed on home screen, then homescreen today call doesnt generate
				 * 
				 */
				System.out.println("Since IM Ad displayed on App Launch, Homescreen Today call: " + cust_param
						+ " id validation is skipped");
				logStep("Since IM Ad displayed on App Launch, Homescreen Today call: " + cust_param
						+ " id validation is skipped");
			} else {
				if (cust_param.contentEquals("displayUrl")) {
					cust_param = "displayurl";
				}
				get_custom_param_values_from_gampadCalls(excelName, sheetName, feedCall, "crt_" + cust_param);
				if (criteogampadcallcount == 0) {
					System.out.println("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: "
							+ cust_param + " validation failed");
					logStep("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: " + cust_param
							+ " validation failed");
					Assert.fail("Ad Call :" + feedCall + " not found in charles session, hence Custom Parameter: "
							+ cust_param + " validation failed");
				} else if (customParamsList.size() == 0) {
					System.out.println("Ad Call :" + feedCall + " not having Custom Parameter: " + cust_param
							+ ", hence Custom Parameter: " + cust_param + " validation failed");
					logStep("Ad Call :" + feedCall + " not having Custom Parameter: " + cust_param
							+ ", hence Custom Parameter: " + cust_param + " validation failed");
					Assert.fail("Ad Call :" + feedCall + " not having Custom Parameter: " + cust_param
							+ ", hence Custom Parameter: " + cust_param + " validation failed");
				} else {

					int maxIterations = 0;
					if (listOf_criteo_Params.size() > customParamsList.size()) {
						maxIterations = customParamsList.size();
					} else {
						maxIterations = listOf_criteo_Params.size();
					}

					if (!sheetName.equalsIgnoreCase("Health(goRun)")
							&& !sheetName.equalsIgnoreCase("Health(boatAndBeach)")) {
						for (int i = 0; i < maxIterations; i++) {

							if (listOf_criteo_Params.get(i).equalsIgnoreCase("-1")) {
								if (listOf_criteo_Params.size() == 1) {
									System.out.println(
											"It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
									logStep("It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
								} else {
									System.out.println("It looks that: " + i
											+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
									logStep("It looks that: " + i
											+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
								}

							} else {

								if (considerCriteoIndex) {
									if (listOf_criteo_Params.size() >= criteoIndex) {
										criteoIndex = criteoIndex - 1;

										if (cust_param.equalsIgnoreCase("displayurl")) {

											if (customParamsList.get(i).equalsIgnoreCase("-1")) {
												System.out.println(
														i + " Occurance of corresponding " + sheetName + " gampad call: "
																+ feedCall + " not having parameter " + cust_param);
												logStep(i + " Occurance of corresponding " + sheetName + " gampad call: "
														+ feedCall + " not having parameter " + cust_param);
												failCount++;
											} else {
												System.out.println(i + " Occurance of Criteo call " + cust_param
														+ " value: " + listOf_criteo_Params.get(criteoIndex)
														+ " is  matched with " + i + " Occurance of corresponding "
														+ sheetName + " gampad call " + cust_param + " value: "
														+ customParamsList.get(i));
												logStep(i + " Occurance of Criteo call " + cust_param + " value: "
														+ listOf_criteo_Params.get(criteoIndex) + " is  matched with " + i
														+ " Occurance of corresponding " + sheetName + " gampad call "
														+ cust_param + " value: " + customParamsList.get(i));

											}

										} else {
											if (listOf_criteo_Params.get(criteoIndex)
													.equalsIgnoreCase(customParamsList.get(i))) {

												System.out.println(criteoIndex + " Occurance of Criteo call " + cust_param
														+ " value: " + listOf_criteo_Params.get(criteoIndex)
														+ " is matched with " + i + " Occurance of corresponding "
														+ sheetName + " gampad call " + cust_param + " value: "
														+ customParamsList.get(i));
												logStep(criteoIndex + " Occurance of Criteo call " + cust_param + " value: "
														+ listOf_criteo_Params.get(criteoIndex) + " is matched with " + i
														+ " Occurance of corresponding " + sheetName + " gampad call "
														+ cust_param + " value: " + customParamsList.get(i));
												break;
											} else {
												if (customParamsList.get(i).equalsIgnoreCase("-1")) {
													System.out.println(i + " Occurance of corresponding " + sheetName
															+ " gampad call: " + feedCall + " not having parameter "
															+ cust_param);
													logStep(i + " Occurance of corresponding " + sheetName
															+ " gampad call: " + feedCall + " not having parameter "
															+ cust_param);
													failCount++;
													break;
												} else {
													System.out.println(criteoIndex + " Occurance of Criteo call "
															+ cust_param + " value: "
															+ listOf_criteo_Params.get(criteoIndex)
															+ " is not matched with " + i + " Occurance of corresponding "
															+ sheetName + " gampad call " + cust_param + " value: "
															+ customParamsList.get(i));
													logStep(criteoIndex + " Occurance of Criteo call " + cust_param
															+ " value: " + listOf_criteo_Params.get(criteoIndex)
															+ " is not matched with " + i + " Occurance of corresponding "
															+ sheetName + " gampad call " + cust_param + " value: "
															+ customParamsList.get(i));
													failCount++;
													break;
												}

											}

										}

									} else {
										System.out.println(criteoIndex + " " + placementId
												+ " Criteo call is not generated in current session, so skipping the "
												+ cust_param + " value verification of " + sheetName + " gampad call ");
										logStep(criteoIndex + " " + placementId
												+ " Criteo call is not generated in current session, so skipping the "
												+ cust_param + " value verification of " + sheetName + " gampad call ");

									}

								} else {

									if (cust_param.equalsIgnoreCase("displayurl")) {

										if (customParamsList.get(i).equalsIgnoreCase("-1")) {
											System.out.println(i + " Occurance of corresponding " + sheetName
													+ " gampad call: " + feedCall + " not having parameter " + cust_param);
											logStep(i + " Occurance of corresponding " + sheetName + " gampad call: "
													+ feedCall + " not having parameter " + cust_param);
											failCount++;
										} else {
											System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
													+ listOf_criteo_Params.get(i) + " is  matched with " + i
													+ " Occurance of corresponding " + sheetName + " gampad call "
													+ cust_param + " value: " + customParamsList.get(i));
											logStep(i + " Occurance of Criteo call " + cust_param + " value: "
													+ listOf_criteo_Params.get(i) + " is  matched with " + i
													+ " Occurance of corresponding " + sheetName + " gampad call "
													+ cust_param + " value: " + customParamsList.get(i));

										}

									} else {
										if (listOf_criteo_Params.get(i).equalsIgnoreCase(customParamsList.get(i))) {

											System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
													+ listOf_criteo_Params.get(i) + " is matched with " + i
													+ " Occurance of corresponding " + sheetName + " gampad call "
													+ cust_param + " value: " + customParamsList.get(i));
											logStep(i + " Occurance of Criteo call " + cust_param + " value: "
													+ listOf_criteo_Params.get(i) + " is matched with " + i
													+ " Occurance of corresponding " + sheetName + " gampad call "
													+ cust_param + " value: " + customParamsList.get(i));
										} else {
											if (customParamsList.get(i).equalsIgnoreCase("-1")) {
												System.out.println(
														i + " Occurance of corresponding " + sheetName + " gampad call: "
																+ feedCall + " not having parameter " + cust_param);
												logStep(i + " Occurance of corresponding " + sheetName + " gampad call: "
														+ feedCall + " not having parameter " + cust_param);
												failCount++;
											} else {
												System.out.println(i + " Occurance of Criteo call " + cust_param
														+ " value: " + listOf_criteo_Params.get(i) + " is not matched with "
														+ i + " Occurance of corresponding " + sheetName + " gampad call "
														+ cust_param + " value: " + customParamsList.get(i));
												logStep(i + " Occurance of Criteo call " + cust_param + " value: "
														+ listOf_criteo_Params.get(i) + " is not matched with " + i
														+ " Occurance of corresponding " + sheetName + " gampad call "
														+ cust_param + " value: " + customParamsList.get(i));
												failCount++;
											}

										}
									}

								}

							}

						}

					} else {
						for (int i = 0; i < maxIterations / 2; i++) {

							if (listOf_criteo_Params.get(i).equalsIgnoreCase("-1")) {
								if (listOf_criteo_Params.size() == 1) {
									System.out.println(
											"It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
									logStep("It looks that the only Occurance of Criteo Call bidding is not happened..Hence skipping the further validation...inspect the charles response for more details");
								} else {
									System.out.println("It looks that: " + i
											+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
									logStep("It looks that: " + i
											+ " Occurance of Criteo Call bidding is not happened..Hence skipping the current instance validation...inspect the charles response for more details");
								}

							} else {
								if (cust_param.equalsIgnoreCase("displayurl")) {

									if (customParamsList.get(i + 1).equalsIgnoreCase("-1")) {
										System.out.println(i + 1 + " Occurance of corresponding " + sheetName
												+ " gampad call: " + feedCall + " not having parameter " + cust_param);
										logStep(i + 1 + " Occurance of corresponding " + sheetName + " gampad call: "
												+ feedCall + " not having parameter " + cust_param);
										failCount++;
									} else {
										System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is  matched with " + i + 1
												+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
												+ " value: " + customParamsList.get(i + 1));
										logStep(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is  matched with " + i + 1
												+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
												+ " value: " + customParamsList.get(i + 1));

									}

								} else {
									if (listOf_criteo_Params.get(i).equalsIgnoreCase(customParamsList.get(i + 1))) {

										System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is matched with " + i + 1
												+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
												+ " value: " + customParamsList.get(i + 1));
										logStep(i + " Occurance of Criteo call " + cust_param + " value: "
												+ listOf_criteo_Params.get(i) + " is matched with " + i + 1
												+ " Occurance of corresponding " + sheetName + " gampad call " + cust_param
												+ " value: " + customParamsList.get(i + 1));
									} else {
										if (customParamsList.get(i + 1).equalsIgnoreCase("-1")) {
											System.out.println(i + 1 + " Occurance of corresponding " + sheetName
													+ " gampad call: " + feedCall + " not having parameter " + cust_param);
											logStep(i + 1 + " Occurance of corresponding " + sheetName + " gampad call: "
													+ feedCall + " not having parameter " + cust_param);
											failCount++;
										} else {
											System.out.println(i + " Occurance of Criteo call " + cust_param + " value: "
													+ listOf_criteo_Params.get(i) + " is not matched with " + i + 1
													+ " Occurance of corresponding " + sheetName + " gampad call "
													+ cust_param + " value: " + customParamsList.get(i + 1));
											logStep(i + " Occurance of Criteo call " + cust_param + " value: "
													+ listOf_criteo_Params.get(i) + " is not matched with " + i + 1
													+ " Occurance of corresponding " + sheetName + " gampad call "
													+ cust_param + " value: " + customParamsList.get(i + 1));
											failCount++;
										}

									}
								}

							}

						}
					}

				}
			}

			if (failCount > 0) {
				System.out.println("Criteo call " + cust_param + " values  not matched with corresponding " + sheetName
						+ " gampad call " + cust_param + " values");
				logStep("Criteo call " + cust_param + " values  not matched with corresponding " + sheetName
						+ " gampad call " + cust_param + " values");
				Assert.fail("Criteo call " + cust_param + " values  not matched with corresponding " + sheetName
						+ "  gampad call " + cust_param + " values");
			}

		}

	/**
	 * When asset calls are dynamic similar to Integrated Feed Card, then we can use
	 * this method to validate by considering the static parameters from the asset
	 * calls.
	 * 
	 * @param host
	 * @param path1
	 * @param path2
	 * @param path3
	 * @throws Exception
	 */
	public static void verifyAssetCallWithHostandPath(String host, String path1, String path2, String path3)
			throws Exception {
		// String[][] data = read_excel_data.exceldataread(sheetName);
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
		// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@host";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

		// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		// String iuId = null;

		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(host)) {
				iuExists = true;
				break;
			}
		}
		boolean hflag = false;
		boolean pflag = false;
		boolean resflag = false;

		if (iuExists) {
			System.out.println(host + "  asset call is present");
			logStep(host + "  asset call is present");
			outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
				// System.out.println("Total transactions: "+nodeList.getLength());
				if (nodeList.item(p) instanceof Node) {
					Node node = nodeList.item(p);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							// System.out.println("node1 length is: "+nl.getLength());
							Node innernode = nl.item(j);
							if (innernode != null) {
								// System.out.println("Innernode name is: "+innernode.getNodeName());
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											// System.out.println("node2 length is: "+n2.getLength());
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													// System.out.println("Innernode2 element name is:
													// "+eElement.getNodeName());
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			/*
																			 * if (content.contains(host)) { hflag =
																			 * true; // System.out.println("request body
																			 * // found " // + content);
																			 * 
																			 * } else if (content.contains(path)) {
																			 * pflag = true; //
																			 * System.out.println("request body // found
																			 * " // + content); }
																			 */
																			if (content.contains(host)) {
																				hflag = true;
																				// System.out.println("request body
																				// found "+ content);

																			} else if (content.contains(path1)) {
																				// System.out.println("request body
																				// found "+ content);
																				if (content.contains(path2)) {
																					// System.out.println("request body
																					// found "+ content);
																					if (content.contains(path3)) {
																						pflag = true;
																						// System.out.println("request
																						// body found "+ content);
																					}
																				}

																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}

								/*
								 * if (flag) { // System.out.println("Exiting after found true "); //
								 * System.out.println("checking innernode name is: "+innernode.getNodeName());
								 * if (innernode.getNodeName().equals("response")) { //
								 * System.out.println(innernode.getNodeName()); if (innernode.hasChildNodes()) {
								 * NodeList n2 = innernode.getChildNodes(); for (int k = 0; k < n2.getLength();
								 * k++) { Node innernode2 = n2.item(k); if (innernode2 != null) { if
								 * (innernode2.getNodeType() == Node.ELEMENT_NODE) { Element eElement =
								 * (Element) innernode2; if (eElement.getNodeName().equals("body")) { String
								 * content = eElement.getTextContent(); //
								 * System.out.println("response body "+content); if
								 * (content.contains(data[13][1])) { resflag = true; break
								 * outerloop;
								 * 
								 * } } } } } } }
								 * 
								 * }
								 */
								if (hflag && pflag) {
									resflag = true;
									break outerloop;
								}
							}
						}
					}
				}
				// flag = false;
			}

		} else {
			System.out.println(path3 + " asset call is not present");
			logStep(path3 + " ad call is not present");

		}

		if (resflag) {
			System.out.println(path3 + " Asset Call is present");
			logStep(path3 + " Asset Call is present");
		} else {
			System.out.println(path3 + " Asset Call is not present");
			logStep(path3 + " Asset Call is not present");
			Assert.fail(path3 + " Asset Call is not present in Charles session");
		}

		// Get Pubad call from

		/*
		 * if (resflag) { System.out.println(host + path
		 * +" call is present in Charles session"); logStep(host + path
		 * +" call is present in Charles session"); return resflag;
		 * 
		 * } else { System.out .println(host + path
		 * +" call is not present in Charles session"); logStep(host + path
		 * +" call is not present in Charles session"); return resflag;
		 * //Assert.fail(host + path +" call is not present in Charles session");
		 * 
		 * }
		 */

	}
	
	public static boolean verify_criteo_request_for_given_placementId_inJsonRequestBody(String placementId,
			String apiData) throws Exception {

		// Functions.Read_Turbo_api("Cust_Param", readSheet);

		JSONParser parser = new JSONParser();
		// System.out.println("adreq1 is : "+adreq1.toString());
		Object obj = parser.parse(new String(apiData));
		// System.out.println("obj : "+obj);
		JSONObject jsonObject = (JSONObject) obj;
		String ApiParamValue = "";
		String JsonParam = null;

		JSONObject mainTag = null;
		JSONArray eleArray = null;
		boolean matchFound = false;

		try {
			JsonParam = "slots".trim();
			eleArray = (JSONArray) jsonObject.get(JsonParam);
			// System.out.println(eleArray);
			try {

				ArrayList<String> Ingredients_names = new ArrayList<>();
				for (int i = 0; i < eleArray.size(); i++) {

					String arrayElement = String.valueOf(eleArray.get(i));

					Ingredients_names.add(arrayElement);
					obj = parser.parse(new String(arrayElement));
					jsonObject = (JSONObject) obj;
					mainTag = (JSONObject) obj;

					try {
						String cApiParamValue = String.valueOf(mainTag.get("placementId"));
						if (cApiParamValue.equalsIgnoreCase(placementId)) {
							
							matchFound = true;

						} else {
							// System.out.println("... noticed");
							continue;
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}

				}
			} catch (Exception e) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return matchFound;
	}
	

	
	
	public static boolean isInterstitialCall_hasResponse(String sheetName) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		File fXmlFile = new File(outfile.getName());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);
// Getting the transaction element by passing xpath expression
		NodeList nodeList = doc.getElementsByTagName("transaction");
		String xpathExpression = "charles-session/transaction/@query";
		List<String> getQueryList = evaluateXPath(doc, xpathExpression);

// Getting custom_params amzn_b values
		List<String> customParamsList = new ArrayList<String>();

		String iuId = null;

		// String iuId =
		// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
		iuId = data[17][1];

		boolean iuExists = false;
		for (String qry : getQueryList) {
			if (qry.contains(iuId)) {
				iuExists = true;
				break;
			}
		}
		boolean flag = false;
		boolean resflag = false;
		if (iuExists) {
			System.out.println(iuId + " ad call is present");
			logStep(iuId + " ad call is present");
			outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
				// System.out.println("Total transactions: "+nodeList.getLength());
				if (nodeList.item(p) instanceof Node) {
					Node node = nodeList.item(p);
					if (node.hasChildNodes()) {
						NodeList nl = node.getChildNodes();
						for (int j = 0; j < nl.getLength(); j++) {
							// System.out.println("node1 length is: "+nl.getLength());
							Node innernode = nl.item(j);
							if (innernode != null) {
								// System.out.println("Innernode name is: "+innernode.getNodeName());
								if (innernode.getNodeName().equals("request")) {
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											// System.out.println("node2 length is: "+n2.getLength());
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													// System.out.println("Innernode2 element name is:
													// "+eElement.getNodeName());
													if (eElement.getNodeName().equals("headers")) {
														if (innernode2.hasChildNodes()) {
															NodeList n3 = innernode2.getChildNodes();
															for (int q = 0; q < n3.getLength(); q++) {
																// System.out.println("node3 length is:
																// "+n3.getLength());
																Node innernode3 = n3.item(q);
																if (innernode3 != null) {
																	// System.out.println("Innernode3 name is:
																	// "+innernode3.getNodeName());
																	if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																		Element eElement1 = (Element) innernode3;
																		// System.out.println("Innernode3 element name
																		// is: "+eElement1.getNodeName());
																		if (eElement1.getNodeName().equals("header")) {
																			String content = eElement1.getTextContent();
																			// System.out.println("request body
																			// "+content);

																			if (content.contains(iuId)) {
																				flag = true;
																				// System.out.println("request body
																				// "+content);
																				// istofRequestBodies.add(content);
																				// System.out.println("request body
																				// found "+content);
																				// break;
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}

								if (flag) {
									// System.out.println("Exiting after found true ");
									// System.out.println("checking innernode name is: "+innernode.getNodeName());
									if (innernode.getNodeName().equals("response")) {
										// System.out.println(innernode.getNodeName());
										if (innernode.hasChildNodes()) {
											NodeList n2 = innernode.getChildNodes();
											for (int k = 0; k < n2.getLength(); k++) {
												Node innernode2 = n2.item(k);
												if (innernode2 != null) {
													if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
														Element eElement = (Element) innernode2;
														if (eElement.getNodeName().equals("body")) {
															String content = eElement.getTextContent();
															// System.out.println("response body "+content);
															if (content.contains(data[13][1])) {
																resflag = true;
																break outerloop;

															}
														}
													}
												}
											}
										}
									}

								}
								// break;
							}
						}
					}
				}
			flag = false;
			}

		} else {
			System.out.println(iuId + " ad call is not present");
			logStep(iuId + " ad call is not present");

		}

		// return flag;

		// Get Pubad call from

		if (resflag) {
			System.out.println("Interstitial call has response, hence Interstitial Ad to be displayed");
			logStep("Interstitial call has response, hence Interstitial Ad to be displayed");
			return resflag;
		} else {
			System.out
					.println("Interstitial call doesnt have response, hence Interstitial Ad not to be displayed");
			logStep("Interstitial call doesnt have response, hence Interstitial Ad not to be displayed");
			return resflag;
		}

	}

	public static void validate_Amazon_aax_call_parameter(String sheetName,
			String cust_param, String expected) throws Exception {
		String[][] data = read_excel_data.exceldataread(sheetName);
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		String host = data[2][1];
		String path =data[3][1];

		boolean flag = verifyAPICalWithHostandPath(host, path);
		if (flag) {
			System.out.println(host + path + " call is present in Charles session");
			logStep(host + path + " call is present in Charles session");

			String actual = get_param_value_from_APIRequest(host, path, cust_param);

			if (actual.equalsIgnoreCase(expected)) {
				System.out.println("Custom Parameter :" + cust_param + " value: " + actual
						+ " is matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + actual + " is matched with the expected value "
						+ expected);
			} else {
				System.out.println("Custom Parameter :" + cust_param + " value: " + actual
						+ " is not matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + actual
						+ " is not matched with the expected value " + expected);
				Assert.fail("Custom Parameter :" + cust_param + " value: " + actual
						+ " is not matched with the expected value " + expected);
			}

		} else {
			System.out.println(host + path + " call is not present in Charles session, hence Custom Parameter: "
					+ cust_param + " validation skipped");
			logStep(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
					+ " validation skipped");

			Assert.fail(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
					+ " validation skipped");

		}

	}
	

		

	

public static void validate_Noncustom_param_val_of_gampad(String sheetName, String cust_param,
		String expected) throws Exception {
	
	String[][] data = read_excel_data.exceldataread(sheetName);
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	boolean adCallFound = false;

	// Read the content form file
	File fXmlFile = new File(CharlesFunctions.outfile.getName());

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	dbFactory.setValidating(false);
	dbFactory.setNamespaceAware(true);
	dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
	// dbFactory.setNamespaceAware(true);
	dbFactory.setFeature("http://xml.org/sax/features/validation", false);
	dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

	Document doc = dBuilder.parse(fXmlFile);
	// Getting the transaction element by passing xpath expression
	NodeList nodeList = doc.getElementsByTagName("transaction");
	String xpathExpression = "charles-session/transaction/@query";
	List<String> getQueryList = evaluateXPath(doc, xpathExpression);

	// Getting custom_params amzn_b values
	List<String> customParamsList = new ArrayList<String>();

	String iuId = null;
	// "iu=%2F7646%2Fapp_iphone_us%2Fdb_display%2Fhome_screen%2Ftoday";
	if (sheetName.equalsIgnoreCase("PreRollVideo")) {
		iuId = videoIUValue;
		System.out.println(iuId);
	} 
	else {
		iuId = data[11][1];
	}
	String tempCustmParam = null;
	for (String qry : getQueryList) {
		if (qry.contains(iuId)) {
			adCallFound = true;
			tempCustmParam = getNonCustomParamBy_iu_value(qry, cust_param);
			//if (!"".equals(tempCustmParam))
			// customParamsList.add(getCustomParamsBy_iu_value(qry));
			break;
		}

	}

	if (!adCallFound) {
		System.out.println("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: "
				+ cust_param + " validation skipped");
		logStep("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
				+ " validation skipped");
		Assert.fail("Ad Call :" + iuId + " not found in charles session, hence Custom Parameter: " + cust_param
				+ " validation skipped");
	} else if (adCallFound && !tempCustmParam.isEmpty()) {
		System.out.println(cust_param + " value of from gampad call  of : " + iuId + " is " + tempCustmParam);
		if (expected.equalsIgnoreCase("NotNull")) {
			if (!tempCustmParam.equalsIgnoreCase("nl")) {
				System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is matched with the expected value " + expected);
			} else {
				System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is not matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is not matched with the expected value " + expected);
				Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is not matched with the expected value " + expected);
			}
		} else {
			if (tempCustmParam.equalsIgnoreCase(expected)) {
				System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is matched with the expected value " + expected);
			} else {
				System.out.println("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is not matched with the expected value " + expected);
				logStep("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is not matched with the expected value " + expected);
				Assert.fail("Custom Parameter :" + cust_param + " value: " + tempCustmParam
						+ " is not matched with the expected value " + expected);
			}
		}

	} else if (tempCustmParam == null || tempCustmParam.isEmpty()) {
		System.out.println("Custom parameter :" + cust_param
				+ " not found/no value in ad call, hence Custom Parameter: " + cust_param + " validation skipped");
		logStep("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
				+ cust_param + " validation skipped");
		Assert.fail("Custom parameter :" + cust_param + " not found/no value in ad call, hence Custom Parameter: "
				+ cust_param + " validation skipped");
	}

}
public static void validate_Criteo_SDK_config_app_call_parameter(String sheetName,
		String cust_param, String expected) throws Exception {
String[][] data = read_excel_data.exceldataread("Criteo");
	
	//readExcelValues.excelValues( sheetName);
	String host = data[2][1];
	String path = data[4][1];
	/*readExcelValues.excelValues(excelName, sheetName);
	String host = readExcelValues.data[2][Cap];
	String path = readExcelValues.data[4][Cap];*/

	boolean flag = verifyAPICalWithHostandPath(host, path);
	if (flag) {
		System.out.println(host + path + " call is present in Charles session");
		logStep(host + path + " call is present in Charles session");

		String actual = get_param_value_from_APIRequest(host, path, cust_param);

		if (actual.equalsIgnoreCase(expected)) {
			System.out.println("Custom Parameter :" + cust_param + " value: " + actual
					+ " is matched with the expected value " + expected);
			logStep("Custom Parameter :" + cust_param + " value: " + actual + " is matched with the expected value "
					+ expected);
		} else {
			System.out.println("Custom Parameter :" + cust_param + " value: " + actual
					+ " is not matched with the expected value " + expected);
			logStep("Custom Parameter :" + cust_param + " value: " + actual
					+ " is not matched with the expected value " + expected);
			Assert.fail("Custom Parameter :" + cust_param + " value: " + actual
					+ " is not matched with the expected value " + expected);
		}

	} else {
		System.out.println(host + path + " call is not present in Charles session, hence Custom Parameter: "
				+ cust_param + " validation skipped");
		logStep(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
				+ " validation skipped");

		Assert.fail(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
				+ " validation skipped");

	}

}
	
	public static void verifyCriteo_inapp_v2_Call_ReponseStatusCode(String excelName, String sheetName, String responseParameter, String responseValue) throws Exception {
String[][] data = read_excel_data.exceldataread("Criteo");
	
	//readExcelValues.excelValues( sheetName);
	String host = data[2][1];
	String path = data[4][1];

	boolean flag = verifyAPICalWithHostandPath(host, path);
	
	if (flag) {
		System.out.println(host + path + " call is present in Charles session");
		logStep(host + path + " call is present in Charles session");
		System.out.println(host + path + " :API Call Verification is successful");
		logStep(host + path + " :API Call Verification is successful");
		boolean resflag = verifyAPICallResponseHeaderParameter(host, path, responseParameter, responseValue);
		if (resflag) {
			System.out.println(host + path + " call response contains response code : "+responseValue);
			logStep(host + path + " call response contains response code : "+responseValue);
			System.out.println(host + path + " :API Call response code validation is successful");
			logStep(host + path + " :API Call response code validation is successful");
		} else {
			System.out.println(host + path + " call response not contains response code : "+responseValue);
			logStep(host + path + " call response not contains response code : "+responseValue);
			System.out.println(host + path + " :API Call response code validation is failed");
			logStep(host + path + " :API Call response code validation is failed");
			Assert.fail(host + path + " :API Call response code validation is failed");
		}
	} else {
		System.out.println(host + path + " call is not present in Charles session");
		logStep(host + path + " call is not present in Charles session");
		System.out.println(host + path + " :API Call response code validation is failed");
		logStep(host + path + " :API Call response code validation is failed");
		Assert.fail(host + path + " call is not present in Charles session, hence response code validation is failed");

	}
}

public static boolean verifyAPICallResponseHeaderParameter(String host, String path, String responseParameter, String responseValue) throws Exception {
	// readExcelValues.excelValues(excelName, sheetName);
	File fXmlFile = new File(CharlesFunctions.outfile.getName());

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	dbFactory.setValidating(false);
	dbFactory.setNamespaceAware(true);
	dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
	// dbFactory.setNamespaceAware(true);
	dbFactory.setFeature("http://xml.org/sax/features/validation", false);
	dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

	Document doc = dBuilder.parse(fXmlFile);
//Getting the transaction element by passing xpath expression
	NodeList nodeList = doc.getElementsByTagName("transaction");
	String xpathExpression = "charles-session/transaction/@host";
	List<String> getQueryList = evaluateXPath(doc, xpathExpression);

//Getting custom_params amzn_b values
	List<String> customParamsList = new ArrayList<String>();

	// String iuId = null;

	boolean iuExists = false;
	for (String qry : getQueryList) {
		if (qry.contains(host)) {
			iuExists = true;
			break;
		}
	}
	boolean flag = false;
	boolean hflag = false;
	boolean pflag = false;
	boolean resflag = false;
	boolean isresponseStatusCheckPass = false;

	if (iuExists) {
		System.out.println(host + "  call is present");
		logStep(host + "  call is present");
		outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
			// System.out.println("Total transactions: "+nodeList.getLength());
			if (nodeList.item(p) instanceof Node) {
				Node node = nodeList.item(p);
				if (node.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					for (int j = 0; j < nl.getLength(); j++) {
						// System.out.println("node1 length is: "+nl.getLength());
						Node innernode = nl.item(j);
						if (innernode != null) {
							// System.out.println("Innernode name is: "+innernode.getNodeName());
							if (innernode.getNodeName().equals("request")) {
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										// System.out.println("node2 length is: "+n2.getLength());
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												// System.out.println("Innernode2 element name is:
												// "+eElement.getNodeName());
												if (eElement.getNodeName().equals("headers")) {
													if (innernode2.hasChildNodes()) {
														NodeList n3 = innernode2.getChildNodes();
														for (int q = 0; q < n3.getLength(); q++) {
															// System.out.println("node3 length is:
															// "+n3.getLength());
															Node innernode3 = n3.item(q);
															if (innernode3 != null) {
																// System.out.println("Innernode3 name is:
																// "+innernode3.getNodeName());
																if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																	Element eElement1 = (Element) innernode3;
																	// System.out.println("Innernode3 element name
																	// is: "+eElement1.getNodeName());
																	if (eElement1.getNodeName().equals("header")) {
																		String content = eElement1.getTextContent();
																	   // System.out.println("request body "+content);

																		if (content.contains(host)) {
																			hflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);

																		} else if (content.contains(path)) {
																			pflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);
																		}
																		
																	}

																	// this condition especially for android since
																	// its file has path value under first-line
																	// element
																	if (eElement1.getNodeName()
																			.equals("first-line")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		if (content.contains(path)) {
																			pflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);
																		}
																	}
																	if (pflag && hflag) {
																		
																		flag = true;
																	} else {
																		flag = false;
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}

							
							if (flag) {
								// System.out.println("Exiting after found true "); //
								//System.out.println("checking innernode name is: " + innernode.getNodeName());
								if (innernode.getNodeName().equals("response")) {
									 //System.out.println(innernode.getNodeName());
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("headers")) {
														String contents = eElement.getTextContent();
														// System.out.println("response body "+contents);
														if (innernode2.hasChildNodes()) {
																NodeList n3 = innernode2.getChildNodes();
																for (int q = 0; q < n3.getLength(); q++) {
																	// System.out.println("node3 length is:
																	// "+n3.getLength());
																	Node innernode3 = n3.item(q);
																	if (innernode3 != null) {
																		// System.out.println("Innernode3 name is:
																		// "+innernode3.getNodeName());
																		if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																			Element eElement1 = (Element) innernode3;
																			// System.out.println("Innernode3 element name
																			// is: "+eElement1.getNodeName());
																			if (eElement1.getNodeName().equals("header")) {
																				String content = eElement1.getTextContent();
																				//System.out.println("response body "+content);

																				if (content.contains(responseParameter) && content.contains(responseValue)) {
																					isresponseStatusCheckPass = true;
																					// System.out.println("request body
																					// found "
																					// + content);
																					break outerloop;

																				} 
																			}

																			// this condition especially for android since
																			// its file has path value under first-line
																			// element
																			if (eElement1.getNodeName()
																					.equals("first-line")) {
																				String content = eElement1.getTextContent();
																				// System.out.println("request body
																				// "+content); "HTTP/1.1 200 OK"

																				if (content.contains(responseValue)) {
																					isresponseStatusCheckPass = true;
																					// System.out.println("request body
																					// found "
																					// + content);
																					break outerloop;
																				}
																			}
																		}
																	}
																}
															}		
														}
												}
											}
										}
									}
								}

							}
							 
							/*
							 * if (hflag && pflag) { resflag = true; break outerloop; }
							 */
						}
					}
				}
			}
			 flag = false;
			 hflag = false;
			 pflag = false;
		}

	} else {
		System.out.println(host + " ad call is not present");
		logStep(host + " ad call is not present");

	}

	return isresponseStatusCheckPass;


}
public static void verifyCriteo_config_app_Call_ReponseStatusCode(String excelName, String sheetName, String responseParameter, String responseValue) throws Exception {
String[][] data = read_excel_data.exceldataread("Criteo");
	
	//readExcelValues.excelValues( sheetName);
	String host = data[2][1];
	String path = data[4][1];
	boolean flag = verifyAPICalWithHostandPath(host, path);

	
	if (flag) {
		System.out.println(host + path + " call is present in Charles session");
		logStep(host + path + " call is present in Charles session");
		System.out.println(host + path + " :API Call Verification is successful");
		logStep(host + path + " :API Call Verification is successful");
		boolean resflag = verifyAPICallResponseHeaderParameter(host, path, responseParameter, responseValue);
		if (resflag) {
			System.out.println(host + path + " call response contains response code : "+responseValue);
			logStep(host + path + " call response contains response code : "+responseValue);
			System.out.println(host + path + " :API Call response code validation is successful");
			logStep(host + path + " :API Call response code validation is successful");
		} else {
			System.out.println(host + path + " call response not contains response code : "+responseValue);
			logStep(host + path + " call response not contains response code : "+responseValue);
			System.out.println(host + path + " :API Call response code validation is failed");
			logStep(host + path + " :API Call response code validation is failed");
			Assert.fail(host + path + " :API Call response code validation is failed");
		}
	} else {
		System.out.println(host + path + " call is not present in Charles session");
		logStep(host + path + " call is not present in Charles session");
		System.out.println(host + path + " :API Call response code validation is failed");
		logStep(host + path + " :API Call response code validation is failed");
		Assert.fail(host + path + " call is not present in Charles session, hence response code validation is failed");

	}
}

public static void validate_Criteo_SDK_config_app_call_response_parameter(String excelName, String sheetName,
		String cust_param, String expected) throws Exception {
String[][] data = read_excel_data.exceldataread("Criteo");
	
	//readExcelValues.excelValues( sheetName);
	String host = data[2][1];
	String path = data[4][1];
	boolean flag = verifyAPICalWithHostandPath(host, path);


	if (flag) {
		System.out.println(host + path + " call is present in Charles session");
		logStep(host + path + " call is present in Charles session");

		String actual = get_param_value_from_APIResponse(host, path, cust_param);

		if (actual.equalsIgnoreCase(expected)) {
			System.out.println("Custom Parameter :" + cust_param + " value: " + actual
					+ " is matched with the expected value " + expected);
			logStep("Custom Parameter :" + cust_param + " value: " + actual + " is matched with the expected value "
					+ expected);
			System.out.println("Criteo parameter: " + cust_param + " validation is successful");
			logStep("Criteo parameter: " + cust_param + " validation is successful");
		} else {
			System.out.println("Custom Parameter :" + cust_param + " value: " + actual
					+ " is not matched with the expected value " + expected);
			logStep("Custom Parameter :" + cust_param + " value: " + actual
					+ " is not matched with the expected value " + expected);
			System.out.println("Criteo parameter: " + cust_param + " validation is failed");
			logStep("Criteo parameter: " + cust_param + " validation is failed");
			Assert.fail("Custom Parameter :" + cust_param + " value: " + actual
					+ " is not matched with the expected value " + expected);
		}

	} else {
		System.out.println(host + path + " call is not present in Charles session, hence Custom Parameter: "
				+ cust_param + " validation skipped");
		logStep(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
				+ " validation skipped");
		System.out.println("Criteo parameter: " + cust_param + " validation is failed");
		logStep("Criteo parameter: " + cust_param + " validation is failed");
		Assert.fail(host + path + " call is not present in Charles session, hence Custom Parameter: " + cust_param
				+ " validation skipped");

	}

}

public static String get_param_value_from_APIResponse(String host, String path, String cust_param) throws Exception {
	// readExcelValues.excelValues(excelName, sheetName);
	File fXmlFile = new File(CharlesFunctions.outfile.getName());

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	dbFactory.setValidating(false);
	dbFactory.setNamespaceAware(true);
	dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
	// dbFactory.setNamespaceAware(true);
	dbFactory.setFeature("http://xml.org/sax/features/validation", false);
	dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

	Document doc = dBuilder.parse(fXmlFile);
	// Getting the transaction element by passing xpath expression
	NodeList nodeList = doc.getElementsByTagName("transaction");
	String xpathExpression = "charles-session/transaction/@host";
	List<String> getQueryList = evaluateXPath(doc, xpathExpression);

	// Getting custom_params amzn_b values
	List<String> customParamsList = new ArrayList<String>();

	// String iuId = null;

	boolean iuExists = false;
	for (String qry : getQueryList) {
		if (qry.contains(host)) {
			iuExists = true;
			break;
		}
	}
	boolean hflag = false;
	boolean pflag = false;
	boolean resflag = false;
	boolean flag = false;
	String ApiParamValue = null;

	if (iuExists) {
		System.out.println(host + "  call is present");
		logStep(host + "  call is present");
		outerloop: for (int p = 0; p < nodeList.getLength(); p++) {
			// System.out.println("Total transactions: "+nodeList.getLength());
			if (nodeList.item(p) instanceof Node) {
				Node node = nodeList.item(p);
				if (node.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					for (int j = 0; j < nl.getLength(); j++) {
						// System.out.println("node1 length is: "+nl.getLength());
						Node innernode = nl.item(j);
						if (innernode != null) {
							// System.out.println("Innernode name is: "+innernode.getNodeName());
							if (innernode.getNodeName().equals("request")) {
								if (innernode.hasChildNodes()) {
									NodeList n2 = innernode.getChildNodes();
									for (int k = 0; k < n2.getLength(); k++) {
										// System.out.println("node2 length is: "+n2.getLength());
										Node innernode2 = n2.item(k);
										if (innernode2 != null) {
											// System.out.println("Innernode2 name is: "+innernode2.getNodeName());
											if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
												Element eElement = (Element) innernode2;
												// System.out.println("Innernode2 element name is:
												// "+eElement.getNodeName());
												if (eElement.getNodeName().equals("headers")) {
													if (innernode2.hasChildNodes()) {
														NodeList n3 = innernode2.getChildNodes();
														for (int q = 0; q < n3.getLength(); q++) {
															// System.out.println("node3 length is:
															// "+n3.getLength());
															Node innernode3 = n3.item(q);
															if (innernode3 != null) {
																// System.out.println("Innernode3 name is:
																// "+innernode3.getNodeName());
																if (innernode3.getNodeType() == Node.ELEMENT_NODE) {
																	Element eElement1 = (Element) innernode3;
																	// System.out.println("Innernode3 element name
																	// is: "+eElement1.getNodeName());
																	if (eElement1.getNodeName().equals("header")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		if (content.contains(host)) {
																			hflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);

																		} else if (content.contains(path)) {
																			pflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);
																		}
																	}
																	// this condition especially for android since
																	// its file has path value under first-line
																	// element
																	if (eElement1.getNodeName()
																			.equals("first-line")) {
																		String content = eElement1.getTextContent();
																		// System.out.println("request body
																		// "+content);

																		if (content.contains(path)) {
																			pflag = true;
																			// System.out.println("request body
																			// found "
																			// + content);
																		}
																	}
																}
															}
														}
													}
												}
												if (hflag && pflag) {
												/*	if (eElement.getNodeName().equals("body")) {
														String scontent = eElement.getTextContent();
														if (scontent.contains(cust_param)) {
															// System.out.println("request body " + scontent);
															ApiParamValue = get_Param_Value_inJsonBody(scontent,
																	cust_param);
															break outerloop;

														}

													}*/
													flag = true;
												}

											}
										}
									}
								}
							}
							if (flag) {
								// System.out.println("Exiting after found true "); //
								//System.out.println("checking innernode name is: " + innernode.getNodeName());
								if (innernode.getNodeName().equals("response")) {
									// System.out.println(innernode.getNodeName());
									if (innernode.hasChildNodes()) {
										NodeList n2 = innernode.getChildNodes();
										for (int k = 0; k < n2.getLength(); k++) {
											Node innernode2 = n2.item(k);
											if (innernode2 != null) {
												if (innernode2.getNodeType() == Node.ELEMENT_NODE) {
													Element eElement = (Element) innernode2;
													if (eElement.getNodeName().equals("body")) {
														String content = eElement.getTextContent();
														// System.out.println("response body "+content);
														if (content.contains(cust_param)) {
															ApiParamValue = get_Param_Value_inJsonBody(content,
																	cust_param);
															break outerloop;

														}
													}
												}
											}
										}
									}
								}

							}
							/*
							 * if (hflag && pflag) { resflag = true; break outerloop; }
							 */
						}
					}
				}
			}
			 flag = false;
			 hflag = false;
			 pflag = false;
		}

	} else {
		System.out.println(host + " ad call is not present");
		logStep(host + " ad call is not present");

	}

	// return resflag;
	// System.out.println("Parameter value obtined from criteo request is :" +
	// ApiParamValue);
	return ApiParamValue;

}

}
