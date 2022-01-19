package twc.Automation.HandleWithAppium;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;



import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.SmokeTestCases.smokeTestCases;
import twc.Automation.General.DeviceStatus;
import twc.Automation.General.Functions;
import twc.Automation.HandleWithCharles.CharlesFunctions;

public class AppiumFunctions extends Drivers{
	static int startY;
	static int endY;
	static LinkedHashSet<String> lhs;
    static boolean integratedAd = false;
	static boolean isIntegratedCheckDone = false;
	static boolean isHourlyCardCheckDone = false;
	static boolean ccCardSwiped = false;
	static int adCard = 1;
	static MobileElement globalcurrentCard = null;
	static MobileElement globalprevCard = null;
	public static String current_IPAddress = null;
	public static String defaultPortNumber = "8222";
	private static AppiumServiceBuilder builder;
	private static  AppiumDriverLocalService service;
	public static void Swipe_Up() throws Exception{
		Thread.sleep(2000);
		Dimension dimensions = Ad.manage().window().getSize();
		Double startY1 = dimensions.getHeight() * 0.7;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/6);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		System.out.println("endY  - "+endY);
		System.out.println("startY  - "+startY);
		Ad.swipe(0, endY, 0, startY,2000);
		Thread.sleep(2000);
	}
	public static void killADB() throws IOException, Exception{
		
	String[] command ={"/usr/bin/killall","-KILL","adb"};
	Runtime.getRuntime().exec(command); 
	String[] command1 ={"/usr/bin/killall","-KILL","-9 adb"}; 
	Runtime.getRuntime().exec(command1);
	Thread.sleep(5000);
	String[] command2 ={"/bin/sh", "-c", "adb start-server"}; 
	Runtime.getRuntime().exec(command2);
	}
	
	public static void UnInstallApp() throws Exception{
		
		
		String[] str1 = {"aswinikumar","adb uninstall com.weather.Weather"};
		Process p1 = Runtime.getRuntime().exec(str1);
	
	}
	
	public static void AppiumServerStop() throws InterruptedException{

		//		String[] command ={"/usr/bin/killall","-KILL","node"};  
		//
		//		try {
		//			Runtime.getRuntime().exec(command);
		//		} catch (IOException e) {
		//			System.out.println("Appium Server Not Yet Killed At This Time");
		//		}  
		String[] command = new String[]{"sh", "-c", "lsof -P | grep ':4726' | awk '{print $2}' | xargs kill -9"};
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(5000);
	}
	
	public static void clearTWCLogs() throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] paths = read_excel_data.exceldataread("Paths");
		System.out.println("Clear Logcat Logs for TWC App");	
		String[] clearLogcatdata ={"/bin/bash", "-c",  paths[15][Cap]+" logcat -c"};
		Runtime.getRuntime().exec(clearLogcatdata);	
		Thread.sleep(4000);
	}
	public static void clickOnNext() throws Exception {
		try {
		Ad.findElementById("com.weather.Weather:id/ok_button").click();
		}
		catch(Exception e) {
			Ad.findElementByName("").click();
		}
		finally {
	     
		}
		Thread.sleep(2000);
	}
	public static void clickOnUnderstandbutton() throws Exception {
		try {
		Ad.findElementById("com.weather.Weather:id/next_button_text").click();
		
		Thread.sleep(2000);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static void AppiumServerStart() throws InterruptedException{
		
		CommandLine command = new CommandLine("/usr/local/bin/studio");
		command.addArgument("/Applications/Appium.app/Contents/Resources/app/node_modules/appium/bin/appium.js", false);
		//		CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");
		//		command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js", false);


		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument("4727");	
		command.addArgument("--no-reset", false);
		command.addArgument("--log-level", false);
		command.addArgument("error");

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			Thread.sleep(5000);
			System.out.println("appium server started");
		} catch (ExecuteException e) {
			System.out.println("Appium Server Not Yet Started");
		} catch (IOException e) {
			System.out.println("Appium Server Not Yet Started");
		}
	}
	
	public static void ReLaunchApp() throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		clearTWCLogs();
		
		String[][] paths = read_excel_data.exceldataread("Paths");
		String adbPath = paths[15][Cap];
		
		String[] str ={"/bin/bash", "-c", adbPath+" shell pm disable com.weather.Weather"};
		Runtime.getRuntime().exec(str);
		Thread.sleep(2000);
		
		String[] str1 ={"/bin/bash", "-c", adbPath+" shell pm enable com.weather.Weather"};
		Runtime.getRuntime().exec(str1);
		
		Ad.closeApp();
		Ad.launchApp();
	}
	
	public static void clearCache() throws Exception{
		Thread.sleep(3000);
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] paths = read_excel_data.exceldataread("Paths");
		String adbPath = paths[15][Cap];
		
		String[] str ={"/bin/bash", "-c", adbPath+" shell pm clear com.weather.Weather"};
		Runtime.getRuntime().exec(str);
		System.out.println("Cleared The App Data Successfully");
		Thread.sleep(2000);
		
	}
	
	//Kill and relaunch the app
	public static void Kill_launch() throws Exception{
		System.out.println("doing kill and launch the app");
		
		try{
			Thread.sleep(5000);
			Ad.closeApp();	
			Thread.sleep(5000);
			Ad.launchApp();  
			
			try{
				if((Ad.findElementById("com.weather.Weather:id/next_button_text").isDisplayed())){
					Ad.findElementById("com.weather.Weather:id/next_button_text").click();
					Thread.sleep(3000);
				}
			}catch(Exception e){
				System.out.println("Location already set");
			}
			
		}
		catch(Exception e){
			try {
				Thread.sleep(5000);
				Ad.closeApp();
			
				Thread.sleep(5000);
				Ad.launchApp();
				try{
					if((Ad.findElementById("com.weather.Weather:id/next_button_text").isDisplayed())){
						Ad.findElementById("com.weather.Weather:id/next_button_text").click();
						Thread.sleep(3000);
					}
				}catch(Exception e6){
					System.out.println("Location already set");
				}
			}
			catch(Exception e1) {
				try {
					Thread.sleep(5000);
					Ad.closeApp();
				
					Thread.sleep(5000);
					Ad.launchApp();
					try{
						if((Ad.findElementById("com.weather.Weather:id/next_button_text").isDisplayed())){
							Ad.findElementById("com.weather.Weather:id/next_button_text").click();
							Thread.sleep(3000);
						}
					}catch(Exception e7){
						System.out.println("Location already set");
					}
				}
				
				catch(Exception e5) {
					
				}
			}
			
			

		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void LaunchApp() throws InterruptedException, IOException{
			
//			killADB();
//			AppiumServerStop();
//			AppiumServerStart();
			
			logStep("Launch The App");
			DeviceStatus device_status = new DeviceStatus();
			int Cap = device_status.Device_Status();
			
			try {
				
				String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
				DesiredCapabilities capabilities = new DesiredCapabilities();
				
				/* --- Start Android Device Capabilities --- */
				if(Cap == 2){
				capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
				capabilities.setCapability(capabilitydata[2][0], capabilitydata[2][Cap]); 
				capabilities.setCapability(capabilitydata[3][0], capabilitydata[3][Cap]);
				capabilities.setCapability(capabilitydata[7][0], capabilitydata[7][Cap]); 
				capabilities.setCapability(capabilitydata[10][0],capabilitydata[10][Cap]);
				capabilities.setCapability(capabilitydata[12][0],capabilitydata[12][Cap]);
				capabilities.setCapability(capabilitydata[13][0],capabilitydata[13][Cap]);
				capabilities.setCapability(capabilitydata[14][0],capabilitydata[14][Cap]);
				Ad = new AndroidDriver(new URL(capabilitydata[15][Cap]), capabilities);
				}
				/* ---End Android Device Capabilities --- */
				Ad.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

				try{
					if((Ad.findElementByName("OK")).isDisplayed()){
						Ad.findElementByName("OK").click();
					}else if((Ad.findElementByName("Continue")).isDisplayed()){
						Ad.findElementByName("Continue").click();
					}else if((Ad.findElementByName("Allow")).isDisplayed()){
						Ad.findElementByName("Allow").click();
					}
				}catch(Exception e){

				}
				
				System.out.println("Capabilities have been launched");
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println("Unable To Launch The Appium Capabilities");
			}
	}

	
	//Handle popups after launch the app
	public static void After_launch(){
		try{
			if((Ad.findElementByName("Allow")).isDisplayed()){
				Thread.sleep(3000);
				Ad.findElementByName("Allow").click();
			}
		}catch(Exception e){
			System.out.println("Location already set");
		}
		try{
				if((Ad.findElementByName("OK")).isDisplayed()){
					Ad.findElementByName("OK").click();
				}else if((Ad.findElementByName("Continue")).isDisplayed()){
					Ad.findElementByName("Continue").click();
				}else if((Ad.findElementByName("Allow")).isDisplayed()){
					Ad.findElementByName("Allow").click();
				}
			}catch(Exception e){

			}
		
		try{
			Thread.sleep(3000);
			
			if(Ad.findElementByName("Please Search For A Location").isDisplayed()){
				System.out.println("Address not found, Entering Manually");
				Ad.findElementByName("Search").click();
				Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.view.ViewGroup[1]/android.support.v7.widget.LinearLayoutCompat[1]").sendKeys("30339");
				Thread.sleep(8000);
				Ad.tap(1, 380, 265, 1000);
				Thread.sleep(5000);
				
			}
			
		}catch(Exception e){
			
		}
		
	}
	
	
	//launch app with full reset
	
	
	@SuppressWarnings("rawtypes")
	public static void LaunchAppWithFullReset(String Reset) throws Exception{
		
		
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		try {
			
			String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			/* --- Start Android Device Capabilities --- */
			if(Cap == 2){
			capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
			capabilities.setCapability(capabilitydata[2][0], capabilitydata[2][Cap]); 
			capabilities.setCapability(capabilitydata[3][0], capabilitydata[3][Cap]);
			capabilities.setCapability(capabilitydata[7][0], capabilitydata[7][Cap]); 
			//capabilities.setCapability(capabilitydata[9][0], Reset);
			capabilities.setCapability("appActivity","com.weather.Weather.app.SplashScreenActivity");
			capabilities.setCapability("automationName","UiAutomator2");
			capabilities.setCapability(capabilitydata[10][0],capabilitydata[10][Cap]);
			capabilities.setCapability(capabilitydata[12][0],capabilitydata[12][Cap]);
			capabilities.setCapability(capabilitydata[13][0],capabilitydata[13][Cap]);
			capabilities.setCapability(capabilitydata[14][0],capabilitydata[14][Cap]);
			Thread.sleep(10000); 
			Ad = new AndroidDriver(new URL(capabilitydata[15][Cap]), capabilities);
			Thread.sleep(10000); 
			}
			/* ---End Android Device Capabilities --- */
			Ad.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		//	Thread.sleep(20000);
			After_launch();
			
			
			System.out.println("Capabilities have been launched  with fullreset with - "+Reset);
		//	Thread.sleep(5000);
		} 
			catch (Exception e) {
			System.out.println("Unable To Launch The Appium Capabilities");
		}
	}
	
     	@SuppressWarnings("rawtypes")
	   public static void LaunchAppWithFullReset() throws Exception{
    		
     		/*killADB();
     		AppiumServerStop();
     			AppiumServerStart();*/
     			
     			DeviceStatus device_status = new DeviceStatus();
     			int Cap = device_status.Device_Status();
     			
//     		try {  		
     				String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
     				DesiredCapabilities capabilities = new DesiredCapabilities();
     				
     				/* --- Start Android Device Capabilities --- */
     				if(Cap == 2){
     				//	capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
     					capabilities.setCapability(capabilitydata[2][0], capabilitydata[2][Cap]); 
     					capabilities.setCapability(capabilitydata[3][0], capabilitydata[3][Cap]);
     					capabilities.setCapability(capabilitydata[7][0], capabilitydata[7][Cap]); 
     					capabilities.setCapability(capabilitydata[9][0], capabilitydata[9][Cap]);
     				//	capabilities.setCapability(capabilitydata[10][0],capabilitydata[10][Cap]);
     					capabilities.setCapability(capabilitydata[12][0],capabilitydata[12][Cap]);
     					capabilities.setCapability("noReset","true");
     		capabilities.setCapability("appActivity","com.weather.Weather.app.SplashScreenActivity");
     			//	capabilities.setCapability("appActivity","com.weather.android.daybreak.MainActivity");
     			//	capabilities.setCapability("automationName","UiAutomator2");
     			//	capabilities.setCapability("enabled","chromedriver_autodownload");
     				//	System.out.println("app : "+capabilitydata[10][Cap]);
     					capabilities.setCapability(capabilitydata[13][0],capabilitydata[13][Cap]);
     					//capabilities.setCapability(capabilitydata[14][0],capabilitydata[14][Cap]);
     					
     					Thread.sleep(10000);
     				
     					Ad = new AndroidDriver(new URL(capabilitydata[15][Cap]), capabilities);
     					Thread.sleep(15000);
     					
     					/* ---End Android Device Capabilities --- */
     	    			Ad.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
     	    			//####added ths to handle allow button			
     	    			attachScreen();
     	    			clickONNext();
     	    			attachScreen();
     	    		    ClickonIUnderstand();
     	    		   attachScreen();
     	        		clickOnAllow();		
     	        		attachScreen();
     	        		Clickonclosebutton();
     	        		attachScreen();
     	    			Thread.sleep(10000);
     	    			System.out.println("Capabilities have been launched  with fullreset ");
     				}
     			
     			
     		//	} 
     			
     			//catch (Exception e) {
     				//System.out.println("Unable To Launch The Appium Capabilities");
     			//}
     		}
     	
     	
     	
     	
     	
     	
     	
     	public static void WelcomeAppLaunchCooredinates()
     	{
     		
     		try {
     		Ad.swipe(1275, 1848, 181, 1874,2000);
     		}
     		catch(Exception e) {
     			Ad.swipe(1275, 1848, 181, 1874,2000);
     		}
     		try {
         		Ad.swipe(1301, 1853, 227, 1910,2000);
         		}
         		catch(Exception e) {
         			Ad.swipe(1301, 1853, 227, 1910,2000);
         		}
     		try {
     			Ad.findElementById("com.weather.Weather.qa:id/ok_button").click();
     		}
     		catch(Exception e) {
     			Ad.findElementByName("Let's Go!").click();
     		}
     		
     	}
     	

     	public static void clickOnVideoElement() throws Exception{
    		System.out.println("clicking video element");
    		logStep("clicking video element");
    		try {
    			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/ok_button")));
    		Ad.findElementById("com.weather.Weather:id/ok_button").click();	
    	//	Thread.sleep(5000);
    	//	Thread.sleep(10000);
    		}
    		catch(Exception e) {
    			try {
    			SwipeUp_Counter(1);
    			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/ok_button")));
    			Ad.findElementById("com.weather.Weather:id/ok_button").click();	
    			//Thread.sleep(5000);
    			System.out.println("video element clicked");
    			logStep("video element clicked");
    			//Thread.sleep(10000);
    			}
    			catch(Exception e1) {
    				try {
    					new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/video_backdrop")));
    					Ad.findElementById("com.weather.Weather:id/video_backdrop").click();
    					//Thread.sleep(5000);
    					System.out.println("video element clicked");
    	    			logStep("video element clicked");
			}
    				catch(Exception e2) {
    					new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/video_player_thumbnail_extra")));
    					Ad.findElementById("com.weather.Weather:id/video_player_thumbnail_extra").click();	
    					System.out.println("video element clicked");
    	    			logStep("video element clicked");
    					 }
    			}
    			
    		}
    		
    	}
    	
public static void clickOnMaps()  throws Exception{
	try {
	System.out.println("clicking on maps");
	logStep("clicking on maps");
	Ad.findElementByAccessibilityId("Radar Tab").click();
	Thread.sleep(3000);
	}
	catch(Exception e) {
		System.out.println("maps element not found");
		logStep("maps element not found");
	}
}
public static void clickOnRadarMaps() throws Exception{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/details_button")));
				Ad.findElementById("com.weather.Weather:id/details_button").click();	
				System.out.println("maps element clicked");
    			logStep("maps element clicked");
				}
				catch(Exception e) {
					try {
						SwipeUp_Counter(1);
						new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/details_button")));
						Ad.findElementById("com.weather.Weather:id/details_button").click();	
						System.out.println("maps element clicked");
		    			logStep("maps element clicked");
					}
					catch(Exception e1) {
						new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/radar_map_image")));
						Ad.findElementById("com.weather.Weather:id/radar_map_image").click();
						System.out.println("maps element clicked");
		    			logStep("maps element clicked");
					}
				}
}	
public static void click_Todaydetails_element() throws Exception
{
		
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/today_card_container")));
		Ad.findElementById("com.weather.Weather:id/today_card_container").click();
		System.out.println("today details  element clicked");
		logStep("today details element clicked");
	
	}
	catch(Exception e) {
		try {
			SwipeUp_Counter(1);
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/details_button")));
			Ad.findElementById("com.weather.Weather:id/details_button").click();
			System.out.println("today details  element clicked");
			logStep("today details element clicked");
		}
		catch(Exception e1) {
			try {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/details_button")));
				Ad.findElementById("com.weather.Weather:id/details_button").click();
				System.out.println("today details  element clicked");
				logStep("today details element clicked");
			}
			catch(Exception e3) {
				try{
					new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/today_card_container")));
						Ad.findElementById("com.weather.Weather:id/today_card_container").click();
						System.out.println("today details  element clicked");
						logStep("today details element clicked");
			}
			catch(Exception e4) {
	
			}
		
}
		}
	}
}

