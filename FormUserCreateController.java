package vms.pkg1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class FormUserCreateController {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtUserPassword;

    @FXML
    private ComboBox<String> comboUserRole;

    @FXML
    private Label lblPassword;

    @FXML
    private Button btnCreateUser;

    @FXML
    private void initialize() {

        // Feed user roles dynamically
        comboUserRole.getItems().addAll(
                "1 - Admin",
                "2 - Candidate"
        );

        // Optional: Pre-select role
        comboUserRole.getSelectionModel().selectFirst();

        // Attach button action
        btnCreateUser.setOnAction(event -> handleCreateUser());

        // Add listener
        comboUserRole.valueProperty().addListener((obs, oldVal, newVal) -> {

            String role = comboUserRole.getValue();
            int _roleId= Integer.parseInt(role.split(" - ")[0]);

            if (_roleId==2) {
                // Hide password field and label
                lblPassword.setVisible(false);
                txtUserPassword.setVisible(false);
                txtUserPassword.setManaged(false); // Optional: exclude from layout
            } else {
                // Show password field and label
                lblPassword.setVisible(true);
                txtUserPassword.setVisible(true);
                txtUserPassword.setManaged(true);
            }
        });


}

    private void handleCreateUser() {

        String username = txtUserName.getText().trim();
        String password = txtUserPassword.getText().trim();
        String role = comboUserRole.getValue();

        // Extract role ID from selected item
        int roleId;

        roleId = Integer.parseInt(role.split(" - ")[0]);
        // Basic validation
        if (username.isEmpty() || (password.isEmpty() && roleId==1) || role == null ) {
            CommonUtility.showAlert(AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        try {


            String insertQry="INSERT INTO sys_user_t (USER_NAME,USER_PASSWORD,ROLE_ID) VALUES (?,?,?)";

            int dbResult= DbHelper.executeUpdate(insertQry,username,password,roleId);

            System.out.println(dbResult);

        } catch (Exception e) {
            CommonUtility.showAlert(AlertType.ERROR, "Role Error", "Invalid role selected.");
            return;
        }

        CommonUtility.showAlert(AlertType.INFORMATION, "Success", "User created successfully.");
    }


    public void gotoLogin(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormLogin.fxml","Login");

    }

    public void gotoData(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormVoterData.fxml","Real Time Data");

    }
}
