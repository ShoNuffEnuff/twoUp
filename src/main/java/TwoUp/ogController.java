package TwoUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static TwoUp.OG.twoUp;

public class ogController {
    private LoginService loginService;

    @FXML
    private Button handleRegisterButton;

    @FXML
    private Button handleLoginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public ogController() {
        // Instantiate the LoginService
        this.loginService = new LoginService();
    }

    /*public ogController(LoginService loginService) {
        this.loginService = loginService;
    }*/

    public void savedLoginData(){
        String username = usernameField.getText();

        LoginData data = LoginData.getInstance();
        data.setUsername(username);
    }
    @FXML
    private void handleRegisterButtonClicked(ActionEvent event) {
        handleRegisterButton.setDisable(true); // Disable the register button

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Registration Failed", "Please enter a username and password.");
            handleRegisterButton.setDisable(false); // Enable the register button
            return;
        }

        if (loginService.registerUser(username, password)) {
            showSuccessAlert("Registration Successful", "Account created successfully.");
        } else {
            showErrorAlert("Registration Failed", "Username already exists.");
        }

        handleRegisterButton.setDisable(false); // Enable the register button
    }

    @FXML
    private void handleLoginButtonClicked(ActionEvent event) {
        handleLoginButton.setDisable(true); // Disable the login button

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Login Failed", "Please enter a username and password.");
            handleLoginButton.setDisable(false); // Enable the login button
            return;
        }

        boolean loginSuccess = loginService.loginUser(username, password);

        if (loginSuccess) {
            LoginData.getInstance();
            LoginData.setUsername(username);
            twoUp();
            OG.playerName.setText(username);



        } else {
            showErrorAlert("Login Failed", "Invalid username or password.");
        }

        handleLoginButton.setDisable(false); // Enable the login button
    }


    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
