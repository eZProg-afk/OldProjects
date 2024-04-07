package spiral.ezprog_afk.notesappaac.Adapters;

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

import spiral.ezprog_afk.notesappaac.Models.Note;
import spiral.ezprog_afk.notesappaac.R;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    //Vars:

    private OnItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority().equals(newItem.getPriority()) &&
                    oldItem.getDateOfTask().equals(newItem.getDateOfTask()) &&
                    oldItem.getTimeOfTask().equals(newItem.getTimeOfTask());
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);

        holder.titleTextView.setText(currentNote.getTitle());
        holder.descriptionTextView.setText(currentNote.getDescription());
        holder.priorityTextView.setText(String.valueOf(currentNote.getPriority()));
        holder.dateTextView.setText(currentNote.getDateOfTask());
        holder.timeTextView.setText(currentNote.getTimeOfTask());

        if (holder.priorityTextView.getText().toString().equals("Important")) {
            holder.importanceImageView.setImageResource(R.drawable.ic_flag_red);
        } else if (holder.priorityTextView.getText().toString().equals("Важно")) {
            holder.importanceImageView.setImageResource(R.drawable.ic_flag_red);
        } else if (holder.priorityTextView.getText().toString().equals("Medium")) {
            holder.importanceImageView.setImageResource(R.drawable.ic_flag_orange);
        } else if (holder.priorityTextView.getText().toString().equals("Средне")) {
            holder.importanceImageView.setImageResource(R.drawable.ic_flag_orange);
        } else if (holder.priorityTextView.getText().toString().equals("Not so important")) {
            holder.importanceImageView.setImageResource(R.drawable.ic_flag_green);
        } else if (holder.priorityTextView.getText().toString().equals("Не так важно")) {
            holder.importanceImageView.setImageResource(R.drawable.ic_flag_green);
        }
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView priorityTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private ImageView importanceImageView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            descriptionTextView = itemView.findViewById(R.id.text_view_description);
            priorityTextView = itemView.findViewById(R.id.text_view_priority);
            importanceImageView = itemView.findViewById(R.id.importance_image_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);

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
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
