package mau.restaurantapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import mau.restaurantapp.R;
import mau.restaurantapp.event.events.FragContainerChangedEvent;

public class Home extends Fragment {
    private View rod;
    private ArrayList<LinearLayout> extraBoxes = new ArrayList<>();
    private ArrayList<TextView> newsEntries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.home, container, false);

        LinearLayout mainLout = (LinearLayout) rod.findViewById(R.id.homeMainLayout);
        LinearLayout newsBox = (LinearLayout) rod.findViewById(R.id.homeNewsBox);

        // This should be pulled from Firebase; this is just dummy-content
        // Note: news entries should use "\n" for line breaks. Use "\n\n" to imitate a new "paragraph"
        newsBox.addView(appendNews("01-12-2016 22:32", "Accumsan gravida. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n\nUt nec mollis orci, in bibendum urna. Pellentesque et nulla et tortor laoreet hendrerit."));
        newsBox.addView(appendNews("25-11-2016 11:05", "Aenean vitae nunc mi. Pellentesque varius ultricies elit, non consectetur quam rutrum eget. Sed feugiat massa quis magna auctor vestibulum."));

        /*******************************************************************************/
        /** This is how to create a new box - note: functionality is limited for this **/
        /*******************************************************************************/
        // Create new layout
        LinearLayout l = newHomeBox();

        // Add textview to new layout
        l.addView(homeBoxEntry("Donec bibendum enim sit amet mauris varius eleifend.\n\nUt nec mollis orci, in bibendum urna. Pellentesque et nulla et tortor laoreet hendrerit."));

        // Add new layout to main layout
        mainLout.addView(l);

        return rod;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * Creates a new TextView to be added to the News Box.
     *
     * @param date    The date of the news-entry
     * @param content News content
     * @return TextView containing the news-entry
     */
    private TextView appendNews(String date, String content) {
        TextView t = new TextView(getActivity());

        t.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        );

        if (newsEntries.size() != 0)
            t.append("\n");

        t.append(date);
        t.append("\n" + content);

        newsEntries.add(t);

        return t;
    }

    /**
     * Will create a new "Home Box"
     *
     * @return LinearLayout-object, "the new box"
     */
    private LinearLayout newHomeBox() {
        LinearLayout l = new LinearLayout(getActivity());

        LinearLayout.LayoutParams pars = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        pars.bottomMargin = 25;

        l.setLayoutParams(pars);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(12, 12, 12, 12);

        l.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_corners));

        extraBoxes.add(l);

        return l;
    }

    /**
     * Will create content for new "Home Boxes".
     *
     * @param content Content to be added to home box
     * @return TextView containing content
     */
    private TextView homeBoxEntry(String content) {
        TextView t = new TextView(getActivity());

        t.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        );

        t.setText(content);

        newsEntries.add(t);

        return t;
    }

    /**
     * This method will delete the introduction-text in the app's home screen.
     * Use "\n" when doing line breaks. Use "\n\n" when you need to imitate a paragraph.
     *
     * @param s The new text for the home screen
     */
    private void setHomeText(String s) {
        TextView t = (TextView) rod.findViewById(R.id.homeWelcomeText);
        t.setText(s);
    }

    /**
     * This methid will delete the current header and replace it with given String s.
     *
     * @param s New header
     */
    private void setHomeHeader(String s) {
        TextView t = (TextView) rod.findViewById(R.id.homeWelcomeHeader);
        t.setText(s);
    }
}