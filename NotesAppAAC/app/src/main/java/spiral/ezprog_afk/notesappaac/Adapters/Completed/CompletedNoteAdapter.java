package spiral.ezprog_afk.notesappaac.Adapters.Completed;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import spiral.ezprog_afk.notesappaac.Models.Completed.CompletedNote;
import spiral.ezprog_afk.notesappaac.Models.Note;
import spiral.ezprog_afk.notesappaac.R;

public class CompletedNoteAdapter extends ListAdapter<CompletedNote, CompletedNoteAdapter.CompletedNoteHolder> {

    //Vars:

    private OnItemClickListener listener;

    public CompletedNoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CompletedNote> DIFF_CALLBACK = new DiffUtil.ItemCallback<CompletedNote>() {
        @Override
        public boolean areItemsTheSame(CompletedNote oldItem, CompletedNote newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(CompletedNote oldItem, CompletedNote newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority().equals(newItem.getPriority());
        }
    };

    @NonNull
    @Override
    public CompletedNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_note_item, parent, false);
        return new CompletedNoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedNoteHolder holder, int position) {
        CompletedNote currentNote = getItem(position);

        holder.completedTitleTextView.setText(currentNote.getTitle());
        holder.completedDescriptionTextView.setText(currentNote.getDescription());
        holder.completedPriorityTextView.setText(String.valueOf(currentNote.getPriority()));
        holder.completedDateTextView.setText(currentNote.getDateOfTask());

        if (holder.completedPriorityTextView.getText().toString().equals("Important")) {
            holder.completedImportanceImageView.setImageResource(R.drawable.ic_flag_red);
        } else if (holder.completedPriorityTextView.getText().toString().equals("Важно")) {
            holder.completedImportanceImageView.setImageResource(R.drawable.ic_flag_red);
        } else if (holder.completedPriorityTextView.getText().toString().equals("Medium")) {
            holder.completedImportanceImageView.setImageResource(R.drawable.ic_flag_orange);
        } else if (holder.completedPriorityTextView.getText().toString().equals("Средне")) {
            holder.completedImportanceImageView.setImageResource(R.drawable.ic_flag_orange);
        } else if (holder.completedPriorityTextView.getText().toString().equals("Not so important")) {
            holder.completedImportanceImageView.setImageResource(R.drawable.ic_flag_green);
        } else if (holder.completedPriorityTextView.getText().toString().equals("Не так важно")) {
            holder.completedImportanceImageView.setImageResource(R.drawable.ic_flag_green);
        }
    }

    public CompletedNote getCompletedNoteAt(int position) {
        return getItem(position);
    }

    class CompletedNoteHolder extends RecyclerView.ViewHolder {

        private TextView completedTitleTextView;
        private TextView completedDescriptionTextView;
        private TextView completedPriorityTextView;
        private TextView completedDateTextView;
        private ImageView completedImportanceImageView;

        public CompletedNoteHolder(@NonNull View itemView) {
            super(itemView);
            completedTitleTextView = itemView.findViewById(R.id.completed_text_view_title);
            completedDescriptionTextView = itemView.findViewById(R.id.completed_text_view_description);
            completedPriorityTextView = itemView.findViewById(R.id.completed_text_view_priority);
            completedImportanceImageView = itemView.findViewById(R.id.completed_importance_image_view);
            completedDateTextView = itemView.findViewById(R.id.completed_date_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CompletedNote completedNote);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
