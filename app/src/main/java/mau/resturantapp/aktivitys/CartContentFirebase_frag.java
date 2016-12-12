package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.adapters.Cartcontent_adapter;
import mau.resturantapp.data.Product;
import mau.resturantapp.data.ShoppingCartItem;
import mau.resturantapp.data.appData;
import mau.resturantapp.event.events.OnSuccesfullLogInEvent;
import mau.resturantapp.event.events.SignOutEvent;

import static mau.resturantapp.data.appData.event;
import static mau.resturantapp.data.appData.firebaseDatabase;
import static mau.resturantapp.data.appData.loggingIn;


public class CartContentFirebase_frag extends Fragment implements Runnable {

    private RecyclerView cartContent;
    private LinearLayoutManager manager;
    private TextView totalPriceTxt;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.kurv_frag, container, false);

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
        appData.adapter.recyclerViewAdapterCleanUp();
        appData.adapter.startRecyclerViewAdapter();
    }

    @Subscribe
    public void SignOutEvent(SignOutEvent event) {
        appData.adapter.recyclerViewAdapterCleanUp();
    }


    @Override
    public void onStart() {
        super.onStart();

        appData.priceObservers.add(this);

        appData.adapter.setRef(appData.firebaseDatabase.getReference("shoppingcart/" + appData.getUID())
        );
        appData.adapter.startAuthStateListener();
        appData.firebaseAuth.addAuthStateListener(appData.adapter.getmAuthListener());

        appData.adapter.startRecyclerViewAdapter();

        cartContent.setAdapter(appData.adapter.getRecyclerViewAdapter());

    }

    @Override
    public void onStop() {
        super.onStop();

        appData.priceObservers.remove(this);

        appData.firebaseAuth.removeAuthStateListener(appData.adapter.getmAuthListener());

        appData.adapter.recyclerViewAdapterCleanUp();

    }


    @Override
    public void run() {
        totalPriceTxt.setText("Totalprice :" + appData.totalPrice);
    }
}
