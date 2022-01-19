package twc.Automation.HandleWithApp;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileBy.ByAccessibilityId;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import twc.Automation.Driver.Drivers;
import twc.Automation.General.DeviceStatus;
import twc.Automation.General.Functions;
import twc.Automation.General.loginModule;
import twc.Automation.HandleWithAppium.AppiumFunctions;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;
import twc.Automation.RetryAnalyzer.RetryAnalyzer;

public class AppFunctions extends Drivers{

	static int startY;
	static int endY;
	public static String TestName=null;
	public static  String homelocation;

	 public static String apkVersion=null;
	
	public static void verifyCall(String excel_sheet_name, String skiCallName) throws Exception{

		String skiResortsCallURL = null;
		String skiResortsCallParam = null ;

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);

		if(skiCallName.equalsIgnoreCase("skiContentAd")){
			skiResortsCallURL = exceldata[14][Cap];
			skiResortsCallParam = exceldata[13][Cap];
			System.out.println("=====Checking for ski Content Ad call====");

		}else if(skiCallName.equalsIgnoreCase("skiSpotlightAd")){
			skiResortsCallURL = exceldata[33][Cap];
			skiResortsCallParam = exceldata[34][Cap];
			System.out.println("====Checking for ski Spotlight Ad call====");
		}else if(skiCallName.equalsIgnoreCase("skiLargeAd")){

			skiResortsCallURL = exceldata[35][Cap];
			skiResortsCallParam = exceldata[36][Cap];
			System.out.println("====Checking for ski LargeAd call====");
		}



		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();

		try {
			String Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(exceldata[15][Cap]));
			String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[16][Cap]));

			String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[17][Cap])+(exceldata[17][Cap].length()),required_info.indexOf(exceldata[18][Cap]));
			String callData = expected_data.toString();



			if(callData.contains(skiResortsCallURL)){
				if(callData.contains(skiResortsCallParam) ){
					System.out.println("Call is generated on "+exceldata[1][Cap]+" Page====");
					logStep("Call is generated on "+exceldata[1][Cap]+" Page");
					RetryAnalyzer.count=0;
				}
				else
				{
					System.out.println("Call is not Generated on "+exceldata[1][Cap]);
					logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
					Assert.fail("Call is not Generated on "+exceldata[1][Cap]);

				}
			}
			else{

				System.out.println("Call is Not Generated on "+exceldata[1][Cap]);
				logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
				Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
			}


		} catch (Exception e) {
			System.out.println( "Call is not Generated on "+exceldata[1][Cap]);
			logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
			Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
		}		

	}

	//Adzone Validation
	public static void Adzone_Validations() throws Exception{

		System.out.println("================= AddZone Test Case Started =========================");
		Map<String, String> pubads_call_results=null;
		AppFunctions.TestName = "Adzone";
		//launch the app in fresh launch mode 
		CharlesFunctions.ClearSessions();
		//start the charles
		//CharlesFunctions.startSessionBrowserData();
		//AppiumFunctions.LaunchApp();
		Ad.quit();
		AppiumFunctions.LaunchAppWithFullReset("true");
		CharlesFunctions.ExportSession();	
		Thread.sleep(5000);
		//Reading Adone from api call
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		//System.out.println(sb);
		Thread.sleep(5000);
		
		//delete the specific folder
		CharlesFunctions.startSessionBrowserData();
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();		
		String[][] exceldata = read_excel_data.exceldataread("Adzones");
		String Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(exceldata[1][Cap]));
		String required_info=Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().lastIndexOf(exceldata[2][Cap]));
		String expected_data=null;
		try{
			expected_data = required_info.toString().substring(required_info.indexOf(exceldata[3][Cap]),required_info.indexOf(exceldata[5][Cap]));
		}catch(Exception e){
			expected_data = required_info.toString().substring(required_info.indexOf(exceldata[3][Cap]),required_info.indexOf("]]"+exceldata[5][Cap]));
		}
		//System.out.println("required_info "+required_info );
		//String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[3][Cap]),required_info.indexOf(exceldata[5][Cap]));
		//expected_data=expected_data.replace("<body><![CDATA[ ", "");
		expected_data= expected_data.toString().replaceAll("]]", "");
		expected_data= expected_data.toString().replaceAll(exceldata[8][Cap], "=");
		expected_data= expected_data.toString().replaceAll(exceldata[9][Cap], "&");
		expected_data= expected_data.toString().replaceAll(exceldata[10][Cap], ",");
		expected_data= expected_data.toString().trim();
		//System.out.println("Expected Data is : "+expected_data.toString());
		//System.out.println("expected Data is : "+expected_data);
		//expected_data= expected_data.toString().replaceAll("%22%7D%5D%7D", "");
		//convert to json object 
		List<String> fgeo_res = new ArrayList<String>();
		List<String> Adzone_res = new ArrayList<String>();
		List<String> faud_res = new ArrayList<String>();
		String req = expected_data;
		//		String expectedValues = expected_data.toString();
		//		String validateSecondValues = exceldata[12][Cap];
		//		String[] validate_Second_Values = validateSecondValues.split(",");
		//		String validateValues = exceldata[11][Cap];
		//		String[] validate_Values = validateValues.split(",");
		//		JSONParser parser = new JSONParser();
		//		
		//		Object obj = parser.parse(expected_data.toString().trim());
		//		JSONObject jsonObject = (JSONObject) obj;
		//		JSONArray fgeoval = (JSONArray) jsonObject.get(validate_Values[0]);


		String Adz;

		String []Adzonearray = req.split("adzone");
		int AdzoneSize = Adzonearray.length;
		int i=0;
		for(String Azone:Adzonearray){

			Adz = Azone.toString().substring(Azone.indexOf(":"),Azone.indexOf(","));
			if(i>0){
				Adz=Adz.replaceAll("\"", "");
				Adz=Adz.replaceAll("/", "");
				Adz=Adz.replaceAll(": ", "");
				System.out.println("Adz is : "+Adz.toString());
				fgeo_res.add(Adz.toString().trim());
				//System.out.println("adzone value is : "+Adz.toString());

			}
			i=i+1;
			Adzonearray = Azone.split("adzone");
		}
		System.out.println("Size of fgeoVal : "+ fgeo_res.size());



		AppFunctions.scrollInToView("News");

		Thread.sleep(2000);
		CharlesFunctions.ClearSessions();
		AppFunctions.verify_adpresent_onextended_page("News");
		//			WebDriverWait wait4 = new WebDriverWait(Ad, 30);		
		//			 wait4.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]")));	
		//			//module2.click();
		//AppFunctions.verify_adpresent_onextended_page("News");
		Thread.sleep(5000);
		//CharlesFunctions.ClearSessions();
		//waiting for news atricle element
		//WebDriverWait wait4 = new WebDriverWait(Ad, 30);	
		CharlesFunctions.ExportSessions();
		//			try{
		//		 wait4.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]")));
		//		 Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]").click();
		//			}
		//			catch(Exception e){
		//				wait4.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]")));
		//				 Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]").click();
		//			}
		//clicking  the news article first time 

		// Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]").click();
		Thread.sleep(2000);


		//Read pubad call for IU value from News mail ad
		pubads_call_results=Functions.readAdzone__Pub_Ad_Call_Data("Adzones");
		//CharlesFunctions.clearSessionBrowserData();
		CharlesFunctions.startSessionBrowserData();
		//Cleasr session and tapping on Video and Download session for Video add

		//clicking the news article video for playing

		//clicking the news article video for playing
		try{
			CharlesFunctions.ClearSessions();
			Thread.sleep(5000);
			Ad.findElementById("com.weather.Weather:id/media_player_view").click();	
			CharlesFunctions.ExportSessions();
			Functions.Validation="Video_iu";
			pubads_call_results=Functions.readAdzone__Pub_Ad_Call_Data("Adzones");

		}
		catch(Exception e){
			Functions.Validation="Video_epmty";
			System.out.println("Video not present on the news Module");
		}

//		for(int j=0;j< Adzone_res.size();j++){
//
////			JSONObject filter = (JSONObject) fgeoval.get(j);
////			if(filter.containsKey(validate_Second_Values[0])){
////				fgeo_res.add(filter.get(validate_Second_Values[0]).toString());
////				//System.out.println("AdZone Values is : "+ filter.get(validate_Second_Values[0]).toString());
////			}	
//		}
		System.out.println("fgeo_res Values is : "+fgeo_res.toString());
