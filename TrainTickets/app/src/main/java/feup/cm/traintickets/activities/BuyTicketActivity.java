package feup.cm.traintickets.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kogitune.activity_transition.ActivityTransition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.runnables.StationGetTask;

public class BuyTicketActivity extends BaseActivity {

    private Spinner origin;
    private Spinner dest;

    private EditText departure;
    private DatePicker depatureDate;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        bottomNav.setSelectedItemId(R.id.action_buyticket);

        origin = (Spinner) findViewById(R.id.origin_station);
        dest = (Spinner) findViewById(R.id.destination_station);
        title = (TextView) findViewById(R.id.title_left);
        departure = (EditText) findViewById(R.id.departure_desc);

        // Set forward button behaviour
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(origin.getSelectedItem().toString()
                        .equals(dest.getSelectedItem().toString())) {
                    TextView errorText = (TextView) dest.getSelectedView();
                    errorText.setError("");
                    Snackbar.make(view, getString(R.string.error_same_stations),
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), TrainListActivity.class);
                startActivity(intent);
            }
        });

        // Set origin change listener
        AdapterView.OnItemSelectedListener sLis = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        origin.setOnItemSelectedListener(sLis);

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

        loadStations();
    }

    protected void loadStations() {
        final List<String> list = new ArrayList<String>();
        StationGetTask task = new StationGetTask(getToken()) {

            @Override
            protected void onPostExecute(Boolean success) {
                if(success) {
                    for(StationModel station : getStations()) {
                        list.add(station.getStationName());
                    }
                    title.setText(list.size() > 0 ? list.get(0) :
                            getString(R.string.error_select_station));
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BuyTicketActivity.this,
                            R.layout.spinner_view, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    origin.setAdapter(dataAdapter);
                    dest.setAdapter(dataAdapter);
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        task.execute((Void) null);
    }

    private void updateLabel(Calendar calendar) {

        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        departure.setText(sdf.format(calendar.getTime()));
    }
}
