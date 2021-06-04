package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;
    public AnchorPane LoginForm;
    public static String name;
    public static String userID;

    public void lblNewAccountOnMouseClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/CreateNewAccount.fxml"));
        Scene scene =new Scene(root);
        Stage primaryStage = (Stage) this.LoginForm.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Account");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        loginEnter();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        loginEnter();
    }

    public void loginEnter(){
            String password = txtPassword.getText();
            String username = txtUserName.getText();

            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user where name = ? and password = ?");
                preparedStatement.setObject(1,username);
                preparedStatement.setObject(2,password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){

                    userID = resultSet.getString(1);
                    name = resultSet.getString(2);


                    Parent parent = FXMLLoader.load(this.getClass().getResource("../view/ToDoForm.fxml"));
                    Scene scene =new Scene(parent);
                    Stage primaryStage = (Stage) this.LoginForm.getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.centerOnScreen();
                    primaryStage.setTitle("Create New Account");

                }else{
                    new Alert(Alert.AlertType.ERROR,"Invalid User Name or Password..").showAndWait();

                    txtUserName.clear();
                    txtPassword.clear();

                    txtUserName.requestFocus();
                }

            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
    }


}
