package mau.restaurantapp.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import mau.restaurantapp.R;
import mau.restaurantapp.data.AppData;
import mau.restaurantapp.utils.firebase.FirebaseWrite;

/**
 * Created by AnwarC on 23/11/2016.
 */

public class Checkout extends Fragment implements View.OnClickListener, Runnable {

    private View root;
    private Button finishBtn;
    private EditText UIcomment;
    private TimePicker UITimepick;
    private LinearLayoutManager manager;
    private RecyclerView finalItems;
    private TextView totalPrice;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.checkout, container, false);

        finishBtn = (Button) root.findViewById(R.id.btn_checkout_sendOrder);
        UIcomment = (EditText) root.findViewById(R.id.editTxt_checkout_UIeTxt);
        UITimepick = (TimePicker) root.findViewById(R.id.timePick_checkout_UItime);
        finalItems = (RecyclerView) root.findViewById(R.id.Relist_checkout_finalItems);
        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        totalPrice = (TextView) root.findViewById(R.id.txt_checkout_totalPrice);
        UITimepick.setIs24HourView(true);
        finalItems.setHasFixedSize(true);
        totalPrice.setText(String.valueOf(AppData.totalPrice + "DKK"));


        finishBtn.setOnClickListener(this);
        finalItems.setLayoutManager(manager);


        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == finishBtn) {
            finishCheckOut();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppData.priceObservers.add(this);
        run();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppData.adapter.startRecyclerViewAdapter();
        finalItems.setAdapter(AppData.adapter.getRecyclerViewAdapter());
    }

    private void finishCheckOut() {
        if (shopIsOpen()) {
            String comment = UIcomment.getText().toString();
            String timeToPickup = "" + UITimepick.getCurrentHour() + ":" + UITimepick.getCurrentMinute();
            FirebaseWrite.placeOrder(comment, timeToPickup);
        } else {
            Log.w("Checkout", "Shop is closed");
            AppData.event.shopCLosed();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        AppData.priceObservers.remove(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppData.adapter.recyclerViewAdapterCleanUp();
    }

    private boolean shopIsOpen() {
        if (UITimepick.getCurrentHour() > AppData.OPENHOUR && UITimepick.getCurrentHour() < AppData.CLOSEHOUR) {
            return true;
        } else if (UITimepick.getCurrentHour() == AppData.OPENHOUR && UITimepick.getCurrentMinute() >= AppData.OPENMINUT) {
            return true;
        } else if (UITimepick.getCurrentHour() == AppData.CLOSEHOUR && UITimepick.getCurrentMinute() < AppData.CLOSEMINUT) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        totalPrice.setText(String.valueOf(AppData.totalPrice));
    }


    private class listAdapter extends ArrayAdapter<String> {
        private int layout;

        public listAdapter(Context context, int resource) {
            super(context, resource);
            layout = resource;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.item = (TextView) convertView.findViewById(R.id.txt_cartContent_mainItemtext);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.txt_cartContent_priceText);
            }


            return super.getView(position, convertView, parent);
        }
    }

    public class ViewHolder {
        TextView item;
        TextView itemPrice;

    }
}
