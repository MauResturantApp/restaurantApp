package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.events.OnSuccesfullLogInEvent;


import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnTabSelectListener {

    //private Toolbar mainTollbar;
    private FloatingActionButton actBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomBar bottomBar;
    private ImageButton drawerButton;


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

        bottomBar.setOnTabSelectListener(this);

        int dp = DPtoPixels(46f);
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
            // TODO: 11/9/16
        }

    }
    /*
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int menuSelect = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;

        switch (menuSelect) {
            case R.id.menu_foodmenu:
                frag = new MenuTabs_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_login:
                frag = new Login_frag();
                                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_findos:
                frag = new FindWay_frag();
                                                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_qrTest:
                frag = new QRTest();
                                                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_home:
                frag = new Home_frag();
                                                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_indstillinger:
                frag = new Settings_frag();
                                                ft.addToBackStack(null);

                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_kontakt:

                frag = new Contact_frag();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_items_menu, menu);
        return true;
    }
*/
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
}
