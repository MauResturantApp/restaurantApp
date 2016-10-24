package mau.resturantapp.test;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import mau.resturantapp.R;
import mau.resturantapp.utils.QRCodeUtils;

public class QRTest extends Fragment {
    // Default values
    private static final int DEFAULT_QR_SIZE = 512;
    private static final String TEST_STRING = "orderid::00004321;;username::testuser";

    // Views
    private ImageView qrcode;
    private TextView qrTestContent;
    private EditText newContentText;
    private Button newContentBtn;

    // Meh
    private View rod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.qr_test, container, false);

        qrcode = (ImageView) rod.findViewById(R.id.qrCodeImg);
        qrTestContent = (TextView) rod.findViewById(R.id.qrTestContent);

        newContentText = (EditText) rod.findViewById(R.id.qrContentGen);
        newContentBtn = (Button) rod.findViewById(R.id.btnNewContent);

        // Encode test
        try {
            qrcode.setImageBitmap(QRCodeUtils.EncodeBitmapQRCode(TEST_STRING, DEFAULT_QR_SIZE));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Decode test
        newContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    qrcode.setImageBitmap(QRCodeUtils.EncodeBitmapQRCode(newContentText.getText().toString(), DEFAULT_QR_SIZE));
                    qrTestContent.setText(QRCodeUtils.DecodeBitmapQRCode(((BitmapDrawable) qrcode.getDrawable()).getBitmap()));
                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rod;
    }
}