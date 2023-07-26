package dev.gabriel.wguMobileApp.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="Assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentID;
    private boolean objective;
    private String assessmentName;
    private String assessmentStartDate;
    private String assessmentEndDate;
    private int courseID;

    public Assessment(int assessmentID, boolean objective, String assessmentName,
                      String assessmentStartDate, String assessmentEndDate, int courseID) {
        this.assessmentID = assessmentID;
        this.objective = objective;
        this.assessmentName = assessmentName;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentEndDate = assessmentEndDate;
        this.courseID = courseID;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public boolean isObjective() {
        return objective;
    }

    public void setObjective(boolean objective) {
        this.objective = objective;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getAssessmentStartDate() {
        return assessmentStartDate;
    }

    public void setAssessmentStartDate(String assessmentStartDate) {
        this.assessmentStartDate = assessmentStartDate;
    }

    public String getAssessmentEndDate() {
        return assessmentEndDate;
    }

    public void setAssessmentEndDate(String assessmentEndDate) {
        this.assessmentEndDate = assessmentEndDate;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourse(int courseID) {
        this.courseID = courseID;
    }
}
