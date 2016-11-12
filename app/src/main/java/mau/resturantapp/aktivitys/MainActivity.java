package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewItemToCartEvent;
import mau.resturantapp.events.NewUserSuccesfullEvent;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;


import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnTabSelectListener, OnNavigationItemSelectedListener,OnTouchListener {

    //private Toolbar mainTollbar;
    private FloatingActionButton actBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomBar bottomBar;
    private ImageButton drawerButton;
    private DrawerLayout drawLayout;
    private NavigationView sideMenu;
    private View fadeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hovedakt_akt);
        EventBus.getDefault().register(this);
        showHomeScreen();

        fadeView = findViewById(R.id.fadeView);


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

        fadeView.setOnTouchListener(this);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if(newState == STATE_DRAGGING){
                    fadeView.animate().alpha(0.3f);
                    fadeView.setVisibility(View.VISIBLE);
                }

                if(newState == STATE_SETTLING){
                    fadeView.animate().alpha(0.3f);
                    fadeView.setVisibility(View.VISIBLE);
                }


                if(newState == STATE_EXPANDED){
                    fadeView.animate().alpha(0.3f);
                    fadeView.setVisibility(View.VISIBLE);
                }
                if(newState == STATE_COLLAPSED || newState == STATE_HIDDEN){
                    fadeView.animate().alpha(0.0f);
                    fadeView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }
        });

        hideCart();

        bottomBar.setElevation(4);

        actBtn = (FloatingActionButton) findViewById(R.id.floatActBtn_cartContent);

        actBtn.setOnClickListener(this);

        if(appData.currentUser == null){
            userLoggedOut();
        }
        else {
            userLoggedIn();
        }



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
            hideCart();
            drawLayout.openDrawer(Gravity.LEFT);
        }


    }

    @Subscribe
    public void logedInEvent(OnSuccesfullLogInEvent event) {
        showHomeScreen();
        userLoggedIn();
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
        hideCart();
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

    private void hideCart(){
        bottomSheetBehavior.setState(STATE_COLLAPSED);
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;
        drawLayout.closeDrawer(Gravity.LEFT);
        item.setChecked(true);

        Log.d("im in", "item selected");
        hideCart();
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

            case R.id.menu_signin:
                frag = new Signup_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;
            case R.id.menu_logout:
                appData.cartContent.clear();
                NewItemToCartEvent event = new NewItemToCartEvent();
                EventBus.getDefault().post(event);
                appData.currentUser = null;
                userLoggedOut();
                showHomeScreen();
            default:


                break;

        }
        return true;

    }

    private void userLoggedIn(){
        Menu navMenu = sideMenu.getMenu();
        navMenu.findItem(R.id.menu_login).setVisible(false);
        navMenu.findItem(R.id.menu_signin).setVisible(false);
        navMenu.findItem(R.id.menu_logout).setVisible(true);
        navMenu.findItem(R.id.menu_qrTest).setVisible(true);


        if(appData.currentUser.isAdmin()){
            navMenu.findItem(R.id.menu_qrCamera).setVisible(true);
            navMenu.findItem(R.id.menu_qrTest).setVisible(true);
        }
    }
    private void userLoggedOut(){
        Menu navMenu = sideMenu.getMenu();
        navMenu.findItem(R.id.menu_login).setVisible(true);
        navMenu.findItem(R.id.menu_signin).setVisible(true);
        navMenu.findItem(R.id.menu_logout).setVisible(false);
        navMenu.findItem(R.id.menu_qrCamera).setVisible(false);
        navMenu.findItem(R.id.menu_qrTest).setVisible(false);



    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v == fadeView){
            if(bottomSheetBehavior.getState() == STATE_EXPANDED)
                hideCart();
        }

        return false;
    }

    @Subscribe
    public void succesSignup(NewUserSuccesfullEvent event) {
        showHomeScreen();
        userLoggedIn();
    }
}
