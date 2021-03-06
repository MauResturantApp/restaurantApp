package mau.restaurantapp.activities.fragments.menuFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mau.restaurantapp.R;
import mau.restaurantapp.data.AppData;

import mau.restaurantapp.data.types.Product;
import mau.restaurantapp.utils.firebase.FirebaseWrite;

/**
 * Created by anwar on 10/17/16.
 */

public class MenuList extends Fragment {
    private static final String argPage = "Arg_Page";
    private static final String argPageTitle = "Arg_PageTitle";
    private static final String argTabKey = "Arg_TabKey";

    private int pageNumber;
    private String pageTitle;
    private String key;

    private DatabaseReference ref;

    private RecyclerView products;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Product, ProductHolder> recyclerViewAdapter;

    public static class ProductHolder extends RecyclerView.ViewHolder {

        View rod;
        ImageView itemIcon;
        TextView mainItemTxt;
        TextView extraItemTxt;
        ImageButton addNewItemBtn;
        ImageButton plusItemBtn;
        ImageButton minusItemBtn;
        TextView amountTxt;
        int amount = 1;
        FrameLayout multipleItemLayout;
        Button addMultiBtn;

        public ProductHolder(View rod) {
            super(rod);
            this.rod = rod;

        }

        public void setProductName(String name) {
            mainItemTxt = (TextView) rod.findViewById(R.id.txt_menu_mainItemtext);
            mainItemTxt.setText(name);
        }

        public void setPrice(int price) {
            extraItemTxt = (TextView) rod.findViewById(R.id.txt_menu_extraItemText);
            extraItemTxt.setText(Integer.toString(price));
        }

        public void setItemIcon() {
            itemIcon = (ImageView) rod.findViewById(R.id.icon_menu_itemIcon);
        }

        public void setImageButton(final int position) {
            //Log.d("recyclerViewAdapter", "imagebutton");
            addNewItemBtn = (ImageButton) rod.findViewById(R.id.imgBtn_menu_removeItem);
        }

        public void setmultipleLayout() {
            plusItemBtn = (ImageButton) rod.findViewById(R.id.imgBtn_menuList_plus);
            minusItemBtn = (ImageButton) rod.findViewById(R.id.imgBtn_menuList_minus);
            multipleItemLayout = (FrameLayout) rod.findViewById(R.id.frameL_multipleItems);
            amountTxt = (TextView) rod.findViewById(R.id.txt_menuList_amount);
            addMultiBtn = (Button) rod.findViewById(R.id.btn_menuList_addMulItems);
        }
    }

