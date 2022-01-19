package twc.Automation.SmokeTestCases;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.qameta.allure.Description;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import twc.Automation.Driver.Drivers;
import twc.Automation.General.DeviceStatus;
import twc.Automation.General.loginModule;
import twc.Automation.HandleMapLocal.MapLocalFunctions;
import twc.Automation.HandleWithApp.AppFunctions;
import twc.Automation.HandleWithAppium.AppiumFunctions;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.HandleWithCharles.CharlesProxy;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;
import twc.Automation.RetryAnalyzer.RetryAnalyzer;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import twc.Automation.Driver.Drivers;
import twc.Automation.General.loginModule;
import twc.Automation.HandleMapLocal.MapLocalFunctions;
import twc.Automation.HandleWithApp.AppFunctions;
import twc.Automation.HandleWithAppium.AppiumFunctions;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.RetryAnalyzer.RetryAnalyzer;
import twc.Automation.General.Functions;
import twc.Automation.General.TwcAndroidBaseTest;
import twc.Automation.General.Utils;

public class smokeTestCases extends  TwcAndroidBaseTest {
//	protected String email;
	public static String CurrentWifiName=null;
	
	private static final String CONFIG_FILE_PATH = "charles_common.config";
	private static final String BN_SEVERE1_CONFIG_FILE_PATH = "BNSevere1charles_common.config";
	private static final String BN_SEVERE2_CONFIG_FILE_PATH = "BNSevere2charles_common.config";
	// public static CharlesProxy proxy;
	public File configFile;
	private CharlesProxy proxy;

	
		@BeforeClass(alwaysRun = true)
	    public void beforeClass() throws Exception {	
		this.configFile = this.charlesGeneralConfigFile(CONFIG_FILE_PATH);
		proxy = new CharlesProxy("localhost", 8333, CONFIG_FILE_PATH);
		proxy.startCharlesProxyWithUI();
		proxy.disableRewriting();
	    proxy.stopRecording();
		proxy.disableMapLocal();
		proxy.startRecording();
		proxy.clearCharlesSession();
		AppiumFunctions.LaunchAppWithFullReset();
		Thread.sleep(90000);
		AppFunctions.gettingApkVersion() ;
	}
	
	/* =======================================all feed ad calls test cases started======================================================================================*/
	
	@Test(priority = 2, enabled = true)
	@Description("Verifying Home screen marquee ad call on FTL")
	public void Smoke_Test_CaseVerify_Homescreen_marquee_adCall_FTL() throws Exception {
		System.out.println(
				"================= verifying iu value for home screen marquee test case started =========================");	
		System.out.println("going to all feed cards and details pages");
		logStep("going to all feed cards and details pages");
	//	CharlesFunctions.archive_folder("Charles");
		AppiumFunctions.SwipeUp_Counter_feedcards(35);
		this.proxy.getXml();
		CharlesFunctions.createXMLFileForCharlesSessionFile();
		Utils.verifyPubadCal("Smoke", "Marquee");
		System.out.println(
				"================= verifying iu value for home screen marquee test case End =========================");
	}
	
	
	  
	  
	  
		@Test(priority = 6, enabled = true)
		@Description("Verify Alert Center ad on My Alerts Page")
		public void Verify_AlertCenterAd() throws Exception {
			System.out.println("==============================================");
			System.out.println("===========================Aleret Center page Adcal iu====================");
			System.out.println("****** Alert Center ad test case Started");
			logStep("****** Alert Center ad test case Started");
			Utils.verifyPubadCal("Smoke", "MyAlerts");
		}



		

		@Test(priority = 8, enabled = true)
		@Description("Verifying feed_1 ad call on FTL")
		public void Smoke_Test_verifying_feed_1adcall_FTL() throws Exception {
			System.out.println("================= Verifying feed_1 ad call tescase Started =========================");
			Utils.verifyPubadCal("Smoke", "Feed1");
			System.out.println("================= Verifying feed_1 ad call tescase End =========================");
		}

		@Test(priority = 10, enabled = true)
		@Description("Verifying feed_2 ad call on FTL")
		public void Smoke_Test_verifying_feed_2adcall_FTL() throws Exception {
			System.out.println("================= Verifying feed_2 ad call tescase Started =========================");
			Utils.verifyPubadCal("Smoke", "Feed2");
			System.out.println("================= Verifying feed_2 ad call tescase End =========================");
		}


		@Test(priority = 12, enabled = true)
		@Description("Verifying feed_3 ad cal on FTL")
		public void Smoke_Test_verifying_feed_3adCall_FTL() throws Exception {
			System.out.println("================= Verifying feed_3 ad call tescase Started =========================");
			System.out.println("================= Verifying feed_2 ad call tescase Started =========================");
			Utils.verifyPubadCal("Smoke", "Feed3");
			System.out.println("================= Verifying feed_3 ad call tescase End =========================");
		}

		@Test(priority = 14, enabled = true)
		@Description("Verifying feed_4 ad call on FTL")
		public void Smoke_Test_verifying_feed_4adcall_FTL() throws Exception {
			System.out.println("================= Verifying feed_4 ad call tescase Started =========================");
			Utils.verifyPubadCal("Smoke", "Feed4");
			System.out.println("================= Verifying feed_4 ad call tescase End =========================");
		}


			@Test(priority = 16, enabled = true)
		@Description("Verifying feed_5 ad call on FTL")
		public void Smoke_Test_verifying_feed_5adCall_FTL() throws Exception {
			System.out.println("================= Verifying feed_5 ad call tescase Started =========================");
			Utils.verifyPubadCal("Smoke", "Feed5");
			System.out.println("================= Verifying feed_5 ad call tescase End =========================");
		}

			
			@Test(priority = 18, enabled = true)
			@Description("Verifying radar&maps details page ad call On FTL")
			public void Smoke_Test_Case_Verify_Radar_Maps_deatailspage_adCall_FTL() throws Exception {
				System.out.println(
						"================= verifying iu value for Radar&Maps deatail card started =========================");
				Utils.verifyPubadCal("Smoke", "Map");
				System.out.println(
						"================= verifying iu value for Radar&Maps deatail card  End =========================");
			}
			
