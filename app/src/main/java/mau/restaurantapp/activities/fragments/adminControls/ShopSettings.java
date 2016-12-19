package mau.restaurantapp.activities.fragments.adminControls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import mau.restaurantapp.R;
import mau.restaurantapp.data.AppData;
import mau.restaurantapp.utils.firebase.FirebaseWrite;

/**
 * Created by AnwarC on 05/12/2016.
 */

public class ShopSettings extends Fragment implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener {
    private View root;

    private Spinner daySpin;
    private EditText maintext;
    private TimePicker openTime;
    private TimePicker closeTime;
    private Switch openCloseshop;
    private Button saveSettingsBtn;
    private String selectedDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.admcontrol_shopsettings, container, false);

        daySpin = (Spinner) root.findViewById(R.id.spin_admcontrol_days);
        maintext = (EditText) root.findViewById(R.id.edittxt_admcontrol_maintext);
        openCloseshop = (Switch) root.findViewById(R.id.switch_admincontrol_closeshop);
        openTime = (TimePicker) root.findViewById(R.id.timepick_admcontrol_open);
        closeTime = (TimePicker) root.findViewById(R.id.timepick_admcontrol_close);
        saveSettingsBtn = (Button) root.findViewById(R.id.btn_admcontrol_savesettings);

        openTime.setIs24HourView(true);
        closeTime.setIs24HourView(true);

        daySpin.setOnItemSelectedListener(this);
        openCloseshop.setOnCheckedChangeListener(this);
        saveSettingsBtn.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.daysForOpeningTimes, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpin.setAdapter(adapter);


        openCloseshop.setChecked(AppData.shopOpen);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == saveSettingsBtn) {
            saveSettings();
        }

    }

    private void saveSettings() {
        int openMinuts = openTime.getCurrentMinute();
        int openHour = openTime.getCurrentHour();
        int closeMinute = closeTime.getCurrentMinute();
        int closeHour = closeTime.getCurrentHour();
        String hometxt = maintext.getText().toString();

        FirebaseWrite.setOpeningHours(openHour, openMinuts, closeHour, closeMinute);
        FirebaseWrite.setShopMainText(hometxt);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        /*
        Rækkefølen står under strings arrayet: DaysforOpening.
         */

        switch (i) {
            case 0: {
                selectedDay = "Monday";

                break;
            }

            case 1: {
                selectedDay = "Tuesday";


                break;
            }

            case 2: {
                selectedDay = "Wednesday";

                break;
            }


            case 3: {
                selectedDay = "Thursday";

                break;
            }

            case 4: {
                selectedDay = "Friday";

                break;
            }

            case 5: {
                selectedDay = "Saturday";

                break;
            }
            case 6: {
                selectedDay = "Friday";

                break;
            }
            case 7: {
                selectedDay = "Weekdays";

                break;
            }

            case 8: {
                selectedDay = "Weekend";

                break;
            }
            default: {
                break;
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (openCloseshop == compoundButton) {
            FirebaseWrite.setShopOpenOrClosed(b);
            AppData.setShopOpenOrClosed(b);
        }

    }
}