//		String AdzoneValue;
//		String[] Adzones;
//		String [] AdzoneName;
//		for(String AdzoneValues : fgeo_res){
//
//			Adzones= AdzoneValues.split(",");
//			for(String Adzone:Adzones){
//				if(Adzone.toString().contains("adzone")){
//					//Adzone= Adzone.replaceAll("\\W", "");
//					AdzoneName=Adzone.split(":");
//					for(String AdzoneParam:AdzoneName){
//						if(AdzoneParam.contains("adzone")){
//
//						}else{
//							AdzoneParam= AdzoneParam.replaceAll("\\W", "");
//							//System.out.println("Adzone Parameter is : "+AdzoneParam.toString().trim());
//							//Map<String, String> pubads_call_results = read_Pub_Ad_Call_Data(excel_sheet_name);
//							Adzone_res.add(AdzoneParam.toString().trim());
//							//Map<String, String> pubads_call_results=Functions.read_Pub_Ad_Call_Data("Adzones");
//							//System.out.println(pubads_call_results+"pubads_call_results");
//
//						}
//					}
//
//				}
//			}
//		}
		
		//Adzone_res=fgeo_res;

		System.out.println("AdZone Values is : "+fgeo_res.toString());
		if(fgeo_res.isEmpty()){
			fgeo_res.add("displaydetailsarticles");
		}
		int adzonelatvalue = fgeo_res.size();
		System.out.println("adzonelatvalue :"+adzonelatvalue);
		int k=0;

		for(String AdzName:fgeo_res){
			if(Functions.Validation.equals("Video_epmty")){
				Functions.AdzoneValueVideo=AdzName;
			}

			if(AdzName.equals(Functions.AdzoneValue)&&AdzName.equals(Functions.AdzoneValueVideo))
			{
				System.out.println("Adzones are matched : "+AdzName+"-----"+Functions.AdzoneValue+"-----"+Functions.AdzoneValueVideo);
				break;
			}
			k=k+1;
			System.out.println("k is :"+k);
			System.out.println(" API Adzone  : "+AdzName+"-----: News ad Adzone : "+Functions.AdzoneValue+"-----: News Video Adzone :"+Functions.AdzoneValueVideo);
		}
		if(k==adzonelatvalue){

			Assert.fail("AdZones are not matched");
		}

	}

	//push alerts Validation
	public static void pushalerts_Validations() throws Exception{
		System.out
		.println("================= Push Alerts Page Test Case Started =========================");

		//launch the app in fresh launch mode 
		CharlesFunctions.ClearSessions();
		//start the charles
		//CharlesFunctions.startSessionBrowserData();
		//AppiumFunctions.LaunchApp();
		Ad.quit();
		AppiumFunctions.LaunchAppWithFullReset("false");
		//AppiumFunctions.LaunchApp();
		//loginModule.login();
		Thread.sleep(8000);
		//Functions.verifySavedAddressList_SelectOne("Hoquiam, WA");
		// AppFunctions.Kill_Launch_App();
		// Ad.closeApp();
		AppFunctions.Change_to_Test_Mode("TestMode");
		AppiumFunctions.Kill_launch();
		Thread.sleep(5000);
		homelocation=Ad.findElementById("com.weather.Weather:id/txt_location_name").getAttribute("name");
		AppFunctions.goToWelcome("TestMode");
		if (Ad.findElement(By.name("My alerts")).isDisplayed()) {
			System.out.println("clicking My alerts");
			Ad.findElement(By.name("My alerts")).click();
		}
		// Winter weather alerts
		// com.weather.Weather:id/alert_title
		// android.widget.TextView
	// 	Ad.findElement(By.name("Winter weather alerts")).click();
// 		System.out.println("Winter weather toggel status is : "+Ad.findElement(By.name("Winter weather alerts")).getText());
// 		// select location checkbox
// 		// Off OFF
// 		// com.weather.Weather:id/alert_switch
// 		// com.weather.Weather:id/alert_switch
// 		// android.widget.Switc
// 		WebElement WWA = Ad.findElement(By.xpath("//android.widget.Switch[@resource-id='com.weather.Weather:id/alert_switch']"));
// 		System.out.println("Winter weather toggel status is : "+WWA.getText());
// 		Thread.sleep(5000);
// 		if(WWA.getText().toString().contains("Off OFF")){
// 			Ad.findElement(
// 					By.xpath("//android.widget.Switch[@resource-id='com.weather.Weather:id/alert_switch']"))
// 					.click();
// 			Thread.sleep(3000);
// 			Ad.findElementByName("Select Location").click();
// 			Thread.sleep(3000);
// 			//Ad.findElement(By.name("Hoquiam, WA")).click();
// 		}else{
// 			System.out.println("Toggle already selected");
// 		}
// 		System.out.println("Winter weather toggel status is : "+WWA.getText());
// 		// Ad.findElement(By.name("Off OFF")).click();
// 		// Hoquiam, WA
// 		// com.weather.Weather:id/title_text_view
// 		// android.widget.TextView
// 
// 		// Ad.findElement(By.xpath("//android.widget.TextView[@resource-id='com.weather.Weather:id/title_text_view']")).click();
// 
// 		// click back
// 
// 		Ad.findElement(By.className("android.widget.ImageButton")).click();

		// on real time rain

		// Real-time rain
		// com.weather.Weather:id/alert_title
		// android.widget.TextView

		Ad.findElement(By.name("Real-time rain")).click();

		// Off OFF
		// com.weather.Weather:id/alert_switch
		// android.widget.Switch
		Thread.sleep(5000);
		WebElement WWA1 = Ad.findElement(By.xpath("//android.widget.Switch[@resource-id='com.weather.Weather:id/alert_switch']"));
		System.out.println("Winter weather toggel status is : "+WWA1.getText());
		Thread.sleep(5000);
		if(WWA1.getText().toString().contains("Off OFF")){
			WWA1.click();
			Thread.sleep(3000);
			//Ad.findElement(By.name("Hoquiam, WA")).click();
		}else{
			System.out.println("Toggle already selected");
		}

		// kill
		// AppFunctions.Kill_Launch_App();
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(3000);
		//AppFunctions.Swipe_Breakignews();
		if(Ad.findElement(By.name("Breaking News")).isDisplayed())
		{
			Ad.findElement(By.name("Breaking News")).click();
		}

		Thread.sleep(5000);
		WebElement WWA2 = Ad.findElement(By.xpath("//android.widget.Switch[@resource-id='com.weather.Weather:id/alert_switch']"));
		System.out.println("Winter weather toggel status is : "+WWA2.getText());
		Thread.sleep(5000);
		if(WWA2.getText().toString().contains("Off OFF")){
			WWA2.click();
			Thread.sleep(3000);
		}
		else{
			System.out.println("Toggle already selected");
		}
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(3000);	
		if(Ad.findElement(By.name("Lightning strikes")).isDisplayed())
		{
			Ad.findElement(By.name("Lightning strikes")).click();
		}	
		Thread.sleep(5000);
		WebElement WWA3 = Ad.findElement(By.xpath("//android.widget.Switch[@resource-id='com.weather.Weather:id/alert_switch']"));
		System.out.println("Winter weather toggel status is : "+WWA3.getText());
		Thread.sleep(5000);
		if(WWA3.getText().toString().contains("Off OFF")){
			WWA3.click();
			Thread.sleep(3000);
		}
		else{
			System.out.println("Toggle already selected");
		}
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(3000);
		if(Ad.findElement(By.name("Government issued alerts")).isDisplayed())
		{
			Ad.findElement(By.name("Government issued alerts")).click();
		}	
		Thread.sleep(5000);
		WebElement WWA4 = Ad.findElement(By.xpath("//android.widget.Switch[@resource-id='com.weather.Weather:id/alert_switch']"));
		System.out.println("Winter weather toggel status is : "+WWA4.getText());
		Thread.sleep(5000);
		if(WWA4.getText().toString().contains("Off OFF")){
			WWA4.click();
			Thread.sleep(4000);
			//AppFunctions.Swipe_Breakignews();
			//System.out.println("Location Name is : "+AppFunctions.homelocation);
			//			try{
			//				if(Ad.findElement(By.name("Cyberabad, Andhra Pradesh (Follow me)")).isDisplayed())
			//				{
			//					Ad.findElement(By.name("Cyberabad, Andhra Pradesh (Follow me)")).click();
			//					Thread.sleep(1000);
			//				}

			//}catch(Exception e){
//			List <WebElement> Locations = Ad.findElementsById("com.weather.Weather:id/title_text_view");
//			for(WebElement LocationName : Locations){
//				if(LocationName.getText().contains(AppFunctions.homelocation)){
//					LocationName.click();
//

			List <WebElement> Locations = Ad.findElementsByClassName("android.widget.CheckBox");
			System.out.println(Locations.size());
			///if(Locations.get(0).isSelected()){
				//System.out.println(Locations.get(0).getText()+" Location already selected");
			//}else{
				//Locations.get(0).click();
			//}
			Thread.sleep(5000);
				//				}

			}

			//Thread.sleep(3000);
		//}
		else{
			System.out.println("Toggle already selected");
		}

		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(2000);
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		//Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(2000);
		// settings
		// AppFunctions.goToWelcome("TestMode");
		// Ad.findElementByName("Settings").click();

		// MobileElement alertPopup;
		// alertPopup=(MobileElement)Ad.findElement(By.xpath("//android.view.View[@content-desc='close_button']"));
		// if(alertPopup.isDisplayed()){
		// System.out.println("clicking the popup");
		// alertPopup.click();
		// }

		//AppFunctions.goToTestModeSettings("TestMode");

		AppFunctions.goToTestModeSettings("TestMode");

		CharlesFunctions.startSessionBrowserData();
		Thread.sleep(3000);
		Ad.findElementByName("Alerts").click();
		Thread.sleep(4000);
		Ad.findElement(
				By.xpath("//android.widget.TextView[@text='real time rain alert']"))
				.click();
		Thread.sleep(4000);
		// List<WebElement> al=Ad.findElementsById("android:id/title");
		// al.get(0).click();
		// Thread.sleep(2000);

		MobileElement statusBar = (MobileElement) Ad
				.findElementById("android:id/statusBarBackground");
		System.out.println("Dragging from status bar");
		Thread.sleep(3000);
		MobileElement videoTile = (MobileElement) Ad
				.findElement(By
						.xpath("//*[@class='android.widget.TextView' and @text='Airlock']"));
		System.out.println("Dragging till " + videoTile.getText());

		TouchAction action = new TouchAction(Ad);
		Dimension dimensions = Ad.manage().window().getSize();
		Double startY = (double) dimensions.getHeight();
		Double startX = (double) dimensions.getWidth();
		System.out.println("StartX :" + startX + "startY" + startY);
		action.longPress(statusBar).moveTo(videoTile).release().perform();
		Thread.sleep(5000);
		CharlesFunctions.startSessionBrowserData();
		CharlesFunctions.ClearSessions();	
		Ad.findElementByName("Real-Time Rain Alert").click();
		Thread.sleep(35000);
		CharlesFunctions.ExportSession();
		//verift real time rain alert cust param 

		//AppFunctions.verifyAdCall("Push Alerts", "weatherAd");

		Map<String, String>  pubads_call_results=null;
		pubads_call_results=Functions.readPushalerts_realtimerain_Pub_Ad_Call_Data("Push Alerts");
		Thread.sleep(4000);
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(4000);
		//AppFunctions.goToWelcome("TestMode");
		AppFunctions.goToTestModeSettings("TestMode");
		Ad.findElementByName("Alerts").click();
		Thread.sleep(4000);
		Ad.findElement(
				By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ListView[1]/android.widget.TextView[1]"))
				.click(); 
		Thread.sleep(15000);
		Functions.Drag_alerts_from_Notificationsbar();
		Thread.sleep(5000);		
		CharlesFunctions.startSessionBrowserData();
		CharlesFunctions.ClearSessions();	
		Thread.sleep(10000);
		List<WebElement> Notificationslist = Ad.findElementsById("android:id/title");

		for(int i=0;i<=Notificationslist.size();i++){
			System.out.println("Notification : "+Notificationslist.get(i).getText());
			if(Notificationslist.get(i).getText().contains("Severe")){
				Notificationslist.get(i).click();
				break;
			}

		}
		Thread.sleep(5000);
		CharlesFunctions.ExportSessions();	
		//Map<String, String>  pubads_call_results=null;
		//pubads_call_results=Functions.readPushalerts_breakingnews_Pub_Ad_Call_Data("Push Alerts");		
		pubads_call_results=Functions.readPushalerts_severe_Pub_Ad_Call_Data("Push Alerts");
		Thread.sleep(5000);
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(4000);
		AppFunctions.goToTestModeSettings("TestMode");
		Ad.findElementByName("Alerts").click();
		Thread.sleep(4000);
		//Ad.findElement(By.name("Breaking News")).click();
		//start the charles and delete the folder	
		Ad.findElement(
				By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ListView[1]/android.widget.TextView[4]"))
				.click(); 
		Thread.sleep(2000);
		Functions.Drag_alerts_from_Notificationsbar();
		Thread.sleep(5000);		
		CharlesFunctions.startSessionBrowserData();
		CharlesFunctions.ClearSessions();	
		Ad.findElementByName("Breaking News").click();
		Thread.sleep(35000);
		CharlesFunctions.ExportSession();		 		
		pubads_call_results=Functions.readPushalerts_breakingnews_Pub_Ad_Call_Data("Push Alerts");
			Thread.sleep(4000);
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(4000);
		//			Ad.findElement(
		//					By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ListView[1]/android.widget.TextView[1]"))
		//					.click(); 
		//			Thread.sleep(5000);
		//			Functions.Drag_alerts_from_Notificationsbar();
		//			Thread.sleep(5000);		
		//			 CharlesFunctions.startSessionBrowserData();
		//			 CharlesFunctions.ClearSessions();
		//			 Thread.sleep(3000);
		//			Ad.findElementByName("Severe Thunderstorm Warning").click();
		//			Thread.sleep(5000);
		//			 CharlesFunctions.ExportSessions();	
		//			//Map<String, String>  pubads_call_results=null;
		//			//pubads_call_results=Functions.readPushalerts_breakingnews_Pub_Ad_Call_Data("Push Alerts");
		//			 pubads_call_results=Functions.readPushalerts_severe_Pub_Ad_Call_Data("Push Alerts");
		//			Thread.sleep(5000);
		//			Ad.findElement(By.className("android.widget.ImageButton")).click();
		AppFunctions.goToTestModeSettings("TestMode");
		Ad.findElementByName("Alerts").click();
		Thread.sleep(4000);
		Ad.findElement(
				By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ListView[1]/android.widget.TextView[6]"))
				.click(); 
		Thread.sleep(2000);
		Functions.Drag_alerts_from_Notificationsbar();
		Thread.sleep(5000);		
		CharlesFunctions.startSessionBrowserData();
		CharlesFunctions.ClearSessions();	
		Ad.findElementByName("Lightning Strike").click();
		Thread.sleep(40000);
		CharlesFunctions.ExportSession();	
		pubads_call_results=Functions.readPushalerts_lightingnews_Pub_Ad_Call_Data("Push Alerts");
		Thread.sleep(4000);
		Ad.findElement(By.className("android.widget.ImageButton")).click();
		Thread.sleep(4000);
		//		 Ad.findElement(By.className("android.widget.ImageButton")).click();
		//		 Thread.sleep(4000);
		//		 AppFunctions.goToTestModeSettings("TestMode");
		//			Ad.findElementByName("Alerts").click();
		//			Thread.sleep(4000);
		//			Ad.findElement(
		//					By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ListView[1]/android.widget.TextView[1]"))
		//					.click(); 
		//			Thread.sleep(5000);
		//			Functions.Drag_alerts_from_Notificationsbar();
		//			Thread.sleep(5000);		
		//			 CharlesFunctions.startSessionBrowserData();
		//			 CharlesFunctions.ClearSessions();	
		//			Ad.findElementByName("Severe Thunderstorm Warning").click();
		//			Thread.sleep(2000);
		//			 CharlesFunctions.ExportSessions();	
		//			//Map<String, String>  pubads_call_results=null;
		//			//pubads_call_results=Functions.readPushalerts_breakingnews_Pub_Ad_Call_Data("Push Alerts");
		//			 pubads_call_results=Functions.readPushalerts_severe_Pub_Ad_Call_Data("Push Alerts");
		//			 Thread.sleep(4000);

		System.out
		.println("================= Push Alerts Page Test Case End =========================");
		Functions.softAssert.assertAll();





	}

	public static void Swipe_Breakignews(){
		Dimension dimensions = Ad.manage().window().getSize();//throwing exception
		Double startY1 = dimensions.getHeight() * 0.4;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/40);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		Ad.swipe(0, startY, 0, endY,2000);

	}

	public static void verifyAdPresentOnExtendedPage(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");  
		MobileElement extendModuleValidate = (MobileElement) Ad.findElementById(exceldata[4][Cap]);
		if(extendModuleValidate.isDisplayed() )
		{
			System.out.println("On Extended "+exceldata[1][Cap]+" page");
			WebDriverWait wait1 = new WebDriverWait(Ad, 10);
			wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
			MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
			if (AdEle.isDisplayed()) {
				logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
				System.out.println("Ad is present on Extended page");
				RetryAnalyzer.count=0;
				Thread.sleep(2000);

			}
		}
	}

	public static void clickOnModule(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");           


		try {
			MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
			module2.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppFunctions.Swipe();
			Thread.sleep(1000);
			MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
			module2.click();
		} 

	}

	public static void scrollInToView(String excel_sheet_name) throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");           

		int swipe = Integer.parseInt(exceldata[2][Cap].trim());
		System.out.println("swipe count: "+swipe);
		Ad.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		System.out.println("@@@@@@@@@@@@@ s");
		MobileElement module;
		int MAX_SWIPES = 12;
		//int MAX_SWIPES = swipe;
		for (int i = 0; i < MAX_SWIPES; i++) {

			try {
				module = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
				if(module.getText().equalsIgnoreCase(exceldata[1][Cap])){
					System.out.println(exceldata[1][Cap] + " Module Is Present On Page");
					logStep(exceldata[1][Cap] + " Module Is Present On Page");
					break;
				}
			} catch (Exception e) {
				System.out.println(exceldata[1][Cap] + " module is not present and so scrolling down");
				Swipe();	
			}
		}
	}

	public static void Swipe_Up(int x){
		Dimension dimensions = Ad.manage().window().getSize();
		Double startY1 = dimensions.getHeight() * 0.7;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/6);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		//System.out.println("endY  - "+endY);
	//	System.out.println("startY  - "+startY);
		for(int i=0;i<=x;i++){
			Ad.swipe(0, endY, 0, startY,2000);
		}
	}

	//SwipeUp based on counter  //by naresh
	public static void SwipeUp_Conter(int Counter) throws Exception{

		int swipeup = Counter;

		for(int i=1;i<=swipeup ;i++){
			//Thread.sleep(2000);
			//Swipe();
			try{
				if(Ad.findElementById("com.weather.Weather:id/temperature").isDisplayed()){
					//System.out.println("Watson ad presented");
					Swipe_Up(5);


					break;
				}
			}catch (Exception e){
				Swipe_Up(5);

			}


			//Thread.sleep(2000);
		}
	}

	//Swipe based on counter  //by naresh
	public static void Swipe_Conter(int Counter) throws Exception{

		int swipe = Counter;

		for(int i=1;i<=swipe ;i++){
			//Thread.sleep(2000);
			//Swipe();
			try{
				Thread.sleep(2000);
				if(Ad.findElementByName("Name any course, dish, or ingredient").isDisplayed()){
					//System.out.println("Watson ad presented");
					break;
				}
			}catch (Exception e){
				Swipe();
				//System.out.println("watson ad not present");
			}


			//Thread.sleep(2000);
		}
	}


	//Search with Watson
	public static void search_with_watson_ad(String SeachText) throws Exception{


		Thread.sleep(5000);
		Ad.findElementByName("Name any course, dish, or ingredient").click();
		//Ad.findElementByName("Name any course, dish, or ingredient").clear();

		Thread.sleep(5000);
		Ad.findElementByName("Name any course, dish, or ingredient").sendKeys(SeachText);
		Thread.sleep(3000);
		//Ad.navigate().back();
		Ad.hideKeyboard();
		Thread.sleep(10000);//changed from 1 to 3 by ravi
		Ad.findElementByAccessibilityId("Enter").click();
		//Ad.findElement(By.name("Enter")).click();
		//Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[2]/android.view.View[1]/android.view.View[1]/android.view.View[3]").click();
		Thread.sleep(15000);
		String SearchedText = Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[12]").getAttribute("name");
		//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]
		//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[9]
		System.out.println(SearchedText);
		//String SearchedText=Ad.findElementByAccessibilityId("Watson invented Hot Cannellini Bean Soup with Campbell’s® Tomato Juice, white kidney beans, Swanson® Natural Goodness® Chicken Broth, california vegetable blend, oregano, garlic powder, black pepper");
		//List<WebElement> se=Ad.findElementsByClassName("android.view.View");
		//String SearchedText=se.get(11).getAttribute("Value");
		//			System.out.println("SS Text is : "+Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]").getAttribute("name"));
		//		System.out.println("Searched Text is :"+SearchedText );
		//			if(Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]").getAttribute("name").contains(SeachText.toString())){
		//				System.out.println("Seached text is matched");
		//			}else
		//			{
		//				Assert.fail("Searched Text : "+ SeachText +" not matched");
		//			}
		if(SearchedText.contains("Hot"))
		{
			System.out.println("Seached text is matched");
		}
		else
		{
			Assert.fail("Searched Text not matched");
		}

		AppFunctions.Swipe_Conter(3);


	}
	public static void Pull_To_Refresh(String excel_sheet_name) throws Exception{
		logStep("On CC Screen Do A Pull To Refresh");
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		/*String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		System.out.println("Pull the screen to REFRESH is Start");*/

		WebDriverWait wait = new WebDriverWait(Ad, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.weather.Weather:id/current_conditions_temperature")));

		//Temperature element
		MobileElement temp = (MobileElement) Ad.findElementById("com.weather.Weather:id/current_conditions_temperature");
		System.out.println("Temp : "+temp.getText());

		MobileElement hilo=null;
		//HILO element
		try{
		hilo = (MobileElement) Ad.findElementById("com.weather.Weather:id/hourly_forecast_card_view");
		}catch(Exception e){
		    Thread.sleep(3000);
			hilo=(MobileElement) Ad.findElementByClassName("android.widget.FrameLayout");
		}
		System.out.println("hilo : "+hilo.getText());
		TouchAction action = new TouchAction(Ad);
		action.longPress(temp).moveTo(hilo).release().perform();
		Thread.sleep(3000);
		System.out.println("Pull the screen to REFRESH is done");
		logStep("Pull the screen to REFRESH is done");
		
	}

	public static void Kill_Launch_App() throws Exception{
try {
		Ad.closeApp();
		Ad.launchApp();
		Thread.sleep(15000);
		}
catch(Exception e) {
	Ad.closeApp();
	Ad.launchApp();
	Thread.sleep(15000);
	try {
		Ad.closeApp();
		Ad.launchApp();
		Thread.sleep(15000);
	}
	catch(Exception e1) {
		Ad.closeApp();
		Ad.launchApp();
		Thread.sleep(15000);
	}
}
	}

	public static void Swipe(){
		Dimension dimensions = Ad.manage().window().getSize();
		Double startY1 = dimensions.getHeight() * 0.8;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/40);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		Ad.swipe(0, startY, 0, endY,2000);
	}


	public static void verify_adpresent_onextended_page(String excel_sheet_name) throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		//System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");           

		int swipe = Integer.parseInt(exceldata[2][Cap]);
		Ad.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);//causing exception
		MobileElement module;
		int MAX_SWIPES = 9;

		for (int i = 0; i < MAX_SWIPES; i++) {
        Thread.sleep(5000);

			try{
				module = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
				//module = (MobileElement) Ad.findElementByName(exceldata[5][Cap]);
				System.out.println("Module"+" "+module.getText()+" Present");
				Thread.sleep(5000);

				if(module.getText().equalsIgnoreCase(exceldata[1][Cap])){
					
					logStep(exceldata[1][Cap] + " Module Is Present On Page"); 
					try {
						
						//commented by aswin
						MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
						//MobileElement module2 = (MobileElement)Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]"));
						module2.click();
						Thread.sleep(6000);
						try{
							if(TestName.equals("Adzone")){
								break;
							}else{
								System.out.println("User on "+module.getText());
							}
						}catch(Exception e){

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						Swipe();
						MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
						module2.click();
						Thread.sleep(5000);
						try{
							if(TestName.equals("Adzone")){
								break;
							}else{
								System.out.println("User on "+module.getText());
							}
						}catch(Exception e1){


						}
					} 

					MobileElement extendModule = (MobileElement) Ad.findElementById(exceldata[6][Cap]);
					if(exceldata[1][Cap].equalsIgnoreCase("Radar & Maps_old")){
						if(extendModule.isDisplayed() )
						{
							MobileElement extendModuleValidate = (MobileElement) Ad.findElementById(exceldata[4][Cap]);
							if(extendModuleValidate.isDisplayed() )
							{
								System.out.println("On Extended "+exceldata[1][Cap]+" page");
								WebDriverWait wait1 = new WebDriverWait(Ad, 10);
								wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
								MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
								if (AdEle.isDisplayed()) {
									logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
									System.out.println("Ad is present on Extended page");
									RetryAnalyzer.count=0;
									Thread.sleep(2000);
									try{
										Ad.findElementByClassName(exceldata[10][Cap]).click();
									}catch(Exception e){
										Ad.findElement(By.name("Navigate up")).click();
									}
									break;
								}else{
									Assert.fail("Ad not presented on - "+exceldata[1][Cap]+" Page");
								}
							}
						}	
					}	
					else {
						if(extendModule.isDisplayed())
						{
							System.out.println("On Extended "+exceldata[1][Cap]+" page");
							WebDriverWait wait1 = new WebDriverWait(Ad, 10);
							wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
							MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
							if (AdEle.isDisplayed()) {
								logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
								System.out.println("Ad is present on Extended page");
								RetryAnalyzer.count=0;
								Thread.sleep(2000);
								try{
									Ad.findElementByClassName(exceldata[10][Cap]).click();
									Thread.sleep(5000);
								}catch(Exception e){
									Ad.findElement(By.name("Navigate up")).click();
									Thread.sleep(5000);
								}
								//click on back arrow
								break;
							}else{
								Assert.fail("Ad not presented on - "+exceldata[1][Cap]+" Page");
							}
						}
					}


				}	
			} catch (Exception e) {
				//System.out.println(e);
				System.out.println(exceldata[1][Cap] + " module is not present and so scrolling down");
				Swipe();	
				if(i==MAX_SWIPES-1){
					Assert.fail(exceldata[1][Cap] +" - Module not found");
				}
			}
		}//for loop end

	}



	public static void CleanLaunch_launch(String excel_sheet_name) throws Exception
	{
		logStep("Verify Ad Calls On App Launch");
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);

		for(int i=1;i<=10 ;i++){
			Thread.sleep(2000);
			Swipe();
			Thread.sleep(2000);
		}

		int MAX_SWIPES = 5;

		for (int j = 0; j < MAX_SWIPES; j++) {

			MobileElement module = null;

			try {

				WebDriverWait wait0 = new WebDriverWait(Ad, 10);
				wait0.until(ExpectedConditions.visibilityOf(Ad.findElementByXPath(exceldata[1][Cap])));
				module = (MobileElement) Ad.findElementByXPath(exceldata[1][Cap]);
			} catch (Exception e) {
				// System.out.println(e);
			}


			if (module!=null && module.isDisplayed()) {
				System.out.println("Last module is present");
				Swipe();
				break;
			} 
			else {
				logStep("Ad Calls Not Presented On App Launch");
				System.out.println("Last module is NOT present,scrolling down");
				Swipe();
			}
		}
	}

	public static void Change_to_Test_Mode(String excel_sheet_name) throws Exception{

		logStep("Make Ads As Test From Test Mode Settings In Order To Get BB Ad Call");
		logStep("TestMode Settings: 1) Click On Menu Button 2) Click On Settings 3) Click On About This App 4) Click 10 Times On App Version 5) TestMode Setting Enabled 6) Click On TestMode Settings 7) Click On Ads");


		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);