			@Test(priority = 20, enabled = true)
			@Description("Verifying iu value Today details Page on FTL")
			public void Smoke_Test_CaseVerify_Today_details_page_iu_FTL() throws Exception {
				System.out.println(
						"================= verifying iu value for Today_details_page_iu started =========================");
				Utils.verifyPubadCal("Smoke", "Today");
				System.out.println(
						"================= verifying iu value for Today_details_page_iu End =========================");
			}
			
			
			
			@Test(priority = 22, enabled = true)
			@Description("Verifying  Air Quality content page ad call on FTL")
			public void Smoke_Test_Verify_Air_Quality_contentpage_adCall_FTL() throws Exception {
				System.out.println(
						"================= Verifying iu value for Air Quality content page testcase  started =========================");
				Utils.verifyPubadCal("Smoke", "Air Quality(Content)");
				System.out.println(
						"================= Verifying iu value for Air Quality content page testcase  End =========================");
			}
			
			
			
			
			@Test(priority = 38, enabled = true)
			@Description("Verifying  outdoor conditions  detailed page ad call on FTL")
			public void Smoke_Test_Verify_outdoor_detailpage_adcall_FTL() throws Exception {
				System.out
						.println("================= Verifying outdoor detailpage_adcall iu Started =========================");
				Utils.verifyPubadCal("Smoke", "SeasonalHub(Details)");
				System.out.println("================= Verifying outdoor detailpage adcall iu End =========================");
			}

			
	
			 
			 
			 
			 @Test(priority = 44, enabled = true)
				@Description("Verifying amazon Slot Id for feed1 prerol ad call")
				public void Smoke_Test_amazon_aaxSlot_feed1_adCall() throws Exception {
					System.out.println(
							"================= Verify amazon aax slot  Id for feed1 adcall  testcase Started =========================");
					logStep(" Verifying amazon aax slot Id for feed1");
					Utils.verifyAAX_SlotId("Smoke", "Feed1");
					System.out.println(
							"================= Verify amazon aax slot Id for feed1  adcall testcase End =========================");

				}
			 
			 
			 @Test(priority = 46, enabled = true)
				@Description("Verifying amazon  Slot Id for feed2 ad call")
				public void Smoke_Test_amazon_aaxSlot_feed2_adcall() throws Exception {
					System.out.println(
							"================= Verify amazon aax slot  Id for feed2 adcall testcase Started =========================");
					logStep(" Verifying amazon aax slot Id for feed2");
					Utils.verifyAAX_SlotId("Smoke", "Feed2");
					System.out.println(
							"================= Verify amazon aax slot Id for feed2l adcall testcase End =========================");
				}

	
				@Test(priority = 48, enabled = true)
				@Description("Verifying amazon  Slot Id for feed3 ad call")
				public void Smoke_Test_amazon_aaxSlot_feed3_adcall() throws Exception {
					System.out.println(
							"================= Verify amazon aax slot  Id for feed3 adcall testcase Started =========================");
					logStep(" Verifying amazon aax slot Id for feed3");
					Utils.verifyAAX_SlotId("Smoke", "Feed3");
					System.out.println(
							"================= Verify amazon aax slot Id for feed3  adcall testcase End =========================");
				}

				
				 @Test(priority = 50, enabled = true)
					@Description("Verifying amazon  Slot Id for feed4 ad call")
					public void Smoke_Test_amazon_aaxSlot_feed4_adcall() throws Exception {
						System.out.println(
								"================= Verify amazon aax slot  Id for feed4 adcall testcase Started =========================");
						logStep(" Verifying amazon aax slot Id for feed4");
						Utils.verifyAAX_SlotId("Smoke", "Feed4");
						System.out.println(
								"================= Verify amazon aax slot Id for feed4 adcall testcase End =========================");
					}
				 
				 @Test(priority = 51, enabled = true)
					@Description("Verifying amazon  Slot Id for feed5 ad call")
					public void Smoke_Test_amazon_aaxSlot_feed5_adcall() throws Exception {
						System.out.println(
								"================= Verify amazon aax slot  Id for feed5 adcall testcase Started =========================");
						logStep(" Verifying amazon aax slot Id for feed4");
						Utils.verifyAAX_SlotId("Smoke", "Feed5");
						System.out.println(
								"================= Verify amazon aax slot Id for feed5 adcall testcase End =========================");
					}
				 
				 
				 @Test(priority = 52, enabled = true)
					@Description("Verifying amazon Slot Id for maps details preroll ad cal")
					public void Smoke_Test_amazon_aax_mapsdetails_adcall() throws Exception {
						System.out.println("================= Verify amazon aax maps detais adcall Started =========================");
						logStep(" Verifying preload amazon aax for  map details");
						Utils.verifyAAX_SlotId("Smoke", "Map");
						System.out
								.println("================= Verify amazon aax maps card preroll adcall End =========================");
					}

				 
				 @Test(priority =54, enabled = true)
					@Description("Verifying amazon aax for today details card")
					public void Smoke_Test_amazon_aax_today_deatailcard() throws Exception {
						System.out.println(
								"================= Verify amazon aax for today detail adcard Started =========================");
						Utils.verifyAAX_SlotId("Smoke", "Today");
						System.out.println("================= Verify amazon aax for today detail adcard End=========================");
					}
				 
				 
				 @Test(priority = 56, enabled = true)
					@Description("Verifying amazon aax for AQ details page")
					public void Smoke_Test_amazon_aax_Airquality_deatailcard() throws Exception {
						System.out.println(
								"================= Verify amazon aax for Air Quality details page Started =========================");
						Utils.verifyAAX_SlotId("Smoke", "Air Quality(Content)");
						System.out.println("================= Verify amazon aax for Air Quality detail Page  End=========================");
					}
					
				
				 
				 @Test(priority = 64, enabled = true)
					@Description("Verifying amazon slot Id for Outdoor details page")
					public void Smoke_Test_Verify_amazon_SlotId_outdoor_details() throws Exception {
						System.out.println(
								"================= verifying amazon SlotId for oudoor details adcall started =========================");
						Utils.verifyAAX_SlotId("Smoke", "SeasonalHub(Details)");
						System.out.println(
								"================= verifying amazon SlotId for outdoor details adcall  End =========================");
					}	
				 
