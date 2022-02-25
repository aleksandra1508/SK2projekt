package application;

import helpers.LoginHelper;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    public TextField loginField;
    public TextField addressField;
    public TextField portField;
    public Label waitingLabel;
    public boolean threadExists = false;
    private Main main;
    public static final String textErrorConnection = "Błąd - nie można połączyć z serwerem";
    public static final String textWaitingForPlayer = "Szukanie gracza...";
    public static final String textErrorAddress = "Błędny adres lub port";
    public static final String textErrorLogin = "Login musi mieć długość od 1 do 8 znaków";

    private LoginHelper loginHelper =new LoginHelper();

    public void setMain(Main m) {
        this.main = m;
    }

    public void joinGame() {
        if (!threadExists) {
            String login = loginField.getText();
            String address = addressField.getText();
            String port = portField.getText();
            int result;
            if (!loginHelper.validLogin(login)) {
                result = -3;
            } else {
                if (loginHelper.validIP(address) & loginHelper.validPort(port)) {
                    result = main.connect(login, address, port);
                } else {
                    result = -2;
                }
            }
            manageResult(result);

        }
    }

    private void manageResult(int result) {
        switch (result) {
            case 0: {
                threadExists = true;
                waitingLabel.setText(textWaitingForPlayer);
                waitingLabel.setVisible(true);
                break;
            }
            case -1: {
                waitingLabel.setText(textErrorConnection);
                waitingLabel.setVisible(true);
                break;
            }
            case -2: {
                waitingLabel.setText(textErrorAddress);
                waitingLabel.setVisible(true);
                break;
            }
            case -3: {
                waitingLabel.setText(textErrorLogin);
                waitingLabel.setVisible(true);
                break;
            }
        }
    }
}
