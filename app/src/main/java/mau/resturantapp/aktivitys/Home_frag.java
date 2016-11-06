package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mau.resturantapp.R;

/**
 * Created by anwar on 10/16/16.
 */

public class Home_frag extends Fragment {

    private View rod;
//jj
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.home_frag, container, false);


        return rod;
    }
}
