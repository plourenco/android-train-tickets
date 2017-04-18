package feup.cm.traintickets.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.Date;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.CreditCardModel;
import feup.cm.traintickets.runnables.CreditCardSaveTask;

public class SettingsActivity extends BaseActivity {

    /*
     * TODO: change expiry date type & put expiry date in shared prefs
     */
    EditText ccNumber;
    EditText cvv2;
    EditText expDate;

    Button saveCard;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ccNumber = (EditText)findViewById(R.id.ccNumber);
        cvv2 = (EditText)findViewById(R.id.cvv2Number);
        expDate = (EditText)findViewById(R.id.expiryDate);

        saveCard = (Button)findViewById(R.id.saveCardBtn);
        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCreditCard();
            }
        });

        sharedPrefs = getSharedPreferences("feup.cm.traintickets", Context.MODE_PRIVATE);
        String cc = sharedPrefs.getString("CC_NUMBER", "");
        String cv = sharedPrefs.getString("CVV2", "");
        userId = sharedPrefs.getInt("LOGIN_ID", -1);
        // TODO: Set date
        //Date exp = sharedPrefs.getString("CC_DATE", "");
        if (!cc.isEmpty() && !cv.isEmpty()/* && exp != null*/) {
            ccNumber.setText(cc);
            cvv2.setText(cv);
        }
    }

    private void saveCreditCard() {

        if (userId == -1){
            Toast.makeText(this, "User id invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        final CreditCardModel creditCard = new CreditCardModel(ccNumber.getText().toString(), cvv2.getText().toString(), new Date());
        CreditCardSaveTask creditCardSaveTask = new CreditCardSaveTask(creditCard, getToken(), userId) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success){
                    saveToSharedPrefs(creditCard);
                    String updateOrCreate;
                    if (getRows().equals("1"))
                        updateOrCreate = "Credit Card Created and Saved";
                    else
                        updateOrCreate = "Credit Card Updated and Saved";
                    Toast.makeText(getApplicationContext(), updateOrCreate, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getErrorString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onCancelled() {}
        };
        creditCardSaveTask.execute((Void) null);
    }

    private void saveToSharedPrefs(CreditCardModel creditCard) {
        if (creditCard.getCvv2().isEmpty() || creditCard.getExpiryDate() == null
                || creditCard.getCcNumber().isEmpty()) {
            Toast.makeText(this, "Not saved to sharedPrefs", Toast.LENGTH_SHORT).show();
            return;
        }

        editor = sharedPrefs.edit();

        // TODO: Add expiryDate
        editor.putString("CC_NUMBER", creditCard.getCcNumber());
        editor.putString("CVV2", creditCard.getCvv2());
        editor.apply();

        Toast.makeText(this, "Saved to sharedPrefs", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getBottomNavId() {
        return R.id.action_settings;
    }

}
