package feup.cm.traintickets.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kogitune.activity_transition.ActivityTransition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.util.DateDeserializer;

public class BuyTicketActivity extends BaseActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.UK);

    private Spinner origin;
    private Spinner dest;
    private Button forward;

    private TextView departure;
    private TextView train;
    private TextView title;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        origin = (Spinner) findViewById(R.id.origin_station);
        dest = (Spinner) findViewById(R.id.destination_station);
        title = (TextView) findViewById(R.id.title_left);
        departure = (TextView) findViewById(R.id.departure_desc);
        train = (TextView) findViewById(R.id.train_desc);
        price = (TextView) findViewById(R.id.ticket_price_value);
        forward = (Button) findViewById(R.id.button);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward();
            }
        });

        // Set origin change listener
        AdapterView.OnItemSelectedListener oLis = new AdapterView.OnItemSelectedListener() {
            boolean first = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title.setText(parent.getItemAtPosition(position).toString());
                if(!first) {
                    train.setText(getString(R.string.display_select_train));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        AdapterView.OnItemSelectedListener dLis = new AdapterView.OnItemSelectedListener() {
            boolean first = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!first) {
                    train.setText(getString(R.string.display_select_train));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        // Open calendar on click input
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar);
            }
        };
        departure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BuyTicketActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
        // Open train list on click input
        train.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!errorProne()) {
                    StationModel org = (StationModel) origin.getSelectedItem();
                    StationModel ds = (StationModel) dest.getSelectedItem();

                    forwardTrain(org, ds);
                }
            }
        });

        loadStations();
        origin.setOnItemSelectedListener(oLis);
        dest.setOnItemSelectedListener(dLis);
    }

    @Override
    protected int getBottomNavId() {
        return R.id.action_buyticket;
    }

    protected void loadStations() {
        final List<StationModel> list = new ArrayList<StationModel>();
        StationGetTask task = new StationGetTask(getToken()) {

            @Override
            protected void onPostExecute(Boolean success) {
                if(success) {
                    for(StationModel station : getStations()) {
                        list.add(station);
                    }
                    title.setText(list.size() > 0 ? list.get(0).toString() :
                            getString(R.string.error_select_station));
                    ArrayAdapter<StationModel> dataAdapter = new ArrayAdapter<StationModel>(
                            BuyTicketActivity.this, R.layout.spinner_view, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    origin.setAdapter(dataAdapter);
                    dest.setAdapter(dataAdapter);

                    restoreSelections();
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        task.execute((Void) null);
    }

    protected void forward() {
        // Finish error checking
        if(!errorProne()) {
            Intent intent = new Intent(getApplicationContext(), TicketSuccessActivity.class);
            intent.putExtra("price", price.getText());
            startActivity(intent);
        }
    }

    protected void forwardTrain(StationModel org, StationModel ds) {
        Intent intent = new Intent(getApplicationContext(), TrainListActivity.class);
        intent.putExtra("origin", org.getId());
        intent.putExtra("destination", ds.getId());
        intent.putExtra("departure", departure.getText());
        intent.putExtra("date", (String) departure.getTag());
        startActivity(intent);
    }

    /**
     * This method is not specifically needed
     * since I could just finish the activity on the other side, so
     * all contents are still saved. However, for other purposes, here it is.
     */
    private void restoreSelections() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int originText = extras.getInt("origin", -1);
            int destText = extras.getInt("destination", -1);
            String date = extras.getString("date");
            int train = extras.getInt("train_id", -1);
            String description = extras.getString("train_desc");

            try {
                // Restore origin and destination
                for (Spinner spin : Arrays.asList(this.origin, this.dest)) {
                    for (int i = 0; i < spin.getAdapter().getCount(); i++) {
                        StationModel model = (StationModel) spin.getItemAtPosition(i);
                        if (model.getId() == (spin == this.origin ? originText : destText)) {
                            spin.setSelection(i);
                            break;
                        }
                    }
                }
                // Restore date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                calendar.setTime(sdf.parse(date));
                this.departure.setText(dateFormat.format(calendar.getTime()));
                this.departure.setTag(date);
                // Restore train
                this.train.setText(description);
                this.train.setTag(train);
            } catch (Exception ignored) { // do not restore anything

            }
        }
    }

    private void updateLabel(Calendar calendar) {
        String myFormat = "MMM dd, yyyy";
        String apiFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        String newDate = sdf.format(calendar.getTime());

        if(!departure.getText().equals(newDate)) {
            train.setText(getString(R.string.display_select_train));
        }
        departure.setText(newDate);
        departure.setTag(new SimpleDateFormat(apiFormat, Locale.US).format(calendar.getTime()));
    }

    private boolean errorProne() {
        TextView errorView = null;
        String error = null;

        // Error checking
        if(origin.getSelectedItem() == null || dest.getSelectedItem() == null) {
            errorView = (TextView) dest.getSelectedView();
            error = getString(R.string.error_field_required);
        }
        else if (origin.getSelectedItem().toString().equals(
                dest.getSelectedItem().toString())) {
            errorView = (TextView) dest.getSelectedView();
            error = getString(R.string.error_same_stations);
        }
        else if(departure.getTag() == null) {
            errorView = departure;
            error = getString(R.string.error_field_required);
        }
        else {
            try {
                DateDeserializer parser = new DateDeserializer();
                parser.deserialize((String) departure.getTag());
            }
            catch(JsonParseException | ClassCastException e) {
                errorView = departure;
                error = getString(R.string.error_invalid_date);
            }
        }
        if(error != null) {
            errorView.setError("");
            Snackbar.make(errorView, error,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return true;
        }
        else {
            ((TextView) origin.getSelectedView()).setError(null);
            ((TextView) dest.getSelectedView()).setError(null);
            departure.setError(null);
            return false;
        }
    }
}
