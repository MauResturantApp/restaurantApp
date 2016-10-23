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

import java.util.ArrayList;
import java.util.List;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;

/**
 * Created by anwar on 10/17/16.
 */

public class MenuList_frag extends Fragment {
    public static final String argPage = "Arg_Page";

    private int pageNumber;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.menu1_frag, container, false);

        ListView menuList = (ListView) rod.findViewById(R.id.menu_ViewPagerContent);
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

        menuList.setAdapter(new FoodMenuAdapter(getContext(), R.layout.kurv_list, tempMenu));


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
                viewer.imgview = (ImageView) convertView.findViewById(R.id.kurv_list_icon);
                viewer.addBut = (ImageButton) convertView.findViewById(R.id.fjern_varer_button);
                viewer.hovedText = (TextView) convertView.findViewById(R.id.vare_id_kurv_text);
                viewer.ekstraText = (TextView) convertView.findViewById(R.id.vare_id_ekstraText);
                viewer.addBut.setImageResource(R.drawable.add_item_icon);
                viewer.hovedText.setText(tempVare.get(position).getNavn() + "" + tempVare.get(position).getPris() + "DKK");
                convertView.setTag(viewer);
                viewer.addBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appData.cartContent.add(tempVare.get(position));
                        Toast.makeText(getContext(), tempVare.get(position).getNavn() + "er tilføget til kurv", Toast.LENGTH_SHORT).show();

                    }
                });


            } else {
                mainViewH = (ViewHolder) convertView.getTag();
                mainViewH.hovedText.setText(tempVare.get(position).getNavn() + "" + tempVare.get(position).getPris() + "DKK");
            }


            return convertView;
        }

        private class ViewHolder {
            ImageView imgview;
            TextView hovedText;
            TextView ekstraText;
            ImageButton addBut;


        }
    }
}
