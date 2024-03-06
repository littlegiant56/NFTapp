package nftspy.scraper;

import nftspy.data.DateTime;
import nftspy.data.Post;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenSeaPostScraper extends PostScraper {
    private static final Map<String, Integer> monthMap = new HashMap<>();
    static {
        monthMap.put("JANUARY", Integer.valueOf(1));
        monthMap.put("FEBRUARY", Integer.valueOf(2));
        monthMap.put("MARCH", Integer.valueOf(3));
        monthMap.put("APRIL", Integer.valueOf(4));
        monthMap.put("MAY", Integer.valueOf(5));
        monthMap.put("JUNE", Integer.valueOf(6));
        monthMap.put("JULY", Integer.valueOf(7));
        monthMap.put("AUGUST", Integer.valueOf(8));
        monthMap.put("SEPTEMBER", Integer.valueOf(9));
        monthMap.put("OCTOBER", Integer.valueOf(10));
        monthMap.put("NOVEMBER", Integer.valueOf(11));
        monthMap.put("DECEMBER", Integer.valueOf(12));
    }

    public OpenSeaPostScraper(String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
    }

    @Override
    public List<Post> browse() {
        System.out.print(prefix + "Initializing ... ");
        getDriver().get("https://opensea.io/blog/news");
        System.out.println("Done");
        List<String> urls = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        for (WebElement e : getDriver().findElements(By.tagName("a"))) {
            String url = "https://opensea.io/blog/news" + e.getAttribute("href");
            urls.add(url);
        }

        for (String url : urls) {
            getDriver().get(url);
            String title = getDriver().findElement(By.className("headline")).getText();
            String content = getDriver().findElement(By.className("article-body-wrapper w-richtext")).getText();
            String time = getDriver().findElement(By.className("article-published-date"))
                            .findElements(By.tagName("div")).get(1).getText();
            String[] timeComponents = time.split(" ");
            DateTime dateTime = new DateTime(
                    Integer.parseInt(timeComponents[2]),
                    monthMap.get(timeComponents[0].toUpperCase()),
                    Integer.parseInt(timeComponents[1].replace(",", "")));
            posts.add(new Post(url, title, content, dateTime));
        }
        return posts;
    }
}
