package apps.denux.mayorga.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import apps.denux.mayorga.controladores.EventosFragment;
import apps.denux.mayorga.controladores.ServidorFragment;

/**
 * Created by dexter on 15/03/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public static int TAB_SERVIDOR = 0;
    public static int TAB_EVENTOS = 1;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new ServidorFragment();
            case 1:
                return new EventosFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}