package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.Product;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewItemToCartEvent;
import mau.resturantapp.events.ShowHideCartEvent;

/**
 * Created by anwar on 10/14/16.
 */

public class CartContent_frag extends Fragment {

    private View view;
    private RecyclerView recycler;
    private CartItemList_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.kurv_frag, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.kurv_listview);
        adapter = new CartItemList_Adapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        return view;
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


    @Subscribe
    public void newItemToCartEvent(NewItemToCartEvent event) {
        adapter.notifyDataSetChanged();

        Log.d("newitemevent", "kaldt");
    }

    public class CartItemList_Adapter extends RecyclerView.Adapter<CartItemList_Adapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView listImgIcon;
            ImageButton listImgBtn;
            TextView listItemtxt;

            public ViewHolder(View itemView) {
                super(itemView);
                listImgIcon = (ImageView) itemView.findViewById(R.id.icon_cartContent_itemIcon);
                listImgBtn = (ImageButton) itemView.findViewById(R.id.imgBtn_cartContent_removeItem);
                listItemtxt = (TextView) itemView.findViewById(R.id.txt_cartContent_mainItemtext);

                listImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appData.cartContent.remove(getAdapterPosition());
                        NewItemToCartEvent event = new NewItemToCartEvent();
                        EventBus.getDefault().post(event);
                        notifyItemRemoved(getAdapterPosition());
                    }
                });

            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.kurv_list, parent, false);
            return new ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(CartItemList_Adapter.ViewHolder holder, int position) {
            Product tempMenuItem = appData.cartContent.get(position);
            holder.listItemtxt.setText(tempMenuItem.getName() + " x 1   " + tempMenuItem.getPrice());
        }

        @Override
        public int getItemCount() {
            return appData.cartContent.size();
        }

    }


}
