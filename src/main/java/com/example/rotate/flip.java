package p1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

public class OG extends Application {

    static int winCount = 0;
    static int loseCount = 0;
    static Label score;
    static Label score2;
    static TextField nameField;
    static Button button;
    static Button button2;
    static Button sbutton;
    static Button ccbutton;
    static Image image;
    static ImageView imageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        final URL url = new URL("https://thumbs.gfycat.com/OrnateBriskAfricancivet-size_restricted.gif");
        image = new Image(url.toString());

        score = new Label("Wins " + String.valueOf(winCount));
        score.setStyle("-fx-text-fill: green; -fx-font-family: Arial; -fx-font-size: 20;");
        score2 = new Label("Losses " + String.valueOf(loseCount));
        score2.setStyle("-fx-text-fill: red; -fx-font-family: Arial; -fx-font-size: 20;");
        nameField = new TextField("Enter Your Name");

        button = new Button("Heads");
        button2 = new Button("Tails");
        sbutton = new Button("Save");
        ccbutton = new Button("Change Color");

        button.setGraphic(new ImageView(new Image("gold1.jpg")));
        button2.setGraphic(new ImageView(new Image("gold2.jpg")));

        button.setOnAction(new Act1());
        button2.setOnAction(new Act2());
        ccbutton.setOnAction(new Act4());
        sbutton.setOnAction(new Act3());

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Open");
        MenuItem fileItem = new MenuItem("File");
        MenuItem leaderItem = new MenuItem("Leaderboard");
        menu.getItems().addAll(fileItem, leaderItem);
        menuBar.getMenus().add(menu);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(nameField, 0, 0);
        gridPane.add(button, 0, 1);
        gridPane.add(button2, 1, 1);
        gridPane.add(ccbutton, 0, 2);
        gridPane.add(sbutton, 1, 2);
        gridPane.add(score, 0, 3);
        gridPane.add(score2, 1, 3);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(new JLabel(new ImageIcon(image)));

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(swingNode);
        borderPane.setBottom(gridPane);
        BorderPane.setAlignment(gridPane, Pos.CENTER);

        Scene scene = new Scene(borderPane, 500, 500);
        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static class Act1 implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event.