public static void click_SH_element() throws Exception
{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/card_dial_index1_item")));
		Ad.findElementById("com.weather.Weather:id/card_dial_index1_item").click();
		System.out.println("outdoor details element clicked");
		logStep("outdoor details  element clicked");
	}
	catch(Exception e) {
		try {
			Swipe_feed();
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/card_dial_index2_item")));
	 Ad.findElementById("com.weather.Weather:id/card_dial_index2_item").click();
		System.out.println("outdoor details element clicked");
		logStep("outdoor details  element clicked");
		}
		catch(Exception e1) {
			try {
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/card_dial_index4_item")));
			Ad.findElementById("com.weather.Weather:id/card_dial_index4_item").click();
			System.out.println("outdoor details element clicked");
			logStep("outdoor details  element clicked");
			}
			catch(Exception e3) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/card_dial_index6_item")));
				Ad.findElementById("com.weather.Weather:id/card_dial_index6_item").click();
				System.out.println("outdoor details element clicked");
				logStep("outdoor details  element clicked");
			}
		}
	}
}

public static void click_MoreNews_element() throws Exception
{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/video_player_thumbnail_extra")));
		Ad.findElementById("com.weather.Weather:id/video_player_thumbnail_extra").click();
		Thread.sleep(10000);
		System.out.println("More News element clicked");
		logStep("More News element clicked");
	}
	catch(Exception e) {
		try {
			Swipe_feed();
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/video_player_thumbnail_extra")));
	 Ad.findElementById("com.weather.Weather:id/video_player_thumbnail_extra").click();
	 Thread.sleep(10000);
		System.out.println("More News element clicked");
		logStep("More News element clicked");
		}
		catch(Exception e1) {
			
		}
		
}
}


