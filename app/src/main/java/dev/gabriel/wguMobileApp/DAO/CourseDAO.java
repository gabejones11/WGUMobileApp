package dev.gabriel.wguMobileApp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.Entities.Course;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM Courses ORDER BY CourseID ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM Courses WHERE termID = :termID ORDER BY CourseID ASC")
    List<Course> getCoursesByTermID(int termID);
}
