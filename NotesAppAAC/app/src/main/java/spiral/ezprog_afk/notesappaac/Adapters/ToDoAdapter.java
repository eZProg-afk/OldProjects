package spiral.ezprog_afk.notesappaac.Adapters;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;
import spiral.ezprog_afk.notesappaac.R;

public class ToDoAdapter extends ListAdapter<ToDoItem, ToDoAdapter.ToDoHolder> {

    //Vars:

    private OnItemClickListener listener;

    public ToDoAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ToDoItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ToDoItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getText().equals(newItem.getText()) &&
                    oldItem.getPriority() == (newItem.getPriority());
        }
    };


    @NonNull
    @Override
    public ToDoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_item, parent, false);
        return new ToDoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoHolder holder, int position) {
        ToDoItem currentToDoItem = getItem(position);

        holder.toDoTextView.setText(currentToDoItem.getText());
        holder.priorityTextView.setText(String.valueOf(currentToDoItem.getPriority()));
    }

    public ToDoItem getToDoItemAt(int position) {
        return getItem(position);
    }

    class ToDoHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBoxComplete;
        private TextView toDoTextView;
        private TextView priorityTextView;

        public ToDoHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxComplete = itemView.findViewById(R.id.complete_to_do_item_chx);
            toDoTextView = itemView.findViewById(R.id.to_do_item_TV);
            priorityTextView = itemView.findViewById(R.id.text_view_priority);
            checkBoxComplete.setChecked(false);

            checkBoxComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        toDoTextView.setPaintFlags(toDoTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        toDoTextView.setPaintFlags(0);
                    }
                }
            });

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
        void onItemClick(ToDoItem toDoItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

