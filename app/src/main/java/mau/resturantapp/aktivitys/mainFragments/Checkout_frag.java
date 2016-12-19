package mau.resturantapp.aktivitys.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import mau.resturantapp.R;
import mau.resturantapp.adapters.Cartcontent_adapter;
import mau.resturantapp.data.appData;
import mau.resturantapp.utils.Firebase.FirebaseWrite;

/**
 * Created by AnwarC on 23/11/2016.
 */

public class Checkout_frag extends Fragment implements View.OnClickListener,Runnable{

    private View root;
    private Button finishBtn;
    private EditText UIcomment;
    private TimePicker UITimepick;
    private LinearLayoutManager manager;
    private RecyclerView finalItems;
    private TextView totalPrice;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.checkout_frag,container,false);

        finishBtn = (Button) root.findViewById(R.id.btn_checkout_sendOrder);
        UIcomment = (EditText) root.findViewById(R.id.editTxt_checkout_UIeTxt);
        UITimepick = (TimePicker) root.findViewById(R.id.timePick_checkout_UItime);
        finalItems = (RecyclerView) root.findViewById(R.id.Relist_checkout_finalItems);
        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        totalPrice = (TextView) root.findViewById(R.id.txt_checkout_totalPrice);
        UITimepick.setIs24HourView(true);
        finalItems.setHasFixedSize(true);
        totalPrice.setText(String.valueOf(appData.totalPrice + "DKK"));


        finishBtn.setOnClickListener(this);
        finalItems.setLayoutManager(manager);


        return root;
    }

    @Override
    public void onClick(View view) {
        if(view == finishBtn){
            finishCheckOut();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appData.priceObservers.add(this);
        run();
    }

    @Override
    public void onStart() {
        super.onStart();
        appData.adapter.startRecyclerViewAdapter();
        finalItems.setAdapter(appData.adapter.getRecyclerViewAdapter());
    }

    private void finishCheckOut(){
        if(shopIsOpen()){
            String comment = UIcomment.getText().toString();
            String timeToPickup = "" + UITimepick.getCurrentHour() + ":" + UITimepick.getCurrentMinute();
            FirebaseWrite.placeOrder(comment, timeToPickup);
        } else {
            Log.w("Checkout" , "Shop is closed");
            appData.event.shopCLosed();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        appData.priceObservers.remove(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        appData.adapter.recyclerViewAdapterCleanUp();
    }

    private boolean shopIsOpen(){
        if(UITimepick.getCurrentHour() > appData.OPENHOUR && UITimepick.getCurrentHour() < appData.CLOSEHOUR){
            return true;
        }
        else if (UITimepick.getCurrentHour() == appData.OPENHOUR && UITimepick.getCurrentMinute() >= appData.OPENMINUT){
            return true;
        }
        else if (UITimepick.getCurrentHour() == appData.CLOSEHOUR && UITimepick.getCurrentMinute() < appData.CLOSEMINUT){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void run() {
        totalPrice.setText(appData.totalPrice);
    }


    private class listAdapter extends ArrayAdapter<String>{
        private int layout;
        public listAdapter(Context context, int resource) {
            super(context, resource);
            layout = resource;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder holder = new ViewHolder();
                holder.item = (TextView) convertView.findViewById(R.id.txt_cartContent_mainItemtext);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.txt_cartContent_priceText);
            }



            return super.getView(position, convertView, parent);
        }
    }

    public class ViewHolder{
        TextView item;
        TextView itemPrice;

    }
}
