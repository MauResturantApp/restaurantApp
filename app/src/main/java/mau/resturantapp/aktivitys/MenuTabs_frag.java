package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;

/**
 * Created by anwar on 10/16/16.
 */

public class MenuTabs_frag extends Fragment {

    private View rod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rod = inflater.inflate(R.layout.food_menu_frag, container, false);

        ViewPager tabs = (ViewPager) rod.findViewById(R.id.menuTabContent);
        tabs.setAdapter(new TabsAdapter(getActivity().getSupportFragmentManager(), getContext()));

        TabLayout tableLayout = (TabLayout) rod.findViewById(R.id.tabLayout);

        tableLayout.setupWithViewPager(tabs);


        return rod;
    }


    public class TabsAdapter extends FragmentPagerAdapter {
        private final int antalTabs = appData.currentTabs.length;
        private String tabNames[] = appData.currentTabs;
        private Context context;

        public TabsAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return MenuList_frag.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return antalTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }
    }


}