//		WebDriverWait wait = new WebDriverWait(Ad, 60);
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));
		// Clicking on Menu Options
		MobileElement menu=null;
		Thread.sleep(5000);
		try{
		 menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
		}catch(Exception e){
			menu = (MobileElement) Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.ImageButton[1]"));
			//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.ImageButton[1]
		}
		Thread.sleep(5000);
		menu.click();
		Thread.sleep(5000);
		Ad.findElementByName(exceldata[5][Cap]).click();
		Thread.sleep(5000);
		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName(exceldata[6][Cap]);
		Thread.sleep(5000);
		aboutThisAPP.click();
		Thread.sleep(5000);
		try{
			if(Ad.findElementByName(exceldata[19][Cap]).isDisplayed()){

				System.out.println("clicking on test mode settings");
			}
		}catch(Exception e){
			for (int i=1; i<=8; i++){
				Ad.findElementById(exceldata[18][Cap]).click();
				Thread.sleep(15000);

			}
		}
		Ad.findElementByName(exceldata[19][Cap]).click();
		Thread.sleep(15000);
		Ad.findElementByName(exceldata[20][Cap]).click();
		Thread.sleep(15000);
		Ad.findElementByName(exceldata[16][Cap]).click();
		Thread.sleep(15000);
		logStep("Changed Ads As Production To Test");

		System.out.println("Changed to Test Mode");
		Thread.sleep(1000);
		logStep("Kill And Relaunch The App");
		//				Ad.closeApp();
		//				System.out.println("Closed the app");
		//				Ad.launchApp();
		AppiumFunctions.Kill_launch();

		try{
			if((Ad.findElementByName("Allow")).isDisplayed()){
				Ad.findElementByName("Allow").click();
			}
		}catch(Exception e){
			System.out.println("Location already set");
		}

		System.out.println("launching the app");
	}


	//Select Address

	public static void enter_selectAddress(String AddressName){

	}

	public static void verifyBBCallLocationFromListInTestMode(String excel_sheet_name,String AddressName) throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		/* --- Start For Android Device --- */
		if(Cap == 2){
			String[][] addressdata = read_excel_data.exceldataread("AddressPage");

			WebDriverWait wait4 = new WebDriverWait(Ad, 40);
			wait4.until(ExpectedConditions.presenceOfElementLocated(By.name(addressdata[4][Cap])));

			//Root Location Element
			Ad.findElementByName(addressdata[4][Cap]).click();
			Thread.sleep(8000);

			Ad.findElementByName(addressdata[4][Cap]).sendKeys(AddressName);
			Thread.sleep(2000);
			Ad.hideKeyboard();


			WebDriverWait wait5 = new WebDriverWait(Ad, 40);
			wait5.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[6][Cap])));

			//List Location Element
			@SuppressWarnings("unchecked")
			List<MobileElement> loclist = Ad.findElements(By.id(addressdata[6][Cap]));
			Thread.sleep(15000);
			for(MobileElement Loc_Name: loclist){
				if(Loc_Name.getText().equals(AddressName)){
					Loc_Name.click();
					Thread.sleep(15000);
					break;
				}
			}
		}
		/*String firsteleXpath = addressdata[5][Cap];
			String[] parts = firsteleXpath.split("Count");

			String expectedLocation = null;
			/* --- Start For Loop For Location Click --- */
		/*for(int i=1;i<= loclist.size();i++){

				String element = null;

				try {

					element = parts[0]+i+parts[1];

					MobileElement ele = (MobileElement) Ad.findElementByXPath(element);
					System.out.println("For This Location ====>"+ele.getText());
					String location = ele.getText();


					if(excel_sheet_name.equals("TestMode")){
						expectedLocation = exceldata[21][Cap];
					}
					else{
						expectedLocation = exceldata[9][Cap];
					}

					if(location.contains(expectedLocation)){
						logStep("TestLocation "+expectedLocation+" Is Presented");
						logStep("Successfully Added The Test Location "+expectedLocation+" In Order To Get The BB Ad Call");

						System.out.println("Location "+location);

						WebDriverWait wait12 = new WebDriverWait(Ad, 40);
						wait12.until(ExpectedConditions.presenceOfElementLocated(By.xpath(parts[0]+1+parts[1])));
						Ad.findElementByXPath(element).click();
						Thread.sleep(2000);
						break;
					}
					else
					{

						WebDriverWait wait9 = new WebDriverWait(Ad, 40);
						wait9.until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));

						Ad.findElementByXPath(element).click();

						WebDriverWait wait10 = new WebDriverWait(Ad, 40);
						wait10.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));

						Ad.findElementById(addressdata[4][Cap]).click();
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					logStep(expectedLocation+" Is Not Found From Location List. So Need To Set The Location For Test Mode");
					System.out.println(expectedLocation+" is not found in the location list. So need to set the Location for Test Mode");
				}
			}*/
		/* --- End For Android Device --- */
	}

	public static void verify_Vedio_Module_Click_On_Forecast_Video(String excel_sheet_name) throws Exception{

		System.out.println("Searching for Video module");
		logStep("Navigating For Video Module");

		Thread.sleep(5000);
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		int swipe = Integer.parseInt(exceldata[2][Cap]);

		for(int i=1;i<=swipe ;i++){
			Swipe();
			Thread.sleep(1000);
		}

		int MAX_SWIPES = 5;

		for (int i = 0; i<MAX_SWIPES; i++) {

			MobileElement video = null;

			try {
				WebDriverWait wait0 = new WebDriverWait(Ad, 120);
				wait0.until(ExpectedConditions.visibilityOf(Ad.findElementById(exceldata[5][Cap])));
				video = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
				logStep("ForeCast Video Module Is Presented");

			} catch (Exception e) {
				// System.out.println("Exception message :: "+e);	
			}

			if(video!=null && video.isDisplayed())
			{  
				System.out.println("Video module is present ");
				Ad.findElementById(exceldata[5][Cap]).click();
				System.out.println("Video module clicked");
				Thread.sleep(5000);
				//WebDriverWait wait1 = new WebDriverWait(Ad, 120);
				//wait1.until(ExpectedConditions.visibilityOf(Ad.findElementById(exceldata[5][Cap])));
				//Ad.findElementByClassName(exceldata[5][Cap]).click();
				//Thread.sleep(6000);
				//logStep("Tap On ForeCast Video Module ");
				//logStep("Pre-Roll Video Will play");
				//Thread.sleep(10000);
				//WebDriverWait wait0 = new WebDriverWait(Ad, 120);
				//wait0.until(ExpectedConditions.visibilityOf(Ad.findElementById(exceldata[6][Cap])));
				System.out.println("clicking the back button");
				Ad.findElementByClassName(exceldata[6][Cap]).click();
				Thread.sleep(5000);
				System.out.println("clicked the back button");
				break;
			}else
			{
				System.out.println("Video module is NOT present and scrolling down");
				Swipe();
			}
		}
	}

	public static void compareBuildVersion() throws Exception{

		String build_ver = null;
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(Ad, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.ImageButton")));

		MobileElement menu = (MobileElement) Ad.findElement(By.className("android.widget.ImageButton"));
		menu.click();

		//Ad.findElementByName("Settings").click();
		List<MobileElement> menuiopts=Ad.findElementsById("com.weather.Weather:id/design_menu_item_text");
		for(MobileElement options:menuiopts)
		{
			System.out.println("Menu options :::::" +options.getText());
			if(options.getText().equalsIgnoreCase("Settings"))
			{
				options.click();
				break;
			}
		}
		Thread.sleep(3000);

	//	Ad.findElementById("com.weather.Weather:id/design_menu_item_layout_6").click();
		//com.weather.Weather:id/precipitation
		

		/*List<WebElement> sett=Ad.findElementsById("com.weather.Weather:id/table_tile");
		sett.get(0).click();
		Thread.sleep(3000);*/
		
		try {
		Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[4]/android.widget.RelativeLayout/android.widget.TextView").click();
		Thread.sleep(3000);
		}
		catch(Exception e)
		
		{
			Ad.findElementByName("About this app").click();
			Thread.sleep(3000);
		}
//		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName("About this app");
//		aboutThisAPP.click();

		String BuildVersion = Ad.findElementById("com.weather.Weather:id/about_version").getText();
		System.out.println("Build Version is : " + BuildVersion);
		//String BuildVersion="6.11.0 690110560 (8a0deba release)";
		BuildVersion = BuildVersion.trim();
		String[] ver = BuildVersion.split(" ");

		System.out.println("Present Build version : "+ver[3]);

		String Build = properties.getProperty("BuildToDownload");

		if(Build.contains("Beta")){
			build_ver = properties.getProperty("AndroidFlagship_BetaBuild");

		}
		else{
			build_ver = properties.getProperty("AndroidFlagship_AlphaBuild");
		}


		if(ver[3].equals(build_ver)){
			System.out.println("New Build Installed");
		}
		else{
			System.out.println("New Build Not Yet Installed");
			//System.exit(1);
		}

	}
	public static void resetApp(){
		Ad.resetApp();
	}


	public static void verify_subModule(String excel_sheet_name, int no) throws Exception{
		MobileElement module2;

		List<String> tab = new ArrayList<String>(5);

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		//
		//	
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name); 
		//		String [] healthModule={"Allergy", "ColdFlu"};
		//		String [] healthModule={"ColdFlu"};
		//		for(int i = 0; i<healthModule.length; i++){

		if(no==0){
			AppiumFunctions.Kill_launch();
			Thread.sleep(5000);
			AppFunctions.scrollInToView(excel_sheet_name);
		}

		//CharlesFunctions.startSessionBrowserData();
		AppFunctions.clickOnModule(excel_sheet_name);
		Thread.sleep(3000);

		if(tab.size()>0){
			tab.clear();
		}
		if(excel_sheet_name.equalsIgnoreCase("Cold & Flu"))
		{

			tab.add("Cold");
			tab.add("Flu");
		}
		else if(excel_sheet_name.equalsIgnoreCase("Allergy"))
		{

			tab.add("Breathing");
			tab.add("Pollen");
			tab.add("Mold");
		}
		else if(excel_sheet_name.equalsIgnoreCase("GoRun"))
		{

			tab.add("Today");
			Thread.sleep(3000);
			tab.add("Tomorrow");
			Thread.sleep(5000);
			tab.add("This Week");
			Thread.sleep(3000);
		}


		for(int i = 0; i<tab.size(); i++){

			module2 = (MobileElement)Ad.findElement(By.xpath("//android.widget.TextView[@text='"+tab.get(i)+"']"));	//tab[i]   
			//MobileElement module2 = (MobileElement)Ad.findElement(By.xpath("//*[@class='android.support.v7.app.ActionBar$Tab' and @index='1']"));	
			//MobileElement module2 = (MobileElement)Ad.findElement(By.xpath("//*[@class='android.widget.TextView' and @text='Flu']"));	

			System.out.println("is tab selected"+module2.isSelected());
			System.out.println("attribute of text"+module2.getAttribute("text"));
			System.out.println("text"+module2.getText());
			System.out.println("is enabled"+module2.isEnabled());

			if(i==0 && module2.getAttribute("text").equalsIgnoreCase(tab.get(0)) && module2.isEnabled()){
				System.out.println("default tab---->"+tab.get(i)+"-------> is selected first");
			}
			else if(!module2.isSelected())
			{
				module2.click();
				Thread.sleep(3000);
			}

			//					if(module2.isSelected()){
			//						
			//					}
			//					else
			//					{
			//						
			//					}


			CharlesFunctions.ExportSessions();
			Thread.sleep(3000);
			AppFunctions.verifyAdPresentOnExtendedPage(excel_sheet_name);


			//String [] callAdsAllergy={"spotLightAd", "details"};
			//for(int k = 0; k<callAdsAllergy.length; k++){
			//AppFunctions.verifyAdCall(healthModule[i], callAdsAllergy[k]);	
			AppFunctions.verifyAdCall(excel_sheet_name, "spotLightAd");	


			if(!tab.get(i).equalsIgnoreCase("Breathing") && !excel_sheet_name.equalsIgnoreCase("GoRun")){
				CharlesFunctions.startSessionBrowserData();
				AppFunctions.Swipe();
				AppFunctions.Swipe();
				AppFunctions.Swipe();
				Thread.sleep(3000);
				CharlesFunctions.ExportSessions();
				Thread.sleep(5000);
			}
			else
			{
				AppFunctions.Swipe();
				AppFunctions.Swipe();
				AppFunctions.Swipe();
				Thread.sleep(5000);
			}




			AppFunctions.verifyAdPresentOnExtendedPage(excel_sheet_name);
			Thread.sleep(3000);

			if(excel_sheet_name.equalsIgnoreCase("GoRun")){
				AppFunctions.verifyAdCall(excel_sheet_name, "largeAds");
			}
			else
			{
				AppFunctions.verifyAdCall(excel_sheet_name, "details");
			}

			CharlesFunctions.startSessionBrowserData();


			if((tab.size()-1)==i){
				Ad.findElementByClassName(exceldata[9][Cap]).click();
			}	
			System.out.println("================= "+excel_sheet_name+" Test Case End =========================");

		}

	}	

	public static void verifyAdCall(String excel_sheet_name, String callName) throws Exception{
		int i;
		MobileElement module = null;
		boolean breathingTabPresent = false;
		String Read_API_Call_Data= null;
		String resortsCallURL = null;
		String resortsCallParam = null;
		String resortsCallURL_spotlight=null;
		String resortsCallParam_spotlight=null; 
		String resortsCallURL_details=null;
		String resortsCallParam_details=null;
		String resortsCallURL_largeAds=null;
		String resortsCallParam_largeAds=null;


		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);

		if(callName.equalsIgnoreCase("contentAd")){
			resortsCallURL = exceldata[14][Cap];
			resortsCallParam = exceldata[13][Cap];

			System.out.println("=====Checking for Content Ad call====");

		}else if(callName.equalsIgnoreCase("spotlightAd")){
			resortsCallURL = exceldata[33][Cap];
			resortsCallParam = exceldata[34][Cap];
			resortsCallURL_spotlight = exceldata[33][Cap];
			resortsCallParam_spotlight = exceldata[34][Cap];

			System.out.println("====Checking for Spotlight Ad call====");
		}else if(callName.equalsIgnoreCase("largeAds")){
			resortsCallURL = exceldata[35][Cap];
			resortsCallParam = exceldata[36][Cap];
			resortsCallURL_largeAds = exceldata[14][Cap];
			resortsCallParam_largeAds = exceldata[37][Cap];
			System.out.println("====Checking for largeAds call====");
		}

		else if(callName.equalsIgnoreCase("weatherAd")){
			resortsCallURL = exceldata[35][Cap];
			resortsCallParam = exceldata[36][Cap];
			resortsCallURL_largeAds = exceldata[14][Cap];
			resortsCallParam_largeAds = exceldata[37][Cap];
			System.out.println("====Checking for largeAds call====");

		}else if(callName.equalsIgnoreCase("details")){
			resortsCallURL = exceldata[14][Cap];
			resortsCallParam = exceldata[37][Cap];
			resortsCallURL_details = exceldata[14][Cap];
			resortsCallParam_details = exceldata[37][Cap];
			System.out.println("====Checking for details call====");
		}



		Thread.sleep(3000);
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		Thread.sleep(7000);
		try {
			System.out.println("in try block");
			if(!callName.equalsIgnoreCase("details") && !callName.equalsIgnoreCase("largeAds")){
				System.out.println("first index");
				Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(exceldata[15][Cap]));
			}
			else
			{	
				System.out.println("getting lastindex of [CDATA[GET /gampad");
				Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[15][Cap]));

			}
			String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[16][Cap]));
			System.out.println("capturing the expected call data");
			String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[17][Cap])+(exceldata[17][Cap].length()),required_info.indexOf(exceldata[18][Cap]));
			String callData = expected_data.toString();
			System.out.println("expected call data is captured");

			String regex = null;


			int regex1 = 1;
			String resortsCallURL1 = "gampad/ads?riv="+regex1+"&_activity_context=";

			int regex2 = 2;
			String resortsCallURL2 = "gampad/ads?riv="+regex2+"&_activity_context=";

			int regex3 = 5;
			String resortsCallURL3 = "gampad/ads?riv="+regex3+"&_activity_context=";

			int regex4 = 6;
			String resortsCallURL4 = "gampad/ads?riv="+regex4+"&_activity_context=";

			int regex5 = 11;
			String resortsCallURL5 = "gampad/ads?riv="+regex5+"&_activity_context=";

			int regex6 = 14;
			String resortsCallURL6 = "gampad/ads?riv="+regex6+"&_activity_context=";

			int regex7 = 15;
			String resortsCallURL7 = "gampad/ads?riv="+regex7+"&_activity_context=";

			if(excel_sheet_name.equalsIgnoreCase("allergy")){
				System.out.println("Allergy module------>finding Breathing element");
				module = (MobileElement)Ad.findElement(By.xpath("//android.widget.TextView[@text='Breathing']"));
			}



			//to handle the Breathing issue
			if(excel_sheet_name.equalsIgnoreCase("allergy")&& module.isSelected()||excel_sheet_name.equalsIgnoreCase("goRun")){
				System.out.println("Allergy module------>Breathing is selected");
				if(callData.contains(resortsCallURL1)||callData.contains(resortsCallURL2)||callData.contains(resortsCallURL3)||callData.contains(resortsCallURL4)){

					if(callData.contains(exceldata[39][Cap])){
						System.out.println("Allergy module------>Breathing call contains ui");	
						if(callData.contains(resortsCallParam) ){
							System.out.println("Cust Param is found for Breathing");
						}else {


							if(callName.equalsIgnoreCase("spotlightAd")){

								if(!excel_sheet_name.equalsIgnoreCase("GoRun")){
									System.out.println("=====Checking for Content Ad call====");
									resortsCallParam_details = exceldata[37][Cap];
									if(callData.contains(resortsCallParam_details) ){
										System.out.println("Cust Param is found for Breathing");
									}
								}else {
									System.out.println("=====Checking for largeAdscall====");
									resortsCallParam_largeAds = exceldata[37][Cap];
									if(callData.contains(resortsCallParam_largeAds) ){		
										System.out.println("Cust Param is found for Breathing");
									}
								}
							}else if(callName.equalsIgnoreCase("details")||callName.equalsIgnoreCase("largeAds")){
								System.out.println("=====Checking for spotlightAd call====");
								resortsCallParam_spotlight = exceldata[34][Cap];
								if(callData.contains(resortsCallParam_spotlight) ){
									System.out.println("Cust Param is found");
								}
							}

						}
					}
				}


			}else{



				//if(excel_sheet_name.equalsIgnoreCase("weatherAd")){

				for(int n = 0; n<20;n++){
					resortsCallURL = "riv="+n+"&_activity_context=";
					try {
						if(callData.contains(resortsCallURL)){
							int k = n;
							//gampad/ads?
							resortsCallURL = "riv="+k+"&_activity_context=";
							break;
						}
					} catch (Exception e) {
						System.out.println("riv value is not found");
					}
				}

				//}


				if(callData.contains(resortsCallURL)||callData.contains(resortsCallURL1)||callData.contains(resortsCallURL2)||callData.contains(resortsCallURL3)||callData.contains(resortsCallURL4)||callData.contains(resortsCallURL5)){

					if(callData.contains(exceldata[39][Cap])){
						if(callData.contains(resortsCallParam)){
							System.out.println("Cust Param is found for the call on  "+exceldata[1][Cap]+" Page====");
							logStep("ust Param is found for the call on "+exceldata[1][Cap]+" Page");
							//Retry.maxRetryCount=0;
						}
						else
						{
							System.out.println("Cust Param is not Matched for the call on "+exceldata[1][Cap]);
							logStep("Cust Param is not Matched for the call on "+exceldata[1][Cap]+" Page");
							Assert.fail("Cust Param is not Matched for the call on "+exceldata[1][Cap]);

						}
					}
				}
				else
				{

					System.out.println("Call is Not Generated on "+exceldata[1][Cap]);
					logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
					Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
				}
			}


		} catch (Exception e) {
			System.out.println( "Calllllll is not Generated on "+exceldata[1][Cap]);
			logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
			Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
		}		

	}






	public static void goToWelcome(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);

		WebDriverWait wait = new WebDriverWait(Ad, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));//settings button
		System.out.println("clicking on Menu option");
		try{
			TouchAction action = new TouchAction(Ad);
			//action.longPress(temp).moveTo(hilo).release().perform();
			action.moveTo(80, 350).perform();
			
		}catch(Exception e){
			Swipe_Up(2);
		}
		Swipe_Up(3);
		Thread.sleep(3000);
		MobileElement menu=null;
		try{
		 menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
		}catch(Exception e){
		Thread.sleep(3000);
		 menu = (MobileElement) Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.ImageButton[1]"));
		}
		menu.click();
	}

	public static void goToTestModeSettings(String excel_sheet_name) throws Exception{

		logStep("Make Ads As Test From Test Mode Settings In Order To Get BB Ad Call");
		logStep("TestMode Settings: 1) Click On Menu Button 2) Click On Settings 3) Click On About This App 4) Click 10 Times On App Version 5) TestMode Setting Enabled 6) Click On TestMode Settings 7) Click On Ads");


		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);

