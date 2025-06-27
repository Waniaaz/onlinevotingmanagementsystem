package vms.pkg1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormCastVoteController {

    @FXML
    private TextField txtVoterCNIC;

    @FXML
    private ComboBox<String> comboCandidate;

    @FXML
    private Button btnCastVote;

    @FXML
    private void initialize() {

        populateCandidateCombo();

        // Optional: Pre-select role
        comboCandidate.getSelectionModel().selectFirst();

        // Attach button action
        btnCastVote.setOnAction(event -> castVote());
    }

    Map<String, String> userNameToIdMap = new HashMap<>();

    private void castVote(){
        String voterCNIC = txtVoterCNIC.getText().trim();
        String candidName = comboCandidate.getSelectionModel().getSelectedItem();
        String candidId = userNameToIdMap.get(candidName);

        if(checkVoterCNIC(voterCNIC)){

            String insertQry="INSERT INTO candid_vote_t (CANDIDATE_ID,VOTER_CNIC,VOTE_COUNT) VALUES (?,?,?)";

            int dbResult= DbHelper.executeUpdate(insertQry,candidId,voterCNIC,1);

            CommonUtility.showAlert(Alert.AlertType.INFORMATION, "Alert", "Vote has been casted.");



        }
        else {

            CommonUtility.showAlert(Alert.AlertType.WARNING, "System Alert", "Vote already casted against given CNIC");
        }
    }

    private void populateCandidateCombo(){

        List<String[]> candidUsers = DbHelper.getData("SELECT USER_ID,USER_NAME FROM sys_user_t WHERE ROLE_ID=2");


        ObservableList<String> observableCandidList = FXCollections.observableArrayList();

        for (String[] row : candidUsers) {
            if (row.length >= 2) {
                String userId = row[0];
                String userName = row[1];

                observableCandidList.add(userName);           // display name in ComboBox
                userNameToIdMap.put(userName, userId);        // link name to ID
            }
        }

        comboCandidate.setItems(observableCandidList);

    }

    private boolean checkVoterCNIC(String CnicNumber){

        List<String[]> existCnic = DbHelper.getData("SELECT * FROM candid_vote_t WHERE VOTER_CNIC=?",CnicNumber);

        if(existCnic.size()>0){
            return  false;
        }
        else {
            return  true;
        }

    }

    public void gotoData(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormVoterData.fxml","Real Time Data");

    }

    public void gotoLogin(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormLogin.fxml","Login");

    }
}
