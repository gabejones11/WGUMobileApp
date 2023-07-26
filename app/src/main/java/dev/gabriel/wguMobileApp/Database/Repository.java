package dev.gabriel.wguMobileApp.Database;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.gabriel.wguMobileApp.DAO.AssessmentDAO;
import dev.gabriel.wguMobileApp.DAO.CourseDAO;
import dev.gabriel.wguMobileApp.DAO.TermsDAO;
import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.Entities.Term;

public class Repository {
    private AssessmentDAO mAssessmentDAO;
    private CourseDAO mCourseDAO;
    private TermsDAO mTermsDAO;
    private List<Assessment> mAllAssessments;
    private List<Course> mAllCourses;
    private List<Term> mAllTerms;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        DataBaseBuilder db = DataBaseBuilder.getDatabase(application);
        mAssessmentDAO = db.assessmentDAO();
        mCourseDAO = db.courseDAO();
        mTermsDAO = db.termsDAO();
    }

    //Assessments
    public List<Assessment> getAllAssessments() {
        databaseExecutor.execute(()-> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }

    public void insert(Assessment assessment) {
        databaseExecutor.execute(()-> {
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Assessment assessment) {
        databaseExecutor.execute(()-> {
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Assessment assessment) {
        databaseExecutor.execute(()-> {
            mAssessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assessment> getAssessmentsByCourseID(int courseID) {
        List<Assessment> assessments = new ArrayList<>();

        databaseExecutor.execute(() -> {
            assessments.addAll(mAssessmentDAO.getAssessmentsByCourseID(courseID));
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return assessments;
    }

    //Terms
    public List<Term> getAllTerms() {
        databaseExecutor.execute(()-> {
            mAllTerms = mTermsDAO.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTerms;
    }

    public void insert(Term term) {
        databaseExecutor.execute(()-> {
            mTermsDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Term term) {
        databaseExecutor.execute(()-> {
            mTermsDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term) {
        databaseExecutor.execute(()-> {
            mTermsDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Courses
    public List<Course> getAllCourses() {
        databaseExecutor.execute(()-> {
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

    public void insert(Course course) {
        databaseExecutor.execute(()-> {
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Course course) {
        databaseExecutor.execute(()-> {
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course) {
        databaseExecutor.execute(()-> {
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getCoursesByTermID(int termID) {
        List<Course> courses = new ArrayList<>();

        databaseExecutor.execute(() -> {
            courses.addAll(mCourseDAO.getCoursesByTermID(termID));
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }
}
