package feup.cm.traintickets.activities;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

import java.util.Date;
import java.util.UUID;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.sqlite.TicketReviserBrowser;
import feup.cm.traintickets.util.QREncryption;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import se.simbio.encryption.Encryption;

public class QRCodeReaderActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    Button button;

    private boolean cameraOpened = false;

    public static final int PERMISSION_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);

        scannerView = new ZXingScannerView(getApplicationContext());

        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request permission.
                // This does it asynchronously so we have to wait for onRequestPermissionResult
                // before trying to open the camera.
                if (!haveCameraPermission()) {
                    if (Build.VERSION.SDK_INT >= 23)
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                }
                else {
                    startCamera();
                }
            }
        });

        if(savedInstanceState != null) {
            Log.d("camera", String.valueOf(savedInstanceState.getBoolean("camera", false)));
            if(savedInstanceState.getBoolean("camera", false)) {
                setContentView(scannerView);
                startCamera();
            }
        }

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean haveCameraPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // This is because the dialog was cancelled when we recreated the activity.
        if (permissions.length == 0 || grantResults.length == 0)
            return;

        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                }
                else {
                    finish();
                }
            }
            break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("camera", cameraOpened);
    }

    public void startCamera() {
        setContentView(scannerView);
        scannerView.setResultHandler(QRCodeReaderActivity.this);
        scannerView.startCamera();
        cameraOpened = true;
    }

    public void stopCamera() {
        setContentView(R.layout.activity_qrcode_reader);
        scannerView.stopCamera();
        cameraOpened = false;
    }

    @Override
    protected int getBottomNavId() {
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (scannerView != null)
            scannerView.stopCamera();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (scannerView != null)
            startCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(final Result result) {
        Log.w("QRReader", result.getText());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Scan Result");

        String decrypted = null;
        boolean match = false;
        boolean alreadyScanned = false;

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
                try {
                    if(getTicketToRevise(uuid, ticketDate, arrStation, depStation, isUsed, trip)) {
                        match = true;
                        setTicketUsed(uuid);
                    }
                } catch (RuntimeException re) {
                    alreadyScanned = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (decrypted != null) {
            if (alreadyScanned)
                alert.setMessage("Already scanned the ticket");
            else
                if (match)
                    alert.setMessage("Valid ticket");
                else
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

    private void setTicketUsed(UUID uuid) {
        TicketReviserBrowser ticketReviserBrowser = new TicketReviserBrowser(this);
        ticketReviserBrowser.setTicketUsed(uuid.toString());
    }

    private void resumeCamera(){
        scannerView.resumeCameraPreview(this);
    }

    private boolean getTicketToRevise(UUID uuid, Date ticketDate, StationModel arrStation,
                                      StationModel depStation, boolean isUsed, TripModel trip)
                                      throws RuntimeException {
        TicketReviserBrowser ticketReviserBrowser = new TicketReviserBrowser(this);
        TicketModel tm = ticketReviserBrowser.get(uuid.toString());

        if (tm.getIsUsed())
            throw new RuntimeException("Already scanned the ticket");

        return tm.getTicketDate().equals(ticketDate)
                && tm.getArrivalStation().getId() == arrStation.getId()
                && tm.getDepartureStation().getId() == depStation.getId()
                && tm.getIsUsed() == isUsed
                && tm.getTrip().getId() == trip.getId();
    }
}
