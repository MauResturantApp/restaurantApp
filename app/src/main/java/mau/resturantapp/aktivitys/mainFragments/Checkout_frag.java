package mau.resturantapp.aktivitys.mainFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;

/**
 * Created by AnwarC on 23/11/2016.
 */

public class Checkout_frag extends Fragment implements View.OnClickListener{

    private View root;
    private Button finishBtn;
    private EditText UIcomment;
    private TimePicker UITimepick;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.checkout_frag,container,false);

        finishBtn = (Button) root.findViewById(R.id.btn_checkout_sendOrder);
        UIcomment = (EditText) root.findViewById(R.id.editTxt_checkout_UIeTxt);
        UITimepick = (TimePicker) root.findViewById(R.id.timePick_checkout_UItime);

        UITimepick.setIs24HourView(true);

        finishBtn.setOnClickListener(this);




        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }


    private void finishCheckOut(){
        if(shopIsOpen()){

        }

    }

    private boolean shopIsOpen(){
        if(UITimepick.getCurrentHour() > appData.OPENHOUR && UITimepick.getCurrentHour() < appData.CLOSEHOUR){
            return true;
        }
        else if (UITimepick.getCurrentHour() == appData.OPENHOUR && UITimepick.getCurrentMinute() >= appData.OPENMINUT){
            return true;
        }
        else if (UITimepick.getCurrentHour() == appData.CLOSEHOUR && UITimepick.getCurrentMinute() < appData.CLOSEMINUT){
            return true;
        }
        else{
            return false;
        }
    }
}
