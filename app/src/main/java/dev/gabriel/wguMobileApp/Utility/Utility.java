package dev.gabriel.wguMobileApp.Utility;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utility {

    private Utility() {}

    public static boolean validateTitle(String title, Context context) {
        if (title.trim().isEmpty()) {
            showMessage(context, "Title field is blank");
            return false;
        }
        return true;
    }

    public static boolean validateInstructorName(String title, Context context) {
        if (title.trim().isEmpty()) {
            showMessage(context, "Instructor name is blank");
            return false;
        }
        return true;
    }


    public static boolean validateStartEndDate(String date, Context context) {
        if (date.trim().isEmpty()) {
            showMessage(context, "One or more of the date fields is empty");
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

        try {
            sdf.parse(date);
        } catch (ParseException e) {
            showMessage(context, "Date inputted in an unacceptable format");
            return false;
        }
        return true;
    }

    public static boolean validateCourseProgress(Integer progress, Context context) {
        if (progress == 0) {
            showMessage(context, "No selection was made for course progress");
            return false;
        }
        return true;
    }

    //checks if phone number has 3 digits followed by a hyphen followed by three more digits and another hyphen followed by 4 digits
    public static boolean validatePhoneNumber(String phone, Context context) {
        if (phone.trim().isEmpty()) {
            showMessage(context, "Phone field is empty");
            return false;
        }

        String phoneRegex = "^\\d{3}-\\d{3}-\\d{4}$";
        if (!Pattern.matches(phoneRegex, phone)) {
            showMessage(context, "Phone inputted in an unacceptable format");
            return false;
        }
        return true;
    }

    //checks if email has characters or numbers followed by an @ sign for email validation
    public static boolean validateEmailAddress(String email, Context context) {
        if (email.trim().isEmpty()) {
            showMessage(context, "Email field is empty");
            return false;
        }

        String emailRegex = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+";
        if (!Pattern.matches(emailRegex, email)) {
            showMessage(context, "Email inputted in an unacceptable format");
            return false;
        }
        return true;
    }

    //generates an error message based on context and inputted message
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
