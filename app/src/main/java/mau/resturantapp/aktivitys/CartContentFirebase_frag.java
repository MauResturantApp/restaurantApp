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
import mau.resturantapp.data.Product;
import mau.resturantapp.data.ShoppingCartItem;
import mau.resturantapp.data.appData;
import mau.resturantapp.event.events.OnSuccesfullLogInEvent;
import mau.resturantapp.event.events.SignOutEvent;

import static mau.resturantapp.data.appData.event;
import static mau.resturantapp.data.appData.firebaseDatabase;
import static mau.resturantapp.data.appData.loggingIn;

/**
 *  Work in progress
 *
 *  Should handle displaying and removing items from the cart in firebase
 *
 *  Currently it would only work if logged in - Handling anonymous users should be implemented
 *
 */

public class CartContentFirebase_frag extends Fragment {

    private DatabaseReference ref;
    private RecyclerView cartContent;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<ShoppingCartItem, CartContentHolder> recyclerViewAdapter;

    private FirebaseAuth.AuthStateListener mAuthListener;

    public static class CartContentHolder extends RecyclerView.ViewHolder{

        View rod;
        ImageButton listImgBtn;
        TextView listItemtxt;
        RelativeLayout relativeLayout;

        public CartContentHolder(View rod){
            super(rod);
            this.rod = rod;

            listImgBtn = (ImageButton) rod.findViewById(R.id.imgBtn_cartContent_removeItem);
            relativeLayout = (RelativeLayout) rod.findViewById(R.id.relativLayout_cartContent);
        }

        public void setItemtxt(String name, int price){
            listItemtxt = (TextView) rod.findViewById(R.id.txt_cartContent_mainItemtext);
            listItemtxt.setText(name + " x 1   " + price);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.kurv_frag, container, false);

        cartContent = (RecyclerView) rod.findViewById(R.id.kurv_listview);
        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        cartContent.setHasFixedSize(false);
        cartContent.setLayoutManager(manager);

        return rod;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void OnSuccesfullLoginEvent(OnSuccesfullLogInEvent event) {
        recyclerViewAdapterCleanUp();
        startRecyclerViewAdapter();
    }

    @Subscribe
    public void SignOutEvent(SignOutEvent event) {
        recyclerViewAdapterCleanUp();
    }


    @Override
    public void onStart() {
        super.onStart();

        ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.getUID());
        Log.d("CartContent on start", ""+ref);
        startAuthStateListener();
        appData.firebaseAuth.addAuthStateListener(mAuthListener);

        startRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();

        appData.firebaseAuth.removeAuthStateListener(mAuthListener);

        recyclerViewAdapterCleanUp();

    }

    private void recyclerViewAdapterCleanUp() {
        if(recyclerViewAdapter != null){
            recyclerViewAdapter.cleanup();
        }
    }

    private void startRecyclerViewAdapter() {
        Log.d("recyclerViewAdapter", ""+ref);
        Query query = ref;
        recyclerViewAdapter = new FirebaseRecyclerAdapter<ShoppingCartItem, CartContentHolder>(
                ShoppingCartItem.class, R.layout.kurv_list, CartContentHolder.class, query) {

            @Override
            protected void populateViewHolder(CartContentHolder CartContentHolder, final ShoppingCartItem shoppingCartItem, int position) {
                CartContentHolder.setItemtxt(shoppingCartItem.getName(), shoppingCartItem.getPrice());
                if(position%2 != 0){
                    CartContentHolder.listItemtxt.setBackgroundResource(R.color.colorSecondary);
                    CartContentHolder.relativeLayout.setBackgroundResource(R.color.colorSecondary);
                }
                CartContentHolder.listImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove from cart representation in firebase
                        appData.removeProductFromCart(shoppingCartItem.getKey());
                    }
                });
            }
        };

        recyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        });

        cartContent.setAdapter(recyclerViewAdapter);
    }

    private void startAuthStateListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Current user", ""+user.getUid());
                    // User is signed in
                    ref = appData.firebaseDatabase.getReference("shoppingcart/" + user.getUid() /*appData.getUID()*/);
                    Log.d("Authstate", "onAuthStateChanged:signed_in:" + user.getUid());
                    if(user.isAnonymous()){
                        //Save logged in as anonymous Reference to later transfer data
                    } else {
                        // User is logged in as a known user
                        appData.loggingIn = false;
                    }
                } else {
                    Log.d("Current user", "Null");
                    recyclerViewAdapterCleanUp();
                    if(!appData.loggingIn) {
                        // User is signed out
                        appData.logInAnonymously();
                    }
                }
            }
        };

    }
}
