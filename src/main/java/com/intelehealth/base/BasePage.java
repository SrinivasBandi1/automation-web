package com.intelehealth.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.intelehealth.listeners.ScreenshotListener;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BasePage {

	protected WebDriver driver;
	Properties prop;
	OptionsManager optionsManager;
	private Properties testData;
	private String browsername;
	Response response;
	// At top of BasePage class
	private static final Map<String, WebDriver> driverMap = new ConcurrentHashMap<>();

	public static String highlight;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	public static synchronized WebDriver getDriver() {
		return tlDriver.get();
	}

	public String getBrowseName() {
		String browserName = prop.getProperty("browser");
		return browserName;
	}

	public String getDateTimeForFile() {
		return new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
	}

	/**
	 * This method is used to initialize the WebDriver based on the browserName
	 * 
	 * @param prop Properties object containing configuration properties
	 * @return WebDriver instance
	 */
	public WebDriver init_driver(Properties prop) {
		String browserName = prop.getProperty("browser");
		// System.setProperty("webdriver.manager", "false");
		System.setProperty("selenium.manager.disabled", "true");
		// System.out.println("Running on ----> " + browserName + " browser");
		optionsManager = new OptionsManager(prop);

		if (browserName.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver(optionsManager.getChromeOptions());
		} else if (browserName.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver(optionsManager.getFirefoxOptions());
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
			// System.out.println(browserName + " is not found, please pass the right
			// browser Name");
		}

		tlDriver.set(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get(prop.getProperty("url"));
		return driver;
	}

	public static synchronized WebDriver getDriver1() {
		return tlDriver.get();
	}

	public WebDriver init_driver1(Properties prop, String browserType) {

		String browserName = prop.getProperty("browser");

		// System.setProperty("webdriver.manager", "false");
		System.setProperty("selenium.manager.disabled", "true");
		// System.out.println("Running on ----> " + browserName + " browser");
		optionsManager = new OptionsManager(prop);

		if (browserName.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver(optionsManager.getChromeOptions());
		} else if (browserName.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver(optionsManager.getFirefoxOptions());
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
			// System.out.println(browserName + " is not found, please pass the right
			// browser Name");
		}

		tlDriver.set(driver);
		driverMap.put(browserType, driver);
		ScreenshotListener.setDriver(driver);

		System.out.println("🧹 Driver successfully created for: " + browserType);
		System.out.println("🧹 Driver map: " + driverMap.toString());

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		try {
			driver.manage().window().maximize();
		} catch (Exception e) {
			driver.manage().window().setSize(new Dimension(1920, 1080));
		}
		driver.manage().deleteAllCookies();
		driver.get(prop.getProperty("url"));
		return driver;
	}

	/*
	 * public WebDriver init_driver2(Properties prop, String browserType,String
	 * browserName) {
	 * 
	 * // String browserName = prop.getProperty("browser");
	 * 
	 * // System.setProperty("webdriver.manager", "false");
	 * //System.setProperty("selenium.manager.disabled", "true"); //
	 * System.out.println("Running on ----> " + browserName + " browser");
	 * optionsManager = new OptionsManager(prop);
	 * 
	 * if (browserName.equalsIgnoreCase("chrome")) { driver = new
	 * ChromeDriver(optionsManager.getChromeOptions()); } else if
	 * (browserName.equalsIgnoreCase("firefox")) {
	 * WebDriverManager.firefoxdriver().setup(); driver = new
	 * FirefoxDriver(optionsManager.getFirefoxOptions()); } else if
	 * (browserName.equalsIgnoreCase("safari")) { driver = new SafariDriver(); }
	 * else { System.out.println(browserName +
	 * " is not found, Defaulting to Chrome."); driver = new
	 * ChromeDriver(optionsManager.getChromeOptions()); //browserType = "chrome"; //
	 * Override browserType to match the actual browser being used }
	 * 
	 * tlDriver.set(driver); driverMap.put(browserType, driver);
	 * ScreenshotListener.setDriver(driver);
	 * 
	 * System.out.println("🧹 Driver successfully created for: " + browserType);
	 * System.out.println("🧹 Driver map: " + driverMap.toString());
	 * 
	 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); try {
	 * driver.manage().window().maximize(); } catch (Exception e) {
	 * driver.manage().window().setSize(new Dimension(1920, 1080)); }
	 * driver.manage().deleteAllCookies(); driver.get(prop.getProperty("url"));
	 * return driver; }
	 */
	// in com.intelehealth.base.BasePage
		public WebDriver init_driver2(Properties prop, String browserType, String browserName) {

	    optionsManager = new OptionsManager(prop);

	    // defensive defaulting
	    if (browserName == null || browserName.trim().isEmpty() || browserName.contains("${")) {
	        System.out.println("Browser not provided or unresolved; defaulting to chrome");
	        browserName = "chrome";
	    }

	    String bn = browserName.trim().toLowerCase();

	    switch (bn) {
	        case "chrome":
	       //     WebDriverManager.chromedriver().setup();
	            driver = new ChromeDriver(optionsManager.getChromeOptions());
	            break;

	        case "firefox":
	            WebDriverManager.firefoxdriver().setup();
	            driver = new FirefoxDriver(optionsManager.getFirefoxOptions());
	            break;

	        case "safari":
	            // SafariDriver works only on macOS runners; fallback to chrome in CI
	            try {
	                driver = new SafariDriver();
	            } catch (Exception e) {
	                System.out.println("Safari not available, defaulting to chrome");
	                WebDriverManager.chromedriver().setup();
	                driver = new ChromeDriver(optionsManager.getChromeOptions());
	            }
	            break;

	        default:
	            System.out.println(browserName + " is not supported; defaulting to chrome");
	       //     WebDriverManager.chromedriver().setup();
	            driver = new ChromeDriver(optionsManager.getChromeOptions());
	            break;
	    }

	    // store to thread-local and map (defensive)
	    tlDriver.set(driver);
	    if (driver == null) {
	        throw new RuntimeException("Driver initialization failed for browser: " + browserName);
	    }
	    driverMap.put(browserType != null ? browserType : "UNKNOWN-" + System.currentTimeMillis(), driver);
	    ScreenshotListener.setDriver(driver);

	    System.out.println("🧹 Driver successfully created for: " + browserType + " / " + browserName);
	    System.out.println("🧹 Driver map: " + driverMap.toString());

	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    try {
	        driver.manage().window().maximize();
	    } catch (Exception e) {
	        // fallback when maximize not supported
	        driver.manage().window().setSize(new Dimension(1920, 1080));
	    }
	    driver.manage().deleteAllCookies();

	    // read URL safely
	    String url = prop.getProperty("url");
	    if (url == null || url.trim().isEmpty()) {
	        throw new RuntimeException("Application URL not configured in properties (prop.getProperty(\"url\") returned null/empty).");
	    }
	    driver.get(url.trim());
	    return driver;
	}

	/**
	 * This method returns properties loaded from a configuration file.
	 * 
	 * @return Properties object containing configuration properties
	 */
	public Properties init_prop() {

		prop = new Properties();
		String path = null;
		String env = null;

		try {
			env = System.getProperty("env");
			if (env == null) {
				path = "./src/main/java/com/intelehealth/config/config.qa.properties";
			} else {
				switch (env) {
				case "qa":
					path = "./src/main/java/com/intelehealth/config/config.qa.properties";
					break;
				case "stg":
					path = "./src/main/java/com/intelehealth/config/config.stg.properties";
					break;
				case "prod":
					path = "./src/main/java/com/qa/hubspot/config/config.properties";
					break;
				default:
					// System.out.println("no env is passed");
					break;
				}
			}
			FileInputStream ip = new FileInputStream(path);
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// System.out.println("config file is not found.....");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * This method takes a screenshot and saves it to the specified directory.
	 * 
	 * @return Path to the saved screenshot
	 */
	public String getScreenshot() {
		File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/TestReports/screenshots/" + System.currentTimeMillis()
				+ ".png";
		File destination = new File(path);

		try {
			FileUtils.copyFile(src, destination);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;
	}

	public List<String> getPropertyList(String name) {
		List<String> list = Arrays.asList(name.toString().split("\\,"));
		// System.out.println(list);
		return list;
	}

	public synchronized void quitDriver(String testClassEnum) {
		WebDriver driver = driverMap.get(testClassEnum);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (driver == null) {
			// System.out.println("⚠️ No active driver found for: " + testClassEnum.name() +
			// " (already quit or never started)");
			return;
		}
		if (driver != null) {
			try {
				driver.quit();
				System.out.println("🧹 Driver successfully quit for: " + testClassEnum);
			} catch (Exception e) {
				// System.err.println("⚠️ Error quitting driver for " + testClassEnum.name() +
				// ": " + e.getMessage());
			} finally {
				driverMap.remove(testClassEnum);
				if (tlDriver.get() == driver) {
					tlDriver.remove();
				}
			}
		}
	}

	public Response getAdmitDataAPI() {
		RequestSpecification req;
		req = RestAssured.given().contentType("application/json");
		Response response = req.get("https://demo.intelehealth.org:4004/api/config/getPublishedConfig");
		return response;
	}
	
	public Boolean getAdmitDataAPIKey(String pathKey) {
		RequestSpecification req;
		req = RestAssured.given().contentType("application/json");
		Response response = req.get("https://nasstaging.intelehealth.org:4004/api/config/getPublishedConfig");
		Boolean isEnabled=response.jsonPath().getBoolean(pathKey);

		return isEnabled;
	}
	public void samplemethid() {
		System.out.println("sample method");
	}
}
