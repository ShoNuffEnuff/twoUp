package TwoUp;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OG extends Application {

    public static boolean OGAppVisible;
    static int winCount = 0;
    static int loseCount = 0;
    static Label score;
    static Label score2;
    static Circle coin1;
    static Circle coin2;
    static Button button;
    static Button button2;
    static Button sbutton;
    static Button ccbutton;
    static TextField usernameField;
    static PasswordField passwordField;
    static Label playerName;
    static RotateTransition rt1;
    static RotateTransition rt2;
    static boolean setOGAppVisible = false;
    static Stage primaryStage;
    static Scene scene1;
    static Parent root1;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/2up";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Hadok3ns77!!";
    private static String username;
    private String password;
    //private Stage primaryStage;
    private Scene loginScene;

    //public static void main(String[] args) {
        //launch(args);


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginform.fxml"));
        ogController controller = new ogController();
        Parent root = loader.load();
        loader.setController(controller);
        Scene scene = new Scene(root, 600, 400);

        this.primaryStage = primaryStage;

        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Show the main app only if login is successful
        //if (setOGAppVisible = Boolean.parseBoolean(("visible"))) {
            //OGVisible();
        }


    public static Scene twoUp() {
        score = new Label("Wins " + String.valueOf(winCount));
        score.setTextFill(Color.CYAN);
        score.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");

        score2 = new Label("Losses " + String.valueOf(loseCount));
        score2.setTextFill(Color.RED);
        score2.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");

        button = new Button("Heads");
        button.setOnAction(e -> flipCoin("heads"));

        button2 = new Button("Tails");
        button2.setOnAction(e -> flipCoin("tails"));

        ccbutton = new Button("Change Color");

        sbutton = new Button("Save");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        playerName = new Label("");
        playerName.setTextFill(Color.WHITE);
        playerName.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");
        Button registerButton = new Button("Register");
        //registerButton.setOnAction(e -> registerUser());

        Button loginButton = new Button("Login");
        //loginButton.setOnAction(e -> loginUser());

        Button logoutButton = new Button("Logout");
        //logoutButton.setOnAction(e -> logoutUser());

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        //gridPane.add(usernameField, 0, 0);
        //gridPane.add(passwordField, 0, 1);
        //gridPane.add(registerButton, 0, 2);
        //gridPane.add(loginButton, 0, 3);
        gridPane.add(button, 1, 0);
        gridPane.add(button2, 2, 0);
        gridPane.add(ccbutton, 1, 1);
        gridPane.add(sbutton, 2, 1);
        gridPane.add(score, 1, 2);
        gridPane.add(score2, 2, 2);
        gridPane.add(playerName, 0, 4, 3, 1);
        gridPane.add(logoutButton, 3, 0);

        coin1 = new Circle(70);
        coin2 = new Circle(70);
        coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
        coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
        coin1.setStroke(Color.BLACK);
        coin2.setStroke(Color.BLACK);

        HBox coinBox = new HBox(10);
        coinBox.getChildren().addAll(coin1, coin2);
        coinBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBackground(createBackground());
        root.setCenter(gridPane);
        root.setBottom(coinBox);
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        BorderPane.setAlignment(coinBox, Pos.CENTER);
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        return scene;
    }

    //public void OGVisible() {
        //primaryStage.setScene(scene1);
        //primaryStage.setTitle("2UP Game");
        //primaryStage.show();
        //OGAppVisible = true;
    //}

    private static Background createBackground() {
        Image backgroundImage = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\aussieflag.gif");
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, null, null, null,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        return new Background(backgroundImg);
    }

    private static void spinCoins() {
        rt1 = new RotateTransition(Duration.seconds(2), coin1);
        rt1.setByAngle(360 * 20);
        rt1.setCycleCount(Animation.INDEFINITE);
        rt1.setAutoReverse(true);
        rt1.setAxis(Rotate.Y_AXIS);
        rt1.play();

        rt2 = new RotateTransition(Duration.seconds(2), coin2);
        rt2.setByAngle(360 * 20);
        rt2.setCycleCount(Animation.INDEFINITE);
        rt2.setAutoReverse(true);
        rt2.setAxis(Rotate.Y_AXIS);
        rt2.play();
    }

    private static void flipCoin(String choice) {
        spinCoins();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            rt1.stop(); // Stop the rotation animation for coin1
            rt2.stop(); // Stop the rotation animation for coin2
            String result = getCoinResult();
            String result2 = getCoinResult2();

            if (result.equals("heads") && result2.equals("heads") && choice.equals("heads")) {
                winCount++;
                score.setText("Wins " + winCount);
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                showAlertWithAnimation("Flip Result", "You Win!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\winner.gif");
            } else if (result.equals("tails") && result2.equals("tails") && choice.equals("tails")) {
                winCount++;
                score.setText("Wins " + winCount);
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                showAlertWithAnimation("Flip Result", "You Win!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\winner.gif");
            } else if (result.equals("tails") && result2.equals("tails") && choice.equals("heads")) {
                loseCount++;
                score2.setText("Losses " + loseCount);
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                showAlertWithAnimation("Flip Result", "You Lose!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\loser.gif");
            } else if (result.equals("heads") && result2.equals("heads") && choice.equals("tails")) {
                loseCount++;
                score2.setText("Losses " + loseCount);
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                showAlertWithAnimation("Flip Result", "You Lose!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\loser.gif");
            } else if (result.equals("tails") && result2.equals("heads") && choice.equals("tails")) {
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                showAlert("Flip Again", "Flip again!");
            } else if (result.equals("tails") && result2.equals("heads") && choice.equals("heads")) {
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                showAlert("Flip Again", "Flip again!");
            } else {
                coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
                coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
                showAlert("Flip Again", "Flip again!");
            }
            ogController controller = new ogController();
            saveScoresToDatabase(controller);
        });

        delay.play();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private static void saveScoresToDatabase(ogController controller) {
        //ogController controller1 = new ogController();
        TextField usernameField = controller.getUsernameField();
        String username = usernameField.getText();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO scores (player, wins, losses, highscore) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setInt(2, winCount);
            statement.setInt(3, loseCount);
            statement.setInt(4, winCount - loseCount);
            statement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save scores to the database: " + e.getMessage());
        }
    }

    private static void showAlertWithAnimation(String title, String message, String gifFileName) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ImageView imageView = new ImageView(new Image(gifFileName));
        VBox vbox = new VBox(imageView);
        vbox.setAlignment(Pos.CENTER);

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(vbox);

        alert.setDialogPane(dialogPane);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            alert.setResult(ButtonType.CLOSE);
            alert.close();
        });

        alert.show();
        delay.play();
    }

    private static String getCoinResult() {
        return Math.random() < 0.5 ? "heads" : "tails";

    }
    private static String getCoinResult2() {
        return Math.random() < 0.5 ? "heads" : "tails";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
