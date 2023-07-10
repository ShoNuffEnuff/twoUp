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

public class Main extends Application {

    static int winCount = 0;
    static int loseCount = 0;
    static int currentCount = winCount - loseCount;
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
    static Button resetButton;
    static Label flipLabel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/2up";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Hadok3ns77!!";
    private String password;
    //private Stage primaryStage;
    private static Scene loginScene;

    private static Image headsImage1;
    private static Image tailsImage1;
    static boolean headsCoin1 = true;
    static boolean headsCoin2 = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginform.fxml"));
        ogController controller = new ogController();
        Parent root = loader.load();
        loader.setController(controller);
        Scene loginScene = new Scene(root, 600, 400);

        // Create an instance of OG class
        Main ogInstance = new Main();

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
        winCount = 0;
        loseCount = 0;
        currentCount = 0;
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    private static void updateScore3() {
        currentCount = winCount - loseCount;
        score3.setText("Score " + currentCount);
    }
    public static Scene twoUp() {
        flipLabel = new Label("Come in spinner! Heads or Tails?");
        flipLabel.setFont(Font.font("Elephant",24));
        flipLabel.setTextFill(Color.WHITE);
        bg = new Button("Change BG");
        bg.setFont(Font.font("Arial",14));
        bg.setOnAction(event -> {
            Background newBackground = createBackground();
            root.setBackground(newBackground);
        });
        score = new Label("Wins " + winCount);
        score.setTextFill(Color.CYAN);
        score.setFont(Font.font("Stencil", 18));

        //score.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");

        score2 = new Label("Losses " + loseCount);
        score2.setTextFill(Color.RED);
        score2.setFont(Font.font("Stencil", 18));

        //score2.setStyle("-fx-font-family: Arial; -fx-font-size: 20;");
        //currentCount = winCount - loseCount;
        score3 = new Label("Score " + currentCount);
        //score3.setText("Score " +(currentCount));
        score3.setTextFill(Color.LIMEGREEN);
        score3.setFont(Font.font("Stencil", 18));
        //ruleLabel = new Label("");
        button = new Button("Heads");
        button.setFont(Font.font("Arial",14));
        button.setOnAction(e -> {
            flipCoin("heads");
            button.setDisable(true);
            button2.setDisable(true);
        });

        button2 = new Button("Tails");
        button2.setFont(Font.font("Arial",14));
        button2.setOnAction(e -> {
            flipCoin("tails");
            button2.setDisable(true);
            button.setDisable(true);
        });
        logoutButton = new Button("Logout");
        logoutButton.setFont(Font.font("Arial",14));
        logoutButton.setOnAction(event -> {
            logoutUser();
            resetBackground();
        });

        cclabel = new Label("Pick a colour");
        ccbutton = new Button("Change Colour");
        ccbutton.setFont(Font.font("Arial",14));
        ccbutton.setOnAction(event -> {
            ccbutton.setDisable(true);
            Stage colorChooserStage = new Stage();
            colorChooserStage.setOnCloseRequest(e -> {
                ccbutton.setDisable(false);
            });
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
                flipLabel.setTextFill(selectedColor);

                colorChooserStage.close(); // Close the color chooser stage after applying the changes
                ccbutton.setDisable(false);
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
        lbutton.setFont(Font.font("Arial",14));
        lbutton.setOnAction(e -> {
            leaderBoard();
            lbutton.setDisable(true);
        });
        scbutton = new Button("Change Size");
        scbutton.setFont(Font.font("Arial",14));
        double[] textSizeValues = {14, 20, 26};
        final int[] currentSizeIndex = {0};
        scbutton.setOnAction(event -> {
            currentSizeIndex[0] = (currentSizeIndex[0] + 1) %
                    textSizeValues.length;
            double textSize = textSizeValues[currentSizeIndex[0]];

            button.setFont(Font.font("Arial",textSize));
            button2.setFont(Font.font("Arial",textSize));
            lbutton.setFont(Font.font(textSize));
            bg.setFont(Font.font(textSize));
            resetButton.setFont(Font.font(textSize));
            ccbutton.setFont(Font.font(textSize));
            logoutButton.setFont(Font.font(textSize));
            scbutton.setFont(Font.font(textSize));
            //playerName.setFont(Font.font(textSize));
            score.setFont(Font.font("Stencil",textSize));
            score2.setFont(Font.font("Stencil",textSize));
            score3.setFont(Font.font("Stencil",textSize));
        });



        //sbutton = new Button("Save");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        playerName = new Label("");
        playerName.setFont(new Font("Old English Text MT", 48));
        playerName.setTextFill(Color.WHITE);
        GridPane gridPane = new GridPane();
        GridPane scorePane = new GridPane();
        GridPane buttonPane = new GridPane();
        resetButton = new Button("Reset Colours");
        resetButton.setFont(Font.font("Arial",14));
        resetButton.setOnAction(event -> {
            // Reset the colors to their original values
            button.setTextFill(Color.BLACK);
            button2.setTextFill(Color.BLACK);
            score.setTextFill(Color.CYAN);
            score2.setTextFill(Color.RED);
            score3.setTextFill(Color.LIMEGREEN);
            playerName.setTextFill(Color.WHITE);
            flipLabel.setTextFill(Color.WHITE);
        });

        buttonPane.add(resetButton, 1, 0);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setHgap(0);
        buttonPane.setVgap(5);
        buttonPane.add(bg, 0, 1);
        buttonPane.add(ccbutton, 0, 2);
        buttonPane.add(scbutton, 0, 3);
        //buttonPane.add(resetButton, 0, 4);
        gridPane.add(button, 0, 6);
        gridPane.add(button2, 1, 6);
        buttonPane.add(lbutton, 3, 0);
        //gridPane.add(sbutton, 2, 1);
        scorePane.add(score, 1, 2);
        scorePane.add(score2, 2, 2);
        scorePane.add(playerName, 0, 1, 3, 1);
        scorePane.add(score3,3,2 );
        buttonPane.add(logoutButton, 1, 0);

        coin1 = new Circle(70);
        coin2 = new Circle(70);
        coin1.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png"), 0, 0, 1, 1, true));
        coin2.setFill(new ImagePattern(new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png"), 0, 0, 1, 1, true));
        coin1.setStroke(Color.BLACK);
        coin2.setStroke(Color.BLACK);
        HBox buttonBox2 = new HBox();
        buttonBox2.setSpacing(10);
        buttonBox2.getChildren().addAll(bg, scbutton, ccbutton, lbutton, logoutButton, resetButton);
        buttonBox2.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox2.setPadding(new Insets(10));


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
        scoreBox.getChildren().addAll(scoreBackground, scoreContent, buttonBox2);
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(button, button2);
        buttonBox.setAlignment(Pos.CENTER);
        VBox buttonCoinBox = new VBox(10);
        buttonCoinBox.getChildren().addAll(buttonBox, coinBox, flipLabel);
        buttonCoinBox.setAlignment(Pos.CENTER);


// Create a new HBox to hold the buttons in rows of three
        // Create the first row of buttons
        HBox firstRow = new HBox(10);
        firstRow.setAlignment(Pos.CENTER);
        firstRow.getChildren().addAll(ccbutton, scbutton, resetButton);

// Create the second row of buttons
        HBox secondRow = new HBox(10);
        secondRow.setAlignment(Pos.CENTER);
        secondRow.getChildren().addAll(bg, lbutton, logoutButton);

// Create the VBox to hold the rows of buttons
        VBox rowsBox = new VBox(10);
        rowsBox.setAlignment(Pos.BOTTOM_CENTER);
        rowsBox.getChildren().addAll(firstRow, secondRow);
        buttonBox2.getChildren().add(rowsBox);
        scoreBox.getChildren().add(rowsBox);
        root = new BorderPane();
        root.setBackground(createBackground());
        root.setCenter(buttonCoinBox);
        //root.setLeft(buttonsBox);
        //root.setBottom(flipLabel);
        root.setRight(scoreBox);
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
        primaryStage.setResizable(false);


        return scene;
    }

    private static void leaderBoard() {
        new LeaderBoard();
    }

    private static int imageIndex = -1;

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
        // Update the heads and tails images for coin1 and coin2 based on the background
        if (imagePath.equals("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\aussieflag.gif")) {
            headsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png");
            tailsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png");
            button.setTextFill(Color.BLACK);
            button2.setTextFill(Color.BLACK);
            score.setTextFill(Color.CYAN);
            score2.setTextFill(Color.RED);
            score3.setTextFill(Color.LIMEGREEN);
            playerName.setTextFill(Color.WHITE);
        } else if (imagePath.equals("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\aussieflagGS.gif")) {
            headsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\silver1.jpg");
            tailsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\silver2.jpg");
            button.setTextFill(Color.BLACK);
            button2.setTextFill(Color.BLACK);
            score.setTextFill(Color.WHITE);
            score2.setTextFill(Color.WHITE);
            score3.setTextFill(Color.WHITE);
            playerName.setTextFill(Color.WHITE);
            flipLabel.setTextFill(Color.WHITE);
        } else if (imagePath.equals("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\ausflagGS.jpg")) {
            headsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\silver1.jpg");
            tailsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\silver2.jpg");
            button.setTextFill(Color.BLACK);
            button2.setTextFill(Color.BLACK);
            score.setTextFill(Color.WHITE);
            score2.setTextFill(Color.WHITE);
            score3.setTextFill(Color.WHITE);
            playerName.setTextFill(Color.WHITE);
            flipLabel.setTextFill(Color.WHITE);
        } else {
            headsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold1.png");
            tailsImage1 = new Image("C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\gold2.png");
            button.setTextFill(Color.BLACK);
            button2.setTextFill(Color.BLACK);
            score.setTextFill(Color.CYAN);
            score2.setTextFill(Color.RED);
            score3.setTextFill(Color.LIMEGREEN);
            playerName.setTextFill(Color.WHITE);
        }
        // Update the coin images
        headsCoin2 = false;
        updateCoinImages(headsCoin1, headsCoin2);


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

            //updateCoinImages(headsCoin1, result.equals("heads"), result2.equals("heads"));

            if (result.equals("heads") && result2.equals("heads") && choice.equals("heads")) {
                winCount++;
                //currentCount++;
                score.setText("Wins " + winCount);
                //score3.setText("Score " + currentCount);
                headsCoin1 = true;  // Set heads for coin1
                headsCoin2 = true;  // Set heads for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlertWithAnimation("Flip Result", "You Win!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\winner.gif");
                flipLabel.setText("HEADS! Flip Again?");
            } else if (result.equals("tails") && result2.equals("tails") && choice.equals("tails")) {
                winCount++;
                //currentCount++;
                score.setText("Wins " + winCount);
                score3.setText("Score " + currentCount);
                headsCoin1 = false;  // Set tails for coin1
                headsCoin2 = false;  // Set tails for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlertWithAnimation("Flip Result", "You Win!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\winner.gif");
                flipLabel.setText("TAILED THEM! Flip Again?");
            } else if (result.equals("tails") && result2.equals("tails") && choice.equals("heads")) {
                loseCount++;
                //currentCount++;
                score2.setText("Losses " + loseCount);
                score3.setText("Score " + currentCount);
                headsCoin1 = false;  // Set tails for coin1
                headsCoin2 = false;  // Set tails for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlertWithAnimation("Flip Result", "You Lose!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\loser.gif");
                flipLabel.setText("You Lose! Flip Again?");
            } else if (result.equals("heads") && result2.equals("heads") && choice.equals("tails")) {
                loseCount++;
                //currentCount++;
                score2.setText("Losses " + loseCount);
                score3.setText("Score " + currentCount);
                headsCoin1 = true;  // Set heads for coin1
                headsCoin2 = true;  // Set heads for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlertWithAnimation("Flip Result", "You Lose!", "C:\\Users\\Sho\\IdeaProjects\\TwoUp\\src\\main\\resources\\TwoUp\\img\\loser.gif");
                flipLabel.setText("You Lose! Flip Again?");
            } else if (result.equals("tails") && result2.equals("heads") && choice.equals("tails")) {
                headsCoin1 = false;  // Set tails for coin1
                headsCoin2 = true;  // Set heads for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlert("Flip Again", "Flip again!");
                flipLabel.setText("Odds! Flip Again?");
            } else if (result.equals("tails") && result2.equals("heads") && choice.equals("heads")) {
                headsCoin1 = false;  // Set tails for coin1
                headsCoin2 = true;  // Set heads for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlert("Flip Again", "Flip again!");
                flipLabel.setText("Odds! Flip Again?");
            } else {
                headsCoin1 = true;  // Set heads for coin1
                headsCoin2 = false;  // Set tails for coin2
                updateCoinImages(headsCoin1, headsCoin2);
                showAlert("Flip Again", "Flip again!");
                flipLabel.setText("Odds! Flip Again?");
            }
            saveScoresToDatabase();
            updateScore3();
            button.setDisable(false);
            button2.setDisable(false);
        });

        delay.play();
    }



    private static void updateCoinImages(boolean headsCoin1, boolean headsCoin2) {
        coin1.setFill(new ImagePattern(headsCoin1 ? headsImage1 : tailsImage1));
        coin2.setFill(new ImagePattern(headsCoin2 ? headsImage1 : tailsImage1));
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private static String saveScoresToDatabase() {
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
        return ("Scores updated for " + username);
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