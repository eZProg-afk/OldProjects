package spiral.bit.dev.stream.activities;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spiral.bit.dev.stream.R;
import spiral.bit.dev.stream.adapters.ContactAdapter;
import spiral.bit.dev.stream.model.User;
import spiral.bit.dev.stream.other.BaseActivity;
import timber.log.Timber;

public class ContactsActivity extends BaseActivity {

    private ArrayList<User> usersArrayList;
    private Map<String, Object> mapUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setUpBottomNavView();

        usersArrayList = new ArrayList<>();
        mapUsers = new HashMap<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("users_stream")
                .document(auth.getCurrentUser().getUid())
                .collection("contacts");

        users.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                users.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                        mapUsers = documentSnapshot.toObjects(User.class);
                    }
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(ContactsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        for (User user : usersArrayList) {
            Timber.d(user.getPhone());
        }
        RecyclerView contactsRecycler = findViewById(R.id.contacts_recycler);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(this));
        ContactAdapter adapter = new ContactAdapter(usersArrayList);
        contactsRecycler.setAdapter(adapter);
        contactsRecycler.setHasFixedSize(true);
        contactsRecycler.setItemAnimator(new DefaultItemAnimator());
    }
}