					@Test(priority = 68, enabled = true)
					@Description("Verifying sz value for Home screen marquee ad call on FTL")
					public void Smoke_Test_CaseVerify_sz_Homescreen_marquee_adCall_FTL() throws Exception {
						System.out.println(
								"================= verifying sz value for home screen marquee test case started =========================");	
						
						Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Marquee", "sz", "360x180");

						System.out.println(
								"================= verifying sz value for home screen marquee test case End =========================");
					}
					
					
					 
					 @Test(priority = 72, enabled = true)
						@Description("Verify sz value for  Alert Center ad on My Alerts Page")
						public void Verify_sz_AlertCenterAd() throws Exception {
							System.out.println("==============================================");
							System.out.println("===========================Alert Center sz value ====================");
							logStep("****** Alert Center sz value test case Started");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "MyAlerts", "sz", "320x50");
						}
					 
					 
					 
						@Test(priority = 74, enabled = true)
						@Description("Verifying sz value for feed_1 ad call on FTL")
						public void Smoke_Test_verifying_sz_feed_1_adcall_FTL() throws Exception {
							System.out.println("================= Verifying sz value feed_1 ad call tescase Started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Feed1", "sz", "300x250%7C320x400");
							System.out.println("================= Verifying sz value feed_1 ad call tescase End =========================");
						}

						
						@Test(priority = 76, enabled = true)
						@Description("Verifying feed_2 ad call on FTL")
						public void Smoke_Test_verifying_sz_feed_2_adcall_FTL() throws Exception {
							System.out.println("================= Verifying sz value feed_2 ad call tescase Started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Feed2", "sz", "300x250");
							System.out.println("================= Verifying sz value  feed_2 ad call tescase End =========================");
						}
					 

						@Test(priority = 78, enabled = true)
						@Description("Verifying feed_3 ad call on FTL")
						public void Smoke_Test_verifying_sz_feed_3_adcall_FTL() throws Exception {
							System.out.println("================= Verifying sz value feed_3 ad call tescase Started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Feed3", "sz", "300x250");
							System.out.println("================= Verifying sz value  feed_3 ad call tescase End =========================");
						}
						
						
						@Test(priority = 80, enabled = true)
						@Description("Verifying feed_4 ad call on FTL")
						public void Smoke_Test_verifying_sz_feed_4_adcall_FTL() throws Exception {
							System.out.println("================= Verifying sz value feed_4 ad call tescase Started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Feed4", "sz", "320x50");
							System.out.println("================= Verifying sz value  feed_4 ad call tescase End =========================");
						}
						@Test(priority = 82, enabled = true)
						@Description("Verifying feed_5 ad call on FTL")
						public void Smoke_Test_verifying_sz_feed_5_adcall_FTL() throws Exception {
							System.out.println("================= Verifying sz value feed_5 ad call tescase Started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Feed5", "sz", "320x50");
							System.out.println("================= Verifying sz value  feed_5 ad call tescase End =========================");
						}
					 
						
						@Test(priority = 84, enabled = true)
						@Description("Verifying sz value for radar&maps details page ad call On FTL")
						public void Smoke_Test_Case_Verify_sz_Radar_Maps_deatailspage_adCall_FTL() throws Exception {
							System.out.println(
									"================= verifying sz  value for Radar&Maps deatail card started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Map", "sz", "320x50");
							System.out.println(
									"================= verifying sz value for Radar&Maps deatail card  End =========================");
						}
						
						@Test(priority = 86, enabled = true)
						@Description("Verifying sz value  for Today details Page on FTL")
						public void Smoke_Test_Case_Verify_sz_Today_details_page_iu_FTL() throws Exception {
							System.out.println(
									"================= verifying iu value for Today_details_page started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Today", "sz", "320x50");
							System.out.println(
									"================= verifying sz value for Today_details_page  End =========================");
						}
						
						
						
						@Test(priority = 88, enabled = true)
						@Description("Verifying  sz value for Air Quality content page ad call on FTL")
						public void Smoke_Test_Verify_sz_AirQuality_contentpage_adCall_FTL() throws Exception {
							System.out.println(
									"================= Verifying sz value for Air Quality content page testcase  started =========================");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Air Quality(Content)", "sz", "320x50");
							System.out.println(
									"================= Verifying sz value for Air Quality content page testcase  End =========================");
						}
						
									
					
						@Test(priority = 98, enabled = true)
						@Description("Verifying  sz value for outdoor conditions  detailed page ad call on FTL")
						public void Smoke_Test_Verify_sz_outdoor_detailpage_adcall_FTL() throws Exception {
							System.out
									.println("================= Verifying sz value for outdoor detailpage ad call Started =========================");
							CharlesFunctions.archive_folder("Charles");
							Utils.validate_Noncustom_param_val_of_gampad("Smoke", "SeasonalHub(Details)", "sz", "320x50");
							System.out.println("================= VVerifying sz value for outdoor detailpage ad call End =========================");
						}
				
				
						/* =======================================hourly details ad calls test cases started======================================================================================*/
						
						@Test(priority = 200, enabled = true)
						@Description("Verifying  hourly detailpage al call on FTL")
						public void Smoke_Test_Verify_hourly_detailpage_adcall_FTL() throws Exception {
							System.out.println("================= Verifying hourly_detailpage_adcall_iu test case  Started =========================");
							proxy.clearCharlesSession();
						   AppFunctions.click_hourly_element();
						   attachScreen();
						   Thread.sleep(10000);
							AppFunctions.Swipe();
							AppFunctions.Swipe();
							AppFunctions.Swipe();
							AppFunctions.Swipe();
							AppFunctions.Swipe();
							AppFunctions.Swipe();
							Thread.sleep(10000);
							Thread.sleep(10000);
							this.proxy.getXml();
						CharlesFunctions.createXMLFileForCharlesSessionFile();
				          Utils.verifyPubadCal("Smoke", "Hourly");
							
							System.out.println("================= Verifying hourly_detailpage_adcall_iu  test case  End =========================");
						}
									
						@Test(priority = 202, enabled = true)
			@Description("Verifying  hourly1  detailpage bigad ad call on FTL")
			public void Smoke_Test_Verify_hourly1_detailpage_bigad_adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying hourly1 big ad detailpage_adcall_iu  test case Started =========================");
				Utils.verifyPubadCal("Smoke", "Hourly1");
				System.out.println(
						"================= Verifying hourly1 big ad detailpage_adcall_iu test case  End =========================");
			}

			@Test(priority = 204, enabled = true)
			@Description("Verifying  hourly2  detailpage bigad ad call on FTL")
			public void Smoke_Test_Verify_hourly2_detailpage_bigad_adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying hourly2 bigad detailpage_adcall_iu test case Started =========================");
				Utils.verifyPubadCal("Smoke", "Hourly2");
				
				System.out.println(
						"================= Verifying hourly2 bigad detailpage_adcall_iu test case End =========================");
			}
			
			@Test(priority = 206, enabled = true)
			@Description("Verifying  hourly3  detailpage bigad ad call on FTL")
			public void Smoke_Test_Verify_hourly3__bigad_detailpage_adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying hourly3 bigad detailpage_adcall_iu test case  Started =========================");
			
				Utils.verifyPubadCal("Smoke", "Hourly3");
				
				System.out.println(
						"================= Verifying hourly3 bigad detailpage_adcall_iu test case  End =========================");
			}
			
			
			@Test(priority = 208, enabled = true)
			@Description("Verifying sz value for  hourly detailpage al call on FTL")
			public void Smoke_Test_Verify_sz_hourly_detailpage_adcall_FTL() throws Exception {
				System.out.println("================= Verifying sz value for hourly detailpage adcall test case  Started =========================");
				CharlesFunctions.createXMLFileForCharlesSessionFile();
				Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Hourly", "sz", "320x50");
				System.out.println("================= Verifying sz value for hourly detailpage adcall test case End =========================");
			}
						
			@Test(priority = 210, enabled = true)
			@Description("Verifying  sz value for hourly1  detailpage bigad ad call on FTL")
			public void Smoke_Test_Verify_sz_hourly1_detailpage_bigad_adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying sz value for hourly1 big ad detailpage adcall  test case Started =========================");
				Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Hourly1", "sz", "300x250");
				System.out.println(
						"================= Verifying sz value for hourly1 big ad detailpage adcall  test case  End =========================");
			}

			@Test(priority = 212, enabled = true)
			@Description("Verifying  sz value hourly2  detailpage bigad ad call on FTL")
			public void Smoke_Test_Verify_sz_hourly2_detailpage_bigad_adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying sz value for hourly2 bigad detailpage_adcall test case Started =========================");
				Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Hourly2", "sz", "300x250");
				System.out.println(
						"================= Verifying sz value for hourly2 bigad detailpage_adcall test case End =========================");
			}
			
			@Test(priority = 214, enabled = true)
			@Description("Verifying  sz value for hourly3  detailpage bigad ad call on FTL")
			public void Smoke_Test_Verify_sz_hourly3__bigad_detailpage_adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying sz value for hourly3 bigad detailpage_adcall test case  Started =========================");
			
				Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Hourly3", "sz", "300x250");
				
				System.out.println(
						"================= Verifying sz value for hourly3 bigad detailpage_adcall test case  End =========================");
			}
							 
							
			
			@Test(priority = 216, enabled = true)
			@Description("Verifying amazon Slot Id for hourly details ad call")
			public void Smoke_Test_amazon_aax_Hourly_details_adcall() throws Exception {
				System.out.println(
						"================= Verify amazon aax Hourly details  adcall test case  Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "Hourly");
				System.out.println("================= Verify amazon aax Hourly details  test case  End =========================");
			}
			

			
			@Test(priority =218, enabled = true)
			@Description("Verifying amazon Slot Id for hourly1 big ad detailspage")
			public void Smoke_Test_amazon_aax_Hourly1_bigaddetails_adcall() throws Exception {
				System.out.println(
						"================= Verify amazon aax Hourly1 details big  adcall  test case Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "Hourly1");
					
				System.out.println("================= Verify amazon aax Hourly1 big ad details  test case  End =========================");

			}

			
			@Test(priority = 220, enabled = true)
			@Description("Verifying amazon Slot Id for hourly2 big ad detailspage")
			public void Smoke_Test_amazon_aax_Hourly2_bigaddetails_adcall() throws Exception {
				System.out.println(
						"================= Verifying  amazon aax Hourly2 details big  adcall  test case Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "Hourly2");
				
				System.out.println("================= Verifying  amazon aax Hourly2  big ad details  test case  End =========================");

			}
			


			@Test(priority = 222, enabled = true)
			@Description("Verifying amazon Slot Id for hourly3 big ad detailspagel")
			public void Smoke_Test_amazon_aax_Hourly3_bigaddetails_adcall() throws Exception {
				System.out.println(
						"================= Verify amazon aax Hourly3 details big  ad call test case  Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "Hourly3");
				System.out.println("================= Verify amazon aax Hourly3 big ad call test case End =========================");
			}
			

			 @Test(priority = 250, enabled = true)
@Description("Verifying new daily details day1 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day1adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying new daily details day1 ad call tescase Started =========================");
	
	proxy.clearCharlesSession();	
	Functions.clickdailydetails();
	attachScreen();
	//Functions.closeInterstailads();
//	Functions.clickongotit();
	Thread.sleep(15000);
	Functions.verifyingdailydetrailsday1today7();
	this.proxy.getXml();
	CharlesFunctions.createXMLFileForCharlesSessionFile();
	
	Utils.verifyPubadCal("Smoke", "DailyDetails");
		
	System.out.println(
			"=================  Verifying new daily details day1 ad call tescase  End =========================");
}
			 
			 /*
			  * This method validates fcnd custom parameter of daily details call
			  */
			  @Test(priority = 251, enabled = true)
			 @Description("Validating 'fcnd' custom parameter of daily details call ")
			 public void Validate_dailydetails_fcnd_Custom_param() throws Exception {
			 	System.out.println("==============================================");
			 	System.out.println("****** Validating fcnd custom parameter of daily details call");
			 	logStep("Validating fcnd custom parameter of daily details call ");
			 	Utils.validate_custom_param_val_of_gampad("Smoke", "DailyDetails", "fcnd", "NotNull");

			 }
			  
			  /*
			   * This method validates fcnd custom parameter of daily details call
			   */
			   @Test(priority = 252, enabled = true)
			  @Description("Validating 'dt' custom parameter of daily details call ")
			  public void Validate_dailydetails_dt_Custom_param() throws Exception {
			  	System.out.println("==============================================");
			  	System.out.println("****** Validating dt custom parameter of daily details call");
			  	logStep("Validating dt custom parameter of daily details call ");
			  	Utils.validate_custom_param_val_of_gampad("Smoke", "DailyDetails", "dt", "NotNull");

			  }

			   /*
			    * This method validates fcnd custom parameter of daily details call
			    */
			    @Test(priority = 253, enabled = true)
			   @Description("Validating 'mnth' custom parameter of daily details call ")
			   public void Validate_dailydetails_mnth_Custom_param() throws Exception {
			   	System.out.println("==============================================");
			   	System.out.println("****** Validating mnth custom parameter of daily details call");
			   	logStep("Validating mnth custom parameter of daily details call ");
			   	Utils.validate_custom_param_val_of_gampad("Smoke", "DailyDetails", "mnth", "NotNull");

			   }

			    
			    /*
			     * This method validates fcnd custom parameter of daily details call
			     */
			     @Test(priority = 254, enabled = true)
			    @Description("Validating 'dynght' custom parameter of daily details call ")
			    public void Validate_dailydetails_dynght_Custom_param() throws Exception {
			    	System.out.println("==============================================");
			    	System.out.println("****** Validating dynght custom parameter of daily details call");
			    	logStep("Validating dynght custom parameter of daily details call ");
			    	Utils.validate_custom_param_val_of_gampad("Smoke", "DailyDetails", "dynght", "NotNull");

			    }

@Test(priority = 255, enabled = true)
@Description("Verifying amazon Slot Id for daily details day1 ad calll")
public void Smoke_Test_amazon_aax_Daily_details_Day1_adcall() throws Exception {
	System.out
			.println("================= Verifying amazon slot id for  daily details day1 adcall test case Started =========================");
	Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
	System.out.println("================= Verifying amazon slot id for  daily details day 1 adcall test case End =========================");
}


@Test(priority = 256, enabled = true)
@Description("Verifying sz value for new daily details day1 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day1adcall_FTL() throws Exception {
System.out.println(
		"================= Verifying sz value for new daily details day1 ad call tescase Started =========================");

Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
System.out.println(
		"=================  Verifying sz value for new daily details day1 ad call tescase  End =========================");
}



@Test(priority = 257, enabled = true)
@Description("Verifying new daily details day2 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day2adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying new daily details day2 ad call test case Started=========================");
CharlesFunctions.archive_folder("Charles");
proxy.clearCharlesSession();
Functions.clickonday2();
attachScreen();
Thread.sleep(10000);
Functions.verifyingdailydetrailsday1today7();
this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");

System.out.println(
	"================= Verifying new daily details day2 ad call test case End =========================");
}

@Test(priority = 258, enabled = true)
@Description("Verifying amazon Slot Id for daily details day2 ad calll")
public void Smoke_Test_amazon_aax_Dailydetails_Day2adcall() throws Exception {
System.out
	.println("================= Verifying amazon slot id for  daily details day2  adcall test case Started =========================");
Utils.verifyAAX_SlotId("Smoke", "DailyDetails");

System.out.println("================= Verifying amazon slot id for  daily details day2 adcall test case End =========================");
}


@Test(priority = 259, enabled = true)
@Description("Verifying sz value for new daily details day2 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day2adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying sz value for new daily details day2 ad call tescase Started =========================");

Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
System.out.println(
	"=================  Verifying sz value for new daily details day2 ad call tescase  End =========================");
}



@Test(priority = 260, enabled = true)
@Description("Verifying new daily details day3 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day3adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying new daily details day3 ad call test case Started =========================");

CharlesFunctions.archive_folder("Charles");
proxy.clearCharlesSession();
//Functions.finding_newDailyBidadcall_day2();
Functions.clickonday3();
attachScreen();
Thread.sleep(10000);
Functions.verifyingdailydetrailsday1today7();
this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");
System.out.println(
	"================= Verifying new daily details day3 ad call test case End =========================");
}

