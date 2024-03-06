package nftspy.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import nftspy.exceptions.NullConfigException;
import nftspy.utils.Config;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private AnchorPane holder;

    @FXML
    private TextField chromeField;

    @FXML
    private TextField driverField;

    @FXML
    private ImageView notificationIcon;

    @FXML
    private Label notificationLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String username = "";
        String password = "";
        String chromePath = "";
        String driverPath = "";
        try {
            Config config = Config.getInstance();
            username = config.getTwitterUsername();
            password = config.getTwitterPassword();
            chromePath = config.getChromePath();
            driverPath = config.getChromeDriverPath();
        } catch (NullConfigException | IOException ignored) {}
        usernameField.setText(username);
        passwordField.setText(password);
        chromeField.setText(chromePath);
        driverField.setText(driverPath);
    }

    @FXML
    void onSaveButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String chromePath = chromeField.getText();
        String driverPath = driverField.getText();

        if (username.equals("") || password.equals("")
                || chromePath.equals("") || driverPath.equals("")) {
            notificationLabel.setText("Please complete all the fields");
            notificationLabel.setStyle("-fx-text-fill: -color-danger-7");
            notificationIcon.setImage(new Image("/resources/images/warning.png"));
        } else {
            try {
                Config.dumpConfig(username, password, chromePath, driverPath);
            } catch (IOException ignored) {}

            notificationLabel.setText("Settings saved");
            notificationLabel.setStyle("-fx-text-fill: -color-success-7");
            notificationIcon.setImage(new Image("/resources/images/done.png"));
        }
    }
}
