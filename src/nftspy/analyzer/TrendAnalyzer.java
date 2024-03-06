package nftspy.analyzer;

import nftspy.data.DateTime;
import nftspy.database.DatabaseHelper;
import nftspy.database.SQLiteHelper;
import nftspy.utils.Config;

import java.util.*;

public class TrendAnalyzer {
    private static final int NUMBER_OF_TAGS = 20;
    private DatabaseHelper db = new SQLiteHelper(Config.DATABASE_PATH);

    public TrendAnalyzer() {}

    public List<String> getTopTags() throws Exception {
        Map<String, Integer> frequencyMap = new HashMap<>();
        DateTime now = DateTime.now();
        DateTime then = DateTime.now();
        then.backInMonth(1);
        List<String> tags = db.getTagList(then, now);
        for (String tag : tags) {
            Integer freq = frequencyMap.get(tag);
            if (freq == null) {
                frequencyMap.put(tag, 1);
            } else {
                frequencyMap.put(tag, frequencyMap.get(tag) + 1);
            }
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(frequencyMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        LinkedHashMap<String, Integer> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return new ArrayList<>(sortedHashMap.keySet());
    }
}
