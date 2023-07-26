package dev.gabriel.wguMobileApp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dev.gabriel.wguMobileApp.DAO.AssessmentDAO;
import dev.gabriel.wguMobileApp.DAO.CourseDAO;
import dev.gabriel.wguMobileApp.DAO.TermsDAO;
import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.Entities.Term;

@Database(entities = {Assessment.class, Course.class, Term.class}, version = 7, exportSchema = false)
public abstract class DataBaseBuilder extends RoomDatabase {
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract TermsDAO termsDAO();

    private static volatile DataBaseBuilder INSTANCE;

    static DataBaseBuilder getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (DataBaseBuilder.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DataBaseBuilder.class, "WGUMobileAppDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
