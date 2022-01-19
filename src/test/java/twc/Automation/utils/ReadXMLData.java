package twc.Automation.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tika.sax.xpath.Matcher;
import org.testng.Assert;

import twc.Automation.General.DeviceStatus;
import twc.Automation.General.Functions;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;
import twc.Automation.utils.ReadExcelData;





public class ReadXMLData {
	
	//public static String sb = null;
	
/*	public static String getXMLData() {
		//sb = null;
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		
		String sb = null;
		try {
			xml_data_into_buffer.read_xml_file_into_buffer_string("normal");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}*/
	
//	public static String getRequiredXMLData(String ExcelWorkbook, String ExcelSheet, String callType, int feedNumber, String xmlData) throws Exception{
//		 String bb_call = null;
//		 String bb_startIndex = null;
//		 String bb_endIndex = null;
//		 String bb_splitIndex = null;
//		 String equalTo_symbol = null; 
//		 String ampersand_symbol = null;
//		 String comma_symbol = null;
//		 String feed_call = null;
//		 String feed_startIndex = null;
//		 String feed_endIndex = null;
//		 String feed_splitIndex = null;
//		 String extendedPage_call = null;
//		 String extendedfeed_startIndex = null;
//		 String extendedPage_endIndex = null;
//		 String extendedPage_splitIndex = null;
//		 
//		 
//		//String sbData= ReadXMLData.sb.toString();
//		String[][] excelData=ReadExcelData.getExcelData(ExcelWorkbook, ExcelSheet);
//		
//		for (int i = 0; i < excelData.length; i++){
//			if(excelData[i][0].equalsIgnoreCase("bb_call")){
//				 bb_call = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("bb_startIndex")){
//				   bb_startIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("bb_endIndex")){
//				   bb_endIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("bb_splitIndex")){
//				bb_splitIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("symbol'='")){
//				   equalTo_symbol = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("symbol'&'")){
//				   ampersand_symbol = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("symbol','")){
//				   comma_symbol = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("feed_Call")){
//				feed_call = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("feed_startIndex")){
//				   feed_startIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("feed_endIndex")){
//				  feed_endIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("feed_splitIndex")){
//				   feed_splitIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("extendedPage_call")){
//				extendedPage_call = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("extendedfeed_startIndex")){
//				extendedfeed_startIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("extendedPage_endIndex")){
//				extendedPage_endIndex = excelData[i][2];
//			}else if(excelData[i][0].equalsIgnoreCase("extendedPage_splitIndex")){
//				extendedPage_splitIndex = excelData[i][2];
//			}
//		}
//		
//		
//		
//		
//		
//		DeviceStatus device_status = new DeviceStatus();
//		int Cap = device_status.Device_Status();
//		String callData = null;
//		if(callType.equalsIgnoreCase("bb")){
//			if(xmlData.toString().contains(bb_call)){ //excelData[18][Cap]
//				try {
//					String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(bb_call));//excelData[18][Cap]
//					String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(bb_startIndex));//excelData[25][Cap]
//					String expected_data = required_info.toString().substring(required_info.indexOf(bb_startIndex)+(bb_startIndex.length()),required_info.indexOf(bb_endIndex));//excelData[20][Cap]
//					expected_data=expected_data.toString().substring(expected_data.indexOf(bb_splitIndex)+(bb_splitIndex.length()));//excelData[19][Cap]
//					expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");//excelData[8][Cap]
//					expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");//excelData[9][Cap]
//					expected_data= expected_data.toString().replaceAll(comma_symbol, ",");//excelData[10][Cap]
//					callData = expected_data.toString();
//				} catch (Exception e) {
//					System.out.println("Could not capture bb call data");
//					e.printStackTrace();
//				}
//			}
//		}else if(callType.equalsIgnoreCase("feed")){
//			if(xmlData.toString().contains(feed_call)){//excelData[17][Cap]
//				try {
//					String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(feed_call)+feedNumber);//excelData[17][Cap]
//					String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(feed_startIndex));//excelData[24][Cap]
//					String expected_data = required_info.toString().substring(required_info.indexOf(feed_startIndex)+(feed_startIndex.length()),required_info.indexOf(feed_endIndex));//excelData[20][Cap]
//					expected_data=expected_data.toString().substring(expected_data.indexOf(feed_splitIndex)+(feed_splitIndex.length()));//excelData[14][Cap]	
//					expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");
//					expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");
//					expected_data= expected_data.toString().replaceAll(comma_symbol, ",");
//					callData = expected_data.toString();
//				} catch (Exception e) {
//					System.out.println("Could not capture feed call data for feed"+feedNumber);
//					e.printStackTrace();
//				}
//			}
//			
//		}//else if(callType.equalsIgnoreCase("Hourly")){
//		else{
//			
//			if(callType.equalsIgnoreCase("hourly")){
//				callType="hourly";
//			}
//			else if(callType.equalsIgnoreCase("daily")){
//				callType="10day";
//			}
//			else if(callType.equalsIgnoreCase("map")){
//				callType="Maps";
//			}
//			else if(callType.equalsIgnoreCase("news")){
//				///7646/app_android_us/weather/news
//				callType="articles";
//				String newsCall[]={"articles", "news"};
//			}
//			callType=callType.toLowerCase();
//			if(xmlData.toString().contains(extendedPage_call+callType)){//excelData[33][Cap]
////				//############
////				String extendedPage_call2 = "%2F7646%2Fapp_android_us.*severe.*";
////				Pattern p = Pattern.compile(extendedPage_call2);
////				java.util.regex.Matcher m = p.matcher(xmlData);
////				boolean b = m.find();
////				//##########
////			
////			    boolean matches = Pattern.matches(extendedPage_call2, xmlData.toString());
////			    System.out.println("matches = " + matches);
////				//####################
//				try {
//					String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(extendedPage_call));//excelData[33][Cap]
//					String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(extendedfeed_startIndex));//excelData[35][Cap]
//					String expected_data = required_info.toString().substring(required_info.indexOf(extendedfeed_startIndex)+(extendedfeed_startIndex.length()),required_info.indexOf(extendedPage_endIndex));//excelData[35][Cap]//excelData[36][Cap]
//					expected_data=expected_data.toString().substring(expected_data.indexOf(extendedPage_splitIndex)+(extendedPage_splitIndex.length()));	//excelData[37][Cap]
//					expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");
//					expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");
//					expected_data= expected_data.toString().replaceAll(comma_symbol, ",");
//					callData = expected_data.toString();
//				} catch (Exception e) {
//					System.out.println("Could not capture extended call data for "+callType);
//					e.printStackTrace();
//				}
//			}
//			
//		}
//		
//		return callData;
//	}
//	
//	
	
