package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mainTollbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hovedakt_akt);
        generateItems();
        mainTollbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainTollbar);
        getSupportActionBar().setTitle(null);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int menuSelect = item.getItemId();

        if (menuSelect == R.id.menu_foodmenu) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment frag = new MenuTabs_frag();
            ft.replace(R.id.mainFrameFrag, frag).commit();
        }
        if (menuSelect == R.id.menu_login) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment frag = new Login_frag();
            ft.replace(R.id.mainFrameFrag, frag).commit();
        }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_items_menu, menu);
        return true;
    }

    public void hidebar() {

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
