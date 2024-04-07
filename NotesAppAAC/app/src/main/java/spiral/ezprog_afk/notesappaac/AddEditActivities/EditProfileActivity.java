package spiral.ezprog_afk.notesappaac.AddEditActivities;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import spiral.ezprog_afk.notesappaac.R;

public class EditProfileActivity extends AppCompatActivity {

    //Vars:

    private ImageView closeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        closeImg = findViewById(R.id.close_image);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
