package dev.gabriel.wguMobileApp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.R;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameTextView;
        private final TextView courseDateTextView;
        private final TextView courseProgressTextView;


        public CourseViewHolder(@NonNull View itemView) {
            //assign all of the fields that we want to display in the view holder
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseDateTextView = itemView.findViewById(R.id.courseDateTextView);
            courseProgressTextView = itemView.findViewById(R.id.courseStatusTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if we click on the view holder we are going to send all the information to the modify screen
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, AddCourseScreen.class);
                    intent.putExtra("id", current.getCourseID());
                    intent.putExtra("courseTitle", current.getCourseTitle());
                    intent.putExtra("courseStartDate", current.getStartDate());
                    intent.putExtra("courseEndDate", current.getEndDate());
                    intent.putExtra("progressRadioGroup", current.getProgress());
                    intent.putExtra("instructorName", current.getInstructorName());
                    intent.putExtra("instructorPhone", current.getInstructorPhone());
                    intent.putExtra("instructorEmail", current.getInstructorEmail());
                    intent.putExtra("notes", current.getNotes());
                    intent.putExtra("termID", current.getTermID());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;
    public CoursesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        //set the text and text color for the view holder card view
        if (mCourses != null) {
            Course current = mCourses.get(position);
            holder.courseNameTextView.setText(current.getCourseTitle());
            holder.courseDateTextView.setText(current.getStartDate() + " - " + current.getEndDate());
            if (current.getProgress() == 1) {
                holder.courseProgressTextView.setText("Complete");
                holder.courseProgressTextView.setTextColor(context.getColor(R.color.green_complete));
            } else if (current.getProgress() == 2) {
                holder.courseProgressTextView.setText("In-progress");
                holder.courseProgressTextView.setTextColor(context.getColor(R.color.yellow_in_progress));
            } else if (current.getProgress() == 3) {
                holder.courseProgressTextView.setText("Dropped");
                holder.courseProgressTextView.setTextColor(context.getColor(R.color.dark_red_dropped));
            } else if (current.getProgress() == 4) {
                holder.courseProgressTextView.setText("Plan to take");
                holder.courseProgressTextView.setTextColor(context.getColor(R.color.black_plan_to_take));
            }
        }
    }

    @Override
    public int getItemCount() {
        //to avoid getting a null pointer exception only return the list size if it's not null
        if (mCourses != null) {
            return mCourses.size();
        }
        return 0;
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    public void deleteCourse(int position) {
        mCourses.remove(position);
        notifyItemRemoved(position);
    }

    public Course getCourse(int position) {
        return mCourses.get(position);
    }
}
