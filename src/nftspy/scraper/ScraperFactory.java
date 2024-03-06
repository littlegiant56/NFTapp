package nftspy.scraper;

import nftspy.exceptions.NullConfigException;
import nftspy.utils.Config;

import java.io.IOException;

public class ScraperFactory {
    private ScraperFactory() {}

    public static PostScraper getPostScraper(ScraperType type) throws IOException, NullConfigException {
        final Config CONFIG = Config.getInstance();
        switch (type) {
            case TWITTER -> {
                return new TwitterPostScraper(
                        CONFIG.getTwitterUsername(),
                        CONFIG.getTwitterPassword(),
                        CONFIG.getChromeDriverPath(),
                        CONFIG.getChromePath());
            }

            case AIRNTFS -> {
                return new AirNFTsPostScraper(
                        CONFIG.getChromeDriverPath(),
                        CONFIG.getChromePath());
            }

            case NFTICALLY -> {
                return new NFTicallyPostScraper(
                        CONFIG.getChromeDriverPath(),
                        CONFIG.getChromePath());
            }

            case OPENSEA -> {
                return new OpenSeaPostScraper(
                        CONFIG.getChromeDriverPath(),
                        CONFIG.getChromePath());
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static CollectionScraper getCollectionScraper(ScraperType type) throws IOException, NullConfigException {
        final Config CONFIG = Config.getInstance();
        switch (type) {
            case COINGECKO -> {
                return new CoinGeckoCollectionScraper(
                        CONFIG.getChromeDriverPath(),
                        CONFIG.getChromePath());
            }
            
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
