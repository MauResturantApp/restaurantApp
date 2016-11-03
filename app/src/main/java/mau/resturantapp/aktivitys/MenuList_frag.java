package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewItemToCartEvent;

/**
 * Created by anwar on 10/17/16.
 */

public class MenuList_frag extends Fragment {
    public static final String argPage = "Arg_Page";

    private int pageNumber;
    private ListView menuList;

    public static MenuList_frag newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(argPage, page);
        MenuList_frag frag = new MenuList_frag();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(argPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        menuList = null;
        pageNumber = 1;

    }

    @Override
    public void onPause() {
        super.onPause();
        pageNumber = 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.menu1_frag, container, false);

        menuList = (ListView) rod.findViewById(R.id.menu_ViewPagerContent);
        ArrayList<MenuItem> tempMenu = new ArrayList<>();
        switch (pageNumber) {
            case 1:
                tempMenu = appData.menu1;
                break;
            case 2:
                tempMenu = appData.menu2;
                break;
            case 3:
                tempMenu = appData.menu3;
                break;
            case 4:
                tempMenu = appData.menu4;
                break;
            case 5:
                tempMenu = appData.menu5;
                break;

        }

        menuList.setAdapter(new FoodMenuAdapter(getContext(), R.layout.menu_item_list, tempMenu));


        return rod;
    }

    public class FoodMenuAdapter extends ArrayAdapter<MenuItem> {
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
                        appData.cartContent.add(tempVare.get(position));
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


    }
}