@Test(priority = 261, enabled = true)
@Description("Verifying amazon Slot Id for daily details day3 ad calll")
public void Smoke_Test_amazon_aax_Dailydetails_Day3adcall() throws Exception {
System.out
	.println("================= Verifying amazon slot id for  daily details day3  adcall test case Started =========================");
Utils.verifyAAX_SlotId("Smoke", "DailyDetails");

System.out.println("================= Verifying amazon slot id for  daily details day3 adcall test case End =========================");
}


@Test(priority = 262, enabled = true)
@Description("Verifying sz value for new daily details day3 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day3adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying sz value for new daily details day3 ad call tescase Started =========================");

Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
System.out.println(
	"=================  Verifying sz value for new daily details day3 ad call tescase  End =========================");
}


@Test(priority = 263, enabled = true)
@Description("Verifying new daily details day4 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day4adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying new daily details day4 ad call tescase Started =========================");
CharlesFunctions.archive_folder("Charles");
proxy.clearCharlesSession();
//Functions.finding_newDailyBidadcall_day2();
Functions.clickonday4();
Thread.sleep(10000);
attachScreen();
Functions.verifyingdailydetrailsday1today7();
this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");

System.out.println(
	"================= Verifying new daily details day4 ad call tescase End =========================");
}

