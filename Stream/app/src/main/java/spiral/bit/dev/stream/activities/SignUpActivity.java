package spiral.bit.dev.stream.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import spiral.bit.dev.stream.R;
import spiral.bit.dev.stream.databinding.ActivitySignUpBinding;
import spiral.bit.dev.stream.model.User;
import timber.log.Timber;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText personMail, personPassword, personName;
    private FirebaseFirestore firestore;
    private ActivitySignUpBinding binding;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        personMail = binding.etEmail;
        personName = binding.etName;
        personPassword = binding.etPassword;
        Button regBtn = findViewById(R.id.btn_reg);
        Button authBtn = findViewById(R.id.btn_login);
        authBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
        regBtn.setOnClickListener(v -> {
            String email, password, name;
            email = personMail.getText().toString();
            password = personPassword.getText().toString();
            name = personName.getText().toString();
            User user = new User(email, password, name, "898989898");
            binding.setUser(user);
            if (!name.isEmpty()) {
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty() && password.length() >= 7) {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        firestore.collection("users_stream")
                                                .document(auth.getCurrentUser().getUid())
                                                .set(user);
                                        if (!getPermissions()) {
                                            getPermissions();
                                        } else {
                                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                                        }
                                    } else {
                                        Toast.makeText(SignUpActivity.this,
                                                getString(R.string.error_toast),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this,
                                        getString(R.string.error_toast),
                                        Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Пароль должен состоять минимум из 7 символов!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    personMail.setError("Введите корректную почту!");
                }
            } else {
                personName.setError("Имя не может быть пустым!");
            }
        });
    }

    private boolean getPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            readContacts(this);
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission
                                    .READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String @NotNull [] permissions,
                                           int @NotNull [] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if ((grantResults.length > 0) &&
                    (grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED)) {
                Timber.d("Permission was granted!");
                readContacts(this);
            } else {
                Timber.d("Permission denied!");
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private void readContacts(Context context) {
        User contact;
        Map<String, Object> contactNumber = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contact = new User();
                String id = cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts._ID));
                contact.setId(id);
                String name = cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts
                                        .DISPLAY_NAME));
                contact.setName(name);
                String has_phone = cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts
                                        .HAS_PHONE_NUMBER));
                if (Integer.parseInt(has_phone) > 0) {
                    Cursor pCur;
                    pCur = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds
                                    .Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds
                                    .Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(
                                        ContactsContract.
                                                CommonDataKinds.
                                                Phone.NUMBER));
                        contact.setPhone(phone);
                        contactNumber.put(contact.getId(), contact);
                    }
                    pCur.close();
                }
                firestore.collection("users_stream")
                        .document(auth.getCurrentUser().getUid())
                        .collection("contacts")
                        .document().set(contactNumber)
                        .addOnSuccessListener(aVoid -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class))).addOnFailureListener(e -> Toast.makeText(SignUpActivity.this,
                        getString(R.string.error_toast),
                        Toast.LENGTH_SHORT).show());
            }
            cursor.close();
        }
    }
}