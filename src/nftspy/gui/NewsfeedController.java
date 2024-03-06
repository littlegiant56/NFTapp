package nftspy.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import nftspy.data.DateTime;
import nftspy.data.Post;
import nftspy.database.DatabaseHelper;
import nftspy.database.SQLiteHelper;
import nftspy.utils.Config;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewsfeedController implements Initializable {
    private static final int NUMBER_OF_POSTS = 50;
    @FXML
    private VBox newsfeedVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseHelper db = new SQLiteHelper(Config.DATABASE_PATH);
        try {
            DateTime now = DateTime.now();
            DateTime then = DateTime.now();
            then.backInMonth(12);
            List<Post> posts = db.getPostList(then, now);
            addPostCard(posts);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPostCard(List<Post> posts) throws IOException {
        for (Post post : posts) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/fxml/postcard.fxml"));
            VBox postCard = loader.load();
            PostCardController postCardController = loader.getController();
            postCardController.setData(post);
            newsfeedVBox.getChildren().add(postCard);
        }
    }

    public void deleteAllCards() {
        newsfeedVBox.getChildren().clear();
    }

}