@Test(priority = 264, enabled = true)
@Description("Verifying amazon Slot Id for daily details day4 ad calll")
public void Smoke_Test_amazon_aax_Dailydetails_Day4adcall() throws Exception {
System.out
	.println("================= Verifying amazon slot id for  daily details day4  adcall test case Started =========================");
Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
System.out.println("================= Verifying amazon slot id for  daily details day4 adcall test case End =========================");
}


@Test(priority = 265, enabled = true)
@Description("Verifying sz value for new daily details day4 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day4adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying sz value for new daily details day4 ad call tescase Started =========================");

Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
System.out.println(
	"=================  Verifying sz value for new daily details day4 ad call tescase  End =========================");
}



@Test(priority = 266, enabled = true)
@Description("Verifying new daily details day5 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day5adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying new daily details day5 ad call tescase Started =========================");
CharlesFunctions.archive_folder("Charles");
proxy.clearCharlesSession();
//Functions.finding_newDailyBidadcall_day2();
Functions.clickonday5();
Thread.sleep(10000);
attachScreen();
Functions.verifyingdailydetrailsday1today7();
this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");

System.out.println(
	"================= Verifying new daily details day5 ad call tescase End =========================");
}


@Test(priority = 267, enabled = true)
@Description("Verifying amazon Slot Id for daily details day5 ad calll")
public void Smoke_Test_amazon_aax_Dailydetails_Day5adcall() throws Exception {
System.out
	.println("================= Verifying amazon slot id for  daily details day5  adcall test case Started =========================");
Utils.verifyAAX_SlotId("Smoke", "DailyDetails");

System.out.println("================= Verifying amazon slot id for  daily details day5 adcall test case End =========================");
}


