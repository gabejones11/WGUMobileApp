package dev.gabriel.wguMobileApp.UI;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.R;
import static dev.gabriel.wguMobileApp.Utility.Utility.*;

public class AddAssessmentScreen extends AppCompatActivity {

    private Switch examTypeSwitch;
    private EditText examTitle;
    private EditText assessmentStartDate;
    private EditText assessmentEndDate;
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;
    private final Calendar calendarStart = Calendar.getInstance();
    private final Calendar calendarEnd = Calendar.getInstance();
    private TextView screenTitle;
    private Spinner associatedCourseSpinner;
    private List<Course> courseList;
    private int selectedCourseID;
    private int assessmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assessment_screen);
        setTitle("Add Assessment");

        //initialize the courseList
        Repository repository = new Repository(getApplication());
        courseList = repository.getAllCourses();

        //UIComponents
        examTypeSwitch = findViewById(R.id.examTypeSwitch);
        examTitle = findViewById(R.id.examTitle);
        assessmentStartDate = findViewById(R.id.startDate);
        assessmentEndDate = findViewById(R.id.endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        assessmentStartDate.setText(sdf.format(new Date()));
        assessmentEndDate.setText(sdf.format(new Date()));
        screenTitle = findViewById(R.id.screenTitle);
        associatedCourseSpinner = findViewById(R.id.associatedCourseSpinner);

        //custom adapter for the spinner
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_item, courseList) {
            @Override
            //displays the course title for the drop down items
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(courseList.get(position).getCourseTitle());
                return view;
            }

            @Override
            //displays the course title for the selected item
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(courseList.get(position).getCourseTitle());
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        associatedCourseSpinner.setAdapter(adapter);

        //listener for selected item
        associatedCourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseID = courseList.get(position).getCourseID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //don't think I need anything here we will validate that an item is selected before saving
            }
        });

        //retrieve extras from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Modify Assessment");
            screenTitle.setText("Modify Assessment");
            assessmentID = intent.getIntExtra("id", 0);
            boolean examType = intent.getBooleanExtra("assessmentType", false);
            String title = intent.getStringExtra("assessmentName");
            String start = intent.getStringExtra("assessmentStartDate");
            String end = intent.getStringExtra("assessmentEndDate");
            int courseID = intent.getIntExtra("courseID", 0);

            examTypeSwitch.setChecked(!examType);
            examTitle.setText(title);
            assessmentStartDate.setText(start);
            assessmentEndDate.setText(end);

            //find the position of the course in the courseList
            int coursePosition = -1;
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).getCourseID() == courseID) {
                    coursePosition = i;
                    break;
                }
            }
            associatedCourseSpinner.setSelection(coursePosition);
        }

        //date picker for start date
        assessmentStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = assessmentStartDate.getText().toString();

                try {
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddAssessmentScreen.this, startDate,
                        calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
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
                assessmentStartDate.setText(sdf.format(calendarStart.getTime()));
            }
        };

        //date picker for end date
        assessmentEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = assessmentEndDate.getText().toString();

                try {
                    calendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddAssessmentScreen.this, endDate, calendarEnd.get(Calendar.YEAR),
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
                assessmentEndDate.setText(sdf.format(calendarEnd.getTime()));
            }
        };

        //cancel button set up
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAssessmentScreen.this, AssessmentsScreen.class);
                startActivity(intent);
            }
        });

        //save button set up
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gather values
                boolean examTypeValue = !examTypeSwitch.isChecked();
                String examTitleValue = String.valueOf(examTitle.getText());
                String startDateValue = String.valueOf(assessmentStartDate.getText());
                String endDateValue = String.valueOf(assessmentEndDate.getText());

                //validate values
                Context context = AddAssessmentScreen.this;
                if (!validateTitle(examTitleValue, context)) {
                    return;
                }

                if (!validateStartEndDate(startDateValue, context)) {
                    return;
                }

                if (!validateStartEndDate(endDateValue, context)) {
                    return;
                }

                //create object
                Assessment assessment = new Assessment(assessmentID, examTypeValue,
                        examTitleValue, startDateValue, endDateValue, selectedCourseID);

                //save to database
                Repository repository = new Repository(getApplication());
                if (intent.hasExtra("id")) {
                    repository.update(assessment);
                    Toast.makeText(getApplicationContext(), "Assessment has been updated", Toast.LENGTH_LONG).show();
                } else {
                    repository.insert(assessment);
                    Toast.makeText(getApplicationContext(), "Assessment has been added", Toast.LENGTH_LONG).show();
                }

                //go back to the assessments screen
                Intent intent = new Intent(AddAssessmentScreen.this, AssessmentsScreen.class);
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
            String dateFromScreen = assessmentStartDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AddAssessmentScreen.this, MyReceiver.class);
            intent.putExtra("newKey", "Assessment Starting Today: " + dateFromScreen);
            PendingIntent sender = PendingIntent.getBroadcast(AddAssessmentScreen.this, ++HomeScreen.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;

        } else if (id == R.id.endNotification) {
            String dateFromScreen = assessmentEndDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(AddAssessmentScreen.this, MyReceiver.class);
            intent.putExtra("newKey", "Assessment Ending Today: " + dateFromScreen);
            PendingIntent sender = PendingIntent.getBroadcast(AddAssessmentScreen.this, ++HomeScreen.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}