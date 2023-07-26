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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.Entities.Term;
import dev.gabriel.wguMobileApp.R;
import static dev.gabriel.wguMobileApp.Utility.Utility.*;

public class AddCourseScreen extends AppCompatActivity {
    private EditText courseTitle;
    private EditText courseStartDate;
    private EditText courseEndDate;
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;
    private final Calendar calendarStart = Calendar.getInstance();
    private final Calendar calendarEnd = Calendar.getInstance();
    private RadioGroup courseProgressGroup;
    private RadioButton complete;
    private RadioButton inProgress;
    private RadioButton dropped;
    private RadioButton planToTake;
    private EditText instructorName;
    private EditText instructorPhone;
    private EditText instructorEmail;
    private TextInputEditText courseNotes;
    private Spinner associatedTermsSpinner;
    private List<Term> termList;
    private int selectedTermID;
    private TextView screenTitle;
    private int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_screen);
        setTitle("Add Course");

        //recycler view set up
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final AssessmentsAdapter assessmentsAdapter = new AssessmentsAdapter(this);
        recyclerView.setAdapter(assessmentsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize the terms list
        Repository repository = new Repository(getApplication());
        termList = repository.getAllTerms();

        //UIComponents
        courseTitle = findViewById(R.id.courseTitle);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        courseStartDate.setText(sdf.format(new Date()));
        courseEndDate.setText(sdf.format(new Date()));
        courseProgressGroup = findViewById(R.id.progressRadioGroup);
        complete = findViewById(R.id.completeRadioButton);
        inProgress = findViewById(R.id.inProgressRadioButton);
        dropped = findViewById(R.id.droppedRadioButton);
        planToTake = findViewById(R.id.planToTakeRadioButton);
        instructorName = findViewById(R.id.instructorName);
        instructorPhone = findViewById(R.id.instructorPhone);
        instructorEmail = findViewById(R.id.instructorEmail);
        courseNotes = findViewById(R.id.notes);
        screenTitle = findViewById(R.id.screenTitle);
        associatedTermsSpinner = findViewById(R.id.associatedTermsSpinner);

        //custom adapter for the spinner
        ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(this, android.R.layout.simple_spinner_item, termList) {
            @Override
            //displays the term title for the drop down items
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(termList.get(position).getTermTitle());
                return view;
            }

            @Override
            //displays the course title for the selected item
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(termList.get(position).getTermTitle());
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        associatedTermsSpinner.setAdapter(adapter);

        //listener for selected item
        associatedTermsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTermID = termList.get(position).getTermID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //don't think I need anything here we will validate that an item is selected before saving
            }
        });

        //retrieve extras from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Modify Course");
            screenTitle.setText("Modify Course");
            courseID = intent.getIntExtra("id", 0);
            String title = intent.getStringExtra("courseTitle");
            String start = intent.getStringExtra("courseStartDate");
            String end = intent.getStringExtra("courseEndDate");
            int progress = intent.getIntExtra("progressRadioGroup", 0);
            String name = intent.getStringExtra("instructorName");
            String phone = intent.getStringExtra("instructorPhone");
            String email = intent.getStringExtra("instructorEmail");
            String notes = intent.getStringExtra("notes");
            int termID = intent.getIntExtra("termID", 0);

            courseTitle.setText(title);
            courseStartDate.setText(start);
            courseEndDate.setText(end);
            if (progress == 1) {
                complete.setChecked(true);
            } else if (progress == 2) {
                inProgress.setChecked(true);
            } else if (progress == 3) {
                dropped.setChecked(true);
            } else if (progress == 4) {
                planToTake.setChecked(true);
            }
            instructorName.setText(name);
            instructorPhone.setText(phone);
            instructorEmail.setText(email);
            courseNotes.setText(notes);

            //find the position of the term in the termList
            int termPosition = -1;
            for (int i = 0; i < termList.size(); i++) {
                if (termList.get(i).getTermID() == termID) {
                    termPosition = i;
                    break;
                }
            }
            associatedTermsSpinner.setSelection(termPosition);

            //assessment list items set up
            List<Assessment> associatedAssessments = repository.getAssessmentsByCourseID(courseID);
            assessmentsAdapter.setAssessments(associatedAssessments);
        }

        //date picker for start date
        courseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = courseStartDate.getText().toString();

                try {
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddCourseScreen.this, startDate, calendarStart.get(Calendar.YEAR),
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
                courseStartDate.setText(sdf.format(calendarStart.getTime()));
            }
        };


        //date picker for end date
        courseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = courseEndDate.getText().toString();

                try {
                    calendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddCourseScreen.this, endDate, calendarEnd.get(Calendar.YEAR),
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
                courseEndDate.setText(sdf.format(calendarEnd.getTime()));
            }
        };

        //cancel button set up
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourseScreen.this, CoursesScreen.class);
                startActivity(intent);
            }
        });

        //save button set up
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gather values
                String courseTitleValue = String.valueOf(courseTitle.getText());
                String startDateValue = String.valueOf(courseStartDate.getText());
                String endDateValue = String.valueOf(courseEndDate.getText());
                int courseProgressValue = 0;
                if (courseProgressGroup.getCheckedRadioButtonId() == R.id.completeRadioButton) {
                    courseProgressValue = 1;
                } else if (courseProgressGroup.getCheckedRadioButtonId() == R.id.inProgressRadioButton) {
                    courseProgressValue = 2;
                } else if (courseProgressGroup.getCheckedRadioButtonId() == R.id.droppedRadioButton) {
                    courseProgressValue = 3;
                } else if (courseProgressGroup.getCheckedRadioButtonId() == R.id.planToTakeRadioButton) {
                    courseProgressValue = 4;
                }
                String instructorNameValue = String.valueOf(instructorName.getText());
                String instructorPhoneValue = String.valueOf(instructorPhone.getText());
                String instructorEmailValue = String.valueOf(instructorEmail.getText());
                String notesValue = String.valueOf(courseNotes.getText());

                //validate values
                Context context = AddCourseScreen.this;
                if (!validateTitle(courseTitleValue, context)) {
                    return;
                }

                if (!validateStartEndDate(startDateValue, context)) {
                    return;
                }

                if (!validateStartEndDate(endDateValue, context)) {
                    return;
                }

                if (!validateCourseProgress(courseProgressValue, context)) {
                    return;
                }

                if (!validateInstructorName(instructorNameValue, context)) {
                    return;
                }

                if (!validatePhoneNumber(instructorPhoneValue, context)) {
                    return;
                }

                if (!validateEmailAddress(instructorEmailValue, context)) {
                    return;
                }

                //create object
                Course course = new Course(courseID, courseTitleValue, startDateValue, endDateValue, courseProgressValue, instructorNameValue,
                        instructorPhoneValue, instructorEmailValue, notesValue, selectedTermID);

                //save to database
                Repository repository = new Repository(getApplication());
                if (intent.hasExtra("id")) {
                    repository.update(course);
                    Toast.makeText(getApplicationContext(), "Course has been updated", Toast.LENGTH_LONG).show();
                } else {
                    repository.insert(course);
                    Toast.makeText(getApplicationContext(), "Course has been added", Toast.LENGTH_SHORT).show();
                }

                //go back to the courses screen
                Intent intent = new Intent(AddCourseScreen.this, CoursesScreen.class);
                startActivity(intent);
            }
        });
    }

    //set up options button for notifications and sharing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_courses, menu);
        return true;
    }

    //handle selected item
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, courseNotes.getText().toString());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "Share");
            startActivity(shareIntent);
            return true;
        }

        else if (id == R.id.startNotification) {
            String dateFromScreen = courseStartDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AddCourseScreen.this, MyReceiver.class);
            intent.putExtra("newKey", "Course Starting Today: " + dateFromScreen);
            PendingIntent sender = PendingIntent.getBroadcast(AddCourseScreen.this, ++HomeScreen.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }

        else if (id == R.id.endNotification) {
            String dateFromScreen = courseEndDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AddCourseScreen.this, MyReceiver.class);
            intent.putExtra("newKey", "Course Ending Today: " + dateFromScreen);
            PendingIntent sender = PendingIntent.getBroadcast(AddCourseScreen.this, ++HomeScreen.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}