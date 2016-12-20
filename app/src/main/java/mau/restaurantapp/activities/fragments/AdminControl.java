package mau.restaurantapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import mau.restaurantapp.R;
import mau.restaurantapp.activities.fragments.adminControls.Accounting;
import mau.restaurantapp.activities.fragments.adminControls.Messages;
import mau.restaurantapp.activities.fragments.adminControls.NewItem;
import mau.restaurantapp.activities.fragments.adminControls.NewMenu;
import mau.restaurantapp.activities.fragments.adminControls.OrderHistory;
import mau.restaurantapp.activities.fragments.adminControls.PendingOrders;
import mau.restaurantapp.activities.fragments.adminControls.ShopSettings;
import mau.restaurantapp.utils.firebase.FirebaseRead;

/**
 * Created by anwar on 10/16/16.
 */

public class AdminControl extends Fragment implements AdapterView.OnItemSelectedListener {
    private View rod;
    private Spinner menu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.admcontrol_frag, container, false);

        menu = (Spinner) rod.findViewById(R.id.spin_control_menu);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.admincontrols, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        menu.setAdapter(adapter);

        menu.setOnItemSelectedListener(this);

        // Orders/product fix (used for Accounting)
        FirebaseRead.getAllProducts();
        FirebaseRead.getAllOrders();

        return rod;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment frag;

        /*


        rækkefølgen lige nu, vidste ikke helt lige hvordan man kan finde ud af hvilke det er
        man trykker på ud fra <item> tag. finder ud af det en anden gang.

            Add new menu
            Add new item
            Accounting
            Pending orders
            Order history
            Messages
            Shop settings
         */

        switch (i) {
            case 0: {
                frag = new NewMenu();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();


                break;
            }

            case 1: {
                frag = new NewItem();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();


                break;
            }

            case 2: {
                frag = new Accounting();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }


            case 3: {
                frag = new PendingOrders();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }

            case 4: {
                frag = new OrderHistory();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }

            case 5: {
                frag = new Messages();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }
            case 6: {
                frag = new ShopSettings();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }
            default: {
                break;
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
