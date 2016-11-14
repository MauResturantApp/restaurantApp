package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewItemToCartEvent;
import mau.resturantapp.events.ShowHideCartEvent;

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
    private FirebaseRecyclerAdapter<Product, CartContentHolder> recyclerViewAdapter;

    private FirebaseAuth.AuthStateListener mAuthListener;

    public static class CartContentHolder extends RecyclerView.ViewHolder{

        View rod;
        ImageView ImgIcon;
        ImageButton ImgBtn;
        TextView Itemtxt;

        public CartContentHolder(View rod){
            super(rod);
            this.rod = rod;

            ImgBtn = (ImageButton) rod.findViewById(R.id.imgBtn_cartContent_removeItem);
        }

        public void setItemtxt(String name, int price){
            Itemtxt = (TextView) rod.findViewById(R.id.txt_cartContent_mainItemtext);
            Itemtxt.setText(name + " x 1   " + price);
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
    public void onShowHideEvent(ShowHideCartEvent event) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_down_out1);
        Fragment fragmentCartContent = fragmentManager.findFragmentById(R.id.cartContentShowHide_frag);
        if (fragmentCartContent.isHidden()) {
            transaction.show(fragmentCartContent).commit();

        } else {
            transaction.hide(fragmentCartContent).commit();

        }

        Log.d("showhideevent", "kaldt");
    }

    @Override
    public void onStart() {
        super.onStart();

        startAuthStateListener();
        appData.firebaseAuth.addAuthStateListener(mAuthListener);

        startRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();

        appData.firebaseAuth.removeAuthStateListener(mAuthListener);

        if(recyclerViewAdapter != null){
            recyclerViewAdapter.cleanup();
        }
    }

    private void startRecyclerViewAdapter() {
        Log.d("recyclerViewAdapter", ref.toString());
        Query query = ref;
        recyclerViewAdapter = new FirebaseRecyclerAdapter<Product, CartContentHolder>(
                Product.class, R.layout.kurv_list, CartContentHolder.class, query) {

            @Override
            protected void populateViewHolder(CartContentHolder CartContentHolder, Product product, int position) {
                Log.d("recyclerViewAdapter", ""+position);
                final int mPosition = position;
                CartContentHolder.setItemtxt(product.getName(), product.getPrice());
                CartContentHolder.ImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove from cart representation in firebase
                        ref.child(mPosition+"").removeValue();
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
                    // User is signed in
                    ref = appData.firebaseDatabase.getReference("Shoppingcart/" + appData.getUID());
                    Log.d("Authstate", "onAuthStateChanged:signed_in:" + user.getUid());
                    if(user.isAnonymous()){
                        //Save logged in as anonymous Reference to later transfer data
                        //appData.anonymousAuth = appData.firebaseAuth;
                    } else {
                        // User is logged in as a known user
                        //appData.transferAnonymousData();
                    }
                } else {
                    // User is signed out
                    appData.firebaseAuth.signInAnonymously()
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Authstate", "signInAnonymously:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Log.w("AuthState", "signInAnonymously", task.getException());
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        };

    }
}
