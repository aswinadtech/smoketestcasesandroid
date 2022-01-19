package twc.Automation.HandleMapLocal;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import twc.Automation.General.DeviceStatus;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.HandleWithCharles.CharlesFunctions;
public class MapLocalFunctions {
	
	@SuppressWarnings("unchecked")
	public static void contentModeModule(String severemode) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread("MapLocal");
		
		String[][] maplocaldata = read_excel_data.exceldataread("MapLocal");
		
		File sourceFile,destinationFile = null;
		
		List<String> get_content_file_name = CharlesFunctions.listFiles(exceldata[4][Cap]+severemode+"/");
		
		for (int i=0;i < get_content_file_name.size();i++)
		{
			if(get_content_file_name.get(i).equals("content-mode-severe.json")){
				
				sourceFile = new File(exceldata[4][Cap]+severemode+"/".concat(get_content_file_name.get(i)));
				destinationFile = new File(exceldata[3][Cap].concat(get_content_file_name.get(i)));
				//Remove Files From Destination Folder
				// if(severemode.equals("normal")){
				// 	FileUtils.cleanDirectory(new File(exceldata[3][Cap]));
				// 	break;
				// }
				// else{
				//Copy  File From Source To Destination Folder
					FileUtils.cleanDirectory(new File(exceldata[3][Cap]));
					FileUtils.copyFile(sourceFile, destinationFile);
					
					Thread.sleep(2000);
					
//					JSONParser parser = new JSONParser();
//					try {
//			        	Object obj = parser.parse(new FileReader(destinationFile));
//			            JSONObject jsonObject = (JSONObject) obj;
//			            jsonObject.put("mode", severemode);
//			            FileWriter jsonFileWriter = new FileWriter(destinationFile); 
//			            jsonFileWriter.write(jsonObject.toJSONString()); 
//			            jsonFileWriter.flush(); 
//			            jsonFileWriter.close();
//			            //System.out.println("mode : "+jsonObject.get("mode"));
//			        } catch (Exception e) {
//			            e.printStackTrace();
//			        }
					Thread.sleep(2000);
				//}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void alertModeChanges(String alertmode) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread("MapLocal");
		
		String[][] maplocaldata = read_excel_data.exceldataread("MapLocal");
		
		File sourceFile,destinationFile = null;
		
		List<String> get_content_file_name = CharlesFunctions.listFiles(exceldata[4][Cap]);
		
		
		for (int i=0;i < get_content_file_name.size();i++)
		{
			if(get_content_file_name.get(i).equals(maplocaldata[5][Cap])){
				
				sourceFile = new File(exceldata[4][Cap].concat(get_content_file_name.get(i)));
				destinationFile = new File(exceldata[3][Cap].concat(get_content_file_name.get(i)));
				
					if(alertmode.equals("withoutalert")){
						FileUtils.cleanDirectory(new File(exceldata[3][Cap]));
						break;
					}
					else{
						FileUtils.cleanDirectory(new File(exceldata[3][Cap]));
					}
					FileUtils.copyFile(sourceFile, destinationFile);
					
					Thread.sleep(2000);
					
					JSONParser parser = new JSONParser();
					try {
			        	Object obj = parser.parse(new FileReader(destinationFile));
			            JSONObject jsonObject = (JSONObject) obj;
			            //System.out.println("values : "+jsonObject.get("vt1alerts"));
			            JSONObject modifyCode = (JSONObject) jsonObject.get("vt1alerts");
			            if(alertmode.equals("withalert")){
			            	
				            JSONArray phenomenacode=new JSONArray();
				            phenomenacode.add("BZ");
				            modifyCode.put("phenomenaCode",phenomenacode);
				            JSONArray significancecode=new JSONArray();
				            significancecode.add("W");
				            modifyCode.put("significanceCode",significancecode);
				            JSONArray severitycode=new JSONArray();
				            severitycode.add(1);
				            modifyCode.put("severityCode",severitycode);
			            }
			            
			            FileWriter jsonFileWriter = new FileWriter(destinationFile); 
			            jsonFileWriter.write(jsonObject.toJSONString()); 
			            jsonFileWriter.flush(); 
			            jsonFileWriter.close();
			            //System.out.println("values : "+jsonObject.get("vt1alerts"));
			            
			        } catch (Exception e) {
			            //e.printStackTrace();
			        }
					Thread.sleep(2000);
			}
		}
	}

}
