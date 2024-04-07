package spiral.ezprog_afk.notesappaac.Adapters;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import spiral.ezprog_afk.notesappaac.Fragments.ShopFragment;
import spiral.ezprog_afk.notesappaac.Fragments.ToDoFragment;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new ToDoFragment();
            case 1:
                ShopFragment tab2 = new ShopFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TODO";
            case 1:
                return "Shop List";
            default:
                return null;
        }
    }
}