//		WebDriverWait wait = new WebDriverWait(Ad, 60);
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));//settings button
//		MobileElement menu=null;
//		try{
//			menu = (MobileElement) Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.ImageButton[1]");
//		}catch(Exception e){
//			
//			menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
//		}
//
//		Thread.sleep(6000);
//		menu.click();
		AppFunctions.goToWelcome("TestMode");
		System.out.println("clicking on Menu option");
		Thread.sleep(5000);
		try{
			Ad.findElementByName(exceldata[5][Cap]).click();
		}catch(Exception e){
			// Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.view.ViewGroup[1]/android.widget.ImageButton[1]").click();
			Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]/android.support.v7.widget.LinearLayoutCompat[5]").click();
		}
		System.out.println("clicking on settings option");
		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName(exceldata[6][Cap]);//about this app
		Thread.sleep(6000);
		aboutThisAPP.click();
		System.out.println("clicking on about this app option");
		try{
			if(Ad.findElementByName(exceldata[19][Cap]).isDisplayed()){
				Ad.findElementByName(exceldata[19][Cap]).click();
				System.out.println("clicking on test mode settings");
			}
		}catch(Exception e){
			System.out.println("tapping continously to get test mode option");	
			for (int i=1; i<=8; i++){
				Ad.findElementById(exceldata[18][Cap]).click(); 
			}
			Ad.findElementByName(exceldata[19][Cap]).click();
			System.out.println("clicking on test mode settings");	
		}	
	}
	public static void Change_to_airlock_testMode(String excel_sheet_name) throws Exception{

		logStep("Make Ads As Test From Test Mode Settings In Order To Get BB Ad Call");
		logStep("TestMode Settings: 1) Click On Menu Button 2) Click On Settings 3) Click On About This App 4) Click 10 Times On App Version 5) TestMode Setting Enabled 6) Click On TestMode Settings 7) Click On Ads");


		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		MobileElement menu=null;
		Thread.sleep(5000);
		try{
		 menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
		}catch(Exception e){
			menu = (MobileElement) Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.ImageButton[1]"));			
		}
		Thread.sleep(5000);
		menu.click();
		Thread.sleep(5000);
		Ad.findElementByName(exceldata[5][Cap]).click();
		Thread.sleep(5000);
		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName(exceldata[6][Cap]);
		Thread.sleep(5000);
		aboutThisAPP.click();
		Thread.sleep(5000);
		try{
			if(Ad.findElementByName(exceldata[19][Cap]).isDisplayed()){

				System.out.println("clicking on test mode settings");
			}
		}catch(Exception e){
			for (int i=1; i<=8; i++){
				Ad.findElementById(exceldata[18][Cap]).click();
				Thread.sleep(15000);

			}
		}
		Ad.findElementByName(exceldata[19][Cap]).click();
		Thread.sleep(15000);
		Ad.findElementByName(exceldata[20][Cap]).click();
		Thread.sleep(15000);
	}
	public static void verify_adpresent_onHourly_page(String excel_sheet_name) throws Exception{
        DeviceStatus device_status = new DeviceStatus();
        int Cap = device_status.Device_Status();
        String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
    //  Ad.findElementById("com.weather.Weather:id/bottom_bar_item_1").click();
     Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.support.v4.view.ViewPager/android.view.ViewGroup/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.HorizontalScrollView/android.widget.LinearLayout/android.support.v7.app.ActionBar.Tab[2]").click();
        System.out.println("Hourly element clicked");
        Thread.sleep(3000);
//        WebDriverWait wait1 = new WebDriverWait(Ad, 60);
//        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
//        MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]);
//        if (AdEle.isDisplayed()) {
//            logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
//        }
    }
	public static void click_HomeButton() throws Exception{
	Ad.findElementById("com.weather.Weather:id/bottom_bar_home").click();
	Thread.sleep(3000);
	}
    public static void verify_adpresent_onDaily_page(String excel_sheet_name) throws Exception{
        DeviceStatus device_status = new DeviceStatus();
        int Cap = device_status.Device_Status();

        String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
        Ad.findElementById("com.weather.Weather:id/bottom_bar_item_2").click();
        System.out.println("Daily element clicked");
        Thread.sleep(3000);
//        WebDriverWait wait1 = new WebDriverWait(Ad, 60);
//        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
//        MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]);
//        if (AdEle.isDisplayed()) {
//            logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
//        }
    }

