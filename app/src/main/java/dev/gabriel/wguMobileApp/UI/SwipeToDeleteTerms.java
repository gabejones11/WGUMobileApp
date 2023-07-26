package dev.gabriel.wguMobileApp.UI;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.gabriel.wguMobileApp.Database.Repository;
import dev.gabriel.wguMobileApp.Entities.Course;
import dev.gabriel.wguMobileApp.Entities.Term;

public class SwipeToDeleteTerms extends ItemTouchHelper.SimpleCallback {
    private Repository repository;
    private TermsAdapter mTermsAdapter;

    //constructs swipe to delete object specifies that the action will take place on left swipe
    public SwipeToDeleteTerms(TermsAdapter termsAdapter, Repository repository) {
        super(0, ItemTouchHelper.LEFT);
        mTermsAdapter = termsAdapter;
        this.repository = repository;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Term deletedTerm = mTermsAdapter.getTerm(position);

        //validate that this term does not have any associated courses
        List<Course> associatedCourses = repository.getCoursesByTermID(deletedTerm.getTermID());
        if (!associatedCourses.isEmpty()) {
            //found associated courses and cannot delete this term
            Toast.makeText(viewHolder.itemView.getContext(), "Cannot delete a term with associated courses", Toast.LENGTH_LONG).show();
            mTermsAdapter.notifyItemChanged(position);
            return;
        }

        //no associated terms were found delete the term
        mTermsAdapter.deleteTerm(position);
        repository.delete(deletedTerm);
        Toast.makeText(viewHolder.itemView.getContext(), "Term Deleted", Toast.LENGTH_LONG).show();
    }
}

