package mau.resturantapp.aktivitys.mainFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import mau.resturantapp.R;
import mau.resturantapp.event.events.FragContainerChangedEvent;

/**
 * Created by anwar on 10/16/16.
 */

public class Contact_frag extends Fragment {
    private View rod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.contact_frag, container, false);


        return rod;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        EventBus.getDefault().post(event);
    }
}
