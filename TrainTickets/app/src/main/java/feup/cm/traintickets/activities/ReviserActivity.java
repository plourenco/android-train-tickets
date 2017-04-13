package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;

public class ReviserActivity extends AppCompatActivity {

    Spinner station;
    Spinner direction;
    Spinner trip;

    Button dlTicket;

    String stationToSend;
    String directionToSend;
    String tripToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviser);

        station = (Spinner)findViewById(R.id.spStation);
        direction = (Spinner)findViewById(R.id.spDirection);
        trip = (Spinner)findViewById(R.id.spTrip);
        dlTicket = (Button)findViewById(R.id.dlTickets);

        dlTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadTickets();
            }
        });

        /**
         * Hardcoded values
         * TODO populate with data managers
         */
        final List<String> stations = new ArrayList<>();
        stations.add("Porto");
        stations.add("Pocinha");

        List<String> directions = new ArrayList<>();
        directions.add("Positive");
        directions.add("Negative");

        List<String> trips = new ArrayList<>();
        trips.add("Morning");
        trips.add("Noon");
        trips.add("Afternoon");
        trips.add("Night");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ReviserActivity.this, R.layout.spinner_view, stations);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        station.setAdapter(dataAdapter);
        station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stationToSend = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stationToSend = parent.getSelectedItem().toString();
            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(ReviserActivity.this, R.layout.spinner_view, directions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        direction.setAdapter(dataAdapter1);
        direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                directionToSend = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                directionToSend = parent.getSelectedItem().toString();
            }
        });

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(ReviserActivity.this, R.layout.spinner_view, trips);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trip.setAdapter(dataAdapter2);
        trip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripToSend = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tripToSend = parent.getSelectedItem().toString();
            }
        });

    }

    private void downloadTickets() {
        //TODO Call async task to do this.
    }
}
