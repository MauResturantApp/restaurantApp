package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;

import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Toolbar mainTollbar;
    private FloatingActionButton actBtn;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hovedakt_akt);
        mainTollbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainTollbar);
        EventBus.getDefault().register(this);
        getSupportActionBar().setTitle(null);
        showHomeScreen();

        FrameLayout sheet = (FrameLayout) findViewById(R.id.frameLayout_bottomSheet);
        bottomSheetBehavior = from(sheet);

        bottomSheetBehavior.setPeekHeight(150);
        bottomSheetBehavior.setHideable(true);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == STATE_HIDDEN){
                    bottomSheetBehavior.setState(STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if(bottomSheetBehavior.getState() == STATE_DRAGGING) {
                        Log.d("stage 1", "state 2 " );
                        if (slideOffset < -0.4) {
                            bottomSheetBehavior.setState(STATE_HIDDEN);
                            Log.d("stage 1", "state 23 " + bottomSheetBehavior.getState());

                        }
                    }
            }
        });


        actBtn = (FloatingActionButton) findViewById(R.id.floatActBtn_cartContent);

        actBtn.setOnClickListener(this);


        FrameLayout mainFrame = (FrameLayout) findViewById(R.id.mainFrameFrag);
        mainFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                bottomSheetBehavior.setState(STATE_HIDDEN);

                return false;
            }
        });

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


}
