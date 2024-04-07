package spiral.ezprog_afk.notesappaac.BottomActivities;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import spiral.ezprog_afk.notesappaac.Adapters.TabAccessorAdapter;
import spiral.ezprog_afk.notesappaac.Other.BaseActivity;
import spiral.ezprog_afk.notesappaac.R;

public class ToDoShopActivity extends BaseActivity {

    //Vars:

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAccessorAdapter tabAccessorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_shop);
        setUpBottomNav();

        tabLayout = findViewById(R.id.main_tab);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.getTabAt(0).setText(R.string.todo);
        tabLayout.getTabAt(0).setText(R.string.shoplist);

        tabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.main_tabs_pager);
        viewPager.setAdapter(tabAccessorAdapter);
    }
}