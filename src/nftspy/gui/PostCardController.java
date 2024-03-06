package nftspy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nftspy.data.Post;

public class PostCardController {

    @FXML
    private Label content;

    @FXML
    private Label date;

    @FXML
    private Label hashtags;

    @FXML
    private Label title;

    public void setData(Post post) {
        title.setText(post.getTitle());
        date.setText(post.getTime().toString());
        content.setText(post.getContent());
        hashtags.setText(String.join(" ", post.getTags()));
    }
}
