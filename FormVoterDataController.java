package vms.pkg1;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class FormVoterDataController {

    @FXML
    private TableView<ObservableList<String>> voteTable;

    @FXML
    private TableColumn<ObservableList<String>, String> voteCountCol;

    @FXML
    private TableColumn<ObservableList<String>, String> userNameCol;

    @FXML
    public void initialize() {
        voteCountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        userNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));

        loadVoteDataFromDb();
    }

    private void loadVoteDataFromDb() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        try {
            String query = "SELECT COUNT(RECORD_ID) VOTE_COUNT,USER_NAME FROM candid_vote_t LEFT JOIN sys_user_t ON CANDIDATE_ID=USER_ID GROUP BY USER_NAME";
            List<String[]> results = DbHelper.getData(query);

            for (String[] row : results) {
                ObservableList<String> rowData = FXCollections.observableArrayList();
                rowData.add(row[0]);
                rowData.add(row[1]);
                data.add(rowData);
            }

            voteTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoLogin(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormLogin.fxml","Login");

    }

    public void gotoCastVote(ActionEvent event){

        CommonUtility.navigateForm(event,"/vms/pkg1/FormCastVote.fxml","Cast Vote");

    }
}


