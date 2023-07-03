package TwoUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LeaderBoard implements Initializable {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String DB_USERNAME = "your-username";
    private static final String DB_PASSWORD = "your-password";

    @FXML
    private TableView<Score> tableView;
    @FXML
    private TableColumn<Score, String> playerNameCol;
    @FXML
    private TableColumn<Score, Integer> scoreCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT player_name, score FROM scores ORDER BY score DESC LIMIT 10")) {

            ObservableList<Score> scoreList = FXCollections.observableArrayList();
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                Score scoreEntry = new Score(playerName, score);
                scoreList.add(scoreEntry);
            }

            tableView.setItems(scoreList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class Score {
        private final String playerName;
        private final int score;

        public Score(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }
    }
}
