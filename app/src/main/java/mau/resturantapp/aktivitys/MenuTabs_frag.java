package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.ResumeTabsEvent;

/**
 * Created by anwar on 10/16/16.
 */

public class MenuTabs_frag extends Fragment {

    private View rod;
    private TabLayout tableLayout;
    private ViewPager tabs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.food_menu_frag, container, false);

        tabs = (ViewPager) rod.findViewById(R.id.menuTabContent);
        tabs.setAdapter(new TabsAdapter(getActivity().getSupportFragmentManager(), getContext()));

        tableLayout = (TabLayout) rod.findViewById(R.id.tabLayout);

        tableLayout.setupWithViewPager(tabs);


        return rod;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("tabs" , "resume: " );

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("tabs" , "pause: " );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("tabs" , "destryoy view: " );

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("tabs" , "detach: " );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tabs" , "destroy: " );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("tabs" , "oncreate: " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("tabs" , "stop: " );
    }

    @Override
    public void onStart() {
        super.onStart();


        Log.d("tabs" , "start: " );
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
            Log.d("getitem funktion" , "position : " + position);
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

