package mau.resturantapp.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import mau.resturantapp.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCamera extends Fragment implements ZXingScannerView.ResultHandler {
    private View rod;
    private ZXingScannerView qrscanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.qr_camera_test, container, false);

        return rod;
    }

    @Override
    public void onPause() {
        super.onPause();
        qrscanner.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        qrscanner.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        //if(result.getBarcodeFormat().toString() == "QR") {
            // TODO IF the scanned format is QR AND the scanned text is valid then stop the fragment
            // and parse the scanned data to some other fragment, end it all with stopping the camera.

            // For now just print the result...
            System.out.println("Type: " + result.getBarcodeFormat().toString() + "\tText:" + result.getText());
        //}

        // In future version, where the fragment will end if an acceptable QR has been read, this
        // code will make sure the camera will keep looking for an acceptable QR before closing.
        qrscanner.resumeCameraPreview(this);
    }

    public void QRScanner(View view) {
        qrscanner = new ZXingScannerView(getActivity());
        getActivity().setContentView(qrscanner);

        qrscanner.setResultHandler(this);
        qrscanner.startCamera();
    }
}