public static void verify_adpresent_onToday_page() throws Exception{
    DeviceStatus device_status = new DeviceStatus();
    int Cap = device_status.Device_Status();

   // String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
    try {
        Ad.findElementByXPath("").click();
        }
        catch(Exception e) {
        	Ad.findElementByName("Today").click();
        }
    System.out.println("Today element clicked");
    Thread.sleep(3000);
//    WebDriverWait wait1 = new WebDriverWait(Ad, 60);
//    wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
//    MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]);
//    if (AdEle.isDisplayed()) {
//        logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
//    }
}
public static void verify_adpresent_onRadar_page(String excel_sheet_name) throws Exception{
    DeviceStatus device_status = new DeviceStatus();
    int Cap = device_status.Device_Status();

    String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
   Ad.findElementById("com.weather.Weather:id/bottom_bar_item_3").click();
System.out.println("Radar element clicked");
    Thread.sleep(3000);
//    WebDriverWait wait1 = new WebDriverWait(Ad, 60);
//    wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
//    MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]);
//    if (AdEle.isDisplayed()) {
//        logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
//    }
}
public static void verify_adpresent_onvideo_page(String excel_sheet_name) throws Exception{
    DeviceStatus device_status = new DeviceStatus();
    int Cap = device_status.Device_Status();

    String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
        Ad.findElementById("com.weather.Weather:id/bottom_bar_item_4").click();
     System.out.println("video element clicked");
    Thread.sleep(3000);
//    WebDriverWait wait1 = new WebDriverWait(Ad, 60);
//    wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
//    MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]);
//    if (AdEle.isDisplayed()) {
//        logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
//    }
}
    public static void AddingLocation() throws Exception{
    	Ad.findElementByAccessibilityId("Search").click();
        //Ad.findElementByName("Search").click();
   
        Ad.findElementById("com.weather.Weather:id/search_text").sendKeys("New York City");
        List<WebElement> location=Ad.findElementsById("com.weather.Weather:id/search_item_container");
        location.get(0).click();
        Thread.sleep(3000);
    }
    public static void SwipeUp_Counter_healthModules_submodules_() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=6 ;i++){

			Swipe();


//			Boolean b=verifyElement(By.id("Health & Activities"));
//			if(b==true)
//			{
//				logStep("Health & Activities page is presented on the screen");		
//				Ad.findElementById("com.weather.Weather:id/hourly_more").click();
//			//	logStep("clicked the hourly page link");
//			//	AppiumFunctions.Check_submodules_Hourly_ad();
//				//Ad.findElementByClassName("android.widget.ImageButton").click();
//				Thread.sleep(5000);
//
//
//				break;
			}
