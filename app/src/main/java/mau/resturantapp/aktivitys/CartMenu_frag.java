package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewItemToCartEvent;
import mau.resturantapp.events.ShowHideCartEvent;

/**
 * Created by anwar on 10/15/16.
 */

public class CartMenu_frag extends Fragment implements View.OnClickListener {

    private View rod;


    private ImageButton hideShowCartBtn;
    private TextView totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.kurvshowhide_frag, container, false);


        hideShowCartBtn = (ImageButton) rod.findViewById(R.id.skjulVisBut);
        totalPrice = (TextView) rod.findViewById(R.id.totalIndkøbText);

        hideShowCartBtn.setOnClickListener(this);
        EventBus.getDefault().register(this);

        return rod;
    }

    @Override
    public void onClick(View v) {
        if (v == hideShowCartBtn) {

            ShowHideCartEvent event = new ShowHideCartEvent();

            EventBus.getDefault().post(event);


        }

    }

    @Subscribe
    public void newItemEvent(NewItemToCartEvent event) {
        int price = getTotalPrice();
        totalPrice.setText("Total pris: " + price);
    }

    private int getTotalPrice() {
        int totalprice = 0;

        for (int i = 0; i < appData.cartContent.size(); i++) {
            totalprice += appData.cartContent.get(i).getPris();
        }


        return totalprice;
    }
}
