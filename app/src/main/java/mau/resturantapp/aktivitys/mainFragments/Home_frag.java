package mau.resturantapp.aktivitys.mainFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import mau.resturantapp.R;
import mau.resturantapp.event.events.FragContainerChangedEvent;

/**
 * Created by anwar on 10/16/16.
 */

public class Home_frag extends Fragment {

    private View rod;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.home_frag, container, false);

        TextView welcomeText = (TextView) rod.findViewById(R.id.homeWelcomeText);
        TextView openingHours = (TextView) rod.findViewById(R.id.homeOpeningHours);
        TextView testBox_1 = (TextView) rod.findViewById(R.id.homeTestBox_1);
        TextView testBox_2 = (TextView) rod.findViewById(R.id.homeTestBox_2);

        // The deprecation is from API24. For now we'll just ignore this, as the only difference
        // in level 24 is to include a version specified as 2nd parameter
        // e.g.: fromHtml(someString, Html.FROM_HTML_MODE_LEGACY);
        welcomeText.setText(Html.fromHtml("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec bibendum enim sit amet mauris varius eleifend.</p><p>Sed ultricies lobortis lorem, sed mattis mi. Aenean vulputate purus et accumsan gravida. Interdum et malesuada fames ac ante ipsum primis in faucibus.</p><p>Ut nec mollis orci, in bibendum urna. Pellentesque et nulla et tortor laoreet hendrerit.</p>"));
        openingHours.setText(Html.fromHtml("<h3>Åbningstider</h3><p>Man-torsdag 9-17</p><p>Fre- & lørdag 9-21</p><p>Søn- & helligdag</p></p>"));
        testBox_1.setText(Html.fromHtml("<h3>Nyheder</h3><p>01-12-2016 22:32</p><p>Accumsan gravida. Interdum et malesuada fames ac ante ipsum primis in faucibus.</p><p>Ut nec mollis orci, in bibendum urna. Pellentesque et nulla et tortor laoreet hendrerit.</p><hr /><p>01-12-2016 22:32</p><p>Aenean vitae nunc mi. Pellentesque varius ultricies elit, non consectetur quam rutrum eget. Sed feugiat massa quis magna auctor vestibulum.</p>"));
        testBox_2.setText(Html.fromHtml("<h3>Lorem</h3><p>Donec bibendum enim sit amet mauris varius eleifend.</p><p>Ut nec mollis orci, in bibendum urna. Pellentesque et nulla et tortor laoreet hendrerit.</p>"));


        return rod;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        EventBus.getDefault().post(event);
    }
}
