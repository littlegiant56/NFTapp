package nftspy.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import nftspy.analyzer.TrendAnalyzer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TrendingController implements Initializable {

    @FXML
    private Label firstHashTag;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label secondHashTag;

    @FXML
    private Label thirdHashTag;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<String> topTags = new TrendAnalyzer().getTopTags();
            firstHashTag.setText(topTags.get(0));
            secondHashTag.setText(topTags.get(1));
            thirdHashTag.setText(topTags.get(2));
            listView.setItems(FXCollections.observableList(topTags));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