public static void click_cold_Flu_element() throws Exception
{
	try {
	new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_cold_flu")));
Ad.findElementById("com.weather.Weather:id/health_activities_cold_flu").click();
System.out.println("Cold&Flu element clicked");
logStep("Cold&Flu  element clicked");
}
catch(Exception e) {
	try {
		SwipeUp_Counter(1);
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_cold_flu")));
		Ad.findElementById("com.weather.Weather:id/health_activities_cold_flu").click();
		System.out.println("Cold&Flu element clicked");
		logStep("Cold&Flu  element clicked");
	}
	catch(Exception e1) {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_cold_flu")));
		Ad.findElementById("com.weather.Weather:id/health_activities_cold_flu").click();
		System.out.println("Cold&Flu element clicked");
		logStep("Cold&Flu  element clicked");
	}
}	
}
public static void click_Running_element() throws Exception
{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_run")));
		Ad.findElementById("com.weather.Weather:id/health_activities_run").click();
		System.out.println("Running element clicked");
		logStep("Running  element clicked");
		}
		catch(Exception e) {
			try {
				Swipe_feed();
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_run")));
				Ad.findElementById("com.weather.Weather:id/health_activities_run").click();
				System.out.println("Running element clicked");
				logStep("Running  element clicked");
			}
			catch(Exception e1) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_run")));
				Ad.findElementById("com.weather.Weather:id/health_activities_run").click();
				System.out.println("Running element clicked");
				logStep("Running  element clicked");
			}
		}	
}

public static void click_hourly_element() throws Exception
{
	try {
	Ad.findElementByAccessibilityId("Hourly").click();
	Thread.sleep(2000);
	}
	catch(Exception e) {
	
	Thread.sleep(2000);
	}
}
public static void click_Boat_Beach_element() throws Exception
{
		
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_boat_beach")));
		Ad.findElementById("com.weather.Weather:id/health_activities_boat_beach").click();	
		System.out.println("Boat&Beach element clicked");
		logStep("Boat&Beach  element clicked");
	}
	catch(Exception e) {
		try {		
			
			SwipeUp_Counter(1);
			Thread.sleep(6000);
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_boat_beach")));
		Ad.findElementById("com.weather.Weather:id/health_activities_boat_beach").click();
		System.out.println("Boat&Beach element clicked");
		logStep("Boat&Beach  element clicked");
		}
		catch(Exception e1) {
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_boat_beach")));
			Ad.findElementById("com.weather.Weather:id/health_activities_boat_beach").click();
			System.out.println("Boat&Beach element clicked");
			logStep("Boat&Beach  element clicked");
		}
	}
}
public static void click_Allergy_element() throws Exception
{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_allergy")));
		Ad.findElementById("com.weather.Weather:id/health_activities_allergy").click();	
		System.out.println("Allergy element clicked");
		logStep("Allergy  element clicked");
	}
	catch(Exception e) {
		try {	
			Swipe_feed();
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_allergy")));
		Ad.findElementById("com.weather.Weather:id/health_activities_allergy").click();
		System.out.println("Allergy element clicked");
		logStep("Allergy  element clicked");
		}
		catch(Exception e1) {
			SwipeUp_Counter(1);
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/health_activities_allergy")));
			Ad.findElementById("com.weather.Weather:id/health_activities_allergy").click();
			System.out.println("Allergy element clicked");
			logStep("Allergy  element clicked");
		}
	}		
}
public static void click_Airpollution_element() throws Exception
{
	
	//com.weather.Weather:id/call_to_action_button
	//com.weather.Weather:id/air_quality_dial
	//com.weather.Weather:id/air_quality_pollutant_title
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/call_to_action_button")));
		Ad.findElementById("com.weather.Weather:id/call_to_action_button").click();
		System.out.println("Air Quality element clicked");
		logStep("Air Quality element clicked");
	}
	catch(Exception e) {
		try {
			SwipeUp_Counter(1);
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/call_to_action_button")));
			Ad.findElementById("com.weather.Weather:id/call_to_action_button").click();
			System.out.println("Air Quality element clicked");
			logStep("Air Quality element clicked");
		}
		catch(Exception e1) {
			try {
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/air_quality_dial")));
			Ad.findElementById("com.weather.Weather:id/air_quality_dial").click();
			System.out.println("Air Quality element clicked");
			logStep("Air Quality element clicked");
			}
			catch(Exception e3) {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/air_quality_pollutant_title")));
				Ad.findElementById("com.weather.Weather:id/air_quality_pollutant_title").click();
				System.out.println("Air Quality element clicked");
				logStep("Air Quality element clicked");
			}
		}
		
	}
}
public static void click_news_element() throws Exception
{
	Ad.findElementById("com.weather.Weather:id/video_backdrop").click();
	Thread.sleep(3000);		
}

public static void click_hurricane_element() throws Exception
{
	try {
	Ad.findElementById("com.weather.Weather:id/swipe_refresh_layout").click();
	Thread.sleep(3000);		
	}
	catch(Exception e) {
		
	}
}

public static void clickonAlertsCenter() throws Exception
{
	
System.out.println("clicking on alerts center icon");
logStep("clicking on alerts center icon");
	try {
	Ad.findElementByAccessibilityId("Go to Alerts and Notifications").click();
	Thread.sleep(3000);		
	}
	catch(Exception e) {
		
	}
}
public static void SwipeUp_Counter(int Counter) throws Exception{

	int swipeup = Counter;
//System.out.println("swipeup");
	for(int i=1;i<=swipeup ;i++){

		Swipe();
		Thread.sleep(5000);
		//Swipe();
	}
}

