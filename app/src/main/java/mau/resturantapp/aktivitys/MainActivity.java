package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;


import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnTabSelectListener, NavigationView.OnNavigationItemSelectedListener {

    //private Toolbar mainTollbar;
    private FloatingActionButton actBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomBar bottomBar;
    private ImageButton drawerButton;
    private DrawerLayout drawLayout;
    private NavigationView sideMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hovedakt_akt);
        EventBus.getDefault().register(this);
        showHomeScreen();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar_main);
        drawerButton = (ImageButton) findViewById(R.id.btn_sidemenu);
        FrameLayout sheet = (FrameLayout) findViewById(R.id.frameLayout_bottomSheet);
        bottomSheetBehavior = from(sheet);
        drawerButton.setOnClickListener(this);
        drawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        sideMenu = (NavigationView) findViewById(R.id.navView_sideMenu);

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                onTabSelected(tabId);
            }
        });

        bottomBar.setOnTabSelectListener(this);

        if(sideMenu != null){
            sideMenu.setNavigationItemSelectedListener(this);
        }

        int dp = DPtoPixels(39f); // husk f efter tallet
        bottomSheetBehavior.setPeekHeight(dp);
        bottomSheetBehavior.setHideable(true);

        bottomSheetBehavior.setState(STATE_COLLAPSED);

        bottomBar.setElevation(4);

        actBtn = (FloatingActionButton) findViewById(R.id.floatActBtn_cartContent);

        actBtn.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        if(v == actBtn){
            if(bottomSheetBehavior.getState() == STATE_EXPANDED)
                bottomSheetBehavior.setState(STATE_HIDDEN);
            else
                bottomSheetBehavior.setState(STATE_EXPANDED);

        }
        if(v == drawerButton){
            drawLayout.openDrawer(Gravity.LEFT);
        }

    }

    @Subscribe
    public void logedInEvent(OnSuccesfullLogInEvent event) {
        showHomeScreen();
    }

    private void hideCart() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentCartContent = fragmentManager.findFragmentById(R.id.cartContentShowHide_frag);
        transaction.hide(fragmentCartContent).commit();
    }



    private void showHomeScreen() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag = new Home_frag();
        ft.replace(R.id.mainFrameFrag, frag).commit();
    }

    private int DPtoPixels(float DP){
       int pixels = 0;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dp = DP;
        float fpixels = metrics.density * dp;
        pixels = (int) (fpixels + 0.5f);
        return pixels;
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;

        Log.d("tab slected", "clicked " + tabId + " --- " + R.id.botbar_food);

        switch (tabId){
            case R.id.botbar_food:
                frag = new MenuTabs_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();

                break;

            case R.id.botbar_home:
                frag = new Home_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();

                break;

            case R.id.botbar_contact:
                frag = new Contact_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                Log.d("tab slected", "contact");

                break;
            default:
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;
        drawLayout.closeDrawer(Gravity.LEFT);
        item.setChecked(true);

        Log.d("im in", "item selected");

    //hej med dig
        switch (item.getItemId()) {

            case R.id.menu_login:
                frag = new Login_frag();
                ft.addToBackStack(null);
                Log.d("item selected", "login");
                ft.replace(R.id.mainFrameFrag, frag).commit();

                break;

            case R.id.menu_findos:
                frag = new FindWay_frag();
                ft.addToBackStack(null);
                Log.d("item selected", "findway");

                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_qrTest:
                frag = new QRTest();
                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;


            case R.id.menu_indstillinger:
                frag = new Settings_frag();
                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();

                break;

            case R.id.menu_qrCamera:
                frag = new QRCamera();
                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();

                break;

            default:
                break;

        }
        return true;

    }
}
