package feup.cm.traintickets.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.runnables.TicketBuyTask;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.TicketGetPriceTask;
import feup.cm.traintickets.util.DateDeserializer;

public class BuyTicketActivity extends BaseActivity {

    /*
    TODO: Changing stations, also resets train
     */

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.UK);

    private Spinner origin;
    private Spinner dest;
    private Button forward;

    private TextView departureView;
    private TextView trainView;
    private TextView titleView;
    private TextView priceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        origin = (Spinner) findViewById(R.id.origin_station);
        dest = (Spinner) findViewById(R.id.destination_station);
        titleView = (TextView) findViewById(R.id.title_left);
        departureView = (TextView) findViewById(R.id.departure_desc);
        trainView = (TextView) findViewById(R.id.train_desc);
        priceView = (TextView) findViewById(R.id.ticket_price_value);
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
                titleView.setText(parent.getItemAtPosition(position).toString());
                if(!first) {
                    trainView.setText(getString(R.string.display_select_train));
                    trainView.setTag(null);
                }
                else first = false;
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
                    trainView.setText(getString(R.string.display_select_train));
                    trainView.setTag(null);
                }
                else first = false;
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
        departureView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BuyTicketActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
        // Open trainView list on click input
        trainView.setOnClickListener(new View.OnClickListener() {

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
                    titleView.setText(list.size() > 0 ? list.get(0).toString() :
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
            forward.setText(String.format("%s...", getString(R.string.display_loading)));

            try {
                // final error pruning
                if(trainView.getTag() == null) {
                    trainView.setError("");
                    Snackbar.make(trainView, getString(R.string.display_select_train),
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(!(priceView.getTag() instanceof Float) ||
                        ((Float) priceView.getTag()) == 0f) {
                    priceView.setError("");
                    Snackbar.make(trainView, getString(R.string.error_invalid_price),
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                StationModel org = (StationModel) origin.getSelectedItem();
                StationModel ds = (StationModel) dest.getSelectedItem();
                String dateStr = (String) departureView.getTag();
                int train = (Integer) trainView.getTag();
                double price = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                TicketBuyTask task = new TicketBuyTask(getToken(), getUserId(), org, ds,
                        sdf.parse(dateStr), price, train) {
                    @Override
                    protected void onPostExecute(Boolean success) {
                        if(success) {
                            Intent intent = new Intent(getApplicationContext(), TicketSuccessActivity.class);
                            if(priceView.getTag() instanceof Float) {
                                intent.putExtra("price", ((Float) priceView.getTag()));
                            }
                            startActivity(intent);
                        }
                        else forwardError();
                    }

                    @Override
                    protected void onCancelled() {
                        forwardError();
                    }
                };
                task.execute((Void) null);
            }
            catch(Exception e) {
                forwardError();
            }
        }
    }

    protected void forwardError() {
        Intent intent = new Intent(getApplicationContext(), TicketFailureActivity.class);
        if(priceView.getTag() instanceof Float) {
            intent.putExtra("price", ((Float) priceView.getTag()));
        }
        startActivity(intent);
    }

    protected void forwardTrain(StationModel org, StationModel ds) {
        Intent intent = new Intent(getApplicationContext(), TrainListActivity.class);
        intent.putExtra("origin", org.getId());
        intent.putExtra("destination", ds.getId());
        intent.putExtra("departureView", departureView.getText());
        intent.putExtra("date", (String) departureView.getTag());
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
                this.departureView.setText(dateFormat.format(calendar.getTime()));
                this.departureView.setTag(date);
                // Restore trainView
                this.trainView.setText(description);
                this.trainView.setTag(train);
                // Calculate price if applicable
                calculatePrice();
            } catch (Exception ignored) { // do not restore anything

            }
        }
    }

    protected void calculatePrice() {
        try {
            StationModel org = (StationModel) origin.getSelectedItem();
            StationModel ds = (StationModel) dest.getSelectedItem();
            int train = (Integer) trainView.getTag();

            TicketGetPriceTask task = new TicketGetPriceTask(getToken(), train,
                    org.getStationNumber(), ds.getStationNumber()) {
                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        TicketModel model = getResult();
                        priceView.setText(String.format("$%s", model.getPrice()));
                        priceView.setTag(model.getPrice());
                    }
                }

                @Override
                protected void onCancelled() {

                }
            };
            task.execute((Void) null);
        }
        catch(Exception ignored) {
            // Failed to generate price
        }
    }

    private void updateLabel(Calendar calendar) {
        String myFormat = "MMM dd, yyyy";
        String apiFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        String newDate = sdf.format(calendar.getTime());

        if(!departureView.getText().equals(newDate)) {
            trainView.setText(getString(R.string.display_select_train));
        }
        departureView.setText(newDate);
        departureView.setTag(new SimpleDateFormat(apiFormat, Locale.US).format(calendar.getTime()));
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
        else if(departureView.getTag() == null) {
            errorView = departureView;
            error = getString(R.string.error_field_required);
        }
        else {
            try {
                DateDeserializer parser = new DateDeserializer();
                parser.deserialize((String) departureView.getTag());
            }
            catch(JsonParseException | ClassCastException e) {
                errorView = departureView;
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
            departureView.setError(null);
            priceView.setError(null);
            return false;
        }
    }
}
