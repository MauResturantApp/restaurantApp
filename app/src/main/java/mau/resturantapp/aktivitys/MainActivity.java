package mau.resturantapp.aktivitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;

import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnMenuTabSelectedListener {

    //private Toolbar mainTollbar;
    private FloatingActionButton actBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hovedakt_akt);
        //mainTollbar = (Toolbar) findViewById(R.id.mainToolbar);
       // setSupportActionBar(mainTollbar);
        EventBus.getDefault().register(this);
       // getSupportActionBar().setTitle(null);
        showHomeScreen();

        bottomBar = BottomBar.attach(this,savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_menu,this);

        FrameLayout sheet = (FrameLayout) findViewById(R.id.frameLayout_bottomSheet);
        bottomSheetBehavior = from(sheet);


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dp = 46f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixls + 0.5f);

        bottomSheetBehavior.setPeekHeight(pixels);
        bottomSheetBehavior.setHideable(true);

        bottomSheetBehavior.setState(STATE_COLLAPSED);

        bottomBar.setElevation(4);

        bottomBar.setActiveTabColor("#e00404");
        bottomBar.setBackgroundColor(Color.rgb(157,219,50));

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


    @Override
    public void onMenuItemSelected(@IdRes int menuItemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;

        switch (menuItemId){
            case R.id.menuItem_food:
                frag = new MenuTabs_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();

                break;

            case R.id.menuItem_home:
                frag = new Home_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menuItem_contact:
                frag = new Contact_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;
            default:
                break;
        }
    }
}
