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

import dev.gabriel.wguMobileApp.Entities.Term;
import dev.gabriel.wguMobileApp.R;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.TermsViewHolder> {
    class TermsViewHolder extends RecyclerView.ViewHolder {
        private final TextView termNameTextView;
        private final TextView termDateTextView;

        public TermsViewHolder(@NonNull View itemView) {
            //assign all of the fields that we want to display in the view holder
            super(itemView);
            termNameTextView = itemView.findViewById(R.id.termNameTextView);
            termDateTextView = itemView.findViewById(R.id.termDateTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if we click on the view holder we are going to send all the information to the modify screen
                    int position = getAdapterPosition();
                    final Term current = mTerms.get(position);
                    Intent intent = new Intent(context, AddTermScreen.class);
                    intent.putExtra("id", current.getTermID());
                    intent.putExtra("termName", current.getTermTitle());
                    intent.putExtra("termStart", current.getStartDate());
                    intent.putExtra("termEnd", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }


    }

    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflater;
    public TermsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsViewHolder holder, int position) {
        //set the text for the view holder card view
        if (mTerms != null) {
            Term current = mTerms.get(position);
            holder.termNameTextView.setText(current.getTermTitle());
            holder.termDateTextView.setText(current.getStartDate() + " - " + current.getEndDate());
        }
    }

    @Override
    public int getItemCount() {
        //to avoid getting a null pointer exception only return the list size if it's not null
        if (mTerms != null) {
            return mTerms.size();
        }
        return 0;
    }

    public void setTerm(List<Term> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

    public void deleteTerm(int position) {
        mTerms.remove(position);
        notifyItemRemoved(position);
    }

    public Term getTerm(int position) {
        return mTerms.get(position);
    }
}
