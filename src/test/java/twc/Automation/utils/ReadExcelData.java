package twc.Automation.utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import twc.Automation.Driver.Drivers;
import twc.Automation.General.DeviceStatus;
import twc.Automation.ReadDataFromFile.read_excel_data;

public class ReadExcelData {
	
//		public static String[][] getExcelData(String ExcelWorkbook, String ExcelSheet){
//		
//		 		String[][] exceldata = null;
//				try {
//					exceldata = ReadExcelData.readExcelData(ExcelWorkbook, ExcelSheet);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return exceldata;
//		}
		
		
		public static String[][] getExcelData(String ExcelWorkbook,  String ExcelSheet) throws Exception {
			
			//Drivers.property();
			String workingDir=System.getProperty("user.dir");
			String excelPath=workingDir+File.separator+ExcelWorkbook;
		
			File f_validation= new File(excelPath);
			
			FileInputStream fis_validation = new FileInputStream(f_validation);
			HSSFWorkbook wb_validation = new HSSFWorkbook(fis_validation);
			HSSFSheet ws = wb_validation.getSheet(ExcelSheet);

			int rownum = ws.getLastRowNum() + 1;
			int colnum = ws.getRow(0).getLastCellNum();
			String data[][] = new String[rownum][colnum];

				for (int i = 0; i < rownum; i++) {
				    HSSFRow row = ws.getRow(i);

				    for (int j = 0; j < colnum; j++) {
					HSSFCell cell = row.getCell(j);
					String value = cell.toString();//Cell_To_String.celltostring(cell);
					data[i][j] = value;
					//System.out.println("Values are :" + value + " : data[" + i + "][" + j + "]");
				    }
				}
			return data;
			}


	
		
}
