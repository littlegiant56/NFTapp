package nftspy.database;

import nftspy.data.DateTime;
import nftspy.data.NFTCollection;
import nftspy.data.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper implements DatabaseHelper {
    private Connection connection;

    public SQLiteHelper(String databasePath) {
        String jdbcURL = "jdbc:sqlite:" + databasePath;
        try {
            connection = DriverManager.getConnection(jdbcURL);
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() throws SQLException {
        createPostTable();
        createCollectionTable();
    }

    private void createPostTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS Post (" +
                "url VARCHAR PRIMARY KEY NOT NULL," +
                "title VARCHAR," +
                "content TEXT," +
                "tags VARCHAR," +
                "time DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)" +
                ");";
        stmt.executeUpdate(query);
        stmt.close();
    }

    private void createCollectionTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS Collection (" +
                "name VARCHAR PRIMARY KEY NOT NULL," +
                "price REAL NOT NULL," +
                "time DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)" +
                ");";
        stmt.executeUpdate(query);
        stmt.close();
    }

    @Override
    public void insert(Post post) throws Exception {
        String title = post.getTitle() == null? "No title" : post.getTitle();
        String tags = String.join(" ", post.getTags());
        String query = String.format(
                "INSERT OR IGNORE INTO Post " +
                        "(url, title, content, tags, time) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s');",
                post.getUrl().replace("'", "''"),
                title.replace("'", "''"),
                post.getContent().replace("'", "''"),
                tags.replace("'", "''"),
                post.getTime().toString().replace("'", "''"));

        Statement stmt = connection.createStatement();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stmt.close();
    }

    @Override
    public void insert(NFTCollection collection) throws Exception {
        String query = String.format(
                "REPLACE INTO Collection (name, price) " +
                "VALUES ('%s', %f);",
                collection.name().replace("'", ""), collection.price());
        Statement stmt = connection.createStatement();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stmt.close();
    }

    @Override
    public void delete(Post post) throws SQLException {

    }

    @Override
    public List<Post> search(String input, int numberOfPosts) throws SQLException {
        String query = "SELECT url, title, content, tags, time FROM Post " +
                "WHERE ";
        String[] terms = input.split(" ");
        for (int i = 0; i < terms.length; i++) {
            terms[i] = "(content LIKE '%" + terms[i].replace("'", "''") + "%'";
            terms[i] += " OR title LIKE '%" + terms[i].replace("'", "''") + "%')";
        }
        String s = String.join(" AND ", terms);
        query += s + ";";
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        List<Post> posts = new ArrayList<>();
        int count = 1;
        while (results.next() && count <= numberOfPosts) {
            String title = results.getString("title");
            String content = results.getString("content");
            String tags = results.getString("tags");
            String url = results.getString("url");
            String time = results.getString("time");
            posts.add(new Post(url, title, content, tags, DateTime.fromString(time)));
            count++;
        }
        stmt.close();
        return posts;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public List<Post> getPostList(DateTime start, DateTime end) throws Exception {
        String query = String.format(
                "SELECT url, title, content, tags, time FROM Post " +
                        "WHERE time BETWEEN '%s' AND '%s' " +
                        "ORDER BY time DESC;",
                start.toString(), end.toString());

        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        List<Post> posts = new ArrayList<>();
        while (results.next()) {
            String title = results.getString("title");
            String content = results.getString("content");
            String tags = results.getString("tags");
            String url = results.getString("url");
            String time = results.getString("time");
            posts.add(new Post(url, title, content, tags, DateTime.fromString(time)));
        }
        stmt.close();
        return posts;
    }

    @Override
    public List<String> getTagList(DateTime start, DateTime end) throws Exception {
        String query = String.format(
                "SELECT tags FROM Post " +
                        "WHERE time BETWEEN '%s' AND '%s' " +
                        "ORDER BY time DESC;",
                start.toString(), end.toString());

        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        List<String> tagList = new ArrayList<>();
        while (results.next()) {
            String tags = results.getString("tags");
            tagList.addAll(Post.parseTags(tags));
        }
        return tagList;
    }

    @Override
    public int getNumberOfPosts(DateTime start, DateTime end) throws Exception {
        String query = String.format(
                "SELECT COUNT(url) AS number_of_posts FROM Post " +
                        "WHERE time BETWEEN '%s' AND '%s';",
                start.toString(), end.toString());

        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        int numberOfPosts = results.getInt("number_of_posts");
        stmt.close();
        return numberOfPosts;
    }

    @Override
    public double getPriceOnAverage(DateTime start, DateTime end) throws Exception {
        String query = String.format(
                "SELECT AVG(price) AS price_on_avg FROM Collection " +
                        "WHERE time BETWEEN '%s' AND '%s';",
                start.toString(), end.toString());
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        double priceOnAverge = results.getDouble("price_on_avg");
        stmt.close();
        return priceOnAverge;
    }
}