    public static MenuList newInstance(int page, String pageTitle, String key) {
        Bundle args = new Bundle();
        args.putInt(argPage, page);
        args.putString(argPageTitle, pageTitle);
        args.putString(argTabKey, key);
        Log.d("recyclerViewAdapter", "imagebutton" + page);
        MenuList frag = new MenuList();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(argPage);
        pageTitle = getArguments().getString(argPageTitle);
        key = getArguments().getString(argTabKey);
        Log.d("oncreate", "pagenumber" + pageNumber);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("start", "pagenumber" + pageNumber);

        startRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("stop", "pagenumber" + pageNumber);

        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.cleanup();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("pause", "pagenumber" + pageNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("destroy", "pagenumber" + pageNumber);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.tabcontent_recyclerlist, container, false);
        Log.d("creater inhold", "page number: " + pageNumber);
        products = (RecyclerView) rod.findViewById(R.id.recyclerview_tabcontent);
        manager = new LinearLayoutManager(getActivity().getApplicationContext());

        products.setHasFixedSize(false); //Test forskel
        products.setLayoutManager(manager);

        ref = AppData.firebaseDatabase.getReference("product/" + key);

        products.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int totalItems = products.getAdapter().getItemCount();

                for (int i = 0; i < totalItems; i++) {
                    try {
                        ProductHolder tempholder = (ProductHolder) products.findViewHolderForLayoutPosition(i);
                        tempholder.multipleItemLayout.setAlpha(0f);
                    } catch (NullPointerException notAvailableYet) {

                    }

                }
                return false;
            }
        });

        //menuList.setAdapter(new FoodMenuAdapter(getContext(), R.layout.menu_item_list, tempMenu));


        return rod;
    }

    private void succesfullAddItem(final ProductHolder holder, final int position) {
        holder.addNewItemBtn.setImageResource(R.drawable.add_item_icon);
        holder.addNewItemBtn.setOnClickListener(null);

        class lockOnClick extends AsyncTask {

            @Override
            protected Object doInBackground(Object[] objects) {
                SystemClock.sleep(750);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                holder.addNewItemBtn.setImageResource(R.drawable.ic_action_additem);
                holder.addNewItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProduct(position, holder);
                    }
                });

            }
        }
        new lockOnClick().execute();

    }

    private void changeAmount(int btn, ProductHolder productHolder) {
        if (btn == 1) {
            if (productHolder.amount < 99) {
                productHolder.amount++;
                productHolder.amountTxt.setText(Integer.toString(productHolder.amount));
            }

        } else if (btn == 0) {
            if (productHolder.amount > 0) {

                productHolder.amount--;
                productHolder.amountTxt.setText(Integer.toString(productHolder.amount));

            }
        }

    }


    private void startRecyclerViewAdapter() {
        Log.d("recyclerViewAdapter", ref.toString());
        Query query = ref;
        recyclerViewAdapter = new FirebaseRecyclerAdapter<Product, ProductHolder>(
                Product.class, R.layout.menu_item_list, ProductHolder.class, query) {

            @Override
            protected void populateViewHolder(final ProductHolder productHolder, final Product product, final int position) {
                //Log.d("recyclerViewAdapter", ""+position);
                final int mPosition = position;
                productHolder.setProductName(product.getName());
                productHolder.setPrice(product.getPrice());
                productHolder.setItemIcon();
                productHolder.setImageButton(mPosition);
                productHolder.setmultipleLayout();
                productHolder.addNewItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProduct(position, productHolder);
                    }
                });

                productHolder.plusItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeAmount(1, productHolder);
                    }
                });

                productHolder.minusItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeAmount(0, productHolder);
                    }
                });

                productHolder.addMultiBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < productHolder.amount; i++) {
                            FirebaseWrite.addProductToCart(product);
                            productHolder.multipleItemLayout.setAlpha(0f);
                        }
                    }
                });

                productHolder.addNewItemBtn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        productHolder.amount = 1;
                        String amount = Integer.toString(productHolder.amount);
                        productHolder.amountTxt.setText(amount);
                        productHolder.multipleItemLayout.animate().setDuration(300).alpha(1f);

                        return true;
                    }
                });
            }
        };

        recyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        });

        products.setAdapter(recyclerViewAdapter);


    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    private void addProduct(int position, ProductHolder holder) {
        Log.d("PRICE!!!!", "PRIIIIIIIIIIIIIIIICE" + recyclerViewAdapter.getItem(position).getPrice());
        AppData.setNewPrice(AppData.totalPrice + recyclerViewAdapter.getItem(position).getPrice());
        FirebaseWrite.addProductToCart(recyclerViewAdapter.getItem(position));
        Toast.makeText(getContext(), recyclerViewAdapter.getItem(position).getName() + "er tilføget til kurv", Toast.LENGTH_SHORT).show();
        AppData.event.newItemToCart();
        succesfullAddItem(holder, position);
    }
/*    public class FoodMenuAdapter extends ArrayAdapter<MenuItem> {
        private int rod;
        private List<MenuItem> tempVare;


        public FoodMenuAdapter(Context context, int resource, List<MenuItem> objects) {
            super(context, resource, objects);
            tempVare = objects;
            rod = resource;

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewH = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(rod, parent, false);
                ViewHolder viewer = new ViewHolder();
                viewer.itemIcon = (ImageView) convertView.findViewById(R.id.icon_menu_itemIcon);
                viewer.addNewItemBtn = (ImageButton) convertView.findViewById(R.id.imgBtn_menu_removeItem);
                viewer.mainItemTxt = (TextView) convertView.findViewById(R.id.txt_menu_mainItemtext);
                viewer.extraItemTxt = (TextView) convertView.findViewById(R.id.txt_menu_extraItemText);
                viewer.addNewItemBtn.setImageResource(R.drawable.add_item_icon);
                viewer.mainItemTxt.setText(tempVare.get(position).getNavn() + "" + tempVare.get(position).getPris() + "DKK");
                convertView.setTag(viewer);
                viewer.addNewItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppData.cartContent.add(tempVare.get(position));
                        Toast.makeText(getContext(), tempVare.get(position).getNavn() + "er tilføget til kurv", Toast.LENGTH_SHORT).show();
                        NewItemToCartEvent event = new NewItemToCartEvent();
                        EventBus.getDefault().post(event);

                    }
                });


            } else {
                mainViewH = (ViewHolder) convertView.getTag();
                mainViewH.mainItemTxt.setText(tempVare.get(position).getNavn() + "" + tempVare.get(position).getPris() + "DKK");
            }


            return convertView;
        }

        private class ViewHolder {
            ImageView itemIcon;
            TextView mainItemTxt;
            TextView extraItemTxt;
            ImageButton addNewItemBtn;


        }


    }*/
}
