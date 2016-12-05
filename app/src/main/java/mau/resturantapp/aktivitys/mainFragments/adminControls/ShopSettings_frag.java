package mau.resturantapp.aktivitys.mainFragments.adminControls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mau.resturantapp.R;

/**
 * Created by AnwarC on 05/12/2016.
 */

public class ShopSettings_frag extends Fragment {
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.amdcontrol_shopsettings,container,false);


        return root;
    }
}
