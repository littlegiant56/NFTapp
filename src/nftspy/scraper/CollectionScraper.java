package nftspy.scraper;

import nftspy.data.NFTCollection;

import java.util.List;

public abstract class CollectionScraper extends Scraper {
    public CollectionScraper(String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
    }

    public abstract List<NFTCollection> browse();
}
