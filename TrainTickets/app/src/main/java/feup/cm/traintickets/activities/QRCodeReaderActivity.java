package feup.cm.traintickets.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

import feup.cm.traintickets.R;
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
    public void handleResult(Result result) {
        Log.w("QRReader", result.getText());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Scan Result");

        String decrypted = null;

        try {
            Encryption encryption = QREncryption.getInstance();
            decrypted = encryption.decryptOrNull(result.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (decrypted != null)
            alert.setMessage(decrypted);
        else
            alert.setMessage("Error reading QRCode");

        AlertDialog dialog = alert.create();
        dialog.show();

        // To resume scanning!
        //scannerView.resumeCameraPreview(this);
    }
}
