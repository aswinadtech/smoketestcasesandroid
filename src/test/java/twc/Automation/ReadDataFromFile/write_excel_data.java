package twc.Automation.ReadDataFromFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import twc.Automation.Driver.Drivers;

public class write_excel_data extends Drivers {
	
	public void enterResult(String sheetName, String Result, String Val, int rowIndex, int resultColIndex, int valueColIndex) {
		
		String Path = properties.getProperty("ExcelFilePath_CustParam_Result");

		try {

			FileInputStream file = new FileInputStream(Path);
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			HSSFSheet sheet = workbook.getSheet(sheetName);

			Cell searchText3 = sheet.getRow(rowIndex).getCell(resultColIndex);
			searchText3.setCellValue(Result);

			Cell searchText4 = sheet.getRow(rowIndex).getCell(valueColIndex);
			searchText4.setCellValue(Val);

			file.close();

			FileOutputStream outFile = new FileOutputStream(Path);
			workbook.write(outFile);
			outFile.close();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeResult( String sheetName, String tmp_val, int i, int resultColIndex) {
		
		String Path = properties.getProperty("ExcelFilePath_CustParam_Result");
		

		try {

			FileInputStream file = new FileInputStream(Path);
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			HSSFSheet sheet = workbook.getSheet(sheetName);

			Cell searchText3 = sheet.getRow(i).getCell(resultColIndex);
			
			searchText3.setCellValue(tmp_val);
			file.close();
			
			FileOutputStream outFile = new FileOutputStream(Path);
			workbook.write(outFile);
			outFile.close();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeappPath(String sheetname, String appPath_val) {
		
		String Path = properties.getProperty("excel_file_path");
		Cell searchText3=null;

		try {

			FileInputStream file = new FileInputStream(Path);
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			HSSFSheet sheet = workbook.getSheet(sheetname);
			if(sheetname.equals("Paths")){
				searchText3 = sheet.getRow(14).getCell(2);
			}
			else{
			searchText3 = sheet.getRow(10).getCell(2);
			}
			
			searchText3.setCellValue(appPath_val);
			file.close();
			
			FileOutputStream outFile = new FileOutputStream(Path);
			workbook.write(outFile);
			outFile.close();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
