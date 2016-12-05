package mau.resturantapp.aktivitys.mainFragments;

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

import mau.resturantapp.R;
import mau.resturantapp.aktivitys.mainFragments.adminControls.Accounting_frag;
import mau.resturantapp.aktivitys.mainFragments.adminControls.Messages_frag;
import mau.resturantapp.aktivitys.mainFragments.adminControls.NewItem_frag;
import mau.resturantapp.aktivitys.mainFragments.adminControls.NewMenu_frag;
import mau.resturantapp.aktivitys.mainFragments.adminControls.OrderHistory_frag;
import mau.resturantapp.aktivitys.mainFragments.adminControls.PendingOrders_frag;
import mau.resturantapp.aktivitys.mainFragments.adminControls.ShopSettings_frag;

/**
 * Created by anwar on 10/16/16.
 */

public class Admincontrol_frag extends Fragment implements AdapterView.OnItemSelectedListener {
    private View rod;
    private Spinner menu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.admincontrol_frag, container, false);

        menu = (Spinner) rod.findViewById(R.id.spin_control_menu);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.admincontrols,android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        menu.setAdapter(adapter);

        menu.setOnItemSelectedListener(this);


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
                frag = new NewMenu_frag();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();


                break;
            }

            case 1: {
                frag = new NewItem_frag();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();


                break;
            }

            case 2: {
                frag = new Accounting_frag();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }


            case 3: {
                frag = new PendingOrders_frag();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }

            case 4: {
                frag = new OrderHistory_frag();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }

            case 5: {
                frag = new Messages_frag();
                ft.replace(R.id.frag_placeholder_adminmenu, frag).commit();

                break;
            }
            case 6: {
                frag = new ShopSettings_frag();
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
