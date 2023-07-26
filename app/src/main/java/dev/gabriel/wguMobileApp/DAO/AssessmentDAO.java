package dev.gabriel.wguMobileApp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.gabriel.wguMobileApp.Entities.Assessment;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM Assessments ORDER BY AssessmentID ASC")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM Assessments WHERE courseID = :courseID ORDER BY AssessmentID ASC")
        List<Assessment> getAssessmentsByCourseID(int courseID);

}
