package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mau.resturantapp.R;

/**
 * Created by anwar on 10/16/16.
 */

public class Checkout_frag extends Fragment implements View.OnClickListener {

    private View rod;
    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editDate;
    private Button checkoutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.checkout_frag, container, false);

        editName = (EditText) rod.findViewById(R.id.edittxt_checkout_name);
        editEmail = (EditText) rod.findViewById(R.id.edittxt_checkout_email);
        editPhone = (EditText) rod.findViewById(R.id.edittxtnum_checkout_phone);
        editDate = (EditText) rod.findViewById(R.id.edittxt_checkout_date);
        checkoutBtn = (Button) rod.findViewById(R.id.btn_checkout_checkout);


        checkoutBtn.setOnClickListener(this);


        return rod;
    }

    @Override
    public void onClick(View v) {
        if (v == checkoutBtn) {
            finishCheckOut();
        }

    }

    private void finishCheckOut() {
        // TODO

    }
}