@Test(priority = 268, enabled = true)
@Description("Verifying sz value for new daily details day5 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day5adcall_FTL() throws Exception {
System.out.println(
	"================= Verifying sz value for new daily details day5 ad call tescase Started =========================");

Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
System.out.println(
	"=================  Verifying sz value for new daily details day5 ad call tescase  End =========================");
}

@Test(priority = 270, enabled = true)
@Description("Verifying new daily details day6 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day6adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying new daily details day6 ad call tescase Started =========================");
	CharlesFunctions.archive_folder("Charles");
	proxy.clearCharlesSession();
//	Functions.finding_newDailyBidadcall_day2();
	Functions.clickonday6();
	Thread.sleep(10000);
	attachScreen();
	Functions.verifyingdailydetrailsday1today7();
	this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");;
		
	System.out.println(
			"================= Verifying new daily details day6 ad call tescase End =========================");
}


@Test(priority = 272, enabled = true)
@Description("Verifying amazon Slot Id for daily details day6 ad calll")
public void Smoke_Test_amazon_aax_Dailydetails_Day6adcall() throws Exception {
	System.out
			.println("================= Verifying amazon slot id for  daily details day6  adcall test case Started =========================");
	Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
		
	System.out.println("================= Verifying amazon slot id for  daily details day6 adcall test case End =========================");
}

@Test(priority = 274, enabled = true)
@Description("Verifying sz value for new daily details day6 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day6adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying sz value for new daily details day6 ad call tescase Started =========================");

	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
	System.out.println(
			"=================  Verifying sz value for new daily details day6 ad call tescase  End =========================");
}



@Test(priority = 276, enabled = true)
@Description("Verifying new daily details day7 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day7adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying new daily details day7 ad call tescase Started =========================");
	CharlesFunctions.archive_folder("Charles");
	proxy.clearCharlesSession();
//	Functions.finding_newDailyBidadcall_day2();
	Functions.clickonday7();
	Thread.sleep(10000);
	attachScreen();
	Functions.verifyingdailydetrailsday1today7();
	this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");;
	
	System.out.println(
			"================= Verifying new daily details day7 ad call tescase End =========================");
}
      @Test(priority =278, enabled = true)
