package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import mau.resturantapp.R;

/**
 * Created by anwar on 10/24/16.
 */

public class Signup_frag extends Fragment {

    private View rod;
    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected Button signUpButton;
    private FirebaseAuth mFirebaseAuth;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.signup_frag, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        passwordEditText = (EditText) rod.findViewById(R.id.passwordField);
        emailEditText = (EditText) rod.findViewById(R.id.emailField);
        signUpButton = (Button) rod.findViewById(R.id.signUpButton);


        return rod;
    }
}
