package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mainTollbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hovedakt_akt);
        generateItems();
        mainTollbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainTollbar);
        EventBus.getDefault().register(this);
        getSupportActionBar().setTitle(null);
        showHomeScreen();
        hideCart();
        final FrameLayout mainFrame = (FrameLayout) findViewById(R.id.mainFrameLayout);
        mainFrame.setOnTouchListener(new FrameLayout.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("hej","hej");
                    hideCart();

                   return false;

            }
        });
    }


    @Override
    public void onClick(View v) {

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

    public void generateItems() {

        appData.menu1.add(new MenuItem(1, "banan", 23, "MENU1"));
        appData.menu1.add(new MenuItem(2, "apelsin", 23, "MENU1"));
        appData.menu1.add(new MenuItem(3, "ananas", 23, "MENU1"));
        appData.menu1.add(new MenuItem(4, "mango", 23, "MENU1"));
        appData.menu2.add(new MenuItem(5, "kød", 23, "MENU2"));
        appData.menu2.add(new MenuItem(6, "kylling", 23, "MENU2"));
        appData.menu2.add(new MenuItem(7, "fisk", 23, "MENU2"));
        appData.menu2.add(new MenuItem(8, "lam", 23, "MENU2"));
        appData.menu3.add(new MenuItem(9, "cola", 23, "MENU3"));
        appData.menu3.add(new MenuItem(10, "pepsi", 23, "MENU3"));
        appData.menu3.add(new MenuItem(11, "sprite", 23, "MENU3"));
        appData.menu3.add(new MenuItem(12, "fanta", 23, "MENU3"));
        appData.menu3.add(new MenuItem(13, "faxi kondi", 23, "MENU3"));
        appData.menu4.add(new MenuItem(14, "kage", 23, "MENU4"));
        appData.menu4.add(new MenuItem(15, "chokoladekage", 23, "MENU4"));
        appData.menu5.add(new MenuItem(16, "chokois", 23, "MENU5"));
        appData.menu5.add(new MenuItem(17, "jodbæris", 23, "MENU5"));
        appData.menu5.add(new MenuItem(18, "vanilieis", 23, "MENU5"));
        appData.menu5.add(new MenuItem(19, "øl", 23, "MENU3"));

    }
}
