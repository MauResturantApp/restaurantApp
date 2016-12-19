package mau.restaurantapp.activities.fragments.adminControls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mau.restaurantapp.R;

/**
 * Created by Yoouughurt on 11-12-2016.
 */

public class RemoveProduct extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.admcontrol_newitem_removeproduct, container, false);


        return rod;
    }
}
