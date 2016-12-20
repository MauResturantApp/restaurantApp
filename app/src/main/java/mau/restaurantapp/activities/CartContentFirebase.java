package mau.restaurantapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.restaurantapp.R;
import mau.restaurantapp.data.AppData;
import mau.restaurantapp.event.events.OnSuccesfullLogInEvent;
import mau.restaurantapp.event.events.SignOutEvent;


public class CartContentFirebase extends Fragment implements Runnable {

    private RecyclerView cartContent;
    private LinearLayoutManager manager;
    private TextView totalPriceTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.shoppingcart, container, false);

        cartContent = (RecyclerView) rod.findViewById(R.id.kurv_listview);
        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        cartContent.setHasFixedSize(false);
        cartContent.setLayoutManager(manager);
        totalPriceTxt = (TextView) rod.findViewById(R.id.txt_cart_total);

        return rod;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void OnSuccesfullLoginEvent(OnSuccesfullLogInEvent event) {
        AppData.adapter.recyclerViewAdapterCleanUp();
        AppData.adapter.startRecyclerViewAdapter();
    }

    @Subscribe
    public void SignOutEvent(SignOutEvent event) {
        AppData.adapter.recyclerViewAdapterCleanUp();
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

        if (AppData.firebaseAuth.getCurrentUser() != null) {
            AppData.adapter.setRef(AppData.firebaseDatabase.getReference("shoppingcart/" + AppData.firebaseAuth.getCurrentUser().getUid())
            );
            AppData.adapter.startAuthStateListener();
            AppData.firebaseAuth.addAuthStateListener(AppData.adapter.getmAuthListener());

            AppData.adapter.startRecyclerViewAdapter();

            cartContent.setAdapter(AppData.adapter.getRecyclerViewAdapter());
        }
    }

    @Override
    public void onPause() {
        AppData.priceObservers.remove(this);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        AppData.firebaseAuth.removeAuthStateListener(AppData.adapter.getmAuthListener());

        AppData.adapter.recyclerViewAdapterCleanUp();
    }

    @Override
    public void run() {
        totalPriceTxt.setText("Totalprice: " + AppData.totalPrice);
    }
}
