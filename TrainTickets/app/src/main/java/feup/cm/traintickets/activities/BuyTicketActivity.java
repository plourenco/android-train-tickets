package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.R;

public class BuyTicketActivity extends AppCompatActivity {

    private Spinner origin;
    private Spinner dest;

    private ListView trains;

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
        //trains = (ListView) findViewById(R.id.train_list);
        loadStations();
        //loadTrains("Pocinha", "Ribeira");
    }

    protected void loadStations() {
        List<String> list = new ArrayList<String>();
        list.add("Pocinha");
        list.add("Ribeira");
        list.add("Bairro Alto");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_view, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        origin.setAdapter(dataAdapter);
        dest.setAdapter(dataAdapter);
    }

    protected void loadTrains(String origin, String destination) {
        List<String> list = new ArrayList<String>();
        list.add("Pocinha");
        list.add("Ribeira");
        list.add("Bairro Alto");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.list_view, list);
        trains.setAdapter(dataAdapter);
    }
}
