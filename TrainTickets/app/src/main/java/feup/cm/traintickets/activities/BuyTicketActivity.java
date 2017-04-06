package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.R;

public class BuyTicketActivity extends AppCompatActivity {

    private Spinner origin;
    private Spinner dest;

    private AppBarLayout appBar;
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
        appBar = (AppBarLayout) findViewById(R.id.app_bar);

        final TextView title = (TextView) findViewById(R.id.toolbar_title);
        final CharSequence titleText = title.getText();
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                boolean toolbarCollapsed = Math.abs(offset) >= appBarLayout.getTotalScrollRange();
                title.setText(toolbarCollapsed ? titleText : "");
            }
        });

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
