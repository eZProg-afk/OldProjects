package spiral.bit.dev.stream.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import spiral.bit.dev.stream.R;
import spiral.bit.dev.stream.model.User;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactsViewHolder> {

    private ArrayList<User> users;

    public ContactAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,
                parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        User user = users.get(position);
        holder.phoneTv.setText(user.getPhone());
        holder.nameTv.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, phoneTv;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            phoneTv = itemView.findViewById(R.id.phone_tv);
        }
    }
}
