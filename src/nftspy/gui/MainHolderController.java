package nftspy.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nftspy.data.Post;
import nftspy.database.DatabaseHelper;
import nftspy.database.SQLiteHelper;
import nftspy.exceptions.NullConfigException;
import nftspy.fetcher.FetchThread;
import nftspy.scraper.ScraperType;
import nftspy.utils.Config;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainHolderController implements Initializable {
    @FXML
    private HBox mainPane;

    @FXML
    private Button analyzerButton;

    @FXML
    private VBox contentVBox;

    @FXML
    private Button fetchButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button settingsButton;

    @FXML
    private Button trendingButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    double xOffset, yOffset;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String contentResourcePath = "/resources/fxml/newsfeed.fxml";
        try {
            Config.getInstance();
        } catch (IOException | NullConfigException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setResizable(false);
            alert.setTitle("Warning!");
            alert.setHeaderText("No configuration found");
            alert.setContentText("Initialize the settings first");
            ButtonType choice = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (choice == ButtonType.CANCEL) {
                System.exit(0);
            }
            contentResourcePath = "/resources/fxml/settings.fxml";
        }
        try {
            goTo(contentResourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goTo(String fxmlPath) throws IOException {
        if (contentVBox.getChildren().size() == 2) {
            contentVBox.getChildren().remove(1);
        }
        Parent frame = FXMLLoader.load(getClass().getResource(fxmlPath));
        contentVBox.getChildren().add(frame);
    }

    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        goTo("/resources/fxml/newsfeed.fxml");
    }

    @FXML
    void onAnalyzerButtonClicked(ActionEvent event) throws IOException {
        goTo("/resources/fxml/analyzer.fxml");
    }

    @FXML
    void onTrendingButtonClicked(ActionEvent event) throws IOException {
        goTo("/resources/fxml/trending.fxml");
    }

    @FXML
    void onSettingsButtonClicked(ActionEvent event) throws IOException {
        goTo("/resources/fxml/settings.fxml");
    }

    @FXML
    void onSearchButtonClicked(ActionEvent event) throws Exception {
        DatabaseHelper db = new SQLiteHelper(Config.DATABASE_PATH);
        String input = searchTextField.getText();
        if (!input.replace(" ", "").equals("")) {
            List<Post> posts = db.search(input, 50);
            VBox vbox = (VBox) ((Node) event.getSource()).getParent().getParent();
            vbox.getChildren().remove(1);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/fxml/newsfeed.fxml"));
            ScrollPane newsfeed = loader.load();
            NewsfeedController controller = loader.getController();
            controller.deleteAllCards();
            controller.addPostCard(posts);
            vbox.getChildren().add(newsfeed);
        }
        db.close();
    }

    @FXML
    void onFetchButtonClicked(ActionEvent event) {
        try {
            new FetchThread(ScraperType.NFTICALLY).start();
            new FetchThread(ScraperType.AIRNTFS).start();
            new FetchThread(ScraperType.COINGECKO).start();
//            new FetchThread(new Fetcher(ScraperType.TWITTER)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void onMainPaneDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    void onMainPanePressed(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }
}