	//############
	public static String getRequiredXMLData(String ExcelWorkbook, String ExcelSheet, String callType, int feedNumber, String xmlData) throws Exception{
		 String bb_call = null;
		 String bb_startIndex = null;
		 String bb_endIndex = null;
		 String bb_splitIndex = null;
		 String equalTo_symbol = null; 
		 String ampersand_symbol = null;
		 String comma_symbol = null;
		 String feed_call = null;
		 String feed_startIndex = null;
		 String feed_endIndex = null;
		 String feed_splitIndex = null;
		 String extendedPage_call = null;
		 String extendedfeed_startIndex = null;
		 String extendedPage_endIndex = null;
		 String extendedPage_splitIndex = null;
		 ArrayList<String> newsList = null;
		 
		//String sbData= ReadXMLData.sb.toString();
		String[][] excelData=ReadExcelData.getExcelData(ExcelWorkbook, ExcelSheet);
		
		for (int i = 0; i < excelData.length; i++){
			if(excelData[i][0].equalsIgnoreCase("bb_call")){
				 bb_call = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("bb_startIndex")){
				   bb_startIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("bb_endIndex")){
				   bb_endIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("bb_splitIndex")){
				bb_splitIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("symbol'='")){
				   equalTo_symbol = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("symbol'&'")){
				   ampersand_symbol = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("symbol','")){
				   comma_symbol = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("feed_Call")){
				feed_call = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("feed_startIndex")){
				   feed_startIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("feed_endIndex")){
				  feed_endIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("feed_splitIndex")){
				   feed_splitIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("extendedPage_call")){
				extendedPage_call = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("extendedfeed_startIndex")){
				extendedfeed_startIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("extendedPage_endIndex")){
				extendedPage_endIndex = excelData[i][2];
			}else if(excelData[i][0].equalsIgnoreCase("extendedPage_splitIndex")){
				extendedPage_splitIndex = excelData[i][2];
			}
		}
		
		
		
		
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		String callData = null;
		if(callType.equalsIgnoreCase("bb")){
			if(xmlData.toString().contains(bb_call)){ //excelData[18][Cap]
				try {
					String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(bb_call));//excelData[18][Cap]
					String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(bb_startIndex));//excelData[25][Cap]
					String expected_data = required_info.toString().substring(required_info.indexOf(bb_startIndex)+(bb_startIndex.length()),required_info.indexOf(bb_endIndex));//excelData[20][Cap]
					expected_data=expected_data.toString().substring(expected_data.indexOf(bb_splitIndex)+(bb_splitIndex.length()));//excelData[19][Cap]
					expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");//excelData[8][Cap]
					expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");//excelData[9][Cap]
					expected_data= expected_data.toString().replaceAll(comma_symbol, ",");//excelData[10][Cap]
					callData = expected_data.toString();
				} catch (Exception e) {
					System.out.println("Could not capture bb call data");
					e.printStackTrace();
				}
			}
		}else if(callType.equalsIgnoreCase("feed")){
			if(xmlData.toString().contains(feed_call)){//excelData[17][Cap]
				try {
					String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(feed_call)+feedNumber);//excelData[17][Cap]
					String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(feed_startIndex));//excelData[24][Cap]
					String expected_data = required_info.toString().substring(required_info.indexOf(feed_startIndex)+(feed_startIndex.length()),required_info.indexOf(feed_endIndex));//excelData[20][Cap]
					expected_data=expected_data.toString().substring(expected_data.indexOf(feed_splitIndex)+(feed_splitIndex.length()));//excelData[14][Cap]	
					expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");
					expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");
					expected_data= expected_data.toString().replaceAll(comma_symbol, ",");
					callData = expected_data.toString();
				} catch (Exception e) {
					System.out.println("Could not capture feed call data for feed"+feedNumber);
					e.printStackTrace();
				}
			}
			
		}//else if(callType.equalsIgnoreCase("Hourly")){
		else{
			
			if(callType.equalsIgnoreCase("hourly")){
				//iu=%2F7646%2Fapp_android_us%2Fdisplay%2Fdetails%2F
				//iu=%2F7646%2Fapp_android_us%2F
				///7646/app_android_us/display/details/hourly
				callType="display%2Fdetails%2Fhourly";
			}
			else if(callType.equalsIgnoreCase("daily")){
				callType="display%2Fdetails%2F10day";
			}
			else if(callType.equalsIgnoreCase("map")){
				///7646/app_android_us/display/details/Maps
				callType="display%2Fdetails%2Fmaps";
			}
			else if(callType.equalsIgnoreCase("news")){
				///7646/app_android_us/weather/news
				///7646/app_android_us/weather/severe/winter
				///7646/app_android_us/weather/news/science/mother_nature_revealed
				
				//weather/severe/tornado
				//video
				//callType="articles";
				//iu=%2F7646%2Fapp_android_us%2Fdisplay%2Fdetails%2F
				newsList=new ArrayList<String>(); 
				newsList.add("display%2Fdetails%2Fnews"); 
				newsList.add("display%2Fdetails%2Farticles"); 
				newsList.add("weather%2Fnews");  
				newsList.add("weather%2Fsevere%2Fwinter");  
				newsList.add("weather%2Fnews%2Fscience%2Fmother_nature_revealed"); 
				newsList.add("weather%2Fsevere%2Ftornado"); 

			}
			
			
			if(callType.equalsIgnoreCase("news")){
			  Iterator itr=newsList.iterator();  
			   while(itr.hasNext()){  
			     
		

			
			    callType=((String) itr.next());
				if(xmlData.toString().contains(extendedPage_call+callType)){//excelData[33][Cap]
//					//############
//					String extendedPage_call2 = "%2F7646%2Fapp_android_us.*severe.*";
//					Pattern p = Pattern.compile(extendedPage_call2);
//					java.util.regex.Matcher m = p.matcher(xmlData);
//					boolean b = m.find();
//					//##########
//				
//				    boolean matches = Pattern.matches(extendedPage_call2, xmlData.toString());
//				    System.out.println("matches = " + matches);
//					//####################
					try {
						String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(extendedPage_call));//excelData[33][Cap]
						String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(extendedfeed_startIndex));//excelData[35][Cap]
						String expected_data = required_info.toString().substring(required_info.indexOf(extendedfeed_startIndex)+(extendedfeed_startIndex.length()),required_info.indexOf(extendedPage_endIndex));//excelData[35][Cap]//excelData[36][Cap]
						expected_data=expected_data.toString().substring(expected_data.indexOf(extendedPage_splitIndex)+(extendedPage_splitIndex.length()));	//excelData[37][Cap]
						expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");
						expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");
						expected_data= expected_data.toString().replaceAll(comma_symbol, ",");
						callData = expected_data.toString();
						//break;
					} catch (Exception e) {
						System.out.println("Could not capture extended call data for "+callType);
						e.printStackTrace();
					}
				
				}

			  }
			}
			
			
			
			if(xmlData.toString().contains(extendedPage_call+callType)){//excelData[33][Cap]
//				//############
//				String extendedPage_call2 = "%2F7646%2Fapp_android_us.*severe.*";
//				Pattern p = Pattern.compile(extendedPage_call2);
//				java.util.regex.Matcher m = p.matcher(xmlData);
//				boolean b = m.find();
//				//##########
//			
//			    boolean matches = Pattern.matches(extendedPage_call2, xmlData.toString());
//			    System.out.println("matches = " + matches);
//				//####################
				try {
					String Read_API_Call_Data = xmlData.toString().substring(xmlData.toString().indexOf(extendedPage_call));//excelData[33][Cap]
					String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(extendedfeed_startIndex));//excelData[35][Cap]
					String expected_data = required_info.toString().substring(required_info.indexOf(extendedfeed_startIndex)+(extendedfeed_startIndex.length()),required_info.indexOf(extendedPage_endIndex));//excelData[35][Cap]//excelData[36][Cap]
					expected_data=expected_data.toString().substring(expected_data.indexOf(extendedPage_splitIndex)+(extendedPage_splitIndex.length()));	//excelData[37][Cap]
					expected_data= expected_data.toString().replaceAll(equalTo_symbol, "=");
					expected_data= expected_data.toString().replaceAll(ampersand_symbol, "&");
					expected_data= expected_data.toString().replaceAll(comma_symbol, ",");
					callData = expected_data.toString();
					//break;
				} catch (Exception e) {
					System.out.println("Could not capture extended call data for "+callType);
					e.printStackTrace();
				}
			
			}

			
		}
		
		
		return callData;
	}
	
	
	
	//############
	
	
