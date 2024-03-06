// Dinh Viet Ha - 20215042


package nftspy.scraper;
import nftspy.data.Post;

import java.util.List;

public abstract class PostScraper extends Scraper {
    public PostScraper(String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
    }

    public abstract List<Post> browse();
}