public static void SwipeUp_Counter_feedcards(int Counter) throws Exception{int radarCount=0;
int MorNewsCount=0;
int TodayDeatilsCount=0;
int HealthActivities=0;
int  RunningCount=0;
int BoatBeachCount=0;
int AllergyCount=0;
int CofFluCount=0;
int videoCount=0;
int  outdoorcount=0;
int AirQualityCount=0;

String[][] data = read_excel_data.exceldataread("General");
String copyRight = null;

int swipeup = Counter;
//System.out.println("swipeup");
System.out.println("Scroll the app till"+ data[1][1] +" is displaying on the screen");
Thread.sleep(5000);
clickonAlertsCenter();
attachScreen();
AppiumFunctions.clickOnBackArrowElement();

for(int i=1;i<=swipeup ;i++){
	Swipe_feed();
	Thread.sleep(1000);

	if(i>15) {
		if(Functions.verifyElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView"))){	
		if(Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").isDisplayed()){
			 copyRight = Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").getAttribute("text");
			if(copyRight.equalsIgnoreCase( data[1][1])) {
					System.out.println(copyRight +" text is displaying on the screen");
					i=150;
					break;	
		}
		}
	}
	}
	
	Thread.sleep(5000);
	String ModuleName;
	Thread.sleep(5000);

	try {
	if(Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed()) {
		try {
ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getAttribute("text");
		}
catch(Exception e) {
ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getText();
}
//System.out.println(ModuleName.toString() +" feed card is presented on the screen");

/*if(ModuleName.toString().contains("Top Stories") ||ModuleName.toString().contains("Low Stories") || ModuleName.toString().contains("Videos")) {
	if(videoCount==0) {
	AppiumFunctions.clickOnVideoElement();
AppiumFunctions.clickOnBackArrowElement();
//Functions.closeInterstailads() ;
videoCount=1;
	}
	}*/

/*if(ModuleName.toString().contains("More News")) {
	if(MorNewsCount==0) {
	AppiumFunctions.clickOnVideoElement();
AppiumFunctions.clickOnBackArrowElement();
Functions.closeInterstailads() ;
MorNewsCount=1;
	}
	}*/
	

if(ModuleName.toString().contains("Maps") ||ModuleName.toString().contains("Thunderstorms possible") || ModuleName.toString().contains("Thunderstorms ending") || ModuleName.toString().contains("Thunderstorms starts")||ModuleName.toString().contains("Dry conditions") || ModuleName.toString().contains("Thunderstorms ending") || ModuleName.toString().contains("Thunderstorms starts")  || ModuleName.toString().contains("Rain possible") || ModuleName.toString().contains("Thunderstorms likely") ||  ModuleName.toString().contains("Rain ending") ||  ModuleName.toString().contains("Thunderstorms likely")) {
//System.out.println(ModuleName.toString() +" feed card is presented on the screen");
if(radarCount==0)
{
AppiumFunctions.clickOnRadarMaps();
attachScreen();
AppiumFunctions.clickOnBackArrowElement();
//Functions.closeInterstailads();
radarCount=1;
}
}

if(ModuleName.toString().contains("Today's Details") ) {
if(TodayDeatilsCount==0) {
AppiumFunctions.click_Todaydetails_element();
attachScreen();
 AppiumFunctions.clickOnBackArrowElement_today();
 Thread.sleep(10000);
 TodayDeatilsCount=1;
}
}

if(ModuleName.toString().contains("Air Quality")) {
if(AirQualityCount==0) {
AppiumFunctions.click_Airpollution_element();
attachScreen();
AppiumFunctions.clickOnBackArrowElement();
Thread.sleep(10000);
AirQualityCount=1;
}
}

if(ModuleName.toString().contains("Outdoor Conditions")) {
if( outdoorcount==0)
click_SH_element() ;
attachScreen();
AppiumFunctions.clickOnBackArrowElement();
Thread.sleep(10000);
outdoorcount=1;

}

if(ModuleName.toString().contains("More News")) {
if( MorNewsCount==0)
click_MoreNews_element();
attachScreen();
AppiumFunctions.clickOnBackArrowElement();
Thread.sleep(10000);
MorNewsCount=1;

}

	
	}
}
	

	catch(Exception e) {
		try {
			Swipe_feed();
		Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
		}
		catch(Exception e1) {
			Swipe_feed();
		}

		
	}
	}}
public static void Swipe(){
	Dimension dimensions = Ad.manage().window().getSize();//throwing exception
	Double startY1 = dimensions.getHeight() * 0.5;  
	startY = startY1.intValue();
	Double endY1 = (double) (dimensions.getHeight()/20);  //  dimensions.getHeight()  0.2;  == 512.0
	endY = endY1.intValue();
	Ad.swipe(0, startY, 0, endY,2000);

}

public static void Swipe_feed(){
	Dimension dimensions = Ad.manage().window().getSize();//throwing exception
	Double startY1 = dimensions.getHeight() * 0.4;  
	startY = startY1.intValue();
	Double endY1 = (double) (dimensions.getHeight()/20);  //  dimensions.getHeight()  0.2;  == 512.0
	endY = endY1.intValue();
	Ad.swipe(0, startY, 0, endY,2000);


}
public static void clickOnBackArrowElement() throws Exception
{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Navigate up")));
		Ad.findElementByAccessibilityId("Navigate up").click();

	}
	catch(Exception e) {
		try {
			new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Navigate up")));
			Ad.findElementByAccessibilityId("Navigate up").click();
		}
		catch(Exception e1) {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/fake_toolbar_back_button")));
	     Ad.findElementById("com.weather.Weather:id/fake_toolbar_back_button").click();
		}
	}
}


public static void clickOnBackArrowElement_trending() throws Exception
{
	try {
		
	//	Navigate up
     Ad.findElementById("com.weather.Weather:id/trending_up_navigation_icon").click();
     Thread.sleep(5000);
	}
	catch(Exception e) {
		Ad.findElementByAccessibilityId("Navigate up").click();
		Thread.sleep(5000);
	}
}
public static void clickONNext() throws Exception{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/ok_button")));
		Ad.findElementById("com.weather.Weather:id/ok_button").click();
	Thread.sleep(5000);
	}
	catch(Exception e) {
		
	}
}
public static void clickOnAllow() throws Exception{
try {
	new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.android.permissioncontroller:id/permission_allow_foreground_only_button")));
	Ad.findElementById("com.android.permissioncontroller:id/permission_allow_foreground_only_button").click();
	Thread.sleep(5000);
}
catch(Exception e) {
	
}
}
public static void clickOnSearch() throws Exception{
Ad.findElementByAccessibilityId("Search").click();
	Thread.sleep(10000);
}
public static void Enterlocation() throws Exception{
Ad.findElementByAccessibilityId("com.weather.Weather:id/txt_location_name").sendKeys("New York");
	Thread.sleep(10000);
}

public static void ClickonIUnderstand() throws Exception{
try {
	new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/next_button_text")));
	Ad.findElementById("com.weather.Weather:id/next_button_text").click();
	Thread.sleep(5000);
}
catch(Exception e)
{
	
}
}

public static void Clickonclosebutton() throws Exception{
try {
	new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("android:id/button2")));
	Ad.findElementById("android:id/button2").click();
	Thread.sleep(5000);
}
catch(Exception e)
{
	
}
}

public static void click_snapShot_button() throws Exception
{
	try {
		Ad.findElementById("com.weather.Weather:id/snapshot_art").click();
	Thread.sleep(38000);
	}
	catch(Exception e) {
	Ad.findElementById("com.weather.Weather:id/snapshot_background").click();
	Thread.sleep(38000);
	}		
}

public static void SwipeUp_Counter_BNcard(int Counter) throws Exception{
	
	int radarCount=0;
	int MorNewsCount=0;
	int TodayDeatilsCount=0;
	int HealthActivities=0;
	int  RunningCount=0;
	int BoatBeachCount=0;
	int AllergyCount=0;
	int CofFluCount=0;
	int videoCount=0;
	int  outdoorcount=0;
	int AirQualityCount=0;
	
	
	
	

	String[][] data = read_excel_data.exceldataread("General");
	//System.out.println("Copy right text is: " + data[1][1]);


	String copyRight = null;
	
	int swipeup = Counter;
//System.out.println("swipeup");
	System.out.println("Scroll the app till"+ data[1][1] +" is displaying on the screen");
	for(int i=1;i<=swipeup ;i++){
		Swipe_feed();
		Thread.sleep(5000);

		if(i>10) {
			//local
			if(Functions.verifyElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView"))){
		//	server
			//if(Functions.verifyElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView"))){
			if(Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").isDisplayed()){
	//	if(text==true) {
				//local
				 copyRight = Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").getAttribute("text");
			//server
			//	copyRight = Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").getAttribute("text");
				if(copyRight.equalsIgnoreCase( data[1][1])) {
						System.out.println(copyRight +" text is displaying on the screen");
						i=150;
						break;	
			}
			}
		}
		}
		
		Thread.sleep(5000);
		String ModuleName;
		Thread.sleep(5000);

		try {
		if(Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed())
			
		
		break;{
			try {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getAttribute("text");
			}
catch(Exception e) {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getText();
}
	System.out.println(ModuleName.toString() +" feed card is presented on the screen");
	
	/*if(ModuleName.toString().contains("Top Stories") ||ModuleName.toString().contains("Low Stories") || ModuleName.toString().contains("Videos")) {
		if(videoCount==0) {
		AppiumFunctions.clickOnVideoElement();
	AppiumFunctions.clickOnBackArrowElement();
	Functions.closeInterstailAds();
	videoCount=1;
		}
		}*/
	
	/*if(ModuleName.toString().contains("More News")) {
		if(MorNewsCount==0) {
		AppiumFunctions.clickOnVideoElement();
	AppiumFunctions.clickOnBackArrowElement();
	Functions.closeInterstailads() ;
	MorNewsCount=1;
		}
		}*/
		
	

if(ModuleName.toString().contains("Breaking News")) {


	
		 AppiumFunctions.clickOnVideoElement();
		 System.out.println("Breaking news card is displayed on screen");
          logStep("Breaking news card is displayed on screen");
	}



}



}
		

		catch(Exception e) {
			try {
				Swipe_feed();
			Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
			}
			catch(Exception e1) {
				try {
				Swipe_feed();
				Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
				}
				catch(Exception e3) {
					Swipe_feed();
				}
			}
	
			
		}
		}
	
}

public static void Swipeforappdeckapk(){
	Dimension dimensions = Ad.manage().window().getSize();//throwing exception
	Double startY1 = dimensions.getHeight() * 0.6;  
	startY = startY1.intValue();
	Double endY1 = (double) (dimensions.getHeight()/30);  //  dimensions.getHeight()  0.2;  == 512.0
	endY = endY1.intValue();
	Ad.swipe(0, 1800, 0, endY,2000);
}
public static void clickOnVideoElementt() throws Exception{
	System.out.println("clicking video element");
	logStep("clicking video element");
	try {
		Ad.findElementByAccessibilityId("Videos Tab").click();
	//Ad.findElementById("com.weather.Weather:id/ok_button").click();	
	Thread.sleep(10000);
	}
	catch(Exception e) {
		try {
			Ad.findElementByAccessibilityId("Videos Tab").click();
		Thread.sleep(10000);
		}
		catch(Exception e1) {

		}
		
	}
}
	