public static Map<String , String> getCustomParameter(String custParamName, String ExcelWorkbook, String ExcelSheet, String callType , String sbData) throws Exception{
			
			String sb = sbData.toString();
			Map<String , String> custParam = null;
			String[][] exceldata;
			exceldata=ReadExcelData.getExcelData(ExcelWorkbook, ExcelSheet);
			DeviceStatus device_status = new DeviceStatus();
			int Cap = device_status.Device_Status();		
						
				String[] keypairs = sbData.split("&");
				custParam = new HashMap<String, String>();
				try{
					for (String keyvalue : keypairs)
					{
						//if(keyvalue.contains("=")){
						if(keyvalue.contains(custParamName.toLowerCase())){
						String[] key_value = keyvalue.split("=");
							if(key_value[0].equalsIgnoreCase(custParamName)){
								custParam.put(key_value[0], key_value[1]);
								break;
							}
					    }
					}
					
			} catch (Exception e) {
				System.out.println(custParamName + " custom parameter Not found in "+callType);
				Assert.fail(custParamName + " custom parameter Not found "+callType);
			}
			
		return custParam;
	}



		public static String validateCustomParam(String custParamName, String ExcelWorkbook, String ExcelSheet, String callType, String sbData) throws Exception{
			
			String result = null;
			Map<String, String> custParam = ReadXMLData.getCustomParameter(custParamName, ExcelWorkbook, ExcelSheet, callType, sbData);
			String custParamValue = custParam.get(custParamName.toLowerCase());
			
//			for ( String key : custParam.keySet() ) {
//			    System.out.println( key );
//			}
			
			//String key = custParam.get(custParam.keySet().toArray()[0]);
			//!Character.isAlphabetic(actCustParamValue)
			
		    if(!custParamValue.isEmpty() && NumberUtils.isNumber(custParamValue)){ 
		    	if(Integer.parseInt(custParamValue)>=0 && Integer.parseInt(custParamValue)<=100){
					System.out.println("custParamValue for " +callType+" === "+custParamValue+" is valid value");
					//result="Pass";
					//System.out.println("Result "+result);
		    	}
			}
			 else 
			 {
					System.out.println("custParamValue " +callType+" === "+custParamValue);
					//result="Pass";
					//System.out.println("Result "+result); 
					Assert.fail("custParamValue "+"==="+custParamValue+" is NOT valid value");
				
			 }
		    //return result;
		    return custParamValue;
		}

	
}



