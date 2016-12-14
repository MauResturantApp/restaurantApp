package mau.resturantapp.aktivitys.mainFragments.userControls;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import mau.resturantapp.R;
import mau.resturantapp.data.UserProfile;
import mau.resturantapp.data.appData;
import mau.resturantapp.utils.Firebase.FirebaseWrite;

public class userProfile_frag extends Fragment implements View.OnClickListener {

    private EditText name;
    private EditText phoneNumber;
    private TextView emailAddress;
    private Button updateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod =  inflater.inflate(R.layout.user_profile_frag, container, false);

        name = (EditText) rod.findViewById(R.id.name_editText);
        phoneNumber = (EditText) rod.findViewById(R.id.phoneNumber_edittext);
        emailAddress = (TextView) rod.findViewById(R.id.emailAddress_editText);

        updateUI();
        emailAddress.setText(appData.firebaseAuth.getCurrentUser().getEmail());

        updateButton = (Button) rod.findViewById(R.id.updateProfile);
        updateButton.setOnClickListener(this);
        return rod;
    }

    @Override
    public void onClick(View v) {
        if(v == updateButton){
            if(hasUpdate()){
                FirebaseWrite.updateUserProfile(name.getText().toString(), phoneNumber.getText().toString());
            }
        }
    }

    private boolean hasUpdate(){
        UserProfile userProfile = appData.userProfile;
        if(userProfile != null){
            if(userProfile.getName() != null && userProfile.getPhoneNumber() != null){
                if(userProfile.getName().equals(name.getText().toString()) && userProfile.getPhoneNumber().equals(phoneNumber.getText().toString())){
                    return false;
                }
            }
        }

        return true;
    }

    private void updateUI(){
        UserProfile userProfile = appData.userProfile;
        if(userProfile != null){
            if(userProfile.getName() != null && userProfile.getPhoneNumber() != null){
                name.setText(userProfile.getName());
                phoneNumber.setText(userProfile.getPhoneNumber());
            }
        }
    }
}