//			else
//			{
//				System.out.println("Module is not present scroll down");
//			}



		}
    public static void SwipeUp_Counter_hc_module() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=7 ;i++){

			Swipe();


//			Boolean b=verifyElement(By.id("Health & Activities"));
//			if(b==true)
//			{
//				logStep("Health & Activities page is presented on the screen");		
//				Ad.findElementById("com.weather.Weather:id/hourly_more").click();
//			//	logStep("clicked the hourly page link");
//			//	AppiumFunctions.Check_submodules_Hourly_ad();
//				//Ad.findElementByClassName("android.widget.ImageButton").click();
//				Thread.sleep(5000);
//
//
//				break;
			}
//			else
//			{
//				System.out.println("Module is not present scroll down");
//			}



		}
    public static void SwipeUp_Counter_healthModules_submodules() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=6 ;i++){

			Swipe();


//			Boolean b=verifyElement(By.id("Health & Activities"));
//			if(b==true)
//			{
//				logStep("Health & Activities page is presented on the screen");		
//				Ad.findElementById("com.weather.Weather:id/hourly_more").click();
//			//	logStep("clicked the hourly page link");
//			//	AppiumFunctions.Check_submodules_Hourly_ad();
//				//Ad.findElementByClassName("android.widget.ImageButton").click();
//				Thread.sleep(5000);
//
//
//				break;
			}
