package feup.cm.traintickets.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.BaseTitleActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.runnables.StationGetTask;

public class BuyTicketActivity extends BaseTitleActivity {

    private Spinner origin;
    private Spinner dest;

    private EditText departure;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        origin = (Spinner) findViewById(R.id.origin_station);
        dest = (Spinner) findViewById(R.id.destination_station);
        title = (TextView) findViewById(R.id.title_left);
        departure = (EditText) findViewById(R.id.departure_desc);

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
                new DatePickerDialog(BuyTicketActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
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
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        departure.setText(sdf.format(calendar.getTime()));
    }
}
