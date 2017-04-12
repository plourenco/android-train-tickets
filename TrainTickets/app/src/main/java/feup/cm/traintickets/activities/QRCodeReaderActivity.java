package feup.cm.traintickets.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

import java.util.Date;
import java.util.UUID;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.sqlite.TicketReviserBrowser;
import feup.cm.traintickets.util.QREncryption;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import se.simbio.encryption.Encryption;

public class QRCodeReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);

        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView = new ZXingScannerView(getApplicationContext());
                setContentView(scannerView);
                scannerView.setResultHandler(QRCodeReaderActivity.this);
                scannerView.startCamera();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (scannerView != null)
            scannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        Log.w("QRReader", result.getText());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Scan Result");

        String decrypted = null;
        boolean match = false;

        try {
            Encryption encryption = QREncryption.getInstance();
            decrypted = encryption.decryptOrNull(result.getText());
            String[] params = decrypted.split(";#");
            UUID uuid;
            Date ticketDate;
            StationModel arrStation;
            StationModel depStation;
            boolean isUsed;
            TripModel trip;
            if (params.length > 0 && params.length <= 6) {
                uuid = UUID.fromString(params[0]);
                ticketDate = java.sql.Date.valueOf(params[1]);
                arrStation = new StationModel(Integer.parseInt(params[2]));
                depStation = new StationModel(Integer.parseInt(params[3]));
                isUsed = Boolean.valueOf(params[4]);
                trip = new TripModel(Integer.parseInt(params[5]));
                if(getTicketToRevise(uuid, ticketDate, arrStation, depStation, isUsed, trip))
                    match = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (decrypted != null) {
          if (match) {
              alert.setMessage("Valid ticket");
          } else
              alert.setMessage("Invalid ticket");
        } else
            alert.setMessage("Error reading QRCode");

        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resumeCamera();
            }
        });
    }

    private void resumeCamera(){
        scannerView.resumeCameraPreview(this);
    }

    private boolean getTicketToRevise(UUID uuid, Date ticketDate, StationModel arrStation,
                                      StationModel depStation, boolean isUsed, TripModel trip) {
        TicketReviserBrowser ticketReviserBrowser = new TicketReviserBrowser(this);
        TicketModel tm = ticketReviserBrowser.get(uuid.toString());

        return tm.getTicketDate().equals(ticketDate)
                && tm.getArrivalStation().getId() == arrStation.getId()
                && tm.getDepartureStation().getId() == depStation.getId()
                && tm.getIsUsed() == isUsed
                && tm.getTrip().getId() == trip.getId();
    }
}
