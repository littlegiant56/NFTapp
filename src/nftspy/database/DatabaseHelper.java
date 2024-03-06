package nftspy.database;

import nftspy.data.DateTime;
import nftspy.data.NFTCollection;
import nftspy.data.Post;

import java.util.List;

public interface DatabaseHelper {
    void initialize() throws Exception;
    void insert(Post post) throws Exception;
    void insert(NFTCollection collection) throws Exception;
    void delete(Post post) throws Exception;
    List<Post> search(String input, int numberOfPosts) throws Exception;
    List<Post> getPostList(DateTime start, DateTime end) throws Exception;
    List<String> getTagList(DateTime start, DateTime end) throws Exception;
    int getNumberOfPosts(DateTime start, DateTime end) throws Exception;
    double getPriceOnAverage(DateTime start, DateTime end) throws Exception;
    void close() throws Exception;
}