//			else
//			{
//				System.out.println("Module is not present scroll down");
//			}



		}
    public static void Swipeapptillend() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=8 ;i++){

			Swipe();


//			Boolean b=verifyElement(By.id("Health & Activities"));
//			if(b==true)
//			{
//				logStep("Health & Activities page is presented on the screen");		
//				Ad.findElementById("com.weather.Weather:id/hourly_more").click();
//			//	logStep("clicked the hourly page link");
//			//	AppiumFunctions.Check_submodules_Hourly_ad();
//				//Ad.findElementByClassName("android.widget.ImageButton").click();
//				Thread.sleep(5000);
//
//
//				break;
			}
//			else
//			{
//				System.out.println("Module is not present scroll down");
//			}



		}
	//}
    public static Boolean verifyElement(By by) {
		try {
			// Get the element using the Unique identifier of the element
			Ad.findElement(by);
		} catch (NoSuchElementException e) {
			// Return false if element is not found
			return false;
		} catch (Exception e) {
			return false;
		}
		// Return true if element is found
		return true;
	}
    public static void click_hourly_element() throws Exception
	{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Hourly Tab")));
		Ad.findElementByAccessibilityId("Hourly Tab").click();
	}
	catch(Exception e) {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Hourly Tab")));
		Ad.findElementByAccessibilityId("Hourly Tab").click();
	}
	}
	public static void click_daily_element() throws Exception
	{
	List<WebElement> ele=Ad.findElementsById("com.weather.Weather:id/icon");
	ele.get(1).click();
	Thread.sleep(2000);
	}
	public static void click_radar_element() throws Exception
	{
	 List<WebElement> ele=Ad.findElementsById("com.weather.Weather:id/icon");
	 ele.get(3).click();
     Thread.sleep(2000);
	}
	
	public static void click_video_element() throws Exception
	{
	List<WebElement> ele=Ad.findElementsById("com.weather.Weather.qa:id/smallLabel");
	ele.get(3).click();
	Thread.sleep(2000);
	}
	
	
	public static void clickOnviewMore() {
	Functions.verifyElement(ByAccessibilityId("View More"));
		try {
		System.out.println("Clicking on View More");
		logStep("Clicking on View More");
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("View More Options Button")));
		Ad.findElementByAccessibilityId("View More Options Button").click();
		Thread.sleep(5000);
		}
		catch(Exception e) {
			
		}
	}
	
	private static By ByAccessibilityId(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void clickOnAboutthisapp() throws Exception {
	//	Functions.verifyElement(ByAccessibilityId("About this App"));
		try {
		System.out.println("Clicking on About this App");
		logStep("Clicking on About this App");
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("About this App")));
		Ad.findElementByAccessibilityId("About this App").click();
		//About this App
	//	Thread.sleep(5000);
		}
		catch(Exception e) {	
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/item_about")));
			Ad.findElementById("com.weather.Weather:id/item_about").click();
		Thread.sleep(5000);
		}
	}
	
	

	public static void clickOnVersionnumber() throws Exception {
try {
		//Thread.sleep(15000);
		Functions.verifyElement(By.id("com.weather.Weather:id/test_mode_settings"));
			//Thread.sleep(15000);
			System.out.println("Clicking on test mode settings");
			logStep("Clicking on test mode settings");
			//Thread.sleep(15000);
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/test_mode_settings")));
			Ad.findElementById("com.weather.Weather:id/test_mode_settings").click();
			//Thread.sleep(5000);		
}
catch(Exception e) {
	System.out.println("Clicking on BuildNumber till test mode settings option is displaying");
	logStep("Clicking on BuildNumber till test mode settings option is displaying");	
	for(int i=1;i<10;i++) {
		 //Thread.sleep(7000);
		 new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/about_version")));
		Ad.findElementById("com.weather.Weather:id/about_version").click();
		 //Thread.sleep(6000);
	}
}
	}
	
	public static void clickOntestmodesettings() throws Exception {
		try {
			Functions.verifyElement(By.id("com.weather.Weather:id/test_mode_settings"));
			if(Ad.findElementById("com.weather.Weather:id/test_mode_settings").isDisplayed())
				//Thread.sleep(5000);
				System.out.println("Clicking on test mode settings");
				logStep("Clicking on test mode settings");
			//Thread.sleep(5000);
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/test_mode_settings")));
				Ad.findElementById("com.weather.Weather:id/test_mode_settings").click();
			}
	
		catch(Exception e) {
			
		}
	}
	
	
	public static void clickOnAirlock() throws Exception {
		
 	//clicking on Airlock
		try {
			Functions.verifyElement(By.id("android:id/title"));
System.out.println("Clicking on Airlock");
logStep("Clicking on Airlock");
//Thread.sleep(15000);
		 List<WebElement> all=Ad.findElementsById("android:id/title");
		// Thread.sleep(15000);
		 for(WebElement Airlock:all) {
			if( Airlock.getAttribute("text").equalsIgnoreCase("Airlock")) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Airlock));
				Airlock.click();
			//	 Thread.sleep(15000);
				 break;
			 }
		 }
		}
		catch(Exception e) {
			Functions.verifyElement(By.className("android.widget.LinearLayout"));
			 List<WebElement> all=Ad.findElementsByClassName("android.widget.LinearLayout");
			 for(WebElement Airlock:all) {
				if( Airlock.getAttribute("text").contains("Airlock")) {
					new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Airlock));
					 Airlock.click();
					// Thread.sleep(5000);
					 break;
		}
			 }
	}
	}
	
	public static void clickOnUserGroups() throws Exception {
		System.out.println("Clicking on User Groups");
		logStep("Clicking on User Groups");
		 //Thread.sleep(15000);
			Functions.verifyElement(By.id("android:id/title"));
		List<WebElement> all=Ad.findElementsById("android:id/title");
		 for(WebElement Airlock:all) {
			if( Airlock.getAttribute("text").equalsIgnoreCase("User Groups")) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Airlock));
				 Airlock.click();
			//	 Thread.sleep(5000);
				 break;
			 }
		 }
	}
	
	public static void clickOnBranch() throws Exception {
		System.out.println("Clicking on Branch");
		logStep("Clicking on Branch");
		Functions.verifyElement(By.id("android:id/title"));
		List<WebElement> all=Ad.findElementsById("android:id/title");
		 for(WebElement Airlock:all) {
			if( Airlock.getAttribute("text").equalsIgnoreCase("Branch")) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Airlock));
				 Airlock.click();
				// Thread.sleep(5000);
				 break;
			 }
		 }
	}
	
	public static void enterRequiredUserGroup(String usergroup) throws Exception {
	
		System.out.println("entering "+ usergroup);
		logStep("entering "+ usergroup);
	//	 Thread.sleep(15000);
			Functions.verifyElement(By.id("com.weather.Weather:id/search_bar"));
		Ad.findElementById("com.weather.Weather:id/search_bar").sendKeys(usergroup);
      Thread.sleep(15000);
      try {
    		 //Thread.sleep(15000);
    			Functions.verifyElement(By.id("android:id/text1"));
      List<WebElement> all=Ad.findElementsById("android:id/text1");
 	// Thread.sleep(5000);
		 for(WebElement req:all) {
			if( req.getAttribute("text").equalsIgnoreCase(usergroup)) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(req));
			//	 Thread.sleep(15000);
				 req.click();
				Thread.sleep(15000);
				 break;
			 }
		 }
      }
      catch(Exception e){
    	  Functions.verifyElement(By.className("android.widget.CheckedTextView"));
    	  List<WebElement> all=Ad.findElementsByClassName("android.widget.CheckedTextView");
    	//  Thread.sleep(15000);
 		 for(WebElement req:all) {
 			if( req.getAttribute("text").equalsIgnoreCase(usergroup)) {
 				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(req));
 				 req.click();
 				 
 				// Thread.sleep(15000);
 				 break;
      }
 		 }
      }
	}
	
	
	
	
	
