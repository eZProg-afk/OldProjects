package spiral.ezprog_afk.notesappaac.Settings;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import spiral.ezprog_afk.notesappaac.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.notes_preferences);
    }
}
