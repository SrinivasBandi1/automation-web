package com.intelehealth.listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.intelehealth.base.BasePage;

//public class ScreenshotListener extends TestListenerAdapter {
//    @Override
//    public void onTestFailure(ITestResult result) {
//    	BasePage basePage = new BasePage();
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
//        String methodName = result.getName();
//        if(!result.isSuccess()){
//            File scrFile = ((TakesScreenshot)basePage.getDriver()).getScreenshotAs(OutputType.FILE);
//            try {
//                String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/target/surefire-reports";
//                File destFile = new File((String) reportDirectory+"/failure_screenshots/"+methodName+"_"+formater.format(calendar.getTime())+".png");
//                FileUtils.copyFile(scrFile, destFile);
//                Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotListener extends TestListenerAdapter {

    private static WebDriver driver;

    public static void setDriver(WebDriver driver) {
        ScreenshotListener.driver = driver;
    }

    @Override
    public void onTestFailure(ITestResult result) 
    {
        captureScreenshot(result.getName() + "_failure");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        captureScreenshot(result.getName() + "_success");
    }

    private void captureScreenshot(String fileName) {
    	if (driver == null) {
            System.out.println("⚠️ Screenshot skipped: driver is null.");
            return;
        }
    	if (driver instanceof org.openqa.selenium.remote.RemoteWebDriver) {
            var remoteDriver = (org.openqa.selenium.remote.RemoteWebDriver) driver;
            if (remoteDriver.getSessionId() == null) {
                System.out.println("⚠️ Screenshot skipped: driver session already quit.");
                return;
            }
        }
        if (driver != null) {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotPath = "./screenshots/" + fileName + "_" + timeStamp + ".png";
            File destFile = new File(screenshotPath);

            try {
                FileUtils.copyFile(srcFile, destFile);
//                System.out.println("Screenshot captured: " + screenshotPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
