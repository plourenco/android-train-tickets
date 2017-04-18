package feup.cm.traintickets.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    TextView expDate;

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
        expDate = (TextView)findViewById(R.id.expiryDate);

        saveCard = (Button)findViewById(R.id.saveCardBtn);
        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCreditCard();
            }
        });

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar);
            }
        };
        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SettingsActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
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

    private void updateLabel(Calendar calendar) {
        String myFormat = "MMM dd, yyyy";
        String apiFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        String newDate = sdf.format(calendar.getTime());

        if (!expDate.getText().equals(newDate)){
            expDate.setText("No date has been chosen");
        }

        expDate.setText(newDate);
        expDate.setTag(new SimpleDateFormat(apiFormat, Locale.US).format(calendar.getTime()));
    }

    private void saveCreditCard() {
        if (userId == -1){
            Toast.makeText(this, "User id invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (expDate.getTag() == null) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            date = sdf.parse(expDate.getTag().toString());
        } catch (ParseException pe) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.before(new Date())){
            Toast.makeText(this, "Date has to be superior to today", Toast.LENGTH_SHORT).show();
            return;
        }

        final CreditCardModel creditCard = new CreditCardModel(ccNumber.getText().toString(), cvv2.getText().toString(), date);
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

        editor.putString("CC_NUMBER", creditCard.getCcNumber());
        editor.putString("CVV2", creditCard.getCvv2());
        editor.putLong("CC_DATE", creditCard.getExpiryDate().getTime());
        editor.apply();

        Toast.makeText(this, "Saved to sharedPrefs", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getBottomNavId() {
        return R.id.action_settings;
    }

}
