// Dinh Viet Ha - 20215042

package nftspy.scraper;

import nftspy.data.Post;
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TwitterPostScraper extends PostScraper {
    private final String email;
    private final String password;

    public TwitterPostScraper(String email, String password, String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
        this.email = email;
        this.password = password;
    }

    private void logIn() {
        getDriver().get("https://twitter.com/login");

        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        WebElement accForm = getDriver().findElement(By.xpath("//input[@name='text']"));
        accForm.sendKeys(email);

        WebElement nextButton = getDriver().findElement(By.xpath("//span[contains(text(),'Next')]"));
        nextButton.click();

        WebElement passwordForm = getDriver().findElement(By.xpath("//input[@name='password']"));
        passwordForm.sendKeys(password);

        WebElement signInButton = getDriver().findElement(By.xpath("//span[contains(text(),'Log in')]"));
        signInButton.click();
    }

    private void search(String query) {
        WebElement searchBox = getDriver().findElement(By.xpath("//input[@aria-label='Search query']"));
        searchBox.sendKeys(query);
        searchBox.sendKeys(Keys.ENTER);
    }

    @Override
    public List<Post> browse() {
        System.out.print(prefix + "Logging in ... ");
        logIn();
        System.out.println("Done");
        search("NFT");

        String title = "Twitter Post";
        List<Post> postList = new ArrayList<>();
        List<WebElement> elements = getDriver().findElements(By.tagName("article"));
        for (WebElement element : elements) {
            String content = element.getText();
            String url = element.findElement(By.className("permalink-tweet-container")).getText();
            postList.add(new Post(url, title, content));
        }
        return postList;
    }
}