public static void clickOnBackArrowElement_today() throws Exception
{
	try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/trending_up_navigation_icon")));
		Ad.findElementById("com.weather.Weather:id/trending_up_navigation_icon").click();    
	}
	catch(Exception e) {
		try {
		new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/trending_up_navigation_icon")));
		Ad.findElementById("com.weather.Weather:id/trending_up_navigation_icon").click();  
		}catch(Exception e1) {
			try {
				new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Navigate up")));
				Ad.findElementByAccessibilityId("Navigate up").click();

			}
			catch(Exception e3) {
				try {
					new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Navigate up")));
					Ad.findElementByAccessibilityId("Navigate up").click();
				}
				catch(Exception e4) {
			
				}
		}
		}
	}
}
public static void getFeedCardsListAndNavigateToThem(boolean includeDetailsPages) throws Exception {
	lhs = new LinkedHashSet<String>();
	String[][] data = read_excel_data.exceldataread("General");
	System.out.println("Copy right text is: " + data[1][1]);
	resetIntegratedAdParameters();
	String cardName = "HomeScreen";
	int u = 1;
	MobileElement allCards = null;
	List<MobileElement> Cards = null;

	for (int i = 0; i <= 50; i++) {

		// allCards =
		// Ad.findElementByXPath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.weather.Weather:id/home_screen_list_view']");
		// Cards = allCards.findElementsByClassName("android.widget.FrameLayout");
		// Cards =
		// Ad.findElementsByXPath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.weather.Weather:id/home_screen_list_view']/android.widget.FrameLayout");
		Cards = Ad.findElementsByXPath(
				"//androidx.recyclerview.widget.RecyclerView[@resource-id='com.weather.Weather:id/home_screen_list_view']/*");

		if (i == 0) {
			try {

				cardName = Cards.get(1).findElementByXPath(
						"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']/android.widget.TextView[@resource-id='com.weather.Weather:id/header_title']")
					.getAttribute("text");
				//
				
				try {
					Cards.get(1).findElementByXPath("//android.widget.LinearLayout[@resource-id='com.weather.Weather:id/hourly_forecast_container']");
					cardName = "Hourly Forecast";
					//
				}catch(Exception e) {
					try {
						Cards.get(1).findElementByXPath("//android.widget.ImageView[@resource-id='com.weather.Weather:id/radar_map_image']");
						cardName = "Radar & Maps";
					}catch(Exception e1){
						
					}
					
				}
				globalcurrentCard = Cards.get(1);
				// globalprevCard = Cards.get(0);
			} catch (Exception e) {
				try {
					cardName = Cards.get(1).findElementByXPath(
							"//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/ad_card_container']/android.view.ViewGroup/android.widget.TextView[@resource-id='com.weather.Weather:id/ad_card_title']")
							.getAttribute("text");
					globalcurrentCard = Cards.get(1);
					// globalprevCard = Cards.get(0);
					if (cardName.contains("Advertisement")) {
						cardName = cardName + adCard;
						adCard++;
					}
				} catch (Exception e1) {
					try {
						MobileElement covid19 = Cards.get(1).findElementByXPath(
								"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']//android.widget.TextView[@text='See the latest in the new information hub, including county-level tracking for the US. Stay safe.']");
						cardName = "COVID19";
						globalcurrentCard = Cards.get(1);
						// globalprevCard = Cards.get(0);
					} catch (Exception e2) {
						try {
							cardName = Cards.get(1).findElementByXPath(
									"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']//android.widget.Button[@text='See the Forecast']")
									.getAttribute("text");
							globalcurrentCard = Cards.get(1);
							// globalprevCard = Cards.get(0);
							if (cardName.equalsIgnoreCase("See the Forecast")) {
								cardName = "WatsonHolidays";
							}
						} catch (Exception e3) {
							globalcurrentCard = Cards.get(1);
							// globalprevCard = Cards.get(0);
							cardName = "UnKnownCard" + u;
							u++;
						}

					}

				}

			}
		} else {
			try {

				cardName = Cards.get(0).findElementByXPath(
						"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']/android.widget.TextView[@resource-id='com.weather.Weather:id/header_title']")
						.getAttribute("text");
				try {
					Cards.get(0).findElementByXPath("//android.widget.LinearLayout[@resource-id='com.weather.Weather:id/hourly_forecast_container']");
					cardName = "Hourly Forecast";
				}catch(Exception e) {
					try {
						Cards.get(0).findElementByXPath("//android.widget.ImageView[@resource-id='com.weather.Weather:id/radar_map_image']");
						cardName = "Radar & Maps";
					}catch(Exception e1){
						
					}
				}
				globalcurrentCard = Cards.get(0);
			} catch (Exception e) {
				try {
					cardName = Cards.get(0).findElementByXPath(
							"//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/ad_card_container']/android.view.ViewGroup/android.widget.TextView[@resource-id='com.weather.Weather:id/ad_card_title']")
							.getAttribute("text");
					globalcurrentCard = Cards.get(0);
					if (cardName.contains("Advertisement")) {
						cardName = cardName + adCard;
						adCard++;
					}
				} catch (Exception e1) {
					try {
						MobileElement covid19 = Cards.get(0).findElementByXPath(
								"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']//android.widget.TextView[@text='See the latest in the new information hub, including county-level tracking for the US. Stay safe.']");
						cardName = "COVID19";
						globalcurrentCard = Cards.get(0);
					} catch (Exception e2) {
						try {
							cardName = Cards.get(0).findElementByXPath(
									"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']//android.widget.Button[@text='See the Forecast']")
									.getAttribute("text");
							globalcurrentCard = Cards.get(0);
							if (cardName.equalsIgnoreCase("See the Forecast")) {
								cardName = "WatsonHolidays";
							}
						} catch (Exception e3) {
							try {

								cardName = Cards.get(1).findElementByXPath(
										"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']/android.widget.TextView[@resource-id='com.weather.Weather:id/header_title']")
										.getAttribute("text");
								try {
									Cards.get(1).findElementByXPath("//android.widget.LinearLayout[@resource-id='com.weather.Weather:id/hourly_forecast_container']");
									cardName = "Hourly Forecast";
								}catch(Exception b) {
									try {
										Cards.get(1).findElementByXPath("//android.widget.ImageView[@resource-id='com.weather.Weather:id/radar_map_image']");
										cardName = "Radar & Maps";
									}catch(Exception b1){
										
									}
								}
								globalcurrentCard = Cards.get(1);
								globalprevCard = Cards.get(0);
							} catch (Exception e4) {
								try {
									cardName = Cards.get(1).findElementByXPath(
											"//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/ad_card_container']/android.view.ViewGroup/android.widget.TextView[@resource-id='com.weather.Weather:id/ad_card_title']")
											.getAttribute("text");
									globalcurrentCard = Cards.get(1);
									globalprevCard = Cards.get(0);
									if (cardName.contains("Advertisement")) {
										cardName = cardName + adCard;
										adCard++;
									}
								} catch (Exception e5) {
									try {
										MobileElement covid19 = Cards.get(1).findElementByXPath(
												"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']//android.widget.TextView[@text='See the latest in the new information hub, including county-level tracking for the US. Stay safe.']");
										cardName = "COVID19";
										globalcurrentCard = Cards.get(1);
										globalprevCard = Cards.get(0);
									} catch (Exception e6) {
										try {
											cardName = Cards.get(1).findElementByXPath(
													"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']//android.widget.Button[@text='See the Forecast']")
													.getAttribute("text");
											globalcurrentCard = Cards.get(1);
											globalprevCard = Cards.get(0);
											if (cardName.equalsIgnoreCase("See the Forecast")) {
												cardName = "WatsonHolidays";
											}
										} catch (Exception e7) {
											globalcurrentCard = Cards.get(1);
											globalprevCard = Cards.get(0);
											cardName = "UnKnownCard" + u;
											u++;
										}
									}

								}

							}
						}

					}

				}

			}
		}
		
		lhs.add(cardName);
		System.out.println("Cards List is: " + lhs);
		
		getPreviousFeedCardSizeandSwipewithNoMargin();
		
		
		if (cardName.contains("Health & Activities")) {
			cardName = "lifestyle1";
		} else if (cardName.contains("Air Quality")) {
			cardName = "aq1";
		} else if (cardName.contains("Radar & Maps")) {
			cardName = "radar&maps";
		} else if (cardName.contains("Outdoor Conditions")) {
			cardName = "seasonalhub1";
		} else if (cardName.contains("Daily Forecast")) {
			cardName = "daily1";
		} else if (cardName.contains("Typhoon Central")) {
			cardName = "hurricane-central";
		} else if (cardName.contains("Today's Details")) {
			cardName = "today";
		} else if (cardName.contains("Top Stories")) {
			cardName = "video";
		} else if (cardName.contains("More News")) {
			cardName = "news";
		} else if (cardName.contains("Hourly Forecast")) {
			cardName = "Hourly";
		}
		if(includeDetailsPages) {
			try {
				if (cardName.equalsIgnoreCase("lifestyle")) {
					List<MobileElement> ls;
					ls = Ad.findElementsByXPath(
							"//android.widget.LinearLayout[@resource-id='com.weather.Weather:id/list_view']/android.widget.RelativeLayout");

					int lssize = ls.size();
					for (int l = 1; l <= lssize; l++) {
						String subIndex = Ad.findElementByXPath(
								"(//android.widget.LinearLayout[@resource-id='com.weather.Weather:id/list_view']/android.widget.RelativeLayout)["
										+ l + "]//android.widget.TextView[1]")
								.getAttribute("text");

						Ad.findElementByXPath(
								"(//android.widget.LinearLayout[@resource-id='com.weather.Weather:id/list_view']/android.widget.RelativeLayout)["
										+ l + "]")
								.click();
						System.out.println("Current subindex : " + subIndex);
						Thread.sleep(2000);

						// ScreenShot(cardName+" "+l,"Passed");
						// attachScreen();
						AppiumFunctions.clickOnBackArrowElement();
					}
				} else if (cardName.equalsIgnoreCase("seasonalhub")) {
					List<MobileElement> sh;
					sh = Ad.findElementsByXPath(
							"//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/card_content_view']/android.widget.LinearLayout/android.widget.RelativeLayout");
					int shsize = sh.size();
					for (int m = 1; m <= shsize; m++) {
						String subIndex = Ad.findElementByXPath(
								"(//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/card_content_view']/android.widget.LinearLayout/android.widget.RelativeLayout)["
										+ m + "]//android.widget.TextView[1]")
								.getAttribute("text");
						// String
						// subIndex=Ad.findElementByXPath("(((//XCUIElementTypeTable[@name=\"lifestyle_combo_container\"])[1]/XCUIElementTypeCell)["+l+"]/XCUIElementTypeStaticText)[2]").getAttribute("label");

						Ad.findElementByXPath(
								"(//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/card_content_view']/android.widget.LinearLayout/android.widget.RelativeLayout)["
										+ m + "]")
								.click();
						// Ad.findElementByXPath("((//XCUIElementTypeTable[@name=\"lifestyle_combo_container\"])[1]/XCUIElementTypeCell)["+l+"]").click();
						System.out.println("Current subindex : " + subIndex);
						Thread.sleep(2000);
						Thread.sleep(2000);

						// ScreenShot(cardName+" "+m,"Passed");
						// attachScreen();
						AppiumFunctions.clickOnBackArrowElement();
					}
				} /*else if (cardName.equalsIgnoreCase("daily")) {
					MobileElement dailyDetails = (MobileElement) Ad.findElementByXPath(
							"//androidx.recyclerview.widget.RecyclerView[@resource-id='com.weather.Weather:id/daily_forecast_list']/android.widget.LinearLayout[1]");
					// android.widget.LinearLayout[@resource-id='com.weather.Weather:id/daily_forecast_container']//android.widget.LinearLayout[1]
					// androidx.recyclerview.widget.RecyclerView[@resource-id='com.weather.Weather:id/daily_forecast_list']/android.widget.LinearLayout[1]

					// android.widget.LinearLayout[@id='com.weather.Weather:resource-id/daily_forecast_container']//android.widget.LinearLayout[1]

					dailyDetails.click();
					System.out.println("Navigated to Daily Details Page");
					Thread.sleep(2000);
					// ScreenShot(cardName,"Passed");
					// attachScreen();
					AppiumFunctions.clickOnBackArrowElement();
				} */else if (cardName.equalsIgnoreCase("hurricane-central")) {
					MobileElement hcDetails;
					// MobileElement hcDetails =
					// Ad.findElementByAccessibilityId("hurricane-central-card_detailedButton");
					try {
						hcDetails = (MobileElement) Ad
								.findElement(By.id("com.weather.Weather:id/hurricane_central_view_more_button"));
					} catch (Exception e) {
						hcDetails = (MobileElement) Ad
								.findElement(By.id("com.weather.Weather:id/hurricane_card_container"));
					}

					hcDetails.click();
					System.out.println("Navigated to Hurricane Central Details Page");
					Thread.sleep(2000);

					AppiumFunctions.clickOnBackArrowElement();

				} else if (cardName.equalsIgnoreCase("radar&maps")) {

					AppiumFunctions.clickOnRadarMaps();
					System.out.println("Navigated to Radars & Maps Details Page");
					AppiumFunctions.clickOnBackArrowElement();

				} else if (cardName.equalsIgnoreCase("today")) {

					AppiumFunctions.click_Todaydetails_element();
					System.out.println("Navigated to Today Details Page");
					//AppiumFunctions.clickOnBackArrowElement();
					clickOnBackArrowElement_today();

				} else if (cardName.equalsIgnoreCase("video")) {

					clickOnVideoElementt();
				//	AppiumFunctions.clickOnVideoElement();
					System.out.println("Navigated to Video Details Page");
					AppiumFunctions.clickOnBackArrowElement();

				} else if (cardName.equalsIgnoreCase("aq")) {

					AppiumFunctions.click_Airpollution_element();
					System.out.println("Navigated to AQ Details Page");
					AppiumFunctions.clickOnBackArrowElement();

				} else if (cardName.equalsIgnoreCase("news")) {

					AppiumFunctions.click_news_element();
					System.out.println("Navigated to News Details Page");
					AppiumFunctions.clickOnBackArrowElement();

				}

			} catch (Exception e) {

			}
		}
		
		if (i > 10) {
			try {
				String copyRight = Ad.findElementByXPath(
						"//androidx.recyclerview.widget.RecyclerView[@resource-id='com.weather.Weather:id/home_screen_list_view']/android.widget.LinearLayout/android.widget.TextView")
						.getAttribute("text");

				if (copyRight.equalsIgnoreCase(data[1][1])) {

					// attachScreen();
					System.out.println("User done scrolling");
					logStep("User done scrolling till last page");
					break;
				}
			} catch (Exception e) {
				System.out.println("last page not found, Need to scrol till the end");
			}
		}

		/*
		 * if(i==0) { identifyFeedCardAndSwipe();
		 * 
		 * }else {
		 */
		try {
			// scroll_Down();
			// Swipe_For_HalfHalfCard();
			getFeedCardSizeandSwipewithNoMargin();
			// swipe_Up();
			// scroll_Down();
		} catch (Exception e) {

			String[] strcleariProxy = { "/bin/bash", "-c", "killall iproxy xcodebuild XCTRunner" };
			Process proc = Runtime.getRuntime().exec(strcleariProxy);
			int rc = proc.waitFor();
			Assert.fail("Scrolling filed, need to execute test Case again	");
		}
//}

	}
	/*
	 * Iterator<String> itr=lhs.iterator(); while(itr.hasNext()){
	 * System.out.println(itr.next()); }
	 */
	try {
		Ad.findElementByAccessibilityId("Hourly").click();
	} catch (Exception e) {
		System.out.println("Hourly Tab is not displayed");
	}
	Thread.sleep(10000);
	try {
		Ad.findElementById("com.weather.Weather:id/bottom_nav_home_icon").click();
	} catch (Exception e) {
		System.out.println("Home Tab is not displayed");
	}

	System.out.println("Cards List is: " + lhs);
}

