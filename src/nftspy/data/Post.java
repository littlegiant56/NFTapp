package nftspy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post {
    private final String content;
    private final String title;
    private final String url;
    private List<String> tags;
    private DateTime time;

    public Post(String url, String title, String content) {
        this.content = content;
        this.title = title;
        this.url = url;
        this.tags = parseTags(content);
    }

    public Post(String url, String title, String content, List<String> tags) {
        this(url, title, content);
        for (String tag : tags) {
            this.tags.add(tag.replace(" ", ""));
        }
    }

    public Post(String url, String title, String content, DateTime time) {
        this(url, title, content);
        this.time = time;
    }

    public Post(String url, String title, String content, List<String> tags, DateTime time) {
        this(url, title, content, tags);
        this.time = time;
    }

    public Post(String url, String title, String content, String tags, DateTime time) {
        this(url, title, content);
        List<String> tagList = Arrays.asList(tags.split(" "));
        for (String tag : tagList) {
            this.tags.add(tag.replace(" ", ""));
        }
        this.time = time;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public DateTime getTime() {
        return time;
    }

    public static List<String> parseTags(String input) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String hashtag = matcher.group().substring(1);
            hashtags.add("#" + hashtag.replace(" ", "_"));
        }
        return hashtags;
    }
}
