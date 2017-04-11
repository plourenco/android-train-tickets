package feup.cm.traintickets.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.sql.Date;
import java.util.UUID;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;

public class SingleTicketActivity extends AppCompatActivity {

    ImageView qrCodeImageView;
    Bitmap bitmap;
    TicketModel ticket;
    public final static int QRCODE_WIDTH = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qrCodeImageView = (ImageView)findViewById(R.id.imageView2);

        /**
         * For test purposes
         * Need to add an Intent here!
         */
        ticket = new TicketModel(1, UUID.randomUUID(), null, new StationModel(1), Date.valueOf("2017-06-12"),
                1, Date.valueOf("2017-06-12"), null, false);

        String textToEncode = ticket.getUniqueId() + "\n" +
                ticket.getTicketDate().toString() + "\n" +
                ticket.getArrivalStation().getId();

        generateQR(textToEncode);

    }

    private void generateQR(final String textToEncode) {
        final Thread runner = new Thread() {
            @Override
            public void run() {
                try {
                    bitmap = textToImageEncode(textToEncode);
                    qrCodeImageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        runner.run();
    }

    private Bitmap textToImageEncode(String editTextValue) throws Exception {
        BitMatrix bitMatrix;

        try {
            bitMatrix = new MultiFormatWriter().encode(
                    editTextValue,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCODE_WIDTH, QRCODE_WIDTH, null
            );
        } catch (IllegalArgumentException iae) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int i = 0; i < bitMatrixHeight; i++) {
            int offset = i * bitMatrixWidth;

            for (int j = 0; j < bitMatrixWidth; j++) {
                pixels[offset + j] = bitMatrix.get(j, i)
                        ? getResources().getColor(R.color.QRCodeBlack)
                        : getResources().getColor(R.color.QRCodeWhite);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
