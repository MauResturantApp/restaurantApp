package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

import mau.resturantapp.R;
import mau.resturantapp.events.ShowHideCartEvent;

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
        if (v == hideShowCartBtn) {

            ShowHideCartEvent event = new ShowHideCartEvent();

            EventBus.getDefault().post(event);
        }

    }
}
