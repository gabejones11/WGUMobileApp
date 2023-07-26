package dev.gabriel.wguMobileApp.UI;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Assessment;

public class SwipeToDeleteAssessments extends ItemTouchHelper.SimpleCallback {
    private Repository mRepository;
    private AssessmentsAdapter mAssessmentsAdapter;

    //constructs swipe to delete object specifies that the action will take place on left swipe
    public SwipeToDeleteAssessments(AssessmentsAdapter assessmentsAdapter, Repository repository) {
        super(0, ItemTouchHelper.LEFT);
        mAssessmentsAdapter = assessmentsAdapter;
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
        Assessment deletedAssessment = mAssessmentsAdapter.getAssessment(position);
        mAssessmentsAdapter.deleteAssessment(position);
        mRepository.delete(deletedAssessment);
        Toast.makeText(viewHolder.itemView.getContext(), "Assessment Deleted", Toast.LENGTH_LONG).show();
    }
}
