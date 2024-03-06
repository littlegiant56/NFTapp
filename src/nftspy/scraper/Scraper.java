package nftspy.scraper;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public abstract class Scraper {
    private final WebDriver driver;
    private final Actions actions;
    protected final String prefix;

    public Scraper(String chromeDriverPath, String chromePath) {
        prefix = "[" + getClass().getSimpleName() + "] ";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions driverOptions = new ChromeOptions();
        driverOptions.setBinary(chromePath);
        driverOptions.addArguments(
                "headless",
                "ignore-certificate-errors",
                "disable-gpu",
                "allow-running-insecure-content",
                "window-size=1920,1080",
                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/94.0.4606.81 Safari/537.36"
                );

        driverOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        driver = new ChromeDriver(driverOptions);
        actions = new Actions(driver);
    }

    public Actions getActions() {
        return actions;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getPrefix() {
        return prefix;
    }

    public abstract List<?> browse();

    public void close() {
        System.out.println(prefix + "Browser driver closed");
        driver.quit();
    }
}
