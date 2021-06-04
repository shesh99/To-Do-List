package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sun.util.resources.cldr.rof.CalendarData_rof_TZ;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountController {
    public JFXTextField txtUserName;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;
    public JFXPasswordField txtConfirmPassword;
    public Label lblpasswordNotMatch;
    public Label lblUserID;
    public JFXButton btnAddNewUser;
    public JFXButton btnRegister;
    public AnchorPane root;


    public void initialize (){
        lblpasswordNotMatch.setVisible(false);

        txtUserName.setDisable(true);
        txtEmail.setDisable(true);
        txtPassword.setDisable(true);
        txtConfirmPassword.setDisable(true);
        btnRegister.setDisable(true);

    }
    public void btnRegisterOnAction(ActionEvent actionEvent) {
        registerUser();
    }

    public void btnAddNewUserOnAction(ActionEvent actionEvent) {
        autoGenerateID();

        txtUserName.setDisable(false);
        txtEmail.setDisable(false);
        txtPassword.setDisable(false);
        txtConfirmPassword.setDisable(false);
        btnRegister.setDisable(false);

        txtUserName.requestFocus();
    }

    public void autoGenerateID(){

        Connection connection = DBConnection.getInstance().getConnection();

        try {

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");

            boolean isExit = resultSet.next();

            if (isExit){

                String oldid = resultSet.getString(1);

                String id = oldid.substring(1, 4);

                int intId = Integer.parseInt(id);

                intId = intId+1;

                if (intId <10){
                    lblUserID.setText("U00"+intId);
                }else if(intId <100){
                    lblUserID.setText("U0"+intId);
                }else{
                    lblUserID.setText("U"+intId);
                }

            }else{
                lblUserID.setText("U001");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void txtConfirmPasswordOnAction(ActionEvent actionEvent) {
        registerUser();
    }

    public void registerUser(){
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (password.equals(confirmPassword)) {
            txtPassword.setStyle("-fx-border-color: transparent");
            txtConfirmPassword.setStyle("-fx-border-color: transparent");
            lblpasswordNotMatch.setVisible(false);

            String id = lblUserID.getText();
            String userName = txtUserName.getText();
            String email = txtEmail.getText();

            Connection connection = DBConnection.getInstance().getConnection();


            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into user values (?,?,?,?)");

                preparedStatement.setObject(1,id);
                preparedStatement.setObject(2,userName);
                preparedStatement.setObject(3,confirmPassword);
                preparedStatement.setObject(4,email);

                int i = preparedStatement.executeUpdate();

                if (i !=0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Successfully Added...").showAndWait();

                    Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
                    Scene scene =new Scene(parent);
                    Stage primaryStage = (Stage) this.root.getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.centerOnScreen();
                    primaryStage.setTitle("Login Form");


                }else {
                    new Alert(Alert.AlertType.ERROR,"Something went Wrong...").showAndWait();

                }


            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }




        }else {
            txtPassword.setStyle("-fx-border-color: red");
            txtConfirmPassword.setStyle("-fx-border-color: red");
            txtPassword.requestFocus();
            lblpasswordNotMatch.setVisible(true);

        }
    }
}
