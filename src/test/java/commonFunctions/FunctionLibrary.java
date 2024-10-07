package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static Properties conpro;
	public static WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser()throws Throwable
	{
		conpro = new Properties();
		conpro.load(new FileInputStream("PropertyFiles/Environment.properties"));
		if(conpro.getProperty("browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is Not matching",true);
		}
		return driver;
	}
	//method for launch url
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}
	//method for to wait for any webelement
	public static void waitForElement(String LocatorType,String LocatorValue,String TestData)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}

	}
	//method for textboxes
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
	}
	//method for buttons,links,radio buttons,checkbox and images
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	//method for page  title validation
	public static void validateTitle(String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title, "Title is Not Matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}
	//method for close browser
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for generate data
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd");
		return df.format(date);
	}
	//method for drop down
	public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value =Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			int value =Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			int value =Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}
	}
	//method to capture stock number into note pad
	public static void capturestock(String LocatorType,String LocatorValue) throws Throwable
	{
		String stockNum="";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stockNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stockNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stockNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stockNum);
		bw.flush();
		bw.close();
	}
	//method for stock number validation
	public static void stockTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		Thread.sleep(2000);
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			//clik search panel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		
		String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Thread.sleep(3000);
		Reporter.log(Exp_Data+"     "+Act_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data, "Stock Number Is Not Matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}

	//method for capturesupplier number
	public static void capturesupp(String LocaterType, String LocaterValue) throws Throwable
	{
		String suppnumber = "";
		if(LocaterType.equalsIgnoreCase("xpath"))
		{
			suppnumber = driver.findElement(By.xpath(LocaterValue)).getAttribute("value");
		}
		if(LocaterType.equalsIgnoreCase("name"))
		{
			suppnumber = driver.findElement(By.name(LocaterValue)).getAttribute("value");
		}
		if(LocaterType.equalsIgnoreCase("id"))
		{
			suppnumber = driver.findElement(By.id(LocaterValue)).getAttribute("value");
		}
		FileWriter fr = new FileWriter("./CaptureData/suppnumber.txt");
		BufferedWriter br = new BufferedWriter(fr);
		br.write(suppnumber);
		br.flush();
		br.close();
	}
	//method for supplier table
	public static void supplierTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/suppnumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		Thread.sleep(2000);
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_Data+" "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Act_Data,"Supplier Number is Not Matching");
		} catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
		
	}
	//method for capture customer number
	public static void capturecust(String LocatorType, String LocatorValue) throws Throwable
	{
		String Custnumber="";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			Custnumber = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			Custnumber = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			Custnumber = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fr = new FileWriter("./CaptureData/custnumber.txt");
		BufferedWriter br = new BufferedWriter(fr);
		br.write(Custnumber);
		br.flush();
		br.close();	
	}
	//method for customer table
	public static void customerTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/custnumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		Thread.sleep(2000);
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Act_Data+" "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Act_Data,"Customer Number is Not Matching");
		} catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
		
	}
	//method for capture supplier number
	public static void capturepur(String LocaterType, String LocaterValue) throws Throwable
	{
		String Suppliernumber = "";
		if(LocaterType.equalsIgnoreCase("xpath"))
		{
			Suppliernumber = driver.findElement(By.xpath(LocaterValue)).getAttribute("value");
		}
		if(LocaterType.equalsIgnoreCase("name"))
		{
			Suppliernumber = driver.findElement(By.name(LocaterValue)).getAttribute("value");
		}
		if(LocaterType.equalsIgnoreCase("id"))
		{
			Suppliernumber = driver.findElement(By.id(LocaterValue)).getAttribute("value");
		}
		FileWriter fr = new FileWriter("./CaptureData/Purnumber.txt");
		BufferedWriter br = new BufferedWriter(fr);
		br.write(Suppliernumber);
		br.flush();
		br.close();
	}
	//method for Supplier table
	public static void purchaseTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/Purnumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		br.close();
		
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Act_Data+" "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Act_Data,"purchase Number is Not Matching");
		} catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	}
}


