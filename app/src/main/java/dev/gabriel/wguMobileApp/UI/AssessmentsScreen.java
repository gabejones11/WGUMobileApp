package dev.gabriel.wguMobileApp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.R;

public class AssessmentsScreen extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessments_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Assessments");

        //recycler view set up
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final AssessmentsAdapter assessmentsAdapter = new AssessmentsAdapter(this);
        recyclerView.setAdapter(assessmentsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //database and list items set up
        repository = new Repository(getApplication());
        List<Assessment> allAssessments = repository.getAllAssessments();
        assessmentsAdapter.setAssessments(allAssessments);

        //swipe to delete set up
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteAssessments(assessmentsAdapter, repository));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plus_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //send the user home or to the add course screen based on their selection in the action bar
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(AssessmentsScreen.this, AddAssessmentScreen.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(AssessmentsScreen.this, HomeScreen.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}