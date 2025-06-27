package vms.pkg1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.util.List;

public class FormLoginController {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtUserPassword;

    @FXML
    private Button btnLogin;


    @FXML
    private void initialize() {


        // Attach button action
        btnLogin.setOnAction(event -> handleLogin(event));
    }

    private void handleLogin(ActionEvent event) {
        String username = txtUserName.getText().trim();
        String password = txtUserPassword.getText().trim();

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            CommonUtility.showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        try {

            String selectQry = "SELECT USER_ID,USER_NAME,ROLE_ID FROM sys_user_t WHERE USER_NAME=? AND USER_PASSWORD=?";

            List<String[]> loginUser = DbHelper.getData(selectQry, username, password);

            if (loginUser.size() > 0) {

                String roleId = loginUser.get(0)[2];

                if (roleId == "2") {

                } else {
                    CommonUtility.navigateForm(event, "/vms/pkg1/FormUserCreate.fxml","Create User");
                }

            }
            else{

                CommonUtility.showAlert(Alert.AlertType.WARNING, "System Alert", "Wrong user name or password");
                return;
            }
        } catch (Exception e) {
            CommonUtility.showAlert(Alert.AlertType.ERROR, "System Alert", "Some error occured");
            return;
        }


    }

    public void gotoData(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormVoterData.fxml","Real Time Data");

    }
}

