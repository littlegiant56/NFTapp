package nftspy.scraper;

import nftspy.data.DateTime;
import nftspy.data.Post;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirNFTsPostScraper extends PostScraper {
    private static final int MAX_POSTS_FETCHED = 100;
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

    public AirNFTsPostScraper(String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
    }

    private List<String> getPostURLs() {
        getDriver().get("https://www.airnfts.com/blog");
        WebElement list = getDriver().findElement(By.xpath("//div[@role='list']"));
        List<WebElement> postThumbnails = list.findElements(By.tagName("a"));
        List<String> postURLs = new ArrayList<>();
        for (WebElement e : postThumbnails) {
            String url = e.getAttribute("href");
            postURLs.add(url);
        }
        return postURLs;
    }

    @Override
    public List<Post> browse() {
        System.out.print(prefix + "Initializing ... ");
        List<String> postURLs = getPostURLs();
        System.out.println("Done");

        List<Post> posts = new ArrayList<>();

        for (String url : postURLs.subList(0, AirNFTsPostScraper.MAX_POSTS_FETCHED)) {
            getDriver().get(url);
            WebElement titleElement = getDriver().findElement(By.className("blog-header-h1"));
            String title = titleElement.getText();
            WebElement articleElement = getDriver().findElement(By.tagName("article"));
            String content = articleElement.getText();

            WebElement timeElement = getDriver().findElement(By.className("blog-detail-date"));
            String time = timeElement.getText();
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
