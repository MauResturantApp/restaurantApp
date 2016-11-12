package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewUserFailedEvent;
import mau.resturantapp.events.NewUserSuccesfullEvent;

/**
 * Created by anwar on 10/24/16.
 */

public class Signup_frag extends Fragment implements OnClickListener {

    private View rod;
    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected Button signUpButton;
    protected EditText passWordConfirm;
    private ProgressBar loader;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.signup_frag, container, false);

        loader = (ProgressBar) rod.findViewById(R.id.progBar_signup);
        passwordEditText = (EditText) rod.findViewById(R.id.passwordField);
        emailEditText = (EditText) rod.findViewById(R.id.emailField);
        signUpButton = (Button) rod.findViewById(R.id.signUpButton);
        passWordConfirm = (EditText) rod.findViewById(R.id.passwordFieldCheck);
        loader.setVisibility(View.GONE);

        signUpButton.setOnClickListener(this);
        EventBus.getDefault().register(this);

        return rod;
    }

    @Override
    public void onClick(View v) {
        loader.setVisibility(View.VISIBLE);
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String passWordCheck = passWordConfirm.getText().toString();

        password = password.trim();
        email = email.trim();
        passWordCheck = passWordCheck.trim();

        if (password.isEmpty() || email.isEmpty() || passWordCheck.isEmpty()) {
            Toast.makeText(getContext(), "Venligst udfyld alle felter", Toast.LENGTH_LONG).show();
            loader.setVisibility(View.GONE);

        } else if (!password.equals(passWordCheck)) {
            Toast.makeText(getContext(), "De indtastede password matcher ikke, venligst prøv igen", Toast.LENGTH_LONG).show();
            loader.setVisibility(View.GONE);
        } else {
            appData.newUser(email,password);
        }
    }

    @Subscribe
    public void succesSignup(NewUserSuccesfullEvent event) {
        loader.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Din bruger er nu oprettet, velkommen til Resturant Navn", Toast.LENGTH_LONG).show();
        passWordConfirm.setText("");
        passwordEditText.setText("");
        emailEditText.setText("");

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        super.onResume();
    }

    @Subscribe
    public void failedSignup(NewUserFailedEvent event) {
        loader.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Noget gik galt, venligst prøv igen", Toast.LENGTH_LONG).show();
        passWordConfirm.setText("");
        passwordEditText.setText("");
    }

}
