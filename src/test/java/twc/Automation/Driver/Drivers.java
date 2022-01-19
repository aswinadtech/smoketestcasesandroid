package twc.Automation.Driver;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

public class Drivers extends read_Property_File_Info {
	@SuppressWarnings("rawtypes")
	protected static AppiumDriver Ad ;
	public static WebDriver driver = null;

	@Step("{0}")
	public static void logStep(String stepName) {
	}
	
	@Attachment("Screenshot")
    public static byte[] attachScreen() {
        logStep("Taking screenshot");
        return (((TakesScreenshot)Ad).getScreenshotAs(OutputType.BYTES));
    }
}
