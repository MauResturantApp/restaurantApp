package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import mau.resturantapp.R;

/**
 * Created by anwar on 10/15/16.
 */

public class CartMenu_frag extends Fragment implements View.OnClickListener {

    private View rod;


    private ImageButton hideShowCartBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.kurvshowhide_frag, container, false);


        hideShowCartBtn = (ImageButton) rod.findViewById(R.id.skjulVisBut);

        hideShowCartBtn.setOnClickListener(this);


        return rod;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_down_out1);
        Fragment fragmentCartContent = fragmentManager.findFragmentById(R.id.cartContentShowHide_frag);
        if (v == hideShowCartBtn) {

            if (fragmentCartContent.isHidden()) {
                transaction.show(fragmentCartContent).commit();

            } else {
                transaction.hide(fragmentCartContent).commit();

            }
        }

    }
}
