package spiral.bit.dev.wallsforteens.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import spiral.bit.dev.wallsforteens.Fragments.CarsFragment;
import spiral.bit.dev.wallsforteens.Fragments.CelebritiesFragment;
import spiral.bit.dev.wallsforteens.Fragments.JapanFragment;
import spiral.bit.dev.wallsforteens.Fragments.ExclusiveFragment;
import spiral.bit.dev.wallsforteens.Fragments.SkateboardingFragment;
import spiral.bit.dev.wallsforteens.Fragments.WallsGirlsFragment;
import spiral.bit.dev.wallsforteens.Fragments.WallsBoysFragment;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new ExclusiveFragment();
            case 1:
                return new WallsBoysFragment();
            case 2:
                return new WallsGirlsFragment();
            case 3:
                return new JapanFragment();
            case 4:
                return new CelebritiesFragment();
            case 5:
                return new SkateboardingFragment();
            case 6:
                return new CarsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Параллакс";
            case 1:
                return "Обои для мальчиков";
            case 2:
                return "Обои для девочек";
            case 3:
                return "Япония";
            case 4:
                return "Звёзды";
            case 5:
                return "Обои";
            case 6:
                return "Машины";
            default:
                return null;
        }
    }
}