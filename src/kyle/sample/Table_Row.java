package kyle.sample;

import javafx.beans.property.SimpleStringProperty;

public class Table_Row {
    private SimpleStringProperty className;
    private SimpleStringProperty teacher;
    private SimpleStringProperty grade;
    private SimpleStringProperty avgGrade;
    private SimpleStringProperty classID;

    public
    Table_Row( String className, String classID, String teacher, String grade, String avgGrade ) {
        this.className = new SimpleStringProperty( className );
        this.teacher = new SimpleStringProperty( teacher );
        this.grade = new SimpleStringProperty( grade );
        this.avgGrade = new SimpleStringProperty( avgGrade );
        this.classID = new SimpleStringProperty( classID );
    }

    public String getClassName() {
        return this.className.get();
    }
    public String getTeacher() {
        return this.teacher.get();
    }
    public String getGrade() {
        return this.grade.get();
    }
    public String getAvgGrade() { return this.avgGrade.get(); }
    public String getClassID() { return this.classID.get(); }
}
