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

import dev.gabriel.wguMobileApp.Entities.Assessment;
import dev.gabriel.wguMobileApp.R;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.AssessmentsViewHolder> {
    class AssessmentsViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentNameView;
        private final TextView assessmentDateView;
        private final TextView assessmentTypeView;
        private AssessmentsViewHolder(View itemView) {
            //assign all of the fields that we want to display in the view holder
            super(itemView);
            assessmentNameView = itemView.findViewById(R.id.assessmentNameTextView);
            assessmentDateView = itemView.findViewById(R.id.assessmentDateTextView);
            assessmentTypeView = itemView.findViewById(R.id.assessmentTypeTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if we click on the view holder we are going to send all the information to the modify screen
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, AddAssessmentScreen.class);
                    intent.putExtra("id", current.getAssessmentID());
                    intent.putExtra("assessmentType", current.isObjective());
                    intent.putExtra("assessmentName", current.getAssessmentName());
                    intent.putExtra("assessmentStartDate", current.getAssessmentStartDate());
                    intent.putExtra("assessmentEndDate", current.getAssessmentEndDate());
                    intent.putExtra("courseID", current.getCourseID());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;
    public AssessmentsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public AssessmentsAdapter.AssessmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentsAdapter.AssessmentsViewHolder holder, int position) {
        //set the text for the view holder card view
        if (mAssessments != null) {
            Assessment current = mAssessments.get(position);
            holder.assessmentNameView.setText(current.getAssessmentName());
            holder.assessmentDateView.setText(current.getAssessmentStartDate() + " - " + current.getAssessmentEndDate());
            if (current.isObjective()) {
                holder.assessmentTypeView.setText("O");
            } else{
                holder.assessmentTypeView.setText("P");
            }

        } else {
            holder.assessmentNameView.setText("");
            holder.assessmentDateView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        //to avoid getting a null pointer exception only return the list size if it's not null
        if (mAssessments != null) {
            return mAssessments.size();
        }
        return 0;
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }

    public void deleteAssessment(int position) {
        mAssessments.remove(position);
        notifyItemRemoved(position);
    }

    public Assessment getAssessment(int position) {
        return mAssessments.get(position);
    }
}
