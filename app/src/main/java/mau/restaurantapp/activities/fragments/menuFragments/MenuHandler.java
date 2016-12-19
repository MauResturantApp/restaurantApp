package mau.restaurantapp.activities.fragments.menuFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mau.restaurantapp.R;

public class MenuHandler extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.menu_handler, container, false);


        return rod;
    }
}
