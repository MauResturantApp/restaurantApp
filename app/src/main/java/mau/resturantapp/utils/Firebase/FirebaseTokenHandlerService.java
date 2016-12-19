package mau.resturantapp.utils.Firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Yoouughurt on 19-12-2016.
 */

public class FirebaseTokenHandlerService extends FirebaseInstanceIdService {
    public static final String instanceID = "instanceID";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString(instanceID, token).apply();
        Log.d("TOKEN",token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token){
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
        }
}


