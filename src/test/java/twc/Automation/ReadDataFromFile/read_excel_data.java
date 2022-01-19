package twc.Automation.ReadDataFromFile;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import twc.Automation.Driver.Drivers;;

public class read_excel_data extends Drivers{
	public static int rowCount;
public static String[][] exceldataread(String Type) throws Exception {
		Drivers.property();
		
		File f_validation= new File(properties.getProperty("excel_file_path"));
		FileInputStream fis_validation = new FileInputStream(f_validation);
		HSSFWorkbook wb_validation = new HSSFWorkbook(fis_validation);
		HSSFSheet ws = wb_validation.getSheet(Type);
//Read Excel
		int rownum = ws.getLastRowNum() + 1;
		int colnum = ws.getRow(0).getLastCellNum();
		String data[][] = new String[rownum][colnum];

//
		String value=null;
		int i,j = 0;
			for (i = 0; i < rownum; i++) {
			    HSSFRow row = ws.getRow(i);
	
			    for (j = 0; j < colnum; j++) {
				HSSFCell cell = row.getCell(j);
				 value = cell.toString();//Cell_To_String.celltostring(cell);
				data[i][j] = value;
				//System.out.println("Values are :" + value + " : data[" + i + "][" + j + "]");
			    }
			}
			//System.out.println("Values are :" + value + " : data[" + i + "][" + j + "]");
		return data;
		}

public static String[][] exceldataread_Custom_Parameters(String ExcelName,  String Type) throws Exception {
	
	Drivers.property();
	String TestSheetName = null;
	
	if(ExcelName.equals("Cust_Param"))
	{
		TestSheetName = "ExcelFilePath_CustParam";
	}else if(ExcelName.equals("Cust_Param_Result")){
		TestSheetName = "ExcelFilePath_CustParam_Result";
	}else if(ExcelName.equals("aaxCals")){
		TestSheetName = "ExcelFilePath_AdUnits";
	}
	else if(ExcelName.equals("smoke")){
		TestSheetName = "ExcelFilePath_AdUnits";
	}
	
	File f_validation= new File(properties.getProperty(TestSheetName));
	
	FileInputStream fis_validation = new FileInputStream(f_validation);
	HSSFWorkbook wb_validation = new HSSFWorkbook(fis_validation);
	HSSFSheet ws = wb_validation.getSheet(Type);

	int rownum = ws.getLastRowNum() + 1;
	int colnum = ws.getRow(0).getLastCellNum();
	String data[][] = new String[rownum][colnum];
	rowCount = ws.getLastRowNum();
		for (int i = 0; i < rownum; i++) {
		    HSSFRow row = ws.getRow(i);

		    for (int j = 0; j < colnum; j++) {
			HSSFCell cell = row.getCell(j);
			String value = cell.toString();//Cell_To_String.celltostring(cell);
			data[i][j] = value;
		//	System.out.println("Values are :" + value + " : data[" + i + "][" + j + "]");
		    }
		}
	return data;
	}
	
	
	public static void Clear_Exceldata() throws Exception{
		
		for(int feeds=1;feeds<=6;feeds++){

			exceldataread_Custom_Parameters("Cust_Param_Result","SMOKE");

			int Getresult1 = feeds*2;
			int ResultColumn_n1=7+Getresult1;
			int ResultColumn_n2=8+Getresult1;

			write_excel_data cleardata=new write_excel_data();
			for(int testcase=1;testcase<=43;testcase++)
			{
				cleardata.enterResult("SMOKE", "n", "n", testcase, ResultColumn_n1, ResultColumn_n2);

			}

		}
	}
}

