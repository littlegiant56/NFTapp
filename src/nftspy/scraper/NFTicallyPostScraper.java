package nftspy.scraper;

import nftspy.data.DateTime;
import nftspy.data.Post;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFTicallyPostScraper extends PostScraper {
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

    public NFTicallyPostScraper(String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
    }

    @Override
    public List<Post> browse() {
        System.out.println(prefix + "Initilizing ... ");
        getDriver().get("https://www.nftically.com/blog/");
        System.out.println(prefix + "Done");
        List<WebElement> elements = getDriver().findElements(By.className("blog-title"));
        List<String> urls = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        for (WebElement e : elements) {
            urls.add(e.findElement(By.tagName("a")).getAttribute("href"));
        }
        for (String url : urls) {
            getDriver().get(url);
            String title = getDriver().findElement(By.className("page-title")).getText();
            String content = getDriver().findElement(By.className("blog-detail-wrap")).getText();
            List<String> tags = new ArrayList<>();
            for (WebElement e : getDriver().findElements(By.className("saspot-label"))) {
                tags.add("#" + e.getText());
            }

            WebElement blogDate = getDriver().findElement(By.className("blog-date"));
            List<WebElement> liTags = blogDate.findElements(By.tagName("li"));
            WebElement timeElement = liTags.get(1);
            String time = timeElement.getText();
            String[] timeComponents = time.split(" ");
            DateTime dateTime = new DateTime(
                    Integer.parseInt(timeComponents[2]),
                    monthMap.get(timeComponents[0].toUpperCase()),
                    Integer.parseInt(timeComponents[1].replace(",", "")));

            posts.add(new Post(url, title, content, tags, dateTime));
        }
        return posts;
    }
}
