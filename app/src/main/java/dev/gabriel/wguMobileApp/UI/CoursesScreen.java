package dev.gabriel.wguMobileApp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.R;

public class CoursesScreen extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Courses");

        //recycler view set up
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final CoursesAdapter coursesAdapter = new CoursesAdapter(this);
        recyclerView.setAdapter(coursesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //database and list items set up
        repository = new Repository(getApplication());
        List<Course> allCourses = repository.getAllCourses();
        coursesAdapter.setCourses(allCourses);

        //swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCourses(coursesAdapter, repository));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plus_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //send the user home or to the add course screen based on the option chose in the actionbar
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(CoursesScreen.this, AddCourseScreen.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(CoursesScreen.this, HomeScreen.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}