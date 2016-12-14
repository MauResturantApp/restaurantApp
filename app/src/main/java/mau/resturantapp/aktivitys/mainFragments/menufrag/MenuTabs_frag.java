package mau.resturantapp.aktivitys.mainFragments.menufrag;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.TabLayout.OnTabSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuTab;
import mau.resturantapp.data.appData;
import mau.resturantapp.event.events.TabsChangedEvent;
import mau.resturantapp.utils.Firebase.FirebaseRead;

public class MenuTabs_frag extends Fragment implements OnTabSelectedListener {

    private View rod;
    private TabLayout tableLayout;
    private ViewPager tabs;
    private ImageView tabsImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.food_menu_frag, container, false);

        tabs = (ViewPager) rod.findViewById(R.id.menuTabContent);
        tabs.setAdapter(new TabsAdapter(getChildFragmentManager()));

        tabsImage = (ImageView) rod.findViewById(R.id.menu_tabs_image);

        tableLayout = (TabLayout) rod.findViewById(R.id.tabLayout);

        tableLayout.setupWithViewPager(tabs);

        tableLayout.addOnTabSelectedListener(this);
        FirebaseRead.getTabs();
        return rod;
    }

    @Subscribe
    public void TabsChangedEvent(TabsChangedEvent event){
        Log.d("MenuTabsFirebase", "TabsChangedEvent");
        TabsAdapter adapter = (TabsAdapter) tabs.getAdapter();
        if(adapter != null) {
            adapter.updatePageTitles();
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        appData.event.fragContainerChanged();
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
        EventBus.getDefault().unregister(this);
        Log.d("tabs" , "stop: " );
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

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

    public class TabsAdapter extends FragmentStatePagerAdapter {
        private ArrayList<MenuTab> tabs = appData.tabs;



        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getItemPosition(Object object){
            //This is used to avoid full view recreation when changes to the data occur

            MenuList_frag item = (MenuList_frag) object;
            int pageNumber = item.getPageNumber();
            String pageTitle = item.getPageTitle();
            MenuTab menuTab = tabs.get(pageNumber);
            if(menuTab != null){
                if(menuTab.getPosition() == pageNumber && menuTab.getName().equals(pageTitle)){
                    return POSITION_UNCHANGED;
                }
            }

            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("getitem funktion" , "position : " + position);

            return MenuList_frag.newInstance(position + 1, tabs.get(position).getName(), tabs.get(position).getKey());
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).getName();
        }

        public void updatePageTitles(){
            tabs = appData.tabs;
        }
    }

}
