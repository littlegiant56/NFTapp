package nftspy.fetcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nftspy.data.NFTCollection;
import nftspy.data.Post;
import nftspy.database.DatabaseHelper;
import nftspy.database.SQLiteHelper;
import nftspy.exceptions.IdenticalPrimaryKeyException;
import nftspy.exceptions.NullConfigException;
import nftspy.scraper.Scraper;
import nftspy.scraper.ScraperFactory;
import nftspy.scraper.ScraperType;
import nftspy.utils.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Fetcher {
    private Scraper scraper;

    public Fetcher(ScraperType type) throws IOException, NullConfigException {
        try {
            scraper = ScraperFactory.getCollectionScraper(type);
        } catch (IllegalStateException e) {
            scraper = ScraperFactory.getPostScraper(type);
        }
    }

    private void saveAsJson(List<?> records) throws IOException, NullConfigException {
        String crawledDirectory = Config.getInstance().getCrawledDirectory();
        String savePath = crawledDirectory + scraper.getPrefix().replace(" ", "")
                + System.currentTimeMillis() + ".json";
        Gson gson = new GsonBuilder().create();
        FileWriter fileWriter = new FileWriter(savePath, false);
        fileWriter.flush();
        gson.toJson(records, fileWriter);
        fileWriter.close();
    }

    private <T> void saveInDatabase(List<T> records) throws Exception {
        String databasePath = Config.DATABASE_PATH;
        DatabaseHelper databaseHelper = new SQLiteHelper(databasePath);
        try {
            databaseHelper.initialize();
        } catch (Exception ignored) {}

        for (T record : records) {
            try {
                if (record instanceof Post) {
                    databaseHelper.insert((Post) record);
                } else {
                    databaseHelper.insert((NFTCollection) record);
                }
            } catch (IdenticalPrimaryKeyException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void fetch() {
        String prefix = scraper.getPrefix();
        List<?> records = scraper.browse();
        System.out.println(prefix + records.size() + " records found");
        System.out.print(prefix + "Dumping to json ... ");
        try {
            saveAsJson(records);
            System.out.println("Done");
            System.out.print(prefix + "Saving into database ... ");
            saveInDatabase(records);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scraper.close();
        }
    }
}
