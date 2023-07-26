package dev.gabriel.wguMobileApp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.Entities.Term;
import dev.gabriel.wguMobileApp.R;
import static dev.gabriel.wguMobileApp.Utility.Utility.*;

public class AddTermScreen extends AppCompatActivity {

    private EditText termTitle;
    private EditText termStartDate;
    private EditText termEndDate;
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;
    private final Calendar calendarStart = Calendar.getInstance();
    private final Calendar calendarEnd = Calendar.getInstance();
    private TextView screenTitleTextView;
    private int termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_term_screen);
        setTitle("Add Term");

        //recycler view set up
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final CoursesAdapter coursesAdapter = new CoursesAdapter(this);
        recyclerView.setAdapter(coursesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize the repository
        Repository repository = new Repository(getApplication());

        //ui components
        termTitle = findViewById(R.id.title);
        termStartDate = findViewById(R.id.startDate);
        termEndDate = findViewById(R.id.endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        termStartDate.setText(sdf.format(new Date()));
        termEndDate.setText(sdf.format(new Date()));
        screenTitleTextView = findViewById(R.id.screenTitle);

        //retrieve extras from intent
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Modify Term");
            screenTitleTextView.setText("Modify Term");
            termID = intent.getIntExtra("id", 0);
            String title = intent.getStringExtra("termName");
            String startDate = intent.getStringExtra("termStart");
            String endDate = intent.getStringExtra("termEnd");

            termTitle.setText(title);
            termStartDate.setText(startDate);
            termEndDate.setText(endDate);

            //course list items set up
            List<Course> associatedCourses = repository.getCoursesByTermID(termID);
            coursesAdapter.setCourses(associatedCourses);
        }

        //date picker for start date
        termStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = termStartDate.getText().toString();

                try {
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddTermScreen.this, startDate, calendarStart.get(Calendar.YEAR),
                        calendarStart.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //update the label
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                termStartDate.setText(sdf.format(calendarStart.getTime()));
            }
        };

        //date picker for end date
        termEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = termEndDate.getText().toString();

                try {
                    calendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddTermScreen.this, endDate, calendarEnd.get(Calendar.YEAR),
                        calendarEnd.get(Calendar.MONTH), calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //update the label
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                termEndDate.setText(sdf.format(calendarEnd.getTime()));
            }
        };

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTermScreen.this, TermsScreen.class);
                startActivity(intent);
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gather values
                String termTitleValue = String.valueOf(termTitle.getText());
                String termStartValue = String.valueOf(termStartDate.getText());
                String termEndValue = String.valueOf(termEndDate.getText());

                //validate values
                Context context = AddTermScreen.this;
                if (!validateTitle(termTitleValue, context)) {
                    return;
                }

                if (!validateStartEndDate(termStartValue, context)) {
                    return;
                }

                if (!validateStartEndDate(termEndValue, context)) {
                    return;
                }

                //create object
                Term term = new Term(termID, termTitleValue, termStartValue, termEndValue);

                //save to database
                Repository repository = new Repository(getApplication());
                if (intent.hasExtra("id")) {
                    repository.update(term);
                    Toast.makeText(getApplicationContext(), "The term has been updated", Toast.LENGTH_LONG).show();
                } else {
                    repository.insert(term);
                    Toast.makeText(getApplicationContext(), "The term has been added", Toast.LENGTH_LONG).show();
                }

                //go back to terms screen
                Intent intent = new Intent(AddTermScreen.this, TermsScreen.class);
                startActivity(intent);
            }
        });
    }

    //set up options button for notifications
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessments_terms, menu);
        return true;
    }

    //handle selected item
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.startNotification) {
            String dateFromScreen = termStartDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AddTermScreen.this, MyReceiver.class);
            intent.putExtra("newKey", "Term Starting Today: " + dateFromScreen);
            PendingIntent sender = PendingIntent.getBroadcast(AddTermScreen.this, ++HomeScreen.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;

        } else if (id == R.id.endNotification) {
            String dateFromScreen = termEndDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AddTermScreen.this, MyReceiver.class);
            intent.putExtra("newKey", "Term Ending Today: " + dateFromScreen);
            PendingIntent sender = PendingIntent.getBroadcast(AddTermScreen.this, ++HomeScreen.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}