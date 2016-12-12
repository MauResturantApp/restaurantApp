package mau.resturantapp.aktivitys.mainFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import mau.resturantapp.R;
import mau.resturantapp.event.events.FragContainerChangedEvent;
import mau.resturantapp.utils.EmailIntent;

public class Contact_frag extends Fragment {
    private View rod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.contact_frag, container, false);

        getDepartments();

        Button send = (Button) rod.findViewById(R.id.contactSendEmail);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String department = ((Spinner) rod.findViewById(R.id.contactMailListSpinner)).getSelectedItem().toString();
                String subject = ((EditText) rod.findViewById(R.id.contactMailSubject)).getText().toString();
                String content = ((EditText) rod.findViewById(R.id.contactMailContent)).getText().toString();

                sendEmail(department, subject, content);

                ((EditText) rod.findViewById(R.id.contactMailSubject)).setText("");
                ((EditText) rod.findViewById(R.id.contactMailContent)).setText("");
            }
        });

        Button reset = (Button) rod.findViewById(R.id.contactResetBtn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) rod.findViewById(R.id.contactMailSubject)).setText("");
                ((EditText) rod.findViewById(R.id.contactMailContent)).setText("");
            }
        });

        return rod;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * Returns a list of departments.
     *
     * @return List of departments
     */
    private List<String> getDepartments() {
        List<String> departments = new ArrayList<>();

        // TODO Get departments from database
        // Using string resources for now
        // Example below how to build a spinner-list
        Spinner s = (Spinner) rod.findViewById(R.id.contactMailListSpinner);

        ArrayAdapter<CharSequence> ap = ArrayAdapter.createFromResource(
                getContext(),
                R.array.contactMailList,
                R.layout.support_simple_spinner_dropdown_item
        );

        ap.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        s.setAdapter(ap);

        return departments;
    }

    /**
     * Will send an email to the chosen department's email with the given content.
     * Achieved by creating an intent and starting this intent's activity.
     * The intent will prompt user to choose their favorite email client (or pick
     * the default email client). Will return to this page upon "send email" has been prssed
     * in the email client (or if some sort of abort-action is done).
     *
     * @param department destination email
     * @param content content of email
     */
    private void sendEmail(String department, String subject, String content) {
        ArrayList<String> to = new ArrayList<>();

        switch (department) {
            case "General inquiries":
                to.add("maagenator@gmail.com");
                break;

            case "Technical department":
                to.add("maagenator@gmail.com");
                break;

            case "Accounting":
                to.add("maagenator@gmail.com");
                break;

            case "Management":
                to.add("maagenator@gmail.com");
                break;

            default:
                to.add("maagenator@gmail.com");
                break;
        }

        String body = content;
        body += "\n\n";
        body += "----DO NOT EDIT AFTER THIS LINE----\n";
        body += "DEPARTMENT::" + department + "\n";
        body += "SENTFROM::PHONEAPP";

        startActivity(EmailIntent.sendEmail(to.toArray(new String[to.size()]), subject, body));
    }
}