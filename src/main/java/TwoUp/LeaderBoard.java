package TwoUp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static TwoUp.OG.primaryStage;
import static TwoUp.OG.twoUp;

public class LeaderBoard {

    @FXML
    private TableView<Score> tableView;
    @FXML
    private Button returnButton;

    public LeaderBoard() {
        initializeTableView();

        fetchTopScoresFromDatabase(); // Fetch top scores from MySQL table and populate the TableView

        Scene scene = new Scene(tableView, 600, 400);
        String css = this.getClass().getResource("leaderboard.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scores");
        primaryStage.show();
    }

    private void initializeTableView() {
        TableColumn<Score, String> playerColumn = new TableColumn<>("Player");
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));

        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("High Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        tableView = new TableView<>();
        tableView.getColumns().addAll(playerColumn, scoreColumn);
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

    public void returnButtonClicked(ActionEvent actionEvent) {
        returnButton.setDisable(true);
        twoUp();
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

