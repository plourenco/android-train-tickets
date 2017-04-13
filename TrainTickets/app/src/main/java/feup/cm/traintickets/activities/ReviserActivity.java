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

import feup.cm.traintickets.R;

public class ReviserActivity extends AppCompatActivity {

    Spinner direction;
    Spinner trip;

    Button dlTicket;

    String directionToSend;
    String tripToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviser);

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
         * TODO DATE
         */
        List<String> directions = new ArrayList<>();
        directions.add("Porto");
        directions.add("Pocinha");

        List<String> trips = new ArrayList<>();
        trips.add("Morning");
        trips.add("Noon");
        trips.add("Afternoon");
        trips.add("Night");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(ReviserActivity.this, R.layout.spinner_view, directions);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