@Description("Verifying amazon Slot Id for daily details day7 ad calll")
public void Smoke_Test_amazon_aax_Dailydetails_Day7adcall() throws Exception {
	System.out
			.println("================= Verifying amazon slot id for  daily details day7  adcall test case Started =========================");
	Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
		
	System.out.println("================= Verifying amazon slot id for  daily details day7 adcall test case End =========================");
}
      
      @Test(priority = 280, enabled = true)
      @Description("Verifying sz value for new daily details day7 ad call on FTL")
      public void Smoke_Test_sz_Verifying_newdailydetails_day7adcall_FTL() throws Exception {
      	System.out.println(
      			"================= Verifying sz value for new daily details day7 ad call tescase Started =========================");

      	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
      	System.out.println(
      			"=================  Verifying sz value for new daily details day7 ad call tescase  End =========================");
      }
      
      
      
      @Test(priority = 282, enabled = true)
		@Description("Verifying new daily details day8 ad call on FTL")
		public void Smoke_Test_Verifying_newdailydetails_day8adcall_FTL() throws Exception {
			System.out.println(
					"================= Verifying new daily details day8 ad call tescase Started =========================");
			CharlesFunctions.archive_folder("Charles");
			proxy.clearCharlesSession();
			//Functions.finding_newDailyBidadcall_day2();
			Functions.clickonday8();
			attachScreen();
			Thread.sleep(10000);
			Functions.verifyingdailydetrailsday8today14();
			this.proxy.getXml();
		CharlesFunctions.createXMLFileForCharlesSessionFile();
		Utils.verifyPubadCal("Smoke", "DailyDetails");;
				
			System.out.println(
					"================= Verifying new daily details day8 ad call tescase End =========================");
		}


		@Test(priority = 283, enabled = true)
		@Description("Verifying amazon Slot Id for daily details day8 ad calll")
		public void Smoke_Test_amazon_aax_Dailydetails_Day8adcall() throws Exception {
			System.out
					.println("================= Verifying amazon slot id for  daily details day8  adcall test case Started =========================");
			Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
				
			System.out.println("================= Verifying amazon slot id for  daily details day8 adcall test case End =========================");
		}
		
		 @Test(priority = 284, enabled = true)
	      @Description("Verifying sz value for new daily details day8 ad call on FTL")
	      public void Smoke_Test_sz_Verifying_newdailydetails_day8adcall_FTL() throws Exception {
	      	System.out.println(
	      			"================= Verifying sz value for new daily details day8 ad call tescase Started =========================");

	      	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
	      	System.out.println(
	      			"=================  Verifying sz value for new daily details day8 ad call tescase  End =========================");
	      }
	      
		 
		 @Test(priority = 285, enabled = true)
			@Description("Verifying new daily details day9 ad call on FTL")
			public void Smoke_Test_Verifying_newdailydetails_day9adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying new daily details day9 ad call tescase Started =========================");
				CharlesFunctions.archive_folder("Charles");
				proxy.clearCharlesSession();
				//Functions.finding_newDailyBidadcall_day2();
				Functions.clickonday9();
				Thread.sleep(10000);
				attachScreen();
				Functions.verifyingdailydetrailsday8today14();
				this.proxy.getXml();
			CharlesFunctions.createXMLFileForCharlesSessionFile();
			Utils.verifyPubadCal("Smoke", "DailyDetails");;
					
				System.out.println(
						"================= Verifying new daily details day9 ad call tescase End =========================");
			}
	 @Test(priority = 286, enabled = true)
			@Description("Verifying amazon Slot Id for daily details day9 ad calll")
			public void Smoke_Test_amazon_aax_Dailydetails_Day9adcall() throws Exception {
				System.out
						.println("================= Verifying amazon slot id for  daily details day9  adcall test case Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
					
				System.out.println("================= Verifying amazon slot id for  daily details day9 adcall test case End =========================");
			}
	 
	 @Test(priority = 287, enabled = true)
     @Description("Verifying sz value for new daily details day9 ad call on FTL")
     public void Smoke_Test_sz_Verifying_newdailydetails_day9adcall_FTL() throws Exception {
     	System.out.println(
     			"================= Verifying sz value for new daily details day9 ad call tescase Started =========================");

     	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
     	System.out.println(
     			"=================  Verifying sz value for new daily details day9 ad call tescase  End =========================");
     }
	 
	 
	 @Test(priority = 288, enabled = true)
		@Description("Verifying new daily details day10 ad call on FTL")
		public void Smoke_Test_Verifying_newdailydetails_day10adcall_FTL() throws Exception {
			System.out.println(
					"================= Verifying new daily details day10 ad call tescase Started =========================");
			CharlesFunctions.archive_folder("Charles");
			proxy.clearCharlesSession();
		//	Functions.finding_newDailyBidadcall_day2();
			Functions.clickonday10();
			Thread.sleep(10000);
			attachScreen();
			Functions.verifyingdailydetrailsday8today14();
			this.proxy.getXml();
		CharlesFunctions.createXMLFileForCharlesSessionFile();
		Utils.verifyPubadCal("Smoke", "DailyDetails");
			
			
			System.out.println(
					"================= Verifying new daily details day10 ad call tescase End =========================");
		}

          @Test(priority = 290, enabled = true)
		@Description("Verifying amazon Slot Id for daily details day10  ad calll")
		public void Smoke_Test_amazon_aax_Dailydetails_Day10adcall() throws Exception {
			System.out
					.println("================= Verifying amazon slot id for  daily details day10  adcall test case Started =========================");
			Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
				
			System.out.println("================= Verifying amazon slot id for  daily details day10 adcall test case End =========================");
		}
          
          @Test(priority = 291, enabled = true)
          @Description("Verifying sz value for new daily details day10 ad call on FTL")
          public void Smoke_Test_sz_Verifying_newdailydetails_day10adcall_FTL() throws Exception {
          	System.out.println(
          			"================= Verifying sz value for new daily details day10 ad call tescase Started =========================");

          	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
          	System.out.println(
          			"=================  Verifying sz value for new daily details day10 ad call tescase  End =========================");
          }
     	 

          
          
          @Test(priority = 292, enabled = true)
			@Description("Verifying new daily details day11 ad call on FTL")
			public void Smoke_Test_Verifying_newdailydetails_day11adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying new daily details day11 ad call tescase Started =========================");
				CharlesFunctions.archive_folder("Charles");
				proxy.clearCharlesSession();
				//Functions.finding_newDailyBidadcall_day2();
				Functions.clickonday11();
				Thread.sleep(10000);
				attachScreen();
				Functions.verifyingdailydetrailsday8today14();
				this.proxy.getXml();
			CharlesFunctions.createXMLFileForCharlesSessionFile();
			Utils.verifyPubadCal("Smoke", "DailyDetails");
					
				System.out.println(
						"================= Verifying new daily details day11 ad call tescase End =========================");
			}
	
	
			@Test(priority = 293, enabled = true)
			@Description("Verifying amazon Slot Id for daily details day11  ad calll")
			public void Smoke_Test_amazon_aax_Dailydetails_Day11adcall() throws Exception {
				System.out
						.println("================= Verifying amazon slot id for  daily details day11  adcall test case Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
					
				System.out.println("================= Verifying amazon slot id for  daily details day11 adcall test case End =========================");
			}
			
			@Test(priority = 294, enabled = true)
	          @Description("Verifying sz value for new daily details day11 ad call on FTL")
	          public void Smoke_Test_sz_Verifying_newdailydetails_day11adcall_FTL() throws Exception {
	          	System.out.println(
	          			"================= Verifying sz value for new daily details day11 ad call tescase Started =========================");

	          	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
	          	System.out.println(
	          			"=================  Verifying sz value for new daily details day11 ad call tescase  End =========================");
	          }
			
			@Test(priority = 295, enabled = true)
			@Description("Verifying new daily details day12 ad call on FTL")
			public void Smoke_Test_Verifying_newdailydetails_day12adcall_FTL() throws Exception {
				System.out.println(
						"================= Verifying new daily details day12 ad call tescase Started =========================");
				CharlesFunctions.archive_folder("Charles");
				proxy.clearCharlesSession();
			//	Functions.finding_newDailyBidadcall_day2();
				Functions.clickonday12();
				Thread.sleep(10000);
				attachScreen();
				Functions.verifyingdailydetrailsday8today14();
				this.proxy.getXml();
			CharlesFunctions.createXMLFileForCharlesSessionFile();
			Utils.verifyPubadCal("Smoke", "DailyDetails");
					
				System.out.println(
						"================= Verifying new daily details day12 ad call tescase End =========================");
			}
	
	@Test(priority = 296, enabled = true)
			@Description("Verifying amazon Slot Id for daily details day12  ad calll")
			public void Smoke_Test_amazon_aax_Dailydetails_Day12adcall() throws Exception {
				System.out
						.println("================= Verifying amazon slot id for  daily details day12  adcall test case Started =========================");
				Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
					
				System.out.println("================= Verifying amazon slot id for  daily details day12 adcall test case End =========================");
			}
	
	@Test(priority = 298, enabled = true)
    @Description("Verifying sz value for new daily details day12 ad call on FTL")
    public void Smoke_Test_sz_Verifying_newdailydetails_day12adcall_FTL() throws Exception {
    	System.out.println(
    			"================= Verifying sz value for new daily details day12 ad call tescase Started =========================");

    	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
    	System.out.println(
    			"=================  Verifying sz value for new daily details day12 ad call tescase  End =========================");
    }

	
	

	@Test(priority = 300, enabled = true)
	@Description("Verifying new daily details day13 ad call on FTL")
	public void Smoke_Test_Verifying_newdailydetails_day13adcall_FTL() throws Exception {
		System.out.println(
				"================= Verifying new daily details day13 ad call tescase Started =========================");
		CharlesFunctions.archive_folder("Charles");
		proxy.clearCharlesSession();
	//	Functions.finding_newDailyBidadcall_day2();
		Functions.clickonday13();
		Thread.sleep(10000);
		attachScreen();
		Functions.verifyingdailydetrailsday8today14();
		this.proxy.getXml();
	CharlesFunctions.createXMLFileForCharlesSessionFile();
	Utils.verifyPubadCal("Smoke", "DailyDetails");
			
		System.out.println(
				"================= Verifying new daily details day13 ad call tescase End =========================");
	}

	@Test(priority = 301, enabled = true)
	@Description("Verifying amazon Slot Id for daily details day13  ad calll")
	public void Smoke_Test_amazon_aax_Dailydetails_Day13adcall() throws Exception {
		System.out
				.println("================= Verifying amazon slot id for  daily details day13  adcall test case Started =========================");
		Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
			
		System.out.println("================= Verifying amazon slot id for  daily details day13 adcall test case End =========================");
	}
	
	@Test(priority = 302, enabled = true)
    @Description("Verifying sz value for new daily details day13 ad call on FTL")
    public void Smoke_Test_sz_Verifying_newdailydetails_day13adcall_FTL() throws Exception {
    	System.out.println(
    			"================= Verifying sz value for new daily details day13 ad call tescase Started =========================");

    	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
    	System.out.println(
    			"=================  Verifying sz value for new daily details day13 ad call tescase  End =========================");
    }
	
	@Test(priority = 304, enabled = true)
	@Description("Verifying new daily details day14 ad call on FTL")
	public void Smoke_Test_Verifying_newdailydetails_day14adcall_FTL() throws Exception {
		System.out.println(
				"================= Verifying new daily details day14 ad call tescase Started =========================");
		CharlesFunctions.archive_folder("Charles");
		proxy.clearCharlesSession();
	//	Functions.finding_newDailyBidadcall_day2();
		Functions.clickonday14();
		Thread.sleep(10000);
		attachScreen();
		Functions.verifyingdailydetrailsday8today14();
		this.proxy.getXml();
	CharlesFunctions.createXMLFileForCharlesSessionFile();
	Utils.verifyPubadCal("Smoke", "DailyDetails");
			
		System.out.println(
				"================= Verifying new daily details day14 ad call tescase End =========================");
	}
@Test(priority = 306, enabled = true)
	@Description("Verifying amazon Slot Id for daily details day14  ad calll")
	public void Smoke_Test_amazon_aax_Dailydetails_Day14adcall() throws Exception {
		System.out
				.println("================= Verifying amazon slot id for  daily details day14  adcall test case Started =========================");
		Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
		System.out.println("================= Verifying amazon slot id for  daily details day14 adcall test case End =========================");
	}

@Test(priority = 308, enabled = true)
@Description("Verifying sz value for new daily details day14 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day14adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying sz value for new daily details day14 ad call tescase Started =========================");

	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
	System.out.println(
			"=================  Verifying sz value for new daily details day14 ad call tescase  End =========================");
}



