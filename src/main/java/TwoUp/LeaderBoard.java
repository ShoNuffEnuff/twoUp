package TwoUp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LeaderBoard {
    private final int INITIAL_TEXT_SIZE = 12;
    private int textSize = INITIAL_TEXT_SIZE;

    @FXML
    private TableView<Score> tableView;

    public LeaderBoard() {
        initializeTableView();

        fetchTopScoresFromDatabase(); // Fetch top scores from MySQL table and populate the TableView
        Stage leaderStage = new Stage();
        Scene scene = new Scene(tableView, 600, 400);
        leaderStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        String css = this.getClass().getResource("leaderboard.css").toExternalForm();
        scene.getStylesheets().add(css);
        tableView.getStyleClass().add("transparent-table-view");
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                leaderStage.close();
            }
        });

        leaderStage.setScene(scene);
        leaderStage.setTitle("Scores");
        leaderStage.show();
    }

    private void initializeTableView() {
        TableColumn<Score, String> playerColumn = new TableColumn<>("Player   -    " + "Press Escape to close.");
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        playerColumn.setSortable(false);
        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("High Score   -    " + "pgUp/pgDn to change text size.");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setSortable(false);
        tableView = new TableView<>();
        tableView.getColumns().addAll(playerColumn, scoreColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setTableViewTextSize(textSize);
    }

    private void fetchTopScoresFromDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/2up", "root", "Hadok3ns77!!");
             Statement statement = connection.createStatement()) {

            // Execute SQL query to fetch top scores
            String query = "SELECT player, highscore FROM scores ORDER BY highscore DESC LIMIT 10";
            ResultSet resultSet = statement.executeQuery(query);

            // Create an ObservableList to store Score objects
            ObservableList<Score> scores = FXCollections.observableArrayList();

            // Iterate over the result set and create Score objects
            while (resultSet.next()) {
                String player = resultSet.getString("player");
                int highScore = resultSet.getInt("highscore");
                Score score = new Score(player, highScore);

                // Calling the getScore method
                //int retrievedScore = score.getScore();

                // You can now use the retrievedScore variable as needed
                //System.out.println("Retrieved score: " + retrievedScore);

                scores.add(score);
            }

            // Bind the ObservableList to the TableView
            tableView.setItems(scores);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.PAGE_UP) {
            increaseTextSize();
        } else if (event.getCode() == KeyCode.PAGE_DOWN) {
            decreaseTextSize();
        }
    }

    private void increaseTextSize() {
        textSize += 2;
        setTableViewTextSize(textSize);
    }

    private void decreaseTextSize() {
        if (textSize > INITIAL_TEXT_SIZE) {
            textSize -= 2;
            setTableViewTextSize(textSize);
        }
    }

    private void setTableViewTextSize(int size) {
        String fontName = Font.getDefault().getFamily();
        tableView.setStyle("-fx-font-family: " + fontName + "; -fx-font-size: " + size + "px;");
    }


    public class Score {
        private final SimpleStringProperty player;
        private final SimpleIntegerProperty score;

        public Score(String player, int score) {
            this.player = new SimpleStringProperty(player);
            this.score = new SimpleIntegerProperty(score);
        }

        public String getPlayer() {
            return player.get();
        }

        public void setPlayer(String player) {
            this.player.set(player);
        }

        public int getScore() {
            return score.get();
        }

        public void setScore(int score) {
            this.score.set(score);
        }

        }
    }

