package mau.resturantapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

public class QRCodeUtils {
    /**
     * Creates a QR image as bitmap with given content string as encoded content.<br>
     * Furthermore, the content of the string shouldn't be longer than the QR's capabilities.<br>
     * @see <a href="https://en.wikipedia.org/wiki/QR_code#Storage">QR Design</a>
     *
     * @param content The content of the QR
     * @param size The size of the image
     * @return QR image as bitmap
     */
    public Bitmap GenerateImageViewQRCode(String content, int size) throws WriterException {
        QRCodeWriter qrw = new QRCodeWriter();
        int w, h;

        // Create a matrix containing the actual code
        BitMatrix bmatrix = qrw.encode(content, BarcodeFormat.QR_CODE, size, size);

        w = bmatrix.getWidth();
        h = bmatrix.getHeight();

        Bitmap bmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);

        for(int i = 0; i < w; i++)
            for(int j = 0; j < w; j++)
                bmap.setPixel(i,j, bmatrix.get(i,j) ? Color.BLACK : Color.WHITE);

        return bmap;
    }

    /**
     * Converts bitmap QR to binary data and decodes the content; method will return the decoded
     * data as a string (text).
     *
     * @param qr Bitmap containing QR
     * @return Text encoded in given QR
     * @throws FormatException
     * @throws ChecksumException
     * @throws NotFoundException
     */
    public String DecodeBitmapQRCodeText(Bitmap qr) throws FormatException, ChecksumException, NotFoundException {
        Reader r;
        BinaryBitmap bmap;
        LuminanceSource src;

        int[] bitarray = new int[qr.getWidth()*qr.getHeight()];

        qr.getPixels(bitarray, 0, qr.getWidth(), 0, 0, qr.getWidth(), qr.getHeight());

        r = new QRCodeReader();
        src = new RGBLuminanceSource(qr.getWidth(), qr.getHeight(), bitarray);
        bmap = new BinaryBitmap(new HybridBinarizer(src));

        return r.decode(bmap).getText();
    }
}
