package kyle.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import java.util.regex.*;


public class Controller implements Initializable {
    Query model = null;

    String currentStudentID = null;

    @FXML private Label studentID;
    @FXML private Label studentName;
    @FXML private Label studentAddress;
    @FXML private Label studentAvg;
    @FXML private Label classGradeAvg;

    @FXML private ChoiceBox<String> studentChoiceBox;
    @FXML private ChoiceBox<String> classesMissingGradesChoiceBox;
    @FXML private ChoiceBox<String> courseGradeAvgChoiceBox;

    @FXML private TableView<Table_Row> scoreTableView;
    @FXML private TableColumn<Table_Row, String> classColumn;
    @FXML private TableColumn<Table_Row, String> classIDColumn;
    @FXML private TableColumn<Table_Row, String> teacherColumn;
    @FXML private TableColumn<Table_Row, String> singleGradeColumn;
    @FXML private TableColumn<Table_Row, String> averageGradeColumn;

    @FXML private TextField gradeInput;

    @FXML
    private Alert alertUser = new Alert( Alert.AlertType.NONE );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get our model singleton
        model = Query.getInstance();

        // Dont need to do this more than once since it is never updated
        // Get list of students and populate selector with it
        this.studentChoiceBox.getItems().addAll( model.studentList( ) );
        // Get list of courses and populate selector with it
        this.courseGradeAvgChoiceBox.getItems().addAll( model.classesList( ) );

        // Link changing student/course in the choice box to selected function
        this.studentChoiceBox.setOnAction( this::studentSelected );
        this.courseGradeAvgChoiceBox.setOnAction( this::courseSelected );

        // Set table columns
        classColumn.setCellValueFactory(new PropertyValueFactory<Table_Row, String>("className"));
        classIDColumn.setCellValueFactory(new PropertyValueFactory<Table_Row, String>("classID"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<Table_Row, String>("teacher"));
        singleGradeColumn.setCellValueFactory(new PropertyValueFactory<Table_Row, String>("grade"));
        averageGradeColumn.setCellValueFactory(new PropertyValueFactory<Table_Row, String>("avgGrade"));
    }

    // Actually updates what is on the screen
    private void updateStudentView() {
        // Get the student ID from the dropdown
        Pattern studentIDregex = Pattern.compile("ID: ([0-9]+)$");
        Matcher dropDownSelect = studentIDregex.matcher(studentChoiceBox.getValue());
        dropDownSelect.find();   // Redundant but regex.groups() doesnt work without it
        String studentID = dropDownSelect.group(1);
        this.currentStudentID = studentID;

        // Get info from student
        // Basic info
        String[] data = model.getStudentInfo( studentID );
        // Get grade info from student
        Table_Row[] rows = model.getStudentClassesInfo( studentID );
        ObservableList<Table_Row> tableData = FXCollections.observableArrayList();
        Collections.addAll(tableData, rows);
        // Get average grade info for student
        String average = model.getStudentAverage( studentID );
        // Get the classes that student is missing grades in
        String[] missing = model.getStudentClassesMissingGrades( studentID );

        // Put info onto view
        this.studentID.setText("Student ID:\t" + data[3]);
        this.studentAddress.setText( "Address:\t\t" + data[1] + ", " + data[0] );
        this.studentName.setText( "Name:\t\t" + data[2] );
        this.studentAvg.setText( "Student Average Grade:\t" + average);

        // Put grade info onto view
        this.scoreTableView.setItems( tableData );

        // Put missing grades into selector
        this.classesMissingGradesChoiceBox.getItems().clear();
        this.classesMissingGradesChoiceBox.getItems().addAll( missing );
    }

    // Run every time the student in the choiceBox is changed
    public void studentSelected( ActionEvent e ) {
        updateStudentView();
    }

    // Run every time the class in choicebox is changed
    public void courseSelected( ActionEvent e ) {
        Pattern classIDregex = Pattern.compile("ID: ([0-9]+)$");
        Matcher classSelect = classIDregex.matcher(this.courseGradeAvgChoiceBox.getValue());
        classSelect.find();   // Redundant but regex.groups() doesnt work without it
        String classID = classSelect.group(1);

        this.classGradeAvg.setText( "Course Average Grade: " + model.getAverageGrade( classID ) );
    }

    public void addGradeToCourse( ActionEvent e ) {
        if ( studentChoiceBox.getValue() != null && classesMissingGradesChoiceBox.getValue() != null ) {
            if ( gradeInput.getText() != null ) {
                // Get the class ID from the dropdown
                Pattern classIDregex = Pattern.compile("ID: ([0-9]+)$");
                Matcher classSelect = classIDregex.matcher(classesMissingGradesChoiceBox.getValue());
                classSelect.find();   // Redundant but regex.groups() doesnt work without it
                String classID = classSelect.group(1);

                // Make sure our input is good (This prevents sql injection)
                Pattern inputRegex = Pattern.compile("^[0-9]+$");
                Matcher inputMatcher = inputRegex.matcher(gradeInput.getText());
                inputMatcher.find();

                // Get the student ID from the dropdown
                Pattern studentIDregex = Pattern.compile("ID: ([0-9]+)$");
                Matcher dropDownSelect = studentIDregex.matcher(studentChoiceBox.getValue());
                dropDownSelect.find();   // Redundant but regex.groups() doesnt work without it
                String studentID = dropDownSelect.group(1);

                if (inputMatcher.matches()) {
                    model.updateStudentGrade(gradeInput.getText(), studentID, classID);

                    // Updates because there is new info
                    updateStudentView();
                } else {
                    this.alertUser.setAlertType(Alert.AlertType.ERROR);
                    this.alertUser.setContentText("Enter A Number, cannot contain letters, or whitespace");
                    this.alertUser.show();
                }
            } else {
                this.alertUser.setAlertType(Alert.AlertType.ERROR);
                this.alertUser.setContentText("Enter a number");
                this.alertUser.show();
            }
        } else {
            this.alertUser.setAlertType(Alert.AlertType.ERROR);
            this.alertUser.setContentText("Select a student and class");
            this.alertUser.show();
        }
    }
}
