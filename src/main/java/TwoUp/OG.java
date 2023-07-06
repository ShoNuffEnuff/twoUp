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
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static TwoUp.LoginData.username;

public class OG extends Application {


    static int winCount = 0;
    static int loseCount = 0;
    static int currentCount = 0;
    static Label score;
    static Label score2;
    static Label score3;
    static Circle coin1;
    static Circle coin2;
    static Button button;
    static Button button2;
    static Button sbutton;
    static Button ccbutton;
    static Button scbutton;
    static TextField usernameField;
    static PasswordField passwordField;
    static Label playerName;
    static RotateTransition rt1;
    static RotateTransition rt2;
    static Stage primaryStage;
    static Button lbutton;
    static Button logoutButton;
    static Label cclabel;
    static Button bg;
    static BorderPane root;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/2up";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Hadok3ns77!!";
    private String password;
    //private Stage primaryStage;
    private static Scene loginScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginform.fxml"));
        ogController controller = new ogController();
        Parent root = loader.load();
        loader.setController(controller);
        Scene loginScene = new Scene(root, 600, 400);

        // Create an instance of OG class
        OG ogInstance = new OG();

        // Set the loginScene as the loginScene property in the ogInstance
        ogInstance.loginScene = loginScene;

        // Set the primaryStage
        ogInstance.primaryStage = primaryStage;

        // Show the loginScene
        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void logoutUser() {
        // Close the twoUp scene and open the login scene
        primaryStage.close();
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static Scene twoUp() {
        bg = new Button("Change BG");
        bg.setOnAction(event -> {
            Background newBackground = createBackground();
            root.setBackground(newBackground);
        });
        score = new Label("Wins " + String.valueOf(winCount));
        score.setTextFill(Color.CYAN);
        score.setFont(Font.font("Stencil", 18));

        //score.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");

        score2 = new Label("Losses " + String.valueOf(loseCount));
        score2.setTextFill(Color.RED);
        score2.setFont(Font.font("Stencil", 18));


        //score2.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");
        currentCount = winCount - loseCount;
        score3 = new Label("Score " +(currentCount));
        score3.setTextFill(Color.LIMEGREEN);
        score3.setFont(Font.font("Stencil", 18));

        button = new Button("Heads");
        button.setOnAction(e -> flipCoin("heads"));

        button2 = new Button("Tails");
        button2.setOnAction(e -> flipCoin("tails"));
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            logoutUser();
            resetBackground();
        });

        cclabel = new Label("Pick a colour. Changes will apply when you close this window.");
        ccbutton = new Button("Change Colour");
        ccbutton.setOnAction(event -> {
            Stage colorChooserStage = new Stage();
            ColorPicker colorPicker = new ColorPicker();
            Button applyButton = new Button("Apply");

            applyButton.setOnAction(e -> {
                Color selectedColor = colorPicker.getValue();

                button.setTextFill(selectedColor);
                button2.setTextFill(selectedColor);
                score.setTextFill(selectedColor);
                score2.setTextFill(selectedColor);
                score3.setTextFill(selectedColor);
                playerName.setTextFill(selectedColor);

                colorChooserStage.close(); // Close the color chooser stage after applying the changes
            });

            VBox colorChooserLayout = new VBox(10);
            colorChooserLayout.getChildren().addAll(colorPicker, applyButton);
            colorChooserLayout.setAlignment(Pos.CENTER);
            colorChooserLayout.setPadding(new Insets(10));

            Scene colorChooserScene = new Scene(colorChooserLayout);

            colorChooserStage.setTitle("Color Chooser");
            colorChooserStage.setScene(colorChooserScene);
            colorChooserStage.show();
        });
        lbutton = new Button("Leaderboard");
        lbutton.setOnAction(e -> leaderBoard());
        scbutton = new Button("Change Button/Text Size");
        double[] textSizeValues = {18, 24, 30};
        final int[] currentSizeIndex = {0};
        scbutton.setOnAction(event -> {
            currentSizeIndex[0] = (currentSizeIndex[0] + 1) %
                    textSizeValues.length;
            double textSize = textSizeValues[currentSizeIndex[0]];

            button.setFont(Font.font("Arial",textSize));
            button2.setFont(Font.font("Arial",textSize));
            lbutton.setFont(Font.font(textSize));
            logoutButton.setFont(Font.font(textSize));
            scbutton.setFont(Font.font(textSize));
            //playerName.setFont(Font.font(textSize));
            score.setFont(Font.font("Stencil",textSize));
            score2.setFont(Font.font("Stencil",textSize));
            score3.setFont(Font.font("Stencil",textSize));
        });



