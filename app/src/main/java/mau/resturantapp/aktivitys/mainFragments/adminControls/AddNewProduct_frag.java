package mau.resturantapp.aktivitys.mainFragments.adminControls;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuTab;
import mau.resturantapp.data.appData;
import mau.resturantapp.utils.Firebase.FirebaseWrite;

/**
 * Created by Yoouughurt on 11-12-2016.
 */

public class AddNewProduct_frag extends Fragment implements View.OnClickListener {

    private EditText name;
    private EditText price;
    private Spinner tab;
    private Button addProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.amdcontrol_newitem_addproduct,container,false);

        addProduct = (Button) root.findViewById(R.id.addNewProduct_button);
        name = (EditText) root.findViewById(R.id.addProduct_name_editText);
        price = (EditText) root.findViewById(R.id.price_editText);
        tab = (Spinner) root.findViewById(R.id.addToTab_spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), R.layout.amd_newitem_addproduct_spinner, R.id.addProduct_spinner, appData.tabs);
        adapter.setDropDownViewResource(R.layout.amd_newitem_addproduct_spinner);
        tab.setAdapter(adapter);
        addProduct.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == addProduct){
            Log.d("AddProductButton", " Clicked");
            if(checkUI()) {
                Log.d("CheckUI", "true");
                MenuTab currentTab = (MenuTab) tab.getSelectedItem();
                FirebaseWrite.addProductToTab(name.getText().toString(), Integer.parseInt(price.getText().toString()), currentTab.getKey());
            }
        }
    }

    private boolean checkUI(){
        if(!(name.getText().toString().equals("") && price.getText().toString().equals(""))){
            try{
                Integer.parseInt(price.getText().toString());
                return true;
            } catch(NumberFormatException e) {
                //TODO Maybe Handle wrong input - show user error message
            }
        }
        return false;
    }

    private class SpinnerHolder {
        TextView textview;
        View root;

        private SpinnerHolder(View view){
            this.root = view;
        }

        public void setText(String text) {
            textview = (TextView) root.findViewById(R.id.addProduct_spinner);
            textview.setText(text);
        }
    }



    private class SpinnerAdapter extends ArrayAdapter<MenuTab> {
        ArrayList<MenuTab> tabs;


        private SpinnerAdapter(Context context, int resource, int textViewResourceId, ArrayList<MenuTab> tabs) {
            super(context, resource, textViewResourceId, tabs);
            this.tabs = tabs;
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             return getRowView(position,convertView,parent);
         }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getRowView(position, convertView, parent);

        }

        private View getRowView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            SpinnerHolder spinnerHolder;
            if(convertView == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.amd_newitem_addproduct_spinner, parent, false);
                spinnerHolder = new SpinnerHolder(view);
                view.setTag(spinnerHolder);
            } else {
                spinnerHolder = (SpinnerHolder) view.getTag();
            }

            spinnerHolder.setText(tabs.get(position).getName());

            return view;
        }
    }
}