public static void getFeedCardSizeandSwipewithNoMargin() {
	MobileElement currentCard = null;
	// boolean ccCardSwiped = false;
	try {
		/*if (!ccCardSwiped) {
			currentCard = (MobileElement) Ad.findElement(By.xpath(
					"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/current_conditions_card_root']"));
			ccCardSwiped = true;
		} else {
			//currentCard = (MobileElement) Ad.findElement(By.xpath("//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']"));
			currentCard = globalcurrentCard;
		}*/
		if (!ccCardSwiped) {
		currentCard = (MobileElement) Ad.findElement(By.xpath(
				"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/current_conditions_card_root']"));
		ccCardSwiped = true;
		} else {
			currentCard = globalcurrentCard;
		}

	} catch (Exception e) {
		//currentCard = (MobileElement) Ad.findElement(By.xpath("//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/ad_card_container']"));
		currentCard = globalcurrentCard;
		//adCard++;
	}
	Dimension d0 = null;
	int globalprevCardHeight = 0;
	
	try {
		d0 = globalprevCard.getSize();
		System.out.println("Previous Card Height is : " + d0.getHeight());
		System.out.println("Previous Card Width is: " + d0.getWidth());
		globalprevCardHeight =  d0.getHeight();
	}catch(Exception e) {
		globalprevCardHeight = 0;
	}
	
	Dimension d = currentCard.getSize();
	System.out.println("Height is : " + d.getHeight());
	System.out.println("Width is: " + d.getWidth());

	/*if (d.getHeight() < 50) {
		try {
			if (!ccCardSwiped) {
			currentCard = (MobileElement) Ad.findElement(By.xpath(
					"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/current_conditions_card_root']"));
			ccCardSwiped = true;
		} else {
			//currentCard = (MobileElement) Ad.findElement(By.xpath("//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout']"));
			currentCard = globalcurrentCard;
		}
		currentCard = (MobileElement) Ad.findElement(By.xpath(
				"//android.view.ViewGroup[@resource-id='com.weather.Weather:id/current_conditions_card_root']"));

		} catch (Exception e) {
			//currentCard = (MobileElement) Ad.findElement(By.xpath("(//android.view.ViewGroup[@resource-id='com.weather.Weather:id/base_card_view_root_layout'])[2]"));
			currentCard = globalcurrentCard;
		}
		Dimension d1 = currentCard.getSize();
		System.out.println("Height is : " + d1.getHeight());
		System.out.println("Width is: " + d1.getWidth());

		// Dimension dimensions = Ad.manage().window().getSize();//throwing exception
		int startx1 = d1.width / 2;
		Double startY11 = d1.getHeight() * 0.00;
		startY = startY11.intValue();
		Double endY11 = (double) ((d1.getHeight()+globalprevCardHeight) * 0.95); // dimensions.getHeight() 0.2; == 512.0
		endY = endY11.intValue();

		if (endY < 200) {
			Ad.swipe(0, endY + 610, 0, startY + 600, 2000);
		} else {
			Ad.swipe(0, endY, 0, startY, 2000);
		}
	}*/ //else {
		// Dimension dimensions = Ad.manage().window().getSize();//throwing exception
		int startx = d.width / 2;
		Double startY1 = d.getHeight() * 0.00;
		startY = startY1.intValue();
		Double endY1 = (double) ((d.getHeight()+globalprevCardHeight) * 0.90); // dimensions.getHeight() 0.2; == 512.0
		endY = endY1.intValue();

		if (endY < 200) {
			Ad.swipe(0, endY + 610, 0, startY + 600, 2000);
		} else {
			Ad.swipe(0, endY, 0, startY, 2000);
		}
	//}

}

