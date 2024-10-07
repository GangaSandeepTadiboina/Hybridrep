package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
String inputpath ="./FileInput/Controller.xlsx";
String outputpath ="./FileOutput/HybridResults.xlsx";
String TCSheet="MasterTestCases";
ExtentReports reports;
ExtentTest logger;
public void startTest() throws Throwable
{
	String Module_status="";
	String Module_New ="";
	//create obejct for Excelfileutil class
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in TCSheet

	for(int i=1;i<=xl.rowCount(TCSheet);i++)
	{
		if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
		{
			//read moudule name cell and store into one variable
			String TCModule =xl.getCellData(TCSheet, i, 1);
			//define path of html
			reports = new ExtentReports("./target/Reports/"+TCModule+"----"+FunctionLibrary.generateDate()+".html");
			logger=reports.startTest(TCModule);
			//iterate all rows in TCModule
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				//read cells from TCModule
				String Description =xl.getCellData(TCModule, j, 0);
				String ObjectType= xl.getCellData(TCModule, j, 1);
				String Ltype = xl.getCellData(TCModule, j, 2);
				String LValue =xl.getCellData(TCModule, j, 3);
				String TestData = xl.getCellData(TCModule, j, 4);
				try {
					if(ObjectType.equalsIgnoreCase("startBrowser"))
					{
					driver =	FunctionLibrary.startBrowser();
					logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl();
						logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(Ltype, LValue, TestData);
						logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(Ltype, LValue, TestData);
						logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(Ltype, LValue);
						logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(TestData);
						logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser();
						logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("dropDownAction"))
					{
						FunctionLibrary.dropDownAction(Ltype, LValue, TestData);
					}
					if(ObjectType.equalsIgnoreCase("capturestock"))
					{
						FunctionLibrary.capturestock(Ltype, LValue);
					}
					if(ObjectType.equalsIgnoreCase("stockTable"))
					{
						FunctionLibrary.stockTable();
					}
					
					if(ObjectType.equalsIgnoreCase("capturesupp"))
					{
						FunctionLibrary.capturesupp(Ltype, LValue);
					}
					if(ObjectType.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable();
					}
					if(ObjectType.equalsIgnoreCase("capturecust"))
					{
						FunctionLibrary.capturecust(Ltype, LValue);
					}
					if(ObjectType.equalsIgnoreCase("customerTable"))
					{
						FunctionLibrary.customerTable();
					}
					
					if(ObjectType.equalsIgnoreCase("capturepur"))
					{
						FunctionLibrary.capturepur(Ltype, LValue);
					}
					if(ObjectType.equalsIgnoreCase("purchaseTable()"))
					{
						FunctionLibrary.purchaseTable();
					}
					
					//write as pass into status cell
					xl.setCellData(TCModule, j, 5, "pass", outputpath);
					logger.log(LogStatus.PASS, Description);
					Module_status="True";
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
					//write as Fail into status cell
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					logger.log(LogStatus.FAIL, Description);
					Module_New="False";
				}
				if(Module_status.equalsIgnoreCase("True"))
				{
					//write as pass into TCsheet
					xl.setCellData(TCSheet, i, 3, "Pass", outputpath);
				}
				if(Module_New.equalsIgnoreCase("False"))
				{
					//write as Fail into TCsheet
					xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
				}
				reports.endTest(logger);
				reports.flush();
			}
			
		}
		else
		{
			//write as Blocked into status cell for Flag N
			xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
		}
	}
	
}

}
