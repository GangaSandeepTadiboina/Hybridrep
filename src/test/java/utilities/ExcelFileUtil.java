package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileUtil {
XSSFWorkbook wb;
public ExcelFileUtil (String Excelpath) throws Throwable {
	FileInputStream fi = new FileInputStream(Excelpath);
	wb = new XSSFWorkbook(fi);
}
//counting no of rows in a sheet
public int rowCount(String sheetname) 
{
	return wb.getSheet(sheetname).getLastRowNum();
		
}
//reading cell data
public String getCellData(String sheetname, int row, int column)
{
	String data="";
	if(wb.getSheet(sheetname).getRow(row).getCell(column).getCellType()==CellType.NUMERIC) 
	{
		int celldata = (int) wb.getSheet(sheetname).getRow(row).getCell(column).getNumericCellValue();
		data = String.valueOf(celldata);
	}
	else 
	{
		data = wb.getSheet(sheetname).getRow(row).getCell(column).getStringCellValue();
	}
	return data;
}
//write result into new workbook
public void setCellData(String sheetname, int row, int column, String status, String WriteExcel) throws Throwable 
{
XSSFSheet ws= wb.getSheet(sheetname);
XSSFRow rownum = ws.getRow(row);
XSSFCell cell = rownum.createCell(column);
cell.setCellValue(status);
if(status.equalsIgnoreCase("Pass")) 
{
	XSSFCellStyle style = wb.createCellStyle();
	XSSFFont font = wb.createFont();
	font.setColor(IndexedColors.GREEN.getIndex());
	font.setBold(true);
	style.setFont(font);
	rownum.getCell(column).setCellStyle(style);
}
else if(status.equalsIgnoreCase("Fail")) 
{
	XSSFCellStyle style = wb.createCellStyle();
	XSSFFont font = wb.createFont();
	font.setColor(IndexedColors.RED.getIndex());
	font.setBold(true);
	style.setFont(font);
	rownum.getCell(column).setCellStyle(style);
}
else if(status.equalsIgnoreCase("Blocked")) 
{
	XSSFCellStyle style = wb.createCellStyle();
	XSSFFont font = wb.createFont();
	font.setColor(IndexedColors.BLUE.getIndex());
	font.setBold(true);
	style.setFont(font);
	rownum.getCell(column).setCellStyle(style);
}
	FileOutputStream fo = new FileOutputStream(WriteExcel);
	wb.write(fo);
}
public static void main(String[] args) throws Throwable {
	ExcelFileUtil xl = new ExcelFileUtil("D://sample.xlsx");
	int rc = xl.rowCount("Emp");
	System.out.println(rc);
	for(int i=1;i<=rc;i++) 
	{
		String fname = xl.getCellData("Emp", i, 0);
		String mname = xl.getCellData("Emp", i, 1);
		String lname = xl.getCellData("Emp", i, 2);
		String eid = xl.getCellData("Emp", i, 3);
		System.out.println(fname+" "+mname+" "+lname+" "+eid);
		//xl.setCellData("Emp", i, 4, "Pass", "D://results.xlsx");
		//xl.setCellData("Emp", i, 4, "Fail", "D://results.xlsx");
		xl.setCellData("Emp", i, 4, "Blocked", "D://results.xlsx");
	}
	
}
}
