package twc.Automation.ReadDataFromFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import twc.Automation.General.DeviceStatus;


public class read_xml_data_into_buffer{
	
	public String read_xml_file_into_buffer_string()throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] paths = read_excel_data.exceldataread("Paths");
		String xml_file_path=null;
		File folder = new File(paths[4][Cap]);
		File[] listOfFiles = folder.listFiles();
		String Filename = null;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				Filename = file.getName();
				xml_file_path = paths[4][Cap]+Filename;
				System.out.println("XML File Name is : "+Filename);
			}
		}
		
		StringBuilder sb=null;
		
		try {
			File xmlFile = new File(xml_file_path); 
			Reader fileReader = new FileReader(xmlFile); 
			BufferedReader bufReader = new BufferedReader(fileReader); 
			sb = new StringBuilder(); 
			String line = bufReader.readLine(); 
			while( (line=bufReader.readLine()) != null)
			{ 
				sb.append(line).append("\n"); 
			} 
			bufReader.close();
		} catch (Exception e) {
			System.out.println("No Data Found in XML File");
		}
		return sb.toString();
		
	}
}