public static void gettingApkVersion() throws Exception{
		
		//cliking View more Button	
	try {
     	clickOnviewMore();
     	attachScreen();
	}
	catch(Exception e) {
	 	clickOnviewMore();
	 	attachScreen();
	}
	try {
     	//cliking on aboutthisapp
     	clickOnAboutthisapp();
     	attachScreen();
	}
	catch(Exception e) {
		//cliking on aboutthisapp
     	clickOnAboutthisapp();
     	attachScreen();
	}
	try {
     	apkVersion=Ad.findElementById("com.weather.Weather:id/about_version").getText();
	}
	catch(Exception e) {
		apkVersion=Ad.findElementById("com.weather.Weather:id/about_version").getText();
	}
     	System.out.println("Android apk build number ::  "+apkVersion);
     apkVersion= apkVersion.split("Version:")[1].trim();
     
     	System.out.println(apkVersion);
    	AppiumFunctions.clickOnBackArrowElement();
     	FileOutputStream fos=new FileOutputStream(new File(System.getProperty("user.dir") + "/JenkinsEmailConfig.Properties"));
     	properties.setProperty("AppVersion", apkVersion);
     	properties.store(fos," App Version read from app and updated");
    fos.close();
     	
	}



	
public static void selectingRequiredUserGroup(String usergroup) throws Exception{
		
		//cliking View more Button		
     	clickOnviewMore();
     	//cliking on aboutthisapp
     	clickOnAboutthisapp();
     	
     	 clickOnVersionnumber();
     	clickOntestmodesettings() ;
     	clickOnAirlock();
     	clickOnUserGroups();
     	enterRequiredUserGroup(usergroup);
	}


public static void selectingRequiredUserGroups(String usergroup) throws Exception{
	
 	enterRequiredUserGroup(usergroup);
}


public static void enablingBranch(String branchName) throws Exception{
	
	//cliking View more Button		
 	clickOnviewMore();
 	//cliking on aboutthisapp
 	clickOnAboutthisapp();
 	 clickOnVersionnumber();
 	clickOntestmodesettings() ;
 	clickOnAirlock();
 	clickOnBranch();
 	seletingRequiredBranch(branchName);
 	Ad.runAppInBackground(10);
}

public static void swipeforbranch() {

	
	TouchAction ta=new TouchAction(Ad);
	  Ad.swipe(769, 1764, 613, 694,3000);	
	
}
public static void seletingRequiredBranch(String branchName) throws Exception {

	Thread.sleep(15000);
	for(int i=0;i<50;i++) {
		swipeforbranch();
	List<WebElement> branch=Ad.findElementsById("android:id/text1");
	//System.out.println(branch.size());
	for( WebElement daily:branch) {
		//System.out.println(daily.getText().toString());
		if(daily.getText().toString().equalsIgnoreCase(branchName))			
		{
			 System.out.println(branchName +" is displayed on the screen");
				logStep(branchName +" is displayed on the screen");
				
			daily.click();
			daily.click();
			Thread.sleep(5000);
			System.out.println(daily.getText() + "  enabled");
			i=51;
			break;		
	}
		
	//
	}
	
	}


}
	
	




public static void enable_adstestadunit() throws Exception {
		//click the user groups 
		 List<WebElement> abouthisapp=Ad.findElementsById("android:id/title");
		 Thread.sleep(4000);
			abouthisapp.get(0).click();	
			Thread.sleep(4000);
			//enetr the adstest ad unit in search box
			try{
			Ad.findElementById("com.weather.Weather:id/search_bar").sendKeys("Adstest");
			Thread.sleep(2000);
			}
			catch(Exception e) {
				Ad.findElementByClassName("android.widget.EditText").sendKeys("Adstest");
				
				Thread.sleep(5000);
			}
			try {
			//click the ads test adunit only
			List<WebElement> adstestadunitonly=Ad.findElementsById("android:id/text1");
			Thread.sleep(15000);
			adstestadunitonly.get(0).click();
			Thread.sleep(5000);
			}
			catch(Exception e) {
				//click the ads test adunit only
				List<WebElement> adstestadunitonly=Ad.findElementsByClassName("android.widget.CheckedTextView");
				Thread.sleep(15000);
				adstestadunitonly.get(0).click();
				Thread.sleep(15000);
			}
			try {
			Ad.findElementById("com.weather.Weather:id/search_bar").sendKeys("Adstest");
			Thread.sleep(2000);
			}
			catch(Exception e) {
				Ad.findElementByClassName("android.widget.EditText").sendKeys("Adstest");
				
				Thread.sleep(5000);
			}
		
	}



public static void ScreenShot(String Adtype,String ScreenType) throws Exception{
	String ScreenShot = System.getProperty("user.dir")+"/Screenshots";
	if(ScreenType.equals("Passed")){

		File Screenshot = ((TakesScreenshot)Ad).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(Screenshot, new File(ScreenShot+"/"+"/ScreenShot_"+ScreenType+" "+Adtype+".png"));
	}else{
		File Screenshot = ((TakesScreenshot)Ad).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(Screenshot, new File(ScreenShot+"/"+"/ScreenShot_"+ScreenType+" "+Adtype+".png"));
		FileUtils.copyFile(Screenshot, new File(ScreenShot+"/Failed/ScreenShot_"+ScreenType+" "+Adtype+".png"));

	}
}

public static void enter_requiredLocation(String location) throws Exception {
	new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Search")));	
Ad.findElementByAccessibilityId("Search").click();
new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.visibilityOfElementLocated(By.id("com.weather.Weather:id/search_text")));	
Ad.findElementById("com.weather.Weather:id/search_text").sendKeys(location);
new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/title")));	
List<WebElement> allLocations=Ad.findElementsById("com.weather.Weather:id/title");

allLocations.get(0).getText();
System.out.println(allLocations.size());

for(int i=0;i<=allLocations.size();i++) {


	if(location.contains("New York City")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("New York City")) {
			Thread.sleep(6000);
			allLocations.get(i).click();
		Thread.sleep(6000);
		break;
		}
	}
	

	if(location.contains("07095")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("Woodbridge Township")) {
			Thread.sleep(6000);
			allLocations.get(i).click();
			Thread.sleep(6000);
		break;
		}
	}
	
	if(location.contains("10005")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("New York City")) {

			allLocations.get(i).click();
		Thread.sleep(3000);
		break;
		}
	}

		
}
}

}
		










