package nftspy.fetcher;

import nftspy.scraper.ScraperType;

public class FetchThread extends Thread {
    private Fetcher fetcher;

    public FetchThread(ScraperType type) {
        try {
            this.fetcher = new Fetcher(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        fetcher.fetch();
    }
}
