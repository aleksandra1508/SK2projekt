package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main extends Application {

    private Stage primaryStage;
    private Socket socket;
    LoginController loginController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Szachy");
        this.primaryStage = primaryStage;
        initializeLoginView();
    }

    public void initializeLoginView(){
        try {
            primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../views/LoginView.fxml"));
            BorderPane sign = loader.load();
            loginController = loader.getController();
            loginController.setMain(this);
            Scene scene = new Scene(sign);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeGameView(String yourLogin, String enemyLogin, String color) {
        try {
            primaryStage.close();
            primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../views/GameView.fxml"));
            BorderPane rootLayout = loader.load();
            Controller gameController = loader.getController();
            gameController.setConnectionThread(socket);
            gameController.setLogins(yourLogin, enemyLogin);
            gameController.startGame(color, this);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e->{
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToLoginView(String title, String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(content);
                alert.showAndWait();

                primaryStage.close();
                initializeLoginView();
            }
        });


    }

    public int connect(String login, String address, String port) {
        try {
            socket = new Socket(address, Integer.parseInt(port));

            System.out.format("[Client] Połaczono z serwerem!\n");
            final Thread newGameThread = new Thread() {
                @Override
                public void run() {
                    System.out.println("Start...");
                    InputStream is;
                    OutputStream os ;
                    String received;
                    String response;
                    int numberOfReadedBytes;
                    byte[] buffer = new byte[5];
                    byte[] buffer_login = new byte[8];
                    try {
                        is = socket.getInputStream();
                        os = socket.getOutputStream();
                        numberOfReadedBytes = is.read(buffer);
                        received = new String(buffer,0,5);
                        System.out.println(received);
                        if (received.equals("LOGIN")) {
                            response = login+"\n";
                            os.write(response.getBytes());
                            System.out.println(response);
                        } else {
                            System.out.println("Błąd: niezrozumiałe polecenie, przerwano logowanie");
                            loginController.threadExists = false;
                            this.interrupt();
                        }

                        numberOfReadedBytes = is.read(buffer_login);
                        received = new String(buffer_login,0,8);
                        System.out.println(received);
                        final String enemyLogin = received;

                        numberOfReadedBytes = is.read(buffer);
                        received = new String(buffer,0,5);
                        final String color = received;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                initializeGameView(login, enemyLogin, color);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
            };
            newGameThread.start();

        } catch (IOException e) {
            System.out.println("Zepsulo sie");
            return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
