package dev.gabriel.wguMobileApp.UI;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.Entities.Course;

public class SwipeToDeleteCourses extends ItemTouchHelper.SimpleCallback {
    private Repository mRepository;
    private CoursesAdapter mCoursesAdapter;

    //constructs swipe to delete object specifies that the action will take place on left swipe
    public SwipeToDeleteCourses(CoursesAdapter coursesAdapter, Repository repository) {
        super(0, ItemTouchHelper.LEFT);
        mCoursesAdapter = coursesAdapter;
        mRepository = repository;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //find the position of the item and delete it from the recycler view and from the repository
        int position = viewHolder.getAdapterPosition();
        Course deletedCourse = mCoursesAdapter.getCourse(position);
        mCoursesAdapter.deleteCourse(position);
        mRepository.delete(deletedCourse);
        Toast.makeText(viewHolder.itemView.getContext(), "Course Deleted", Toast.LENGTH_LONG).show();
    }
}
