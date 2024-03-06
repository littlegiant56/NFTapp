package nftspy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import nftspy.exceptions.NullConfigException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static Config INSTANCE = null;

    public static final String APPDATA_DIRECTORY;
    public static final String CONFIG_PATH;
    public static final String DATABASE_PATH;
    public static final String CRAWLED_DIRECTORY;
    private String TWITTER_USERNAME;
    private String TWITTER_PASSWORD;
    private String CHROME_PATH;
    private String CHROME_DRIVER_PATH;

    static {
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            APPDATA_DIRECTORY = System.getenv("AppData") + "/NFTSpy/";
        } else {
            APPDATA_DIRECTORY = System.getProperty("user.home") + "/Library/Application Support/NFTSpy/";
        }

        CONFIG_PATH = APPDATA_DIRECTORY + "config/config.json";
        CRAWLED_DIRECTORY = APPDATA_DIRECTORY + "crawled/";
        DATABASE_PATH = APPDATA_DIRECTORY + "database/nft.db";

        File file = new File(APPDATA_DIRECTORY);
        if (!file.exists()) {
            initializeAppDataDirectories();
        }
    }

    private Config() {}

    private static Config fromJson(String path) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));

        Config config = gson.fromJson(reader, Config.class);
        return config;
    }

    public static Config getInstance() throws IOException, NullConfigException {
        if (Config.INSTANCE == null) {
            try {
                Config temp = Config.fromJson(CONFIG_PATH);
                if (temp.getTwitterPassword() == null
                        || temp.getTwitterUsername() == null
                        || temp.getChromeDriverPath() == null
                        || temp.getChromePath() == null) {
                    throw new NullConfigException("Null configuration found");
                }
                INSTANCE = temp;
            } catch (IOException e) {
                throw new IOException("No configuration found");
            }
        }
        return Config.INSTANCE;
    }

    private static void initializeAppDataDirectories() {
        new File(APPDATA_DIRECTORY).mkdir();
        new File(CRAWLED_DIRECTORY).mkdir();
        new File(DATABASE_PATH).getParentFile().mkdir();
        new File(CONFIG_PATH).getParentFile().mkdir();
    }

    public static void dumpConfig(String username, String password, String chromePath, String driverPath) throws IOException {
        Gson gson = new GsonBuilder().create();
        FileWriter fileWriter = new FileWriter(CONFIG_PATH, false);
        fileWriter.flush();
        Config config = new Config();
        config.TWITTER_USERNAME = username;
        config.TWITTER_PASSWORD = password;
        config.CHROME_PATH = chromePath;
        config.CHROME_DRIVER_PATH = driverPath;
        gson.toJson(config, fileWriter);
        System.out.println("Configuration saved");
        fileWriter.close();
    }

    public String getCrawledDirectory() {
        return CRAWLED_DIRECTORY;
    }

    public String getTwitterUsername() {
        return TWITTER_USERNAME;
    }

    public String getTwitterPassword() {
        return TWITTER_PASSWORD;
    }

    public String getChromePath() {
        return CHROME_PATH;
    }

    public String getChromeDriverPath() {
        return CHROME_DRIVER_PATH;
    }
}
