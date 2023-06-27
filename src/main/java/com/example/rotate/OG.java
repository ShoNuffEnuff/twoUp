package com.example.rotate;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
    static Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        // Placeholder image URLs
        final URL url = new URL("https://via.placeholder.com/500x400.png");
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

        button.setGraphic(new ImageView(new Image("https://via.placeholder.com/40x40.png")));
        button2.setGraphic(new ImageView(new Image("https://via.placeholder.com/40x40.png")));

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

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(400);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(imageView);
        borderPane.setBottom(gridPane);
        BorderPane.setAlignment(gridPane, Pos.CENTER);

        Scene scene = new Scene(borderPane, 500, 500);
        primaryStage.setTitle("2UP Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static class Act1 implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Handle button action
        }
    }

    static class Act2 implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Handle button2 action
        }
    }

    static class Act3 implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Handle sbutton action
            saveData();
        }
    }

    static class Act4 implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Handle ccbutton action
        }
    }

    private static void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "your-username";
        String password = "your-password";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    private static void saveData() {
        String name = nameField.getText();
        String hashedName = hashString(name);

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String query = "INSERT INTO players (name, hashed_name) VALUES ('" + name + "', '" + hashedName + "')";
                statement.executeUpdate(query);
                System.out.println("Data saved successfully.");
            } catch (SQLException e) {
                System.out.println("Failed to save data.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection to the database is not established.");
        }
    }

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Failed to hash the string.");
            e.printStackTrace();
            return "";
        }
    }
}
