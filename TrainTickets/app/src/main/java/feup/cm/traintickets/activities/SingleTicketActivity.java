package feup.cm.traintickets.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
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
    public View cardView;
    public View ticketProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardView = findViewById(R.id.card_view);
        ticketProgressView = findViewById(R.id.ticket_progress);
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
        showProgress(true);
        final Thread runner = new Thread() {
            @Override
            public void run() {
                try {
                    bitmap = textToImageEncode(textToEncode);
                    qrCodeImageView.setImageBitmap(bitmap);
                    showProgress(false);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        cardView.setVisibility(show ? View.GONE : View.VISIBLE);
        cardView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cardView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        ticketProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        ticketProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ticketProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
