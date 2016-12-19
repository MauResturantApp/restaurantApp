package mau.restaurantapp.activities.fragments.userControls;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import mau.restaurantapp.R;
import mau.restaurantapp.data.AppData;
import mau.restaurantapp.data.types.UserProfileType;
import mau.restaurantapp.utils.firebase.FirebaseWrite;

public class UserProfile extends Fragment implements View.OnClickListener {

    private EditText name;
    private EditText phoneNumber;
    private TextView emailAddress;
    private Button updateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.user_profile, container, false);

        name = (EditText) rod.findViewById(R.id.name_editText);
        phoneNumber = (EditText) rod.findViewById(R.id.phoneNumber_edittext);
        emailAddress = (TextView) rod.findViewById(R.id.emailAddress_editText);

        updateUI();
        emailAddress.setText(AppData.firebaseAuth.getCurrentUser().getEmail());

        updateButton = (Button) rod.findViewById(R.id.updateProfile);
        updateButton.setOnClickListener(this);
        return rod;
    }

    @Override
    public void onClick(View v) {
        if (v == updateButton) {
            if (hasUpdate()) {
                FirebaseWrite.updateUserProfile(name.getText().toString(), phoneNumber.getText().toString());
            }
        }
    }

    private boolean hasUpdate() {
        UserProfileType userProfileType = AppData.userProfileType;
        if (userProfileType != null) {
            if (userProfileType.getName() != null && userProfileType.getPhoneNumber() != null) {
                if (userProfileType.getName().equals(name.getText().toString()) && userProfileType.getPhoneNumber().equals(phoneNumber.getText().toString())) {
                    return false;
                }
            }
        }

        return true;
    }

    private void updateUI() {
        UserProfileType userProfileType = AppData.userProfileType;
        if (userProfileType != null) {
            if (userProfileType.getName() != null && userProfileType.getPhoneNumber() != null) {
                name.setText(userProfileType.getName());
                phoneNumber.setText(userProfileType.getPhoneNumber());
            }
        }
    }

    /**
     * Testing connection to server - Should not be in here in final build
     *
     * @param token represents the real device token
     */
    public void sendTokenToServer(String token) {
        testServerConnection test = new testServerConnection();
        test.token = token;
        test.execute();
    }

    private class testServerConnection extends AsyncTask<Void, Void, Void> {

        private String token;

        @Override
        protected Void doInBackground(Void... params) {
            byte[] data = token.getBytes();

            InetAddress ipAddress = null;
            try {
                ipAddress = InetAddress.getByName("185.15.73.229");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 9000);
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                socket.send(packet);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Packet send", packet.toString());
            return null;
        }
    }
}