@Test(priority = 310, enabled = true)
@Description("Verifying new daily details day15 ad call on FTL")
public void Smoke_Test_Verifying_newdailydetails_day15adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying new daily details day15 ad call tescase Started =========================");
	CharlesFunctions.archive_folder("Charles");
	proxy.clearCharlesSession();
//	Functions.finding_newDailyBidadcall_day2();
	Functions.clickonday15();
	Thread.sleep(10000);
	attachScreen();
	Functions.verifyingdailydetrailsday15();
	this.proxy.getXml();
CharlesFunctions.createXMLFileForCharlesSessionFile();
Utils.verifyPubadCal("Smoke", "DailyDetails");
	
	System.out.println(
			"================= Verifying new daily details day15 ad call tescase End =========================");
}

@Test(priority = 312, enabled = true)
@Description("Verifying amazon Slot Id for daily details day15  ad call")
public void Smoke_Test_amazon_aax_Dailydetails_Day15adcall() throws Exception {
	System.out
			.println("================= Verifying amazon slot id for  daily details day15  adcall test case Started =========================");
	Utils.verifyAAX_SlotId("Smoke", "DailyDetails");
		
	System.out.println("================= Verifying amazon slot id for  daily details day15 adcall test case End =========================");
}

@Test(priority = 314, enabled = true)
@Description("Verifying sz value for new daily details day15 ad call on FTL")
public void Smoke_Test_sz_Verifying_newdailydetails_day15adcall_FTL() throws Exception {
	System.out.println(
			"================= Verifying sz value for new daily details day15 ad call tescase Started =========================");

	Utils.validate_Noncustom_param_val_of_gampad("Smoke", "DailyDetails", "sz", "360x210%7C300x250");
	System.out.println(
			"=================  Verifying sz value for new daily details day15 ad call tescase  End =========================");
}





	



	
	
	
	
	
	

}
