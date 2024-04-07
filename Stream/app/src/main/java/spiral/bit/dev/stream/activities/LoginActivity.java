package spiral.bit.dev.stream.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import spiral.bit.dev.stream.R;
import spiral.bit.dev.stream.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText personMail, personPassword;
    private Button regBtn, authBtn;
    private FirebaseAuth auth;
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        personMail = findViewById(R.id.et_email);
        personPassword = findViewById(R.id.et_password);
        regBtn = findViewById(R.id.btn_reg);
        authBtn = findViewById(R.id.btn_login);

        authBtn.setOnClickListener(v -> {
            String email, password;
            email = personMail.getText().toString();
            password = personPassword.getText().toString();
            if (!personMail.getText().toString().isEmpty() &&
                    Patterns.EMAIL_ADDRESS.matcher(personMail.getText().toString()).matches()) {
                if (!personPassword.getText().toString().isEmpty() &&
                        personPassword.getText().toString().length() >= 7) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                } else {
                                    Toast.makeText(this, "Такого пользователя не существует.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Такого пользователя не существует.", Toast.LENGTH_SHORT).show());
                } else {
                    personPassword.setError("Пароль должен состоять минимум из 7 символов!");
                }
            } else {
                personMail.setError("Введите корректную почту!");
            }
        });
        regBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }
}