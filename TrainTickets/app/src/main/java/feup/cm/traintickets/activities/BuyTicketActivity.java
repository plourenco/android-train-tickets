package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.runnables.StationGetTask;

public class BuyTicketActivity extends BaseActivity {

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
}
