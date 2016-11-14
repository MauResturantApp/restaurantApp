package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.FragContainerChangedEvent;

/**
 * Created by anwar on 10/16/16.
 */

public class MenuTabs_frag extends Fragment implements OnTabSelectedListener {

    private View rod;
    private TabLayout tableLayout;
    private ViewPager tabs;
    private ImageView tabsImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.food_menu_frag, container, false);

        tabs = (ViewPager) rod.findViewById(R.id.menuTabContent);
        tabs.setAdapter(new TabsAdapter(getChildFragmentManager(), getContext()));

        tabsImage = (ImageView) rod.findViewById(R.id.menu_tabs_image);

        tableLayout = (TabLayout) rod.findViewById(R.id.tabLayout);

        tableLayout.setupWithViewPager(tabs);

        tableLayout.addOnTabSelectedListener(this);

        return rod;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        EventBus.getDefault().post(event);
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



    public void newTabSelectedEvent(int tabPage){
        int pageNumber = tabPage + 1;

        switch (pageNumber){
            case 1:
                tabsImage.setImageResource(R.mipmap.tabs_aqurk);
                break;
            case 2:
                tabsImage.setImageResource(R.mipmap.tabs_burger);
                break;
            case 3:
                tabsImage.setImageResource(R.mipmap.tabs_cola);
                break;
            case 4:
                tabsImage.setImageResource(R.mipmap.tabs_pizza);
                break;
            case 5:
                tabsImage.setImageResource(R.mipmap.tabs_aqurk);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        newTabSelectedEvent(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