public static void getPreviousFeedCardSizeandSwipewithNoMargin() {
	MobileElement currentCard = null;
	
	Dimension d0 = null;
	int globalprevCardHeight = 0;
	
	try {
		d0 = globalprevCard.getSize();
		System.out.println("Previous Card Height is : " + d0.getHeight());
		System.out.println("Previous Card Width is: " + d0.getWidth());
		globalprevCardHeight =  d0.getHeight();
	}catch(Exception e) {
		globalprevCardHeight = 0;
	}
	
	if(globalprevCardHeight>0) {
		int startx = d0.width / 2;
		Double startY1 = d0.getHeight() * 0.00;
		startY = startY1.intValue();
		Double endY1 = (double) (globalprevCardHeight * 0.90); // dimensions.getHeight() 0.2; == 512.0
		endY = endY1.intValue();

		if (endY < 200) {
			Ad.swipe(0, endY + 610, 0, startY + 600, 2000);
		} else {
			Ad.swipe(0, endY, 0, startY, 2000);
		}
	}
	
}

public static void navigateToAllCardsByCardSize() throws Exception {
	lhs = new LinkedHashSet<String>();
	String[][] data = read_excel_data.exceldataread("General");
	System.out.println("Copy right text is: " + data[1][1]);
	resetIntegratedAdParameters();

	for (int i = 0; i <= 50; i++) {
//System.out.println(i);
		if (i > 10) {
			try {
				String copyRight = Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").getAttribute("text");
						
			//	/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView

		//	System.out.println(copyRight);
				if (copyRight.equalsIgnoreCase(data[1][1])) {

					// attachScreen();
					System.out.println("User done scrolling");
					logStep("User done scrolling till last page");
					break;
				}
			} catch (Exception e) {
				System.out.println("last page not found, Need to scrol till the end");
			}
		}

		try {
			//getFeedCardSizeandSwipewithMargin();
			// scroll_Down();
			Swipe_For_HalfCard();
			// swipe_Up();
			// scroll_Down();
		} catch (Exception e) {

			String[] strcleariProxy = { "/bin/bash", "-c", "killall iproxy xcodebuild XCTRunner" };
			Process proc = Runtime.getRuntime().exec(strcleariProxy);
			int rc = proc.waitFor();
			Assert.fail("Scrolling filed, need to execute test Case again	");
		}

	}
	/*
	 * Iterator<String> itr=lhs.iterator(); while(itr.hasNext()){
	 * System.out.println(itr.next()); }
	 */

	try {
		Ad.findElementById("com.weather.Weather:id/bottom_nav_home_icon").click();
	} catch (Exception e) {
		System.out.println("Home Tab is not displayed");
	}

}

public static void resetIntegratedAdParameters() {
	integratedAd = false;
	isIntegratedCheckDone = false;
	isHourlyCardCheckDone = false;
}

public static void Swipe_For_HalfCard() {
	Dimension dimensions = Ad.manage().window().getSize();// throwing exception
	int startx = dimensions.width / 2;
	Double startY1 = dimensions.getHeight() * 0.44;
	startY = startY1.intValue();
	Double endY1 = (double) (dimensions.getHeight() / 70); // dimensions.getHeight() 0.2; == 512.0
	endY = endY1.intValue();
	Ad.swipe(startx, startY, startx, endY, 2000);

}


public static void SwipeUp_Counter_video_maps_feedcards(int Counter) throws Exception{
	int swipeup = Counter;
//System.out.println("swipeup");
	for(int i=1;i<=swipeup ;i++){
		Swipe();
		String ModuleName;
		try {
		if(Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed()) {
			try {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getAttribute("text");
			}
catch(Exception e) {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getText();
}
	System.out.println(ModuleName.toString() +" feed card is presented on the screen");
	
	if(ModuleName.toString().contains("Top Stories") ||ModuleName.toString().contains("Low Stories")) {
		
		AppiumFunctions.clickOnVideoElement();
	AppiumFunctions.clickOnBackArrowElement();
		}
if(ModuleName.toString().contains("Maps") ||ModuleName.toString().contains("Thunderstorms possible") || ModuleName.toString().contains("Thunderstorms ending") || ModuleName.toString().contains("Thunderstorms starts")||ModuleName.toString().contains("Dry conditions")) {
AppiumFunctions.clickOnRadarMaps();
	AppiumFunctions.clickOnBackArrowElement();
	i=50;
	break;
	
}
		}
		}
		catch(Exception e) {
			try {
			Swipe();
			Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
			}
			catch(Exception e1) {
				Swipe();
			}
	
			
		}
			}	
}

public static void SwipeUp_Counter_Daily_feedcard(int Counter) throws Exception{
	int swipeup = Counter;
//System.out.println("swipeup");
	for(int i=1;i<=swipeup ;i++){
		Swipe();
		String ModuleName;
		try {
		if(Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed()) {
			try {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getAttribute("text");
			}
catch(Exception e) {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getText();
}
	System.out.println(ModuleName.toString() +" feed card is presented on the screen");
	
	if(ModuleName.toString().contains("Daily Forecast")) {
		Swipe();	
		i=50;
		break;		
		}
		}
		}
		catch(Exception e) {
			try {
			Swipe();
			Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
			}
			catch(Exception e1) {
				Swipe();
			}
	
			
		}
			}	
}


