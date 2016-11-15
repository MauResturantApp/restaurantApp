package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import mau.resturantapp.aktivitys.dialogs.Dialog_askForLogin;
import mau.resturantapp.aktivitys.dialogs.Dialog_login;
import mau.resturantapp.aktivitys.dialogs.Dialog_signup;
import mau.resturantapp.data.appData;
import mau.resturantapp.event.events.FragContainerChangedEvent;
import mau.resturantapp.event.events.NewUserSuccesfullEvent;
import mau.resturantapp.event.events.OnFailedLogIn;
import mau.resturantapp.event.events.OnSuccesfullLogInEvent;
import mau.resturantapp.event.events.ShowAskforLoginDialogEvent;
import mau.resturantapp.event.events.ShowLogInDialogEvent;
import mau.resturantapp.event.events.ShowSignupDialogEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;


import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnTabSelectListener, OnNavigationItemSelectedListener,OnTouchListener {

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
        showHomeScreen();


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
    public void userlogedInEvent(OnSuccesfullLogInEvent event) {
        showHomeScreen();
        userLoggedIn();
    }



    private void showHomeScreen() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag = new Home_frag();
        ft.replace(R.id.mainFrameFrag, frag).commit();
        bottomBar.selectTabAtPosition(0);
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
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.mainFrameFrag);

        Log.d("tab slected", "clicked " + tabId + " --- " + R.id.botbar_food);

        switch (tabId){
            case R.id.botbar_food:
                if (!(currentFrag instanceof MenuTabs_frag)){


                    frag = new MenuTabs_frag();
                    ft.addToBackStack(null);
                    ft.replace(R.id.mainFrameFrag, frag).commit();
                }

                break;

            case R.id.botbar_home:

                if(!(currentFrag instanceof Home_frag)){

                    frag = new Home_frag();
                    ft.addToBackStack(null);
                    ft.replace(R.id.mainFrameFrag, frag).commit();
                }

                break;

            case R.id.botbar_contact:
                if (!(currentFrag instanceof Contact_frag) ){

                    frag = new Contact_frag();
                    ft.addToBackStack(null);
                    ft.replace(R.id.mainFrameFrag, frag).commit();
                    Log.d("tab slected", "contact");
                }

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
        switch (item.getItemId()) {


            case R.id.menu_login:
                showLoginDialog();
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
                showSignupDialog();
                break;
            case R.id.menu_logout:
                appData.cartContent.clear();
                appData.event.newItemToCart();
                appData.currentUser = null;
                userLoggedOut();
                showHomeScreen();
            default:


                break;

        }
        setActiveBarTab();
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

    public void Btn_Checkout_clicked(View v){
      if(appData.currentUser == null){
          showAskForLoginDialog();
      }
        else{
          // vis checkout frag her.
      }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v == fadeView){
            if(bottomSheetBehavior.getState() == STATE_EXPANDED)
                hideCart();
        }

        return false;
    }

    private void showSignupDialog(){
        Dialog_signup d = new Dialog_signup();
        d.show(getSupportFragmentManager(),null);
    }

    private void showLoginDialog(){
        Dialog_login d = new Dialog_login();
        d.show(getSupportFragmentManager(),null);
    }

    private void showAskForLoginDialog(){
        Dialog_askForLogin d = new Dialog_askForLogin();
        d.show(getSupportFragmentManager(),null);

    }

    @Subscribe
    public void succesSignup(NewUserSuccesfullEvent event) {
        showHomeScreen();
        userLoggedIn();
    }

    @Subscribe
    public void askForLoginDialogEvent(ShowAskforLoginDialogEvent event){
        showAskForLoginDialog();
    }

    @Subscribe
    public void signupDialogEvent(ShowSignupDialogEvent event){
        showSignupDialog();
    }

    @Subscribe
    public void loginDialogEvent(ShowLogInDialogEvent event){
        showLoginDialog();
    }






    @Subscribe
    public void setActiveBarTab(){
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.mainFrameFrag);
        bottomBar.setActiveTabColor(ContextCompat.getColor(this,R.color.colorSecondaryDark));
        Log.d("ggggggg","gggggg");
        if(currentFrag instanceof Home_frag){
            bottomBar.selectTabAtPosition(0);

        }
        else if (currentFrag instanceof MenuTabs_frag){
                        bottomBar.selectTabAtPosition(1);


        }
        else if (currentFrag instanceof Contact_frag ){
                        bottomBar.selectTabAtPosition(2);

        }
        else {

            bottomBar.setActiveTabColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }

    }

}