        sbutton = new Button("Save");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        playerName = new Label("");
        playerName.setFont(new Font("Old English Text MT", 48));
        playerName.setTextFill(Color.WHITE);
        //playerName.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");
        Button registerButton = new Button("Register");
        //registerButton.setOnAction(e -> registerUser());

        Button loginButton = new Button("Login");
        //loginButton.setOnAction(e -> loginUser());

        //Button logoutButton = new Button("Logout");
        //logoutButton.setOnAction(e -> logoutUser());

        GridPane gridPane = new GridPane();
        GridPane scorePane = new GridPane();
        GridPane buttonPane = new GridPane();
        Button resetButton = new Button("Reset Colours");
        resetButton.setOnAction(event -> {
            // Reset the colors to their original values
            button.setTextFill(Color.BLACK);
            button2.setTextFill(Color.BLACK);
            score.setTextFill(Color.CYAN);
            score2.setTextFill(Color.RED);
            score3.setTextFill(Color.LIMEGREEN);
            playerName.setTextFill(Color.WHITE);
        });

        buttonPane.add(resetButton, 1, 0);

        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setHgap(0);
        buttonPane.setVgap(5);
        buttonPane.add(bg, 0, 0);
        buttonPane.add(ccbutton, 1, 6);
        buttonPane.add(scbutton, 0, 6);
        //gridPane.add(loginButton, 0, 3);
        gridPane.add(button, 0, 6);
        gridPane.add(button2, 1, 6);
        buttonPane.add(lbutton, 2, 6);
        //gridPane.add(sbutton, 2, 1);
        scorePane.add(score, 1, 2);
        scorePane.add(score2, 2, 2);
        scorePane.add(playerName, 0, 1, 3, 1);
        scorePane.add(score3,3,2 );
        buttonPane.add(logoutButton, 0, 7);

        coin1 = new Circle(70);
        coin2 = new Circle(70);
        coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
        coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
        coin1.setStroke(Color.BLACK);
        coin2.setStroke(Color.BLACK);

        HBox coinBox = new HBox(10);
        coinBox.getChildren().addAll(coin1, coin2);
        coinBox.setAlignment(Pos.BOTTOM_CENTER);
        VBox scoreContent = new VBox(20);
        scoreContent.getChildren().addAll(playerName, score, score2, score3);
        scoreContent.setAlignment(Pos.CENTER);

        Region scoreBackground = new Region();
        scoreBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        scoreBackground.setPrefSize(300, 200);

        StackPane scoreBox = new StackPane();
        scoreBox.getChildren().addAll(scoreBackground, scoreContent);
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(button, button2);
        buttonBox.setAlignment(Pos.CENTER);
        VBox buttonCoinBox = new VBox(10);
        buttonCoinBox.getChildren().addAll(buttonBox, coinBox);
        buttonCoinBox.setAlignment(Pos.CENTER);

        root = new BorderPane();
        root.setBackground(createBackground());
        root.setCenter(gridPane);
        root.setLeft(buttonPane);
        root.setBottom(buttonCoinBox);
        root.setRight(scoreBox); // Add the scoreBox to the right side of the BorderPane
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        BorderPane.setAlignment(buttonPane, Pos.CENTER);
        BorderPane.setAlignment(coinBox, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(scoreBox, Pos.CENTER);
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();

        return scene;
    }

    private static void leaderBoard() {
        new LeaderBoard();
    }





    private static int imageIndex = -1; // Initialize with -1 to start at index 0

    private static Background createBackground() {
        String[] imagePaths = {
                "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\aussieflag.gif",
                "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\aussieflagGS.gif",
                "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\ausflag.jpg",
                "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\ausflagGS.jpg"
        };

        // Increment the index for the next image (cycling back to 0 if necessary)
        imageIndex = (imageIndex + 1) % imagePaths.length;

        // Get the image path at the current index
        String imagePath = imagePaths[imageIndex];

        Image backgroundImage = new Image(imagePath);
        BackgroundImage backgroundImg = new BackgroundImage(
                backgroundImage,
                null,
                null,
                null,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        return new Background(backgroundImg);
    }

    private static void resetBackground() {
        imageIndex = -1; // Reset the image index to 0
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
            //ogController controller = new ogController();
            saveScoresToDatabase();
            System.out.println("Username: " + username);
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

    private static String saveScoresToDatabase() {
        //ogController controller = new ogController();
        String username = LoginData.getUsername();

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
        return username;
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