public static void getIpaddress() {
	Process p;
	try {
		File bashFile = new File(System.getProperty("user.dir") + "/getIpAddress.sh");
		String[] cmd = { "/bin/sh", bashFile.getName() };

		p = Runtime.getRuntime().exec(cmd);

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String s;
		while ((s = reader.readLine()) != null) {
			System.out.println("Current IP Address of the system is : " + s);
			logStep("Current IP Address of the system is : " + s);
			current_IPAddress = s;
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("There is an exception while finding current MAC IP Address");
		e.printStackTrace();
	}

}


public static void settingProxyEnable(String type,String ipAddress,String portNumber) throws Exception{
		//clicking connection option on settings app
	clickRequiredEleemntononSettingsapp("Connections");	
	Thread.sleep(8000);
	//clicking wifi option
	clickRequiredEleemntononSettingsapp("Wi-Fi");
	Thread.sleep(8000);
	//clicking on wifi name
	clickRequiredEleemntononSettingsapp(smokeTestCases.CurrentWifiName);		
	Thread.sleep(10000);
	
	//clicking on edit
	try {
	Ad.findElementById("android:id/button2").click();
	Thread.sleep(10000);
	}catch(Exception e) {
		
	}
	//clicking on advaced option
	try {
		Ad.findElementById("com.android.settings:id/wifi_advanced_togglebox").click();
		Thread.sleep(10000);
	}catch(Exception e) {
		
	}    		
	
	//clicking on proxy fields drop down
	Ad.findElementById("com.android.settings:id/proxy_settings").click();
	Thread.sleep(10000);
	//clicking on None option;
	 Thread.sleep(10000);
	 clickRequiredproxyOption(type);
	Thread.sleep(8000);
	enterIpAddressPortNumber(current_IPAddress, defaultPortNumber); 
	Ad.findElementById("android:id/button1").click();
	Thread.sleep(8000);
	}

public static void clickRequiredEleemntononSettingsapp(String name) throws Exception {
	List<WebElement> searchelements=Ad.findElementsById("android:id/title");
	for(WebElement search:searchelements) {
		if(search.getText().contains(name)) {
			search.click()  ; 				
			Thread.sleep(5000);

			break;
			
		}
	}
}

public static void clickRequiredproxyOption(String proxyOption) throws Exception {
	List<WebElement> searcheleme=Ad.findElementsById("android:id/text1");
	for(WebElement search:searcheleme) {
		if(search.getText().contains(proxyOption)) {
			search.click();
			Thread.sleep(5000);
			break;
		}
	}
	
}


public static void enterIpAddressPortNumber(String ipAddress, String portNumber) throws Exception {
	
	Ad.findElementById("com.android.settings:id/proxy_hostname").clear();
	Thread.sleep(10000);
	Ad.findElementById("com.android.settings:id/proxy_hostname").sendKeys(ipAddress);
	Ad.findElementById("com.android.settings:id/proxy_port").clear();
	Thread.sleep(10000);
	Ad.findElementById("com.android.settings:id/proxy_port").sendKeys(portNumber);
}

@SuppressWarnings("rawtypes")
public static void LaunchSettingsAppWithFullReset() throws Exception{
		
	//killADB();
	//	AppiumServerStop();
	//	AppiumServerStart();
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
//	try {  			
		//File app = new File(“/Users/aswinikumar/Downloads/e449d6f73bc1b22577ce2eeebe6083887a4ec8bb729eef9d650517b602f88aef.apk”);
			//String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			/* --- Start Android Device Capabilities --- */

				//capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
				capabilities.setCapability("platformName", "Android"); 
				capabilities.setCapability("platformVersion", "11.0");
				capabilities.setCapability("deviceName", "Samsung"); 
				capabilities.setCapability("noReset","true");
			//	capabilities.setCapability("app",app.getAbsolutePath() );
				capabilities.setCapability("appPackage","com.android.settings");
	         capabilities.setCapability("appActivity","com.android.settings.Settings");
			//	capabilities.setCapability("appActivity","dev.firebase.appdistribution.main.MainActivity");
	capabilities.setCapability("automationName","UiAutomator2");  			
				capabilities.setCapability("newCommandTimeout","10000");
				capabilities.setCapability("autoLaunch","true");    				
				Thread.sleep(5000);    				
				Ad = new AndroidDriver(new URL("http://127.0.0.1:4727/wd/hub"), capabilities);
				Thread.sleep(50000);
				  /* ---End Android Device Capabilities --- */
 			Ad.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
 			System.out.println("Capabilities have been launched  with fullreset ");
	
		
		
		//} 
		
		//catch (Exception e) {
			System.out.println("Unable To Launch The Appium Capabilities");
		//}
	}
	


public static void UnInstallApk() throws Exception{
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();

	String[][] paths = read_excel_data.exceldataread("Paths");
	String adbPath = paths[15][Cap];

	//System.out.println("adbPath "+ adbPath);

	System.out.println("Uninstall the APP and Installing");
//	/usr/bin/env bash
	String[] uninstall ={"/usr/bin/env bash", " adb uninstall com.weather.Weather"};
	
	Runtime.getRuntime().exec(uninstall);
	System.out.println("Uninstall completed");
	Thread.sleep(5000);
	Thread.sleep(5000);
}
public static void SwipeUp_Counter_Watsoncards(int Counter) throws Exception{
	
	int radarCount=0;
	int MorNewsCount=0;
	int TodayDeatilsCount=0;
	int HealthActivities=0;
	int  RunningCount=0;
	int BoatBeachCount=0;
	int AllergyCount=0;
	int CofFluCount=0;
	int videoCount=0;
	int  outdoorcount=0;
	int AirQualityCount=0;
	
	
	
	

	String[][] data = read_excel_data.exceldataread("General");
	//System.out.println("Copy right text is: " + data[1][1]);


	String copyRight = null;
	
	int swipeup = Counter;
//System.out.println("swipeup");
	System.out.println("Scroll the app till"+ data[1][1] +" is displaying on the screen");
	for(int i=1;i<=swipeup ;i++){
		Swipe_feed();
		Thread.sleep(5000);

		if(i>10) {
			//local
			if(Functions.verifyElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView"))){
		//	server
			//if(Functions.verifyElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView"))){
			if(Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").isDisplayed()){
	//	if(text==true) {
				//local
				 copyRight = Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").getAttribute("text");
			//server
			//	copyRight = Ad.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout/android.widget.TextView").getAttribute("text");
				if(copyRight.equalsIgnoreCase( data[1][1])) {
						System.out.println(copyRight +" text is displaying on the screen");
						i=150;
						break;	
			}
			}
		}
		}
		
		Thread.sleep(5000);
		String ModuleName;
		Thread.sleep(5000);

		try {
		if(Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed())
			
		
		break;{
			try {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getAttribute("text");
			}
catch(Exception e) {
 ModuleName=Ad.findElementById("com.weather.Weather:id/header_title").getText();
}
	System.out.println(ModuleName.toString() +" feed card is presented on the screen");
	
	/*if(ModuleName.toString().contains("Top Stories") ||ModuleName.toString().contains("Low Stories") || ModuleName.toString().contains("Videos")) {
		if(videoCount==0) {
		AppiumFunctions.clickOnVideoElement();
	AppiumFunctions.clickOnBackArrowElement();
	Functions.closeInterstailAds();
	videoCount=1;
		}
		}*/
	
	/*if(ModuleName.toString().contains("More News")) {
		if(MorNewsCount==0) {
		AppiumFunctions.clickOnVideoElement();
	AppiumFunctions.clickOnBackArrowElement();
	Functions.closeInterstailads() ;
	MorNewsCount=1;
		}
		}*/
		
	

if(ModuleName.toString().contains("Flu")) {


	System.out.println("Flu card presented");
	}

	
/*	if(RunningCount==0) {
		 AppiumFunctions.click_Running_element();
		 AppiumFunctions.clickOnBackArrowElement();
		   Thread.sleep(10000);
		 RunningCount=1;
		}*/
	

	
/*	if(CofFluCount==0) {
	 AppiumFunctions.click_cold_Flu_element();
	  AppiumFunctions.clickOnBackArrowElement();
	   Thread.sleep(10000);
	  Thread.sleep(5000);
	  CofFluCount=1;
	}
	if(BoatBeachCount==0) {
		 AppiumFunctions.click_Boat_Beach_element();
		  AppiumFunctions.clickOnBackArrowElement();
		   Thread.sleep(10000);
		  BoatBeachCount=1;
		}*/

}
if(ModuleName.toString().contains("Allergy") ) {
	System.out.println("Allergy watson card was presented ");
}



if(ModuleName.toString().contains("Weekend")) {
	System.out.println("Weekend card presented on the screen");
 
	}
		

if(ModuleName.toString().contains("PlanaHead")) {
	System.out.println("PlanaHead card presented on the screen");
 
	}
		}

		

		catch(Exception e) {
			try {
				Swipe_feed();
			Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
			}
			catch(Exception e1) {
				try {
				Swipe_feed();
				Ad.findElementById("com.weather.Weather:id/header_title").isDisplayed();
				}
				catch(Exception e3) {
					Swipe_feed();
				}
			}
	
			
		}
		}
	
}

	
	public static void enter_requiredLocation(String location) throws Exception {
	new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementByAccessibilityId("Search")));	
Ad.findElementByAccessibilityId("Search").click();
new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.visibilityOfElementLocated(By.id("com.weather.Weather:id/search_text")));	
Ad.findElementById("com.weather.Weather:id/search_text").sendKeys(location);
Thread.sleep(10000);
new WebDriverWait(Ad, Functions.maxTimeout).until(ExpectedConditions.elementToBeClickable(Ad.findElementById("com.weather.Weather:id/title")));	
List<WebElement> allLocations=Ad.findElementsById("com.weather.Weather:id/title");
Thread.sleep(6000);

//allLocations.get(0).getText();
//System.out.println(allLocations.size());

for(int i=0;i<=allLocations.size();i++) {


	if(location.contains("New York City")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("New York City")) {
			Thread.sleep(6000);
			allLocations.get(i).click();
		Thread.sleep(6000);
		attachScreen();
		break;
		}
	}
	
	if(location.contains("10010")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("New York City")) {
			Thread.sleep(6000);
			allLocations.get(i).click();
		Thread.sleep(6000);
		attachScreen();
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
	if(location.contains("73645")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("Erick")) {
			Thread.sleep(6000);
			allLocations.get(i).click();
			Thread.sleep(6000);
		break;
		}
	}
	
	if(location.contains("30124")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("Cave Spring")) {

			allLocations.get(i).click();
		Thread.sleep(3000);
		break;
		}
	}

	
	if(location.contains("08824")) {
		//System.out.println(loc.getText());
		if(allLocations.get(i).getText().contains("South Brunswick")) {

			allLocations.get(i).click();
		Thread.sleep(3000);
		break;
		}
	}
		
}
}
}
                          

    
                 

                     
