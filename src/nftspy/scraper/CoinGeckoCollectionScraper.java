package nftspy.scraper;

import nftspy.data.NFTCollection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CoinGeckoCollectionScraper extends CollectionScraper {
    public CoinGeckoCollectionScraper(String chromeDriverPath, String chromePath) {
        super(chromeDriverPath, chromePath);
    }

    @Override
    public List<NFTCollection> browse() {
        System.out.println(prefix + "Initilizing ... ");
        getDriver().get("https://www.coingecko.com/en/nft");
        System.out.println(prefix + "Done");

        List<NFTCollection> collections = new ArrayList<>();
        List<WebElement> cards = getDriver().findElements(By.tagName("tr"));

        for (WebElement card : cards.subList(1, cards.size())) {
            String name = card.findElement(By.tagName("a")).getText();
            String priceInString = card.findElements(By.tagName("td")).get(3).getAttribute("data-sort");;
            double price = Double.parseDouble(priceInString.replace(",", "").replace("$", ""));
            collections.add(new NFTCollection(name, price));
        }
        return collections;
    }
}
