package mau.restaurantapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import mau.restaurantapp.R;
import mau.restaurantapp.data.AppData;
import mau.restaurantapp.data.types.ShoppingCartItem;
import mau.restaurantapp.utils.firebase.FirebaseAuthentication;
import mau.restaurantapp.utils.firebase.FirebaseRead;
import mau.restaurantapp.utils.firebase.FirebaseWrite;

/**
 * Created by AnwarC on 02/12/2016.
 */

public class CartContent {
    private DatabaseReference ref;
    private FirebaseRecyclerAdapter<ShoppingCartItem, CartContentHolder> recyclerViewAdapter;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private List<CartContentHolder> holderList = new ArrayList<CartContentHolder>() {
    };


    public CartContent() {
    }

    public void recyclerViewAdapterCleanUp() {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.cleanup();
        }
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }

    public void startRecyclerViewAdapter() {
        Query query = ref;
        recyclerViewAdapter = new FirebaseRecyclerAdapter<ShoppingCartItem, CartContentHolder>(
                ShoppingCartItem.class, R.layout.shoppingcart_list, CartContentHolder.class, query) {

            @Override
            protected void populateViewHolder(CartContentHolder CartContentHolder, final ShoppingCartItem shoppingCartItem, int position) {
                CartContentHolder.setItemtxt(shoppingCartItem.getName(), shoppingCartItem.getPrice());
                holderList.add(CartContentHolder);
                if (position % 2 != 0) {
                    CartContentHolder.listItemtxt.setBackgroundResource(R.color.colorSecondary);
                    CartContentHolder.relativeLayout.setBackgroundResource(R.color.colorSecondary);
                }
                CartContentHolder.listImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove from cart representation in firebase
                        FirebaseWrite.removeProductFromCart(shoppingCartItem.getKey());
                        AppData.setNewPrice(AppData.totalPrice - shoppingCartItem.getPrice());
                    }
                });
            }
        };

        recyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        });
    }

    public List<CartContentHolder> getHolderList() {
        return holderList;
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public void startAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Current user", "" + user.getUid());
                    // User is signed in
                    ref = AppData.firebaseDatabase.getReference("shoppingcart/" + user.getUid() /*AppData.getUID()*/);
                    FirebaseRead.getCartContent();
                    Log.d("Authstate", "onAuthStateChanged:signed_in:" + user.getUid());
                    if (user.isAnonymous()) {
                        //Save logged in as anonymous Reference to later transfer data
                        AppData.loggingIn = false;
                    } else {
                        // User is logged in as a known user
                        AppData.loggingIn = false;
                    }
                } else {
                    Log.d("Current user", "Null");
                    recyclerViewAdapterCleanUp();
                    if (!AppData.loggingIn) {
                        // User is signed out
                        AppData.loggingIn = true;
                        FirebaseAuthentication.logInAnonymously();
                    }
                }
            }
        };
    }


    public FirebaseRecyclerAdapter<ShoppingCartItem, CartContentHolder> getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    private static class CartContentHolder extends RecyclerView.ViewHolder {

        View rod;
        private ImageButton listImgBtn;
        private TextView listItemtxt;
        private RelativeLayout relativeLayout;

        public CartContentHolder(View rod) {
            super(rod);
            this.rod = rod;

            listImgBtn = (ImageButton) rod.findViewById(R.id.imgBtn_cartContent_removeItem);
            relativeLayout = (RelativeLayout) rod.findViewById(R.id.relativLayout_cartContent);
        }

        public void setItemtxt(String name, int price) {
            listItemtxt = (TextView) rod.findViewById(R.id.txt_cartContent_mainItemtext);
            listItemtxt.setText(name + " x 1   " + price);
        }
    }
}
